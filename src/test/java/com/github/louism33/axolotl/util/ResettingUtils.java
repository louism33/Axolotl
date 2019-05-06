package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.SearchSpecs;

import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.DEFAULT_PAWN_TABLE_SIZE_MB;
import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.PAWN_TABLE_SIZE;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;

public class ResettingUtils {

    public static void reset() {
        PRINT_PV = false;
        MASTER_DEBUG = false;
        DEBUG = false;
        Engine.sendBestMove = false;
        TABLE_SIZE = DEFAULT_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
        TABLE_SIZE_MB = DEFAULT_TABLE_SIZE_MB;
        NUMBER_OF_THREADS = DEFAULT_THREAD_NUMBER;

        PAWN_TABLE_SIZE = DEFAULT_PAWN_TABLE_SIZE_MB;
        SearchSpecs.reset();
        Engine.resetFull();
    }

}
