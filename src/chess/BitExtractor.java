package chess;

import java.util.ArrayList;
import java.util.List;

public class BitExtractor {

    public static List<Long> getAllPieces(long pieces, long ignoreThesePieces) {
        List<Long> indexes = new ArrayList<>();
        long temp = pieces & (~ignoreThesePieces);
        while (temp != 0) {
            long firstPiece = getFirstPiece(temp);
            indexes.add(firstPiece);
            temp ^= firstPiece;
        }
        return indexes;
    }

    static long getFirstPiece(long l) {
        return Long.highestOneBit(l);
    }

//    static long getLastPiece(long l) {
//        return Long.lowestOneBit(l);
//    }

//    public int getPieceOn(Chessboard board, boolean white, long square) {
//        List<Move> moves = new ArrayList<>();
//
//        if ((square & board.WHITE_PAWNS) != 0) {
//            return 1;
//        }
//        if ((square & board.WHITE_KNIGHTS) != 0) {
//            return 2;
//        }
//        if ((square & board.WHITE_BISHOPS) != 0) {
//            return 3;
//        }
//        if ((square & board.WHITE_ROOKS) != 0) {
//            return 4;
//        }
//        if ((square & board.WHITE_QUEEN) != 0) {
//            return 5;
//        }
//        if ((square & board.WHITE_KING) != 0) {
//            return 6;
//        }
//
//        if ((square & board.BLACK_PAWNS) != 0) {
//            return 7;
//        }
//        if ((square & board.BLACK_KNIGHTS) != 0) {
//            return 8;
//        }
//        if ((square & board.BLACK_BISHOPS) != 0) {
//            return 9;
//        }
//        if ((square & board.BLACK_ROOKS) != 0) {
//            return 10;
//        }
//        if ((square & board.BLACK_QUEEN) != 0) {
//            return 11;
//        }
//        if ((square & board.BLACK_KING) != 0) {
//            return 12;
//        }
//        return 0;
//    }
}
