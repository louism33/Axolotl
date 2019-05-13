package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.search.EngineSpecifications.NUMBER_OF_THREADS;
import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_EVAL;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;
import static java.lang.Long.numberOfTrailingZeros;

@SuppressWarnings("ALL")
public final class PawnTranspositionTable {

    public static int DEFAULT_PAWN_TABLE_SIZE_MB = 1;

    public static final int ENTRIES_PER_KEY = 9;
    private static final int PAWN_TABLE_SIZE_PER_MB = (1024 * 1024) / 64;
    public static int PAWN_TABLE_SIZE = DEFAULT_PAWN_TABLE_SIZE_MB * PAWN_TABLE_SIZE_PER_MB / (ENTRIES_PER_KEY + 1);

    public static boolean tableReady = false;

    private static final int bucketSize = 4;
    private static int shiftAmount = 64 - numberOfTrailingZeros(PAWN_TABLE_SIZE >> 1);

    public static final int CAPTURES = 0, SPANS = 2, FILE_WITHOUT_MY_PAWNS = 4, PASSED_PAWNS = 6, SCORE = 8;
    static final long[] noPawnsData = {0, 0, 0, 0, -1, -1, 0, 0, 0};
    private static final int XOR_INDEX = CAPTURES;

    public static int totalRequests = 0;
    public static int newEntries = 0;
    public static int hit = 0;
    public static int override = 0;

    /*
    what to store:
    one long for each type of pawn
    one long for weak squares
     */
    public static long[][] keys = new long[NUMBER_OF_THREADS][PAWN_TABLE_SIZE];
    //    private static long[][] returnArray = new long[NUMBER_OF_THREADS][ENTRIES_PER_KEY];
    public static long[][] pawnMoveData = new long[NUMBER_OF_THREADS][PAWN_TABLE_SIZE * ENTRIES_PER_KEY];

    public static void reset() {
        for (int t = 0; t < NUMBER_OF_THREADS; t++) {
            Arrays.fill(keys[t], 0);
            Arrays.fill(pawnMoveData[t], 0);
//            Arrays.fill(returnArray[t], 0);
        }
    }

    public static void initPawnTableMegaByte(int mb) {
        int maxSize = mb * PAWN_TABLE_SIZE_PER_MB;
        int offset;
        if (BitOperations.populationCount(ENTRIES_PER_KEY + 1) != 1) {
            offset = numberOfTrailingZeros(Integer.highestOneBit(ENTRIES_PER_KEY + 1) << 1);
        } else {
            offset = 0;
        }

        PAWN_TABLE_SIZE = maxSize;

        int actualTableSize = maxSize >> offset;
        shiftAmount = 64 - numberOfTrailingZeros(actualTableSize);

        actualTableSize += ENTRIES_PER_KEY;

        newEntries = 0;
        hit = 0;
        override = 0;
        totalRequests = 0;

        if (keys != null && keys.length == NUMBER_OF_THREADS && keys[0] != null && keys[0].length == actualTableSize) {
            for (int t = 0; t < NUMBER_OF_THREADS; t++) {
                Arrays.fill(keys[t], 0);
                Arrays.fill(pawnMoveData[t], 0);
//                Arrays.fill(returnArray[t], 0);
            }
        } else {
            keys = new long[NUMBER_OF_THREADS][actualTableSize];
            pawnMoveData = new long[NUMBER_OF_THREADS][actualTableSize * ENTRIES_PER_KEY];
//            returnArray = new long[NUMBER_OF_THREADS][ENTRIES_PER_KEY];
        }

        tableReady = true;
    }

    public static void initPawnTableMegaByte() {
        initPawnTableMegaByte(DEFAULT_PAWN_TABLE_SIZE_MB);
    }

