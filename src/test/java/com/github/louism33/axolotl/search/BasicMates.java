package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.util.ResettingUtils;
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
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.chesscore.MaterialHashUtil.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KRK;

@Ignore
public class BasicMates {

    // thanks to guido for many of these positions  http://kirill-kryukov.com/chess/discussion-board/viewtopic.php?f=6&t=920
    Engine engine = new Engine();

    @BeforeEach
    void setup() {
        ResettingUtils.reset();
        PRINT_PV = true;
    }


    @AfterEach
    void tearDown() {
        ResettingUtils.reset();
    }

    @Test
    void KQKwin() {
//        PRINT_PV = false;

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
            System.out.println(board);
            System.out.println("searching checkmate found for KQK: " + board.toFenString());
            engine.receiveSearchSpecs(board, true, 20_000);

            engine.simpleSearch();
            System.out.println("score: " + aiMoveScore);
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
            System.out.println("checkmate found");
        }
        
    }

    @Test
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
            System.out.println(board);
            engine.receiveSearchSpecs(board, true, 60000);

            Evaluator.eval(board, board.generateLegalMoves());
            engine.simpleSearch();
            System.out.println("score: " + aiMoveScore);
            Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
        }
    }

    @Test
    void KRKWins() {
        PRINT_PV = true;

        //mate in 16
        String pos = "" +
                "8/8/5k1K/6r1/8/8/8/8 b - -\n" +
                "8/8/8/8/8/2k5/1R6/K7 w\n" +
                "8/8/8/8/8/3k4/2R5/K7 w\n" +
                "8/8/8/8/8/3k4/4R3/K7 w\n" +
                "8/8/2R5/8/8/3k4/8/K7 w\n" +
//                "8/8/8/8/8/4k3/3R4/K7 w\n" +
//                "8/2R5/8/8/8/3k4/8/K7 w\n" +
//                "8/8/8/8/8/4k3/5R2/K7 w\n" +
//                "2R5/8/8/8/8/3k4/8/K7 w\n" +
//                "8/8/8/8/8/5k2/6R1/K7 w\n" +
//                "8/8/5R2/8/8/4k3/8/K7 w\n" +
//                "8/5R2/8/8/8/4k3/8/K7 w\n" +
//                "8/8/6R1/8/8/5k2/8/K7 w\n" +
//                "5R2/8/8/8/8/4k3/8/K7 w\n" +
//                "8/8/8/8/3k4/8/1R6/K7 w\n" +
//                "8/8/8/8/4k3/8/1R6/K7 w\n" +
//                "8/8/8/8/3k4/8/2R5/K7 w\n" +
//                "8/8/8/8/4k3/8/2R5/K7 w\n" +
//                "8/8/8/8/3k4/8/5R2/K7 w\n" +
//                "8/8/8/8/4k3/8/5R2/K7 w\n" +
//                "8/8/8/8/3k4/2R5/8/K7 w\n" +
//                "8/8/8/8/4k3/8/6R1/K7 w\n" +
//                "8/8/8/8/3k4/5R2/8/K7 w\n" +
//                "8/8/8/8/4k3/8/7R/K7 w\n" +
//                "8/8/8/8/3k4/6R1/8/K7 w\n" +
//                "8/8/8/8/4k3/1R6/8/K7 w\n" +
//                "8/8/8/8/4k3/2R5/8/K7 w\n" +
//                "8/8/8/8/4k3/3R4/8/K7 w\n" +
//                "8/8/5R2/8/3k4/8/8/K7 w\n" +
//                "8/8/8/8/4k3/5R2/8/K7 w\n" +
//                "8/8/6R1/8/3k4/8/8/K7 w\n" +
//                "8/8/8/8/4k3/6R1/8/K7 w\n" +
//                "8/8/8/8/4k3/7R/8/K7 w\n" +
//                "8/6R1/8/8/3k4/8/8/K7 w\n" +
//                "8/8/1R6/8/4k3/8/8/K7 w\n" +
//                "8/8/2R5/8/4k3/8/8/K7 w\n" +
//                "8/8/3R4/8/4k3/8/8/K7 w\n" +
//                "8/8/5R2/8/4k3/8/8/K7 w\n" +
//                "8/8/8/8/5k2/2R5/8/K7 w\n" +
//                "8/8/6R1/8/4k3/8/8/K7 w\n" +
//                "8/8/8/8/5k2/6R1/8/K7 w\n" +
//                "8/8/7R/8/4k3/8/8/K7 w\n" +
//                "8/1R6/8/8/4k3/8/8/K7 w\n" +
//                "8/2R5/8/8/4k3/8/8/K7 w\n" +
//                "8/8/2R5/8/5k2/8/8/K7 w\n" +
//                "8/5R2/8/8/4k3/8/8/K7 w\n" +
//                "8/8/3R4/8/5k2/8/8/K7 w\n" +
//                "8/6R1/8/8/4k3/8/8/K7 w\n" +
//                "8/8/6R1/8/5k2/8/8/K7 w\n" +
//                "8/7R/8/8/4k3/8/8/K7 w\n" +
//                "8/2R5/8/8/5k2/8/8/K7 w\n" +
//                "1R6/8/8/8/4k3/8/8/K7 w\n" +
//                "8/6R1/8/8/5k2/8/8/K7 w\n" +
//                "2R5/8/8/8/4k3/8/8/K7 w\n" +
//                "5R2/8/8/8/4k3/8/8/K7 w\n" +
//                "6R1/8/8/8/4k3/8/8/K7 w\n" +
//                "7R/8/8/8/4k3/8/8/K7 w\n" +
//                "8/8/8/8/6k1/7R/8/K7 w\n" +
//                "8/8/8/4k3/8/8/1R6/K7 w\n" +
//                "8/8/8/4k3/8/8/2R5/K7 w\n" +
//                "8/8/8/4k3/8/8/5R2/K7 w\n" +
//                "8/8/8/4k3/8/8/6R1/K7 w\n" +
//                "8/8/8/5k2/8/2R5/8/K7 w\n" +
//                "8/8/8/4k3/8/8/7R/K7 w\n" +
//                "8/8/8/5k2/8/6R1/8/K7 w\n" +
//                "8/8/8/4k3/8/2R5/8/K7 w\n" +
//                "8/8/8/5k2/8/7R/8/K7 w\n" +
//                "8/8/8/4k3/8/5R2/8/K7 w\n" +
//                "8/8/8/5k2/6R1/8/8/K7 w\n" +
//                "8/8/8/4k3/8/6R1/8/K7 w\n" +
//                "8/8/2R5/5k2/8/8/8/K7 w\n" +
//                "8/8/8/4k3/8/7R/8/K7 w\n" +
//                "8/8/3R4/5k2/8/8/8/K7 w\n" +
//                "8/8/8/4k3/3R4/8/8/K7 w\n" +
//                "8/8/6R1/5k2/8/8/8/K7 w\n" +
//                "8/8/8/4k3/5R2/8/8/K7 w\n" +
//                "8/8/7R/5k2/8/8/8/K7 w\n" +
//                "8/2R5/8/5k2/8/8/8/K7 w\n" +
//                "8/8/5R2/4k3/8/8/8/K7 w\n" +
//                "8/6R1/8/5k2/8/8/8/K7 w\n" +
//                "8/8/6R1/4k3/8/8/8/K7 w\n" +
//                "8/7R/8/5k2/8/8/8/K7 w\n" +
//                "8/8/7R/4k3/8/8/8/K7 w\n" +
//                "8/6R1/8/4k3/8/8/8/K7 w\n" +
//                "8/7R/8/4k3/8/8/8/K7 w\n" +
//                "7R/8/8/4k3/8/8/8/K7 w\n" +
//                "8/8/7R/6k1/8/8/8/K7 w\n" +
//                "8/8/5k2/8/8/2R5/8/K7 w\n" +
//                "8/8/5k2/8/8/6R1/8/K7 w\n" +
//                "8/6R1/5k2/8/8/8/8/K7 w\n" +
//                "8/7R/5k2/8/8/8/8/K7 w\n" +
//                "8/7R/6k1/8/8/8/8/K7 w\n" +
//                "8/8/8/8/8/3k4/2R5/1K6 w\n" +
//                "8/8/2R5/8/8/3k4/8/1K6 w\n" +
//                "8/8/8/8/8/4k3/5R2/1K6 w\n" +
//                "8/8/5R2/8/8/4k3/8/1K6 w\n" +
//                "8/8/8/8/3k4/8/2R5/1K6 w\n" +
//                "8/8/8/8/3k4/8/5R2/1K6 w\n" +
//                "8/8/8/8/3k4/5R2/8/1K6 w\n" +
//                "8/8/2R5/8/3k4/8/8/1K6 w\n" +
//                "8/8/8/8/4k3/8/2R5/1K6 w\n" +
//                "8/8/5R2/8/3k4/8/8/1K6 w\n" +
//                "8/8/8/8/4k3/8/5R2/1K6 w\n" +
//                "8/8/8/8/4k3/5R2/8/1K6 w\n" +
//                "8/8/2R5/8/4k3/8/8/1K6 w\n" +
//                "8/8/3R4/8/4k3/8/8/1K6 w\n" +
//                "8/8/8/8/5k2/6R1/8/1K6 w\n" +
//                "8/8/5R2/8/4k3/8/8/1K6 w\n" +
//                "8/8/8/3k4/8/5R2/8/1K6 w\n" +
//                "8/8/1R6/2k5/8/8/8/1K6 w\n" +
//                "8/8/2R5/3k4/8/8/8/1K6 w\n" +
//                "8/8/5R2/3k4/8/8/8/1K6 w\n" +
//                "8/8/8/4k3/8/5R2/8/1K6 w\n" +
//                "8/8/2R5/4k3/8/8/8/1K6 w\n" +
//                "8/8/3R4/4k3/8/8/8/1K6 w\n" +
//                "8/8/5R2/4k3/8/8/8/1K6 w\n" +
//                "8/8/3R4/5k2/8/8/8/1K6 w\n" +
//                "8/8/6R1/5k2/8/8/8/1K6 w\n" +
//                "8/2R5/3k4/8/8/8/8/1K6 w\n" +
//                "8/3R4/4k3/8/8/8/8/1K6 w\n" +
//                "8/5R2/4k3/8/8/8/8/1K6 w\n" +
//                "8/6R1/5k2/8/8/8/8/1K6 w" +
                "";

        String[] krkPositions = pos.split("\n");

        for (int i = 0; i < krkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(krkPositions[i]);
            Assert.assertEquals(KRK, typeOfEndgame(board));
            System.out.println(board);
            engine.receiveSearchSpecs(board, true, 10000);

            Evaluator.eval(board, board.generateLegalMoves());
            final int move = engine.simpleSearch();
            System.out.println("best move: " + MoveParser.toString(move));
            System.out.println("score: " + aiMoveScore);
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
        }
    }

    @Test
    void KRRKWins() {
        PRINT_PV = true;

        //mate in 7
        String pos = "" +
                "8/8/8/8/8/3k4/2R5/KR6 w\n" +
                "8/8/8/8/8/3k4/2R5/K5R1 w\n" +
                "8/8/8/8/8/3k4/2R5/K6R w\n" +
                "8/8/8/8/8/3k4/R1R5/K7 w\n" +
                "8/8/8/8/8/3k4/1RR5/K7 w\n" +
                "8/8/8/8/8/3k4/2R3R1/K7 w\n" +
                "8/8/8/8/8/3k4/2R4R/K7 w\n" +
                "8/8/7R/8/8/3k4/2R5/K7 w\n" +
                "8/R7/8/8/8/3k4/2R5/K7 w\n" +
                "8/1R6/8/8/8/3k4/2R5/K7 w" +
                "";

        String[] krkPositions = pos.split("\n");

        for (int i = 0; i < krkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(krkPositions[i]);
//            Assert.assertEquals(KRRK, typeOfEndgame(board));
            System.out.println(board);
            engine.receiveSearchSpecs(board, true, 10000);

            Evaluator.eval(board, board.generateLegalMoves());
            final int move = engine.simpleSearch();
            System.out.println("best move: " + MoveParser.toString(move));
            System.out.println("score: " + aiMoveScore);
            Assert.assertTrue(aiMoveScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
        }
    }

    @Test
    void KRKlosses() {
        PRINT_PV = true;

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

        String[] krkPositions = pos.split("\n");

        for (int i = 0; i < krkPositions.length; i++) {
            Engine.resetFull();
            Chessboard board = new Chessboard(krkPositions[i]);
            Assert.assertEquals(KRK, typeOfEndgame(board));
            System.out.println(board);
            engine.receiveSearchSpecs(board, true, 10000);

            Evaluator.eval(board, board.generateLegalMoves());
            final int move = engine.simpleSearch();
            System.out.println("best move: " + MoveParser.toString(move));
            System.out.println("score: " + aiMoveScore);
            Assert.assertTrue(aiMoveScore < IN_CHECKMATE_SCORE_MAX_PLY);
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
