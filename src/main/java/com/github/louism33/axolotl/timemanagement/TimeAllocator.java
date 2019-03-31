package com.github.louism33.axolotl.timemanagement;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;

public final class TimeAllocator {

    public static long lastPrint;

    public static long allocateTime(long maxTime, long enemyTime, long increment, Integer movesToGo){
        if (maxTime < 10000){
            return 1000 + (increment / 3);
        }        
        long extraTime = maxTime > (enemyTime + (enemyTime / 5))
                ? (maxTime - enemyTime) / 5
                : 0;
        if (movesToGo != null && movesToGo < 10) {
            extraTime += maxTime / 20;
        }
        return (maxTime / 25) + (increment / 3) + extraTime;
    }

    public static boolean weShouldStopSearching(long timeLimitMillis, long timeLeftMillis){
        return timeLeftMillis < (timeLimitMillis) / 2;
    }

    public static boolean outOfTime(long startTime, long timeLimitMillis, boolean manageTime){

        lastPrint = Math.max(lastPrint, startTime);

        boolean outOfTime = false;
        if (!EngineSpecifications.ALLOW_TIME_LIMIT){
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long stopTime = startTime + timeLimitMillis;
        long timeLeftMillis = stopTime - currentTime;

        if (timeLeftMillis < 0) {
            outOfTime = true;
        }
        if (manageTime && weShouldStopSearching(timeLimitMillis, timeLeftMillis)) {
            // not enough time to search another ply
            outOfTime = true;
        }

        if (outOfTime){
            EngineBetter.stopNow = true;
        }
        return outOfTime;
    }

}
