package javacode.chessengine;

import javacode.chessprogram.chess.Move;

public class KillerMoveArray {
    
    private KillerMoveArray killerMoveArray = new KillerMoveArray();
    private Move[] killerMoves = new Move[2];
    
    private KillerMoveArray(){}
    
    public KillerMoveArray getInstance(){
        return killerMoveArray;
    }
}
