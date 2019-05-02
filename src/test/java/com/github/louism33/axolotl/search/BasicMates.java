package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.BoardConstants;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.DEBUG;
import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_PV;
import static com.github.louism33.chesscore.MaterialHashUtil.*;

@Ignore
public class BasicMates {

    // thanks to guido for many of these positions  http://kirill-kryukov.com/chess/discussion-board/viewtopic.php?f=6&t=920
    Engine engine = new Engine();

    @BeforeEach
    void setup() {
        ResettingUtils.reset();
        sendBestMove = false;
    }


    @AfterEach
    void tearDown() {
        ResettingUtils.reset();
        sendBestMove = true;
    }

    @Test
    void KRRKWins() {
        PRINT_PV = true;

        //mate in 7 at worst
        String pos = "" +
                "8/8/8/8/8/3k4/2R5/KR6 w\n" +
                "8/8/8/8/8/3k4/2R5/K5R1 w\n" +
                "8/8/8/8/8/3k4/2R5/K6R w\n" +
                "8/8/8/8/8/3k4/R1R5/K7 w\n" +
                "8/8/8/8/8/3k4/1RR5/K7 w\n" +
                "8/8/8/8/8/3k4/2R3R1/K7 w\n" +

                "8/8/8/8/8/3K4/2r4r/k7 b - - 0 1\n" +
                "8/8/8/8/8/3K4/2r5/k5r1 b - - 0 1\n" +
                "k6r/2r5/3K4/8/8/8/8/8 b - - 0 1\n" +
                "7k/8/6r1/8/5K2/8/8/7r b - - 0 1\n" +

                "7k/7r/7r/2K5/8/8/8/8 b - - 0 1\n" +
                "8/7r/8/8/8/6K1/8/3r3k b - - 0 1\n" +
                "8/8/8/4R3/4K3/3R4/8/7k w - - 0 1\n" +
                "7R/7R/7K/8/8/8/8/k7 w - - 0 1\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KRRK, typeOfEndgame(board));
            engine.receiveSearchSpecs(board, true, 4_000);
            System.out.println("searching checkmate for KRRK: " + board.toFenString());
            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }

