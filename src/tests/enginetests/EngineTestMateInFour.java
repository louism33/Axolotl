package tests.enginetests;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.miscAdmin.MoveParserFromAN;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.graphicsandui.Art;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class EngineTestMateInFour {


    private static final int timeLimit = 15000;
    
    @Test
    void test1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r5rk/2p1Nppp/3p3P/pp2p1P1/4P3/2qnPQK1/8/R6R w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "hxg7+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test2() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("1r2k1r1/pbppnp1p/1b3P2/8/Q7/B1PB1q2/P4PPP/3R2K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qxd7+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test3Hades() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("Q7/p1p1q1pk/3p2rp/4n3/3bP3/7b/PP3PPK/R1B2R2 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(chessboard, chessboard.isWhiteTurn());
        System.out.println(moves);

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Bxg2");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test4() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1bqr3/ppp1B1kp/1b4p1/n2B4/3PQ1P1/2P5/P4P2/RN4K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qe5");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test5Meanie() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b3kr/3pR1p1/ppq4p/5P2/4Q3/B7/P5PP/5RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxg7");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test6() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2k4r/1r1q2pp/QBp2p2/1p6/8/8/P4PPP/2R3K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qa8");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test7() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6kr/pp2r2p/n1p1PB1Q/2q5/2B4P/2N3p1/PPP3P1/7K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qg7");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test8() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r3k3/pbpqb1r1/1p2Q1p1/3pP1B1/3P4/3B4/PPP4P/5RK1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Bxg6");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test9() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("8/6R1/p2kp2r/qb5P/3p1N1Q/1p1Pr3/PP6/1K5R w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qe7+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test10Cruelty() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r5nr/6Rp/p1NNkp2/1p3b2/2p5/5K2/PP2P3/3R4 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nxf5");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test11() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2b2k2/2p2r1p/p2pR3/1p3PQ1/3q3N/1P6/2P3PP/5K2 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Ng6+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }
    
    
    @Test
    void test12() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("1Qb2b1r/1p1k1p1p/3p1p2/3p4/p2NPP2/1R6/q1P3PP/4K2R w K - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxb7+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test13() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3r2k1/3q2p1/1b3p1p/4p3/p1R1P2N/Pr5P/1PQ3P1/5R1K b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxh3+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test14() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3r2k1/pp5p/6p1/2Ppq3/4Nr2/4B2b/PP2P2K/R1Q1R2B b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rf2+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test15() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("1q1N4/3k1BQp/5r2/5p2/3P3P/8/3B1PPb/3n3K w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Be6+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test16() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3r1rk1/ppqn3p/1npb1P2/5B2/2P5/2N3B1/PP2Q1PP/R5K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qg4+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test17() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2r1k3/2P3R1/3P2K1/6N1/8/8/8/3r4 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Re7+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }


    @Test
    void test18() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("r1b2k1r/pppp4/1bP2qp1/5pp1/4pP2/1BP5/PBP3PP/R2Q1R1K b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Rxh2+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test19() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("rr2k3/5p2/p1bppPpQ/2p1n1P1/1q2PB2/2N4R/PP4BP/6K1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Qf8+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }

    @Test
    void test20() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2r1rk2/p1q3pQ/4p3/1pppP1N1/7p/4P2P/PP3P2/1K4R1 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nxe6+");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }
    
}