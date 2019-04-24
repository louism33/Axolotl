package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_PV;

public class TTPositions {

    private Engine engine = new Engine();
    
    private static final int timeLimit = 100_000;

    @BeforeAll
    public static void setup() {
        Util.reset();
        PRINT_PV = false;
        System.out.println("testing fine 70 with time " + timeLimit);
    }

    @AfterAll
    public static void after() {
        PRINT_PV = false;
    }
    
    @Test
    void fine70() {
        Engine.resetFull();
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        engine.receiveSearchSpecs(board, true, timeLimit);
        int move = engine.simpleSearch();
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }

    @Test
    void fine70MT() {
        Engine.resetFull();
        Chessboard board = new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -");
        Engine.setThreads(4);
        engine.receiveSearchSpecs(board, true, timeLimit);
        int move = engine.simpleSearch();
        Assert.assertEquals(MoveParser.toString(move), "a1b1");
    }
}
