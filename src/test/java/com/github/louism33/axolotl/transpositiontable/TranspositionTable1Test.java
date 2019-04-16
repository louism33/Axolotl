package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.MoveConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MAXIMUM;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.EXACT;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public class TranspositionTable1Test {

    @BeforeAll
    static void setup() {
        EngineBetter.resetFull();
        
    }

    @AfterAll
    static void reset() {
        EngineBetter.resetFull();
        
    }
    
    @Test
    public void buildTableEntrySingleTest() {
        int total = 1_000_000;
        for (int i = 0; i < total; i ++) {

            Random r = new Random();

            int move = r.nextInt(MoveConstants.CHECKING_MOVE_MASK - 1) + 1;
            int score = r.nextInt(SHORT_MAXIMUM * 2) - SHORT_MAXIMUM;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);
            int age = r.nextInt(8);

            long entry = buildTableEntry(move, score, depth, flag, ply, age);

            Assert.assertEquals(getMove(entry), move);
            Assert.assertEquals(getScore(entry, ply), score);
            Assert.assertEquals(getDepth(entry), depth);
            Assert.assertEquals(getFlag(entry), flag);
            Assert.assertEquals(getAge(entry), age);
        }
    }

    @Test
    public void buildTableEntrySingle2Test() {
        int total = 1_000_000;
        for (int i = 0; i < total; i ++) {

            Random r = new Random();

            int move = r.nextInt(MoveConstants.CHECKING_MOVE_MASK-1)+1;
            int score = r.nextInt(SHORT_MAXIMUM * 2) - SHORT_MAXIMUM;
            int depth = r.nextInt(126)+1;
            int flag = r.nextInt(3);
            int ply = r.nextInt(98)+1;
            int age = r.nextInt(8);

            long entry = buildTableEntry(move, score, depth, flag, ply, age);

            Assert.assertEquals(getMove(entry), move);
            Assert.assertEquals(getScore(entry, ply), score);
            Assert.assertEquals(getDepth(entry), depth);
            Assert.assertEquals(getFlag(entry), flag);
            Assert.assertEquals(getAge(entry), age);
        }
    }

    @Test
    public void ageTest() {

        int move = 111;
        int score = 222;
        int depth = 3;
        int flag = EXACT;
        int ply = 4;

        int age = 5;

        long entry = buildTableEntry(move, score, depth, flag, ply, age);

        Assert.assertEquals(getMove(entry), move);
        Assert.assertEquals(getScore(entry, ply), score);
        Assert.assertEquals(getDepth(entry), depth);
        Assert.assertEquals(getFlag(entry), flag);
        Assert.assertEquals(getAge(entry), age);
    }

    @Test
    public void buildTableEntryTestToLimit() {
        int total = EngineSpecifications.TABLE_SIZE;
        
        TranspositionTable.initTable(total);
        total /= 2; // because two arrays in the TT

        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();

        for (int k = 0; k < total; k ++) {
            long key = (long) k << shiftAmount;
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            if (move == 0){
                move = 1;
            }
            int score = r.nextInt(SHORT_MAXIMUM * 2) - SHORT_MAXIMUM;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);
            int age = 3;

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);
            ages.add(age);

            long entry = buildTableEntry(move, score, depth, flag, ply, age);

            allEntries.add(entry);

            TranspositionTable.addToTableReplaceByDepth(key, move, score, depth, flag, ply, age);
        }


        for (int k = 0; k < total; k ++) {
            long key = (long) k << shiftAmount;
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(k));

            Assert.assertEquals(getMove(entry), (int) moves.get(k));
            Assert.assertEquals(getScore(entry, plys.get(k)), (int) scores.get(k));
            Assert.assertEquals(getDepth(entry), (int) depths.get(k));
            Assert.assertEquals(getFlag(entry), (int) flags.get(k));
            Assert.assertEquals(getAge(entry), (int) ages.get(k));
        }
    }



    @Test
    public void tableBucketTest() {
        // a test to see if adding different items to the same key results in them being in different indexes

        int total = bucketSize;

        TranspositionTable.initTable(total);
        total /= 2; // because two arrays in the TT
        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();

        for (int i = 0; i < total; i ++) {
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            int score = r.nextInt(SHORT_MAXIMUM * 2) - SHORT_MAXIMUM;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);
            int age = 3; // not random so no overwrite

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);
            ages.add(age);

            long entry = buildTableEntry(move, score, depth, flag, ply, age);
            allEntries.add(entry);
            TranspositionTable.addToTableReplaceByDepth(i, move, score, depth, flag, ply, age);
        }


        for (int key = 0; key < total; key ++) {
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(key));

            Assert.assertEquals(getMove(entry), (int) moves.get(key));
            Assert.assertEquals(getScore(entry, plys.get(key)), (int) scores.get(key));
            Assert.assertEquals(getDepth(entry), (int) depths.get(key));
            Assert.assertEquals(getFlag(entry), (int) flags.get(key));
            Assert.assertEquals(getAge(entry), (int) ages.get(key));
        }
    }




    @Test
    public void overrideByDepthTest() {
        int total = EngineSpecifications.TABLE_SIZE;

        TranspositionTable.initTable(total);
        total /= 2; // because two arrays in the TT
        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();

        for (int k = 0; k < total; k++) {
            long key = (long) k << shiftAmount;
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            if (move == 0){
                move = 1;
            }
            int score = r.nextInt(SHORT_MAXIMUM * 2) - SHORT_MAXIMUM;
            int depth = 10;
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);
            int age = 3;

            TranspositionTable.addToTableReplaceByDepth(key, move, score, depth, flag, ply, age);
        }
        
        for (int k = 0; k < total; k++) {
            long key = (long) k << shiftAmount;
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            if (move == 0){
                move = 1;
            }
            int score = r.nextInt(SHORT_MAXIMUM * 2) - SHORT_MAXIMUM;
            int depth = 20;
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);
            int age = 3;

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);
            ages.add(age);

            long entry = buildTableEntry(move, score, depth, flag, ply, age);

            allEntries.add(entry);

            TranspositionTable.addToTableReplaceByDepth(key, move, score, depth, flag, ply, age);
        }

        for (int k = 0; k < total; k ++) {
            long key = (long) k << shiftAmount;
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(k));

            Assert.assertEquals(getMove(entry), (int) moves.get(k));
            Assert.assertEquals(getScore(entry, plys.get(k)), (int) scores.get(k));
            Assert.assertEquals(getDepth(entry), (int) depths.get(k));
            Assert.assertEquals(getFlag(entry), (int) flags.get(k));
            Assert.assertEquals(getAge(entry), (int) ages.get(k));
        }
    }
}