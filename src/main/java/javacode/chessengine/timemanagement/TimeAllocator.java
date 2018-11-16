package javacode.chessengine.timemanagement;

import javacode.chessprogram.chess.Chessboard;

public class TimeAllocator {
    
    public long allocateTime(Chessboard board, long maxTime){
        return maxTime / 25;
    }
}
