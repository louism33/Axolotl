package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.*;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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
import static com.github.louism33.chesscore.MaterialHashUtil.makeMaterialHash;
import static com.github.louism33.chesscore.MaterialHashUtil.typeOfEndgame;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;
import static com.github.louism33.chesscore.MoveConstants.MOVE_SCORE_MASK;
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
    public static boolean running = false; // todo consider replacing booleans with int(as enum)
    public static boolean quitOnSingleMove = true;
    public static boolean computeMoves = true;

    public static int aiMoveScore;
    public static boolean weHavePanicked = false;

    public static long nps;
    public static long totalMovesMade;
    public static long[] numberOfMovesMade;
    static long[] numberOfQMovesMade;

    public static int age = 0;

    private static final int PHASE_HASH = 0, PHASE_KILLER_ONE = 1, PHASE_KILLER_TWO = 2, PHASE_GEN_MOVES = 3, PHASE_ORDER_MOVES = 4, PHASE_REG_MOVES = 5;

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

        scoreMovesAtRoot(rootMoves[MASTER_THREAD], numberOfRealMoves, Engine.board);

        Engine.board = board;

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

    public static int hashCutoff = 0, mateKillerCutoff = 0, killerOneCutoff = 0;
    public static int killerTwoCutoff = 0, oldKillerScoreOneCutoff = 0, oldKillerScoreTwoCutoff = 0, otherCutoff = 0;
    public static int noMovesAndHash = 0, noMovesAndNoHash = 0;
    public static int yesMovesAndHash = 0, yesMovesAndNoHash = 0;
    public static int cutoffWithoutMoveGen = 0;

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
        final boolean rootNode = ply == 0;
