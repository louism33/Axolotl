package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.protocols.NoProtocolException;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UCIDontCrash {

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
    void searchResetSearch() throws IllegalNotationException {
        UCIEntry uciEntry = new UCIEntry();
        EngineBetter.resetFull();
        EngineBetter.uciEntry = uciEntry;

        for (int i = 0; i < 3; i++) {
            String mmm = "d2d4";
            String[] ms = mmm.split(" ");

            GenericBoard genericBoard = new GenericBoard(518);

            List<GenericMove> moves = new ArrayList<>();
            for (String m : ms) {
                moves.add(new GenericMove(m));
            }

            EngineAnalyzeCommand eac = new EngineAnalyzeCommand(genericBoard, moves);

            EngineStartCalculatingCommand start = new EngineStartCalculatingCommand();
            start.setDepth(5);
            EngineNewGameCommand engineNewGameCommand = new EngineNewGameCommand();

            try {
                uciEntry.receive(eac);
                uciEntry.receive(start);
            } catch (NoProtocolException ignored) {
                List<GenericMove> genericMoves = PVLine.retrievePV(uciEntry.board);
                
                uciEntry.board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(uciEntry.board, 
                        genericMoves.get(0).toString()));
                
            } catch (Exception | Error e) {
                throw new AssertionError("crashed");
            }
            uciEntry.receive(engineNewGameCommand);
        }

        System.out.println("didn't crash");
    }

}
