package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.moveordering.MoveOrderingConstants;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.updateKillerMoves;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.updateMateKillerMoves;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.allocateTime;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.outOfTime;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

public class Engine {

    private static final Object lock = new Object();
    private volatile static int HACK = 0;

    volatile static int deepestThread = 0;

    private static ChessThread[] threads;
    static int[] depths;

    static int[] rootMoves;
    static int aiMoveScore;
    private static boolean isReady = false;
    private static boolean stopInstruction = false;
    private static long nps;
    private static int[] numberOfMovesMade;
    static int[] numberOfQMovesMade;
    private static long startTime = 0;
    private static boolean manageTime = true;
    public static boolean stopNow = false;

    private static UCIEntry uciEntry;

    public static long getNps() {
        calculateNPS();
        return nps;
    }

    public static int getAiMove() {
        return rootMoves[0];
    }

    public static UCIEntry getUciEntry() {
        return uciEntry;
    }

    public static void setUciEntry(UCIEntry uciEntry) {
        Engine.uciEntry = uciEntry;
    }

    private static boolean isStopInstruction() {
        return stopInstruction;
    }

    public static void setStopInstruction(boolean instruction) {
        stopInstruction = instruction;
    }

    private static void calculateNPS(){
        long time = System.currentTimeMillis() - startTime;
        if (time < 1000){
            nps = 0;
        }
        else {
            System.out.println("time: " + time);
            nps = ((1000 * getNodesSearched()) / time);
        }
    }

    public static long getNodesSearched() {
        long ans = 0;
        for (int i = 0; i < numberOfMovesMade.length; i++){
            ans += numberOfMovesMade[i];
        }
        for (int i = 0; i < numberOfQMovesMade.length; i++){
            ans += numberOfQMovesMade[i];
        }
        return ans;
    }

    static boolean stopSearch(long startTime, long timeLimiMillis) {
        return Engine.isStopInstruction()
                || (EngineSpecifications.ALLOW_TIME_LIMIT && outOfTime(startTime, timeLimiMillis, manageTime));
    }

    public static void giveThreadsBoard(Chessboard board) {
        for (int c = 0; c < THREAD_NUMBER; c++) {
            threads[c].setBoard(new Chessboard(board));
        }
    }

    public static void setupThreads() {
        depths = new int[THREAD_NUMBER];
        threads = new ChessThread[THREAD_NUMBER];
        numberOfMovesMade = new int[THREAD_NUMBER];
        numberOfQMovesMade = new int[THREAD_NUMBER];
        for (int c = 0; c < THREAD_NUMBER; c++) {
            threads[c] = new ChessThread("I"+c, c);
        }
    }

    public static void setup() {
        setupThreads();
        stopInstruction = false;
        isReady = true;
        reset();
    }

    private static void reset() {
        HACK = 0;
        nps = 0;
        stopInstruction = false;
        aiMoveScore = SHORT_MINIMUM;

        stopNow = false;

        initTable(TABLE_SIZE);
        
        MoveOrderer.initMoveOrderer();
    }

    private static void putAIMoveFirst(int[] rootMoves, int aiMove) {

        if (rootMoves[0] == aiMove) {
            return;
        }

        System.arraycopy(rootMoves, 0,
                rootMoves, 1,
                Ints.indexOf(rootMoves, aiMove));

        rootMoves[0] = aiMove;
    }

