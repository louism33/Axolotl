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
    
    private static final int timeLimit = 60_000;

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
        // 1 thread:
        // info depth 23 seldepth 28 multipv 1 score cp 72 nodes 112938 nps 93880 tbhits 0 time 1203 pv a1b1 a7b7 b1c1 b7c7 c1d1 c7d7 d1c2 d7d8 c2c3 d8c7
        // 2 
        // info depth 27 seldepth 33 multipv 1 score cp 204 nodes 2863023 nps 452651 tbhits 0 time 6325 pv a1b1 a7b7 b1c1 b7c7 c1d1 c7d7 d1c2 d7c8 c2d2 c8d8 
        // 4 
        // info depth 29 seldepth 22 multipv 1 score cp 330 nodes 17944937 nps 954061 tbhits 0 time 18809 pv a1b1 a7b7 b1c1 b7c7 c1d1 c7d7 d1c2 d7c7 c2d3 c7b6
        ResettingUtils.reset();
        int threads = 4;
        System.out.println("\ntesting fine 70 with time " + timeLimit + " and " + threads + " threads");
//        EngineSpecifications.DEBUG = true;
        PRINT_PV = true;
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        Engine.setThreads(threads);
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }
}
