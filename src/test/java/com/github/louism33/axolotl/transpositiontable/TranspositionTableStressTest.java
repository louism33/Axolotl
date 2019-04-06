package com.github.louism33.axolotl.transpositiontable;

import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import com.github.louism33.axolotl.main.UCIEntryOld;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.Chessboard;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;

public class TranspositionTableStressTest {

    static final int depth = 10;

    @BeforeAll
    static void setup() {
        Util.reset();
    }

    @AfterAll
    static void reset() {
        Util.reset();
    }
    
    @Test
    void testMin() {
        System.out.println("min: ");
        stressTestToDepthTest(depth, new Chessboard(), MIN_TABLE_SIZE_MB);
    }
    
    @Test
    void testDefault() {
        System.out.println("default: ");
        stressTestToDepthTest(depth, new Chessboard(), DEFAULT_TABLE_SIZE_MB);
    }
    
    @Test
    void testMax() {
        System.out.println("max:");
        stressTestToDepthTest(depth, new Chessboard(), MAX_TABLE_SIZE_MB);
    }

    private static void stressTestToDepthTest(int depth, Chessboard board, int hashSize) {
        UCIEntryOld uciEntry = new UCIEntryOld();
        EngineSetOptionCommand e = new EngineSetOptionCommand("Hash", "" + hashSize);
        uciEntry.receive(e);

        EngineBetter.searchFixedDepth(board, depth);
        
        System.out.println("new entries:           " + newEntries);
        System.out.println("aged out entries:      " + agedOut);
        System.out.println("hits:                  " + hit);
        System.out.println("hits but already good: " + hitButAlreadyGood);
        System.out.println("hits to replace:       " + hitReplace);
        System.out.println("override:              " + override);
        System.out.println("real keys :            " + countRealEntries(keys, false));
        System.out.println("real entries:          " + countRealEntries(entries, false));
        System.out.println("---");
        
        long tks = countRealEntries(keys, true);
        System.out.println("total keys:            " + tks);
        long tes = countRealEntries(entries, true);
        System.out.println("total entries:         " + tes);
        System.out.println("TT size from Spec:     " + TABLE_SIZE);
        System.out.println("total space in bits:   " + (64 * (tks + tes)));
        System.out.println("---");
        System.out.println();
        System.out.println();
    }
    
    private static long countRealEntries(long[] arr, boolean t) {
        long total = 0;
        if (t) {
            return arr.length;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                continue;
            }
            total++;
        }
        return total;
    }

}

