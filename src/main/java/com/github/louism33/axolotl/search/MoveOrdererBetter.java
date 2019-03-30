package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveConstants;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_DEPTH_HARD;
import static com.github.louism33.axolotl.search.EngineSpecifications.THREAD_NUMBER;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.getMove;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.retrieveFromTable;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;

public final class MoveOrdererBetter {


    public static boolean ready = false;
    public static int[][] mateKillers;
    public static int[][][] killerMoves;
    public static int[][][] historyMoves;

    public static final int whichThread = 0;

    public static void initMoveOrderer(){
        mateKillers = new int[THREAD_NUMBER][MAX_DEPTH_HARD];
        killerMoves = new int[THREAD_NUMBER][MAX_DEPTH_HARD][2];
        historyMoves = new int[THREAD_NUMBER * 2][64][64]; // one for each side to move. number of squares ** 2
        ready = true;
    }

    public static void resetMoveOrderer(){
        Assert.assertTrue(ready);
        for (int i = 0; i < THREAD_NUMBER; i++) {
            Arrays.fill(mateKillers[i], 0);
            for (int d = 0; d < MAX_DEPTH_HARD; d++) {
                Arrays.fill(killerMoves[i][d], 0);
            }
        }
    }

    public static int getMoveScore (int move){
        Assert.assertTrue(move > 0);
        int i = (move & MOVE_SCORE_MASK) >>> moveScoreOffset;
        return i;
    }

    public static int buildMoveScore(int move, int score){
        Assert.assertTrue(move > 0);

        Assert.assertTrue(score > 0);

        int i1 = score << moveScoreOffset;

        if (i1 < 0) {
            System.out.println(isCaptureMove(move));
            MoveParser.printMove(move);
            System.out.println(move + "    "+ score);
            Art.printLong(move);
            Art.printLong(i1);
        }

        Assert.assertTrue(i1 > 0);
        int i = move | i1;

        Assert.assertTrue(i > 0);
        return i;
    }

    public static void scoreMovesAtRoot(int[] moves, int numberOfMoves, Chessboard board){
        if (!ready) {
            initMoveOrderer();
        }

        for (int i = 0; i < numberOfMoves; i++) {
            if (moves[i] == 0){
                break;
            }

            if (isPromotionToQueen(moves[i])) {
                if (isCaptureMove(moves[i])) {
                    moves[i] = buildMoveScore(moves[i], queenCapturePromotionScore);
                }
                else {
                    moves[i] = buildMoveScore(moves[i], queenQuietPromotionScore);
                }
            }
            else if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                moves[i] = buildMoveScore(moves[i], captureBiasOfLastMovedPiece + mvvLVA(moves[i]));
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

            else if (checkingMove(board, moves[i])) {
                moves[i] = MoveParser.setCheckingMove(moves[i]);
                Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
            }

            else if (isCastlingMove(moves[i])) {
                moves[i] = buildMoveScore(moves[i], castlingMove);
            }
            else {

                moves[i] = buildMoveScore(moves[i], quietHeuristicMoveScore(moves[i], board.turn, maxRootQuietScore));
            }
        }
    }
    

    public static void scoreMovesAtRootNEW(int[] moves, int numberOfMoves, Chessboard board){
        if (!ready) {
            initMoveOrderer();
        }

        int hashMove = getMove(retrieveFromTable(board.zobristHash));
//        int hashMove = 0;

        for (int i = 0; i < numberOfMoves; i++) {
            if (moves[i] == 0){
                break;
            }

            int move = moves[i];

            if (move == hashMove) {
                moves[i] = buildMoveScore(move, hashScore);
            } else if (isPromotionToQueen(move)) {
                if (isCaptureMove(move)) {
                    moves[i] = buildMoveScore(move, queenCapturePromotionScore);
                } else {
                    moves[i] = buildMoveScore(move, queenQuietPromotionScore);
                }
            } else if (board.moveIsCaptureOfLastMovePiece(move)) {
                moves[i] = buildMoveScore(move, captureBiasOfLastMovedPiece + mvvLVA(move));
            } else if (isPromotionToKnight(move)) {
                moves[i] = buildMoveScore(move, knightPromotionScore);
            } else if (isPromotionToBishop(move) || isPromotionToRook(move)) {
                moves[i] = buildMoveScore(move, uninterestingPromotion);
            } else if (isCaptureMove(move)) {
                moves[i] = buildMoveScore(move, mvvLVA(move));
            } else if (checkingMove(board, moves[i])) {
                moves[i] = MoveParser.setCheckingMove(moves[i]);
                Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
            } else if (isCastlingMove(move)) {
                moves[i] = buildMoveScore(move, castlingMove);
            } else {

                moves[i] = buildMoveScore(move, quietHeuristicMoveScore(move, board.turn, maxRootQuietScore));  
                
            }
        }
    }

