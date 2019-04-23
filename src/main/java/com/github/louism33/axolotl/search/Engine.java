package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant;
import com.github.louism33.axolotl.evaluation.PawnTranspositionTable;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.*;
import static com.github.louism33.axolotl.search.QuiescenceBetter.quiescenceSearch;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;
import static com.github.louism33.chesscore.MoveConstants.MOVE_SCORE_MASK;
import static com.github.louism33.chesscore.MoveParser.*;

@SuppressWarnings("ALL")
public final class Engine {
    public static int aiMoveScore;
    public static long nps;
    public static volatile boolean stopNow = false;
    public static int[][] rootMoves = new int[NUMBER_OF_THREADS][];

    public static long[] numberOfMovesMade = new long[1];
    static long[] numberOfQMovesMade = new long[1];
    static boolean manageTime = true;
    static boolean weHavePanicked = false;

    private static long startTime = 0;
    private static boolean isReady = false;

    private static long timeLimitMillis;
    private static long absoluteMaxTimeLimit;

    public static boolean quitOnSingleMove = true;
    public static boolean computeMoves = true;

    public static int age = 0;

    public UCIEntry uciEntry = new UCIEntry(this);

    // chess22k / ethereal reduction idea and numbers
    public final static int[][] reductions = new int[64][64];

    static {
        for (int depth = 1; depth < 64; depth++) {
            for (int moveNumber = 1; moveNumber < 64; moveNumber++) {
                reductions[depth][moveNumber] = (int) (0.5f + Math.log(depth) * Math.log(moveNumber * 1.2f) / 2.5f);
            }
        }
    }

    public static void resetFull() {
        rootMoves = new int[NUMBER_OF_THREADS][];
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            rootMoves[i] = new int[Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH];
        }
        resetBetweenMoves();
        age = 0;
        initTable(TABLE_SIZE);
        PawnTranspositionTable.initPawnTable(PAWN_TABLE_SIZE);
        MAX_DEPTH = ABSOLUTE_MAX_DEPTH;
        if (!EvaluationConstants.ready) {
            EvaluationConstants.setup();
        }

