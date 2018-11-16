package tests.enginetests;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.miscAdmin.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EngineTestMateInSix {

    private static final int timeLimit = 30000;

    @Test
    void test1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("2r3k1/p2R1p2/1p5Q/4N3/7P/4P3/b5PK/5q2 w - - 1 0");
        System.out.println(Art.boardArt(chessboard));

        Move move = new Engine().searchFixedTime(chessboard, timeLimit);
        System.out.println(move);

        int moveToWin = MoveParserFromAN.destinationIndex(chessboard, "Nc6");
        int destination = move.destinationIndex;
        Assert.assertEquals(moveToWin, destination);
    }
    
}
