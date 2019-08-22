package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.axolotl.search.SearchSpecs;
import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_PV;

//@Disabled
public class TTPositionsTest {

    private Engine engine = new Engine();
    
    private static final int timeLimit = 45_000;

    @BeforeAll
    public static void setup() {
        ResettingUtils.reset();
        PRINT_PV = false;
    }

    @AfterAll
    public static void after() {
        ResettingUtils.reset();
    }
    
    @Test
    void fine70() {
        ResettingUtils.reset();
        System.out.println("\ntesting fine 70 with time " + timeLimit + " and one thread");
//        EngineSpecifications.DEBUG = true;
//        PRINT_PV = true;
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }

    @Disabled
    @Test
    void fine70MT() {
        ResettingUtils.reset();
        int threads = 4;
        System.out.println("\ntesting fine 70 with time " + timeLimit + " and " + threads + " threads");
//        EngineSpecifications.DEBUG = true;
//        PRINT_PV = true;
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        Engine.setThreads(threads);
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }
}
