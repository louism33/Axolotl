package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.helper.protocolhelperclasses.PVLine;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.main.UCIPrinter;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.moveordering.MoveOrderingConstants;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.updateKillerMoves;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.updateMateKillerMoves;
import static com.github.louism33.axolotl.search.EngineSpecifications.ASPIRATION_MAX_TRIES;
import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_DEPTH;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.allocateTime;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.outOfTime;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

public class Engine {

    private static int aiMove;
    private static int aiMoveScore;
    private static boolean isReady = false;
    private static boolean stopInstruction = false;
    private static long nps;
    public static long regularMovesMade;
    public static long quiescentMovesMade;
    private static long startTime = 0;

    private static UCIEntry uciEntry;

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

    static int getAiMoveScore() {
        return aiMoveScore;
    }

    private static void calculateNPS(){
        long time = System.currentTimeMillis() - startTime;
        if (time < 1000){
            nps = 0;
        }
        else {
            nps = ((1000 * (regularMovesMade + quiescentMovesMade)) / time);
        }
    }

    private static boolean stopSearch(long startTime, long timeLimiMillis, int depth, int maxDepth) {
        return Engine.isStopInstruction()
                || (EngineSpecifications.ALLOW_TIME_LIMIT && outOfTime(startTime, timeLimiMillis))
                || (!EngineSpecifications.ALLOW_TIME_LIMIT && (depth >= maxDepth));
    }

    public static void setup() {
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
    }

    public static int searchFixedDepth(Chessboard board, int depth) {
        EngineSpecifications.ALLOW_TIME_LIMIT = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0);
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

        return aiMove & MoveOrderer.MOVE_MASK;
    }

    private static void iterativeDeepeningWithAspirationWindows(Chessboard board, long startTime, long timeLimitMillis) throws IllegalUnmakeException {
        int depth = 0;
        int aspirationScore = 0;

        while (!stopSearch(startTime, timeLimitMillis, depth, MAX_DEPTH)) {
            depth++;

            int score;

            score = aspirationSearch(board, depth, aspirationScore);

            if (EngineSpecifications.INFO && depth > 6){
                UCIPrinter.sendInfoCommand(aiMove, aiMoveScore, depth);
            }

            aspirationScore = score;

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                break;
            }
        }
    }

    private static int aspirationSearch(Chessboard board, int depth, int aspirationScore) throws IllegalUnmakeException {

        int alpha;
        int beta;
        int alphaAspirationAttempts = 0;
        int betaAspirationAttempts = 0;

        alpha = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[alphaAspirationAttempts];
        beta = aspirationScore + EngineSpecifications.ASPIRATION_WINDOWS[betaAspirationAttempts];

        int score;

        while (true) {
            score = principleVariationSearch(board, depth, 0, alpha, beta, 0);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                return score;
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
        return score;
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

        long previousTableData = TranspositionTable.retrieveFromTable(board.getZobrist());
        if (previousTableData != 0) {
            score = TranspositionTable.getScore(previousTableData, ply);
            hashMove = TranspositionTable.getMove(previousTableData);

            if (TranspositionTable.getDepth(previousTableData) >= depth
                    && PVLine.verifyMove(board, hashMove, moves)){
                int flag = TranspositionTable.getFlag(previousTableData);
                if (flag == EXACT) {
                    if (ply == 0){
                        setAiMove(hashMove);
                        aiMoveScore = score;
                    }
                    return score;
                } 
                else if (flag == LOWERBOUND) {
                    if (score >= beta){
                        if (ply == 0){
                            setAiMove(hashMove);
                            aiMoveScore = score;
                        }
                        return score;
                    }
                } 
                else if (flag == UPPERBOUND) {
                    if (score <= alpha){
                        if (ply == 0){
                            setAiMove(hashMove);
                            aiMoveScore = score;
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
                            alpha - specificAlphaRazorMargin + 1);

                    if (qScore + specificAlphaRazorMargin <= alpha){
                        return qScore;
                    }
                }
            }


            int R = nullMoveDepthReduction();
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

        MoveOrderer.scoreMoves(moves, board, ply, hashMove);

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
                            && numberOfMovesSearched >= depth * 3 + 4) {
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
            regularMovesMade++;
            numberOfMovesSearched++;

            if (board.drawByRepetition(board.isWhiteTurn())) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = alpha + 1;

                int R = lateMoveDepthReduction();

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

        Assert.assertTrue(bestMove != 0);

        int flag;
        if (bestScore <= originalAlpha){
            flag = UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = LOWERBOUND;
        } else {
            flag = EXACT;
        }

        TranspositionTable.addToTableAlwaysReplace(board.getZobrist(),
                TranspositionTable.buildTableEntry(bestMove & MoveOrderer.MOVE_MASK, bestScore, depth, flag, ply));


        return bestScore;
    }
}