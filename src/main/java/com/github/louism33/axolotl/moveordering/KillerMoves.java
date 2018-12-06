package com.github.louism33.axolotl.moveordering;

import static com.github.louism33.chesscore.MoveParser.copyMove;

public class KillerMoves {

    public static final int[][] killerMoves = new int[100][2];
    public static final int[] mateKiller = new int[100];

    public static void updateKillerMoves(int move, int ply){
        /*
        if we have a new killer move, shift killers to the right
         */
        if (move != killerMoves[ply][0]){
            if (killerMoves[ply][0] != 0) {
                killerMoves[ply][1] = copyMove(killerMoves[ply][0]);
            }
            killerMoves[ply][0] = copyMove(move);
        }
    }

}
