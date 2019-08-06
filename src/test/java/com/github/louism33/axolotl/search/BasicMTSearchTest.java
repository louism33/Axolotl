package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import static com.github.louism33.axolotl.search.EngineSpecifications.MASTER_DEBUG;
import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_THREADS;

//@Disabled
public class BasicMTSearchTest {

    private static Engine engine = new Engine();
    private static int timeLimit = 10_000;

    @BeforeClass
    public static void setup() {
        ResettingUtils.reset();
    }

    @AfterClass
    public static void tearDown() {
        ResettingUtils.reset();
    }

    public void nps() {
        Engine.calculateNPS();
        if (Engine.nps != 0) {
            System.out.println("nps: " + Engine.nps);
        }
        Engine.resetFull();
    }

    @Test
    public void testSingleDefault() {
        Engine.resetFull();
        final Chessboard board = new Chessboard();
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
    }

    @Test
    public void testSingleSet() {
        System.out.println("Testing simple search with one thread");
        final Chessboard board = new Chessboard();
        MASTER_DEBUG = true;
        Engine.setThreads(1);
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        nps();
    }

    @Test
    public void testTwo() {
        System.out.println("Testing simple search with two threads");
        final Chessboard board = new Chessboard();
        int threads = 2;
        Engine.setThreads(threads);
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        nps();
    }

    @Test
    public void testMax() {
        System.out.println("Testing simple search with " + MAX_THREADS + " threads");
        final Chessboard board = new Chessboard();
        Engine.setThreads(MAX_THREADS);
//        DEBUG = true;
        MASTER_DEBUG = true;
        SearchSpecs.basicTimeSearch(timeLimit);
        final int move = engine.simpleSearch(board);
        nps();
        MASTER_DEBUG = false;
    }

    @Test
    public void testAll() {
        MASTER_DEBUG = true;
        final Chessboard board = new Chessboard();
        System.out.println("Testing search with number of Threads: ");
        for (int t = 1; t <= MAX_THREADS; t++) {
            System.out.println(t + " ");
            Engine.setThreads(t);
            SearchSpecs.basicDepthSearch(10);
            final int move = engine.simpleSearch(board);
            Engine.resetFull();
        }
        System.out.println("\nAll ok.");
        MASTER_DEBUG = false;
    }
}


