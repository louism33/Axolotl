package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.helper.timemanagement.TimeAllocator;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.helper.timemanagement.TimeAllocator.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_DEPTH;

public class Engine {

    private static int aiMove;
    private static int aiMoveScore;
    private static boolean isReady = false;
    private static boolean stopInstruction = false;
    public static long nps;
    public static long regularMovesMade;
    public static long quiescentMovesMade;

    public static int getAiMove() {
        return aiMove;
    }

    public static boolean isStopInstruction() {
        return stopInstruction;
    }

    public static void setStopInstruction(boolean instruction) {
        stopInstruction = instruction;
    }

    public static int getAiMoveScore() {
        return aiMoveScore;
    }

    private static boolean stopSearch(long startTime, long timeLimiMillis, int depth, int maxDepth) {
        return Engine.isStopInstruction()
                || (EngineSpecifications.ALLOW_TIME_LIMIT && outOfTime(startTime, timeLimiMillis))
                || (!EngineSpecifications.ALLOW_TIME_LIMIT && (depth > maxDepth));
    }

    public static void setup() {
        TranspositionTable.initTable(EngineSpecifications.DEFAULT_TABLE_SIZE);
        stopInstruction = false;
        isReady = true;
        nps = 0;
    }

    private static void reset() {
        nps = 0;
        regularMovesMade = 0;
        stopInstruction = false;
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

        long startTime = System.currentTimeMillis();

        try {
            iterativeDeepeningWithAspirationWindows(board, startTime, maxTime);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;
        
        if (time != 0) {
            nps = ((1000 * (regularMovesMade + quiescentMovesMade)) / time);
        }

        return aiMove & MoveOrderer.MOVE_MASK;
    }

    private static void iterativeDeepeningWithAspirationWindows(Chessboard board, long startTime, long timeLimitMillis) throws IllegalUnmakeException {
        int aspirationScore = 0;
        int depth = 0;

        while (!stopSearch(startTime, timeLimitMillis, depth, MAX_DEPTH)) {

            int score = aspirationSearch(board, startTime, timeLimitMillis, depth, aspirationScore);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                break;
            }

            aspirationScore = score;
            depth++;
        }
    }

    private static int aspirationSearch(Chessboard board, long startTime, long timeLimitMillis,
                                        int depth, int aspirationScore) throws IllegalUnmakeException {
        int firstWindow = 50, alpha, beta;

        alpha = aspirationScore - firstWindow;
        beta = aspirationScore + firstWindow;

        int score;

        while (true) {
            score = principleVariationSearch(board,
                    startTime, timeLimitMillis,
                    depth, depth, 0, alpha, beta, 0, false);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                return score;
            }

            if (outOfTime(startTime, timeLimitMillis)) {
                return score;
            }

            if (score <= alpha) {
                alpha = SHORT_MINIMUM;
            } else if (score >= beta) {
                beta = SHORT_MAXIMUM;
            } else {
                break;
            }
        }
        return score;
    }


    private static int principleVariationSearch(Chessboard board,
                                                long startTime, long timeLimitMillis,
                                                int originalDepth, int depth, int ply,
                                                int alpha, int beta,
                                                int nullMoveCounter, boolean reducedSearch) throws IllegalUnmakeException {

        boolean boardInCheck = board.inCheck(board.isWhiteTurn());

        int[] moves = null;

        depth += Extensions.extensions(board, ply, boardInCheck);

        Assert.assertTrue(depth >= 0);

        if (outOfTime(startTime, timeLimitMillis) || Engine.isStopInstruction()) {
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }

        if (depth <= 0){
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }

        alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
        beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
        if (alpha >= beta){
            return alpha;
        }

        int hashMove = 0;
        int score;
        int staticBoardEval = SHORT_MINIMUM;

        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        if (!thisIsAPrincipleVariationNode && !boardInCheck) {
            if (moves == null){
                moves = board.generateLegalMoves();
            }
            staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(), moves);
        }

        if (moves == null){
            moves = board.generateLegalMoves();
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

            int move = moves[i];

            if (i == 0){
                Assert.assertTrue(move >= moves[i+1]);
            } else {
                Assert.assertTrue(move <= moves[i - 1]);
                Assert.assertTrue(move >= moves[i + 1]);
            }

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

                if (numberOfMovesSearched > 1) {

                    score = -principleVariationSearch(board,
                            startTime, timeLimitMillis,
                            originalDepth, depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0, reducedSearch);

                }

                if (score > alpha) {
                    score = -principleVariationSearch(board,
                            startTime, timeLimitMillis,
                            originalDepth, depth - 1, ply + 1,
                            -beta, -alpha, 0, false);
                }
            }

            board.unMakeMoveAndFlipTurn();
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, score);
                if (ply == 0) {
                    aiMoveScore = score;
                    aiMove = move;
                }
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (numberOfMovesSearched == 0) {
            if (boardInCheck) {
                return IN_CHECKMATE_SCORE + ply;
            }
            else {
                return IN_STALEMATE_SCORE;
            }
        }

        return bestScore;
    }
}