package javacode.chessprogram.chess;

import java.util.ArrayList;
import java.util.List;


public class BitIndexing {

    public static final long UNIVERSE = 0xffffffffffffffffL;

    public static int getIndexOfFirstPiece (long pieces) {
        return Long.numberOfTrailingZeros(pieces);
    }

    public static List<Integer> getIndexOfAllPieces(long pieces){
        List<Integer> indexes = new ArrayList<>();
        while (pieces != 0){
            indexes.add(Long.numberOfLeadingZeros(pieces));
            pieces &= pieces - 1;
        }
        return indexes;
    }

    public static int populationCount (long pieces) {
        return Long.bitCount(pieces);
    }

}
