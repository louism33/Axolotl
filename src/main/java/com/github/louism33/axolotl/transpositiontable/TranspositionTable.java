package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.chesscore.BitOperations;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;
import static java.lang.Long.numberOfTrailingZeros;

public final class TranspositionTable {

    public static long[] keys;
    public static long[] entries;
    private static boolean readyTT = false;
    public static int shiftAmount;
    static final int bucketSize = 4;

    public static void initTableMegaByte(int mb, boolean force) {
        if (readyTT && !force) {
            return;
        }

        int maxSize = mb * TABLE_SIZE_PER_MB;
        int offset = 1;

        if (BitOperations.populationCount(maxSize) != 1) {
            maxSize = Integer.highestOneBit(maxSize) << 1;
        }

        TABLE_SIZE = maxSize;

        int actualTableSize = maxSize >> offset; // keys[] and entries[] will have this number of elements
        shiftAmount = 64 - numberOfTrailingZeros(actualTableSize);

        actualTableSize += bucketSize;

        totalAdds = 0;
        newEntries = 0;
        agedOut = 0;
        totalHits = 0;
        hitButAlreadyGood = 0;
        hitReplace = 0;
        override = 0;

        successfulLookup = 0;
        failedLookup = 0;
        totalLookup = 0;

        if (keys != null && keys.length == actualTableSize) {
            Arrays.fill(keys, 0);
            Arrays.fill(entries, 0);
        } else {
            keys = new long[actualTableSize];
            entries = new long[actualTableSize];
        }

        readyTT = true;
    }

    // used for testing
    public static void initTable(int maxEntries) {
        if (BitOperations.populationCount(maxEntries) != 1) {
            System.out.println("please select a multiple of two for the hash table size");
            maxEntries = Integer.highestOneBit(maxEntries) << 1; // round up
        }

        if (DEBUG) {
            System.out.println("info string initialising hash table with value " + maxEntries);
        }

        int maxSize = maxEntries;

        int offset = 1;

        TABLE_SIZE = maxSize;

        int actualTableSize = maxSize >> offset;

        shiftAmount = 64 - numberOfTrailingZeros(actualTableSize);

        actualTableSize += bucketSize;

        totalAdds = 0;
        newEntries = 0;
        agedOut = 0;
        totalHits = 0;
        hitButAlreadyGood = 0;
        hitReplace = 0;
        override = 0;

        successfulLookup = 0;
        failedLookup = 0;
        totalLookup = 0;

        if (keys != null && keys.length == actualTableSize) {
            Arrays.fill(keys, 0);
            Arrays.fill(entries, 0);
        } else {
            keys = new long[actualTableSize];
            entries = new long[actualTableSize];
        }

        readyTT = true;
    }

    public static int totalAdds = 0;
    public static int newEntries = 0;
    public static int agedOut = 0;
    public static int totalHits = 0;
    public static int hitButAlreadyGood = 0;
    public static int hitReplace = 0;
    public static int override = 0;

    public static int successfulLookup = 0;
    public static int failedLookup = 0;
    public static int totalLookup = 0;

    public static void addToTableReplaceByDepth(long key, int bestMove,
                                                int bestScore, int depth, int flag, int ply, int age) {
        initTableMegaByte(TABLE_SIZE_MB, false);
        Assert.assertTrue(readyTT);

        totalAdds++;

        final int index = getIndex(key);

        int replaceMeIndex = 0;
        int worstDepth = SHORT_MAXIMUM;

        for (int i = index; i < index + bucketSize; i++) {
            final long currentKey = (keys[i] ^ entries[i]);
            final long currentEntry = entries[i];

            if (currentEntry == 0) {
                newEntries++;
                replaceMeIndex = i;
                break;
            }

            final int currentDepth = getDepth(currentEntry);

            if (key == currentKey) {
                totalHits++;
                if (depth < currentDepth && flag != EXACT) {
                    hitButAlreadyGood++;
                    return;
                }
                hitReplace++;
                replaceMeIndex = i;
                break;
            }

            final int ageOfEntryInTable = getAge(currentEntry);

            if (isTooOld(ageOfEntryInTable, age)) {
                agedOut++;
                replaceMeIndex = i;
                break;
            }

            override++;

            if (currentDepth < worstDepth) {
                worstDepth = currentDepth;
                replaceMeIndex = i;
            }
        }

        final long possibleEntry = buildTableEntry(bestMove & MOVE_MASK_WITH_CHECK, bestScore, depth, flag, ply, age);

        keys[replaceMeIndex] = key ^ possibleEntry;
        entries[replaceMeIndex] = possibleEntry;
    }

    public static long retrieveFromTable(long key) {
        initTableMegaByte(TABLE_SIZE_MB, false);
        Assert.assertTrue(readyTT);

        totalLookup++;

        final int index = getIndex(key);

        for (int i = index; i < index + bucketSize; i++) {
            if ((keys[i] ^ entries[i]) == key) {
                successfulLookup++;
                return entries[i];
            }
        }

        failedLookup++;

        return 0;
    }

    static boolean isTooOld(int alreadyThere, int goingIn) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(alreadyThere < ageModulo);
            Assert.assertTrue(goingIn < ageModulo);
        }

        for (int i = 0; i < acceptableAges; i++) {
            int i1 = (alreadyThere + i) % ageModulo;
            if (i1 == goingIn) {
                return false;
            }
        }
        return true;
    }

    public static int getIndex(long key) {
        return (int) (key >>> shiftAmount);
    }

    static long buildTableEntry(int move, int score, int depth, int flag, int ply, int age) {
        // move can be 0 if null move?
        if (MASTER_DEBUG) {
            //        Assert.assertTrue(move != 0);
            Assert.assertTrue(score > Short.MIN_VALUE && score < Short.MAX_VALUE);
            Assert.assertTrue(flag >= 0 && flag < 4);
        }


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
        int score = (int) (l1 > twoFifteen ? l1 - twoSixteen : l1);
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
