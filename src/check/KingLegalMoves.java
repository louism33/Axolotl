package check;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGenerationUtilities;
import moveGeneration.PieceMoveKing;

import java.util.List;

public class KingLegalMoves {


    public static List<Move> kingLegalMovesOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int indexOfFirstPiece = BitIndexing.getIndexOfFirstPiece(myKing);
        return MoveGenerationUtilities.movesFromAttackBoard(kingLegalPushAndCaptureTable(board, white), indexOfFirstPiece);
    }

    public static long kingLegalPushAndCaptureTable(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long kingSafeSquares = ~CheckUtilities.kingDangerSquares(board, white);
        long kingSafeMoves = PieceMoveKing.singleKingAllMoves(board, myKing, white, kingSafeSquares, kingSafeSquares);
        return kingSafeMoves;
    }

}
