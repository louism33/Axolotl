package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.helper.timemanagement.TimeAllocator;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.moveordering.MoveOrderingConstants;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.helper.timemanagement.TimeAllocator.allocateTime;
import static com.github.louism33.axolotl.helper.timemanagement.TimeAllocator.outOfTime;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.updateKillerMoves;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.updateMateKillerMoves;
import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_DEPTH;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;

public class Engine {

    private static int aiMove;
    private static int aiMoveScore;
    private static boolean isReady = false;
    private static boolean stopInstruction = false;
    public static long nps;
    public static long regularMovesMade;
    public static long quiescentMovesMade;
    private static long startTime = 0;
    private static UCIEntry uciEntry;

    public static int getAiMove() {
        return aiMove;
    }

    private static void setAiMove(int aiMove) {
        Engine.aiMove = aiMove;
    }

    private static boolean isStopInstruction() {
        return stopInstruction;
    }

    public static void setStopInstruction(boolean instruction) {
        stopInstruction = instruction;
    }

    public static int getAiMoveScore() {
        return aiMoveScore;
    }

    public static void calculateNPS(){
        long time = System.currentTimeMillis() - startTime;
        if (time < 1000){
            nps = 0;
        }
        else {
            nps = ((1000 * (regularMovesMade + quiescentMovesMade)) / time);
        }
    }

    public static long getNps() {
        calculateNPS();
        return nps;
    }

    public static UCIEntry getUciEntry() {
        return uciEntry;
    }

    public static void setUciEntry(UCIEntry uciEntry) {
        Engine.uciEntry = uciEntry;
    }

    private static boolean stopSearch(long startTime, long timeLimiMillis, int depth, int maxDepth) {
        return Engine.isStopInstruction()
                || (EngineSpecifications.ALLOW_TIME_LIMIT && outOfTime(startTime, timeLimiMillis))
                || (!EngineSpecifications.ALLOW_TIME_LIMIT && (depth >= maxDepth));
    }

    public static void setup() {
        TranspositionTable.initTable(EngineSpecifications.DEFAULT_TABLE_SIZE);
        stopInstruction = false;
        isReady = true;
        reset();
    }

    private static void reset() {
        nps = 0;
        regularMovesMade = 0;
        stopInstruction = false;
        regularMovesMade = 0;
        quiescentMovesMade = 0;
        aiMove = 0;
        aiMoveScore = SHORT_MINIMUM;
        TranspositionTable.reset();
    }

