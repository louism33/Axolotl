package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

public class TranspositionTable {

    private static long[] keys;
    private static long[] entries;
    private static boolean tableReady = false;
    private static int shiftAmount;

    public static void initTable(int mb){
        int index = Integer.numberOfTrailingZeros(mb);
        Assert.assertTrue(Integer.bitCount(mb) == 1
                && index < 16);
        int size = mb; 
        // todo ? modulo ?
        keys = new long[size];
        entries = new long[size];
        shiftAmount = 64 - index;
        
        tableReady = true;
    }
    
    public static void reset(){
        Arrays.fill(keys, 0);
        Arrays.fill(entries, 0);
    }
    
    public static void addToTable(long key, long entry){
        if (!tableReady){
            initTable(EngineSpecifications.DEFAULT_TABLE_SIZE);
        }
        int index = (int) (key >>> shiftAmount);
        keys[index] = key;
        entries[index] = entry;
    }
    
    public static long retrieveFromTable(long key){
        if (!tableReady){
            initTable(EngineSpecifications.DEFAULT_TABLE_SIZE);
        }
        int index = (int) (key >>> shiftAmount);
        Assert.assertTrue(index >= 0);
        long keyValue = keys[index];
        if (keyValue == 0){
            return 0;
        }
        long entryValue = entries[index];
        Assert.assertTrue(entryValue != 0);
        return entryValue;
    }

    public static long buildTableEntry(int move, int score, int depth, int flag, int ply){
        Assert.assertTrue(move != 0);
        Assert.assertTrue(score > Short.MIN_VALUE && score < Short.MAX_VALUE);
        Assert.assertTrue(flag >= 0 && flag < 4);

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

    public static int getScore(long entry){
        long l1 = (entry & SCORE_MASK) >>> scoreOffset;
        return (int) (l1 > twoFifteen ? l1 - twoSixteen : l1);
    }

    public static int getDepth(long entry){
        return (int) ((entry & DEPTH_MASK) >>> depth_offset);
    }

    public static int getFlag(long entry){
        return (int) ((entry & FLAG_MASK) >>> flagOffset);
    }


    public TranspositionTable(){}

}
