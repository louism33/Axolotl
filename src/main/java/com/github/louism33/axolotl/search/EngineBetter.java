package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.main.UCIPrinter;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.nio.charset.CharacterCodingException;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.Engine.stopSearch;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.MoveOrdererBetter.*;
import static com.github.louism33.axolotl.search.MoveOrdererBetter.MOVE_MASK;
import static com.github.louism33.axolotl.search.SearchUtils.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.allocateTime;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.outOfTime;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

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

        stopNow = false;

        initTable(TABLE_SIZE);

        MoveOrderer.initMoveOrderer();
    }

    public static void main(String[] args) {

        boolean b = false;

        if (!b) {
            final long l1 = System.currentTimeMillis();
            Chessboard board = new Chessboard();

            final int move = searchFixedTime(board, 10_000);

            MoveParser.printMove(move);

            final long l2 = System.currentTimeMillis();
            System.out.println("***********************************");
            System.out.println("time taken: " + (l2 - l1)/1000);
        }

        if (b) {

            String wac = "1rb2k2/pp3ppQ/7q/2p1n1N1/2p5/2N5/P3BP1P/K2R4 w - - 1 0 bm Qg8+;";
//        wac = "1rb2kQ1/pp3pp1/7q/2p1n1N1/2p5/2N5/P3BP1P/K2R4 b - - 1 0 bm kg8";
            int depth = 4;
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(wac);

            final Chessboard board = EPDObject.getBoard();
            System.out.println(board);
            System.out.println(EPDObject.getBoardFen());
            int[] bestMoves = EPDObject.getBestMoves();
            EngineSpecifications.INFO = false;
            int move = searchFixedDepth(board, depth);

            System.out.println("best moves:");
            MoveParser.printMove(bestMoves);
            final String s = MoveParser.toString(move);
            System.out.println("move found: " + s);

            if (contains(bestMoves, move & MOVE_MASK)){
                System.out.println("success");
            }
            else {
                System.out.println("failure");
            }
        }

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

        startTime = System.currentTimeMillis();

        rootMoves = board.generateLegalMoves();
        aiMoveX = rootMoves[0];

//        System.out.println("++++++++++++++");
//        System.out.println(rootMoves[rootMoves.length - 1]);
//        MoveParser.printMove(rootMoves);
//        System.out.println("++++++++++++++");

        int numberOfRealMoves = rootMoves[rootMoves.length - 1];
        if (numberOfRealMoves == 0 || numberOfRealMoves == 1){
            return rootMoves[0] & MOVE_MASK;
        }

        scoreMovesAtRoot(rootMoves, numberOfRealMoves, board);
        Ints.sortDescending(rootMoves, 0, numberOfRealMoves);

        search(board, maxTime, depth);

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
        if (bestMove != actual) {

            System.out.println("root");
            MoveParser.printMove(rootMoves[0]);
            System.out.println("ai recoreder");
            MoveParser.printMove(aiMoveX);

            Art.printLong(rootMoves[0]);
            Art.printLong(aiMoveX);

            Assert.assertEquals(bestMove, actual);
        }
        return bestMove;
    }

    public static void calculateNPS(){
        final long l = System.currentTimeMillis();
        long time = l - startTime;
        if (time < 1000){
            nps = 0;
        }
        else {
            System.out.println("time: " + time);
            nps = ((1000 * numberOfMovesMade[0]) / time);
        }
        if (nps < 0) {
            throw new RuntimeException();
        }
    }

    public static void search(Chessboard board, long timeLimitMillis, int depthLimit){
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
                        alpha, beta, 0, timeLimitMillis);

                if (stopNow || stopSearch(startTime, timeLimitMillis)){
                    System.out.println("ASPIRATION STOP, stopnow: " + stopNow + ", stopSearch: " + stopSearch(startTime, timeLimitMillis));
                    break everything;
                }

                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    System.out.println("score above CM, score: " + score +"    and limit is " + CHECKMATE_ENEMY_SCORE_MAX_PLY);
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
            System.out.println(MoveParser.toString(rootMoves[0]) + " with score " + aiMoveScore);
            UCIPrinter.sendInfoCommand(board, rootMoves[0], aiMoveScore, depth);

            aspirationScore = score;
        }

        System.out.println(MoveParser.toString(rootMoves[0]) + " with score " + aiMoveScore);
        UCIPrinter.sendInfoCommand(board, rootMoves[0], aiMoveScore, depth);
    }


    public static int whichThread = 0;


    static int principleVariationSearch(Chessboard board,
                                        int depth, int ply,
                                        int alpha, int beta,
                                        int nullMoveCounter,
                                        long timeLimitMillis){

        final int originalAlpha = alpha;

        int[] moves = ply == 0 ? rootMoves : board.generateLegalMoves();

        boolean boardInCheck = board.inCheckRecorder;

        depth += extensions(board, ply, boardInCheck, moves);

        Assert.assertTrue(depth >= 0);

        if (depth <= 0){
//            Assert.assertTrue(!board.inCheck(board.isWhiteTurn()));
            return 0;
//            return Evaluator.eval(board, board.isWhiteTurn(), moves);
//            return QuiescenceBetter.quiescenceSearchBetter(board, alpha, beta);
        }

        alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
        beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
        if (alpha >= beta){
            return alpha;
        }

        int hashMove = 0;
        int score;


        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);

        int staticBoardEval = SHORT_MINIMUM;


        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;
        final int lastMove = moves[moves.length - 1];
        if (ply != 0) {
            scoreMoves(moves, board, ply, hashMove);
            Ints.sortDescending(moves, 0, lastMove);
        }

        int numberOfMovesSearched = 0;

//        if (ply == 0 && hashMove != 0){
//            Assert.assertEquals(moves[0] & MoveOrderer.MOVE_MASK, hashMove);
//        }

//        System.out.println("last move is: " + lastMove);
        for (int i = 0; i < lastMove; i++) {
            if (moves[i] == 0) {
                break;
            }

            int move = moves[i] & MoveOrderer.MOVE_MASK;

            board.makeMoveAndFlipTurn(move);
            numberOfMovesMade[0]++;
            numberOfMovesSearched++;

            if (board.drawByRepetition(board.isWhiteTurn())) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = alpha + 1;

                if (numberOfMovesSearched > 1 && score > alpha) {
                    score = -principleVariationSearch(board,
                            depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0, timeLimitMillis);
                }

                if (score > alpha) {
                    score = -principleVariationSearch(board,
                            depth - 1, ply + 1,
                            -beta, -alpha, 0, timeLimitMillis);
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

        return bestScore;
    }


    private static int aiMoveX = 0;
    private static void putAIMoveFirst(int aiMove) {
        aiMoveX = aiMove;
        final int aiMoveMask = aiMove & MOVE_MASK;
        if ((rootMoves[0] & MOVE_MASK) == aiMoveMask) {
            return;
        }

//        System.out.println("ai move was ");
//        MoveParser.printMove(rootMoves[0]);
//        System.out.println("is now");
//        MoveParser.printMove(aiMove);
//        MoveParser.printMove(rootMoves);

        final int maxMoves = rootMoves.length - 1;
        for (int i = 0; i < maxMoves; i++) {
            final int rootMove = rootMoves[i] & MOVE_MASK;
            if (rootMove == aiMove || rootMove == aiMoveMask) {
                Assert.assertTrue(i != 0);
                System.arraycopy(rootMoves, 0,
                        rootMoves, 1, i);
                rootMoves[0] = rootMove;
                break;
            }
        }
//        MoveParser.printMove(rootMoves);
    }
}
