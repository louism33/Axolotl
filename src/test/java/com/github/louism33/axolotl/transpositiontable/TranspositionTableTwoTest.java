package com.github.louism33.axolotl.transpositiontable;

import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.main.PVLine;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.EXACT;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.ageModulo;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public class TranspositionTableTwoTest {

    @Test
    public void persistenceTest() {

        Chessboard board = new Chessboard();

        int bestMove = EngineBetter.searchFixedDepth(board, 3);
        int move = getMove(retrieveFromTable(board.zobristHash));

        Assert.assertEquals(bestMove, move);

        List<GenericMove> genericMoves = PVLine.retrievePV(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0).toString()));

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(1).toString()));

        EngineBetter.resetFull();
    }


    @Test
    void retrieveFromTableSimpleTest() {
        Chessboard board = new Chessboard();

        int bestMove = 123, bestScore = 666, depth = 5, flag = EXACT, ply = 1, age = 2;

        addToTableReplaceByDepth(board.zobristHash,
                bestMove & MOVE_MASK_WITHOUT_CHECK, bestScore, depth, flag, ply, age);

        long previousTableData = retrieveFromTable(board.zobristHash);

        Assert.assertTrue(previousTableData != 0);
        Assert.assertEquals(bestScore, getScore(previousTableData, ply));
        Assert.assertEquals(bestMove, getMove(previousTableData));
        Assert.assertEquals(flag, getFlag(previousTableData));
        Assert.assertEquals(age, getAge(previousTableData));

        EngineBetter.resetFull();
    }


    @Test
    void isWritingTest() {
        Chessboard board = new Chessboard();

        EngineBetter.searchFixedDepth(board, 3);

        long previousTableData = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData != 0);

        EngineBetter.resetFull();
    }

    @Test
    void isModuloingTest() {
        EngineBetter.resetFull();
        Assert.assertEquals(EngineBetter.age, 0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < ageModulo; j++) {
                Assert.assertEquals(EngineBetter.age, j);
                EngineBetter.resetBetweenMoves();
            }
        }

        EngineBetter.resetFull();
    }

    @Test
    void isModuloing2Test() {
        Assert.assertFalse(isTooOld(7, 7));
        Assert.assertFalse(isTooOld(6, 7));
        Assert.assertFalse(isTooOld(5, 7));

        Assert.assertTrue(isTooOld(4, 7));
        Assert.assertTrue(isTooOld(3, 7));
        Assert.assertTrue(isTooOld(2, 7));
        Assert.assertTrue(isTooOld(1, 7));
        Assert.assertTrue(isTooOld(0, 7));

        EngineBetter.resetFull();
    }

    @Test
    void isModuloing3Test() {
        Assert.assertFalse(isTooOld(1, 1));
        Assert.assertFalse(isTooOld(0, 1));
        Assert.assertFalse(isTooOld(7, 1));

        Assert.assertTrue(isTooOld(6, 1));
        Assert.assertTrue(isTooOld(5, 1));
        Assert.assertTrue(isTooOld(4, 1));
        Assert.assertTrue(isTooOld(3, 1));
        Assert.assertTrue(isTooOld(2, 1));

        EngineBetter.resetFull();
    }

    @Test
    void isPersistingTest() {
        Chessboard board = new Chessboard();

        int bestMove = EngineBetter.searchFixedDepth(board, 6);

        long previousTableData1 = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData1 != 0);

        List<GenericMove> genericMoves = PVLine.retrievePV(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0).toString()));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(1).toString()));

        EngineBetter.resetBetweenMoves();
        long previousTableData2 = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData2 != 0);
        System.out.println("persisting ok");

        EngineBetter.resetFull();
    }

    @Test
    void fullResetTest() {
        Chessboard board = new Chessboard();

        int bestMove = EngineBetter.searchFixedDepth(board, 6);

        long previousTableData1 = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData1 != 0);

        List<GenericMove> genericMoves = PVLine.retrievePV(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0).toString()));

        EngineBetter.resetFull();
        long previousTableData2 = retrieveFromTable(board.zobristHash);
        Assert.assertEquals(0, previousTableData2);
        System.out.println("full reset working");

        EngineBetter.resetFull();
    }

}