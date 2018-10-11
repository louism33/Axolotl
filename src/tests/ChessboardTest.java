package tests;

import main.chess.Chessboard;
import main.utils.RandomBoard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessboardTest {

    static Chessboard[] bs;
    
    @BeforeAll
    static void setUp() {
        bs = RandomBoard.boardForTests();
        RandomBoard.printBoards(bs);
    }

    @Test
    void equals() {
        
        for (Chessboard b : bs){
            Assert.assertTrue(b.equals(b));
        }
        
    }
}