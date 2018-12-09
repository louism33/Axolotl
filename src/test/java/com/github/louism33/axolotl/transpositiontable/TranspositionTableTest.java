package com.github.louism33.axolotl.transpositiontable;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;

public class TranspositionTableTest {

    @Test
    public void buildTableEntryTest() {
        Random r = new Random();
        
        int total = 1_000_000;
        for (int i = 0; i < total; i ++) {
            int move = r.nextInt();
            int score = r.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(127);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            Assert.assertEquals(getMove(entry), move);
            Assert.assertEquals(getScore(entry), score);
            Assert.assertEquals(getDepth(entry), depth);
            Assert.assertEquals(getFlag(entry), flag);
        }
    }
}