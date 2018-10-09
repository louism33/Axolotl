package check;

import chess.BitIndexing;
import chess.Chessboard;
import moveGeneration.PieceMoveKing;
import moveGeneration.PieceMoveKnight;
import moveGeneration.PieceMovePawns;
import moveGeneration.PieceMoveSliding;

public class CheckChecker {


    public static boolean boardInCheck(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing);
        return numberOfCheckers > 0;
    }


    public static int numberOfPiecesThatLegalThreatenSquare(Chessboard board, boolean white, long square){
        long pawns, knights, bishops, rooks, queens, king;
        if (!white){
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
            numberOfThreats += BitIndexing.populationCount(PieceMovePawns.singlePawnCaptures(board, square, white, pawns));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (knights != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveKnight.singleKnightCaptures(board, square, white, knights));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (bishops != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleBishopCaptures(board, square, white, bishops));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (rooks != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleRookCaptures(board, square, white, rooks));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (queens != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleQueenCaptures(board, square, white, queens));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (king != 0) {
            numberOfThreats += BitIndexing.populationCount(PieceMoveKing.singleKingCaptures(board, square, white, king));
        }

        return numberOfThreats;
    }
}
