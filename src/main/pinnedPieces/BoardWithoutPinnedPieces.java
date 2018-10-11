//package main.pinnedPieces;
//
//import main.check.CheckUtilities;
//import main.chess.Chessboard;
//
//public class BoardWithoutPinnedPieces {
//
//    public static Chessboard removePinnedPieces (Chessboard board, boolean white){
//
//        Chessboard boardWithoutPinnedPieces = CheckUtilities.copyBoard(board, white, false);
//        long main.pinnedPieces = whichPiecesArePinned(board, white);
//        return boardWithoutPinnedPieces;
//    }
//
//    static long whichPiecesArePinned(Chessboard board, boolean white){
//
////        long enemySlidingAttackTable = PieceMoveSliding.masterAttackTableSliding(board, !white, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE);
//
////        Art.printLong(enemySlidingAttackTable);
//
//        /*
//        put a bishop on king, then a rook, if hit a friendly piece, then if hit a enemy bishop / queen then rook/queen, profit
//         */
//        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
//
//
//        return 0;
//    }
//
//}
