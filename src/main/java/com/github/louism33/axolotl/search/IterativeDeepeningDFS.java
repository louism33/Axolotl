package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.timemanagement.TimeAllocator.outOfTime;

class IterativeDeepeningDFS {

    private static boolean stopSearch(boolean outOfTime, int depth, int maxDepth){
        return Engine.isStopInstruction()
                || (Engine.getEngineSpecifications().ALLOW_TIME_LIMIT && outOfTime)
                || (!Engine.getEngineSpecifications().ALLOW_TIME_LIMIT && (depth > maxDepth));
    }

    static void iterativeDeepeningWithAspirationWindows(Chessboard board, long startTime, long timeLimitMillis) throws IllegalUnmakeException {
        int aspirationScore = 0;
        int depth = 0;
        boolean outOfTime = false;

        /*
        Iterative Deepening Depth First Search:
        call searchMyTime function at increasing depths, the data we get from lower depths is easily worth it
         */
        while (!stopSearch(outOfTime, depth, Engine.MAX_DEPTH)){

            Chessboard initial = new Chessboard(board);
            Assert.assertEquals(board, initial);

            int score = AspirationSearch.aspirationSearch(board, startTime, timeLimitMillis, depth, aspirationScore);

//            PVLine pvLine = PVLine.retrievePVfromTable(board);
            
            // send various info through UCI protocol
            long timeTaken = System.currentTimeMillis() - startTime;

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                /*
                stop search when a checkmate has been found, however far away
                 */
                int distanceToCheckmate = CHECKMATE_ENEMY_SCORE - score;
//                UCIPrinter.acceptPVLine(pvLine, depth, true, distanceToCheckmate, timeTaken);
                break;

            } else if (score <= IN_CHECKMATE_SCORE_MAX_PLY){
                /*
                if we are certain to lose, don't search any further
                 */
//                UCIPrinter.acceptPVLine(pvLine, depth, false, 0, timeTaken);
                break;
            }
            else {
                if (depth > 5 && Engine.PRINT_INFO) {
//                    UCIPrinter.acceptPVLine(pvLine, depth, false, 0, timeTaken);
                }
            }

            if (outOfTime(startTime, timeLimitMillis)) {
                outOfTime = true;
            }

            aspirationScore = score;
            depth++;
        }
    }

}
