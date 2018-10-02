package check;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGenerationUtilities;
import moveGeneration.PieceMoveJumping;

import java.util.List;

public class KingLegalMoves {


    public static List<Move> kingLegalMovesOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int indexOfFirstPiece = BitIndexing.getIndexOfFirstPiece(myKing);
        return MoveGenerationUtilities.movesFromAttackBoard(kingLegalTableOnly(board, white), indexOfFirstPiece);
    }

    public static long kingLegalTableOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long kingSafeSquares = ~CheckUtilities.kingDangerSquares(board, white);
        long kingSafeMoves = PieceMoveJumping.kingMove(board, myKing, white, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE) & kingSafeSquares;
        return kingSafeMoves;
    }

}
