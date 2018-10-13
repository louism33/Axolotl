package main.chess;

import java.util.ArrayList;
import java.util.List;


public class BitIndexing {

    public static final long UNIVERSE = 0xffffffffffffffffL;

    public static int getIndexOfFirstPiece (long pieces) {
        if (pieces == 0) return -1;
        long finder = pieces;
        int i = 0;
        while (((finder % 2) != 1) && ((finder % 2) != -1)){
            finder >>>= 1;
            i++;
        }
        return i;
    }

    public static List<Integer> getIndexOfAllPieces(long pieces){
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

    public static int populationCount (long pieces) {
        return Long.bitCount(pieces);
    }

}
