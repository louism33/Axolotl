package tests.enginetests;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.graphicsandui.Art;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class EngineTestMisc {




    @Test
    void checkmateInPromotion() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/8/3q3p/K7 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, 1000);
        Move moveOne = new Move(8, 0, false, false, true, false, false, true, false, 666);
        Move moveTwo = new Move(8, 0, false, false, true, false, false, false, true, 666);
        Assert.assertTrue(move.equals(moveOne) || move.equals(moveTwo));
    }


    @Test
    void checkmateInOne() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/7r/3q4/K7 b - - 0 1");
        System.out.println(Art.boardArt(chessboard));
        Move move = Engine.search(chessboard, 1000);
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(chessboard, chessboard.isWhiteTurn());
        System.out.println(moves);
        System.out.println(move);
        Assert.assertEquals(move, new Move(16, 0));
    }

    @Test
    void takeTheQueen() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/8/2Kq4/8 w - - 0 1");
        System.out.println(Art.boardArt(chessboard));
        Move move = Engine.search(chessboard, 1000);
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(chessboard, chessboard.isWhiteTurn());
        System.out.println(move);
        Assert.assertEquals(move, new Move(13, 12));
    }
    
}
