package chess;

public class BitManipulations {

    private static long EMPTY = 0x0000000000000000L;

    static long northOne (long l){
        return l << 8;
    }

    static long southOne (long l){
        return l >> 8;
    }

    static boolean isEmpty (long l){
        return l == 0;
    }

    static int pieceOnSquare(int s){
        return 0;
    }

    static int getIndexOfFirstPiece (long pieces) {
        long finder = pieces;
        int i = 0;
        while (finder % 2 != 1){
            finder >>= 1;
            i++;
        }
        return i;
    }

    // because a long is signed, and so the final rook causes it to be negative.
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


    static int getIndexOfLastPiece (long pieces) {
        return 0;
    }

    static int populationCount (long pieces) {
        return 0;
    }

}
