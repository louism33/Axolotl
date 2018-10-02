package pinnedPieces;

import check.CheckUtilities;
import chess.Art;
import chess.BitIndexing;
import chess.Chessboard;
import moveGeneration.PieceMoveSliding;

public class BoardWithoutPinnedPieces {


    public static Chessboard removePinnedPieces (Chessboard board, boolean white){
        //todo
        Chessboard boardWithoutPinnedPieces = CheckUtilities.chessboardCopier(board, white, false);
        long pinnedPieces = whichPiecesArtPinned(board, white);
        return boardWithoutPinnedPieces;
    }

    static long whichPiecesArtPinned (Chessboard board, boolean white){

        long enemySlidingAttackTable = PieceMoveSliding.masterAttackTableSliding(board, !white, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE);

        Art.printLong(enemySlidingAttackTable);

        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;


        return 0;
    }

}
