package com.github.louism33.axolotl.search;

import challenges.Utils;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.MaterialHashUtil.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KRK;

public class BasicMates {

    Engine engine = new Engine();

    @BeforeEach
    void setup() {
        Util.reset();
        EngineSpecifications.PRINT_PV = true;
    }


    @AfterEach
    void tearDown() {
        Util.reset();
    }

    @Test
    void KRK() {
        Chessboard board = new Chessboard("8/8/5k1K/6r1/8/8/8/8 b - - 5 143");
//        Assert.assertEquals(KRK, typeOfEndgame(board));
        System.out.println(board);
        engine.receiveSearchSpecs(board, true, 1000);
        
        Evaluator.eval(board, board.generateLegalMoves());
        final int move = engine.simpleSearch();
        Assert.assertTrue(Utils.contains(new String[]{"g5e5", "g5d5", "g5c5", "g5b5", "g5a5"}, MoveParser.toString(move)));
    }

    @Test
    void KQK() {
        Chessboard board = new Chessboard("8/8/5k1K/6q1/8/8/8/8 b - - 5 143");
        Assert.assertEquals(KQK, typeOfEndgame(board));
        
        System.out.println(board);
        engine.receiveSearchSpecs(board, true, 1000);
        final int move = engine.simpleSearch();
        Assert.assertEquals(MoveParser.toString(move), "g5e5");
    }
    /*
    position fen rnb1kbnr/ppqppppp/2p5/8/2P5/2Q5/PP1PPPPP/RNB1KBNR b KQkq - 0 1 moves g8f6 g1f3 d7d5 d2d3 e7e6 c1e3 f8e7 b1d2 e8g8 g2g3 d5c4 e3f4 c7b6 d2c4 b6d8 e2e4 b8a6 c3d4 e7b4 c4d2 d8d4 f3d4 f8d8 d4c2 b4c5 f1e2 a6b4 c2b4 c5b4 a2a3 b4d2 f4d2 b7b6 a1c1 c8b7 d2c3 b7a6 e4e5 f6d5 c3d2 a6b7 e1g1 d8d7 f2f4 f7f5 e5f6 d5f6 b2b4 a8d8 f4f5 e6e5 d2c3 d8e8 c1e1 b7a6 e2f3 d7d3 c3e5 f6d7 e5c3 e8e1 c3e1 d3a3 f3c6 a6f1 g1f1 d7e5 c6d5 g8f8 f1e2 a7a5 b4a5 b6a5 e1f2 a3d3 d5e4 d3d7 f2c5 f8f7 c5e3 a5a4 e3c5 f7f6 h2h4 d7c7 c5b6 c7c3 b6e3 a4a3 e3g5 f6f7 e4d5 f7e8 f5f6 g7f6 g5f6 c3c2 e2d1 c2c5 d5g8 h7h5 d1e2 a3a2 g8a2 e5g4 f6g5 c5c2 e2f3 c2a2 f3e4 e8d7 e4f5 a2a6 g5f4 a6e6 f5g5 e6h6 f4e5 h6c6 e5f4 c6c5 g5g6 d7e6 f4g5 c5c3 g6h5 c3g3 h5g6 g4e5 g6h6 e5f7 h6g6 g3g2 g6g7 f7g5 h4g5 g2g5 g7h6 e6f5 h6h7 f5f6 h7h6 
     */
}