    public static void initPawnTable(int maxEntries) {
        int maxSize = maxEntries;
        int offset;
        if (BitOperations.populationCount(ENTRIES_PER_KEY + 1) != 1) {
            offset = numberOfTrailingZeros(Integer.highestOneBit(ENTRIES_PER_KEY + 1) << 1);
        } else {
            offset = 0;
        }

        PAWN_TABLE_SIZE = maxSize;

        int actualTableSize = maxSize >> offset;
        shiftAmount = 64 - numberOfTrailingZeros(actualTableSize);

        actualTableSize += ENTRIES_PER_KEY;

        newEntries = 0;
        hit = 0;
        override = 0;
        totalRequests = 0;

        if (keys != null && keys.length == actualTableSize) {
            Arrays.fill(keys, 0);
            Arrays.fill(pawnMoveData, 0);
//            Arrays.fill(returnArray, 0);
        } else {
            keys = new long[NUMBER_OF_THREADS][actualTableSize];
            pawnMoveData = new long[NUMBER_OF_THREADS][actualTableSize * ENTRIES_PER_KEY];
//            returnArray = new long[NUMBER_OF_THREADS][ENTRIES_PER_KEY];
        }

        tableReady = true;
    }

    private static void addToTableReplaceArbitrarily(long key, long[] pawnData, int whichThread) {
        Assert.assertTrue(tableReady);

        int index = getIndex(key);

        int replaceMeIndex = 0;

        boolean newEntry = false;

        for (int i = index; i < index + bucketSize; i++) {
            final int entryIndex = i * ENTRIES_PER_KEY;
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
        System.arraycopy(pawnData, 0, pawnMoveData[whichThread], replaceMeIndex * ENTRIES_PER_KEY, ENTRIES_PER_KEY);
    }


    public static long[] getPawnData(Chessboard board, long key, int percentOfStartgame) {
        return getPawnData(board, key, percentOfStartgame, 0);
    }

    public static long[] getPawnData(Chessboard board, long key, int percentOfStartgame, int whichThread) {
        if (!tableReady) {
            initPawnTableMegaByte();
        }

        if (key == 0) {
            return noPawnsData;
        }

        long[] returnArray = new long[ENTRIES_PER_KEY];

        totalRequests++;

        if (!PRINT_EVAL) {
            int index = getIndex(key);
            for (int i = index; i < index + bucketSize; i++) {
                final int entryIndex = i * ENTRIES_PER_KEY;
                if ((keys[whichThread][i] ^ pawnMoveData[whichThread][entryIndex]) == key) {

                    System.arraycopy(pawnMoveData[whichThread], entryIndex, returnArray, 0, ENTRIES_PER_KEY);
                    hit++;

                    return returnArray;
                }
            }
        }

        returnArray = PawnEval.calculatePawnData(board, percentOfStartgame);
        int pawnFeatureScore = (int) returnArray[SCORE];
        addToTableReplaceArbitrarily(board.zobristPawnHash, returnArray, whichThread);

        return returnArray;
    }


    public static int getIndex(long key) {
        int index = (int) (key >>> shiftAmount);
        Assert.assertTrue(index >= 0);
        return index;
    }

    static long buildTableEntry(int move, int score, int depth, int flag, int ply, int age) {
        Assert.assertTrue(move != 0);
        Assert.assertTrue(score > Short.MIN_VALUE && score < Short.MAX_VALUE);
        Assert.assertTrue(flag >= 0 && flag < 4);

        if (score > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
            score += ply;
        } else if (score < IN_CHECKMATE_SCORE_MAX_PLY) {
            score -= ply;
        }
        long entry = 0;
        entry |= (move & TT_MOVE_MASK);
        entry |= (((long) score & SCORE_CLEANER) << scoreOffset);
        entry |= (((long) depth) << depth_offset);
        entry |= (((long) flag) << flagOffset);
        entry |= (((long) age) << ageOffset);
        return entry;
    }

    public static int getMove(long entry) {
        return (int) (entry & MOVE_MASK_WITHOUT_CHECK);
    }

    public static int getScore(long entry, int ply) {
        long l1 = (entry & SCORE_MASK) >>> scoreOffset;
        int score = 0; //(int) (l1 > twoFifteen ? l1 - twoSixteen : l1);
        if (score > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
            score -= ply;
        } else if (score < IN_CHECKMATE_SCORE_MAX_PLY) {
            score += ply;
        }
        return score;
    }

    public static int getDepth(long entry) {
        return (int) ((entry & DEPTH_MASK) >>> depth_offset);
    }

    public static int getFlag(long entry) {
        return (int) ((entry & FLAG_MASK) >>> flagOffset);
    }

    public static int getAge(long entry) {
        return (int) ((entry & AGE_MASK) >>> ageOffset);
    }

}
