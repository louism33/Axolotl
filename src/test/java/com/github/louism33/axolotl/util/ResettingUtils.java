package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.evaluation.PawnTranspositionTable;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.SearchSpecs;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.DEFAULT_PAWN_TABLE_SIZE_MB;
import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;

public class ResettingUtils {

    public static void reset() {
        PRINT_PV = false;
        MASTER_DEBUG = false;
        DEBUG = false;
        sendBestMove = false;
        TABLE_SIZE = DEFAULT_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
        TABLE_SIZE_MB = DEFAULT_TABLE_SIZE_MB;
        NUMBER_OF_THREADS = DEFAULT_THREAD_NUMBER;

        Engine.setThreads(NUMBER_OF_THREADS);

        PAWN_TABLE_SIZE_MB = DEFAULT_PAWN_TABLE_SIZE_MB;
        SearchSpecs.reset();
        Engine.resetFull();
        PawnTranspositionTable.initPawnTableDefault(true);

        for (int i = 0; i < TranspositionTable.keys.length; i++) {
            Assert.assertEquals(TranspositionTable.keys[i], 0);
            Assert.assertEquals(TranspositionTable.entries[i], 0);
        }

        for (int i = 0; i < PawnTranspositionTable.keys[0].length; i++) {
            Assert.assertEquals(PawnTranspositionTable.keys[0][i], 0);
        }
    }

}
