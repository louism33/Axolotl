package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.SearchSpecs.*;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.allocatePanicTime;
import static com.github.louism33.chesscore.MaterialHashUtil.makeMaterialHash;
import static com.github.louism33.chesscore.MaterialHashUtil.typeOfEndgame;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public final class ChessThreadBetter extends Thread {
    public static final int MASTER_THREAD = 0;

    private UCIEntry uciEntry = null;
    private int whichThread;
    private boolean masterThread = false;
    Chessboard board;
    private long startTime;

    ChessThreadBetter(int whichThread, Chessboard board, long startTime) {
        Assert.assertTrue(whichThread != MASTER_THREAD);
        this.whichThread = whichThread;
        this.board = board;
        this.startTime = startTime;
        this.setName("T" + whichThread);
    }

    ChessThreadBetter(UCIEntry uciEntry, Chessboard board, long startTime) {
        this.uciEntry = uciEntry;
        this.whichThread = MASTER_THREAD;
        this.masterThread = true;
        this.board = board;
        this.startTime = startTime;
        this.setName("MasterThread");
    }


    @Override
    public void run() {
        if (EngineSpecifications.DEBUG) {
            System.out.println("info string     start run of " + this.getName());
        }

        if (MASTER_DEBUG) {
            Assert.assertEquals(MaterialHashUtil.makeMaterialHash(board), board.materialHash);
            Assert.assertEquals(MaterialHashUtil.typeOfEndgame(board), board.typeOfGameIAmIn);
        }


        int depth = 0;
        int aspirationScore = 0;

        int alpha;
        int beta;
        int alphaAspirationAttempts = 0;
        int betaAspirationAttempts = 0;

        alpha = aspirationScore - ASPIRATION_WINDOWS[alphaAspirationAttempts];
        beta = aspirationScore + ASPIRATION_WINDOWS[betaAspirationAttempts];

        int score = 0;

        if (DEBUG) {
            System.out.println("info string starting main id loop for thread " + this.getName());
        }

        everything:
        while (depth < SearchSpecs.maxDepth && running) {

            depth++;

            if (MASTER_DEBUG) {
                Assert.assertEquals(makeMaterialHash(board), board.materialHash);
                Assert.assertEquals(typeOfEndgame(board), board.typeOfGameIAmIn);
            }

            if (!masterThread) {
                if (depth % skipLookup[whichThread] == 0) {
                    if (DEBUG) {
                        System.out.println("info string " + this.getName() + " will skip depth " + depth + " and go to depth " + (depth + skipBy[whichThread]));
                    }
                    depth += skipBy[whichThread];
                }
            }

            if (DEBUG) {
                System.out.println("info string " + this.getName() + " is at depth " + depth);
            }

            int previousAi = rootMoves[whichThread][0] & MOVE_MASK_WITHOUT_CHECK;
            int previousAiScore = 0;

            if (depth == SearchSpecs.maxDepth) {
                nonTerminalTime = System.currentTimeMillis() - startTime;
                terminal = true;
            }

            if (MASTER_DEBUG) {
                Assert.assertEquals(makeMaterialHash(board), board.materialHash);
                Assert.assertEquals(typeOfEndgame(board), board.typeOfGameIAmIn);
                Assert.assertEquals(board.zobristPawnHash, cloneBoard.zobristPawnHash);
                Assert.assertEquals(board.zobristHash, cloneBoard.zobristHash);
            }

            while (running) {

                if (MASTER_DEBUG) {
                    Assert.assertEquals(makeMaterialHash(board), board.materialHash);
                    Assert.assertEquals(typeOfEndgame(board), board.typeOfGameIAmIn);
                    Assert.assertEquals(board.zobristPawnHash, cloneBoard.zobristPawnHash);
                    Assert.assertEquals(board.zobristHash, cloneBoard.zobristHash);
                }

                score = principleVariationSearch(board, depth, 0,
                        alpha, beta, 0, whichThread);   

                if (masterThread && (manageTime && !weHavePanicked)
                        && (depth >= 6 && aiMoveScore < previousAiScore - PANIC_SCORE_DELTA)) { // todo thread specific or not?
                    timeLimitMillis = allocatePanicTime(timeLimitMillis, absoluteMaxTimeLimit);
                    weHavePanicked = true;
                }

                previousAiScore = aiMoveScore;

                if  (!running || stopSearch(startTime, timeLimitMillis)) {
                    break everything;
                }

                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) { // todo, make only main thread?, or remove?
//                    System.out.println(this.getName() + " breaking, mate found with score " + score);
                    break everything;
                }

                aspTotal++;
                if (score <= alpha) {
                    aspFailA++;
                    alphaAspirationAttempts++;
                    if (alphaAspirationAttempts + 1 >= ASPIRATION_MAX_TRIES) {
                        alpha = SHORT_MINIMUM;
                    } else {
                        alpha = aspirationScore - ASPIRATION_WINDOWS[alphaAspirationAttempts];
                    }
                } else if (score >= beta) {
                    aspFailB++;
                    betaAspirationAttempts++;
                    if (betaAspirationAttempts + 1 >= ASPIRATION_MAX_TRIES) {
                        beta = SHORT_MAXIMUM;
                    } else {
                        beta = aspirationScore - ASPIRATION_WINDOWS[betaAspirationAttempts];
                    }
                } else {
                    aspSuccess++;
                    break;
                }
            }

            long time = System.currentTimeMillis() - startTime;
            if ((time > 500 || depth > 5) && PRINT_PV && masterThread) {
                uciEntry.send(board, aiMoveScore, depth, depth, time);
            }

            aspirationScore = score;
        }

        if (depth == SearchSpecs.maxDepth) {
            terminalTime = System.currentTimeMillis() - startTime;
        }

        if (PRINT_PV && masterThread) {
            long time = System.currentTimeMillis() - startTime;
            uciEntry.send(board, aiMoveScore, depth, depth, time);
        }

        if (EngineSpecifications.DEBUG) {
            System.out.println("info string     stop run of " + this.getName());
        }

        threadsNumber.decrementAndGet();
    }

}
