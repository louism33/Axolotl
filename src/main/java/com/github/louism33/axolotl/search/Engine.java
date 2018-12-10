package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.helper.protocolhelperclasses.PVLine;
import com.github.louism33.axolotl.helper.timemanagement.TimeAllocator;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
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
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;

public class Engine {

    public static int flipflop = 0;
    public static int realisticflipflop = 0;

    private static int aiMove;
    private static int aiMoveScore;
    private static boolean isReady = false;
    private static boolean stopInstruction = false;
    public static long nps;
    public static long regularMovesMade;
    public static long quiescentMovesMade;
    public static long startTime = 0;
    private static UCIEntry uciEntry;

    public static int getAiMove() {
        return aiMove;
    }

    private static void setAiMove(int aiMove) {
        flipflop++;
        Engine.aiMove = aiMove;
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
                || (!EngineSpecifications.ALLOW_TIME_LIMIT && (depth > maxDepth));
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
        flipflop = 0;
        realisticflipflop = 0;
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
        int aspirationScore = 0;
        int depth = 0;


        while (!stopSearch(startTime, timeLimitMillis, depth, MAX_DEPTH)) {

            int preAI = aiMove;

            System.out.println("----- Depth: " + depth + ", aiMove at start: " + MoveParser.toString(aiMove));

            int score = aspirationSearch(board, startTime, timeLimitMillis, depth, aspirationScore);

            System.out.println("ai move at end: " + MoveParser.toString(aiMove));
            System.out.println();

            if (preAI != aiMove){
                realisticflipflop++;
            }

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

            TimeAllocator.printManager(board, false);

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


        int originalAlpha = alpha;
        int[] moves = board.generateLegalMoves();
        boolean boardInCheck = board.inCheckRecorder;

        Assert.assertEquals(boardInCheck, board.inCheck(board.isWhiteTurn()));

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

        long previousTableData = TranspositionTable.retrieveFromTable(board.getZobrist());
        if (previousTableData != 0) {
            score = TranspositionTable.getScore(previousTableData);
            hashMove = TranspositionTable.getMove(previousTableData);
            if (TranspositionTable.getDepth(previousTableData) >= depth
                    && PVLine.verifyMove(board, hashMove, moves)){
                moves = board.generateLegalMoves();
                int flag = TranspositionTable.getFlag(previousTableData);
                if (flag == EXACT) {
                    if (ply == 0){
//                            System.out.println("TTExact, change aiMove from " + MoveParser.toString(aiMove) + " to " +MoveParser.toString(hashMove));
                        setAiMove(hashMove);
                        aiMoveScore = score;
                    }
                    return score;
                } else if (flag == LOWERBOUND) {
                    alpha = Math.max(alpha, score);
                } else if (flag == UPPERBOUND) {
                    beta = Math.min(beta, score);
                }
                if (alpha >= beta) {
                    if (ply == 0){
//                            System.out.println("TT AB, change aiMove from " + MoveParser.toString(aiMove) + " to " +MoveParser.toString(hashMove));
                        setAiMove(hashMove);
                        aiMoveScore = score;
                    }
                    return score;
                }
            }
            else {
                hashMove = 0;
                score = 0;
            }
        }



        int staticBoardEval = SHORT_MINIMUM;

        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        if (!thisIsAPrincipleVariationNode && !boardInCheck) {
            if (moves == null){
                moves = board.generateLegalMoves();
            }
            staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(), moves);
            
            
            /*
            null move pruning
             */
            if (nullMoveCounter < 2 && depth > 3 && !(populationCount(board.allPieces()) < 9)){
                int R = 2;

                Chessboard initial = new Chessboard(board);

                board.makeNullMoveAndFlipTurn();

                int nullScore = -principleVariationSearch(board,
                        startTime, timeLimitMillis,
                        originalDepth, depth - R - 1, ply + 1,
                        -beta, -beta + 1, nullMoveCounter + 1, false);

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

        if (moves == null){
            moves = board.generateLegalMoves();
        }

//        MoveParser.printMoves(moves);

        if (ply == 0 && aiMove != 0 && hashMove != 0){
            Assert.assertEquals(aiMove, hashMove);
        }

        MoveOrderer.scoreMoves(moves, board, board.isWhiteTurn(), ply, hashMove);

//        MoveParser.printMoves(moves);

        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;
        int realMoves = MoveParser.numberOfRealMoves(moves);
        Ints.sortDescending(moves, 0, realMoves);
        int numberOfMovesSearched = 0;

//        MoveParser.printMoves(moves);




        if (hashMove != 0){
//        PVLine.verifyMove(board, hashMove, moves)){
            if ((moves[0] & MoveOrderer.MOVE_MASK) != hashMove){
                int fmove = moves[0];
                int firstMove = fmove & MoveOrderer.MOVE_MASK;
                System.out.println("hash move      : " + MoveParser.toString(hashMove) +"   "+ hashMove);
                System.out.println("first 2 search : " + MoveParser.toString(firstMove)+"   " + firstMove);
                System.out.println("Is it contained? "+PVLine.verifyMove(board, hashMove, moves));
                MoveParser.printMoves(moves);
                System.out.println(board);
            }
            Assert.assertEquals((moves[0] & MoveOrderer.MOVE_MASK), hashMove);
        }



        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0) {
                break;
            }



            int move = moves[i];
            int moveScore = MoveOrderer.getMoveScore(moves[i]);

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
                    
                    
                    /*
                    late move reductions
                     */
                    if (depth > 2 && numberOfMovesSearched > 3 && !captureMove && !promotionMove && !pawnToSeven && !boardInCheck && !givesCheckMove) {

                        int R = 2;

                        score = -principleVariationSearch(board,
                                startTime, timeLimitMillis,
                                originalDepth, depth - R - 1, ply + 1,
                                -alpha - 1, -alpha, nullMoveCounter, true);


                        if (score > alpha) {

                            score = -principleVariationSearch(board,
                                    startTime, timeLimitMillis,
                                    originalDepth, depth - 1, ply + 1,
                                    -alpha - 1, -alpha, 0, false);

                        }
                    }
                    /*
                    principle variation
                     */
                    else {
                        score = -principleVariationSearch(board,
                                startTime, timeLimitMillis,
                                originalDepth, depth - 1, ply + 1,
                                -alpha - 1, -alpha, 0, reducedSearch);

                    }
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
                bestMove = move & MoveOrderer.MOVE_MASK;
                alpha = Math.max(alpha, score);
                if (ply == 0) {

//                    System.out.println("      AI score improvement, change aiMove from " + MoveParser.toString(aiMove) + " to " +MoveParser.toString(move));
//                    System.out.println("    aiscore was " + aiMoveScore +" is now " + score + " (bestscore was "+ plop+")");
//                    
                    setAiMove(bestMove);
                    aiMoveScore = score;
                }
            }

            if (alpha >= beta) {
                int justMove = move & MoveOrderer.MOVE_MASK;
                if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    updateMateKillerMoves(justMove, ply);
                } else {
                    updateKillerMoves(justMove, ply);
                }

                MoveOrderer.updateHistoryMoves(justMove, ply);

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