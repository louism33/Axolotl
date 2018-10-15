package tests;

import javacode.chessprogram.bitboards.RandomBoard;
import javacode.chessprogram.chess.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChessboardTest {

    private static Chessboard[] bs;
    
    @BeforeAll
    static void setUp() {
        bs = RandomBoard.boardForTests();
        RandomBoard.printBoards(bs);
    }

    @Test
    void equals() {
        
        for (Chessboard b : bs){
            Assert.assertEquals(b, b);
        }
        
    }
}