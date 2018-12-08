package com.github.louism33.axolotl.moveordering;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import static com.github.louism33.axolotl.moveordering.HistoryMoves.historyMoveScore;
import static com.github.louism33.axolotl.moveordering.KillerMoves.killerMoves;
import static com.github.louism33.axolotl.moveordering.KillerMoves.mateKiller;
import static com.github.louism33.axolotl.moveordering.MoveOrderingConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;

@SuppressWarnings("FieldCanBeLocal")
public class MoveOrderer {

    public static final int MOVE_MASK = ~MOVE_SCORE_MASK;

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

    /*
    Move Ordering:
    previous Hash Moves, promotions, capture of last moved piece, good captures, killers, killers from earlier plies, 
    castling, bad captures, quiet moves, bad promotions
     */
    public static void scoreMoves(int[] moves, Chessboard board, boolean white, int ply,
                                  int hashMove){
        try {
            scoreMovesHelper(moves, board, white, ply, hashMove);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }
    }

    private static void scoreMovesHelper(int[] moves, Chessboard board, boolean white, int ply,
                                         int hashMove) throws IllegalUnmakeException {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0){
                break;
            }
            if (moves[i] == hashMove) {
                moves[i] = buildMoveScore(moves[i], hashScore);
//            } else if (ply == 0 && moves[i] == aiMove) {
//                moves[i] = buildMoveScore(moves[i], aiScore);
            } else if (Engine.getEngineSpecifications().ALLOW_MATE_KILLERS && mateKiller[ply] != 0 && moves[i] == (mateKiller[ply])) {
                moves[i] = buildMoveScore(moves[i], mateKillerScore);
            } else if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                moves[i] = buildMoveScore(moves[i], CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(board, moves[i]));
            } else if (isPromotionToQueen(moves[i])) {
                if (isCaptureMove(moves[i])) {
                    moves[i] = buildMoveScore(moves[i], queenCapturePromotionScore);
                }
                else {
                    moves[i] = buildMoveScore(moves[i], queenQuietPromotionScore);
                }
            } else if (isPromotionToKnight(moves[i])) {
                moves[i] = buildMoveScore(moves[i], knightPromotionScore);
            } else if (isPromotionToBishop(moves[i]) || isPromotionToRook(moves[i])) {
//                 promotions to rook and bishop are considered right at the end
                moves[i] = buildMoveScore(moves[i], uninterestingPromotion);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && killerMoves[ply][0] != 0 && killerMoves[ply][0] == moves[i]) {
                moves[i] = buildMoveScore(moves[i], killerOneScore);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && killerMoves[ply][1] != 0 && killerMoves[ply][1] == moves[i]) {
                moves[i] = buildMoveScore(moves[i], killerTwoScore);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && ply >= 2 && killerMoves.length > 2
                    && killerMoves[ply - 2][0] != 0 && killerMoves[ply - 2][0] == moves[i]) {
                moves[i] = buildMoveScore(moves[i], oldKillerScoreOne);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && ply >= 2 && killerMoves.length > 2
                    && killerMoves[ply - 2][1] != 0 && killerMoves[ply - 2][1] == (moves[i])) {
                moves[i] = buildMoveScore(moves[i], oldKillerScoreTwo);
            } else if (checkingMove(board, moves[i])) {
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
            } else if (MoveParser.isCaptureMove(moves[i])) {
                moves[i] = buildMoveScore(moves[i], mvvLVA(board, moves[i]));
            } else if (MoveParser.isCastlingMove(moves[i])) {
                moves[i] = buildMoveScore(moves[i], castlingMove);
            } else if (Engine.getEngineSpecifications().ALLOW_HISTORY_MOVES) {
                moves[i] = buildMoveScore(moves[i], Math.max(historyMoveScore(moves[i]), uninterestingMove));
            } else {
                moves[i] = buildMoveScore(moves[i], uninterestingMove);
            }
        }
    }

    static boolean positiveCapture(int moveScore){
        return getMoveScore(moveScore) > CAPTURE_BIAS;
    }

    private static int mvvLVA (Chessboard board, int move){
        int sourceScore = scoreByPiece(move, MoveParser.getMovingPieceInt(move));
        int destinationScore = scoreByPiece(move, MoveParser.getVictimPieceInt(move));
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
    public static void scoreMovesQuiescence(int[] moves, Chessboard board, boolean white){
        scoreMovesQuiescenceHelper(moves, board, white);
    }

    private static void scoreMovesQuiescenceHelper(int[] moves, Chessboard board, boolean white){
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0){
                break;
            }
            if (MoveParser.isCaptureMove(moves[i])) {
                if (isPromotionMove(moves[i])) {
                    /*
                    ignore under promotions in Q search
                     */
                    if (isPromotionToQueen(moves[i])) {
                        moves[i] = buildMoveScore(moves[i], queenCapturePromotionScore);
                    }
                } else if (board.moveIsCaptureOfLastMovePiece(moves[i])) {
                    moves[i] = buildMoveScore(moves[i], CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(board, moves[i]));
                } else {
                    moves[i] = buildMoveScore(moves[i], mvvLVA(board, moves[i]));
                }
            } else if (isPromotionMove(moves[i])) {
                if (isPromotionToQueen(moves[i])) {
                    moves[i] = buildMoveScore(moves[i], queenQuietPromotionScore);
                }
            } else {
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

    public static void updateHistoryMoves(int move, int ply){
        HistoryMoves.updateHistoryMoves(move, ply);
    }
}