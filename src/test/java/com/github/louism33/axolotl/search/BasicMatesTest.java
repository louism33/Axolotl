package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.search.Engine.aiMoveScore;
import static com.github.louism33.axolotl.search.Engine.quitOnSingleMove;
import static com.github.louism33.axolotl.search.EngineSpecifications.DEBUG;
import static com.github.louism33.chesscore.MaterialHashUtil.*;

//@Disabled
public class BasicMatesTest {

    // thanks to guido for many of these positions  http://kirill-kryukov.com/chess/discussion-board/viewtopic.php?f=6&t=920
    Engine engine = new Engine();

    @BeforeEach
    void setup() {
        ResettingUtils.reset();
        EngineSpecifications.sendBestMove = false;
    }


    @AfterEach
    void tearDown() {
        ResettingUtils.reset();
        EngineSpecifications.sendBestMove = true;
    }

    @Test
    void KRRKWins() {
        System.out.println("\nsearching checkmate for KRRK positions: ");
        String pos = "" +
                "8/8/8/8/8/3k4/1RR5/K7 w\n" +
                "8/8/8/8/8/3k4/2R3R1/K7 w\n" +

                "8/8/8/8/8/3K4/2r4r/k7 b - - 0 1\n" +
                "8/8/8/8/8/3K4/2r5/k5r1 b - - 0 1\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KRRK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(4_000);
            final int move = engine.simpleSearch(board);
            final boolean condition = aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(condition);
            System.out.print(". ");
        }
    }
    
    @Test
    void KRRKloss() {
        System.out.println("\nsearching my checkmate for KRRK positions: ");
        String pos = "" +
                "8/8/8/8/8/3k4/R1R5/K7 b\n" +
                "8/8/8/8/8/3k4/1RR5/K7 b\n" +
                "8/8/8/8/8/3K4/2r4r/k7 w - - 0 1\n" +
                "7k/8/6r1/8/5K2/8/8/7r w - - 0 1\n" +
                "";

        String[] kqkPositions = pos.split("\n");

        for (int i = 0; i < kqkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(kqkPositions[i]);
            Assert.assertEquals(KRRK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(20_000);
            final int move = engine.simpleSearch(board);

            final boolean condition = aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(condition);
            System.out.print(". ");
        }
    }
    

    @Test
    void KQKwin() {
        System.out.println("\nsearching checkmate for KQK positions: ");
        String pos = "" +
                "3q4/3k4/8/8/8/8/7K/8 b - - 0 1\n" +
                "4k3/8/1K6/8/8/8/4q3/8 b - - 0 1\n" +
                "k7/6KQ/8/8/8/8/8/8 w - - 0 1\n" +
                "8/8/3Q4/8/8/7k/8/3K4 w - - 0 1\n" +
                "";

        String[] kqkPositions = pos.split("\n");

        for (int i = 0; i < kqkPositions.length; i++) {
            Engine.resetFull(); 
            Chessboard board = new Chessboard(kqkPositions[i]);
            Assert.assertEquals(KQK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(20_000);
            final int move = engine.simpleSearch(board);

            final boolean condition = aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.print(". ");
        }
    }

    @Test
    void KQKloss() {
        System.out.println("\nsearching my checkmate for KQK positions: ");
        String pos = "" +
                "8/8/8/8/8/8/3k3K/7Q b - - 0 1\n" +
                "4k3/8/1K6/8/8/8/2Q5/8 b - - 0 1\n" +
                "3q4/3k4/8/8/8/8/7K/8 w - - 0 1\n" +
                "8/8/8/8/7q/7k/8/3K4 w - - 0 1\n" +
                "";

        String[] kqkPositions = pos.split("\n");

        for (int i = 0; i < kqkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(kqkPositions[i]);
            Assert.assertEquals(KQK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(25_000);
            final int move = engine.simpleSearch(board);

            final boolean condition = aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(condition);
            System.out.print(". ");
        }
    }

    @Test
    void KRKWins() {
        System.out.println("\nsearching checkmate for KRK positions: ");
        //mate in 16
        String pos = "" +
                "8/3K4/4r3/4k3/8/8/8/8 b - - 0 1\n" +
                "4k3/8/2r5/8/8/8/8/4K3 b - - 0 1\n" +
                "8/6KR/8/2k5/8/8/8/8 w - - 0 1\n" +
                "8/8/k7/8/8/3K1R2/8/8 w - - 0 1\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KRK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(15_000);
            final int move = engine.simpleSearch(board);
            final boolean condition = aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.print(". ");
        }
        System.out.println();
    }



    @Test
    void KRKlosses() {
        System.out.println("\nsearching my checkmate for KRK positions: ");
        String pos = "" +
                "8/8/8/4k2K/3r4/8/8/8 w - - 0 1\n" +
                "8/3K4/4r3/4k3/8/8/8/8 w - - 0 1\n" +
//                "K7/8/8/1R6/8/8/4k3/8 b - - 0 1\n" +
                "6R1/6K1/8/8/6k1/8/8/8 b - - 0 1\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KRK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(14_000);
            final int move = engine.simpleSearch(board);
            
            final boolean condition = aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(condition);
            System.out.print(". ");
        }
    }   
    
    @Test
    void KBBKwins() {
        System.out.println("\nsearching checkmate for KBBK positions: ");

        String pos = "" +
                "2bb4/8/8/4k3/8/8/2K5/8 b - - 0 1\n" +
                "2bb4/8/8/4k3/8/8/2K5/8 b - - 0 1\n" +
                "k4B2/8/8/8/6B1/8/2K5/8 w - - 0 1\n" +
//                "8/4B3/8/8/8/8/3k4/K2B4 w\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KBBK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(60_000);
            final int move = engine.simpleSearch(board);
            final boolean condition = aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.print(". ");
        }
    }

    @Test
    void KBNKwins() {
        System.out.println("\nsearching checkmate for KBNK positions: ");

        String pos = "" +
                "7k/8/3N4/8/7K/6B1/8/8 w - - 0 1" +
//                "1k6/8/2K1BN2/8/8/8/8/8 w - - 0 1\n" +
//                "8/2B4N/8/2k5/8/8/8/K7 w - - 0 1\n" +
//                "8/8/8/8/4n1b1/8/6K1/4k3 b - - 0 1\n" +
//                "8/5n2/8/7k/8/8/8/3K2b1 b - - 0 1\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KBNK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(90_000);
            final int move = engine.simpleSearch(board);
            final boolean condition = aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail: ");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.print(". ");
        }
    }

    @Test
    void KBNKloss() {
        System.out.println("\nsearching checkmate for KBNK positions: ");

        String pos = "" +
                "7k/8/3N4/8/7K/6B1/8/8 b - - 0 1" +
//                "8/8/8/8/1B6/3K4/8/k2N4 b - - 0 1\n" +
//                "8/2k1bn2/8/8/8/7K/8/8 w - - 0 1\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KBNK, typeOfEndgame(board));
            SearchSpecs.basicTimeSearch(10_000);
            final int move = engine.simpleSearch(board);
            final boolean condition = aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY;
            if (!condition) {
                System.err.println("fail:");
                System.err.println(board.toFenString());
            }
            Assert.assertTrue(condition);
            System.out.print(". ");
        }
    }
    
    @Test
    void mate14() {
        // mate in 14
        DEBUG = false;
        Chessboard board = new Chessboard("kq4n1/4p2Q/1P2P3/1K6/8/8/p7/8");
        SearchSpecs.basicTimeSearch(6_000);
        final int move = engine.simpleSearch(board);

        Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
        Assert.assertEquals("h7e4", MoveParser.toString(move));

        Engine.resetFull();

        // now from black pov, in order to check if uci dtm is correct
        Chessboard bboard = new Chessboard("kq4n1/4p2Q/1P2P3/1K6/8/8/p7/8");
        bboard.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(bboard, "h7e4"));
        quitOnSingleMove = false;
        SearchSpecs.basicTimeSearch(2_000);
        final int bmove = engine.simpleSearch(bboard);
        
        Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
        Assert.assertEquals("b8b7", MoveParser.toString(bmove));

        Engine.resetFull();
    }
}
