package com.github.louism33.axolotl.helper.timemanagement;

import com.github.louism33.axolotl.helper.protocolhelperclasses.UCIPrinter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;

public class TimeAllocator {

    private static long lastPrint;

    public static long allocateTime(Chessboard board, long maxTime){
        return maxTime / 25;
    }

    private static boolean weShouldStopSearching(long timeLimitMillis, long timeLeftMillis){
        return timeLeftMillis < timeLimitMillis / 2;
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

    public static void printManager(Chessboard board, boolean printNow){
        if (printNow || (EngineSpecifications.INFO_LOG && timeToPrint())){
            try {
                lastPrint = System.currentTimeMillis();
                UCIPrinter.printPV(board);
            } catch (IllegalUnmakeException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static boolean timeToPrint(){
        return System.currentTimeMillis() - lastPrint > EngineSpecifications.PRINT_FREQUENCY_MS;
    }

}