        if (!EvaluatorPositionConstant.ready) {
            EvaluatorPositionConstant.setup();
        }
    }

    public static void resetBetweenMoves() { // todo
        //don't reset moves if uci will provide them
        final int length = rootMoves.length;
        Assert.assertEquals(length, NUMBER_OF_THREADS);
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
        MAX_DEPTH = ABSOLUTE_MAX_DEPTH;
        isReady = true;
        nps = 0;
        aiMoveScore = SHORT_MINIMUM;

        stopNow = false;
        resetMoveOrderer();
        age = (age + 1) % ageModulo;
    }

    public static void setThreads(int totalThreads) {
        if (totalThreads > MAX_THREADS) {
            totalThreads = MAX_THREADS;
        }
        else if (totalThreads < 0) {
            totalThreads = DEFAULT_THREAD_NUMBER;
        }

        if (DEBUG) {
            System.out.println("setting threads from " + NUMBER_OF_THREADS + " to " + totalThreads);
        }

        NUMBER_OF_THREADS = totalThreads;
        rootMoves = new int[NUMBER_OF_THREADS][];
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            rootMoves[i] = new int[Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH];
        }
        numberOfMovesMade = new long[NUMBER_OF_THREADS];
        numberOfQMovesMade = new long[NUMBER_OF_THREADS];

        setupMoveOrderer();
    }

    public static int getAiMove() {
        return rootMoves[MASTER_THREAD][0] & MOVE_MASK_WITHOUT_CHECK;
    }

    static boolean stopSearch(long startTime, long timeLimiMillis) {
        return (EngineSpecifications.ALLOW_TIME_LIMIT && outOfTime(startTime, timeLimiMillis, manageTime));
    }

    public static int searchMyTime(Chessboard chessboard, long maxMyTime, long maxEnemyTime, long increment, Integer movesToGo) {
        EngineSpecifications.ALLOW_TIME_LIMIT = true;
        manageTime = true;
        masterBoard = chessboard;
        timeLimitMillis = allocateTime(maxMyTime, maxEnemyTime, increment, movesToGo, masterBoard.fullMoveCounter);

        return searchFixedTime(new UCIEntry(), 0);
    }

    public static final int searchFixedDepth(Chessboard chessboard, int depth) {
        EngineSpecifications.ALLOW_TIME_LIMIT = false;
        masterBoard = chessboard;
        manageTime = false;
        timeLimitMillis = absoluteMaxTimeLimit = 0;
        MAX_DEPTH = depth;
        return searchFixedTime(new UCIEntry(), 0);
    }

    public static final int searchFixedTime(final Chessboard chessboard, final long maxTime) {
        EngineSpecifications.ALLOW_TIME_LIMIT = true;
        masterBoard = chessboard;
        manageTime = false;
        timeLimitMillis = absoluteMaxTimeLimit = maxTime;
        return searchFixedTime(new UCIEntry(), 0);
    }

    public final void receiveSearchSpecs(final Chessboard chessboard, final int fixedDepth) {
        Chessboard[] chessboards = new Chessboard[NUMBER_OF_THREADS];
        chessboards[MASTER_THREAD] = chessboard;
        if (NUMBER_OF_THREADS > 1) {
            for (int t = 1; t < NUMBER_OF_THREADS; t++) {
                chessboards[t] = new Chessboard(chessboard);
            }
        }
        receiveSearchSpecs(chessboards, fixedDepth, 0, false, 0, 0, 0, 0);
    }

    public final void receiveSearchSpecs(final Chessboard chessboard, final boolean fixedTimeIgnored, final long fixedTime) {
        Chessboard[] chessboards = new Chessboard[NUMBER_OF_THREADS];
        chessboards[MASTER_THREAD] = chessboard;
        if (NUMBER_OF_THREADS > 1) {
            for (int t = 1; t < NUMBER_OF_THREADS; t++) {
                chessboards[t] = new Chessboard(chessboard);
            }
        }
        receiveSearchSpecs(chessboards, 0, fixedTime, false, 0, 0, 0, 0);
    }

    public final void receiveSearchSpecs(final Chessboard[] chessboards, final int fixedDepth, final long fixedTime,
                                         boolean myTime, long maxMyTime, long maxEnemyTime, long increment, Integer movesToGo) {
        boards = chessboards;
        masterBoard = boards[0];
        rootMoves = new int[boards.length][];
        receiveSearchSpecs(masterBoard, fixedDepth, fixedTime, myTime, maxMyTime, maxEnemyTime, increment, movesToGo);
    }

    public final void receiveSearchSpecs(final Chessboard chessboard, final int fixedDepth, final long fixedTime,
                                         boolean myTime, long maxMyTime, long maxEnemyTime, long increment, Integer movesToGo) {
        masterBoard = chessboard;
        resetBetweenMoves();
        if (myTime) {
            EngineSpecifications.ALLOW_TIME_LIMIT = true;
            manageTime = true;
            timeLimitMillis = allocateTime(maxMyTime, maxEnemyTime, increment, movesToGo, masterBoard.fullMoveCounter);
            absoluteMaxTimeLimit = maxMyTime;
            MAX_DEPTH = ABSOLUTE_MAX_DEPTH;

        } else if (fixedDepth != 0) {
            EngineSpecifications.ALLOW_TIME_LIMIT = false;
            manageTime = false;
            timeLimitMillis = absoluteMaxTimeLimit = 0;
            MAX_DEPTH = fixedDepth;

        } else if (fixedTime != 0) {
            EngineSpecifications.ALLOW_TIME_LIMIT = true;
            manageTime = false;
            timeLimitMillis = absoluteMaxTimeLimit = fixedTime;
        }
    }


    private static boolean searchFinished = false;

    // set threads first if you want more than one
    public int simpleSearch() {
        searchFinished = false;
        go();
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return getAiMove();
            }
            if (searchFinished) {
                return getAiMove();
            }
        }
    }

    static Chessboard masterBoard;
    static Chessboard[] boards;

    public void go() {
        searchFixedTime(uciEntry, true);
    }

    public static final void searchFixedTime(UCIEntry uciEntry, boolean whatever) {
        startTime = System.currentTimeMillis();

        //UCI can provide root moves if doing searchmoves
        if (rootMoves[MASTER_THREAD] == null && computeMoves) {
            rootMoves[MASTER_THREAD] = masterBoard.generateLegalMoves();
        }

        int numberOfRealMoves = rootMoves[MASTER_THREAD][rootMoves[MASTER_THREAD].length - 1];
        if (numberOfRealMoves == 0) {
            uciEntry.sendBestMove(0);
            searchFinished = true;
            return;
        }

        if (numberOfRealMoves == 1 && quitOnSingleMove) {
            uciEntry.sendBestMove(rootMoves[MASTER_THREAD][0] & MOVE_MASK_WITHOUT_CHECK);
            searchFinished = true;
            return;
        }

        scoreMovesAtRoot(rootMoves[MASTER_THREAD], numberOfRealMoves, masterBoard);
        Ints.sortDescending(rootMoves[MASTER_THREAD], 0, numberOfRealMoves);

        // to avoid regenerating and rescoring root moves
        for (int i = 1; i < NUMBER_OF_THREADS; i++) {
            System.arraycopy(rootMoves[MASTER_THREAD], 0, rootMoves[i], 0, rootMoves[MASTER_THREAD].length);
        }

        if (NUMBER_OF_THREADS == 1) {
            new ChessThread(uciEntry, masterBoard).start();
        } else {
            for (int t = 1; t < NUMBER_OF_THREADS; t++) {
                new ChessThread(t, boards[t]).start();
            }
            new ChessThread(uciEntry, boards[0]).start();
        }
    }

    public static final int searchFixedTime(UCIEntry uciEntry, int whichThread) {
        if (true) {
            return 666;
        }
        startTime = System.currentTimeMillis();

        //UCI can provide root moves if doing searchmoves
        if (rootMoves == null && computeMoves) {
            rootMoves[whichThread] = masterBoard.generateLegalMoves();
        }

        int numberOfRealMoves = rootMoves[whichThread][rootMoves.length - 1];
        if (numberOfRealMoves == 0) {
            return 0;
        }

        if (numberOfRealMoves == 1 && quitOnSingleMove) {
            return rootMoves[whichThread][0] & MOVE_MASK_WITHOUT_CHECK;
        }

        scoreMovesAtRoot(rootMoves[whichThread], numberOfRealMoves, masterBoard);
        Ints.sortDescending(rootMoves[whichThread], 0, numberOfRealMoves);

        search(masterBoard, uciEntry, whichThread);

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        if (time != 0) {
            if (time < 1000) {
                nps = 0;
            } else {
                calculateNPS();
            }
        }

        final int bestMove = rootMoves[whichThread][0] & MOVE_MASK_WITHOUT_CHECK;
        if (uciEntry != null) {
            uciEntry.sendBestMove(bestMove);
        }
        return bestMove;
    }

    public static void calculateNPS() {
        final long l = System.currentTimeMillis();
        long time = l - startTime;
        if (time < 1000) {
            nps = 0;
        } else {
            long total = 0;
            for (int i = 0; i < numberOfMovesMade.length; i++) {
                total += numberOfMovesMade[i] + numberOfQMovesMade[i];
            }
            nps = ((1000 * total) / time);
        }
        if (nps < 0) {
            throw new RuntimeException();
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


    static void search(Chessboard board, UCIEntry uciEntry, int whichThread) {
        nonTerminalNodes = 0;
        terminalNodes = 0;
        nonTerminalTime = 0;
        terminalTime = 0;
        terminal = false;

        final boolean masterThread = whichThread == MASTER_THREAD;

        int depth = 0;
        int aspirationScore = 0;

        int alpha;
        int beta;
        int alphaAspirationAttempts = 0;
        int betaAspirationAttempts = 0;

        alpha = aspirationScore - ASPIRATION_WINDOWS[alphaAspirationAttempts];
        beta = aspirationScore + ASPIRATION_WINDOWS[betaAspirationAttempts];

        int score;

        if (DEBUG) {
            System.out.println("STARTING MAIN I-D LOOP FOR THREAD " + whichThread + ", " + Thread.currentThread());
            System.out.println();
        }

        everything:
        while (depth < MAX_DEPTH) {
            depth++;

            if (DEBUG) {
                System.out.println("- " + Thread.currentThread() + " is at depth " + depth);
            }

            int previousAi = rootMoves[whichThread][0] & MOVE_MASK_WITHOUT_CHECK;
            int previousAiScore = 0;

            if (depth == MAX_DEPTH) {
                nonTerminalTime = System.currentTimeMillis() - startTime;
                terminal = true;
            }

            while (true) {

                score = principleVariationSearch(board, depth, 0,
                        alpha, beta, 0, whichThread);

                if ((manageTime && !weHavePanicked)
                        && (depth >= 6 && aiMoveScore < previousAiScore - PANIC_SCORE_DELTA)) {
                    timeLimitMillis = allocatePanicTime(timeLimitMillis, absoluteMaxTimeLimit);
                    weHavePanicked = true;
                }

                previousAiScore = aiMoveScore;

                if (stopNow || stopSearch(startTime, timeLimitMillis)) {
                    break everything;
                }

                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
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

            if (PRINT_PV && masterThread) {
                long time = System.currentTimeMillis() - startTime;
                uciEntry.send(board, aiMoveScore, depth, time, numberOfMovesMade[0]);
            }

            aspirationScore = score;
        }

        if (depth == MAX_DEPTH) {
            terminalTime = System.currentTimeMillis() - startTime;
        }

        if (PRINT_PV && masterThread) {
            long time = System.currentTimeMillis() - startTime;
            uciEntry.send(board, aiMoveScore, depth, time, numberOfMovesMade[0]);
        }


        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        if (time != 0) {
            if (time < 1000) {
                nps = 0;
            } else {
                calculateNPS();
            }
        }

        if (masterThread) {
            final int bestMove = rootMoves[MASTER_THREAD][0] & MOVE_MASK_WITHOUT_CHECK;
            uciEntry.sendBestMove(bestMove);
            searchFinished = true;
        }
    }
    

    static int principleVariationSearch(Chessboard board,
                                        int depth, int ply,
                                        int alpha, int beta,
                                        int nullMoveCounter,
                                        int whichThread) {

        final int originalAlpha = alpha;
        final int turn = board.turn;

        if (whichThread != 0) {
//            MoveParser.printMove(rootMoves[whichThread]);
        }
        int[] moves = ply == 0 ? rootMoves[whichThread] : board.generateLegalMoves();

        boolean inCheck = board.inCheckRecorder;

        if (ply + depth < MAX_DEPTH_HARD - 2) {
            depth += extensions(board, ply, inCheck, moves);
        }

        Assert.assertTrue(depth >= 0);

        if (depth <= 0) {

            if (terminal) {
                terminalNodes++;
            } else {
                nonTerminalNodes++;
            }

            if (ply >= MAX_DEPTH_HARD) {
                return Evaluator.eval(board, moves);
            }

            return quiescenceSearch(board, alpha, beta);
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
                    if (ply == 0) {
                        if (whichThread == 0) {
                            putAIMoveFirst(hashMove, whichThread);
                            aiMoveScore = score;
                        }
                    }
                    return score;
                } else if (flag == LOWERBOUND) {
                    if (score >= beta) {
                        if (ply == 0) {
                            if (whichThread == 0) {
                                putAIMoveFirst(hashMove, whichThread);
                                aiMoveScore = score;
                            }
                        }
                        return score;
                    }
                } else if (flag == UPPERBOUND) {
                    if (score <= alpha) {
                        if (ply == 0) {
                            if (whichThread == 0) {
                                putAIMoveFirst(hashMove, whichThread);
                                aiMoveScore = score;
                            }
                        }
                        return score;
                    }
                }
            }
        }

        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        int staticBoardEval = SHORT_MINIMUM;

        if (!thisIsAPrincipleVariationNode && !inCheck) {

            staticBoardEval = Evaluator.eval(board, moves);

            if (isBetaRazoringOkHere(depth, staticBoardEval)) {
                betaTotal++;
                int specificBetaRazorMargin = betaRazorMargin[depth];
                if (staticBoardEval - specificBetaRazorMargin >= beta) {
                    betaSuccess++;
                    return staticBoardEval;
                }
                betaFail++;
            }

            if (isAlphaRazoringMoveOkHere(depth, alpha)) {
                int specificAlphaRazorMargin = alphaRazorMargin[depth];
                if (staticBoardEval + specificAlphaRazorMargin < alpha) {
                    alphaTotal++;
                    int qScore = quiescenceSearch(board,
                            alpha - specificAlphaRazorMargin,
                            alpha - specificAlphaRazorMargin + 1);

                    if (qScore + specificAlphaRazorMargin <= alpha) {
                        alphaSuccess++;
                        return qScore;
                    }
                    alphaFail++;
                }
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

        if (hashMove == 0
                && depth >= iidDepth) {
            int d = thisIsAPrincipleVariationNode ? depth - 2 : depth >> 1;
            principleVariationSearch(board, d, ply, alpha, beta, nullMoveCounter, whichThread); // todo, allow null move?
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
        final int lastMove = moves[moves.length - 1];
        if (ply != 0) {
            scoreMoves(moves, board, ply, hashMove);
            Ints.sortDescending(moves, 0, lastMove);
        }

        int numberOfMovesSearched = 0;

        for (int i = 0; i < lastMove; i++) {
            if (moves[i] == 0) {
                break;
            }
            int moveScore = getMoveScore(moves[i]);

            if (i < lastMove - 1) {
                if (i == 0) {
                    Assert.assertTrue(moveScore >= getMoveScore(moves[i + 1]));
                } else {
                    Assert.assertTrue(moveScore <= getMoveScore(moves[i - 1]));

                    Assert.assertTrue(moveScore >= getMoveScore(moves[i + 1]));
                }
            }

            int move = moves[i] & MOVE_MASK_WITHOUT_CHECK;

            Assert.assertTrue(moveScore != 0);

            final boolean captureMove = isCaptureMove(move);
            final boolean promotionMove = isPromotionMove(move);
            final boolean queenPromotionMove = promotionMove ? isPromotionToQueen(move) : false;
            final boolean givesCheckMove = isCheckingMove(moves[i]); // keep moves[i] here
            final boolean pawnToSix = moveIsPawnPushSix(turn, move);
            final boolean pawnToSeven = moveIsPawnPushSeven(turn, move);
            final boolean quietMove = !(captureMove || promotionMove);

            if (captureMove && !promotionMove) {
                Assert.assertTrue(moveScore >= (neutralCapture - 5));
            }

            if (queenPromotionMove) {
                boolean condition = moveScore >= queenQuietPromotionScore;
                Assert.assertTrue(condition);
            }

            if (!thisIsAPrincipleVariationNode) {
                if (bestScore < CHECKMATE_ENEMY_SCORE_MAX_PLY
                        && notJustPawnsLeft(board)) {
                    if (!queenPromotionMove
                            && !givesCheckMove
                            && !pawnToSix
                            && !pawnToSeven
                            && depth <= 4
                            && numberOfMovesSearched >= depth * 3 + 3) {
                        lmpTotal++;
                        continue;
                    }
                }

                if (isFutilityPruningAllowedHere(depth,
                        queenPromotionMove, givesCheckMove, pawnToSix, pawnToSeven, numberOfMovesSearched)) {

                    futilityTotal++;

                    if (staticBoardEval == SHORT_MINIMUM) {
                        staticBoardEval = Evaluator.eval(board,
                                moves);
                    }

                    int futilityScore = staticBoardEval + futilityMargin[depth];

                    if (futilityScore <= alpha) {
                        futilitySuccess++;
                        if (futilityScore > bestScore) {
                            bestScore = futilityScore;
                        }
                        continue;
                    }
                    futilityFail++;
                }
            }

            try {
                board.makeMoveAndFlipTurn(move);
            } catch (Exception | Error e) {
                System.err.println("error in search for thread " + Thread.currentThread());
                System.err.println(Thread.currentThread());
                System.err.println(board);
                System.err.println(board.toFenString());
                MoveParser.printMove(moves);
                e.printStackTrace();
                System.out.println();
                System.out.println();
            }

            numberOfMovesMade[whichThread]++;
            numberOfMovesSearched++;

            if (board.isDrawByInsufficientMaterial()
                    || (!captureMove && !promotionMove &&
                    (board.isDrawByRepetition(1) || board.isDrawByFiftyMoveRule()))) {
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
                    if (moveScore >= killerTwoScore) {
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

            if (TimeAllocator.outOfTime(startTime, timeLimitMillis, manageTime)) {
                return 0;
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, score);

                if (ply == 0) {
                    if (whichThread == 0) {
                        putAIMoveFirst(bestMove, whichThread);
                        aiMoveScore = score;
                    }
                }
            }

            if (alpha >= beta) {
                Assert.assertTrue((move & MOVE_SCORE_MASK) == 0);
                if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    updateMateKillerMoves(whichThread, move, ply);
                    break;
                }
                if (!captureMove) {
                    updateKillerMoves(whichThread, move, ply);
                }
                break;
            }
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

        addToTableReplaceByDepth(board.zobristHash,
                bestMove & MOVE_MASK_WITHOUT_CHECK, bestScore, depth, flag, ply, age);

        return bestScore;
    }


    private static void putAIMoveFirst(int aiMove, int whichThread) {
        final int aiMoveMask = aiMove & MOVE_MASK_WITHOUT_CHECK;
        if ((rootMoves[whichThread][0] & MOVE_MASK_WITHOUT_CHECK) == aiMoveMask) {
            return;
        }

        final int maxMoves = rootMoves[whichThread].length - 1;
        for (int i = 0; i < maxMoves; i++) {
            final int rootMove = rootMoves[whichThread][i] & MOVE_MASK_WITHOUT_CHECK;
            if (rootMove == aiMove || rootMove == aiMoveMask) {
                Assert.assertTrue(i != 0);
                System.arraycopy(rootMoves[whichThread], 0,
                        rootMoves[whichThread], 1, i);
                rootMoves[whichThread][0] = buildMoveScore(aiMoveMask, hashScore); // todo
                break;
            }
        }
    }


}