    public static int searchFixedDepth(Chessboard board, int depth) {
        EngineSpecifications.ALLOW_TIME_LIMIT = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0, false);
    }

    public static int searchMyTime(Chessboard board, long maxTime, long increment) {
        EngineSpecifications.ALLOW_TIME_LIMIT = true;

        if (maxTime < 1000) {
            return searchFixedDepth(board, 1);
        }
        if (maxTime < 5000) {
            return searchFixedDepth(board, 2);
        }
        long timeLimit = allocateTime(maxTime, increment);

        return searchFixedTime(board, timeLimit, false);
    }

    static void increment(){
        synchronized (lock){
            HACK++;
        }
    }

    static void threadDepthIncrement(int depth){
        if (depth > deepestThread){
            deepestThread = depth;
        }
    }

    synchronized public static int searchFixedTime(Chessboard board, long maxTime, boolean manageTimee) {
        if (!isReady) {
            setup();
        }

        setupThreads();
        giveThreadsBoard(board);

        manageTime = manageTimee;

        reset();

        startTime = System.currentTimeMillis();

        rootMoves = board.generateLegalMoves();

        int numberOfRealMoves = MoveParser.numberOfRealMoves(rootMoves);
        if (numberOfRealMoves == 0 || numberOfRealMoves == 1){
            return rootMoves[0];
        }

        for (int c = 0; c < THREAD_NUMBER; c++) {
            Engine.threads[c].setTime(startTime, maxTime);
            Engine.threads[c].start();
        }

        while (HACK < THREAD_NUMBER || !stopNow) {
            if (HACK >= THREAD_NUMBER){
                stopNow = true;
            }
        }

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        if (time != 0) {
            calculateNPS();
        }

        return rootMoves[0];
    }


    static int principleVariationSearch(Chessboard board, int[] rootMoves,
                                        int depth, int ply,
                                        int alpha, int beta,
                                        int nullMoveCounter,
                                        long startTime, long timeLimitMillis,
                                        int whichThread) throws IllegalUnmakeException {

        int originalAlpha = alpha;

        int[] moves = board.generateLegalMoves();

        boolean boardInCheck = board.inCheckRecorder;

        depth += extensions(board, ply, boardInCheck, moves);

        Assert.assertTrue(depth >= 0);

        if (depth <= 0){
            Assert.assertTrue(!board.inCheck(board.isWhiteTurn()));
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta, whichThread);
        }

        alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
        beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
        if (alpha >= beta){
            return alpha;
        }

        int hashMove = 0;
        int score;

        long previousTableData = retrieveFromTable(board.getZobrist());
        if (previousTableData != 0) {
            score = getScore(previousTableData, ply);
            hashMove = getMove(previousTableData);

            if (getDepth(previousTableData) >= depth
//                    && PVLine.verifyMove(hashMove, moves)
            ){
                int flag = getFlag(previousTableData);
                if (flag == EXACT) {
                    if (ply == 0){
                        if (whichThread == 0){
                            putAIMoveFirst(rootMoves, hashMove);
                            aiMoveScore = score;
                        }
                    }
                    return score;
                }
                else if (flag == LOWERBOUND) {
                    if (score >= beta){
                        if (ply == 0){
                            if (whichThread == 0){
                                putAIMoveFirst(rootMoves, hashMove);
                                aiMoveScore = score;
                            }
                        }
                        return score;
                    }
                }
                else if (flag == UPPERBOUND) {
                    if (score <= alpha){
                        if (ply == 0){
                            if (whichThread == 0){
                                putAIMoveFirst(rootMoves, hashMove);
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

        if (!thisIsAPrincipleVariationNode && !boardInCheck) {

            staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(), moves);

            if (isBetaRazoringOkHere(depth, staticBoardEval)){
                int specificBetaRazorMargin = betaRazorMargin[depth];
                if (staticBoardEval - specificBetaRazorMargin >= beta){
                    return staticBoardEval;
                }
            }

            if (isAlphaRazoringMoveOkHere(depth, alpha)){
                int specificAlphaRazorMargin = alphaRazorMargin[depth];
                if (staticBoardEval + specificAlphaRazorMargin < alpha){
                    int qScore = QuiescenceSearch.quiescenceSearch(board,
                            alpha - specificAlphaRazorMargin,
                            alpha - specificAlphaRazorMargin + 1,
                            whichThread);

                    if (qScore + specificAlphaRazorMargin <= alpha){
                        return qScore;
                    }
                }
            }

            int R = nullMoveDepthReduction();
            if (isNullMoveOkHere(board, nullMoveCounter, depth, R)){
                board.makeNullMoveAndFlipTurn();

                int nullScore = -principleVariationSearch(board, rootMoves,
                        depth - R - 1, ply + 1,
                        -beta, -beta + 1, nullMoveCounter + 1,
                        startTime, timeLimitMillis,
                        whichThread
                );

                board.unMakeNullMoveAndFlipTurn();

                if (nullScore >= beta){
                    if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                        nullScore = beta;
                    }
                    return nullScore;
                }
            }
        }

        MoveOrderer.scoreMoves(whichThread, moves, board, ply, hashMove);

        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;
        int realMoves = MoveParser.numberOfRealMoves(moves);
        Ints.sortDescending(moves, 0, realMoves);
        int numberOfMovesSearched = 0;

        if (ply == 0 && hashMove != 0){
            Assert.assertEquals(moves[0] & MoveOrderer.MOVE_MASK, hashMove);
        }
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

            boolean captureMove = MoveParser.isCaptureMove(move);
            boolean promotionMove = MoveParser.isPromotionMove(move);
            boolean givesCheckMove = MoveOrderer.checkingMove(board, move);
            boolean pawnToSix = MoveParser.moveIsPawnPushSix(move);
            boolean pawnToSeven = MoveParser.moveIsPawnPushSeven(move);

            if (!thisIsAPrincipleVariationNode) {
                if (bestScore < CHECKMATE_ENEMY_SCORE_MAX_PLY
                        && notJustPawnsLeft(board, board.isWhiteTurn())) {
                    if (!promotionMove
                            && !givesCheckMove
                            && !pawnToSix
                            && !pawnToSeven
                            && depth <= 4
                            && numberOfMovesSearched >= depth * 3 + 3) {
                        continue;
                    }
                }

                if (isFutilityPruningAllowedHere(depth,
                        promotionMove, givesCheckMove, pawnToSix, pawnToSeven, numberOfMovesSearched)) {

                    if (staticBoardEval == SHORT_MINIMUM) {
                        staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(),
                                board.generateLegalMoves());
                    }

                    int futilityScore = staticBoardEval + futilityMargin[depth];

                    if (futilityScore <= alpha) {
                        if (futilityScore > bestScore) {
                            bestScore = futilityScore;
                        }
                        continue;
                    }
                }
            }


            board.makeMoveAndFlipTurn(move);
            numberOfMovesMade[whichThread]++;
            numberOfMovesSearched++;

            if (board.drawByRepetition(board.isWhiteTurn())) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = alpha + 1;

                int R = lateMoveDepthReduction(depth, numberOfMovesSearched);

                if (numberOfMovesSearched > 1
                        && depth > R && !captureMove && !promotionMove
                        && !pawnToSeven && !boardInCheck && !givesCheckMove) {

                    score = -principleVariationSearch(board, rootMoves,
                            depth - R - 1, ply + 1,
                            -alpha - 1, -alpha, nullMoveCounter,
                            startTime, timeLimitMillis,
                            whichThread);
                }

                if (numberOfMovesSearched > 1 && score > alpha) {
                    score = -principleVariationSearch(board, rootMoves,
                            depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0,
                            startTime, timeLimitMillis,
                            whichThread);
                }

                if (score > alpha) {
                    score = -principleVariationSearch(board, rootMoves,
                            depth - 1, ply + 1,
                            -beta, -alpha, 0,
                            startTime, timeLimitMillis,
                            whichThread);
                }
            }

            board.unMakeMoveAndFlipTurn();

            if (outOfTime(startTime, timeLimitMillis, manageTime)){
                return 0;
            }


            if (score > bestScore) {
                bestScore = score;
                bestMove = move & MoveOrderer.MOVE_MASK;
                alpha = Math.max(alpha, score);

                if (ply == 0) {
                    if (whichThread == 0){
                        putAIMoveFirst(rootMoves, bestMove);
                        aiMoveScore = score;
                    }
                }
            }

            if (alpha >= beta) {
                if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    updateMateKillerMoves(whichThread, move, ply);
                } else {
                    updateKillerMoves(whichThread, move, ply);
                }
                MoveOrderer.updateHistoryMoves(whichThread, move, ply);
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

        Assert.assertTrue(bestMove != 0);

        int flag;
        if (bestScore <= originalAlpha){
            flag = UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = LOWERBOUND;
        } else {
            flag = EXACT;
        }

        addToTableReplaceByDepth(board.getZobrist(),
                bestMove & MoveOrderer.MOVE_MASK, bestScore, depth, flag, ply);


        return bestScore;
    }
}