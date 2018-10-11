package tests;

import main.check.CheckChecker;
import main.chess.Art;
import main.chess.Chessboard;
import main.utils.RandomBoard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CheckCheckerTest {


    static int num = 11;
    static Chessboard[] bs;

    @BeforeAll
    static void setUp() {
        bs = RandomBoard.boardForTests();
    }

    @Test
    void boardInCheck() {
        Assert.assertFalse(CheckChecker.boardInCheck(bs[0], true));
        Assert.assertFalse(CheckChecker.boardInCheck(bs[0], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[1], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[1], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[2], true));
        Assert.assertFalse(CheckChecker.boardInCheck(bs[2], false));

        Assert.assertFalse(CheckChecker.boardInCheck(bs[3], true));
        Assert.assertFalse(CheckChecker.boardInCheck(bs[3], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[4], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[4], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[5], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[5], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[6], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[6], false));

        Assert.assertFalse(CheckChecker.boardInCheck(bs[7], true));
        Assert.assertFalse(CheckChecker.boardInCheck(bs[7], false));

        Assert.assertFalse(CheckChecker.boardInCheck(bs[8], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[8], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[9], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[9], false));

        Assert.assertTrue(CheckChecker.boardInCheck(bs[10], true));
        Assert.assertTrue(CheckChecker.boardInCheck(bs[10], false));
    }

    @Test
    void numberOfPiecesThatLegalThreatenSquare() {
    }
}