package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.chesscore.Chessboard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import static com.github.louism33.axolotl.search.Engine.sendBestMove;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;

public class BasicMTSearch {

    private static Engine engine = new Engine();
    private static int timeLimit = 10_000;
    
    @BeforeClass
    public static void setup() {
        sendBestMove = false;
        PRINT_PV = false;
        DEBUG = false;
        engine.uciEntry = new UCIEntry();
        Engine.resetFull();
    }

    @AfterClass
    public static void tearDown() {
        PRINT_PV = false;
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
        engine.receiveSearchSpecs(board, true, timeLimit);
        engine.simpleSearch();
    }
    
    @Test
    public void testSingleSet() {
        System.out.println("Testing simple search with one thread");
        final Chessboard board = new Chessboard();
        Engine.setThreads(1);
        engine.receiveSearchSpecs(board, true, timeLimit);
        engine.simpleSearch();
        nps();
    }

    @Test
    public void testTwo() {
        System.out.println("Testing simple search with two threads");
        final Chessboard board = new Chessboard();
        int threads = 2;

        Engine.setThreads(threads);
        engine.receiveSearchSpecs(board, true, timeLimit);
        engine.simpleSearch();
        nps();
    }

    @Test
    public void testMax() {
        System.out.println("Testing simple search with " + MAX_THREADS + " threads");
        final Chessboard board = new Chessboard();
        Engine.setThreads(MAX_THREADS);

        engine.receiveSearchSpecs(board, true, timeLimit);
        engine.simpleSearch();
        nps();
    }

    @Test
    public void testAll() {
        MASTER_DEBUG = true;
        final Chessboard board = new Chessboard();
        System.out.println("Testing search with number of Threads: ");
        for (int t = 1; t <= MAX_THREADS; t++) {
            System.out.println(t);
            Engine.setThreads(t);
            engine.receiveSearchSpecs(board, 10);
            engine.simpleSearch();
            Engine.resetFull();
        }
        System.out.println("All ok.");
        MASTER_DEBUG = false;
    }
}


