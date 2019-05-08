package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.Engine;
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
    
    private static final int timeLimit = 25_000;

    @BeforeAll
    public static void setup() {
        ResettingUtils.reset();
        PRINT_PV = false;
        System.out.println("testing fine 70 with time " + timeLimit);
    }

    @AfterAll
    public static void after() {
        ResettingUtils.reset();
    }
    
    @Test
    void fine70() {
        ResettingUtils.reset();
        PRINT_PV = true;
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }

    @Test
    void fine70MT() {
        ResettingUtils.reset();
        PRINT_PV = true;
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        Engine.setThreads(4);
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }
}
