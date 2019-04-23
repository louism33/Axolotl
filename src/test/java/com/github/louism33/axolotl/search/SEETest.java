package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SEETest {
    
    @Test
    public void rxpTest() {
        Chessboard board = new Chessboard("1k1r4/1pp4p/p7/4p3/8/P5P1/1PP4P/2K1R3 w - -");
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Rxe5"));
        Assert.assertEquals(100, see);
    }   
    
    @Test
    public void nxe5Test() {
        Chessboard board = new Chessboard("1k1r3q/1ppn3p/p4b2/4p3/8/P2N2P1/1PP1R1BP/2K1Q3 w - -");
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Nxe5"));
        Assert.assertEquals(see, -225);
    }

    @Test
    public void winningCapTest() {
        Chessboard board = new Chessboard();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "a7a6"));
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "f3e5"));
        Assert.assertTrue(see > 0);
    }

    @Test
    public void losingCapTest() {
        Chessboard board = new Chessboard();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7d5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "a7a6"));
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "c3d5"));
        Assert.assertTrue(see < 0);
    }

    @Test
    public void neutralTest() {
        Chessboard board = new Chessboard();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7d5"));
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "e4d5"));
        Assert.assertEquals(0, see);
    }

    @Test
    public void kingRecapTest() {
        Chessboard board = new Chessboard("3r4/1pp4p/p3k3/4p3/8/P5P1/1PP4P/2K1R3 w - -");
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Rxe5"));
        Assert.assertEquals(-400, see);
    }

    @Test
    public void kingDoesNotRecapTest() {
        Chessboard board = new Chessboard("3r4/1pp4p/p3k3/4p3/8/P5P1/1PP1R2P/2K1R3 w - -");
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "Rxe5"));
        Assert.assertEquals(100, see);
    }

    @Test
    public void maxHighTest() {
        Chessboard board = new Chessboard("k7/8/8/3q4/4P3/4K3/8/8 w KQkq -");
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "e4d5"));
        Assert.assertEquals(900, see);
    }

    @Test
    public void maxLowTest() {
        Chessboard board = new Chessboard("k7/8/8/3q4/4P3/5P2/8/K7 b KQkq -");
        board.generateLegalMoves();
        final int see = SEE.getSEE(board, MoveParserFromAN.buildMoveFromANWithOO(board, "d5e4"));
        Assert.assertEquals(-800, see);
    }

    @Test
    public void aTest() {
        Chessboard board = new Chessboard("8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -");
//        Engine.searchFixedDepth(masterBoard, 5);
        System.out.println(900 / 200);
    }
}
