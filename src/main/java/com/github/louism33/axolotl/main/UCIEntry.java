package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.options.Options;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;

import java.util.List;

import static com.github.louism33.axolotl.main.UCIBoardParser.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.chesscore.BoardConstants.WHITE;

public class UCIEntry extends AbstractEngine {

    public Chessboard board = new Chessboard();
    public GenericBoard genericBoard;
    public List<GenericMove> moves;

    private int lastMoveMade = 0;

    public UCIEntry() {
        super();
    }

    public UCIEntry(EngineBetter engine) {
        super();
    }

    // "uci"
    @Override
    public void receive(EngineInitializeRequestCommand command) {
        EngineBetter.resetFull();
        EngineBetter.uciEntry = this;

        ProtocolInitializeAnswerCommand firstCommand
                = new ProtocolInitializeAnswerCommand("axolotl_v1.5", "Louis James Mackenzie-Smith");

        firstCommand.addOption(Options.newHashOption(DEFAULT_TABLE_SIZE_MB, MIN_TABLE_SIZE_MB, MAX_TABLE_SIZE_MB));

        lastMoveMade = 0;

        this.getProtocol().send(firstCommand);
    }

    @Override
    protected void quit() {
        System.exit(1);
    }

    // setoption name Hash value 2
    @Override
    public void receive(EngineSetOptionCommand command) {
        if (command == null || command.name == null || command.name.isEmpty()) {
            return;
        }

        if (command.name.equalsIgnoreCase("Hash")) {
            int size = Integer.parseInt(command.value);
            int number = size * TABLE_SIZE_PER_MB;
            if (number >= MIN_TABLE_SIZE && number <= MAX_TABLE_SIZE) {
                TABLE_SIZE = number;
            } else if (number > MAX_TABLE_SIZE) {
                TABLE_SIZE = MAX_TABLE_SIZE;
            } else {
                TABLE_SIZE = MIN_TABLE_SIZE;
            }

            TranspositionTable.initTable(TABLE_SIZE);
        }
    }

    //debug on
    @Override
    public void receive(EngineDebugCommand command) {
        if (command != null) {
            if (command.toggle) {
                DEBUG = !DEBUG;
            } else {
                DEBUG = command.debug;
            }
        }
    }
    
    @Override
    public void receive(EngineReadyRequestCommand command) {
        this.getProtocol().send(new ProtocolReadyAnswerCommand(""));
    }

    @Override
    public void receive(EngineNewGameCommand command) {
        moves = null;
        board = new Chessboard();
        genericBoard = null;
        lastMoveMade = 0;
        EngineBetter.resetFull();
    }

    // position fen N7/P3pk1p/3p2p1/r4p2/8/4b2B/4P1KP/1R6 w - - 0 34
    @Override
    public void receive(EngineAnalyzeCommand command) {
        genericBoard = command.board;
        moves = command.moves;
        lastMoveMade = convertGenericBoardToChessboard(board, genericBoard, moves, lastMoveMade);
    }

    // go movetime 30000
    @Override
    public void receive(EngineStartCalculatingCommand command) {
        if (command == null) {
            return;
        }
        int aiMove = calculatingHelper(command);
        EngineBetter.quitOnSingleMove = true;
        EngineBetter.computeMoves = true;
        MAX_DEPTH = ABSOLUTE_MAX_DEPTH;
        if (aiMove != 0) {
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
    }

    private int calculatingHelper(EngineStartCalculatingCommand command) {
        MAX_DEPTH = ABSOLUTE_MAX_DEPTH;

        List<GenericMove> searchMoveList = command.getSearchMoveList();
        EngineBetter.quitOnSingleMove = true;
        if (searchMoveList != null) {
            EngineBetter.quitOnSingleMove = false;
            EngineBetter.computeMoves = false;
            EngineBetter.rootMoves = UCIBoardParser.convertGenericMovesToMyMoves(board, searchMoveList);
        }

        long clock = timeOnClock(command, false);
        if (clock != 0) {
            Long clockIncrement = command.getClockIncrement(convertMyColourToGenericColour(board.isWhiteTurn()));
            return EngineBetter.searchMyTime(board, clock, timeOnClock(command, true),
                    clockIncrement == null ? 0 : clockIncrement,
                    command.getMovesToGo());
        } else if (command.getMoveTime() != null && command.getMoveTime() != 0) {
            return EngineBetter.searchFixedTime(board, command.getMoveTime());
        } else {
            int searchDepth = MAX_DEPTH;
            if (command.getInfinite()) {
                return EngineBetter.searchFixedDepth(board, searchDepth);
            } else if (command.getDepth() != null) {
                searchDepth = command.getDepth();
                return EngineBetter.searchFixedDepth(board, searchDepth);
            } else {
                throw new RuntimeException("I do not know how I should search for a move");
            }
        }
    }

    private long timeOnClock(EngineStartCalculatingCommand command, boolean enemy) {
        long time = 0;
        try {
            if (enemy) {
                time = command.getClock(board.turn != WHITE ? GenericColor.WHITE : GenericColor.BLACK);
            } else {
                time = command.getClock(board.turn == WHITE ? GenericColor.WHITE : GenericColor.BLACK);
            }
        } catch (NullPointerException e) {
            return time;
        }
        return time;
    }

    private long timeOnClockEnemy(EngineStartCalculatingCommand command) {
        long time = 0;
        try {
            time = command.getClock(genericBoard.getActiveColor().opposite());
        } catch (NullPointerException e) {
            return time;
        }
        return time;
    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        int aiMove = EngineBetter.getAiMove();
        if (aiMove != 0) {
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
        EngineBetter.stopNow = true;
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        System.out.println("I don't know how to ponder :(");
    }

    void sendInformation(ProtocolInformationCommand protocolInformationCommand) {
        this.getProtocol().send(protocolInformationCommand);
    }

    public static void main(String[] args) {
        System.out.println("axolotl v1.5 by Louis James Mackenzie-Smith");
        Thread thread = new Thread(new UCIEntry());
        thread.start();
    }
}
