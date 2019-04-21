package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.jupiter.api.Test;

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
