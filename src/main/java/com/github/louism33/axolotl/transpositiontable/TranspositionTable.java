package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

public class TranspositionTable {

    static long[] keys;
    static long[] entries;
    private static boolean tableReady = false;
    private static int moduloAmount;
    static int bucketSize = 4;

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

    public static int newEntries = 0;
    public static int hit = 0;
    public static int hitButAlreadyGood = 0;
    public static int hitReplace = 0;
    public static int override = 0;
    
    public static void addToTableReplaceByDepth(long key, int bestMove, 
                                                int bestScore, int depth, int flag, int ply){
        if (!tableReady){
            initTable(EngineSpecifications.TABLE_SIZE);
        }

        int index = getIndex(key);

        int replaceMeIndex = 0;
        int worstDepth = EvaluationConstants.SHORT_MAXIMUM;

        for (int i = 0; i < bucketSize; i++) {
            int enhancedIndex = (index + i) % moduloAmount;
            
            long currentKey = (keys[enhancedIndex] ^ entries[enhancedIndex]);
            long currentEntry = entries[enhancedIndex];

            if (currentEntry == 0) {
                newEntries++;
                replaceMeIndex = enhancedIndex;
                break;
            }

            int currentDepth = getDepth(currentEntry);
            
            if (key == currentKey) {
                hit++;
                if (depth < currentDepth && flag != EXACT) {
                    hitButAlreadyGood++;
                    return;
                }
                hitReplace++;
                replaceMeIndex = enhancedIndex;
                break;
            }

            override++;

            if (currentDepth < worstDepth){
                worstDepth = currentDepth;
                replaceMeIndex = enhancedIndex;
            }
        }
        
        long possibleEntry = buildTableEntry(bestMove & MoveOrderer.MOVE_MASK, bestScore, depth, flag, ply);

        keys[replaceMeIndex] = key ^ possibleEntry;
        entries[replaceMeIndex] = possibleEntry;
    }

    public static long retrieveFromTable(long key){
        if (!tableReady){
            initTable(EngineSpecifications.TABLE_SIZE);
        }

        int index = getIndex(key);

        for (int i = 0; i < bucketSize; i++) {
            int enhancedIndex = (index + i) % moduloAmount;
            if ((keys[enhancedIndex] ^ entries[enhancedIndex]) == key) {
                return entries[enhancedIndex];
            }
        }

        return 0;
    }

    private static int getIndex(long key) {
        int index = (int) (key % moduloAmount);

        if (index < 0){
            index += moduloAmount;
        }

        Assert.assertTrue(index >= 0);

        return index;
    }

    static long buildTableEntry(int move, int score, int depth, int flag, int ply){
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
