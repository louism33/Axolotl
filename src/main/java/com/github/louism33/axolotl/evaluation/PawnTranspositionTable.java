package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.search.EngineSpecifications.PAWN_TABLE_SIZE;
import static com.github.louism33.axolotl.search.EngineSpecifications.TABLE_SIZE;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;
import static java.lang.Long.numberOfTrailingZeros;

@SuppressWarnings("ALL")
public final class PawnTranspositionTable {
    
    /*
    what to store:
    one long for each type of pawn
    one long for weak squares
     */

    private static final int ENTRIES_PER_KEY = 16;

    static final int PUSHES = 0, CAPTURES = 2, DOUBLE_CAPTURES = 4, SPANS = 6, FILE_WITHOUT_MY_PAWNS = 8, PASSED_PAWNS = 10;
    private static final int XOR_INDEX = PUSHES;
    
    public static long[] keys = new long[PAWN_TABLE_SIZE];
    public static int[] scores = new int[PAWN_TABLE_SIZE];
    public static long[] pawnMoveData = new long[PAWN_TABLE_SIZE * ENTRIES_PER_KEY];
    
    public static boolean tableReady = false;
    public static int shiftAmount = 64 - numberOfTrailingZeros(PAWN_TABLE_SIZE >> 1);
    static final int bucketSize = 4;

    public static void reset() {
        Arrays.fill(keys, 0);
        Arrays.fill(pawnMoveData, 0);
        Arrays.fill(scores, 0);
    }
    
    public static void initPawnTable(int maxEntries) {
        if (BitOperations.populationCount(maxEntries) != 1) {
            System.out.println("please select a multiple of two for the hash table size");
        }

        TABLE_SIZE = maxEntries;

        int actualTableSize = maxEntries >> 1;
        shiftAmount = 64 - numberOfTrailingZeros(actualTableSize);

        actualTableSize += ENTRIES_PER_KEY;

        newEntries = 0;
        agedOut = 0;
        hit = 0;
        hitButAlreadyGood = 0;
        hitReplace = 0;
        override = 0;

        if (keys != null && keys.length == actualTableSize) {
            Arrays.fill(keys, 0);
            Arrays.fill(pawnMoveData, 0);
            Arrays.fill(scores, 0);
        } else {
            keys = new long[actualTableSize];
            pawnMoveData = new long[actualTableSize * ENTRIES_PER_KEY];
            scores = new int[actualTableSize];
        }

        tableReady = true;
    }

    public static int newEntries = 0;
    public static int agedOut = 0;
    public static int hit = 0;
    public static int hitButAlreadyGood = 0;
    public static int hitReplace = 0;
    public static int override = 0;

    public static void addToTableReplaceArbitrarily(long key, long[] pawnTable, int score) {
        if (!tableReady) {
            initPawnTable(PAWN_TABLE_SIZE);
        }

        int index = getIndex(key);

        int replaceMeIndex = 0;

        for (int i = index; i < index + bucketSize; i++) {
            final int entryIndex = i * ENTRIES_PER_KEY;
            final long pawnMoveDatum = pawnMoveData[entryIndex];
            final long currentKey = (keys[i] ^ pawnMoveDatum);
            final long currentEntry = pawnMoveDatum;

            if (pawnTable[XOR_INDEX] != 0 && currentEntry == 0) {
                newEntries++;
                replaceMeIndex = i;
                break;
            }

            if (key == currentKey) {
                hit++;
                return;
            }

            override++;

            replaceMeIndex = i;
        }

        keys[replaceMeIndex] = key ^ pawnTable[XOR_INDEX];
        scores[replaceMeIndex] = score;
        System.arraycopy(pawnTable, 0, pawnMoveData, replaceMeIndex * ENTRIES_PER_KEY, ENTRIES_PER_KEY);
    }

    private static long[] returnArray = new long[ENTRIES_PER_KEY];
    
    public static long[] retrieveFromTable(long key) {
        Arrays.fill(returnArray, 0);
        if (!tableReady) {
            initPawnTable(PAWN_TABLE_SIZE);
        }

        int index = getIndex(key);

        for (int i = index; i < index + bucketSize; i++) {
            final int entryIndex = i * ENTRIES_PER_KEY;
            if ((keys[i] ^ pawnMoveData[entryIndex]) == key) {
                System.arraycopy(pawnMoveData, entryIndex, returnArray, 0, ENTRIES_PER_KEY);
                PawnEval.pawnScore = scores[i];
                return returnArray;
            }
        }

        return null;
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

        if (score > EvaluationConstantsOld.CHECKMATE_ENEMY_SCORE_MAX_PLY) {
            score += ply;
        } else if (score < EvaluationConstantsOld.IN_CHECKMATE_SCORE_MAX_PLY) {
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
        if (score > EvaluationConstantsOld.CHECKMATE_ENEMY_SCORE_MAX_PLY) {
            score -= ply;
        } else if (score < EvaluationConstantsOld.IN_CHECKMATE_SCORE_MAX_PLY) {
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
