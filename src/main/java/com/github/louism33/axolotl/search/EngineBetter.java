package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.EvalPrintObject;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.main.UCIPrinter;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.moveordering.MoveOrderingConstants;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.moveordering.MoveOrderingConstants.MOVE_SIZE_LIMIT;
import static com.github.louism33.axolotl.moveordering.MoveOrderingConstants.hashScore;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.MoveOrdererBetter.*;
import static com.github.louism33.axolotl.search.MoveOrdererBetter.MOVE_MASK;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.search.SearchUtils.isNullMoveOkHere;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.allocateTime;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.outOfTime;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_UPPER_BOUND;

@SuppressWarnings("ALL")
public class EngineBetter {

    static int aiMoveScore;
    private static boolean isReady = false;
    public static long nps;
    public static long[] numberOfMovesMade = new long[1];
    static long[] numberOfQMovesMade = new long[1];
    private static long startTime = 0;
    public static boolean stopNow = false;
    private static boolean stopInstruction = false;

    static boolean manageTime = true;
    private static long timeLimitMillis;

    public static boolean contains(int[] ints, int target) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == target) {
                return true;
            }
        }
        return false;
    }

    public static void setup() {
        stopInstruction = false;
        isReady = true;
        reset();
    }

    private static void reset() {
        nps = 0;
        stopInstruction = false;
        aiMoveScore = SHORT_MINIMUM;

        numberOfMovesMade[0] = 0;
        numberOfQMovesMade[0] = 0;

        stopNow = false;

        initTable(TABLE_SIZE);

        MoveOrderer.initMoveOrderer();
    }

    public static void main(String[] args) {

        String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);
        board = new Chessboard();

        System.out.println(board);

        searchFixedDepth(board, 14);
//        searchFixedDepth(board, 22);


