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


//    @Test
//    @Disabled
//    public void testNothing() { 
//        ResettingUtils.reset();
//        /*
//        info depth 15 seldepth 28 multipv 1 score mate 5 nodes 130859 nps 1147885 tbhits 0 time 114 pv f5g7 h5h6 f1f6 h6g7 g5h7 g7h7 f6f4 c5d4 f4h4
//        
//        me: 
//        info depth 14 seldepth 29 multipv 1 score cp 788 nodes 263847 nps 100974 tbhits 0 time 2613 pv f5g7 h5h6 g5h3 f6g4 g3g4 f7f5 g7f5 f8f5 f1f5 a7g7
//        
//        
//        f1f6 h6g7 g5h7 g7h7 f6f4 c5d4 f4h4
//        g5h3 f6g4 g3g4 f7f5 g7f5 f8f5 f1f5 a7g7
//        
//        f1f6 h6g7 -->f6b6 g7h8 d4c5 f7f5 
//         */
////        final Chessboard board = new Chessboard("5r2/r4p1p/1p3n2/n1pp1NNk/p2P4/P1P3R1/1P5P/5RK1 w - - 1 0");
//        final Chessboard board = new Chessboard("r1r3k1/1bq2pbR/p5p1/1pnpp1B1/3NP3/3B1P2/PPPQ4/1K5R w - - 1 0");
////        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "f5g7"));
////        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "h5h6"));
////        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "f1f6"));
////        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "h6g7"));
//
//        System.out.println(board);
//
//        final int[] moves = board.generateLegalMoves();
//        MoveParser.printMove(moves);
//        System.out.println();
//        scoreMoves(moves, board, 0, 0, 0);
//        MoveParser.printMove(moves);
//        for (int i = 0; i < MoveParser.numberOfRealMoves(moves); i++) {
//            System.out.println(MoveParser.toString(moves[i]) + ": " + MoveOrderer.getMoveScore(moves[i]));
//        }
//        System.out.println();
//
////        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g5h3"));
//        PRINT_PV = true;
//        SearchSpecs.basicTimeSearch(6_000);
//        final int move = engine.simpleSearch(board);
////        Assert.assertEquals( "f5g7", MoveParser.toString(move));
//
//    }
}


