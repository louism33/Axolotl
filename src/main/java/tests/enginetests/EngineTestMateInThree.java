package tests.enginetests;

import com.github.louism33.axolotl.search.Engine;
import old.chessprogram.chess.Chessboard;
import old.chessprogram.chess.Move;
import old.chessprogram.graphicsandui.Art;
import old.chessprogram.miscAdmin.FenParser;
import old.chessprogram.miscAdmin.MoveParserFromAN;
import old.chessprogram.moveGeneration.MoveGeneratorMaster;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class EngineTestMateInThree {

    private static final int timeLimit = 10000;
    
    @Test
    void test1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b1kb1r/pppp1ppp/5q2/4n3/3KP3/2N3PN/PPP4P/R1BQ1B1R b kq - 0 1");
        System.out.println(Art.boardArt(chessboard));

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(chessboard, chessboard.isWhiteTurn());
        System.out.println(moves);
        Move correctMove = moves.get(11);
        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        Assert.assertEquals(correctMove, move);
    }

    @Test
    void test2() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r3k2r/ppp2Npp/1b5n/4p2b/2B1P2q/BQP2P2/P5PP/RN5K w kq - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Bb5+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test3() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b3kr/ppp1Bp1p/1b6/n2P4/2p3q1/2Q2N2/P4PPP/RN2R1K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxh8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test4() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r2n1rk1/1ppb2pp/1p1p4/3Ppq1n/2B3P1/2P4P/PP1N1P1K/R2Q1RN1 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxf2+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test5() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3q1r1k/2p4p/1p1pBrp1/p2Pp3/2PnP3/5PP1/PP1Q2K1/5R1R w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxh7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test6() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("4r1k1/5ppp/p2p4/4r3/1pNn4/1P6/1PPK2PP/R3R3 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nf3+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test7() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("k2r3r/p3Rppp/1p4q1/1P1b4/3Q1B2/6N1/PP3PPP/6K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxa7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test8() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b5/5p2/5Npk/p1pP2q1/4P2p/1PQ2R1P/6P1/6K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ng8+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test9() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b2rk1/1p3pb1/2p3p1/p1B5/P3N3/1B1Q1Pn1/1PP3q1/2KR3R w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nf6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test10theDevil() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1bq1rk1/p3b1np/1pp2ppQ/3nB3/3P4/2NB1N1P/PP3PP1/3R1RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ng5");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test11() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("rk5r/2p3pp/p1p5/4N3/4P3/2q4P/P4PP1/R2Q2K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rb1+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test12() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r3kb1r/1b4p1/pq2pn1p/1N2p3/8/3B2Q1/PPP2PPP/2KRR3 w kq - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Bg6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test13() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("4rk2/5p1b/1p3R1K/p6p/2P2P2/1P6/2q4P/Q5R1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxf7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test14() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("8/6pk/pb5p/8/1P2qP2/P3p3/2r2PNP/1QR3K1 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "exf2+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test15() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2bkr3/5Q1R/p2pp1N1/1p6/8/2q3P1/P4P1K/8 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ne5");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test16() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b2nrk/1p3p1p/p2p1P2/5P2/2q1P2Q/8/PpP5/1K1R3R w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxh7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test17() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2r5/1Nr1kpRp/p3b3/N3p3/1P3n2/P7/5PPP/K6R b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rc1+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test18() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r5k1/2p2ppp/p1P2n2/8/1pP2bbQ/1B3PP1/PP1Pq2P/RNB3K1 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qe1+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test19() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2bqr2k/1r1n2bp/pp1pBp2/2pP1PQ1/P3PN2/1P4P1/1B5P/R3R1K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ng6+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test20() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("rn1q3r/pp2kppp/3Np3/2b1n3/3N2Q1/3B4/PP4PP/R1B2RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxf7+");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }

}
