package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import standalone.KPK;

import java.util.ArrayList;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;
import static standalone.KPK.*;

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
    @Disabled
    void basic() {
        // pawn promote immediately
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 12, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 0, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 1, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 2, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 3, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 4, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 5, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 6, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(30, 51, 7, WHITE));

        // cap pawn immediately
        Assert.assertFalse(probePKPForWinForWhite(63, 17, 18, BLACK));
        Assert.assertFalse(probePKPForWinForWhite(0, 17, 18, BLACK));

        Assert.assertTrue(probePKPForWinForWhite(50, 51, 0, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(52, 51, 0, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(52, 51, 7, WHITE));
        Assert.assertTrue(!probePKPForWinForWhite(0, 51, 50, WHITE));

        Assert.assertTrue(probePKPForWinForWhite(52, 51, 7, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(53, 51, 7, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(53, 50, 7, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(40, 50, 7, WHITE));


        Assert.assertFalse(probePKPForWinForWhite(0, 51, 50, BLACK));
        Assert.assertFalse(probePKPForWinForWhite(0, 51, 59, BLACK));
        Assert.assertFalse(probePKPForWinForWhite(0, 51, 52, BLACK));
        
        Assert.assertTrue(probePKPForWinForWhite(56, 57-8, 0, WHITE));    
        Assert.assertTrue(probePKPForWinForWhite(56, 57-8, 0, BLACK));    
        Assert.assertTrue(probePKPForWinForWhite(56, 56-8, 0, BLACK));    

        Assert.assertTrue(probe(56, 58-8, 0, WHITE, WHITE));
        Assert.assertTrue(probe(56, 57-8, 0, WHITE, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(56, 57-8, 0, WHITE));
        Assert.assertTrue(probePKPForWinForWhite(56, 56 - 8, 0, WHITE));
        Assert.assertTrue(probe(56, 56-8, 0, WHITE, WHITE));

        Assert.assertTrue(probe(30, 51, 12, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 0, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 1, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 2, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 3, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 4, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 5, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 6, WHITE, WHITE));
        Assert.assertTrue(probe(30, 51, 7, WHITE, WHITE));

        // cap pawn immediately
        Assert.assertFalse(probe(63, 17, 18, BLACK, WHITE));
        Assert.assertFalse(probe(0, 17, 18, BLACK, WHITE));

        Assert.assertTrue(probe(50, 51, 0, WHITE, WHITE));
        Assert.assertTrue(probe(52, 51, 0, WHITE, WHITE));
        Assert.assertTrue(probe(52, 51, 7, WHITE, WHITE));
        Assert.assertTrue(!probe(0, 51, 50, WHITE, WHITE));

        Assert.assertTrue(probe(52, 51, 7, WHITE, WHITE));
        Assert.assertTrue(probe(53, 51, 7, WHITE, WHITE));
        Assert.assertTrue(probe(53, 50, 7, WHITE, WHITE));
        Assert.assertTrue(probe(40, 50, 7, WHITE, WHITE));


        Assert.assertFalse(probe(0, 51, 50, BLACK, WHITE));
        Assert.assertFalse(probe(0, 51, 59, BLACK, WHITE));
        Assert.assertFalse(probe(0, 51, 52, BLACK, WHITE));

        Assert.assertTrue(!probe(30, 51, 12, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 0, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 1, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 2, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 3, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 4, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 5, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 6, WHITE, BLACK));
        Assert.assertTrue(!probe(30, 51, 7, WHITE, BLACK));
        
        Assert.assertTrue(probe(63, 8, 63 - 16, BLACK, BLACK));
    }

    @Test
    void basic2() {
//        Assert.assertTrue(probe(48, 41+8, 44, WHITE));
//        Assert.assertTrue(probe(48, 41+8, 51, BLACK));
//        Assert.assertTrue(probe(48, 41, 51, WHITE));
//        Assert.assertTrue(probe(48, 41, 58, BLACK));
//        Assert.assertTrue(probe(40, 41, 58, WHITE));
        
        System.out.println("******************************************");

//        final boolean probe = probe(40, 41, 50, BLACK);
//        Assert.assertTrue(probe);
//        Assert.assertTrue(probe(40, 41-8, 50, WHITE));
        final boolean probe1 = probe(40, 41 - 8, 57, BLACK);
        Assert.assertTrue(probe1);
        Assert.assertTrue(probe(41, 41-8, 57, WHITE));
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
        KPK.generateKPKBitbase();
//        Assert.assertTrue(!probe(new Chessboard("8/8/8/7K/8/8/P7/k7 b")));
    }
    
    public static void print(KPKPosition ck) {
        System.out.println("turn white " + (ck.us == WHITE));
        System.out.println("strong king");
        Art.printLong(newPieceOnSquare(ck.ksq[WHITE]));
        System.out.println("weak king");
        Art.printLong(newPieceOnSquare(ck.ksq[BLACK]));
        System.out.println("pawn");
        System.out.println(ck.psq);
        Art.printLong(newPieceOnSquare(ck.psq));
        System.out.println();
        System.out.println();
        System.out.println("res:");
        System.out.println(ck.result);
        System.out.println();
        System.out.println();
        System.out.println("********************************");
        System.out.println();
        System.out.println();
    }

    @Test
    @Disabled
    void abvc() {
        KPK.generateKPKBitbase();

        int a = 0;
        a |= 40; // white king
        a |= 50 << 6; // black king
        a |= 1 << 12;
        a |= 1 << 13; // f
        a |= 1 << 15; // r
        final KPKPosition ak = new KPKPosition(a);
        final int aaa = ak.result;
        print(ak);

        final boolean probe = probe(40, 41, 50, BLACK);
        System.out.println(probe);
        System.out.println();
    }
    
    
    @Test
    @Disabled
    void a() {
//        final ArrayList<KPKPosition> db = new ArrayList<>();
//        for (int idx = 0; idx < MAX_INDEX; ++idx) {
//            final KPKPosition e = new KPKPosition(idx);
//            db.add(e);
//        }

        
        int a = 0;
        a |= 32; // white king
        a |= 56 << 6; // black king
//        a |= 1 << 12;
        a |= 1 << 13; // f
        a |= 0 << 15; // r
        final KPKPosition ak = new KPKPosition(a);
//        db.set(a, ak);
        final int aaa = ak.result;

        print(ak);
    }
    

    @Test
    @Disabled
    void aaa() {

        final ArrayList<KPKPosition> db = new ArrayList<>();
        for (int idx = 0; idx < MAX_INDEX; ++idx) {
            final KPKPosition e = new KPKPosition(idx);
            db.add(e);
        }
        int a = 0;
        a |= 56-16; // white king
        a |= 19 << 6; // black king
        a |= 1 << 12; // turn
        a |= 2 << 13; // f
        a |= 4 << 15; // r
        final KPKPosition ak = new KPKPosition(a);
        db.set(a, ak);
        final int aaa = ak.result <= 1 ? ak.classify(db) : ak.result;

        print(ak);

//        int b = 0;
//        b |= 57; // white king
//        b |= 8 << 6; // black king
//        b |= 0 << 13; // f
//        b |= 0 << 15; // r
//        final KPKPosition bk = new KPKPosition(b);
//        db.set(b, bk);
//        final int bbb = bk.result <= 1 ? bk.classify(db) : bk.result;
//
//        print(bk);
//
//        int c = 0;
//        c |= 57; // white king
//        c |= 9 << 6; // black king
//        c |= 0 << 13; // f
//        c |= 0 << 15; // r
//        final KPKPosition ck = new KPKPosition(c);
//        db.set(c, ck);
//        final int ccc = ck.result <= 1 ? ck.classify(db) : ck.result;
//
//        print(ck);
//        
//        int ii = 0;
//        ii |= 57; // white king
//        ii |= 0 << 6; // black king
//        ii |= 1 << 12; // turn
//        ii |= 0 << 13; // f
//        ii |= 0 << 15; // r
//        final KPKPosition kkzero = new KPKPosition(ii);
//        db.set(ii, kkzero);
////        final int classify3 = kkzero.classify(db);
//        print(kkzero);
//        
//        final int classify3 = kkzero.result <= 1 
//                ? kkzero.classify(db) 
//                : kkzero.result;
//
//
//        
//
//        System.out.println();
//        System.out.println("classify: " + classify3);
//        
//        
//        
//        int e = 0;
//        e |= 56; // white king
//        e |= 0 << 6; // black king
//        e |= 0 << 13; // f
//        e |= 0 << 15; // r
//        final KPKPosition kpkPosition2 = new KPKPosition(e);
//        final int classify2 = kpkPosition2.classify(db);
//

    }


    @Test
    public void testDraw() {
//        Assert.assertTrue(!KPK.probe(new Chessboard("7k/7P/7K/8/8/8/8/8 b - - 0 0")));
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
