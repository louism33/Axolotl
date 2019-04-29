package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.MaterialHashUtil.*;

public class EndgameEvaluatorTest {

    @Test
    void isKPKTestWhite() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/6Pk/4K3");
        Assert.assertEquals(KPK, typeOfEndgame(board));
    }

    @Test
    void isKPKTestBlack() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/6pk/4K3");
        Assert.assertEquals(KPK, typeOfEndgame(board));
    }

    @Test
    void isKRKTestWhite() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/6Rk/4K3");
        Assert.assertEquals(KRK, typeOfEndgame(board));
    }

    @Test
    void isKRKTestBlack() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/6rk/4K3");
        Assert.assertEquals(KRK, typeOfEndgame(board));
    }

    @Test
    void isKQKTestWhite() {
        Chessboard board = new Chessboard("8/7Q/8/8/8/8/7k/4K3");
        Assert.assertEquals(KQK, typeOfEndgame(board));
    }

    @Test
    void isKQKTestBlack() {
        Chessboard board = new Chessboard("8/7q/8/8/8/8/7k/4K3");
        Assert.assertEquals(KQK, typeOfEndgame(board));
    }

    @Test
    void isKBNKTestWhite() {
        Chessboard board = new Chessboard("8/6BN/8/8/8/8/7k/4K3");
        Assert.assertEquals(KNBK, typeOfEndgame(board));
    }

    @Test
    void isKBNKTestBlack() {
        Chessboard board = new Chessboard("8/6bn/8/8/8/8/7k/4K3");
        Assert.assertEquals(KNBK, typeOfEndgame(board));
    }

    @Test
    void isKNKNTest() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/6nk/3NK3");
        Assert.assertTrue(isBasicallyDrawn(board));
    }

    @Test
    void isKBKBTestDifferentSq() {
        Chessboard board = new Chessboard("6bB/8/8/8/8/8/7k/K7");
        Assert.assertTrue(isBasicallyDrawn(board));
    }
    
    @Test
    void isKBKBTestSameSq() {
        Chessboard board = new Chessboard("5b1B/8/8/8/8/8/7k/K7");
        Assert.assertTrue(isBasicallyDrawn(board));
    }

    @Test
    void isKBBKTestDiffSqBlack() {
        Chessboard board = new Chessboard("6bb/8/8/8/8/8/7k/K7");
        Assert.assertFalse(isBasicallyDrawn(board));
    }
}
