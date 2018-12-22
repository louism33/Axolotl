package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.options.AbstractOption;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;

import java.util.List;

import static com.github.louism33.axolotl.main.UCIBoardParser.*;

public class UCIEntry extends AbstractEngine {

    private Chessboard board;
    private GenericBoard genericBoard;
    private List<GenericMove> moves;

    private UCIEntry(){
        super();
    }

    public UCIEntry(Engine engine){
        super();
    }

    // "uci"
    @Override
    public void receive(EngineInitializeRequestCommand command) {
        Engine.setup();
        Engine.setUciEntry(this);

        ProtocolInitializeAnswerCommand firstCommand 
                = new ProtocolInitializeAnswerCommand("axolotl_v1.3", "Louis James Mackenzie-Smith");
        
        firstCommand.addOption(new AbstractOption("Log") {
            @Override
            protected String type() {
                return "check";
            }
        });

        firstCommand.addOption(new AbstractOption("PrintEval") {
            @Override
            protected String type() {
                return "check";
            }
        });

        firstCommand.addOption(new AbstractOption("HashSize") {
            @Override
            protected String type() {
                return "spin";
            }
        });

        firstCommand.addOption(new AbstractOption("Threads") {
            @Override
            protected String type() {
                return "spin";
            }
        });

        this.getProtocol().send(firstCommand);
    }

    @Override
    protected void quit() {
        System.out.println("Quitting");
        System.exit(1);
    }

    // setoption name Write Debug Log true
    @Override
    public void receive(EngineSetOptionCommand command) {
        if (command == null || command.name == null || command.name.equals("")){
            return;
        }
        if (command.name.equalsIgnoreCase("Log")){
            EngineSpecifications.INFO = Boolean.valueOf(command.value);
        }        
        
        if (command.name.equalsIgnoreCase("PrintEval")){
            EngineSpecifications.PRINT = Boolean.valueOf(command.value);
        }

        if (command.name.equalsIgnoreCase("HashSize")){
            int size = Integer.parseInt(command.value);
            int number = size * 62_500;
            if (number > 0 && number < EngineSpecifications.MAX_TABLE_SIZE) {
                EngineSpecifications.TABLE_SIZE = number;
            }
            if (number > EngineSpecifications.MAX_TABLE_SIZE){
                EngineSpecifications.TABLE_SIZE = EngineSpecifications.MAX_TABLE_SIZE;
            }
        }

        if (command.name.equalsIgnoreCase("Threads")){
            int threadNumber = Integer.parseInt(command.value);
            if (threadNumber < 1 || threadNumber > EngineSpecifications.MAX_THREADS){
                return;
            }
            EngineSpecifications.THREAD_NUMBER = threadNumber;
            Engine.setupThreads();
        }
    }

    @Override
    public void receive(EngineDebugCommand command) {
        System.out.println("I'm sorry, I cannot do that");
    }

    @Override
    public void receive(EngineReadyRequestCommand command) {
        this.getProtocol().send(new ProtocolReadyAnswerCommand(""));
    }

    @Override
    public void receive(EngineNewGameCommand command) {
        moves = null;
        board = null;
        genericBoard = null;
        Engine.setup();
    }

    // ex:
    // position fen N7/P3pk1p/3p2p1/r4p2/8/4b2B/4P1KP/1R6 w - - 0 34
    @Override
    public void receive(EngineAnalyzeCommand command) {
        genericBoard = command.board;
        moves = command.moves;
        board = convertGenericBoardToChessboard(genericBoard, moves);

        if (EngineSpecifications.PRINT) {
            System.out.println(board);
            Evaluator.printEval(board);
        }
    }

    // go movetime 30000
    @Override
    public void receive(EngineStartCalculatingCommand command) {
        if (command == null){
            return;
        }
        Engine.giveThreadsBoard(board);
        int aiMove = calculatingHelper(command);
        if (aiMove != 0){
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
    }

    private int calculatingHelper(EngineStartCalculatingCommand command) {
        long clock = timeOnClock(command);
        if (clock != 0){
            Long clockIncrement = command.getClockIncrement(convertMyColourToGenericColour(board.isWhiteTurn()));
            return Engine.searchMyTime(board, clock, clockIncrement);
        }
        else if (command.getMoveTime() != null && command.getMoveTime() != 0){
            return Engine.searchFixedTime(board, command.getMoveTime(), true);
        }
        else {
            int searchDepth = EngineSpecifications.MAX_DEPTH;
            if (command.getInfinite()){
                return Engine.searchFixedDepth(board, searchDepth);
            }
            else if (command.getDepth() != null){
                searchDepth = command.getDepth();
                return Engine.searchFixedDepth(board, searchDepth);
            }
            else {
                throw new RuntimeException("I do not know how I should search for a move");
            }
        }
    }

    private long timeOnClock(EngineStartCalculatingCommand command){
        long time = 0;
        try {
            time = command.getClock(genericBoard.getActiveColor());
        } catch (NullPointerException e){
            return time;
        }
        return time;
    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        int aiMove = Engine.getAiMove();
        if (aiMove != 0) {
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
        Engine.setStopInstruction(true);
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        System.out.println("I don't know how to ponder :(");
    }

    void sendInformation(ProtocolInformationCommand protocolInformationCommand){
        this.getProtocol().send(protocolInformationCommand);
    }

    public static void main(String[] args) {
        Thread thread = new Thread( new UCIEntry() );
        thread.start();
    }

}
