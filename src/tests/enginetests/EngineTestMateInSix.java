package tests.enginetests;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.miscAdmin.MoveParserFromAN;
import javacode.graphicsandui.Art;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EngineTestMateInSix {

    private static final int timeLimit = 30000;

    @Test
    void test1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2r3k1/p2R1p2/1p5Q/4N3/7P/4P3/b5PK/5q2 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = Engine.search(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nc6");
        int destination = move.destination;
        Assert.assertEquals(moveToWin, destination);
    }
    
}
