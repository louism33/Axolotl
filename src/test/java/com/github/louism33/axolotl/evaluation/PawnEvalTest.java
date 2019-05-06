package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.*;

//@Disabled
public class PawnEvalTest {

    @BeforeAll
    static void reset() {
        ResettingUtils.reset();
    }

    @Test
    void retrieveFromTableSimpleTest() {
        PawnTranspositionTable.reset();

        Chessboard board = new Chessboard();

        long[] pawnData = PawnTranspositionTable.getPawnData(board, board.zobristPawnHash, 0);
        Assert.assertNotNull(pawnData);
        long[] pawnDataTest = PawnEval.calculatePawnData(board, 0);
        Assert.assertNotNull(pawnDataTest);
        Assert.assertArrayEquals(pawnDataTest, pawnData);
    }

    @Test
    void retrieveFromTableFenTest() {
        PawnTranspositionTable.reset();

        Chessboard board = new Chessboard("7k/p7/1R5K/6r1/6p1/6P1/8/8 w - - 0 0");

        Assert.assertTrue(board.zobristPawnHash != 0);
        long[] pawnData = PawnTranspositionTable.getPawnData(board, board.zobristPawnHash, 0);
        Assert.assertNotNull(pawnData);
        long[] testPawnData = PawnEval.calculatePawnData(board, 0);
        Assert.assertNotNull(testPawnData);
        if (!Arrays.equals(testPawnData, pawnData)) {
            System.out.println(board);
            System.out.println(board.toFenString());
            System.out.println("test data:");
            System.out.println(Arrays.toString(testPawnData));
            System.out.println("from table: ");
            System.out.println(Arrays.toString(pawnData));
            Art.printLong(testPawnData[0]);
            Art.printLong(pawnData[0]);
        }
        Assert.assertArrayEquals(testPawnData, pawnData);
    }

    @Test
    void retrieveFromTableFen2Test() {
        PawnTranspositionTable.reset();

        Chessboard board = new Chessboard("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1");

        Assert.assertTrue(board.zobristPawnHash != 0);
        long[] pawnData = PawnTranspositionTable.getPawnData(board, board.zobristPawnHash, 0);
        Assert.assertNotNull(pawnData);
        long[] testPawnData = PawnEval.calculatePawnData(board, 0);
        Assert.assertNotNull(testPawnData);
        if (!Arrays.equals(testPawnData, pawnData)) {
            System.out.println(board);
            System.out.println(board.toFenString());
            System.out.println("test data:");
            System.out.println(Arrays.toString(testPawnData));
            System.out.println("from table: ");
            System.out.println(Arrays.toString(pawnData));
            Art.printLong(testPawnData[0]);
            Art.printLong(pawnData[0]);
        }
        Assert.assertArrayEquals(testPawnData, pawnData);
    }

    @Test
    void noPawnsTest() {
        PawnTranspositionTable.reset();

        Chessboard board = new Chessboard("7k/8/7K/8/6R1/6r1/8/8 w - - 0 8");

        Assert.assertEquals(0, board.zobristPawnHash);
        long[] pawnData = PawnTranspositionTable.getPawnData(board, board.zobristPawnHash, 0);

        Assert.assertArrayEquals(pawnData, noPawnsData);
    }

    @Test
    public void percentStuff() {
        Chessboard board = new Chessboard();
        Engine engine = new Engine();
        System.out.println();
        engine.receiveSearchSpecs(board, 16);
        final int move = engine.simpleSearch();

        System.out.println("size of pawn table in mb: " + DEFAULT_PAWN_TABLE_SIZE_MB);
        System.out.println("entries in pawnMoveData : " + pawnMoveData.length);
        System.out.println("total keys              : " + keys.length);
        System.out.println();
        System.out.println("total requests          : " + totalRequests);
        System.out.println("newEntries              : " + newEntries);
        System.out.println("hit                     : " + hit);
        System.out.println("override                : " + override);
        System.out.println("ratio new total         : " + ((double) newEntries / (double) totalRequests));
        System.out.println("ratio hit total         : " + ((double) hit / (double) totalRequests));

    }
}
