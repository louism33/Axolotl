package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.protocols.NoProtocolException;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.MoveParser;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UCIPermanenceTest {

    @BeforeAll
    static void setup() {
        Util.reset();
    }

    @AfterAll
    static void reset() {
        Util.reset();
    }
    
    @Test
    void persistBoardTest() throws IllegalNotationException {
        String mmm = "e2e4 ";
        GenericBoard genericBoard = new GenericBoard(518);
        EngineBetter.resetFull();
        int max = 20;
        int d = 10;
        UCIEntry uciEntry = new UCIEntry();
        EngineBetter.uciEntry = uciEntry;
        for (int i = 0; i < max; i++) {
            String[] ms = mmm.split(" ");

            List<GenericMove> moves = new ArrayList<>();
            for (String m : ms) {
                moves.add(new GenericMove(m));
            }

            EngineAnalyzeCommand eac = new EngineAnalyzeCommand(genericBoard, moves);

            EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
            start.setDepth(d);

            try {
                uciEntry.receive(eac);
                uciEntry.receive(start);
            } catch (NoProtocolException ignored) {
                List<GenericMove> genericMoves = PVLine.retrievePV(uciEntry.board);
                String s = MoveParser.toString(EngineBetter.getAiMove());
                mmm += genericMoves.get(0) + " " + genericMoves.get(1) + " ";
                
            } catch (Exception | Error e) {
                throw new AssertionError("problem with uci persistence");
            }
        }
        System.out.println("uci board persists ok");
    }
}
