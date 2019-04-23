package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.Engine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class TranspositionTable3Test {

    @BeforeAll
    static void setup() {
        Engine.resetFull();
        
    }

    @AfterAll
    static void reset() {
        Engine.resetFull();
        
    }

//    @Test
//    void depthTest()  {
//        UCIEntryOld uciEntry = new UCIEntryOld();
//        EngineSpecifications.PRINT_PV = false;
//        
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        String mmm = "";
//        String[] ms = mmm.split(" ");
//        GenericBoard genericBoard = new GenericBoard(518);
//        List<GenericMove> moves = new ArrayList<>();
////        for (String m : ms) {
////            moves.add(new GenericMove(m));
////        }
//
//        EngineAnalyzeCommand eac = new EngineAnalyzeCommand(genericBoard, moves);
//
//        EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//        start.setDepth(10);
//
//        try {
//           
//            
//            uciEntry.receive(eac);
//            uciEntry.receive(start);
//        } catch (NoProtocolException ignored) {
//            int aiMove = EngineBetter.rootMoves[0];
//            Chessboard board = uciEntry.board;
//            List<GenericMove> genericMoves = PVLine.retrievePV(board);
//
//            board.makeMoveAndFlipTurn(buildMoveFromLAN(board, genericMoves.get(0).toString()));
//
//            board.makeMoveAndFlipTurn(buildMoveFromLAN(board, genericMoves.get(1).toString()));
//
//            resetBetweenMoves();
//
//            Assert.assertTrue(TranspositionTable.getPawnData(board.zobristHash) != 0);
//            resetFull();
//            Assert.assertEquals(0, TranspositionTable.getPawnData(board.zobristHash));
//
//        } catch (Exception e) {
//            throw new AssertionError("crashed on depth searched");
//        }
//
//        resetFull();
//        System.out.println("persisting ok");
//    }
}
