package javacode.chessengine.timemanagement;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Chessboard;

public class TimeAllocator {
    
    public long allocateTime(Chessboard board, long maxTime){
        return maxTime / 25;
    }

    private static boolean weShouldStopSearching(long timeLeft, long maxTime){
        return timeLeft > maxTime / 2;
    }
    
    public static boolean outOfTime(Engine engine, long startTime, long timeLimitMillis){
        boolean outOfTime = false;
        
        if (!engine.getEngineSpecifications().ALLOW_TIME_LIMIT){
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long maxTime = startTime + timeLimitMillis;
        long timeLeft = maxTime - currentTime;
        if (timeLeft < 0) {
            outOfTime = true;
        }
        if (weShouldStopSearching(timeLeft, maxTime)) {
            // not enough time to search another ply
            outOfTime = true;
        }
        
        return outOfTime;
    }
}
