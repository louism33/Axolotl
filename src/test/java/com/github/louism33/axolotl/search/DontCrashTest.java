//package com.github.louism33.axolotl.search;
//
//import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
//import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
//import com.fluxchess.jcpi.models.GenericBoard;
//import com.fluxchess.jcpi.models.GenericColor;
//import com.fluxchess.jcpi.models.GenericMove;
//import com.fluxchess.jcpi.models.IllegalNotationException;
//import com.fluxchess.jcpi.protocols.NoProtocolException;
//import com.github.louism33.axolotl.main.PVLine;
//import com.github.louism33.axolotl.main.UCIEntryOld;
//import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
//import com.github.louism33.axolotl.util.Util;
//import com.github.louism33.chesscore.Chessboard;
//import com.github.louism33.chesscore.MoveParser;
//import com.github.louism33.utils.MoveParserFromAN;
//import com.github.louism33.utils.PGNParser;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SuppressWarnings("ALL")
//public class DontCrashTest {
//    @BeforeAll
//    static void setup() {
//        Util.reset();
//    }
//
//    @AfterAll
//    static void reset() {
//        Util.reset();
//    }
//    
//    @Test
//    void searchMovesTest() throws IllegalNotationException {
//        String mmm = "d2d4";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setMoveTime(5000L);
//
//        List<GenericMove> searchmoves = new ArrayList<>();
//        searchmoves.add(new GenericMove("d7d5"));
//        searchmoves.add(new GenericMove("a7a5"));
//        start.setSearchMoveList(searchmoves);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on search moves");
//        }
//
//        System.out.println("search moves ok");
//    }
//
//    @Test
//    void movesToGoTest() throws IllegalNotationException {
//        String mmm = "d2d4";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setMoveTime(5000L);
//        start.setMovesToGo(12);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on moves to go");
//        }
//
//        System.out.println("moves to go ok");
//    }
//
//
//    @Test
//    void ttStressTest() throws IllegalNotationException {
//        String mmm = "e2e4 ";
//        GenericBoard board = new GenericBoard(518);
//        EngineBetter.resetFull();
//        int max = 20;
//        int d = 10;
//        UCIEntryOld uciEntry = new UCIEntryOld();
//        EngineBetter.uciEntry = uciEntry;
//        for (int i = 0; i < max; i++) {
//            TranspositionTable.agedOut = 0;
//            String[] ms = mmm.split(" ");
//
//            List<GenericMove> moves = new ArrayList<>();
//            for (String m : ms) {
//                moves.add(new GenericMove(m));
//            }
//
//            EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//            EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//            start.setDepth(d);
//
//            try {
//                uciEntry.receive(eac);
//                uciEntry.receive(start);
//            } catch (NoProtocolException ignored) {
//                List<GenericMove> genericMoves = PVLine.retrievePV(uciEntry.board);
//                String s = MoveParser.toString(EngineBetter.getAiMove());
//                mmm += genericMoves.get(0) + " " + genericMoves.get(1) + " ";
//
//            } catch (Exception | Error e) {
//                throw new AssertionError("crashed on tt stress test");
//            }
//
//        }
//
//        System.out.println("tt stress test ok");
//    }
//
//    @Test
//    void depthTest() throws IllegalNotationException {
//        String mmm = "d2d4";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setDepth(7);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on depth searched");
//        }
//
//        System.out.println("depth ok");
//    }
//
//    @Test
//    void clockTest() throws IllegalNotationException {
//        String mmm = "d2d4";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setClock(GenericColor.BLACK, 2000L);
//        start.setClock(GenericColor.WHITE, 2000L);
//        start.setClockIncrement(GenericColor.BLACK, 1000L);
//        start.setClockIncrement(GenericColor.WHITE, 1000L);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on clock");
//        }
//
//        System.out.println("clock ok");
//    }
//
//    @Test
//    void tirsaTest() throws IllegalNotationException {
//        /*
//        position startpos moves d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 c5d4 d1a4 c8d7 a4d4 e6d5 g1f3 b8c6 d4d1 f8b4 e2e3 g8f6
//         */
//
//        String mmm = "d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 c5d4 d1a4 c8d7 a4d4 e6d5 g1f3 b8c6 d4d1 f8b4 e2e3 g8f6";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setMoveTime(5000L);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on tirsa's position");
//        }
//
//        System.out.println("tirsa ok");
//    }
//
//
//
//    @Test
//    void endgameTest() throws IllegalNotationException {
//
//        String mmm = "c2c4 e7e5 b1c3 g8f6 g1f3 b8c6 e2e4 f8b4 d2d3 d7d6 f1e2 h7h6 e1g1 b4c3 b2c3 a7a5 c1e3 e8e7 d1c2 c8g4 a1b1 d8c8 d3d4 e5d4 f3d4 g4e2 c2e2 e7d7 f2f3 a5a4 d4f5 h8g8 e2d3 c6e5 d3e2 d7e6 a2a3 c7c5 b1b6 e5c6 f1d1 f6e8 f3f4 c8c7 d1b1 a8b8 e2f3 g8f8 f5g3 e8f6 f4f5 e6d7 f3e2 f8e8 e3f4 h6h5 e2d1 c6e5 f4g5 e8h8 g5f6 g7f6 g3h5 d7c8 d1e2 e5d7 b6b2 c7a5 b2c2 c8c7 h5f4 c7c6 e2d3 d7e5 d3d5 c6c7 f4d3 h8f8 d3e5 f6e5 f5f6 a5a6 c2f2 a6c6 d5d3 f8h8 h2h3 c6e8 d3d5 e8d7 f2f1 h8g8 g1h2 g8h8 h2g1 h8g8 g1h2 g8h8 b1b2 b7b6 b2f2 b6b5 f2d2 b5c4 d5c4 b8b6 d2d3 h8b8 f1d1 b8d8 d3d2 d7e6 c4a4 e6f6 h2g1 f6e6 c3c4 e6g6 a4a7 c7c6 d1e1 d8b8 a7a4 c6c7 a4a7 b8b7 a7a5 g6e6 d2c2 c7d7 a5d2 f7f5 d2d5 e6d5 e4d5 b6b3 e1a1 b7b8 g1h2 f5f4 a3a4 b3b1 a1b1 b8b1 c2a2 b1b4 a4a5 d7c8 a5a6 c8b8 h3h4 b8a7 h2h3 b4c4 h4h5 c4c3 h3g4 c3c1 g4f5 c1h1 f5e6 h1h2 a2a1 h2h5 e6d6 h5g5 d6c6 g5g2 d5d6 g2d2 d6d7 e5e4 a1a4 c5c4 a4c4 f4f3 c4e4 d2c2 c6d6 c2d2 d6e7 f3f2 e4f4 d2e2 e7d6 e2d2 d6e6 d2e2 e6d5 e2d2 d5e6 d2e2 e6d5 e2d2 d5c6 d2c2 c6b5 c2d2 d7d8q d2d8 f4f7 a7a8 f7f2 d8b8 b5a5 b8e8 f2f6 a8b8 a5b6 b8a8 f6c6 e8b8 b6a5 b8d8 a5b5 d8b8 c6b6 a8a7 b6b8 a7b8 b5b6 b8a8 b6c6 a8b8 c6c5";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setMoveTime(5000L);
//
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on endgame position");
//        }
//
//        System.out.println("endgame ok");
//    } 
//    
//    @Test
//    void extremeEndgameTest() throws IllegalNotationException {
//
//        String mmm = "d2d4 d7d5 c1f4 e7e6 e2e3 f8d6 d1f3 g8f6 b1c3 e8g8 f1d3 c7c5 d4c5 d6c5 g1e2 b8c6 f4g5 c8d7 e1g1 c5e7 f3f4 e6e5 f4h4 e5e4 d3b5 c6e5 b5d7 d8d7 h4g3 e7d6 f2f4 e4f3 g5f6 e5g4 g3f3 g4f6 a1d1 f8d8 e2f4 d6f4 f3f4 d7e7 f4d4 a7a5 f1f4 a8c8 d4d3 d8e8 d1e1 e7e6 e1e2 e6c6 e2f2 e8e5 f4f5 c8e8 f2f3 e5f5 f3f5 e8d8 d3d4 d8d6 f5e5 b7b6 e5f5 d6d7 a2a3 h7h6 b2b4 c6e6 f5e5 e6c6 h2h3 d7d6 b4b5 c6d7 g2g4 d7d8 g1g2 d6d7 g4g5 h6g5 e5g5 d7d6 h3h4 d8e7 g2f2 e7e6 h4h5 g8h7 f2e2 e6d7 a3a4 h7h6 g5e5 d6e6 e5f5 e6d6 d4f4 h6h7 h5h6 d5d4 c3e4 f6e4 f4e4 d7e6 f5e5 e6g6 h6g7 g6e4 e5e4 d4e3 e4g4 h7g8 e2e3 d6h6 e3d4 h6h2 c2c3 h2a2 d4d5 f7f5 g4c4 g8g7 d5c6 g7f6 c6b6 f6e5 b6a5 a2g2 b5b6 g2b2 c4b4 b2d2 b6b7 d2d8 b7b8q d8b8 b4b8 e5e6 b8b7 f5f4 c3c4 e6e5 b7f7 e5e4 a5b6 e4e5 a4a5 e5e6 f7f8 e6e7 f8f4 e7e6 a5a6 e6e5 f4f8 e5d4 b6b5 d4e4 a6a7 e4d3 f8f3 d3e2 a7a8q e2d1 f3f1 d1d2 a8e4 d2c3 e4a8 c3d2 f1f4 d2c2 f4f8 c2d3 f8f3 d3e2 b5b4 e2d1 b4b5 d1e2 f3f4 e2d2 b5b4 d2d3 b4b5";
//        
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setClock(GenericColor.BLACK, 86000L);
//        start.setClock(GenericColor.WHITE, 20000L);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on endgame position");
//        }
//
//        System.out.println("extreme endgame ok");
//    }
//    
//    @Test
//    void previousCrashTest() throws IllegalNotationException {
//
//        String mmm = "d2d4 g8f6 c2c4 g7g6 b1c3 d7d6 e2e4 f8g7 g1f3 e8g8 f1d3 e7e5 e1g1 c8g4 d4d5 b8a6 c1g5 a6c5 b2b4 c5d3 d1d3 h7h6 g5f6 d8f6 f3d2 g6g5 a2a4 f6e7 f2f3 g4d7 b4b5 b7b6 d3c2 e7f6 d2b3 f6f4 c2e2 h6h5 b3c1 f4f6 c1d3 h5h4 e2b2 h4h3 g2g4 a7a5 b2e2 f8e8 d3f2 f6h6 c3d1 a8b8 d1e3 g7f6 e2d3 e8f8 f1d1 g8g7 a1a2 f8h8 d3b3 g7g8 a2c2 f6e7 d1f1 h6f6 f2h1 b8e8 h1g3 f6g6 b3c3 g8h7 c3d3 h7g8 f1d1 g6f6 d3c3 g8h7 c2f2 e8a8 c3d3 f6h6 d3f1 a8b8 g3h5 b8d8 f1h3 h7g8 h3g2 h6g6 f2c2 d8e8 g2f1 e8a8 f1e2 e7f6 e2d3 g8h7 d3b3 f6e7 c2d2 h7g8 h5g3 a8d8 d1e1 e7f6 d2c2 d8a8 b3d3 g8h7 c2e2 h7g7 g1h1 g7g8 e2b2 a8d8 e1c1 f6g7 h1g1 g7f6 b2a2 f6g7 c1f1 g7f6 d3c3 d8b8 a2e2 b8d8 f1c1 d8e8 c3b3 e8d8 e2d2 g8h7 b3d3 h7g8 g3h5 d8a8 d2e2 f6g7 e2c2";
//        
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setMoveTime(5000L);
////        start.setMoveTime(25000L);
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on previous position");
//        }
//
//        System.out.println("crash test ok");
//    }
//
//
//    @Test
//    void superLongGameTest() throws IllegalNotationException {
//        String mmm = "c2c4 e7e5 b1c3 g8f6 g1f3 b8c6 e2e4 f8b4 d2d3 d7d6 f1e2 h7h6 e1g1 b4c3 b2c3 a7a5 c1e3 e8e7 d1c2 c8g4 a1b1 d8c8 d3d4 e5d4 f3d4 g4e2 c2e2 e7d7 f2f3 a5a4 d4f5 h8g8 e2d3 c6e5 d3e2 d7e6 a2a3 c7c5 b1b6 e5c6 f1d1 f6e8 f3f4 c8c7 d1b1 a8b8 e2f3 g8f8 f5g3 e8f6 f4f5 e6d7 f3e2 f8e8 e3f4 h6h5 e2d1 c6e5 f4g5 e8h8 g5f6 g7f6 g3h5 d7c8 d1e2 e5d7 b6b2 c7a5 b2c2 c8c7 h5f4 c7c6 e2d3 d7e5 d3d5 c6c7 f4d3 h8f8 d3e5 f6e5 f5f6 a5a6 c2f2 a6c6 d5d3 f8h8 h2h3 c6e8 d3d5 e8d7 f2f1 h8g8 g1h2 g8h8 h2g1 h8g8 g1h2 g8h8 b1b2 b7b6 b2f2 b6b5 f2d2 b5c4 d5c4 b8b6 d2d3 h8b8 f1d1 b8d8 d3d2 d7e6 c4a4 e6f6 h2g1 f6e6 c3c4 e6g6 a4a7 c7c6 d1e1 d8b8 a7a4 c6c7 a4a7 b8b7 a7a5 g6e6 d2c2 c7d7 a5d2 f7f5 d2d5 e6d5 e4d5 b6b3 e1a1 b7b8 g1h2 f5f4 a3a4 b3b1 a1b1 b8b1 c2a2 b1b4 a4a5 d7c8 a5a6 c8b8 h3h4 b8a7 h2h3 b4c4 h4h5 c4c3 h3g4 c3c1 g4f5 c1h1 f5e6 h1h2 a2a1 h2h5 e6d6 h5g5 d6c6 g5g2 d5d6 g2d2 d6d7 e5e4 a1a4 c5c4 a4c4 f4f3 c4e4 d2c2 c6d6 c2d2 d6e7 f3f2 e4f4 d2e2 e7d6 e2d2 d6e6 d2e2 e6d5 e2d2 d5e6 d2e2 e6d5 e2d2 d5c6 d2c2 c6b5 c2d2 d7d8q d2d8 f4f7 a7a8 f7f2 d8b8 b5a5 b8e8 f2f6 a8b8 a5b6 b8a8 f6c6 e8b8 b6a5 b8d8 a5b5 d8b8 c6b6 a8a7 b6b8 a7b8 b5b6 b8a8 b6c6 a8b8 c6c5";
//        String[] ms = mmm.split(" ");
//
//        GenericBoard board = new GenericBoard(518);
//
//        List<GenericMove> moves = new ArrayList<>();
//        for (String m : ms) {
//            moves.add(new GenericMove(m));
//        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(board, moves);
//
//        UCIEntryOld uciEntry = new UCIEntryOld();
//
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setMoveTime(5000L);
//
//
//        try {
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//
//        } catch (Exception | Error e) {
//            throw new AssertionError("crashed on endgame position");
//        }
//
//        System.out.println("endgame ok");
//    }
//
//    @Test
//    void crashedTest() {
//        String pgn = "" +
//                "1. e4 {book} e6 {book} 2. d4 {0.70s} d5 {0.95s} 3. e5 {0.69s} c5 {0.93s}\n" +
//                "4. Nf3 {0.67s} cxd4 {0.91s} 5. Nxd4 {0.66s} Bc5 {0.90s} 6. Nb3 {0.65s}\n" +
//                "Bb6 {0.88s} 7. Nc3 {0.63s} Nc6 {0.86s} 8. Bb5 {0.62s} Ne7 {0.85s}\n" +
//                "9. Bxc6+ {0.61s} Nxc6 {0.83s} 10. O-O {0.59s} Nxe5 {0.81s} 11. Bf4 {0.58s}\n" +
//                "Ng6 {0.80s} 12. Be3 {0.57s} O-O {0.78s} 13. a4 {0.56s} Bd7 {0.77s}\n" +
//                "14. Qd3 {0.55s} Qc7 {0.76s} 15. Bxb6 {0.54s} axb6 {0.74s} 16. Qe3 {0.53s}\n" +
//                "Qd6 {0.72s} 17. f4 {0.52s} Rfc8 {0.71s} 18. Nd4 {0.51s} Qb4 {0.70s} 19. f5\n" +
//                "exf5 {0.69s} 20. Nxd5 Qd6 {0.67s} 21. Nc3 Re8 {0.66s} 22. Qf2 f4 {0.65s}\n" +
//                "23. Nde2 Bf5 {0.64s} 24. Nd4 Bg4 {0.62s} 25. Ndb5 Qc6 {0.61s} 26. Rfe1\n" +
//                "Red8 {0.60s} 27. Ne4 Kh8 {0.59s} 28. Nd4 Qc4 {0.58s} 29. Nb5 Rd7 {0.56s}\n" +
//                "30. Qxb6 Qxc2 {0.56s} 31. Nf2 Bh5 {0.54s} 32. Nd6 Qc7 {0.54s} 33. Qxc7\n" +
//                "Rxc7 {0.52s} 34. Re8+ Rxe8 {0.51s} 35. Nxe8 Rc6 {0.51s} 36. b4 Ne5 37. b5 Re6\n" +
//                "38. Nc7 Rd6 39. a5 Rg6 40. Nd5 f3 {1.5s} 41. g3 {1.1s} Rd6 {1.5s} 42. a6 {1.1s}\n" +
//                "bxa6 {1.5s} 43. bxa6 {1.1s} Nc6 {1.4s} 44. Nf4 {1.1s} g6 {1.4s} 45. a7 {1.0s}\n" +
//                "Nxa7 {1.4s} 46. Rxa7 {1.0s} Kg8 {1.4s} 47. Ne4 {0.99s} Rb6 {1.3s}\n" +
//                "48. Nxh5 {0.97s} gxh5 {1.3s} 49. Ng5 {0.95s} Kg7 {1.3s} 50. Nxf7 {0.93s}\n" +
//                "Kf6 {1.3s} 51. h4 {0.91s} Rb2 {1.2s} 52. Ng5 {0.90s} Kf5 {1.2s} 53. Nxf3 {0.88s}\n" +
//                "Kg4 {1.2s} 54. Nd4 {0.86s} Kxg3 {1.2s} 55. Rg7+ {0.84s} Kf4 {1.1s}\n" +
//                "56. Rxh7 {0.83s} Ke4 {1.1s} 57. Nc6 {0.81s} Rb6 {1.1s} 58. Rh6 {0.79s}\n" +
//                "Ke3 {1.1s} 59. Kf1 {0.78s} Rb1+ {1.1s} 60. Kg2 Rb5 {1.1s} 61. Re6+ {0.76s}\n" +
//                "Kf4 {1.0s} 62. Nd4 {0.75s} Rb4 {1.0s} 63. Rf6+ {0.73s} Ke4 {0.99s}\n" +
//                "64. Nf3 {0.72s} Ke3 {0.97s} 65. Re6+ {0.70s} Kf4 {0.95s} 66. Re2 {0.69s}\n" +
//                "Rc4 {0.94s} 67. Kf2 {0.68s} Rc3 {0.92s} 68. Ng5 {0.66s} Kg4 {0.90s}\n" +
//                "69. Re4+ {0.65s} Kf5 70. Rb4 {0.64s} Rd3 {0.87s} 71. Ne4 {0.62s} Rd5 {0.85s}\n" +
//                "72. Ng3+ {0.61s} Ke6 {0.83s} 73. Rb6+ {0.60s} Ke5 {0.82s} 74. Nxh5 {0.59s}\n" +
//                "Rd4 {0.80s} 75. Rb5+ {0.57s} Kd6 {0.79s} 76. Kg3 {0.56s} Kc6 {0.77s}\n" +
//                "77. Rb2 {0.55s} Kd5 {0.76s} 78. Nf4+ {0.54s} Ke4 {0.74s} 79. h5 {0.53s}\n" +
//                "Ra4 {0.73s} 80. h6 {0.52s} Ra7 {1.8s} 81. Rb4+ {1.3s} Kf5 {1.7s} 82. Rb5+ {1.3s}\n" +
//                "Ke4 {1.7s} 83. Rh5 {1.3s} Rh7 {1.7s} 84. Ne6 {1.2s} Ke3 {1.6s} 85. Ng5 {1.2s}\n" +
//                "Rh8 {1.6s} 86. h7 {1.2s} Kd3 {1.6s} 87. Nf7 {1.2s} Rxh7 {1.6s} 88. Rxh7 {1.1s}\n" +
//                "Ke4 {1.5s} 89. Rh5 {1.1s} Ke3 {1.5s} 90. Re5+ {1.1s} Kd3 {1.5s} 91. Kf4 {1.1s}\n" +
//                "Kd4 {1.4s} 92. Rh5 {1.0s} Kc3 {1.4s} 93. Rd5 {1.0s} Kc4 {1.4s} 94. Ke4 {1.0s}\n" +
//                "Kb3 {1.4s} 95. Kd3 Kb4 {1.4s} 96. Ne5 Kb3 {0.68s} 97. Rd4 Kb2 {1.4s} 98. Rd5\n" +
//                "Kb3 {1.4s} 99. Rd4" +
//                "";
//
//        List<String> pgns = new ArrayList<>();
//        pgns.add(pgn);
//        try{
//            for (int p = 0; p < pgns.size(); p++) {
//                List<String> s = PGNParser.parsePGN(pgns.get(p));
//
//                Chessboard board = new Chessboard();
//                for (int i = 0; i < s.size(); i++) {
//                    String move = s.get(i);
//
//                    move = move.trim();
//
//                    int move1 = 0;
//                    try {
//                        move1 = MoveParserFromAN.buildMoveFromANWithOO(board, move);
//                    } catch (Exception | Error e) {
//                        System.out.println(s);
//                        System.out.println(board);
//                        System.out.println(board.zobristHash);
//                        System.out.println(move);
//                        System.out.println(MoveParser.toString(move1));
//                        e.printStackTrace();
//                    }
//                    board.makeMoveAndFlipTurn(move1);
//                }
//            }
//        } catch (Exception | Error e) {
//            throw new AssertionError("failed on stress test");
//        }
//    }
//}
