package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.*;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static challenges.Utils.contains;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.KPK.generateKPKBitbase;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.*;
import static com.github.louism33.axolotl.search.Quiescence.quiescenceSearch;
import static com.github.louism33.axolotl.search.SearchSpecs.manageTime;
import static com.github.louism33.axolotl.search.SearchSpecs.timeLimitMillis;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.outOfTime;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.BoardConstants.BLACK_PAWN;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.MaterialHashUtil.makeMaterialHash;
import static com.github.louism33.chesscore.MaterialHashUtil.typeOfEndgame;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;
import static com.github.louism33.chesscore.MoveParser.*;

@SuppressWarnings("ALL")
public final class Engine {

    public UCIEntry uciEntry;
    static Chessboard cloneBoard;
    public static final AtomicInteger threadsNumber = new AtomicInteger(0);
    static Chessboard[] boards;
    static Chessboard board;
    public static int[][] rootMoves;
    public static boolean inCheckAtRoot = false;
    public static boolean knownCheckStateRoot = false;

    private static long startTime = 0;
    public static boolean running = false;
    public static boolean quitOnSingleMove = true;// todo consider replacing booleans with int(as enum)
    public static boolean computeMoves = true;

    public static int aiMoveScore;
    public static boolean weHavePanicked = false;

    public static long nps;
    public static long totalMovesMade;
    public static long[] numberOfMovesMade;
    static long[] numberOfQMovesMade;

    public static int age = 0;

    private static final int
            PHASE_HASH = 0,
            PHASE_GEN_CAPTURE_MOVES = 1,
            PHASE_GEN_MOVES = 2,
            PHASE_SCORE_MOVES = 3,
            PHASE_PROMOTION_MOVES = 4,
            PHASE_GOOD_CAP_MOVES = 5,
            PHASE_EVEN_CAP_MOVES = 6,
            PHASE_MATE_KILLER = 7,
            PHASE_KILLER_ONE = 8,
            PHASE_KILLER_TWO = 9,
            PHASE_SORT_QUIET_MOVES = 10,
            PHASE_QUIET_MOVES = 11,
            PHASE_BAD_CAP_MOVES = 12,
            PHASE_UNDERPROMOTIONS = 13,
            PHASE_REST_OF_MOVES_ROOT = 14;

    public Engine() {
        this.uciEntry = new UCIEntry(this);
    }

    // chess22k / ethereal reduction idea and numbers
    public final static int[][] reductions = new int[64][64];

    static {
        for (int depth = 1; depth < 64; depth++) {
            for (int moveNumber = 1; moveNumber < 64; moveNumber++) {
                reductions[depth][moveNumber] = (int) (0.5f + Math.log(depth) * Math.log(moveNumber * 1.2f) / 2.5f);
            }
        }
    }

    public static final void resetFull() { // todo, should this reset uci options?
        if (DEBUG) {
            System.out.println("info string Full resetting");
        }
        numberOfMovesMade = new long[NUMBER_OF_THREADS];
        numberOfQMovesMade = new long[NUMBER_OF_THREADS];
        rootMoves = new int[NUMBER_OF_THREADS][];
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            rootMoves[i] = new int[Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH];
        }
        resetBetweenMoves();
        age = 0;
        initTableMegaByte(TABLE_SIZE_MB, true);

        PawnTranspositionTable.initPawnTableDefault(true);

        SearchSpecs.maxDepth = ABSOLUTE_MAX_DEPTH;

        EvaluationConstants.setupEvalConst(false);

        EvaluatorPositionConstant.setupEvalPosConst(false);

        SEE.setupSEE(false);

        Evaluator.initEvaluator(false);

