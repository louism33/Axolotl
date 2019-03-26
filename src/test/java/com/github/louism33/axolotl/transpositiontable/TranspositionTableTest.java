package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.MoveConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public class TranspositionTableTest {

    @Test
    public void buildTableEntryTestSingle() {
        int total = 1_000_000;
        for (int i = 0; i < total; i ++) {

            Random r = new Random();

            int move = r.nextInt(MoveConstants.CHECKING_MOVE_MASK);
            int score = r.nextInt(EvaluationConstants.SHORT_MAXIMUM * 2) - EvaluationConstants.SHORT_MAXIMUM;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            Assert.assertEquals(getMove(entry), move);
            Assert.assertEquals(getScore(entry, ply), score);
            Assert.assertEquals(getDepth(entry), depth);
            Assert.assertEquals(getFlag(entry), flag);
        }
    }

    @Test
    public void buildTableEntryTestToLimit() {
        int total = EngineSpecifications.TABLE_SIZE;

        TranspositionTable.initTable(total);

        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();

        for (int key = 0; key < total; key ++) {
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            if (move == 0){
                move = 1;
            }
            int score = r.nextInt(EvaluationConstants.SHORT_MAXIMUM * 2) - EvaluationConstants.SHORT_MAXIMUM;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            allEntries.add(entry);

            TranspositionTable.addToTableReplaceByDepth(key, move, score, depth, flag, ply);
        }

        for (int key = 0; key < total; key ++) {
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(key));

            Assert.assertEquals(getMove(entry), (int) moves.get(key));
            Assert.assertEquals(getScore(entry, plys.get(key)), (int) scores.get(key));
            Assert.assertEquals(getDepth(entry), (int) depths.get(key));
            Assert.assertEquals(getFlag(entry), (int) flags.get(key));
        }
    }



    @Test
    public void tableBucketTest() {
        // a test to see if adding different items to the same key results in them being in different buckets

        int total = bucketSize;

        TranspositionTable.initTable(total);

        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();

        for (int i = 0; i < total; i ++) {
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            int score = r.nextInt(EvaluationConstants.SHORT_MAXIMUM * 2) - EvaluationConstants.SHORT_MAXIMUM;
            int depth = r.nextInt(127);
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);

            long entry = buildTableEntry(move, score, depth, flag, ply);
            allEntries.add(entry);
            TranspositionTable.addToTableReplaceByDepth(4*i, move, score, depth, flag, ply);
        }

        for (int key = 0; key < total; key ++) {
            long entry = retrieveFromTable(4*key);

            Assert.assertEquals(entry, (long) allEntries.get(key));

            Assert.assertEquals(getMove(entry), (int) moves.get(key));
            Assert.assertEquals(getScore(entry, plys.get(key)), (int) scores.get(key));
            Assert.assertEquals(getDepth(entry), (int) depths.get(key));
            Assert.assertEquals(getFlag(entry), (int) flags.get(key));
        }
    }




    @Test
    public void overrideTest() {
        int total = EngineSpecifications.TABLE_SIZE;

        TranspositionTable.initTable(total);

        List<Long> allEntries = new ArrayList<>();
        List<Integer> moves = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        List<Integer> flags = new ArrayList<>();
        List<Integer> plys = new ArrayList<>();

        for (int key = 0; key < total; key ++) {
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            if (move == 0){
                move = 1;
            }
            int score = r.nextInt(EvaluationConstants.SHORT_MAXIMUM * 2) - EvaluationConstants.SHORT_MAXIMUM;
            int depth = 10;
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);

            TranspositionTable.addToTableReplaceByDepth(key, move, score, depth, flag, ply);
        }

        for (int key = 0; key < total; key ++) {
            Random r = new Random();

            int move = r.nextInt() & MOVE_MASK_WITHOUT_CHECK;
            if (move == 0){
                move = 1;
            }
            int score = r.nextInt(EvaluationConstants.SHORT_MAXIMUM * 2) - EvaluationConstants.SHORT_MAXIMUM;
            int depth = 20;
            int flag = r.nextInt(3);
            int ply = r.nextInt(99);

            moves.add(move);
            scores.add(score);
            depths.add(depth);
            flags.add(flag);
            plys.add(ply);

            long entry = buildTableEntry(move, score, depth, flag, ply);

            allEntries.add(entry);

            TranspositionTable.addToTableReplaceByDepth(key, move, score, depth, flag, ply);
        }

        for (int key = 0; key < total; key ++) {
            long entry = retrieveFromTable(key);

            Assert.assertEquals(entry, (long) allEntries.get(key));

            Assert.assertEquals(getMove(entry), (int) moves.get(key));
            Assert.assertEquals(getScore(entry, plys.get(key)), (int) scores.get(key));
            Assert.assertEquals(getDepth(entry), (int) depths.get(key));
            Assert.assertEquals(getFlag(entry), (int) flags.get(key));
        }
    }
}