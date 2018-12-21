package com.github.louism33.axolotl.moveordering;

import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import org.junit.Assert;

import static com.github.louism33.axolotl.moveordering.MoveOrderingConstants.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.chesscore.MoveParser.*;

public class MoveOrderer {

    private static boolean ready = false;
    private static int[][] mateKillers;
    private static int[][][] killerMoves;
    private static int[][][] historyMoves;

    public static final int MOVE_MASK = ~MOVE_SCORE_MASK;

    public static void initMoveOrderer(){
        mateKillers = new int[THREAD_NUMBER][128];
        killerMoves = new int[THREAD_NUMBER][128][2];
        historyMoves = new int[THREAD_NUMBER][64][64];
        ready = true;
    }
    
    public static int getMoveScore (int moveScore){
        Assert.assertTrue(moveScore > 0);
        int i = (moveScore & MOVE_SCORE_MASK) >>> moveScoreOffset;
        Assert.assertTrue(i > 0);
        return i;
    }

    static int buildMoveScore(int move, int score){
        Assert.assertTrue(move > 0);

        Assert.assertTrue(score > 0);

        int i1 = score << moveScoreOffset;

        Assert.assertTrue(i1 > 0);
        int i = move | i1;

        Assert.assertTrue(i > 0);
        return i;
    }
    
    public static void scoreMoves(int whichThread, int[] moves, Chessboard board, int ply,
                                  int hashMove){

        if (!ready) {
            initMoveOrderer();
        }
        try {
            scoreMovesHelper(whichThread, moves, board, ply, hashMove);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }
    }

    private static void scoreMovesHelper(int whichThread, int[] moves, Chessboard board, int ply,
                                         int hashMove) throws IllegalUnmakeException {

        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0){
                break;
            }

            int move = moves[i];

            Assert.assertTrue(move < MOVE_SIZE_LIMIT);

