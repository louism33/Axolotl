package javacode.chessprogram.chess;

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

   

    public static int populationCount (long pieces) {
        return Long.bitCount(pieces);
    }

}
