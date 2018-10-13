package main.check;

import main.chess.BitIndexing;
import main.chess.Chessboard;
import main.chess.Move;
import main.moveGeneration.MoveGenerationUtilities;
import main.moveGeneration.PieceMoveKing;
import org.junit.Assert;

import java.util.List;

public class KingLegalMoves {

    public static List<Move> kingLegalMovesOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int indexOfKing = BitIndexing.getIndexOfFirstPiece(myKing);
        return MoveGenerationUtilities.movesFromAttackBoard(kingLegalPushAndCaptureTable(board, white), indexOfKing);
    }

    private static long kingLegalPushAndCaptureTable(Chessboard board, boolean white){
        long ans = 0;
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long kingSafeSquares = ~CheckUtilities.kingDangerSquares(board, white);
        long enemyPieces = (!white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long kingSafeCaptures = enemyPieces & kingSafeSquares;
        long kingSafePushes = (~board.ALL_PIECES() & kingSafeSquares);
        
        ans |= PieceMoveKing.singleKingPushes(board, myKing, white, kingSafePushes);
        ans |= PieceMoveKing.singleKingCaptures(board, myKing, white, kingSafeCaptures);

        Assert.assertTrue(((kingSafeCaptures & kingSafePushes) == 0));

        return ans;
    }

}
