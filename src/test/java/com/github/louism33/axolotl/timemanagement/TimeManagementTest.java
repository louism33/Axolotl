package com.github.louism33.axolotl.timemanagement;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.protocols.NoProtocolException;
import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.axolotl.util.Util;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeManagementTest {

    @BeforeAll
    static void setup() {
        EngineBetter.resetFull();
        EngineBetter.uciEntry = null;
    }

    @AfterAll
    static void reset() {
        EngineBetter.resetFull();
        EngineBetter.uciEntry = null;
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

            long allocateTime = TimeAllocator.allocateTime(maxTime, enemyTime, increment, movesToGo, fullMoves);

            Assert.assertTrue(allocateTime > 0);
            Assert.assertTrue(allocateTime < maxTime);
        }
    }
    
    @Test
    void dominantTest(){
        long allocateTime = TimeAllocator.allocateTime(485370, 38948, 6000, 0, 200);

        System.out.println(allocateTime);

    }

    @Test
    void movesToGoTest() throws IllegalNotationException {
        String mmm = "d2d4";
        String[] ms = mmm.split(" ");

        GenericBoard board = new GenericBoard(518);

        List<GenericMove> moves = new ArrayList<>();
        for (String m : ms) {
            moves.add(new GenericMove(m));
        }

        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);

        UCIEntry uciEntry = new UCIEntry();

        EngineBetter.resetFull();
        EngineBetter.uciEntry = uciEntry;

        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
        start.setClock(GenericColor.WHITE, 38948L);
        start.setClock(GenericColor.WHITE, 485370L);
        start.setClock(GenericColor.BLACK, 485370L);

//        start.setClock(GenericColor.WHITE, 60_000L);
//        start.setClock(GenericColor.BLACK, 60_000L);
        
        start.setClockIncrement(GenericColor.WHITE, 6000L);
        start.setClockIncrement(GenericColor.BLACK, 6000L);

        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception | Error e) {
            throw new AssertionError("crashed on moves to go");
        }

        System.out.println("moves to go ok");
    }
}
