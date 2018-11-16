package tests.enginetests;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.miscAdmin.MoveParserFromAN;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class EngineTestMisc {



    @Test
    void normalBoard() {
        Chessboard chessboard = new Chessboard();
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedDepth(chessboard, 10);
        System.out.println(move);

//        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nc6");
//        int destination = move.destinationIndex;
//        Assert.assertEquals(moveToWin, destination);
    }
    
    @Test
    void blathyGrotesque() {

        // white wins through underpromotion
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("8/8/8/2p5/1pp5/brpp4/qpprpK1P/1nkbn3 w - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, 1000);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Kxe1");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }
    
    @Test
    void gorgievGrotesque() {

        // white draws through sacrifices
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("8/8/4N3/4Q3/1pp5/1p3N2/bpqp1p2/nrkrbK2 w - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, 1000);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nf4");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);

    }

    
    @Test
    void paulLamfordGrotesque() {

        // ridiculous board position and checkmate, white to win
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("8/8/8/1k3p2/p1p1pPp1/PpPpP1Pp/1P1P3P/QNK2NRR w - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, 1000);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Kd1");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);

    }

    @Test
    void retiEndgameStudy() {

        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("7K/8/k1P5/7p/8/8/8/8 w - -");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, 1000);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Kg7");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);

    }


    @Test
    void checkmateInPromotion() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/8/3q3p/K7 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, 1000);
        Move moveOne = new Move(8, 0, false, false, true, false, false, true, false, 666);
        Move moveTwo = new Move(8, 0, false, false, true, false, false, false, true, 666);
        Assert.assertTrue(move.equals(moveOne) || move.equals(moveTwo));
    }


    @Test
    void checkmateInOne() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/7r/3q4/K7 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));
        Move move = new Engine().searchFixedTime(chessboard, 1000);
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(chessboard, chessboard.isWhiteTurn());
        System.out.println(moves);
        System.out.println(move);
        Assert.assertEquals(move, new Move(16, 0));
    }

    @Test
    void takeTheQueen() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/8/2Kq4/8 w - - 0 1");
        System.out.println(Art.boardArt(chessboard));
        Move move = new Engine().searchFixedTime(chessboard, 1000);
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(chessboard, chessboard.isWhiteTurn());
        System.out.println(move);
        Assert.assertEquals(move, new Move(13, 12));
    }

}
