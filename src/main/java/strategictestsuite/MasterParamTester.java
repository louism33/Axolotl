package strategictestsuite;

import com.github.louism33.axolotl.search.Engine;

public class MasterParamTester {

    public static boolean printBoard = false, printFen = false, enableAssert = false, printMyMove = false;
    public static boolean allBestMoves = false; // should you accept all best moves (the ones with scores), or just the single BM
    
//    public static final int timeLimit = 10_000;
//    public static final int timeLimit = 1_000;
    public static final int timeLimit = 50;
    public Engine engine = new Engine();


}

    
