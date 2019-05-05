package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.evaluation.KPK.*;

@SuppressWarnings("ALL")
public class PKPTest {

    @Test
    void flip() {
        Assert.assertEquals(0, flipCentre(7));
        Assert.assertEquals(1, flipCentre(6));
        Assert.assertEquals(2, flipCentre(5));
        Assert.assertEquals(3, flipCentre(4));
        Assert.assertEquals(4, flipCentre(3));
        Assert.assertEquals(5, flipCentre(2));
        Assert.assertEquals(6, flipCentre(1));
        Assert.assertEquals(7, flipCentre(0));
        Assert.assertEquals(56, flipCentre(63));
        
        
        Assert.assertEquals(63, flipUpDown(7));
        Assert.assertEquals(7, flipUpDown(63));
        Assert.assertEquals(0, flipUpDown(56));
    }

    @Test
    void cbTestWhiteSimpleWins() {
        Assert.assertTrue(probe(new Chessboard("8/P7/8/8/k6K/8/8/8 w")));
        Assert.assertTrue(probe(new Chessboard("8/7P/8/8/k6K/8/8/8 w")));
        Assert.assertTrue(probe(new Chessboard("8/7P/8/8/8/8/8/k6K w")));
        Assert.assertTrue(probe(new Chessboard("8/8/7P/8/8/8/8/k6K w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/7P/8/8/8/k6K w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/8/7P/8/8/k6K w")));
        
        Assert.assertTrue(probe(new Chessboard("7K/8/8/8/7P/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/7K/8/8/7P/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/7K/8/7P/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/7K/7P/8/8/k7 w")));
        
        Assert.assertTrue(probe(new Chessboard("8/P7/8/7K/8/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/P7/7K/8/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/P6K/8/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/7K/P7/8/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/7K/8/P7/8/k7 w")));
        Assert.assertTrue(probe(new Chessboard("8/8/8/7K/8/8/P7/k7 w")));
    }

    @Test
    void cbTestBlackSimpleDraws() {
        Assert.assertTrue(!probe(new Chessboard("8/8/8/7K/8/8/P7/k7 b")));
    }
    

    @Test
    public void testDraw() {
        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/7K/8/8/8/8/8 b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/7K/8/8/8/8/8 w - - 0 0")));
        
        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/8/7K/8/8/8/8 w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/8/7K/8/8/8/8 b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/8/8/8/8/7K/8 b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/8/8/8/8/7K/8 w - - 0 0")));
        
        Assert.assertTrue(!KPK.probe(new Chessboard("8/6kP/8/8/8/8/7K/8 w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/6kP/8/8/8/8/7K/8 b - - 0 0")));
        
        Assert.assertTrue(!KPK.probe(new Chessboard("8/5k1P/8/8/8/8/7K/8 b - - 0 0")));
        Assert.assertTrue(KPK.probe(new Chessboard("8/5k1P/8/8/8/8/7K/8 w - - 0 0")));
    }

    @Test
    public void testMore() {
        Assert.assertTrue(KPK.probe(new Chessboard("8/k7/8/6K1/7P/8/8/8 w - - 0 0")));
    }

    @Test
    public void testPawnPromotion() {
        Assert.assertTrue(KPK.probe(new Chessboard("8/5k1P/8/8/8/7K/8/8 w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/5k1P/8/8/8/7K/8/8 b - - 0 0")));
    }

    @Test
    public void testKingCapturesPawn() {
        Assert.assertTrue(!KPK.probe(new Chessboard("8/6kP/8/8/8/7K/8/8 b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/4kP2/8/8/7K/8/8 b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/2Pk4/8/8/K7/8/8 b - - 0 0")));
        
        Assert.assertTrue(KPK.probe(new Chessboard("7K/8/8/8/8/8/4kP2/8 w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("7K/8/8/8/8/8/4kP2/8 b - - 0 0")));

        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/4kP2/8/8/8/8/7K w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("7K/8/8/8/8/4kP2/8/8 w - - 0 0")));
        
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/4Kp2/8/8/8/8/7k b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/4Kp2/8/8/7k/8/8 b - - 0 0")));
        
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/2pK4/8/8/k7/8/8 b - - 0 0")));
    }

    @Test
    public void testPositions() {
        Assert.assertTrue(KPK.probe(new Chessboard("8/1k6/8/8/8/7K/7P/8 w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/8/p7/k7/4K3/8/8 w - - 0 0")));
        
        Assert.assertTrue(KPK.probe(new Chessboard("6k1/8/6K1/6P1/8/8/8/8 w - - 0 0")));
        
        Assert.assertTrue(KPK.probe(new Chessboard("8/8/8/6p1/7k/8/6K1/8 b - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("8/8/8/1p6/1k6/8/8/1K6 w - - 0 0")));
        Assert.assertTrue(!KPK.probe(new Chessboard("5k2/8/2K1P3/8/8/8/8/8 b - - 0 0")));
        
    }

}
