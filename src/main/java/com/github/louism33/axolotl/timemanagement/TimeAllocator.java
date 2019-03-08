package com.github.louism33.axolotl.timemanagement;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;

public class TimeAllocator {

    public static long lastPrint;

    public static long allocateTime(long maxTime, long increment){
        if (maxTime < 10000){
            return 1000;
        }
        return (maxTime / 25) + (increment / 3);
    }

    public static boolean weShouldStopSearching(long timeLimitMillis, long timeLeftMillis){
//        return false;
        return timeLeftMillis < (timeLimitMillis) / 3;
    }

    public static boolean outOfTime(long startTime, long timeLimitMillis, boolean allowTimeLimit){

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
        if (allowTimeLimit && weShouldStopSearching(timeLimitMillis, timeLeftMillis)) {
            // not enough time to search another ply
            outOfTime = true;
        }

        if (outOfTime){
            Engine.stopNow = true;
        }
        return outOfTime;
    }

}
