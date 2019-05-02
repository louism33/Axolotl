package com.github.louism33.axolotl.main;

import com.github.louism33.axolotl.search.Engine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class UCIDontCrashTest {

    @BeforeAll
    static void setup() {
        Engine.resetFull();

    }

    @AfterAll
    static void reset() {
        Engine.resetFull();

    }


//    @Test
//    void searchResetSearch()  {
//        UCIEntryOld uciEntry = new UCIEntryOld();
//        EngineBetter.resetFull();
//        EngineBetter.uciEntry = uciEntry;
//
//        for (int i = 0; i < 3; i++) {
//            String mmm = "d2d4";
//            String[] ms = mmm.split(" ");
//
//            GenericBoard genericBoard = new GenericBoard(518);
//
//            List<GenericMove> moves = new ArrayList<>();
//            for (String m : ms) {
//                moves.add(new GenericMove(m));
//            }
//
//            EngineAnalyzeCommand eac = new EngineAnalyzeCommand(genericBoard, moves);
//
//            EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
//            start.setDepth(5);
//            EngineNewGameCommand engineNewGameCommand = new EngineNewGameCommand();
//
//            try {
//                uciEntry.receive(eac);
//                uciEntry.receive(start);
//            } catch (NoProtocolException ignored) {
//                List<GenericMove> genericMoves = PVLine.retrievePV(uciEntry.board);
//                
//                uciEntry.board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(uciEntry.board, 
//                        genericMoves.get(0).toString()));
//                
//            } catch (Exception | Error e) {
//                throw new AssertionError("crashed");
//            }
//            uciEntry.receive(engineNewGameCommand);
//        }
//
//        System.out.println("didn't crash");
//    }

}
