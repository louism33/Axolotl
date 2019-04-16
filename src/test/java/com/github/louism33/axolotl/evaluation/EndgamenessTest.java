package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.evaluation.Evaluator.getPercentageOfEndgameness;
import static com.github.louism33.axolotl.evaluation.Evaluator.getPercentageOfStartGame;
import static com.github.louism33.chesscore.BoardConstants.*;

public class EndgamenessTest {

    @Test
    void startPos() {
        Chessboard board = new Chessboard();
        int percentageOfEndgameness = 100 - getPercentageOfStartGame(board);
        Assert.assertEquals(0, percentageOfEndgameness);
    }

    @Test
    void noPawnsPos() {
        Chessboard board = new Chessboard();
        board.pieces[WHITE][PAWN] = 0;
        board.pieces[BLACK][PAWN] = 0;
        int percentageOfEndgameness = 100 - getPercentageOfStartGame(board);
        Assert.assertEquals(0, percentageOfEndgameness);
    }

    @Test
    void endPoss() {
        Chessboard board = new Chessboard("8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -");
        System.out.println(board);
        int percentageOfEndgameness = 100 - getPercentageOfStartGame(board);
        Assert.assertTrue(percentageOfEndgameness >= 80);
    }

    @Test
    void endPos() {
        Chessboard board = new Chessboard("k6K/8/8/8/8/8/8/8");
        int percentageOfEndgameness = 100 - getPercentageOfStartGame(board);
        Assert.assertEquals(100, percentageOfEndgameness);
    }

}
