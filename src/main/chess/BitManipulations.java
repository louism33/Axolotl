package main.chess;

public class BitManipulations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

//    public static boolean isEmptySquare (Chessboard board, long square){
//        return ((square & board.ALL_PIECES()) == 0);
//    }
//
//    public static long northOne (long l){
//        return l << 8;
//    }
//
//    public static long southOne (long l){
//        return l >> 8;
//    }
//
//    static boolean isZero(long l){
//        return l == 0;
//    }
//
//    public static long EnemyOrEmptySquares (long l) {
//        return ~l;
//    }
//
//    static long rotateLeft(long l, int distance){
//        return Long.rotateLeft(l, distance);
//    }
//
//    static long rotateRight(long l, int distance){
//        return Long.rotateRight(l, distance);
//    }

}
