package com.github.louism33.axolotl.timemanagement;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.util.ResettingUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TimeManagementTest {

    @BeforeAll
    static void setup() {
        ResettingUtils.reset();
    }

    @AfterAll
    static void reset() {
        ResettingUtils.reset();
    }

    @Test
    void dontAllocateBelowZeroTest() {
        for (int i = 0; i < 100_000; i++) {
            Random r = new Random();

            int maxTime = r.nextInt(60_000_000) + 5001;
            int enemyTime = r.nextInt(60_000_000);
            int increment = r.nextInt(10_000);
            int movesToGo = r.nextInt(100);
            int fullMoves = r.nextInt(10000);

            long allocateTime = TimeAllocator.allocateTime(maxTime, enemyTime, increment, movesToGo);

            Assert.assertTrue(allocateTime > 0);
            Assert.assertTrue(allocateTime < maxTime);
        }
    }

    @Test
    void dominantTest() {
        long allocateTime = TimeAllocator.allocateTime(485370, 38948, 6000, 0);
        System.out.println(allocateTime);
    }

    @Test
    void shortTCTest() {
        long allocateTime = TimeAllocator.allocateTime(2000, 2000, 75, 0);
        System.out.println(allocateTime);
    }

}
