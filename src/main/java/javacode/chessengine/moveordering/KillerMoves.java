package javacode.chessengine.moveordering;

import javacode.chessprogram.chess.Move;

import static javacode.chessprogram.chess.Copier.copyMove;

public class KillerMoves {

    public static Move[][] killerMoves = new Move[100][2];
    public static Move[] mateKiller = new Move[100];

    public static void updateKillerMoves(Move move, int ply){
        /*
        if we have a new killer move, shift killers to the right
         */
        if (!move.equals(killerMoves[ply][0])){
            if (killerMoves[ply][0] != null) {
                killerMoves[ply][1] = copyMove(killerMoves[ply][0]);
            }
            killerMoves[ply][0] = copyMove(move);
        }
    }

}
