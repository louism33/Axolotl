package com.github.louism33.axolotl.search;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.protocols.NoProtocolException;
import com.github.louism33.axolotl.main.UCIEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DontCrashTest {

    @Test
    private void searchMovesTest() throws IllegalNotationException {
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
        start.setMoveTime(5000L);

        List<GenericMove> searchmoves = new ArrayList<>();
        searchmoves.add(new GenericMove("d7d5"));
        searchmoves.add(new GenericMove("a7a5"));
        start.setSearchMoveList(searchmoves);

        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception e) {
            throw new AssertionError("crashed on search moves");
        }

        System.out.println("search moves ok");
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
        start.setMoveTime(5000L);
        start.setMovesToGo(12);

        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception e) {
            throw new AssertionError("crashed on moves to go");
        }

        System.out.println("moves to go ok");
    }


    @Test
    void depthTest() throws IllegalNotationException {
        String mmm = "d2d4";
        String[] ms = mmm.split(" ");

        GenericBoard board = new GenericBoard(518);

        List<GenericMove> moves = new ArrayList<>();
        for (String m : ms) {
            moves.add(new GenericMove(m));
        }

        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);

        UCIEntry uciEntry = new UCIEntry();

        EngineBetter.reset();
        EngineBetter.uciEntry = uciEntry;

        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
        start.setDepth(7);

        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception e) {
            throw new AssertionError("crashed on depth searched");
        }

        System.out.println("depth ok");
    }

    @Test
    void clockTest() throws IllegalNotationException {
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
        start.setClock(GenericColor.BLACK, 2000L);
        start.setClock(GenericColor.WHITE, 2000L);
        start.setClockIncrement(GenericColor.BLACK, 1000L);
        start.setClockIncrement(GenericColor.WHITE, 1000L);

        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception e) {
            throw new AssertionError("crashed on clock");
        }

        System.out.println("clock ok");
    }

    @Test
    void tirsaTest() throws IllegalNotationException {
        /*
        position startpos moves d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 c5d4 d1a4 c8d7 a4d4 e6d5 g1f3 b8c6 d4d1 f8b4 e2e3 g8f6
         */

        String mmm = "d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 c5d4 d1a4 c8d7 a4d4 e6d5 g1f3 b8c6 d4d1 f8b4 e2e3 g8f6";
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
        start.setMoveTime(5000L);

        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception e) {
            throw new AssertionError("crashed on tirsa's position");
        }

        System.out.println("tirsa ok");
    }



    @Test
    void endgame() throws IllegalNotationException {
        /*
     position startpos moves c2c4 e7e5 b1c3 g8f6 g1f3 b8c6 e2e4 f8b4 d2d3 d7d6 f1e2 h7h6 e1g1 b4c3 b2c3 a7a5 c1e3 e8e7 d1c2 c8g4 a1b1 d8c8 d3d4 e5d4 f3d4 g4e2 c2e2 e7d7 f2f3 a5a4 d4f5 h8g8 e2d3 c6e5 d3e2 d7e6 a2a3 c7c5 b1b6 e5c6 f1d1 f6e8 f3f4 c8c7 d1b1 a8b8 e2f3 g8f8 f5g3 e8f6 f4f5 e6d7 f3e2 f8e8 e3f4 h6h5 e2d1 c6e5 f4g5 e8h8 g5f6 g7f6 g3h5 d7c8 d1e2 e5d7 b6b2 c7a5 b2c2 c8c7 h5f4 c7c6 e2d3 d7e5 d3d5 c6c7 f4d3 h8f8 d3e5 f6e5 f5f6 a5a6 c2f2 a6c6 d5d3 f8h8 h2h3 c6e8 d3d5 e8d7 f2f1 h8g8 g1h2 g8h8 h2g1 h8g8 g1h2 g8h8 b1b2 b7b6 b2f2 b6b5 f2d2 b5c4 d5c4 b8b6 d2d3 h8b8 f1d1 b8d8 d3d2 d7e6 c4a4 e6f6 h2g1 f6e6 c3c4 e6g6 a4a7 c7c6 d1e1 d8b8 a7a4 c6c7 a4a7 b8b7 a7a5 g6e6 d2c2 c7d7 a5d2 f7f5 d2d5 e6d5 e4d5 b6b3 e1a1 b7b8 g1h2 f5f4 a3a4 b3b1 a1b1 b8b1 c2a2 b1b4 a4a5 d7c8 a5a6 c8b8 h3h4 b8a7 h2h3 b4c4 h4h5 c4c3 h3g4 c3c1 g4f5 c1h1 f5e6 h1h2 a2a1 h2h5 e6d6 h5g5 d6c6 g5g2 d5d6 g2d2 d6d7 e5e4 a1a4 c5c4 a4c4 f4f3 c4e4 d2c2 c6d6 c2d2 d6e7 f3f2 e4f4 d2e2 e7d6 e2d2 d6e6 d2e2 e6d5 e2d2 d5e6 d2e2 e6d5 e2d2 d5c6 d2c2 c6b5 c2d2 d7d8q d2d8 f4f7 a7a8 f7f2 d8b8 b5a5 b8e8 f2f6 a8b8 a5b6 b8a8 f6c6 e8b8 b6a5 b8d8 a5b5 d8b8 c6b6 a8a7 b6b8 a7b8 b5b6 b8a8 b6c6 a8b8 c6c5 b8a7 c5d5 a7a6
         */

        String mmm = "c2c4 e7e5 b1c3 g8f6 g1f3 b8c6 e2e4 f8b4 d2d3 d7d6 f1e2 h7h6 e1g1 b4c3 b2c3 a7a5 c1e3 e8e7 d1c2 c8g4 a1b1 d8c8 d3d4 e5d4 f3d4 g4e2 c2e2 e7d7 f2f3 a5a4 d4f5 h8g8 e2d3 c6e5 d3e2 d7e6 a2a3 c7c5 b1b6 e5c6 f1d1 f6e8 f3f4 c8c7 d1b1 a8b8 e2f3 g8f8 f5g3 e8f6 f4f5 e6d7 f3e2 f8e8 e3f4 h6h5 e2d1 c6e5 f4g5 e8h8 g5f6 g7f6 g3h5 d7c8 d1e2 e5d7 b6b2 c7a5 b2c2 c8c7 h5f4 c7c6 e2d3 d7e5 d3d5 c6c7 f4d3 h8f8 d3e5 f6e5 f5f6 a5a6 c2f2 a6c6 d5d3 f8h8 h2h3 c6e8 d3d5 e8d7 f2f1 h8g8 g1h2 g8h8 h2g1 h8g8 g1h2 g8h8 b1b2 b7b6 b2f2 b6b5 f2d2 b5c4 d5c4 b8b6 d2d3 h8b8 f1d1 b8d8 d3d2 d7e6 c4a4 e6f6 h2g1 f6e6 c3c4 e6g6 a4a7 c7c6 d1e1 d8b8 a7a4 c6c7 a4a7 b8b7 a7a5 g6e6 d2c2 c7d7 a5d2 f7f5 d2d5 e6d5 e4d5 b6b3 e1a1 b7b8 g1h2 f5f4 a3a4 b3b1 a1b1 b8b1 c2a2 b1b4 a4a5 d7c8 a5a6 c8b8 h3h4 b8a7 h2h3 b4c4 h4h5 c4c3 h3g4 c3c1 g4f5 c1h1 f5e6 h1h2 a2a1 h2h5 e6d6 h5g5 d6c6 g5g2 d5d6 g2d2 d6d7 e5e4 a1a4 c5c4 a4c4 f4f3 c4e4 d2c2 c6d6 c2d2 d6e7 f3f2 e4f4 d2e2 e7d6 e2d2 d6e6 d2e2 e6d5 e2d2 d5e6 d2e2 e6d5 e2d2 d5c6 d2c2 c6b5 c2d2 d7d8q d2d8 f4f7 a7a8 f7f2 d8b8 b5a5 b8e8 f2f6 a8b8 a5b6 b8a8 f6c6 e8b8 b6a5 b8d8 a5b5 d8b8 c6b6 a8a7 b6b8 a7b8 b5b6 b8a8 b6c6 a8b8 c6c5";
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
        start.setMoveTime(5000L);


        try {
            uciEntry.receive(eac);
            uciEntry.receive(start);
        } catch (NoProtocolException ignored) {

        } catch (Exception e) {
            throw new AssertionError("crashed on endgame position");
        }

        System.out.println("endgame ok");
    }
}