        PawnEval.initPawnEvaluator(false);
    }

    public static void resetBetweenMoves() { // todo
        if (DEBUG) {
            System.out.println("info string soft resetting");
        }

        knownCheckStateRoot = false;
        inCheckAtRoot = false;
        //don't reset moves if uci will provide them
        hashTableReturn = 0;
        final int length = rootMoves.length;

        Assert.assertEquals(length, NUMBER_OF_THREADS);

        if (numberOfMovesMade == null || numberOfMovesMade.length != NUMBER_OF_THREADS) {
            numberOfMovesMade = new long[NUMBER_OF_THREADS];
        }
        if (numberOfQMovesMade == null || numberOfQMovesMade.length != NUMBER_OF_THREADS) {
            numberOfQMovesMade = new long[NUMBER_OF_THREADS];
        }
        for (int i = 0; i < length; i++) {
            if (rootMoves[i] == null) {
                rootMoves[i] = new int[Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH];
            }

            numberOfMovesMade[i] = 0;
            numberOfQMovesMade[i] = 0;
            Arrays.fill(rootMoves[i], 0);
        }
        if (computeMoves) {
            rootMoves[MASTER_THREAD] = null; // todo
        }
        weHavePanicked = false;
        SearchSpecs.maxDepth = ABSOLUTE_MAX_DEPTH;
        nps = 0;
        aiMoveScore = SHORT_MINIMUM;

        running = false;
        resetMoveOrderer();
        age = (age + 1) % ageModulo;
    }

    public static void setThreads(int totalThreads) {

        Assert.assertEquals(0, threadsNumber.get());

        if (totalThreads > MAX_THREADS) {
            totalThreads = MAX_THREADS;
        } else if (totalThreads < 0) {
            totalThreads = DEFAULT_THREAD_NUMBER;
        }

        if (DEBUG) {
            System.out.println("info string setting threads from " + NUMBER_OF_THREADS + " to " + totalThreads);
        }

        NUMBER_OF_THREADS = totalThreads;

        rootMoves = new int[NUMBER_OF_THREADS][];
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            rootMoves[i] = new int[Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH];
        }
        numberOfMovesMade = new long[NUMBER_OF_THREADS];
        numberOfQMovesMade = new long[NUMBER_OF_THREADS];

        setupMoveOrderer(true);
        PawnTranspositionTable.initPawnTableDefault(true);
        SEE.setupSEE(true);
        Evaluator.initEvaluator(true);
        PawnEval.initPawnEvaluator(true);
    }

    public static int getAiMove() {
        return rootMoves[MASTER_THREAD][0] & MOVE_MASK_WITHOUT_CHECK;
    }

    static boolean stopSearch(long startTime, long timeLimitMillis) {
        return (SearchSpecs.allowTimeLimit && outOfTime(startTime, timeLimitMillis, manageTime));
    }

    // set threads first if you want more than one
    public int simpleSearch(Chessboard board) {
        Assert.assertEquals(0, threadsNumber.get());

        go(board);

        running = false;

        return getAiMove();
    }


    public void go(Chessboard board) {
        this.board = board;
        running = true;

        generateKPKBitbase();

        searchFixedTime(uciEntry, board);
    }

    private static final void searchFixedTime(UCIEntry uciEntry, Chessboard board) {
        startTime = System.currentTimeMillis();

        //UCI can provide root moves if doing searchmoves
        if (rootMoves[MASTER_THREAD] == null || computeMoves) {
            rootMoves[MASTER_THREAD] = board.generateLegalMoves();
            Assert.assertTrue(board.currentCheckStateKnown);
        }

        knownCheckStateRoot = true;
        inCheckAtRoot = board.inCheckRecorder;

        int numberOfRealMoves = rootMoves[MASTER_THREAD][rootMoves[MASTER_THREAD].length - 1];
        if (numberOfRealMoves == 0) {
            uciEntry.sendNoMove();
            running = false;
            return;
        }

        if (numberOfRealMoves == 1 && quitOnSingleMove) {
            Assert.assertTrue(Engine.threadsNumber.get() == 0);
            running = false;
            return;
        }

        scoreMovesAtRootNewFirst(rootMoves[MASTER_THREAD], numberOfRealMoves, Engine.board);

        Engine.board = board; // todo, move before move order?

        Assert.assertTrue(running);

        if (MASTER_DEBUG) {
            cloneBoard = new Chessboard(board);
        }

        if (boards == null || boards.length != NUMBER_OF_THREADS) {
            boards = new Chessboard[NUMBER_OF_THREADS];
        }

        if (NUMBER_OF_THREADS == 1) {
            ChessThread thread = new ChessThread(uciEntry, Engine.board, startTime);
            threadsNumber.incrementAndGet();
            thread.run();
            running = false;
        } else {
            final int totalMoves = rootMoves[MASTER_THREAD].length;
            for (int t = 1; t < NUMBER_OF_THREADS; t++) {
                // to avoid regenerating and rescoring root moves
                System.arraycopy(rootMoves[MASTER_THREAD], 0, rootMoves[t], 0, totalMoves);

                Assert.assertTrue(rootMoves[t][rootMoves[t].length - 1] == rootMoves[MASTER_THREAD][rootMoves[MASTER_THREAD].length - 1]);

                boards[t] = new Chessboard(board);
                ChessThread thread = new ChessThread(t, boards[t], startTime);
                threadsNumber.incrementAndGet();
                thread.start();
            }
            ChessThread masterThread = new ChessThread(uciEntry, Engine.board, startTime);
            threadsNumber.incrementAndGet();
            masterThread.run();
            running = false;


            while (threadsNumber.get() != 0) {
                if (threadsNumber.get() < 0) {
                    System.out.println("THREADS NUMBER BELOW ZERO! ");
                }

                Thread.yield();
            }
        }

        Assert.assertTrue("search fixed time ended, but some threads still running! " + threadsNumber.get(), threadsNumber.get() == 0);
    }


    public static void calculateNPS() {
        final long l = System.currentTimeMillis();
        long time = l - startTime;

        long total = 0;
        for (int i = 0; i < numberOfMovesMade.length; i++) {
            total += numberOfMovesMade[i] + numberOfQMovesMade[i];
        }
        if (time > 500) {
            nps = ((1000 * total) / time);
        }
        totalMovesMade = total;
        if (nps < 0) {
            throw new RuntimeException("nps below zero");
        }
    }


    // todo, testing features
    public static int nonTerminalNodes = 0;
    public static int terminalNodes = 0;
    public static long nonTerminalTime = 0;
    public static long terminalTime = 0;
    public static boolean terminal = false;

    public static int iidSuccess = 0, iidFail = 0, iidTotal = 0;
    public static int futilitySuccess = 0, futilityFail = 0, futilityTotal = 0;
    public static int nullSuccess = 0, nullFail = 0, nullTotal = 0;
    public static int betaSuccess = 0, betaFail = 0, betaTotal = 0;
    public static int alphaSuccess = 0, alphaFail = 0, alphaTotal = 0;

    public static int lmpTotal = 0;
    public static int aspSuccess = 0, aspFailA = 0, aspFailB = 0, aspTotal = 0;
    public static int quiescenceFutility = 0, quiescenceDelta = 0, quiescenceSEE = 0;

    public static final int[] indexOfCutoff = new int[128];
    public static final int[] phaseCutoff = new int[PHASE_REST_OF_MOVES_ROOT + 1];
    public static final int[] rootBestMoveIndex = new int[128];
    public static int numberOfAIMoveFlipsAfterSearch = 0;
    public static int numberOfAIMoveFlips = 0;
    public static int aiMoveConfirmedAsBest = 0;
    public static int newAiMove = 0;

    public static int hashCutoff = 0, mateKillerCutoff = 0, killerOneCutoff = 0;
    public static int killerTwoCutoff = 0, oldKillerScoreOneCutoff = 0, oldKillerScoreTwoCutoff = 0, otherCutoff = 0;
    public static int noMovesAndHash = 0, noMovesAndNoHash = 0;
    public static int yesMovesAndHash = 0, yesMovesAndNoHash = 0;
    public static int cutoffWithoutMoveGenPossible = 0;
    public static int cutoffWithoutMoveGen = 0;
    public static int cutoffWithMoveGen = 0;
    public static int cutoffWithoutMoveGenOTHER = 0;

    public static int hashTableReturn = 0;

    static int principleVariationSearch(Chessboard board,
                                        int depth, int ply,
                                        int alpha, int beta,
                                        int nullMoveCounter,
                                        int whichThread) {

        final int originalAlpha = alpha;
        final int turn = board.turn;

        Assert.assertTrue(alpha < beta);

        if (!running) { // todo keep here?
            return 0;
        }

        int[] moves = null;
        long checkers;
        // todo, consider using inCheck() instead of getCheckers(), as this can be shortcut by making a move we know is checking. 
        //todo  But then must recalc pinners later, so see if tradeoff worth it
        final boolean rootNode = ply == 0;
        final boolean firstIteration = rootNode && depth == 1;
        Assert.assertTrue(!firstIteration || (alpha == SHORT_MINIMUM && beta == SHORT_MAXIMUM));

        if (rootNode) {
            checkers = board.checkingPieces;
            moves = rootMoves[whichThread];
        } else {
            checkers = board.getCheckers();
        }

        Assert.assertTrue(board.currentCheckStateKnown);

        if (MASTER_DEBUG && rootNode) {
            Assert.assertEquals(rootMoves[whichThread], moves);
            Assert.assertEquals(board.zobristPawnHash, cloneBoard.zobristPawnHash);
            Assert.assertEquals(board.zobristHash, cloneBoard.zobristHash);
        }

        boolean inCheck = board.inCheckRecorder;

        if (ply + depth < MAX_DEPTH_HARD - 2) { // consider singular reply only depth 0
            depth += extensions(board, ply, inCheck, moves); // todo, move to parent node (due to singular reply extension)
        }

        final boolean trunkNode = !rootNode && depth != 0;
        final boolean leafNode = !rootNode && depth == 0;
        if (rootNode) {
            moves = rootMoves[whichThread];
        }

        Assert.assertTrue(depth >= 0);

        if (depth <= 0) {

            if (terminal) {
                terminalNodes++;
            } else {
                nonTerminalNodes++;
            }

            if (ply >= MAX_DEPTH_HARD) {
                Assert.assertTrue(moves != null); // todo, can we remove?
                return Evaluator.eval(board, whichThread);
            }

            Assert.assertEquals(0, depth);
            return quiescenceSearch(board, alpha, beta, whichThread, ply, depth);
        }

        alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
        beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
        if (alpha >= beta) {
            return alpha;
        }

        int hashMove = 0;
        int score;

        long previousTableData = retrieveFromTable(board.zobristHash);

        if (previousTableData != 0) {
            score = getScore(previousTableData, ply);
            hashMove = getMove(previousTableData);

            if (getDepth(previousTableData) >= depth) {
                int flag = getFlag(previousTableData);
                if (flag == EXACT) {
                    if (rootNode) {
                        putAIMoveFirst(hashMove, whichThread);
                        if (whichThread == 0) {
                            aiMoveScore = score;
                        }
                    }
                    hashTableReturn++;
                    return score;
                } else if (flag == LOWERBOUND) {
                    if (score >= beta) {
                        if (rootNode) {
                            putAIMoveFirst(hashMove, whichThread);
                            if (whichThread == 0) {
                                aiMoveScore = score;
                            }
                        }
                        hashTableReturn++;
                        return score;
                    }
                } else if (flag == UPPERBOUND) {
                    if (score <= alpha) {
                        if (rootNode) {
                            putAIMoveFirst(hashMove, whichThread);
                            if (whichThread == 0) {
                                aiMoveScore = score;
                            }
                        }
                        hashTableReturn++;
                        return score;
                    }
                }
            }
        }
        
        final boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        int staticBoardEval = SHORT_MINIMUM;

        if (!thisIsAPrincipleVariationNode && !inCheck) {
            Assert.assertTrue(depth > 0);

            staticBoardEval = Evaluator.eval(board, whichThread);

            if (isBetaRazoringOkHere(depth, staticBoardEval)) {
                betaTotal++;
                int specificBetaRazorMargin = betaRazorMargin[depth];
                if (staticBoardEval - specificBetaRazorMargin >= beta) {
                    betaSuccess++;
                    return staticBoardEval;
                }
                betaFail++;
            }

            int R = depth > 6 ? 3 : 2;
            if (isNullMoveOkHere(board, nullMoveCounter, depth, R)) {

                board.makeNullMoveAndFlipTurn();

                nullTotal++;

                int d = Math.max(depth - R - 1, 0);

                int nullScore = -principleVariationSearch(board,
                        d, ply + 1,
                        -beta, -beta + 1, nullMoveCounter + 1, whichThread);

                board.unMakeNullMoveAndFlipTurn();

                if (nullScore >= beta) {
                    if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                        nullScore = beta;
                    }
                    nullSuccess++;

                    return nullScore;
                }
                nullFail++;
            }
        }

        if (hashMove == 0 && depth >= iidDepth) {

            int d = thisIsAPrincipleVariationNode ? depth - 2 : depth >> 1;
            principleVariationSearch(board, d, ply + 1, alpha, beta, nullMoveCounter, whichThread); // todo, allow null move?
            hashMove = getMove(retrieveFromTable(board.zobristHash));
            if (hashMove == 0) {
                iidFail++;
            } else {
                iidSuccess++;
            }
            iidTotal++;
        }

        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;

        int PHASE_BACKEND = PHASE_HASH;

        if (rootNode) {
            if (hashMove != 0) {
                PHASE_BACKEND = PHASE_HASH;
            } else {
                PHASE_BACKEND = firstIteration ? PHASE_REST_OF_MOVES_ROOT : PHASE_SCORE_MOVES;
            }
        } else if (moves == null) {
            Assert.assertTrue(!rootNode);

            if (hashMove != 0) {
                noMovesAndHash++;
                Assert.assertTrue(PHASE_BACKEND == PHASE_HASH);
            } else {
                noMovesAndNoHash++;
                PHASE_BACKEND = PHASE_GEN_CAPTURE_MOVES;
            }

        } else {
            if (hashMove != 0) {
                yesMovesAndHash++;
                PHASE_BACKEND = PHASE_HASH;
            } else {
                yesMovesAndNoHash++;
                PHASE_BACKEND = PHASE_SCORE_MOVES;
            }
        }

        int lastMove = 2;
