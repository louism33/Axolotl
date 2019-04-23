package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.chesscore.Chessboard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.*;

public class BasicMTSearch {

    private static Engine engine = new Engine();

    @BeforeClass
    public static void setup() {
        PRINT_PV = false;
        DEBUG = false;
        engine.uciEntry = new UCIEntry();
    }

    @AfterClass
    public static void tearDown() {
        PRINT_PV = false;
    }

    @Test
    public void testSingleSet() {
        Engine.resetFull();
        final Chessboard board = new Chessboard();
        Engine.setThreads(1);
        engine.receiveSearchSpecs(board, true, 1_000);
        engine.simpleSearch();
    }

    @Test
    public void testSingleDefault() {
        Engine.resetFull();
        final Chessboard board = new Chessboard();
        engine.receiveSearchSpecs(board, true, 1_000);
        engine.simpleSearch();
    }

    @Test
    public void testTwo() {
        Engine.resetFull();
        final Chessboard board = new Chessboard();
        int threads = 2;

        Engine.setThreads(threads);
        engine.receiveSearchSpecs(board, 8);
        engine.simpleSearch();
    }

    @Test
    public void testMax() {
        final Chessboard board = new Chessboard();
        Engine.setThreads(MAX_THREADS);

        engine.receiveSearchSpecs(board, 10);
        engine.receiveSearchSpecs(board, true, 2_000);
        engine.simpleSearch();
    }

    @Test
    public void testAll() {
        final Chessboard board = new Chessboard();
        for (int t = 1; t <= MAX_THREADS; t++) {
            Engine.setThreads(t);
            engine.receiveSearchSpecs(board, 10);
            engine.simpleSearch();
        }
    }
}


