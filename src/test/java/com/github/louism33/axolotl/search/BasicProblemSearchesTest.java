package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
        ResettingUtils.reset();
        final Chessboard board = new Chessboard("5r2/r4p1p/1p3n2/n1pp1NNk/p2P4/P1P3R1/1P5P/5RK1 w - - 1 0");
        SearchSpecs.basicTimeSearch(6_000);
        final int move = engine.simpleSearch(board);
        Assert.assertEquals("f5g7", MoveParser.toString(move));
    }
    
    
    @Test
    public void testMate() { 
        ResettingUtils.reset();
        final Chessboard board = new Chessboard("r1r3k1/1bq2pbR/p5p1/1pnpp1B1/3NP3/3B1P2/PPPQ4/1K5R w - - 1 0");
//        PRINT_PV = true;
//        System.out.println(board);
        SearchSpecs.basicTimeSearch(10_000);
        final int move = engine.simpleSearch(board);
//        Assert.assertEquals(MoveParser.toString(MoveParserFromAN.buildMoveFromAN(board, "Bf6")), MoveParser.toString(move));
        Assert.assertTrue(Engine.aiMoveScore > EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY);
    }


}


