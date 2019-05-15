package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.search.SearchSpecs.SEARCH_TYPE.NONE;

public final class SearchSpecs {
    
    public enum SEARCH_TYPE{
        NONE,
        TO_DEPTH,
        MOVE_TIME,
        UCI,
        INFINITE,
        TO_MATE,
        TO_NODES
    }

    public static SEARCH_TYPE searchType;

    public static long timeLimitMillis;
    public static long maxEnemyTime;
    public static long myInc;
    public static long enemyInc;
    public static int movesToGo;
    public static int fullMoveCounter;

    public static int maxDepth = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public static long maxMyTime;
    public static int maxNodes;
    public static boolean allowTimeLimit = true;
    public static boolean manageTime;
    public static long absoluteMaxTimeLimit;


    public static void reset() {
        searchType = NONE;
        
        maxEnemyTime = 0;
        myInc = 0;
        enemyInc = 0;
        movesToGo = 0;
        
        maxDepth = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
        maxMyTime = 0;
        allowTimeLimit = true;
        manageTime = false;
        timeLimitMillis = 0;
        absoluteMaxTimeLimit = 0;
        maxNodes = 0;
    }

    public static void basicDepthSearch(int depth) {
        reset();
        SearchSpecs.allowTimeLimit = false;
        SearchSpecs.manageTime = false;
        SearchSpecs.searchType = SEARCH_TYPE.TO_DEPTH;
        SearchSpecs.maxDepth = depth;
    }

    public static void basicTimeSearch(long timeLimit) {
        reset();
        allowTimeLimit = true;
        manageTime = false;
        SearchSpecs.searchType = SearchSpecs.SEARCH_TYPE.MOVE_TIME;
        SearchSpecs.timeLimitMillis = timeLimit;
    }

    
    // position startpos moves f2f4 d7d5 g1f3 g8f6 b2b3 g7g6 c1b2 f8g7 e2e3 e8g8 f1e2 c7c5 e1g1 b8c6 f3e5
    // go wtime 5000 btime 5251 winc 500 binc 500
    // go wtime 50000 btime 52510 winc 5000 binc 5000
    
    public static void uciSearch(long mytime, long enemytime, long myinc, long enemyinc, int movestogo, int fullmovecounter) {
        reset();
        
        manageTime = true;
        allowTimeLimit = true;

        maxMyTime = mytime;
        maxEnemyTime = enemytime;
        myInc = myinc;
        enemyInc = enemyinc;
        movesToGo = movestogo;
        fullMoveCounter = fullmovecounter;
        timeLimitMillis = TimeAllocator.allocateTime(maxMyTime, maxEnemyTime, myInc, movesToGo, fullMoveCounter);
        
        SearchSpecs.searchType = SearchSpecs.SEARCH_TYPE.MOVE_TIME;

        
    }
}