//        System.out.println("*******************************");
//        board.makeMoveAndFlipTurn(move);
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        iter(depth, board, move);
//        System.out.println("*******************************");
//        System.out.println("*******************************");
//        System.out.println("*******************************");
//        System.out.println("*******************************");
    }

    private static void iter(int depth, Chessboard board, int move) {
        System.out.println(board);
        int move2 = searchFixedDepth(board, depth);
        board.makeMoveAndFlipTurn(move2);
    }

    private static int[] rootMoves;

    public static int searchMyTime(Chessboard board, long maxTime, long increment) {
        EngineSpecifications.ALLOW_TIME_LIMIT = true;
        manageTime = true;

        if (maxTime < 1000) {
            return searchFixedDepth(board, 1);
        }
        if (maxTime < 5000) {
            return searchFixedDepth(board, 2);
        }
        long timeLimit = allocateTime(maxTime, increment);

        return searchFixedTime(board, timeLimit, MAX_DEPTH);
    }

    public static final int searchFixedDepth(Chessboard board, int depth) {
        EngineSpecifications.ALLOW_TIME_LIMIT = false;
        manageTime = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0, depth);
    }

    static boolean stopSearch(long startTime, long timeLimiMillis) {
        return stopInstruction
                || (EngineSpecifications.ALLOW_TIME_LIMIT && outOfTime(startTime, timeLimiMillis, manageTime));
    }

    public static final int searchFixedTime(final Chessboard board, final long maxTime) {
        EngineSpecifications.ALLOW_TIME_LIMIT = true;
        manageTime = false;
        return searchFixedTime(board, maxTime, MAX_DEPTH);
    }

    private static final int searchFixedTime(final Chessboard board, final long maxTime, final int depth) {
        setup();

        startTime = System.currentTimeMillis() - 100;

        timeLimitMillis = maxTime;

        rootMoves = board.generateLegalMoves();
        aiMoveX = rootMoves[0];

        int numberOfRealMoves = rootMoves[rootMoves.length - 1];
        if (numberOfRealMoves == 0 || numberOfRealMoves == 1){
            return rootMoves[0] & MOVE_MASK;
        }

        scoreMovesAtRoot(rootMoves, numberOfRealMoves, board);
        Ints.sortDescending(rootMoves, 0, numberOfRealMoves);

        search(board, depth);

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        if (time != 0) {
            if (time < 1000){
                nps = 0;
            }
            else {
                calculateNPS();
                System.out.println("time spent: " + (time / 1000) + " and nps: " + nps);
            }
        }

        final int bestMove = rootMoves[0] & MOVE_MASK;
        final int actual = aiMoveX & MOVE_MASK;
        return bestMove;
    }

    public static void calculateNPS(){
        final long l = System.currentTimeMillis();
        long time = l - startTime;
        if (time < 1000){
            nps = 0;
        }
        else {
            System.out.println("time: " + time + " seconds.");
            final long total = numberOfMovesMade[0] + numberOfQMovesMade[0];
            nps = ((1000 * total) / time);
        }
        if (nps < 0) {
            throw new RuntimeException();
        }
    }

    private static void search(Chessboard board, int depthLimit){
        int depth = 0;
        int aspirationScore = 0;

        int alpha;
        int beta;
        int alphaAspirationAttempts = 0;
        int betaAspirationAttempts = 0;

        alpha = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[alphaAspirationAttempts];
        beta = aspirationScore + EngineSpecifications.ASPIRATION_WINDOWS[betaAspirationAttempts];

        int score;

        everything:
        while (depth < depthLimit){
            depth++;

            int previousAi = rootMoves[0];

            while (true){
                score = principleVariationSearch(board, depth, 0,
                        alpha, beta, 0);

                if (stopNow || stopSearch(startTime, timeLimitMillis)){
                    break everything;
                }

                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    break everything;
                }

                if (score <= alpha) {
                    alphaAspirationAttempts++;
                    if (alphaAspirationAttempts + 1 >= ASPIRATION_MAX_TRIES){
                        alpha = SHORT_MINIMUM;
                    }
                    else {
                        alpha = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[alphaAspirationAttempts];
                    }
                } else if (score >= beta) {
                    betaAspirationAttempts++;
                    if (betaAspirationAttempts + 1 >= ASPIRATION_MAX_TRIES){
                        beta = SHORT_MAXIMUM;
                    }
                    else {
                        beta = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[betaAspirationAttempts];
                    }
                } else {
                    break;
                }
            }

//            boolean info = true;
//            if (info && rootMoves[0] != previousAi){
//                UCIPrinter.sendInfoCommand(board, rootMoves[0], Engine.aiMoveScore, depth);
//            }

            UCIPrinter.sendInfoCommand(board, rootMoves[0], aiMoveScore, depth);

            aspirationScore = score;
        }

        UCIPrinter.sendInfoCommand(board, rootMoves[0], aiMoveScore, depth);
    }


    public static int whichThread = 0;


    static int principleVariationSearch(Chessboard board,
                                        int depth, int ply,
                                        int alpha, int beta,
                                        int nullMoveCounter){

        final int originalAlpha = alpha;

        int[] moves = ply == 0 ? rootMoves : board.generateLegalMoves();

        boolean boardInCheck = board.inCheckRecorder;

        depth += extensions(board, ply, boardInCheck, moves);

        Assert.assertTrue(depth >= 0);

        if (depth <= 0){
            Assert.assertTrue(!board.inCheck(board.isWhiteTurn()));
            return QuiescenceBetter.quiescenceSearchBetter(board, alpha, beta);
        }

        alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
        beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
        if (alpha >= beta){
            return alpha;
        }

        int hashMove = 0;
        int score;

        long previousTableData = retrieveFromTable(board.zobristHash);
        if (previousTableData != 0) {
            score = getScore(previousTableData, ply);
            hashMove = getMove(previousTableData);

            if (getDepth(previousTableData) >= depth){
                int flag = getFlag(previousTableData);
                if (flag == EXACT) {
                    if (ply == 0){
                        if (whichThread == 0){
                            putAIMoveFirst(hashMove);
                            aiMoveScore = score;
                        }
                    }
                    return score;
                }
                else if (flag == LOWERBOUND) {
                    if (score >= beta){
                        if (ply == 0){
                            if (whichThread == 0){
                                putAIMoveFirst(hashMove);
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
                                putAIMoveFirst(hashMove);
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

            staticBoardEval = Evaluator.eval(board, moves);

            if (isBetaRazoringOkHere(depth, staticBoardEval)){
                int specificBetaRazorMargin = betaRazorMargin[depth];
                if (staticBoardEval - specificBetaRazorMargin >= beta){
                    return staticBoardEval;
                }
            }

            if (isAlphaRazoringMoveOkHere(depth, alpha)){
                int specificAlphaRazorMargin = alphaRazorMargin[depth];
                if (staticBoardEval + specificAlphaRazorMargin < alpha){
                    int qScore = QuiescenceBetter.quiescenceSearchBetter(board,
                            alpha - specificAlphaRazorMargin,
                            alpha - specificAlphaRazorMargin + 1);

                    if (qScore + specificAlphaRazorMargin <= alpha){
                        return qScore;
                    }
                }
            }

            int R = 2;
            if (isNullMoveOkHere(board, nullMoveCounter, depth, R)){
                board.makeNullMoveAndFlipTurn();

                int nullScore = -principleVariationSearch(board,
                        depth - R - 1, ply + 1,
                        -beta, -beta + 1, nullMoveCounter + 1);

                board.unMakeNullMoveAndFlipTurn();

                if (nullScore >= beta){
                    if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                        nullScore = beta;
                    }
                    return nullScore;
                }
            }
        }

        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;
        final int lastMove = moves[moves.length - 1];
        if (ply != 0) {
            scoreMoves(moves, board, ply, hashMove);
            Ints.sortDescending(moves, 0, lastMove);
        }

        int numberOfMovesSearched = 0;

        if (ply == 0 && hashMove != 0){
            Assert.assertEquals(moves[0] & MoveOrderer.MOVE_MASK, hashMove);
        }

        for (int i = 0; i < lastMove; i++) {
            if (moves[i] == 0) {
                break;
            }
            int moveScore = MoveOrdererBetter.getMoveScore(moves[i]);

            if (i < lastMove - 1) {
                if (i == 0) {
                    Assert.assertTrue(moveScore >= MoveOrdererBetter.getMoveScore(moves[i + 1]));
                } else {
                    Assert.assertTrue(moveScore <= MoveOrdererBetter.getMoveScore(moves[i - 1]));

                    Assert.assertTrue(moveScore >= MoveOrdererBetter.getMoveScore(moves[i + 1]));
                }
            }


            int move = moves[i] & MoveOrderer.MOVE_MASK;

            Assert.assertTrue(moveScore != 0);

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
                        staticBoardEval = Evaluator.eval(board,
//                                board.generateLegalMoves());
                                moves);
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
            numberOfMovesMade[0]++;
            numberOfMovesSearched++;

            if (board.drawByRepetition(board.isWhiteTurn())) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = alpha + 1;


                int R = 2; //lateMoveDepthReduction(depth, numberOfMovesSearched);

                if (numberOfMovesSearched > 1
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

            if (TimeAllocator.outOfTime(startTime, timeLimitMillis, manageTime)){
                return 0;
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, score);

                if (ply == 0) {
                    if (whichThread == 0){
                        putAIMoveFirst(bestMove);
                        aiMoveScore = score;
                    }
                }
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (numberOfMovesSearched == 0) {
            if (board.inCheckRecorder) {
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

        addToTableReplaceByDepth(board.zobristHash,
                bestMove & MoveOrderer.MOVE_MASK, bestScore, depth, flag, ply);

        return bestScore;
    }


    private static int aiMoveX = 0;
    private static void putAIMoveFirst(int aiMove) {
        aiMoveX = aiMove;
        final int aiMoveMask = aiMove & MOVE_MASK;
        if ((rootMoves[0] & MOVE_MASK) == aiMoveMask) {
            return;
        }

        final int maxMoves = rootMoves.length - 1;
        for (int i = 0; i < maxMoves; i++) {
            final int rootMove = rootMoves[i] & MOVE_MASK;
            if (rootMove == aiMove || rootMove == aiMoveMask) {
                Assert.assertTrue(i != 0);
                System.arraycopy(rootMoves, 0,
                        rootMoves, 1, i);
                rootMoves[0] = MoveOrdererBetter.buildMoveScore(aiMoveMask, hashScore);
                break;
            }
        }
    }
}
