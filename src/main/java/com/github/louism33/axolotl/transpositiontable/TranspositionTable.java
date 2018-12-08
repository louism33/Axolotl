package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.Assert;

import java.util.HashMap;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

public class TranspositionTable extends HashMap<Long, TranspositionTable.TableObject> {

    public static long[] keys;
    public static long[] entries;
    public static boolean tableReady = false;
    public static int shiftAmount;

    public static void initTable(int mb){
        int index = Integer.numberOfTrailingZeros(mb);
        Assert.assertTrue(Integer.bitCount(mb) == 1
                && index < 16);
        int size = mb; 
        keys = new long[size];
        entries = new long[size];
        shiftAmount = 64 - index;
        
        tableReady = true;
    }
    
    public static void addToTable(long key, long entry){
        if (!tableReady){
            initTable(EngineSpecifications.DEFAULT_TABLE_SIZE);
        }
        int index = (int) key >>> shiftAmount;
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


    public static class TableObject {
        private final int move;
        private final int score;
        private final int depth;
        private final Flag flag;

        public enum Flag {
            EXACT, LOWERBOUND, UPPERBOUND
        }

        public TableObject(int move, int score, int depth, Flag flag) {
            this.move = move;
            this.score = score;
            this.depth = depth;
            this.flag = flag;
        }

        public int getMove() {
            return move;
        }

        public int getScore(int ply) {
            if (score > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score - ply;
            }
            else if (score < IN_CHECKMATE_SCORE_MAX_PLY){
                return score + ply;
            }
            return score;
        }

        public Flag getFlag() {
            return flag;
        }

        public int getDepth() {
            return depth;
        }

        @Override
        public String toString() {
            return "TableObject{" +
                    "move=" + move +
                    ", score=" + score +
                    ", depth=" + depth +
                    ", flag=" + flag +
                    '}';
        }
    }
}
