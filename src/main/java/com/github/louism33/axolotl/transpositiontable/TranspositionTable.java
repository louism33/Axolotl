package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

public class TranspositionTable {

    private static long[] keys;
    private static long[] entries;
    private static boolean tableReady = false;
    private static int moduloAmount;

    public static void initTable(int maxEntries){
        moduloAmount = maxEntries;

        keys = new long[maxEntries];
        entries = new long[maxEntries];

        reset();

        tableReady = true;
    }

    private static void reset(){
        Arrays.fill(keys, 0);
        Arrays.fill(entries, 0);
    }

    public static void addToTableAlwaysReplace(long key, long entry){
        if (!tableReady){
            initTable(EngineSpecifications.TABLE_SIZE);
        }

        int index = getIndex(key);

        keys[index] = key;
        entries[index] = entry;
    }

    public static long retrieveFromTable(long key){
        if (!tableReady){
            initTable(EngineSpecifications.TABLE_SIZE);
        }

        int index = getIndex(key);

        return entries[index];
    }

    private static int getIndex(long key) {
        int index = (int) (key % moduloAmount);

        if (index < 0){
            index += moduloAmount;
        }

        Assert.assertTrue(index >= 0);

        return index;
    }

    public static long buildTableEntry(int move, int score, int depth, int flag, int ply){
        Assert.assertTrue(move != 0);
        Assert.assertTrue(score > Short.MIN_VALUE && score < Short.MAX_VALUE);
        Assert.assertTrue(flag >= 0 && flag < 4);

        if (score > EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY){
            score += ply;
        }
        else if (score < EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY){
            score -= ply;
        }
        long entry = 0;
        entry |= (move & MOVE_MASK);
        entry |= (((long) score & SCORE_CLEANER) << scoreOffset);
        entry |= (((long) depth) << depth_offset);
        entry |= (((long) flag) << flagOffset);
        return entry;
    }

    public static int getMove(long entry){
        return (int) (entry & MOVE_MASK);
    }

    public static int getScore(long entry, int ply){
        long l1 = (entry & SCORE_MASK) >>> scoreOffset;
        int score = (int) (l1 > twoFifteen ? l1 - twoSixteen : l1);
        if (score > EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY){
            score -= ply;
        }
        else if (score < EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY){
            score += ply;
        }
        return score;
    }

    public static int getDepth(long entry){
        return (int) ((entry & DEPTH_MASK) >>> depth_offset);
    }

    public static int getFlag(long entry){
        return (int) ((entry & FLAG_MASK) >>> flagOffset);
    }


}