    @Test
    void KQKwin() {
        String pos = "" +
                "8/8/8/8/8/8/1Q6/K6k w - - 1 1\n" +
                "8/8/3Q4/8/8/7k/8/3K4 w - - 0 1\n" +
                "8/3q4/8/8/2K1k3/8/8/8 b - - 1 1\n" +
                "8/8/8/8/7q/7k/8/3K4 b - - 0 1\n" +
                
                "8/8/8/5k2/8/8/1Q6/K7 w - - 1 1\n" + // mate in 10
                "8/8/8/5K2/8/8/1q6/k7 b - - 1 1\n" + // mate in 10
                "7k/6q1/8/8/2K5/8/8/8 b - - 1 1\n" + // mate in 10
                "7K/6Q1/8/8/2k5/8/8/8 w - - 1 1\n" + // mate in 10
                "";

        String[] kqkPositions = pos.split("\n");

        for (int i = 0; i < kqkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(kqkPositions[i]);
            Assert.assertEquals(KQK, typeOfEndgame(board));
//            System.out.println(board);
            System.out.println("searching checkmate found for KQK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 10_000);

            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }

    @Test
    @Ignore
    void KQKloss() {
        //loss in 10
        String pos = "" +
                "7K/6Q1/8/8/8/8/2k5/8 b\n" +
                "K7/1Q6/8/8/8/8/3k4/8 b\n" +
                "7K/6Q1/8/8/8/8/3k4/8 b\n" +
                "7K/6Q1/8/8/8/2k5/8/8 b\n" +
                "K7/1Q6/8/8/8/3k4/8/8 b\n" +
                "7K/6Q1/8/8/8/3k4/8/8 b\n" +
                "8/8/8/8/3k4/8/6Q1/7K b\n" +
                "7K/6Q1/8/8/3k4/8/8/8 b" +
                "";

        String[] kqkPositions = pos.split("\n");

        for (int i = 0; i < kqkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(kqkPositions[i]);
            Assert.assertEquals(KQK, typeOfEndgame(board));
            System.out.println("searching my checkmate for KQK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 20_000);

            Evaluator.eval(board, board.generateLegalMoves());
            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }

    @Test
    void KRKWins() {
//        PRINT_PV = true;

        //mate in 16
        String pos = "" +
                "8/8/5k1K/6r1/8/8/8/8 b - -\n" +
                "8/8/8/8/8/2k5/1R6/K7 w\n" +
                "8/8/8/8/8/3k4/2R5/K7 w\n" +
                "8/8/8/8/8/3k4/4R3/K7 w\n" +
                "8/8/2R5/8/8/3k4/8/K7 w\n" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KRK, typeOfEndgame(board));
            System.out.println("searching checkmate for KRK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 10_000);

            Evaluator.eval(board, board.generateLegalMoves());
            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }



    @Test
    void KRKlosses() {
//        PRINT_PV = true;

        //loss in 16
        String pos = "" +
                "K7/8/8/8/8/8/8/3k1R2 b\n" +
                "7K/8/8/8/8/8/8/1R1k4 b\n" +
                "7K/8/8/8/8/8/8/3k1R2 b\n" +
                "8/8/1R6/8/8/8/1k6/7K b\n" +
                "7K/8/8/8/8/8/1k3R2/8 b\n" +
                "8/8/8/8/8/8/1Rk5/K7 b\n" +
                "8/8/8/8/8/8/2k1R3/K7 b\n" +
                "7K/8/5R2/8/8/8/1k6/8 b\n" +
                "8/8/2R5/8/8/8/2k5/K7 b\n" +
                "8/2R5/8/8/8/8/2k5/K7 b" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KRK, typeOfEndgame(board));
            System.out.println("searching my checkmate for KRK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 10_000);

            final int move = engine.simpleSearch();
            Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }   
    
    @Test
    @Ignore
    void KBBKwins() {
//        PRINT_PV = true;

        String pos = "" +
                "8/8/8/8/7B/8/3k4/K2B4 w\n" +
                "8/4B3/8/8/8/8/3k4/K2B4 w\n" +
                "3B4/8/8/8/8/8/3k4/K2B4 w\n" +
                "8/4B3/8/8/8/8/3kB3/K7 w\n" +
                "8/8/8/8/B7/3k4/3B4/K7 w\n" +
                "8/3B4/8/8/8/3k4/3B4/K7 w\n" +
                "4B3/8/8/8/8/3k4/3B4/K7 w\n" +
                "8/8/8/8/7B/4k3/4B3/K7 w\n" +
                "8/4B3/8/8/8/4k3/4B3/K7 w\n" +
                "8/8/8/6B1/2Bk4/8/8/K7 w\n" +
                "8/8/8/8/5kB1/8/8/K3B3 w\n" +
                "4B3/8/8/8/6kB/8/8/K7 w\n" +
                "8/8/8/5kB1/B7/8/8/K7 w\n" +
                "8/8/8/1B3kB1/8/8/8/K7 w\n" +
                "4B3/8/8/5kB1/8/8/8/K7 w" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KBBK, typeOfEndgame(board));
            System.out.println(board);
            System.out.println("searching checkmate for KBBK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 10_000);

            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }

    @Test
    @Ignore
    void KBNKwins() {
//        PRINT_PV = true;

        String pos = "" +
                "8/8/8/8/8/8/3B4/K2k3N w\n" +
                "8/8/7N/8/8/8/8/K2kB3 w\n" +
                "8/8/7N/8/8/8/8/K1k1B3 w\n" +
                "8/6N1/8/8/8/8/3B4/K2k4 w\n" +
                "8/7N/8/8/8/8/8/K2kB3 w\n" +
                "8/7N/8/8/8/8/3B4/K2k4 w\n" +
                "N7/8/8/8/8/8/8/K2kB3 w\n" +
                "N7/8/8/8/8/8/3B4/K2k4 w\n" +
                "8/7N/8/8/8/8/8/K1k1B3 w\n" +
                "N7/8/8/8/7B/8/8/K1k5 w\n" +
                "5N2/8/8/8/8/8/8/K2kB3 w\n" +
                "5N2/8/8/8/8/8/3B4/K2k4 w\n" +
                "6N1/8/8/8/8/8/8/K2kB3 w\n" +
                "6N1/8/8/8/8/8/3B4/K2k4 w\n" +
                "7N/8/8/8/8/8/8/K2kB3 w\n" +
                "7N/8/8/8/8/8/3B4/K2k4 w\n" +
                "6N1/8/8/8/8/8/8/K1k1B3 w\n" +
                "7N/8/8/8/8/8/8/K1k1B3 w\n" +
                "8/8/8/8/8/B7/8/K5kN w\n" +
                "8/4B3/8/8/8/8/8/K5kN w" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KBNK, typeOfEndgame(board));
            System.out.println(board);
            System.out.println("searching checkmate for KBNK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 10_000);

            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }

    @Test
    @Ignore
    void KBNKloss() {
//        PRINT_PV = true;

        String pos = "" +
                "8/8/8/8/8/8/4B3/K1kN4 b\n" +
                "8/8/8/8/8/8/4B3/K1k4N b\n" +
                "8/8/7N/8/8/8/8/K1k1B3 b\n" +
                "8/8/7N/8/7B/8/8/K1k5 b\n" +
                "8/4B3/7N/8/8/8/8/K1k5 b\n" +
                "8/N7/8/8/8/B7/8/K1k5 b\n" +
                "8/7N/8/8/8/8/8/K1k1B3 b\n" +
                "8/7N/8/8/8/B7/8/K1k5 b\n" +
                "N7/8/8/8/8/8/8/K1k1B3 b\n" +
                "N7/8/8/8/8/8/4B3/K1k5 b\n" +
                "N7/8/8/8/8/4B3/8/K1k5 b" +
                "";

        String[] positions = pos.split("\n");

        for (int i = 0; i < positions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(positions[i]);
            Assert.assertEquals(KBNK, typeOfEndgame(board));
            System.out.println(board);
            System.out.println("searching my checkmate for KBNK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 10_000);

            engine.simpleSearch();
            Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
    }
    
    @Test
    void mate14() {
        // mate in 14
        DEBUG = false;
        Chessboard board = new Chessboard("kq4n1/4p2Q/1P2P3/1K6/8/8/p7/8");
        engine.receiveSearchSpecs(board, true, 2000);
        Evaluator.eval(board, board.generateLegalMoves());
        final int move = engine.simpleSearch();
        Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
        Assert.assertEquals("h7e4", MoveParser.toString(move));

        Engine.resetFull();

        // now from black pov, in order to check if uci dtm is correct
        Chessboard bboard = new Chessboard("kq4n1/4p2Q/1P2P3/1K6/8/8/p7/8");
        bboard.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(bboard, "h7e4"));
        quitOnSingleMove = false;
        engine.receiveSearchSpecs(bboard, true, 2000);
        Evaluator.eval(bboard, bboard.generateLegalMoves());
        final int bmove = engine.simpleSearch();
        Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
        Assert.assertEquals("b8b7", MoveParser.toString(bmove));

        Engine.resetFull();
    }
}
