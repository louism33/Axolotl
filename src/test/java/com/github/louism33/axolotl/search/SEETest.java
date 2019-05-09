//package com.github.louism33.axolotl.search;
//
//import com.github.louism33.chesscore.Chessboard;
//import com.github.louism33.utils.MoveParserFromAN;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//
//public class SEETest {
//
//    @Test
//    public void rxpTest() {
//        Chessboard board = new Chessboard("1k1r4/1pp4p/p7/4p3/8/P5P1/1PP4P/2K1R3 w - -");
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Rxe5"));
//        Assert.assertEquals(100, see);
//    }
//
//    @Test
//    public void nxe5Test() {
//        Chessboard board = new Chessboard("1k1r3q/1ppn3p/p4b2/4p3/8/P2N2P1/1PP1R1BP/2K1Q3 w - -");
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Nxe5"));
//        Assert.assertEquals(see, -225);
//    }
//
//    @Test
//    public void winningCapTest() {
//        Chessboard board = new Chessboard();
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "a7a6"));
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "f3e5"));
//        Assert.assertTrue(see > 0);
//    }
//
//    @Test
//    public void losingCapTest() {
//        Chessboard board = new Chessboard();
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7d5"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "a7a6"));
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "c3d5"));
//        Assert.assertTrue(see < 0);
//    }
//
//    @Test
//    public void neutralTest() {
//        Chessboard board = new Chessboard();
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7d5"));
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "e4d5"));
//        Assert.assertEquals(0, see);
//    }
//
//    @Test
//    public void kingRecapTest() {
//        Chessboard board = new Chessboard("3r4/1pp4p/p3k3/4p3/8/P5P1/1PP4P/2K1R3 w - -");
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Rxe5"));
//        Assert.assertEquals(-400, see);
//    }
//
//    @Test
//    public void kingDoesNotRecapTest() {
//        Chessboard board = new Chessboard("3r4/1pp4p/p3k3/4p3/8/P5P1/1PP1R2P/2K1R3 w - -");
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Rxe5"));
//        Assert.assertEquals(100, see);
//    }
//
//    @Test
//    public void maxHighTest() {
//        Chessboard board = new Chessboard("k7/8/8/3q4/4P3/4K3/8/8 w KQkq -");
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "e4d5"));
//        Assert.assertEquals(900, see);
//    }
//
//    @Test
//    public void maxLowTest() {
//        Chessboard board = new Chessboard("k7/8/8/3q4/4P3/5P2/8/K7 b KQkq -");
//        board.generateLegalMoves();
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "d5e4"));
//        Assert.assertEquals(-800, see);
//    }
//
//    // below tests based on carballo's tests
//
//    void testSee(String fen, String m, int seeScore) {
//        Chessboard board = new Chessboard(fen);
//        board.generateLegalMoves();
//        System.out.println(board);
//        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, m));
//        Assert.assertEquals(seeScore, see);
//    }
//
//    @Test
//    public void test1() {
//        testSee("1k1r4/1pp4p/p7/4p3/8/P5P1/1PP4P/2K1R3 w - -", "Rxe5", 100);
//    }
//
//    @Test
//    public void test2() {
//        testSee("5K1k/8/8/8/8/8/1r6/Rr6 w QKqk - 0 0", "a1b1", 0);
//    }
//
//    @Test
//    public void test3() {
//        testSee("5K1k/8/8/8/8/8/b7/RrR5 w QKqk - 0 0", "a1b1", 350);
//    }
//
//    @Test
//    public void test4() {
//        testSee("1k1r3q/1ppn3p/p4b2/4p3/8/P2N2P1/1PP1R1BP/2K1Q3 w - -", "Ne5", -225);
//    }
//
//    @Test
//    public void testNoOtherPiecesAttack() {
//        testSee("rq2r1k1/5pp1/p7/5NP1/1p2P2P/8/PQ4K1/5R1R b - - 0 2", "Re8xe4", 100);
//    }
//
//    @Test
//    public void testSeeError() {
//        testSee("8/1kb2p2/4b1p1/8/2Q2NB1/8/8/K7 w - - 0 1", "Nf4xe6", 105);
//    }
//
//    @Test
//    public void testSeeEnpassant() {
//        testSee("7k/8/8/8/2pP1n2/8/2B5/7K b - d3 0 1", "c4xd3", 100);
//    }
//}
