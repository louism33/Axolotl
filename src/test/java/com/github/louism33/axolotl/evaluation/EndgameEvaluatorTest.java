package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.chesscore.MaterialHashUtil.*;

public class EndgameEvaluatorTest {

    Engine engine = new Engine();
    
    @BeforeAll
    static void setup() {
        ResettingUtils.reset();
    }
    
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
        Assert.assertEquals(KBNK, typeOfEndgame(board));
    }

    @Test
    void isKBNKTestBlack() {
        Chessboard board = new Chessboard("8/6bn/8/8/8/8/7k/4K3");
        Assert.assertEquals(KBNK, typeOfEndgame(board));
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
        Assert.assertEquals(KBBK, typeOfEndgame(board));
    }

    @Test
    void isKRRKTestBlack() {
        Chessboard board = new Chessboard("rr6/8/8/8/8/8/7K/k7");
        Assert.assertFalse(isBasicallyDrawn(board));
        Assert.assertEquals(KRRK, typeOfEndgame(board));
    }
    
    @Test
    void isKRRKTestWhite() {
        Chessboard board = new Chessboard("RR6/8/8/8/8/8/7k/K7");
        Assert.assertFalse(isBasicallyDrawn(board));
        Assert.assertEquals(KRRK, typeOfEndgame(board));
    }
    
    @Test
    void krrk() {
        Chessboard board = new Chessboard("rr6/8/8/8/8/8/7K/k7 b");
        System.out.println(board);
        System.out.println(board.typeOfGameIAmIn);
    }

    @Test
    void compareValuesOfEndgames() {
        // longest dtm

        int krrk = Evaluator.eval(new Chessboard("rr6/8/8/8/8/8/7K/k7 b")); // 7
        int kqk = Evaluator.eval(new Chessboard("8/7q/8/8/8/8/7k/4K3 b"));  // 10
        int krk = Evaluator.eval(new Chessboard("8/8/8/8/8/8/6Rk/4K3 w"));  // 16
        int kbbk = Evaluator.eval(new Chessboard("6bb/8/8/8/8/8/7k/K7 b")); // 19
        int kpk = Evaluator.eval(new Chessboard("8/8/8/8/8/8/6Pk/4K3 w"));  // 28
        int kbnk = Evaluator.eval(new Chessboard("8/6bn/8/8/8/8/7k/4K3 b"));// 33
        
        Assert.assertTrue(krrk > kqk);
        Assert.assertTrue(kqk > krk);
        Assert.assertTrue(krk > kbbk);
        Assert.assertTrue(kbbk > kbnk);
        
//        Assert.assertTrue(kbbk > kpk); // make sure kpk is a won one
//        Assert.assertTrue(kpk > kbnk);
    }

    @Test
    void comparePureEndgameToEndgamePlusMaterialKRRK() {
        int krrkPure = Evaluator.eval(new Chessboard("rr6/8/8/8/8/8/7K/k7 b"));
        int krrkExtra = Evaluator.eval(new Chessboard("rrr5/8/8/8/8/8/7K/k7 b"));

        Assert.assertTrue(krrkExtra > krrkPure);
    }

    @Test
    void KBNKCompareCorrectCornerBadCorner() {
        final Chessboard board = new Chessboard("n7/8/8/8/8/7K/8/1B5k w");
        int g = Evaluator.eval(board);
        System.out.println(board);
        System.out.println(g);
        int krrkExtra = Evaluator.eval(new Chessboard("rrr5/8/8/8/8/8/7K/k7 b"));

    }
}
