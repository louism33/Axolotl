package check;

import chess.Art;
import chess.BitIndexing;
import chess.BitManipulations;
import chess.Chessboard;
import moveGeneration.*;

public class CheckChecker {


    public static boolean boardInCheck(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing);
        return numberOfCheckers > 0;
    }


    public static int numberOfPiecesThatLegalThreatenSquare(Chessboard board, boolean white, long square){
        long ans = 0, pawns, knights, bishops, rooks, queens, king;
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

        numberOfThreats += BitIndexing.populationCount(PieceMovePawns.singlePawnCaptures(board, square, white, pawns));
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        numberOfThreats += BitIndexing.populationCount(PieceMoveKnight.singleKnightCaptures(board, square, white, knights));
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleBishopCaptures(board, square, white, bishops));
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleRookCaptures(board, square, white, rooks));
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        numberOfThreats += BitIndexing.populationCount(PieceMoveSliding.singleQueenCaptures(board, square, white, queens));
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        numberOfThreats += BitIndexing.populationCount(PieceMoveKing.singleKingCaptures(board, square, white, king));

        return numberOfThreats;
    }
}
