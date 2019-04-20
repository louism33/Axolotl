package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.evaluation.PawnEval;
import com.github.louism33.axolotl.evaluation.PawnTranspositionTable;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.*;
import static com.github.louism33.axolotl.main.PVLine.getPV;

public class Util {
    
    public static void reset() {
        EngineSpecifications.DEBUG = false;
        Engine.resetFull();
    }

    @Test
    void www() {
//        Chessboard board = new Chessboard("8/R7/7K/8/6k1/8/8/8 w - - 0 14");
//        System.out.println(board);
//        final long[] testPawnData = PawnEval.calculatePawnData(board, 0);
//        System.out.println(Arrays.toString(testPawnData));
//        Art.printLong(testPawnData[FILE_WITHOUT_MY_PAWNS]);
//        Art.printLong(testPawnData[FILE_WITHOUT_MY_PAWNS + 1]);
    }
}
