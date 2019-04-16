package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.axolotl.search.EngineBetter.*;
import static com.github.louism33.axolotl.search.EngineBetter.searchFixedDepth;

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
