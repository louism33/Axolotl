package tests.enginetests;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.miscAdmin.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EngineTestMateInFive {


    private static final int timeLimit = 30000;

    @Test
    void test1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6r1/p3p1rk/1p1pPp1p/q3n2R/4P3/3BR2P/PPP2QP1/7K w - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxh6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test2() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2q1nk1r/4Rp2/1ppp1P2/6Pp/3p1B2/3P3P/PPP1Q3/6K1 w - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxe8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test3Lucifer() {

        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6k1/3b3r/1p1p4/p1n2p2/1PPNpP1q/P3Q1p1/1R1RB1P1/5K2 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "qxf4");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
        
        
        
        
    }


    @Test
    void test4() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2R3Bk/6p1/1p5p/4pbPP/1P1b4/5pK1/5P2/8 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Be6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }




    @Test
    void test5() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("5r2/r4p1p/1p3n2/n1pp1NNk/p2P4/P1P3R1/1P5P/5RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ng7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }



    @Test
    void test6Flagg() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3r4/1b5p/ppqP1Ppk/2p1rp2/2P1P3/3n2N1/P5QP/3R1RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nxf5+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test7() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6kr/4Bpb1/1p1p4/3B1P2/4R1Q1/2qn2P1/2P2P2/6K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Bxf7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test8() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("5r1k/2q4b/p3p2Q/1pp4p/8/1P3r2/P1P4P/1KBR2R1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rd7");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test9Satan() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("7k/1p2r1p1/pPq4p/7R/1P1Nn2P/P5p1/1B3r2/3Q2K1 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "ng5");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test10() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6k1/1P3p1p/3b2p1/2NQ2n1/8/2P2p1P/5PP1/4qBK1 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "nxh3+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test11() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r4bk1/q5pp/3N1p2/2p5/1p2PB2/1Pp2PP1/4Q2P/1K1R4 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "c2+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }



    @Test
    void test12() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3kr3/pp2r3/2n2Q1p/2Rp4/2p2B2/2P3P1/2q2P1P/4R1K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qd6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test13() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6Nk/5prp/pp1p1Q2/2pP4/P1P3b1/7R/3q3P/4R2K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxh7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test14() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("7k/1p2r1p1/pPq4p/7R/1P1Nn2P/P5p1/1B3r2/3Q2K1 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ng5");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }
}