            if (move == hashMove) {
                moves[i] = buildMoveScore(moves[i], hashScore);
            }
            else if (mateKillers[whichThread][ply] != 0 && moves[i] == mateKillers[whichThread][ply]) {
                Assert.assertTrue(mateKillers[whichThread][ply] < MOVE_SIZE_LIMIT);
                moves[i] = buildMoveScore(moves[i], mateKillerScore);
            }
            else if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                moves[i] = buildMoveScore(moves[i], CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(moves[i]));
            }
            else if (isPromotionToQueen(moves[i])) {
                if (isCaptureMove(moves[i])) {
                    moves[i] = buildMoveScore(moves[i], queenCapturePromotionScore);
                }
                else {
                    moves[i] = buildMoveScore(moves[i], queenQuietPromotionScore);
                }
            }
            else if (isPromotionToKnight(moves[i])) {
                moves[i] = buildMoveScore(moves[i], knightPromotionScore);
            }
            else if (isPromotionToBishop(moves[i]) || isPromotionToRook(moves[i])) {
                moves[i] = buildMoveScore(moves[i], uninterestingPromotion);
            }
            else if (isCaptureMove(moves[i])) {
                moves[i] = buildMoveScore(moves[i], mvvLVA(moves[i]));
            }
            else if (killerMoves[whichThread][ply][0] != 0 && killerMoves[whichThread][ply][0] == moves[i]) {
                Assert.assertTrue(killerMoves[whichThread][ply][0] < MOVE_SIZE_LIMIT);
                moves[i] = buildMoveScore(moves[i], killerOneScore);
            }
            else if (killerMoves[whichThread][ply][1] != 0 && killerMoves[whichThread][ply][1] == moves[i]) {
                Assert.assertTrue(killerMoves[whichThread][ply][1] < MOVE_SIZE_LIMIT);
                moves[i] = buildMoveScore(moves[i], killerTwoScore);
            }
            else if (ply >= 2 && killerMoves[whichThread][ply - 2][0] != 0 
                    && killerMoves[whichThread][ply - 2][0] == moves[i]) {
                Assert.assertTrue(killerMoves[whichThread][ply - 2][0] < MOVE_SIZE_LIMIT);
                moves[i] = buildMoveScore(moves[i], oldKillerScoreOne);
            }
            else if (ply >= 2 && killerMoves[whichThread][ply - 2][1] != 0 
                    && killerMoves[whichThread][ply - 2][1] == (moves[i])) {
                Assert.assertTrue(killerMoves[whichThread][ply - 2][1] < MOVE_SIZE_LIMIT);
                moves[i] = buildMoveScore(moves[i], oldKillerScoreTwo);
            }
            else if (checkingMove(board, moves[i])) {
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
            }
            else if (isCastlingMove(moves[i])) {
                moves[i] = buildMoveScore(moves[i], castlingMove);
            }
            else {
                moves[i] = buildMoveScore(moves[i], 
                        Math.max(historyMoveScore(whichThread, moves[i]), uninterestingMove));
            }
        }
    }

    private static int mvvLVA(int move){
        int sourceScore = scoreByPiece(move, getMovingPieceInt(move));
        int destinationScore = scoreByPiece(move, getVictimPieceInt(move));
        return CAPTURE_BIAS + destinationScore - sourceScore;
    }

    private static int scoreByPiece(int move, int piece){
        switch (piece){
            case NO_PIECE:
                return 0;
            case WHITE_PAWN:
            case BLACK_PAWN:
                return 1;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                return 3;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return 3;
            case WHITE_ROOK:
            case BLACK_ROOK:
                return 5;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return 9;
            case WHITE_KING:
            case BLACK_KING:
                return 10;
            default:
                throw new RuntimeException("score by piece problem "+ move);
        }
    }

    /*
    Quiescence Search ordering:
    order moves by most valuable victim and least valuable aggressor
     */
    public static void scoreMovesQuiescence(int[] moves, Chessboard board){
        scoreMovesQuiescenceHelper(moves, board);
    }

    private static void scoreMovesQuiescenceHelper(int[] moves, Chessboard board){
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0){
                break;
            }

            int move = moves[i];

            if (isCaptureMove(move)) {
                if (isPromotionMove(move) && isPromotionToQueen(move)) {
                    moves[i] = buildMoveScore(move, queenCapturePromotionScore);
                } else if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                    moves[i] = buildMoveScore(move, CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(moves[i]));
                } else {
                    moves[i] = buildMoveScore(move, mvvLVA(moves[i]));
                }
            } else if (isPromotionMove(move) && (isPromotionToQueen(moves[i]))) {
                moves[i] = buildMoveScore(moves[i], queenQuietPromotionScore);
            }
            else {
                moves[i] = 0;
            }
        }
    }

    public static boolean checkingMove(Chessboard board, int move) throws IllegalUnmakeException {
        board.makeMoveAndFlipTurn(move);
        boolean checkingMove = board.inCheck(board.isWhiteTurn());
        board.unMakeMoveAndFlipTurn();
        return checkingMove;
    }

    public static void updateHistoryMoves(int whichThread, int move, int ply){
        historyMoves[whichThread][getSourceIndex(move)][getDestinationIndex(move)] += (2 * ply);
    }

    private static int historyMoveScore(int whichThread, int move){
        int maxMoveScoreOfHistory = MAX_HISTORY_MOVE_SCORE;
        int historyScore = historyMoves[whichThread][getSourceIndex(move)][getDestinationIndex(move)];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }


    public static void updateKillerMoves(int whichThread, int move, int ply){

        Assert.assertTrue(move < MOVE_SIZE_LIMIT);
        
        if (move != killerMoves[whichThread][ply][0]){
            if (killerMoves[whichThread][ply][0] != 0) {
                killerMoves[whichThread][ply][1] = killerMoves[whichThread][ply][0];
            }
            killerMoves[whichThread][ply][0] = move;
        }
    }

    public static void updateMateKillerMoves(int whichThread, int move, int ply){
        mateKillers[whichThread][ply] = move;
    }


}
