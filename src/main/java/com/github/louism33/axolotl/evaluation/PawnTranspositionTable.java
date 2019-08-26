package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.search.EngineSpecifications.NUMBER_OF_THREADS;
import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_EVAL;
import static com.github.louism33.chesscore.BoardConstants.*;
import static java.lang.Long.numberOfTrailingZeros;

@SuppressWarnings("ALL")
public final class PawnTranspositionTable {

    public static final int DEFAULT_PAWN_TABLE_SIZE_MB = 1;
    public static int PAWN_TABLE_SIZE_MB = DEFAULT_PAWN_TABLE_SIZE_MB;

    public static final int ENTRIES_PER_KEY = 9;
    public static int ROUNDED_ENTRIES_PER_KEY;
    private static final int PAWN_TABLE_SIZE_PER_MB = 1_000_000 / 8;

    public static boolean tableReady = false;

    private static final int bucketSize = 4;
    private static int shiftAmount;

    public static final int CAPTURES = 0, SPANS = 2, FILE_WITHOUT_MY_PAWNS = 4, PASSED_PAWNS = 6, SCORE = 8;
    static final long[] noPawnsData = {0, 0, 0, 0, -1, -1, 0, 0, 0};
    private static final int XOR_INDEX = CAPTURES;

    public static int totalRequests = 0;
    public static int newEntries = 0;
    public static int hit = 0;
    public static int override = 0;

    public static long[][] keys;
    private static long[][] returnArrayBackend;
    public static long[][] pawnMoveData;

    public static void reset() {
        for (int t = 0; t < NUMBER_OF_THREADS; t++) {
            Arrays.fill(keys[t], 0);
            Arrays.fill(pawnMoveData[t], 0);
            Arrays.fill(returnArrayBackend[t], 0);
        }
    }

    public static void initPawnTableDefault(boolean force) {
        if (force || tableReady) {
            initPawnTableMB(DEFAULT_PAWN_TABLE_SIZE_MB);
        }
        Assert.assertTrue(tableReady);
    }
    
    private static void initPawnTableMB(int mb) {
        int maxSize = mb * PAWN_TABLE_SIZE_PER_MB; // amount of longs that can be used in pmd[]
        int offset;

        if (BitOperations.populationCount(maxSize) != 1) {
            maxSize = Integer.highestOneBit(maxSize) << 1;
        } 
        
        if (BitOperations.populationCount(ENTRIES_PER_KEY) != 1) {
            ROUNDED_ENTRIES_PER_KEY = Integer.highestOneBit(ENTRIES_PER_KEY) << 1;
            offset = numberOfTrailingZeros(ROUNDED_ENTRIES_PER_KEY);
        } else {
            ROUNDED_ENTRIES_PER_KEY = ENTRIES_PER_KEY;
            offset = numberOfTrailingZeros(ENTRIES_PER_KEY);
        }

        int sizeOfIndexArray = maxSize >> offset;
        shiftAmount = 64 - numberOfTrailingZeros(sizeOfIndexArray);

        newEntries = 0;
        hit = 0;
        override = 0;
        totalRequests = 0;

        if (keys != null && keys.length == NUMBER_OF_THREADS && keys[0] != null && keys[0].length == maxSize) {
            for (int t = 0; t < NUMBER_OF_THREADS; t++) {
                Arrays.fill(keys[t], 0);
                Arrays.fill(pawnMoveData[t], 0);
                Arrays.fill(returnArrayBackend[t], 0);
            }
        } else {
            keys = new long[NUMBER_OF_THREADS][sizeOfIndexArray + ROUNDED_ENTRIES_PER_KEY];
            pawnMoveData = new long[NUMBER_OF_THREADS][maxSize + bucketSize * ROUNDED_ENTRIES_PER_KEY];
            returnArrayBackend = new long[NUMBER_OF_THREADS][ROUNDED_ENTRIES_PER_KEY];
            Assert.assertTrue(sizeOfIndexArray * ROUNDED_ENTRIES_PER_KEY == maxSize);
        }

        Assert.assertTrue(noPawnsData.length == ENTRIES_PER_KEY);
        tableReady = true;
    }

    private static void addToTableReplaceArbitrarily(long key, long[] pawnData, int whichThread) {
        Assert.assertTrue(tableReady);

        int index = getIndex(key);

        int replaceMeIndex = 0;

        boolean newEntry = false;

        for (int i = index; i < index + bucketSize; i++) {
            final int entryIndex = i * ROUNDED_ENTRIES_PER_KEY;
            final long pawnMoveDatum = pawnMoveData[whichThread][entryIndex];
            final long currentKey = (keys[whichThread][i] ^ pawnMoveDatum);
            final long currentEntry = pawnMoveDatum;

            if (currentEntry == 0) {
                newEntry = true;
                replaceMeIndex = i;
                break;
            }

            replaceMeIndex = i;
        }

        if (newEntry) {
            newEntries++;
        } else {
            override++;
        }

        keys[whichThread][replaceMeIndex] = key ^ pawnData[XOR_INDEX];
        System.arraycopy(pawnData, 0, pawnMoveData[whichThread], replaceMeIndex * ROUNDED_ENTRIES_PER_KEY, ROUNDED_ENTRIES_PER_KEY);
    }


    public static long[] getPawnData(Chessboard board, long key, int percentOfStartgame) {
        return getPawnData(board, key, percentOfStartgame, 0);
    }

    public static long[] getPawnData(Chessboard board, long key, int percentOfStartgame, int whichThread) {
        if (!tableReady) {
            initPawnTableDefault(false);
            Assert.assertTrue(tableReady);
        }

        if (key == 0) {
            Assert.assertTrue((board.pieces[WHITE][PAWN] | board.pieces[BLACK][PAWN]) == 0);
            return noPawnsData;
        }
        
        long[] returnArray = returnArrayBackend[whichThread];

        totalRequests++;

        if (!PRINT_EVAL) {
            int index = getIndex(key);
            for (int i = index; i < index + bucketSize; i++) {
                final int entryIndex = i * ROUNDED_ENTRIES_PER_KEY;
                if ((keys[whichThread][i] ^ pawnMoveData[whichThread][entryIndex]) == key) {

                    System.arraycopy(pawnMoveData[whichThread], entryIndex, returnArray, 0, ROUNDED_ENTRIES_PER_KEY);
                    hit++;

                    return returnArray;
                }
            }
        }

        returnArray = PawnEval.calculatePawnData(board, percentOfStartgame, whichThread);
        int pawnFeatureScore = (int) returnArray[SCORE];
        addToTableReplaceArbitrarily(board.zobristPawnHash, returnArray, whichThread);

        return returnArray;
    }


    public static int getIndex(long key) {
        int index = (int) (key >>> shiftAmount);
        Assert.assertTrue(index >= 0);
        return index;
    }

}
