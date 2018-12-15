package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;

public class TranspositionTableTest {

    @Test
    public void buildTableEntryTestSingle() {
        int total = 1_000_000;
        for (int i = 0; i < total; i ++) {
            
            Random r = new Random();
            
            int move = r.nextInt();
            int score = r.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(127);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            Assert.assertEquals(getMove(entry), move);
            Assert.assertEquals(getScore(entry, 0), score);
            Assert.assertEquals(getDepth(entry), depth);
            Assert.assertEquals(getFlag(entry), flag);
        }
    }

    @Test
    public void buildTableEntryTestToLimit() {
        int total = EngineSpecifications.DEFAULT_TABLE_SIZE;

        TranspositionTable.initTable(total);

        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();
        
        for (int key = 0; key < total; key ++) {
            Random r = new Random();

            int move = r.nextInt();
            int score = r.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(127);

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);
            
            long entry = buildTableEntry(move, score, depth, flag, ply);

            allEntries.add(entry);
            
            TranspositionTable.addToTableAlwaysReplace(key, entry);
        }

        for (int key = 0; key < total; key ++) {
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(key));
            
            Assert.assertEquals(getMove(entry), (int) moves.get(key));
            Assert.assertEquals(getScore(entry, 0), (int) scores.get(key));
            Assert.assertEquals(getDepth(entry), (int) depths.get(key));
            Assert.assertEquals(getFlag(entry), (int) flags.get(key));
        }
    }



    @Test
    public void overrideTest() {
        int total = EngineSpecifications.DEFAULT_TABLE_SIZE;

        TranspositionTable.initTable(total);

        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();

        for (int key = 0; key < total; key ++) {
            Random r = new Random();

            int move = r.nextInt();
            int score = r.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(127);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            TranspositionTable.addToTableAlwaysReplace(key, entry);
        }

        for (int key = 0; key < total; key ++) {
            Random r = new Random();

            int move = r.nextInt();
            int score = r.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(127);

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            allEntries.add(entry);

            TranspositionTable.addToTableAlwaysReplace(key, entry);
        }

        for (int key = 0; key < total; key ++) {
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(key));

            Assert.assertEquals(getMove(entry), (int) moves.get(key));
            Assert.assertEquals(getScore(entry, 0), (int) scores.get(key));
            Assert.assertEquals(getDepth(entry), (int) depths.get(key));
            Assert.assertEquals(getFlag(entry), (int) flags.get(key));
        }

    }
}