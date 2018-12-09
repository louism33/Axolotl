package com.github.louism33.axolotl.helper.timemanagement;

import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;

public class TimeAllocator {
    
    public static long allocateTime(Chessboard board, long maxTime){
        return maxTime / 25;
    }

    private static boolean weShouldStopSearching(long timeLeft, long maxTime){
        return timeLeft > maxTime / 1.2;
    }
    
    public static boolean outOfTime(long startTime, long timeLimitMillis){
        boolean outOfTime = false;
        
        if (!EngineSpecifications.ALLOW_TIME_LIMIT){
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
