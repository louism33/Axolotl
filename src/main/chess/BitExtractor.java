package main.chess;

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

    private static long getFirstPiece(long l) {
        return Long.highestOneBit(l);
    }

}
