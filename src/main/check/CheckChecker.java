package main.check;

import main.chess.BitIndexing;
import main.chess.Chessboard;
import main.moveGeneration.PieceMoveKing;
import main.moveGeneration.PieceMoveKnight;
import main.moveGeneration.PieceMovePawns;
import main.moveGeneration.PieceMoveSliding;

public class CheckChecker {

    public static boolean boardInCheck(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing);
        return numberOfCheckers > 0;
    }

    public static int numberOfPiecesThatLegalThreatenSquare(Chessboard board, boolean myColour, long square){
        long pawns, knights, bishops, rooks, queens, king;
        if (!myColour){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
            king = board.WHITE_KING;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
            king = board.BLACK_KING;
        }

        int numberOfThreats = 0;

        if (pawns != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMovePawns.singlePawnCaptures(board, square, myColour, pawns));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (knights != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveKnight.singleKnightCaptures(board, square, myColour, knights));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (bishops != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleBishopCaptures(board, square, myColour, bishops));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (rooks != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleRookCaptures(board, square, myColour, rooks));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (queens != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleQueenCaptures(board, square, myColour, queens));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (king != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveKing.singleKingCaptures(board, square, myColour, king));
        }

        return numberOfThreats;
    }
}
