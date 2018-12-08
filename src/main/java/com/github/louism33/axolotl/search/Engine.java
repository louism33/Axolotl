package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import org.junit.Assert;

public class Engine {

    public static final Statistics statistics = new Statistics();
    private static final boolean HEAVY_INFO_LOG = false;
    public static final boolean PRINT_INFO = false;
    public static int MAX_DEPTH = 12;
    public static long nps;

    public static UCIEntry uciEntry;
    private static final EngineSpecifications engineSpecifications = new EngineSpecifications();

    private static boolean stopInstruction;
    private static boolean setup = false;
    
    static int aiMove;

    public Engine(UCIEntry uciEntry) {
        Engine.uciEntry = uciEntry;
    }

    public static void setup(UCIEntry entry){
        uciEntry = entry;
    }
    
    private static void setup(){
        TranspositionTable.initTable(EngineSpecifications.DEFAULT_TABLE_SIZE);
        stopInstruction = false;
        setup = true;
    }

    public static int searchFixedDepth(Chessboard board, int depth){
        engineSpecifications.ALLOW_TIME_LIMIT = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0);
    }

    public static int searchMyTime (Chessboard board, long maxTime){
        engineSpecifications.ALLOW_TIME_LIMIT = true;

        if (maxTime < 1000){
            return searchFixedDepth(board, 1);
        }
        if (maxTime < 5000){
            return searchFixedDepth(board, 2);
        }
        long timeLimit = TimeAllocator.allocateTime(board, maxTime);
        return searchFixedTime(board, timeLimit);
    }

    public static int searchFixedTime(Chessboard board, long maxTime){
        if (!setup){
            setup();
        }

        long startTime = System.currentTimeMillis();

        int[] moves = board.generateLegalMoves();

        if (moves.length == 1){
            return moves[0];
        }

        nps = 0;
        
        int move = 0;
        try {
            IterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows
                    (board, startTime, maxTime);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }

        if (HEAVY_INFO_LOG){
            statistics.printStatistics();
        }

        long endTime = System.currentTimeMillis();
        statistics.infoLog(endTime, startTime, move);

        long time = endTime - startTime;
        
        if (time > 1000) {
            nps = ((1000 * (statistics.numberOfMovesMade + statistics.numberOfQuiescentMovesMade)) / time);
        }
        
        return aiMove & MoveOrderer.MOVE_MASK;
    }

    public static int getAiMove(){
//        return PrincipleVariationSearch.getAiMove();
        return aiMove;
    }

    public static UCIEntry getUciEntry() {
        return uciEntry;
    }

    public static boolean isStopInstruction() {
        return stopInstruction;
    }

    public static void setStopInstruction(boolean stopInstruction) {
        stopInstruction = stopInstruction;
    }

    public static EngineSpecifications getEngineSpecifications() {
        return engineSpecifications;
    }

}