    public static int searchFixedDepth(Chessboard board, int depth) {
        EngineSpecifications.ALLOW_TIME_LIMIT = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0);
    }

    public static int searchMyTime(Chessboard board, long maxTime) {
        EngineSpecifications.ALLOW_TIME_LIMIT = true;

        if (maxTime < 1000) {
            return searchFixedDepth(board, 1);
        }
        if (maxTime < 5000) {
            return searchFixedDepth(board, 2);
        }
        long timeLimit = allocateTime(board, maxTime);

        return searchFixedTime(board, timeLimit);
    }

    public static int searchFixedTime(Chessboard board, long maxTime) {
        if (!isReady) {
            setup();
        }

        reset();

        startTime = System.currentTimeMillis();

        try {
            iterativeDeepeningWithAspirationWindows(board, startTime, maxTime);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        if (time != 0) {
            calculateNPS();
        }

        TimeAllocator.printManager(board, true);

        return aiMove & MoveOrderer.MOVE_MASK;
    }

    private static void iterativeDeepeningWithAspirationWindows(Chessboard board, long startTime, long timeLimitMillis) throws IllegalUnmakeException {
        int depth = 0;

        while (!stopSearch(startTime, timeLimitMillis, depth, MAX_DEPTH)) {
            depth++;

            int score;

            score = principleVariationSearch(board,
                    depth, 0, SHORT_MINIMUM, SHORT_MAXIMUM, 0);

            TimeAllocator.printManager(board, true);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                break;
            }
        }
    }

    private static int principleVariationSearch(Chessboard board,
                                                int depth, int ply,
                                                int alpha, int beta,
                                                int nullMoveCounter) throws IllegalUnmakeException {

        int originalAlpha = alpha;
        int[] moves = board.generateLegalMoves();
        boolean boardInCheck = board.inCheckRecorder;

        depth += extensions(board, ply, boardInCheck, moves);

        Assert.assertTrue(depth >= 0);

        if (depth <= 0){
            Assert.assertTrue(!board.inCheck(board.isWhiteTurn()));
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }

        alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
        beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
        if (alpha >= beta){
            return alpha;
        }

        int hashMove = 0;
        int score;

        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        if (!thisIsAPrincipleVariationNode && !boardInCheck) {

            int R = nullMoveDepthReduction(depth);

            if (isNullMoveOkHere(board, nullMoveCounter, depth, R)){

                Chessboard initial = new Chessboard(board);

                board.makeNullMoveAndFlipTurn();

                int nullScore = -principleVariationSearch(board,
                        depth - R - 1, ply + 1,
                        -beta, -beta + 1, nullMoveCounter + 1);

                board.unMakeNullMoveAndFlipTurn();

                Assert.assertEquals(initial, board);

                if (nullScore >= beta){
                    if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                        nullScore = beta;
                    }
                    return nullScore;
                }
            }
        }

        MoveOrderer.scoreMoves(moves, board, board.isWhiteTurn(), ply, hashMove);

        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;
        int realMoves = MoveParser.numberOfRealMoves(moves);
        Ints.sortDescending(moves, 0, realMoves);
        int numberOfMovesSearched = 0;

        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0) {
                break;
            }

            Assert.assertTrue(moves[i] > MoveOrderingConstants.MOVE_SIZE_LIMIT);

            if (i == 0) {
                Assert.assertTrue(moves[i] >= moves[i + 1]);
            } else {
                Assert.assertTrue(moves[i] <= moves[i - 1]);
                Assert.assertTrue(moves[i] >= moves[i + 1]);
            }

            int move = moves[i] & MoveOrderer.MOVE_MASK;
            int moveScore = MoveOrderer.getMoveScore(moves[i]);

            boolean captureMove = MoveParser.isCaptureMove(move);
            boolean promotionMove = MoveParser.isPromotionMove(move);
            boolean givesCheckMove = MoveOrderer.checkingMove(board, move);
            boolean pawnToSix = MoveParser.moveIsPawnPushSix(move);
            boolean pawnToSeven = MoveParser.moveIsPawnPushSeven(move);

            board.makeMoveAndFlipTurn(move);
            regularMovesMade++;
            numberOfMovesSearched++;

            if (board.drawByRepetition(board.isWhiteTurn())) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = alpha + 1;

                int R = lateMoveDepthReduction(depth);

                if (numberOfMovesSearched > 3
                        && depth > R && !captureMove && !promotionMove
                        && !pawnToSeven && !boardInCheck && !givesCheckMove) {

                    score = -principleVariationSearch(board,
                            depth - R - 1, ply + 1,
                            -alpha - 1, -alpha, nullMoveCounter);
                }

                if (numberOfMovesSearched > 1 && score > alpha) {
                    score = -principleVariationSearch(board,
                            depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0);
                }

                if (score > alpha) {
                    score = -principleVariationSearch(board,
                            depth - 1, ply + 1,
                            -beta, -alpha, 0);
                }
            }

            board.unMakeMoveAndFlipTurn();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move & MoveOrderer.MOVE_MASK;
                alpha = Math.max(alpha, score);
                
                if (ply == 0) {
                    setAiMove(bestMove);
                    aiMoveScore = bestScore;
                }
            }

            if (alpha >= beta) {
                if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    updateMateKillerMoves(move, ply);
                } else {
                    updateKillerMoves(move, ply);
                }
                MoveOrderer.updateHistoryMoves(move, ply);
                break;
            }
        }

        if (numberOfMovesSearched == 0) {
            if (board.inCheck(board.isWhiteTurn())) {
                return IN_CHECKMATE_SCORE + ply;
            }
            else {
                return IN_STALEMATE_SCORE;
            }
        }

        int flag;
        if (bestScore <= originalAlpha){
            flag = UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = LOWERBOUND;
        } else {
            flag = EXACT;
        }

        TranspositionTable.addToTable(board.getZobrist(),
                TranspositionTable.buildTableEntry(bestMove & MoveOrderer.MOVE_MASK, bestScore, depth, flag, ply));

        return bestScore;
    }
}