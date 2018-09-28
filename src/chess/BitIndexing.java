package chess;

import java.util.ArrayList;
import java.util.List;


public class BitIndexing {

    static long getValueOfFirstPiece(long l){
        return Long.highestOneBit(l);
    }

    static long getValueOfLastPiece(long l){
        return Long.lowestOneBit(l);
    }

    static int getIndexOfFirstPiece (long pieces) {
        if (pieces == 0) return -1;
        long finder = pieces;
        int i = 0;
        while (((finder % 2) != 1) && ((finder % 2) != -1)){
            finder >>>= 1;
            i++;
        }
        return i;
    }

    static int getIndexOfLastPiece (long pieces) {
        long finder = pieces;
        int i = 63;
        while (finder > 0){
            finder <<= 1;
            i--;
        }
        return i;
    }

    static List<Integer> getIndexOfAllPieces(long pieces){
        List<Integer> indexes = new ArrayList<>();
        long temp = pieces;
        long endSquareMask = 0x0000000000000001L;
        int i = 0;
        while (temp != 0){
            if ((temp & endSquareMask) == endSquareMask) indexes.add(i);
            temp >>>= 1;
            i++;
        }
        return indexes;
    }

    public static int extractPieceIndexOrganiser(long pieces){
        if (pieces > 0) {
            return getIndexOfFirstPiece(pieces);
        }
        long pieceIndexHack = 0x8000000000000000L;

        if (pieces == pieceIndexHack) {
            return 63;
        }

        if (pieces < 0) {
            pieces ^= pieceIndexHack;
            return getIndexOfFirstPiece(pieces);
        }
        return -1;
    }

    static int pieceOnSquare(Chessboard board, int s){
        long square = BitManipulations.newPieceOnSquare(s);
        if ((square & board.WHITE_PAWNS) != 0) return 1;
        if ((square & board.WHITE_KNIGHTS) != 0) return 2;
        if ((square & board.WHITE_BISHOPS) != 0) return 3;
        if ((square & board.WHITE_ROOKS) != 0) return 4;
        if ((square & board.WHITE_QUEEN) != 0) return 5;
        if ((square & board.WHITE_KING) != 0) return 6;

        if ((square & board.BLACK_PAWNS) != 0) return 7;
        if ((square & board.BLACK_KNIGHTS) != 0) return 8;
        if ((square & board.BLACK_BISHOPS) != 0)  return 9;
        if ((square & board.BLACK_ROOKS) != 0) return 10;
        if ((square & board.BLACK_QUEEN) != 0) return 11;
        if ((square & board.BLACK_KING) != 0) return 12;

        else return 0;
    }


    static int populationCount (long pieces) {
        return Long.bitCount(pieces);
    }

}
