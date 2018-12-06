package tests.enginetests;

import com.github.louism33.axolotl.search.Engine;
import old.chessprogram.chess.Chessboard;
import old.chessprogram.chess.Move;
import old.chessprogram.graphicsandui.Art;
import old.chessprogram.miscAdmin.FenParser;
import old.chessprogram.miscAdmin.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EngineTestMateInTwo {

    private static final int timeLimit = 10000;
    
    @Test
    void test1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r2qkb1r/pp2nppp/3p4/2pNN1B1/2BnP3/3P4/PPP2PPP/R2bK2R w KQkq - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int nf6 = MoveParserFromAN.destinationIndex(chessboard, "Nf6");
        int destination = move.destinationIndex;
        Assert.assertEquals(nf6, destination);
    }

    @Test
    void test2() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("1rb4r/pkPp3p/1b1P3n/1Q6/N3Pp2/8/P1P3PP/7K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int Qd5 = MoveParserFromAN.destinationIndex(chessboard, "Qd5+");
        int destination = move.destinationIndex;
        Assert.assertEquals(Qd5, destination);
    }

    @Test
    void test3() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("4kb1r/p2n1ppp/4q3/4p1B1/4P3/1Q6/PPP2PPP/2KR4 w k - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int Qb8 = MoveParserFromAN.destinationIndex(chessboard, "Qb8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(Qb8, destination);
    }

    @Test
    void test4() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b2k1r/ppp1bppp/8/1B1Q4/5q2/2P5/PPP2PPP/R3R1K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qd8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test5() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("5rkr/pp2Rp2/1b1p1Pb1/3P2Q1/2n3P1/2p5/P4P2/4R1K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxg6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test6() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("1r5k/3b3p/3p3b/2qPp3/Pnp4P/Q1N5/8/K5RR b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nc2+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test7() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("1k1r4/pp5R/2p5/P5p1/7b/4Pq2/1PQ2P2/3NK3 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxd1+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test8() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("5bk1/R4p1p/6p1/8/3p2K1/1Q4P1/1P3P1q/2r5 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qh5+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test9() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3rr2k/pp1b2b1/4q1pp/2Pp1p2/3B4/1P2QNP1/P6P/R4RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxh6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test10() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3r2k1/6pp/1nQ1R3/3r4/3N2q1/6N1/n4PPP/4R1K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Re8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test11() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r3k2r/p3bpp1/2q1p1b1/1ppPP1B1/3n3P/5NR1/PP2NP2/K1QR4 b kq - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nb3+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test12() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("4rk2/pp2N1bQ/5p2/8/2q5/P7/3r2PP/4RR1K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxf6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test13() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r4rk1/4bp2/1Bppq1p1/4p1n1/2P1Pn2/3P2N1/P2Q1PBK/1R5R b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qh3+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test14Styx() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2q1r3/4pR2/3rQ1pk/p1pnN2p/Pn5B/8/1P4PP/3R3K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nf3");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }
    
    @Test
    void test15() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("q2br1k1/1b4pp/3Bp3/p6n/1p3R2/3B1N2/PP2QPPP/6K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxe6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test16() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("5r1k/p2n1p1p/5P1N/1p1p4/2pP3P/8/PP4RK/8 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rg8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test17() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("8/7p/5pk1/3n2pq/3N1nR1/1P3P2/P6P/4QK2 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qe8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test18() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2Q5/pp2rk1p/3p2pq/2bP1r2/5RR1/1P2P3/PB3P1P/7K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxf5+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test19() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("4r1k1/pQ3pp1/7p/4q3/4r3/P7/1P2nPPP/2BR1R1K b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxh2+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test20() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3R1rk1/1pp2pp1/1p6/8/8/P7/1q4BP/3Q2K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxf8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }
    
}
