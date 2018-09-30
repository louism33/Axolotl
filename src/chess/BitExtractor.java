package chess;

import java.util.ArrayList;
import java.util.List;

public class BitExtractor {

    public static List<Long> getAllPieces(long pieces){
        List<Long> indexes = new ArrayList<>();
        long temp = pieces;
        while (temp != 0){
            long firstPiece = getFirstPiece(temp);
            indexes.add(firstPiece);
            temp ^= firstPiece;
        }
        return indexes;
    }

    static long getFirstPiece(long l){
        return Long.highestOneBit(l);
    }

    static long getLastPiece(long l){
        return Long.lowestOneBit(l);
    }


}
