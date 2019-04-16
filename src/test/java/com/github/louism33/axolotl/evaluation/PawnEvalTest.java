package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.util.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PawnEvalTest {

    @BeforeAll
    static void reset() {
        Util.reset();
    }
    
    @Test
    void retrieveFromTableSimpleTest() {
//        PawnTranspositionTable.reset();
//        
//        Chessboard board = new Chessboard();
//
//        long[] pawnData = PawnTranspositionTable.retrieveFromTable(board.zobristPawnHash, 0);
//
//        Assert.assertNull(pawnData);
//
//        pawnData = PawnEval.calculatePawnData(board, 0);
////        PawnTranspositionTable.addToTableReplaceArbitrarily(board.zobristPawnHash, pawnData, PawnEval.pawnScore);
//
//
//        Assert.assertNotNull(pawnData);
    }

}
