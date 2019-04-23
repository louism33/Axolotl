package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.main.PVLine;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.louism33.axolotl.search.EngineSpecifications.DEFAULT_TABLE_SIZE_MB;
import static com.github.louism33.axolotl.search.EngineSpecifications.TABLE_SIZE;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public class TranspositionTable2Test {

    private Engine engine = new Engine();

    @BeforeAll
    static void setup() {
        Engine.resetFull();
    }

    @AfterAll
    static void reset() {
        Engine.resetFull();
    }

    @Test
    public void persistenceTest() {

        Chessboard board = new Chessboard();

        engine.receiveSearchSpecs(board, 6);
        int bestMove = engine.simpleSearch();
        int move = getMove(retrieveFromTable(board.zobristHash));

        Assert.assertEquals(bestMove, move);

        List<String> genericMoves = PVLine.retrievePV(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0)));

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(1)));

        Engine.resetFull();
    }

    @Test
    public void ageTest() {
        Engine.resetFull();
        Chessboard board = new Chessboard();

        engine.receiveSearchSpecs(board, 6);
        int move = engine.simpleSearch();

        int length = entries.length;
        for (int i = 0; i < length; i++) {
            long entry = entries[i];
            int age = getAge(entry);
            Assert.assertTrue(age >= 0);
            Assert.assertTrue(age < ageModulo);
        }
        Engine.resetFull();
    }

    @Test
    void notAgeOutTest() {
//        Engine.resetFull();
//        Chessboard board = new Chessboard();
//
//        agedOut = 0;
//
//        addToTableReplaceByDepth(board.zobristHash, 1, 666, 10, EXACT, 10, 0);
//        addToTableReplaceByDepth(board.zobristHash, 100, 666, 10, EXACT, 10, 0);
//
//        Assert.assertEquals(100, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(0, agedOut);
//
//        // one turn later
//        addToTableReplaceByDepth(board.zobristHash, 200, 666, 10, EXACT, 10, 1);
//        Assert.assertEquals(200, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(0, agedOut);
//
//        // entry is aged one
//        // 2 turns later
//        addToTableReplaceByDepth(board.zobristHash, 300, 666, 10, EXACT, 10, 1 + 2);
//        Assert.assertEquals(300, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(0, agedOut);
//
//        agedOut = 0;
        Engine.resetFull();

    }

    @Test
    void ageOutTest() {
        Engine.resetFull();
        Chessboard board = new Chessboard();
        TranspositionTable.initTableMegaByte(DEFAULT_TABLE_SIZE_MB);
//        System.out.println(TABLE_SIZE);


//        TranspositionTable.initTable(TABLE_SIZE);
//        
//        agedOut = 0;
//
//        int initAge = 0;
//        addToTableReplaceByDepth(64 << shiftAmount, 1, 666, 10, UPPERBOUND, 10, initAge);
//        addToTableReplaceByDepth(board.zobristHash, 100, 666, 10, UPPERBOUND, 10, (initAge + 3+acceptableAges) % ageModulo);

//        Assert.assertEquals(100, getMove(retrieveFromTable(board.zobristHash)));
//
//        System.out.println(agedOut);
//
//        System.out.println(TranspositionTableStressTest.countRealEntries(keys, false));
//        System.out.println(TranspositionTableStressTest.countRealEntries(entries, false));


//        Assert.assertEquals(1, agedOut);
//
//        // reset
//        addToTableReplaceByDepth(board.zobristHash, 1, 666, 10, UPPERBOUND, 10, initAge);
//        Assert.assertEquals(1, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(2, agedOut);
//
//
//        addToTableReplaceByDepth(board.zobristHash, 200, 666, 10, UPPERBOUND, 10, (initAge + acceptableAges + 1) % ageModulo);
//
//        Assert.assertEquals(200, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(3, agedOut);
//
//        agedOut = 0;
        Engine.resetFull();
    }


    @Test
    void ageOut2Test() {
        Engine.resetFull();
        Chessboard board = new Chessboard();

        agedOut = 0;

        int initAge = 0;
        addToTableReplaceByDepth(board.zobristHash, 1, 666, 10, UPPERBOUND, 10, initAge);
        for (int i = 0; i < ageModulo; i++) {
            addToTableReplaceByDepth(board.zobristHash, 100, 666, 10, UPPERBOUND, 10, (initAge + i) % ageModulo);
        }

//        Assert.assertEquals(100, getMove(retrieveFromTable(board.zobristHash)));

        System.out.println(agedOut);

        System.out.println(TranspositionTableStressTest.countRealEntries(keys, false));

        System.out.println(TranspositionTableStressTest.countRealEntries(entries, false));


//        Assert.assertEquals(1, agedOut);
//
//        // reset
//        addToTableReplaceByDepth(board.zobristHash, 1, 666, 10, UPPERBOUND, 10, initAge);
//        Assert.assertEquals(1, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(2, agedOut);
//
//
//        addToTableReplaceByDepth(board.zobristHash, 200, 666, 10, UPPERBOUND, 10, (initAge + acceptableAges + 1) % ageModulo);
//
//        Assert.assertEquals(200, getMove(retrieveFromTable(board.zobristHash)));
//        Assert.assertEquals(3, agedOut);
//
//        agedOut = 0;
        Engine.resetFull();
    }


    @Test
    void retrieveFromTableSimpleTest() {
        Engine.resetFull();
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

        Engine.resetFull();
    }


    @Test
    void isWritingTest() {
        Engine.resetFull();
        Chessboard board = new Chessboard();

        engine.receiveSearchSpecs(board, 3);
        int move = engine.simpleSearch();

        long previousTableData = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData != 0);

        Engine.resetFull();
    }

    @Test
    void isModuloingTest() {
        Engine.resetFull();
        Assert.assertEquals(Engine.age, 0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < ageModulo; j++) {
                Assert.assertEquals(Engine.age, j);
                Engine.resetBetweenMoves();
            }
        }

        Engine.resetFull();
    }

    @Test
    void isModuloing2Test() {
        Engine.resetFull();
        Assert.assertFalse(isTooOld(7, 7));
        Assert.assertFalse(isTooOld(6, 7));
        Assert.assertFalse(isTooOld(5, 7));

        Assert.assertTrue(isTooOld(4, 7));
        Assert.assertTrue(isTooOld(3, 7));
        Assert.assertTrue(isTooOld(2, 7));
        Assert.assertTrue(isTooOld(1, 7));
        Assert.assertTrue(isTooOld(0, 7));

        Engine.resetFull();
    }

    @Test
    void isModuloing3Test() {
        Engine.resetFull();
        Assert.assertFalse(isTooOld(1, 1));
        Assert.assertFalse(isTooOld(0, 1));
        Assert.assertFalse(isTooOld(7, 1));

        Assert.assertTrue(isTooOld(6, 1));
        Assert.assertTrue(isTooOld(5, 1));
        Assert.assertTrue(isTooOld(4, 1));
        Assert.assertTrue(isTooOld(3, 1));
        Assert.assertTrue(isTooOld(2, 1));

        Engine.resetFull();
    }

    @Test
    void isPersistingTest() {
        Engine.resetFull();
        Chessboard board = new Chessboard();

        engine.receiveSearchSpecs(board, 6);
        int move = engine.simpleSearch();

        long previousTableData1 = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData1 != 0);

        List<String> genericMoves = PVLine.retrievePV(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0)));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(1)));

        Engine.resetBetweenMoves();
        long previousTableData2 = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData2 != 0);
        System.out.println("persisting ok");

        Engine.resetFull();
    }

    @Test
    void fullResetTest() {
        Engine.resetFull();
        Chessboard board = new Chessboard();

        engine.receiveSearchSpecs(board, 6);
        int move = engine.simpleSearch();

        long previousTableData1 = retrieveFromTable(board.zobristHash);
        Assert.assertTrue(previousTableData1 != 0);

        List<String> genericMoves = PVLine.retrievePV(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0)));

        Engine.resetFull();
        long previousTableData2 = retrieveFromTable(board.zobristHash);
        Assert.assertEquals(0, previousTableData2);
        System.out.println("full reset working");

        Engine.resetFull();
    }

}