    public static void scoreMoves(int[] moves, Chessboard board, int ply,
                                  int hashMove){
        if (!ready) {
            initMoveOrderer();
        }
        scoreMovesHelper(moves, board, ply, hashMove);
    }

    public static void scoreMovesHelper(int[] moves, Chessboard board, int ply,
                                        int hashMove){

        int maxMoves = moves[moves.length - 1];
        int turn = board.turn;

        // todo
        int mateKiller = 0;
        int firstKiller = 0;
        int secondKiller = 0;
        int firstOldKiller = 0;
        int secondOldKiller = 0;

        if (ply < MAX_DEPTH_HARD) {
            mateKiller = mateKillers[whichThread][ply];
            firstKiller = killerMoves[whichThread][ply][0];
            secondKiller = ply >= 2 ? killerMoves[whichThread][ply][1] : 0;
            firstOldKiller = ply >= 2 ? killerMoves[whichThread][ply - 2][0] : 0;
            secondOldKiller = ply >= 2 ? killerMoves[whichThread][ply - 2][1] : 0;
        }

        for (int i = 0; i < maxMoves; i++) {
            if (moves[i] == 0){
                break;
            }

            moves[i] = moves[i] & MOVE_MASK_WITH_CHECK;

            int move = moves[i];

            Assert.assertTrue(move < MoveConstants.FIRST_FREE_BIT);
            Assert.assertTrue(hashMove < MoveConstants.FIRST_FREE_BIT);

            final boolean captureMove = isCaptureMove(move);

            if (move == hashMove) {
                moves[i] = buildMoveScore(move, hashScore);
            }
            else if (isPromotionToQueen(move)) {
                if (isCaptureMove(move)) {
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
            else if (captureMove) {
                if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                    moves[i] = buildMoveScore(moves[i], captureBiasOfLastMovedPiece + mvvLVA(moves[i]));
                }
                else {
                    moves[i] = buildMoveScore(moves[i], mvvLVA(moves[i]));
                }
            }
            else if (mateKiller != 0 && moves[i] == mateKiller) {
                Assert.assertTrue(mateKiller < MoveConstants.FIRST_FREE_BIT);
                moves[i] = buildMoveScore(moves[i], mateKillerScore);
            }

            else if (firstKiller != 0 && firstKiller == moves[i]) {
                Assert.assertTrue(firstKiller < MoveConstants.FIRST_FREE_BIT);
                moves[i] = buildMoveScore(moves[i], killerOneScore);
            }
            else if (secondKiller != 0 && secondKiller == moves[i]) {
                Assert.assertTrue(secondKiller < MoveConstants.FIRST_FREE_BIT);
                moves[i] = buildMoveScore(moves[i], killerTwoScore);
            }
            else if (checkingMove(board, moves[i])) {
                moves[i] = MoveParser.setCheckingMove(move);
                Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
            }
            else if (isCastlingMove(moves[i])) {
                moves[i] = buildMoveScore(moves[i], castlingMove);
            }
            else if (ply >= 2 && firstOldKiller != 0 && firstOldKiller == moves[i]) {
                Assert.assertTrue(firstOldKiller < MoveConstants.FIRST_FREE_BIT);
                moves[i] = buildMoveScore(moves[i], oldKillerScoreOne);
            }
            else if (ply >= 2 && secondOldKiller != 0 && secondOldKiller == (moves[i])) {
                Assert.assertTrue(secondOldKiller < MoveConstants.FIRST_FREE_BIT);
                moves[i] = buildMoveScore(moves[i], oldKillerScoreTwo);
            }
            else {

                boolean pawnToSeven = MoveParser.moveIsPawnPushSeven(turn, move);
                if (pawnToSeven) {
                    moves[i] = buildMoveScore(move, pawnPushToSeven);
                    continue;
                }

                boolean pawnToSix = MoveParser.moveIsPawnPushSix(turn, move);
                if (pawnToSix) {
                    moves[i] = buildMoveScore(move, pawnPushToSix);
                    continue;
                }

                moves[i] = buildMoveScore(move, quietHeuristicMoveScore(move, turn, maxNodeQuietScore));
                
            }

            Assert.assertTrue(moves[i] >= FIRST_FREE_BIT);
            Assert.assertTrue(moves[i] > MOVE_MASK_WITH_CHECK);
        }

    }

    public static int mvvLVA(int move){
        int sourceScore = scoreByPiece(move, getMovingPieceInt(move));
        int destinationScore = scoreByPiece(move, getVictimPieceInt(move));
        return captureBias + destinationScore - sourceScore;
    }

    public static int scoreByPiece(int move, int piece){
        switch (piece){
            case NO_PIECE:
                return 0;
            case WHITE_PAWN:
            case BLACK_PAWN:
                return 1;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return 2;
            case WHITE_ROOK:
            case BLACK_ROOK:
                return 3;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return 4;
            case WHITE_KING:
            case BLACK_KING:
                return 5;
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

    public static void scoreMovesQuiescenceHelper(int[] moves, Chessboard board){
        final int maxMoves = moves[moves.length - 1];
        for (int i = 0; i < maxMoves; i++) {
            if (moves[i] == 0){
                break;
            }

            int move = moves[i];

            if (isCaptureMove(move)) {
                if (isPromotionMove(move) && isPromotionToQueen(move)) {
                    moves[i] = buildMoveScore(move, queenCapturePromotionScore);
                } else if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                    moves[i] = buildMoveScore(move, captureBiasOfLastMovedPiece + mvvLVA(moves[i]));
                } else {
                    moves[i] = buildMoveScore(move, mvvLVA(moves[i]));
                }
            } else if (isPromotionMove(move) && (isPromotionToQueen(moves[i]))) {
                moves[i] = buildMoveScore(moves[i], queenQuietPromotionScore);
            }
        }
    }

    private static boolean checkingMove(Chessboard board, int move){
        Assert.assertTrue(move != 0);

        try {
            board.makeMoveAndFlipTurn(move);
        } catch (Exception e) {
            System.out.println(board);
            System.out.println(move);
            MoveParser.printMove(move);
            System.out.println();
        }
        boolean checkingMove = board.inCheck(board.isWhiteTurn());
        board.unMakeMoveAndFlipTurn();
        return checkingMove;
    }

    public static void updateHistoryMoves(int whichThread, int move, int ply, int turnOfMover){
        historyMoves[whichThread + turnOfMover][getSourceIndex(move)][getDestinationIndex(move)] += (ply * ply);
    }

    public static int historyMoveScore(int move, int whichThread, int turn){
        int maxMoveScoreOfHistory = maxNodeQuietScore;
        int historyScore = historyMoves[whichThread + turn][getSourceIndex(move)][getDestinationIndex(move)];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }

    public static void updateKillerMoves(int whichThread, int move, int ply){
        Assert.assertTrue(killerMoves.length != 0 && killerMoves[0].length != 0);
        Assert.assertTrue(move < MoveConstants.FIRST_FREE_BIT);

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

    public static int quietHeuristicMoveScore(int move, int turn, int maxScore){
        int d = getDestinationIndex(move);
        int piece = getMovingPieceInt(move);
        if (piece > WHITE_KING) {
            piece -= 6;
        }
        int score = quietsILikeToMove[piece] * goodQuietDestinations[turn][63 - d];
        if (score > maxScore) {
            return maxScore;
        }

        if (score < uninterestingMove) {
            return uninterestingMove;
        }

        return score;
    }
}

