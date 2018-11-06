package javacode.chessengine;

import javacode.chessprogram.chess.Move;

class KillerMoves {

    static final Move[][] killerMoves = new Move[100][2];
    static final Move[] mateKiller = new Move[100];

    static void updateKillerMoves(Move move, int ply){
        /*
        if we have a new killer move, shift killers to the right
         */
        if (!move.equals(killerMoves[ply][0])){
            killerMoves[ply][1] = killerMoves[ply][0];
            killerMoves[ply][0] = move;
        }
    }
    
}
