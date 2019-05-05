package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.Engine.hashTableReturn;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;

@Ignore
@Disabled
public class TranspositionTableStressTest {

    static final int depth = 12;
    static Engine engine = new Engine();
    static long sleepBetween = 1000;
    
    @BeforeEach
    public void setup() throws InterruptedException {
        Thread.sleep(sleepBetween);
        System.gc();
        ResettingUtils.reset();
        Engine.sendBestMove = false;
    }

    @AfterAll
    public static void reset() {
        ResettingUtils.reset();
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

    @Test
    void testNumber() {
        int d = 18;
        System.out.println("default to depth " + d);
        stressTestToDepthTest(d, new Chessboard(), DEFAULT_TABLE_SIZE_MB);
    }

    
    
//    @Test
//    void testDefaultMT() {
//        System.out.println("default MT: ");
//        PRINT_PV = true;
//        stressTestToDepthTest(12, new Chessboard(), DEFAULT_TABLE_SIZE_MB, 4);
//    }


    @Test
    void testFine70Single() {
        System.out.println("fine 70 tt stats single: ");
        stressTestToDepthTest(32,
                new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -"), DEFAULT_TABLE_SIZE_MB);
    }

//    @Test
//    void testFine70MT() {
//        System.out.println("fine 70 tt stats MT: ");
//        stressTestToDepthTest(32, 
//                new Chessboard("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - -"), DEFAULT_TABLE_SIZE_MB, 4);
//        ResettingUtils.reset();
//    }
//    
    private static void stressTestToDepthTest(int depth, Chessboard board, int hashSize) {
        stressTestToDepthTest(depth, board, hashSize, 1);
    }
    
    private static void stressTestToDepthTest(int depth, Chessboard board, int hashSize, int numThreads) {
        TranspositionTable.initTableMegaByte(hashSize);
        Engine.setThreads(numThreads);

        Assert.assertEquals(Engine.numberOfMovesMade.length, numThreads);
        
        engine.receiveSearchSpecs(board, depth);
        engine.simpleSearch();

        System.out.println();
        System.out.println("total adds :           " + totalAdds);
        System.out.println("new entries:           " + newEntries);
        System.out.println("aged out entries:      " + agedOut);
        System.out.println("total hits:            " + totalHits);
        System.out.println("hits but already good: " + hitButAlreadyGood);
        System.out.println("hits to replace:       " + hitReplace);
        System.out.println("override:              " + override);
        System.out.println();
        System.out.println("successfulLookup:      " + successfulLookup);
        System.out.println("failed Lookup:         " + failedLookup);
        System.out.println("total Lookup:          " + totalLookup);
        System.out.println();
        System.out.println("depth enough to return " + hashTableReturn);
        System.out.println("ratio ret not ret      " + ((double) hashTableReturn / (double) totalLookup));
        System.out.println();
        if (numThreads == 1) {
//            Assert.assertTrue(hashTableReturn <= successfulLookup);
//            Assert.assertEquals(totalLookup - successfulLookup, failedLookup);
        }
        System.out.println("ratio succ total       " + ((double) successfulLookup / (double) totalLookup));
        System.out.println("ratio miss total       " + ((double) failedLookup / (double) totalLookup));
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

    public static long countRealEntries(long[] arr, boolean t) {
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

