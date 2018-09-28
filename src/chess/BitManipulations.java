package chess;

public class BitManipulations {

    private static long EMPTY = 0x0000000000000000L;

    static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

    static long northOne (long l){
        return l << 8;
    }

    static long southOne (long l){
        return l >> 8;
    }

    static boolean isEmpty (long l){
        return l == 0;
    }

    static long EnemyOrEmptySquares (long l) {
        return ~l;
    }

    static long rotateLeft(long l, int distance){
        return Long.rotateLeft(l, distance);
    }

    static long rotateRight(long l, int distance){
        return Long.rotateRight(l, distance);
    }

}
