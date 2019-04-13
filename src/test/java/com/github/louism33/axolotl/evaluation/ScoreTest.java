package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Art;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.evaluation.Score.*;

public class ScoreTest {

    @Test
    void anotherTest() {
        int m = -89;
        int e = 54;
        int score = bs(m, e);
        Assert.assertEquals(m, getMGScore(score));
        Assert.assertEquals(e, getEGScore(score));
    }
    
    @Test
    void mScoreTest() {
        int m = 123;
        int score = bs(m, 0);
        Assert.assertEquals(m, getMGScore(score));
    }

    @Test
    void mnScoreTest() {
        int m = -maxAbsScore;
        int score = bs(m, 0);
        Assert.assertEquals(m, getMGScore(score));
    }

    @Test
    void enScoreTest() {
        int m = -maxAbsScore;
        int score = bs(0, m);
        Assert.assertEquals(m, getEGScore(score));
    }

    @Test
    void menScoreTest() {
        int m = -maxAbsScore;
        int score = bs(m, m);
        Assert.assertEquals(m, getMGScore(score));
        Assert.assertEquals(m, getEGScore(score));
    }

    @Test
    void eScoreTest() {
        int m = 123;
        int score = bs(0, m);
        Assert.assertEquals(m, getEGScore(score));
    }

    @Test
    void buildScoreTest() {
        int mgScore = 123;
        int egScore = 321;
        int score = bs(mgScore, egScore);

        Assert.assertEquals(mgScore, getMGScore(score));
        Assert.assertEquals(egScore, getEGScore(score));
    }


    @Test
    void buildScoreNegTest() {
        int mgScore = -123;
        int egScore = 321;
        int score = bs(mgScore, egScore);

        Assert.assertEquals(mgScore, getMGScore(score));
        Assert.assertEquals(egScore, getEGScore(score));
    }

    @Test
    void buildBulkTest() {
        int l = maxAbsScore;
        for (int m = -l; m < l; m++) {
            for (int e = -l; e < l; e++) {
                int score = bs(m, e);
                Assert.assertEquals(m, getMGScore(score));
                Assert.assertEquals(e, getEGScore(score));
            }
        }
    }

    @Test
    void testRetrieve() {
        int[] earlyMaterial = {100, 200, 300, 400, 500};
        int[] endMaterial = {111, 222, 333, 444, 555};
        int[] material = new int[earlyMaterial.length];
        
        for (int i = 0; i < earlyMaterial.length; i++) {
            material[i] = Score.bs(earlyMaterial[i], endMaterial[i]);
        }

        for (int i = 0; i < earlyMaterial.length; i++) {
            final int sscore = getScore(material[i], 100);
            Assert.assertEquals(sscore, earlyMaterial[i]);

            final int escore = getScore(material[i], 0);
            Assert.assertEquals(escore, endMaterial[i]);

        }
    }

    @Test
    void test2Retrieve() {
        int[] earlyMaterial = {100, 200, 300, 400, 500};
        int[] endMaterial = {111, 222, 333, 444, 555};
        int[] material = new int[earlyMaterial.length];

        int testTotalm = 0;
        int testTotale = 0;
        
        for (int i = 0; i < endMaterial.length; i++) {
            material[i] = Score.bs(earlyMaterial[i], endMaterial[i]);
            testTotalm += earlyMaterial[i];
            testTotale += endMaterial[i];
        }

        int total = 0;
        
        for (int i = 0; i < endMaterial.length; i++) {
            total += material[i];
        }

        Assert.assertEquals(testTotalm, Score.getScore(total, 100));
        Assert.assertEquals(testTotale, Score.getScore(total, 0));
    }


    @Test
    void test3Retrieve() {
        int[] earlyMaterial = {-100, -200, -300, -400, -500};
        int[] endMaterial = {-111, -222, -333, -444, -555};
        int[] material = new int[earlyMaterial.length];

        int testTotalm = 0;
        int testTotale = 0;

        for (int i = 0; i < endMaterial.length; i++) {
            material[i] = Score.bs(earlyMaterial[i], endMaterial[i]);
            testTotalm += earlyMaterial[i];
            testTotale += endMaterial[i];
        }

        int total = 0;

        for (int i = 0; i < endMaterial.length; i++) {
            total += material[i];
        }

        Assert.assertEquals(testTotalm, Score.getScore(total, 100));
        Assert.assertEquals(testTotale, Score.getScore(total, 0));
    }
}
