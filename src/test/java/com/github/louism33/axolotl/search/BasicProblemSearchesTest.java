package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_PV;

public class BasicProblemSearchesTest {

    // tests from positions that at some point or other caused problems/crashes for the engine
    
    private static Engine engine = new Engine();
    
    @BeforeClass
    public static void setup() {
        ResettingUtils.reset();
    }

    @AfterClass
    public static void tearDown() {
        ResettingUtils.reset();
    }


    @Test
    public void testMate78() {
        Engine.resetFull();
        final Chessboard board = new Chessboard("5r2/r4p1p/1p3n2/n1pp1NNk/p2P4/P1P3R1/1P5P/5RK1 w - - 1 0");
//        PRINT_PV = true;
        System.out.println(board);
        SearchSpecs.basicTimeSearch(10_000);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals( "f5g7", MoveParser.toString(move));
        
    }
    
}


