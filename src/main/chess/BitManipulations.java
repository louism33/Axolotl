package main.chess;

public class BitManipulations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }
}