//        int lastMove = !rootNode && moves == null ? 2 : moves[moves.length - 1];

        int numberOfMovesSearched = 0;

        int moveScore = rootNode ? specialRootMoveScore : notALegalMoveScore;
        int i = 0;
        int move = -1;
        int debugLastMoveSearched = -1;
        int debugLastMoveScoreSearched = Integer.MAX_VALUE;
        int indexOfNextBestMove = -1;
        boolean hashAlreadyTried = false;
        int[] nextBestMoveIndexAndScore;


        boolean givesCheckMove = false;
        boolean moveCheckStateKnown = false;

        everything:
        while (i < lastMove) {
            nextBestMoveIndexAndScore = null;
            moveCheckStateKnown = false;

            switch (PHASE_BACKEND) {
                case PHASE_HASH:
                    if (hashAlreadyTried) {
                        PHASE_BACKEND++;
                        if (rootNode) {

                            Assert.assertTrue(moves != null);
                            lastMove = moves[moves.length - 1];
                            PHASE_BACKEND = firstIteration ? PHASE_REST_OF_MOVES_ROOT : PHASE_SCORE_MOVES;
                            continue;
                        }
                    } else {

                        Assert.assertTrue(!hashAlreadyTried);
                        Assert.assertTrue(hashMove != 0);

                        move = hashMove;
                        moveScore = rootNode ? specialRootMoveScore : hashScoreNew;

                        hashAlreadyTried = true;

                        if (MASTER_DEBUG) {
                            Chessboard clone = new Chessboard(board.toFenString());
                            final int[] cloneMoves = clone.generateLegalMoves();
                            Assert.assertTrue(contains(cloneMoves, hashMove));
                        }

                        break;
                    }

                    // currently not implemented
                case PHASE_GEN_CAPTURE_MOVES:
                    Assert.assertTrue(!rootNode);
                    PHASE_BACKEND++;


                case PHASE_GEN_MOVES:
                    Assert.assertTrue(!rootNode);
                    if (moves == null) {
                        Assert.assertTrue(!rootNode);
                        moves = board.generateLegalMoves(checkers);
                    }

                    if (numberOfRealMoves(moves) == 0) {
                        break everything;
                    }

                    PHASE_BACKEND++;


                case PHASE_SCORE_MOVES:
                    Assert.assertTrue(moves != null);
                    lastMove = moves[moves.length - 1];

                    if (!rootNode) {
                        scoreMovesNewWithoutQuiets(moves, ply, hashMove, whichThread);
                        if (hashAlreadyTried) {
                            MoveOrderer.invalidateHashMove(moves, hashMove, whichThread, ply);
                        }
                    }
                    PHASE_BACKEND++;

                    if (rootNode) {
                        MoveOrderer.scoreMovesAtRootNewInNode(moves, whichThread, lastMove);
                        PHASE_BACKEND = PHASE_REST_OF_MOVES_ROOT;
                        continue;
                    }


                case PHASE_PROMOTION_MOVES:
                    Assert.assertTrue(nextBestMoveIndexAndScore == null);
                    nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);
                    move = moves[nextBestMoveIndexAndScore[INDEX]];
                    moveScore = nextBestMoveIndexAndScore[SCORE];
                    final boolean promotionMove = isPromotionMove(move);
                    if (promotionMove && moveScore >= queenQuietPromotionScoreNew) {
                        Assert.assertTrue(isPromotionToQueen(move));
                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        Assert.assertTrue(!isPromotionToQueen(move));
                        PHASE_BACKEND++;
                    }


                case PHASE_GOOD_CAP_MOVES:
                    Assert.assertTrue(!rootNode);
                    Assert.assertTrue(moves != null);
                    Assert.assertEquals(moves[moves.length - 1], scores[whichThread][ply][scores[whichThread][ply].length - 1]);

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] <= evenCaptureScore);
                        Assert.assertTrue(moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];

                    }

                    if (moveScore > captureBaseScoreMVVLVA) {

                        Assert.assertTrue(moveScore != evenCaptureScore);
                        Assert.assertTrue((isCaptureMove(move) || isEnPassantMove(move)));

                        final int seeScore = SEE.getSEE(board, move, whichThread);

                        // by setting the move score to this lower number, the move will be searched later
                        if (seeScore < 0) {
                            MoveOrderer.setCaptureToLosingCapture(nextBestMoveIndexAndScore[INDEX], seeScore, whichThread, ply);

                            Assert.assertTrue(moveScore > seeScore);
                            Assert.assertTrue(getScoreOfMove(moves, move, whichThread, ply) < killerOneScoreNew);
                            Assert.assertTrue(getScoreOfMove(moves, move, whichThread, ply) < killerTwoScoreNew);
                            Assert.assertTrue(getScoreOfMove(moves, move, whichThread, ply) < captureBaseScoreMVVLVA);
                            continue;

                        } else if (seeScore == 0) {
                            MoveOrderer.setCaptureToEqualCapture(nextBestMoveIndexAndScore[INDEX], whichThread, ply);

                            Assert.assertTrue(moveScore > seeScore);
                            Assert.assertTrue(getScoreOfMove(moves, move, whichThread, ply) > mateKillerScoreNew);
                            Assert.assertTrue(getScoreOfMove(moves, move, whichThread, ply) == captureBaseScoreMVVLVA);
                            continue;
                        } else {
                            setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                            break;
                        }
                    } else {
                        PHASE_BACKEND++;
                    }

                    if (move == 0 || moveScore == alreadySearchedScore || moveScore == dontSearchMeScore) {
                        break everything;
                    }


                case PHASE_EVEN_CAP_MOVES:
                    Assert.assertTrue(!rootNode);

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < evenCaptureScore);
                        Assert.assertTrue(moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];
                    }

                    if (moveScore == evenCaptureScore) {
                        Assert.assertTrue(MoveParser.isCaptureMove(move) || MoveParser.isEnPassantMove(move));

                        Assert.assertTrue(!isPromotionMove(move));

                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        PHASE_BACKEND++;
                        Assert.assertTrue(moveScore < evenCaptureScore);
                    }

                    if (move == 0 || moveScore == alreadySearchedScore || moveScore == dontSearchMeScore || moveScore == notALegalMoveScore) {
                        break everything;
                    }


                case PHASE_MATE_KILLER:
                    Assert.assertTrue(!rootNode);
                    // todo, move verification instead of linear search

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < mateKillerScoreNew);
                        Assert.assertTrue(moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];
                    }

                    if (moveScore == mateKillerScoreNew) {
                        Assert.assertEquals(move, mateKillers[whichThread][ply]);
                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        PHASE_BACKEND++;
                    }


                case PHASE_KILLER_ONE:
                    Assert.assertTrue(!rootNode);

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < killerOneScoreNew);
                        Assert.assertTrue(moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];
                    }

                    Assert.assertTrue(moveScore <= killerOneScoreNew);

                    if (moveScore == killerOneScoreNew) {
                        Assert.assertEquals(move, killerMoves[whichThread][ply * 2]);
                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        PHASE_BACKEND++;
                    }

                case PHASE_KILLER_TWO:
                    Assert.assertTrue(!rootNode);

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < killerTwoScoreNew);
                        Assert.assertTrue(moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];
                    }

                    Assert.assertTrue(moveScore <= killerTwoScoreNew);

                    if (moveScore == killerTwoScoreNew) {
                        Assert.assertEquals(move, killerMoves[whichThread][ply * 2 + 1]);
                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        PHASE_BACKEND++;
                    }

                case PHASE_SORT_QUIET_MOVES:
                    Assert.assertTrue(!rootNode);
                    Assert.assertTrue(moves != null);

                    final boolean badCaptureMove = moveScore <= captureMaxScoreSEE && moveScore >= 0;
                    Assert.assertTrue(moveScore == unscoredQuietScore || badCaptureMove);

                    scoreQuietMoves(moves, board, ply, whichThread);

                    nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);
                    // we get a new move here, as the one we have till this point has not been scored
                    move = moves[nextBestMoveIndexAndScore[INDEX]];
                    moveScore = nextBestMoveIndexAndScore[SCORE];

                    if (isPromotionMove(move)) {
                        Assert.assertTrue(!isPromotionToQueen(move));
                    }

                    if (moveScore >= uninterestingMoveScoreNew) {
                        Assert.assertTrue(!isCaptureMove(move));
                        Assert.assertTrue(!isEnPassantMove(move));
                        Assert.assertTrue(!isPromotionMove(move));
                        final boolean unevenMoveScoreIndicatesGivesCheck = (moveScore & 1) != 0;

                        moveCheckStateKnown = true;
                        givesCheckMove = unevenMoveScoreIndicatesGivesCheck;

                        if (MASTER_DEBUG && moveCheckStateKnown) {
                            board.makeMoveAndFlipTurn(move);
                            board.getCheckers();
                            Assert.assertTrue(board.inCheckRecorder == givesCheckMove);
                            board.unMakeMoveAndFlipTurn();
                        }
                    }

                    PHASE_BACKEND++;


                case PHASE_QUIET_MOVES:
                    Assert.assertTrue(!rootNode);
                    Assert.assertTrue(moves != null);

                    Assert.assertEquals(moves[moves.length - 1], scores[whichThread][ply][scores[whichThread][ply].length - 1]);

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < captureMaxScoreSEE - 1000);
                        Assert.assertTrue(moveScore == unscoredQuietScore || moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];

                        Assert.assertTrue(moveScore < absoluteMaxQuietScore);
                    }

                    if (moveScore == alreadySearchedScore) {
                        break everything;
                    }

                    if (hashAlreadyTried && i == 0) {
                        Assert.assertTrue(moveScore == hashScoreNew);
                        Assert.assertTrue(move == hashMove || move == (hashMove & MOVE_MASK_WITHOUT_CHECK));
                        i++;
                        continue;
                    }

                    Assert.assertTrue(!rootNode);
                    if (moveScore >= uninterestingMoveScoreNew) {
                        Assert.assertTrue(!isCaptureMove(move));
                        Assert.assertTrue(!isEnPassantMove(move));
                        Assert.assertTrue(!isPromotionMove(move));
                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        PHASE_BACKEND++;
                    }
                    final boolean condition = isPromotionMove(move) || (((isCaptureMove(move) || isEnPassantMove(move)) && moveScore <= captureMaxScoreSEE));
                    Assert.assertTrue(condition);
                    Assert.assertTrue(!isPromotionToQueen(move));


                case PHASE_BAD_CAP_MOVES:

                    Assert.assertTrue(!rootNode);
                    Assert.assertTrue(moves != null);

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < killerTwoScoreNew);
                        Assert.assertTrue(moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];
                    }

                    Assert.assertTrue(moveScore <= captureMaxScoreSEE);

                    if ((isCaptureMove(move) || isEnPassantMove(move)) && !isPromotionMove(move)) {
                        Assert.assertTrue(moveScore >= captureMaxScoreSEE - 1000);
                        Assert.assertTrue(moveScore < captureMaxScoreSEE);
                        setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);
                        break;
                    } else {
                        PHASE_BACKEND++;
                    }
                    Assert.assertTrue(!isPromotionToQueen(move));


                case PHASE_UNDERPROMOTIONS:

                    Assert.assertTrue(!rootNode);
                    Assert.assertTrue(isPromotionMove(move));
                    Assert.assertTrue(!isPromotionToQueen(move));

                    if (nextBestMoveIndexAndScore == null) {
                        nextBestMoveIndexAndScore = getNextBestMoveIndexAndScoreNoChange(whichThread, ply);

                        Assert.assertTrue(move != moves[nextBestMoveIndexAndScore[INDEX]] || nextBestMoveIndexAndScore[SCORE] < captureMaxScoreSEE - 1000);
                        Assert.assertTrue(moveScore == unscoredQuietScore || moveScore == notALegalMoveScore || moveScore >= nextBestMoveIndexAndScore[SCORE]);

                        move = moves[nextBestMoveIndexAndScore[INDEX]];
                        moveScore = nextBestMoveIndexAndScore[SCORE];

                        Assert.assertTrue(moveScore <= knightPromotionScoreNew);
                    }

                    setMoveScoreToSearched(whichThread, ply, nextBestMoveIndexAndScore[INDEX]);

                    break;

                case PHASE_REST_OF_MOVES_ROOT:
                    Assert.assertTrue(moves != null);
                    Assert.assertTrue(rootNode);

                    Assert.assertTrue(move != (moves[i] & MOVE_MASK_WITHOUT_CHECK));
                    move = moves[i] & MOVE_MASK_WITHOUT_CHECK;

                    if (moveScore == alreadySearchedScore || move == 0) {
                        break everything;
                    }

            }

            Assert.assertTrue(move != debugLastMoveSearched);
            Assert.assertTrue(moveScore <= debugLastMoveScoreSearched);

            debugLastMoveSearched = move;
            debugLastMoveScoreSearched = moveScore;

            Assert.assertTrue(PHASE_BACKEND != PHASE_GEN_CAPTURE_MOVES);
            Assert.assertTrue(PHASE_BACKEND != PHASE_GEN_MOVES);
            Assert.assertTrue(PHASE_BACKEND != PHASE_SCORE_MOVES);

            Assert.assertTrue(rootNode ? PHASE_BACKEND <= PHASE_REST_OF_MOVES_ROOT : PHASE_BACKEND <= PHASE_UNDERPROMOTIONS);
            Assert.assertTrue(PHASE_BACKEND >= PHASE_HASH);
            Assert.assertTrue(move != -1);
            Assert.assertTrue(move != 0);
            Assert.assertTrue(moveScore != notALegalMoveScore);
            Assert.assertTrue(moveScore != unscoredQuietScore);
            Assert.assertTrue(moveScore != alreadySearchedScore);
            Assert.assertTrue(moveScore != dontSearchMeScore);

            if (PHASE_BACKEND == PHASE_HASH) {
                Assert.assertTrue(i == 0);
            } else if (!rootNode) { // currently in root any move that has once been hashMove will keep this score
                Assert.assertTrue(move != hashMove && move != (hashMove & MOVE_MASK_WITHOUT_CHECK));
                Assert.assertTrue(moveScore != hashScoreNew);
            }

            final boolean captureMove = isCaptureMove(move);

            final boolean promotionMove = isPromotionMove(move);
            final boolean queenPromotionMove = promotionMove ? isPromotionToQueen(move) : false;

            if (PHASE_BACKEND != PHASE_HASH) {
                Assert.assertTrue(!(MoveParser.isCheckingMove(moves[i])));
            }

            // in scoreQuietMoves() we already see if a move gives check, so no need to recalculate
            // we have not generated moves in PHASE_HASH, and so do not have up to date attack information, which we must therefore force recalculate
            givesCheckMove = moveCheckStateKnown
                    ? givesCheckMove
                    : board.moveGivesCheck(move, PHASE_BACKEND == PHASE_HASH);

            moveCheckStateKnown = true;

            if (MASTER_DEBUG) {
                board.makeMoveAndFlipTurn(move);
                board.getCheckers();
                Assert.assertTrue(board.inCheckRecorder == givesCheckMove);
                board.unMakeMoveAndFlipTurn();
            }

            final boolean pawnToSix = moveIsPawnPushSix(turn, move);
            final boolean pawnToSeven = moveIsPawnPushSeven(turn, move);
            final int movingPiece = getMovingPieceInt(move);
            final boolean enPassantMove = isEnPassantMove(move);
            final boolean quietMove = !(captureMove || promotionMove || enPassantMove);
            final boolean fiftyMoveBreaker = !(captureMove || promotionMove || movingPiece == WHITE_PAWN || movingPiece == BLACK_PAWN);

            Assert.assertTrue(!(PHASE_BACKEND == PHASE_GOOD_CAP_MOVES || PHASE_BACKEND == PHASE_EVEN_CAP_MOVES || PHASE_BACKEND == PHASE_BAD_CAP_MOVES) || (captureMove || enPassantMove));

            if (MASTER_DEBUG) {
                if (!rootNode && queenPromotionMove) {
                    boolean condition = moveScore >= queenQuietPromotionScoreNew;
                    Assert.assertTrue(condition);
                }
            }


            if (!thisIsAPrincipleVariationNode && PHASE_BACKEND == PHASE_QUIET_MOVES && !inCheck) {
                if (bestScore < CHECKMATE_ENEMY_SCORE_MAX_PLY
                        && notJustPawnsLeft(board)) {
                    if (!captureMove &&
                            !queenPromotionMove
                            && !givesCheckMove
                            && !pawnToSix
                            && !pawnToSeven
                            && depth <= 4
                            && numberOfMovesSearched >= depth * 3 + 3) {
                        lmpTotal++;
                        i++;
                        continue;
                    }
                }

                if (isFutilityPruningAllowedHere(depth,
                        queenPromotionMove, givesCheckMove, pawnToSix, pawnToSeven, numberOfMovesSearched)) {

                    futilityTotal++;

                    if (MASTER_DEBUG) {
                        Chessboard clone = new Chessboard(board);
                        final boolean b = board.moveGivesCheck(move);
                        board.makeMoveAndFlipTurn(move);
                        final boolean actual = board.getCheckers() != 0;
                        Assert.assertEquals(b, actual);

                        Assert.assertEquals(b, givesCheckMove);
                        Assert.assertEquals(givesCheckMove, actual);
                        Assert.assertEquals(givesCheckMove, board.inCheck());

                        board.unMakeMoveAndFlipTurn();
                    }

                    if (staticBoardEval == SHORT_MINIMUM) {
                        Assert.assertTrue(moves != null);
                        Assert.assertTrue(!givesCheckMove);
                        if (MASTER_DEBUG) {
                            Assert.assertTrue(!board.inCheck());
                        }
                        staticBoardEval = Evaluator.eval(board, whichThread);
                    }

                    Assert.assertTrue(depth > 0);

                    int futilityScore = staticBoardEval + futilityMargin[depth];

                    if (futilityScore <= alpha) {
                        futilitySuccess++;
                        if (futilityScore > bestScore) {
                            bestScore = futilityScore;
                        }
                        i++;
                        continue;
                    }
                    futilityFail++;
                }
            }

            if (MASTER_DEBUG) {
                Assert.assertEquals(typeOfEndgame(board), board.typeOfGameIAmIn);
                Assert.assertEquals(makeMaterialHash(board), board.materialHash);
            }


            Assert.assertTrue(move != 0);
            Assert.assertTrue(moveCheckStateKnown);

            rootCount[whichThread][i]++;

            Assert.assertTrue(PHASE_BACKEND != PHASE_HASH || move == hashMove);
            Assert.assertTrue(move != hashMove || PHASE_BACKEND == PHASE_HASH);
            Assert.assertTrue(!rootNode || PHASE_BACKEND == PHASE_HASH || move == rootMoves[whichThread][i]);

            board.makeMoveAndFlipTurn(move, givesCheckMove);

            numberOfMovesMade[whichThread]++;
            numberOfMovesSearched++;

            if (board.isDrawByInsufficientMaterial()
                    || (fiftyMoveBreaker &&
                    (board.isDrawByRepetition(1) || board.isDrawByFiftyMoveRule()))) {
                Assert.assertTrue(!enPassantMove);
                score = IN_STALEMATE_SCORE;
            } else {
                score = alpha + 1;

                if (numberOfMovesSearched > 1
                        && depth > 1 && quietMove
                        && !pawnToSeven && !inCheck && !givesCheckMove) {

                    int R = reductions[Math.min(depth, 63)][Math.min(numberOfMovesSearched, 63)];

                    if (!thisIsAPrincipleVariationNode) {
                        R++;
                    }
                    if (moveScore >= killerTwoScoreNew) {
                        R--;
                    }

                    int d = Math.max(depth - R - 1, 0);
                    score = -principleVariationSearch(board,
                            d, ply + 1,
                            -alpha - 1, -alpha, nullMoveCounter, whichThread);
                }

                if (numberOfMovesSearched > 1 && score > alpha) {
                    score = -principleVariationSearch(board,
                            depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0, whichThread);
                }

                if (score > alpha) {
                    score = -principleVariationSearch(board,
                            depth - 1, ply + 1,
                            -beta, -alpha, 0, whichThread);
                }
            }

            board.unMakeMoveAndFlipTurn();

            if (!running) {
                return 0;
            }

            if (whichThread == MASTER_THREAD && TimeAllocator.outOfTime(startTime, timeLimitMillis, manageTime)) {
                return 0;
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, score);

                if (rootNode) {
                    rootBestMoveIndex[i]++;
                    numberOfAIMoveFlipsAfterSearch++;

                    putAIMoveFirst(bestMove, whichThread);
                    if (whichThread == 0) {
                        aiMoveScore = score;
                    }
                }
            }

            if (alpha >= beta) {

                if (PHASE_BACKEND == PHASE_HASH) {
                    cutoffWithoutMoveGenPossible++;
                    if (moves == null) {
                        cutoffWithoutMoveGen++;
                    }
                } else {
                    cutoffWithMoveGen++;
                    if (moves != null) {
                        cutoffWithoutMoveGenOTHER++;
                    }
                }

                indexOfCutoff[i]++;
                if (move == hashMove) {
                    hashCutoff++;
                } else if (move == mateKillers[whichThread][ply]) {
                    mateKillerCutoff++;
                } else if (move == killerMoves[whichThread][ply * 2]) {
                    killerOneCutoff++;
                } else if (move == killerMoves[whichThread][ply * 2 + 1]) {
                    killerTwoCutoff++;
                } else if (ply >= 2 && move == killerMoves[whichThread][ply * 2 - 4]) {
                    oldKillerScoreOneCutoff++;
                } else if (ply >= 2 && move == killerMoves[whichThread][ply * 2 - 4 + 1]) {
                    oldKillerScoreTwoCutoff++;
                } else {
                    otherCutoff++;
                }

                phaseCutoff[PHASE_BACKEND]++;

                if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    updateMateKillerMoves(whichThread, move, ply);
                    break;
                }
                if (!captureMove && !isPromotionToQueen(move)) {
                    updateKillerMoves(whichThread, move, ply);
                    updateHistoryMoves(move, depth, turn, whichThread);
                }
                break;
            }
            i++;
        }

        if (numberOfMovesSearched == 0) {
            if (board.inCheckRecorder) {
                return IN_CHECKMATE_SCORE + ply;
            } else {
                return IN_STALEMATE_SCORE;
            }
        }

        Assert.assertTrue(bestMove != 0);

        int flag;
        if (bestScore <= originalAlpha) {
            flag = UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = LOWERBOUND;
        } else {
            flag = EXACT;
        }

        // todo an "incheck" flag for when not generating moves?
        if (running) {
            addToTableReplaceByDepth(board.zobristHash,
                    bestMove & MOVE_MASK_WITHOUT_CHECK, bestScore, depth, flag, ply, age);
        }

        return bestScore;
    }


    private static void putAIMoveFirst(int aiMove, int whichThread) {

        numberOfAIMoveFlips++;
        final int aiMoveMask = aiMove & MOVE_MASK_WITHOUT_CHECK;
        if ((rootMoves[whichThread][0] & MOVE_MASK_WITHOUT_CHECK) == aiMoveMask) { // todo, get rid of the need for these masks if possible
            aiMoveConfirmedAsBest++;
            return;
        }

        newAiMove++;
        final int maxMoves = rootMoves[whichThread][rootMoves[whichThread].length - 1];
        for (int i = 0; i < maxMoves; i++) {
            final int rootMove = rootMoves[whichThread][i] & MOVE_MASK_WITHOUT_CHECK;
            if (rootMove == aiMove || rootMove == aiMoveMask) {
                Assert.assertTrue(i != 0);

                System.arraycopy(rootMoves[whichThread], 0,
                        rootMoves[whichThread], 1, i);

                rootMoves[whichThread][0] = aiMoveMask;

                final int count = rootCount[whichThread][i];
                System.arraycopy(rootCount[whichThread], 0,
                        rootCount[whichThread], 1, i);

                rootCount[whichThread][0] = count;

                return;
            }
        }
        throw new RuntimeException("trying to put an aiMove first that is not in root moves ");
    }

}

 