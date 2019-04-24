package com.github.louism33.axolotl.timemanagement;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;

public final class TimeAllocator {

    public static long allocateTime(long maxTime, long enemyTime, long increment, Integer movesToGo, int fullMovesCounter) {
        if (maxTime < 1000) {
            return 100;
        }

        if (maxTime < 5000) {
            return 1000 + (increment / 3);
        }

        long time;

        if (fullMovesCounter <= 40) {
            time = maxTime / 22 + increment / 2;
        } else {
            time = maxTime / 25 + increment / 2;
        }

        if (maxTime > enemyTime + enemyTime / 5) {
            final long dominanceBonus = (maxTime - enemyTime) / 6;
            time = Math.min(time * 2, time + dominanceBonus);
        }

        if (movesToGo != null && movesToGo != 0 && movesToGo < 10) {
            time += maxTime / (14 * movesToGo);
        }

        long absoluteMaximumTime = maxTime >>> 3;
        if (time > absoluteMaximumTime) {
            time = absoluteMaximumTime;
        }

        return time;
    }

    public static long allocatePanicTime(long timeLimitMillis, long absoluteMaxTimeLimit) {
        if (timeLimitMillis > (absoluteMaxTimeLimit >>> 3)) {
            return timeLimitMillis << 2;
        }
        return timeLimitMillis;
    }

    public static boolean weShouldStopSearching(long timeLimitMillis, long timeLeftMillis) {
        return timeLeftMillis < (timeLimitMillis) / 2;
    }

    public static boolean outOfTime(long startTime, long timeLimitMillis, boolean manageTime) {

        boolean outOfTime = false;
        if (!EngineSpecifications.ALLOW_TIME_LIMIT) {
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

        if (outOfTime) {
            Engine.stopNow = true;
        }
        return outOfTime;
    }

}