//        final boolean trunkNode = !rootNode && depth != 0;
//        final boolean leafNode = !rootNode && depth == 0;
        if (rootNode) {
            checkers = board.checkingPieces;
            moves = rootMoves[whichThread];
        } else {
            checkers = board.getCheckers();
        }


        if (MASTER_DEBUG && rootNode) {
            Assert.assertEquals(rootMoves[whichThread], moves);
            Assert.assertEquals(board.zobristPawnHash, cloneBoard.zobristPawnHash);
            Assert.assertEquals(board.zobristHash, cloneBoard.zobristHash);
        }

        boolean inCheck = board.inCheckRecorder;

        if (ply + depth < MAX_DEPTH_HARD - 2) { // consider singular reply only depth 0
            depth += extensions(board, ply, inCheck, moves); // todo, move to parent node
        }

        final boolean trunkNode = !rootNode && depth != 0;
        final boolean leafNode = !rootNode && depth == 0;
        if (rootNode) {
//            checkers = board.checkingPieces;
            moves = rootMoves[whichThread];
        } else if (leafNode) {
//            checkers = board.getCheckers();
            moves = board.generateLegalMoves(checkers);
        } else {
//            checkers = board.getCheckers();
//            moves = board.generateLegalMoves(checkers);
        }
        

        Assert.assertTrue(depth >= 0);

        if (depth <= 0) {

            if (terminal) {
                terminalNodes++;
            } else {
                nonTerminalNodes++;
            }

            if (ply >= MAX_DEPTH_HARD) {
                return Evaluator.eval(board, moves, whichThread);
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

        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        int staticBoardEval = SHORT_MINIMUM;

        if (!thisIsAPrincipleVariationNode && !inCheck) {

            if (moves == null) {
                moves = board.generateLegalMoves(checkers);
            }
            staticBoardEval = Evaluator.eval(board, moves, whichThread);

            if (isBetaRazoringOkHere(depth, staticBoardEval)) {
                betaTotal++;
                int specificBetaRazorMargin = betaRazorMargin[depth];
                if (staticBoardEval - specificBetaRazorMargin >= beta) {
                    betaSuccess++;
                    return staticBoardEval;
                }
                betaFail++;
            }

            Assert.assertTrue(depth > 0);

            if (isAlphaRazoringMoveOkHere(depth, alpha)) {
                int specificAlphaRazorMargin = alphaRazorMargin[depth];
                if (staticBoardEval + specificAlphaRazorMargin < alpha) {
                    alphaTotal++;
                    int qScore = quiescenceSearch(board,
                            alpha - specificAlphaRazorMargin,
                            alpha - specificAlphaRazorMargin + 1,
                            whichThread, 0, 0); // we pass 0 here as we are not looking for seldepth info

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

        if (hashMove == 0 && depth >= iidDepth) {
            int d = thisIsAPrincipleVariationNode ? depth - 2 : depth >> 1;
            principleVariationSearch(board, d, ply, alpha, beta, nullMoveCounter, whichThread); // todo, allow null move?
            // todo, ply + 1 ?
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

        int PHASE = PHASE_HASH;

        if (moves == null) {
            
//            moves = board.generateLegalMoves(checkers);
            
            if (hashMove != 0) {
//                PHASE = PHASE_HASH;
                noMovesAndHash++;
            } else{
//                PHASE++;
                noMovesAndNoHash++;
            } 
            
            PHASE = PHASE_GEN_MOVES; // todo
        } else {
            if (hashMove != 0) {
                yesMovesAndHash++;
            } else{
//                PHASE++;
                yesMovesAndNoHash++;
            }

            PHASE = PHASE_ORDER_MOVES; // todo
        }
        
        
        
        int lastMove = 2;

        int numberOfMovesSearched = 0;

        // prob need while loop + int Phase
        int moveScore;
        int i = 0;
        int move;
        while (i < lastMove) {

            switch (PHASE) {
//                case PHASE_HASH:
//                    Assert.assertTrue(hashMove != 0);
//                case PHASE_KILLER_ONE:
//                case PHASE_KILLER_TWO:
                case PHASE_GEN_MOVES:
                    Assert.assertTrue(moves == null);

                    moves = board.generateLegalMoves(checkers);

                    PHASE++;

                case PHASE_ORDER_MOVES:
                    lastMove = moves[moves.length - 1];
                    if (ply != 0) {
                        scoreMoves(moves, board, ply, hashMove, whichThread, false);
                    }
                    PHASE++;
                    
                case PHASE_REG_MOVES:
                    final boolean condition = moves != null;
                    if (!condition) {
                        System.out.println();
                    }
                    Assert.assertTrue(condition);
            }

//            System.out.println("phase: " + PHASE+ ", i: " + i);
            
            if (moves[i] == 0) {
                break;
            }
            moveScore = getMoveScore(moves[i]);


            if (MASTER_DEBUG) {
                if (i < lastMove - 1) {
                    if (i == 0) {
                        Assert.assertTrue(moveScore >= getMoveScore(moves[i + 1]));
                    } else {
                        Assert.assertTrue(moveScore <= getMoveScore(moves[i - 1]));

                        Assert.assertTrue(moveScore >= getMoveScore(moves[i + 1]));
                    }
                }
            }

            move = moves[i] & MOVE_MASK_WITHOUT_CHECK;

//            MoveParser.printMove(move);
            
            if (MASTER_DEBUG) {
                board.makeMoveAndFlipTurn(move);
                board.generateLegalMoves();
                final boolean condition = board.inCheckRecorder;
                board.unMakeMoveAndFlipTurn();
                if (moveScore == giveCheckMove) {
                    Assert.assertTrue(condition);
                } else if (moveScore < giveCheckMove && condition) {
                    final boolean isSpecialMove = isCaptureMove(move) || isPromotionMove(move)
                            || isEnPassantMove(move) || isCastlingMove(move) || (move == hashMove);
                    Assert.assertTrue(isSpecialMove);
                }

                if (condition) {
                    final boolean condition1 = moveScore >= giveCheckMove || (MoveParser.isPromotionMove(move) && !MoveParser.isPromotionToQueen(move)) || MoveParser.isCastlingMove(move);
                    if (!condition1) {
                        System.out.println(board);
                        MoveParser.printMove(moves);
                        MoveParser.printMove(move);
                        System.out.println(moveScore);
                    }
                    Assert.assertTrue(condition1);
                }
            }

            final boolean captureMove = isCaptureMove(move);
            final boolean promotionMove = isPromotionMove(move);
            final boolean queenPromotionMove = promotionMove ? isPromotionToQueen(move) : false;
            final boolean givesCheckMove = isCheckingMove(moves[i]); // keep moves[i] here
            final boolean pawnToSix = moveIsPawnPushSix(turn, move);
            final boolean pawnToSeven = moveIsPawnPushSeven(turn, move);
            final boolean quietMove = !(captureMove || promotionMove);

            if (MASTER_DEBUG) {
                if (captureMove && !promotionMove) {
                    if (!(moveScore >= (neutralCapture - 5))) {
                        System.out.println();
                        System.out.println(rootNode);
                        System.out.println(leafNode);
                        System.out.println(trunkNode);
                        System.out.println(depth);
                        System.out.println(ply);
                        Assert.assertTrue(moveScore >= (neutralCapture - 5));
                    }
                    Assert.assertTrue(moveScore >= (neutralCapture - 5));
                }

                if (queenPromotionMove) {
                    boolean condition = moveScore >= queenQuietPromotionScore;
                    Assert.assertTrue(condition);
                }
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
                        i++;
                        continue;
                    }
                }

                if (isFutilityPruningAllowedHere(depth,
                        queenPromotionMove, givesCheckMove, pawnToSix, pawnToSeven, numberOfMovesSearched)) {

                    futilityTotal++;

                    if (staticBoardEval == SHORT_MINIMUM) {
                        staticBoardEval = Evaluator.eval(board, moves, whichThread);
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

            board.makeMoveAndFlipTurn(move);

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
                    putAIMoveFirst(bestMove, whichThread);
                    if (whichThread == 0) {
                        aiMoveScore = score;
                    }
                }
            }

            if (alpha >= beta) {

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


//        System.out.println("        Set ai move to: " + MoveParser.toString(aiMove));
//        MoveParser.printMove(rootMoves[0]);
    }


}
