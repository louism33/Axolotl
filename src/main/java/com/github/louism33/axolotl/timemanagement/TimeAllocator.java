package com.github.louism33.axolotl.timemanagement;

import com.github.louism33.axolotl.search.EngineSpecifications;

public class TimeAllocator {

    private static long lastPrint;

    public static long allocateTime(long maxTime, long increment){
        if (maxTime < 10000){
            return 1000;
        }
        return (maxTime / 20) + (increment / 2);
    }

    private static boolean weShouldStopSearching(long timeLimitMillis, long timeLeftMillis){
        return timeLeftMillis < (timeLimitMillis) / 3;
    }

    public static boolean outOfTime(long startTime, long timeLimitMillis){

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
        if (weShouldStopSearching(timeLimitMillis, timeLeftMillis)) {
            // not enough time to search another ply
            outOfTime = true;
        }

        return outOfTime;
    }

}