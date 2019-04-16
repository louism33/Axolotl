package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.louism33.axolotl.main.PVLine.getPV;

public class Util {
    
    public static void reset() {
        EngineSpecifications.DEBUG = false;
        Engine.resetFull();
    }

    @Test
    void www() throws IOException {
        Chessboard board = new Chessboard();
        final int i = Engine.searchFixedDepth(board, 6);

        final int[] pv = getPV(board);
        MoveParser.printMove(pv);
    }
}
