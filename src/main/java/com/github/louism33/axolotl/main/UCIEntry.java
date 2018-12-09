package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.util.List;

import static com.github.louism33.axolotl.main.UCIBoardParser.convertGenericBoardToChessboard;
import static com.github.louism33.axolotl.main.UCIBoardParser.convertMyMoveToGenericMove;

public class UCIEntry extends AbstractEngine {

    private Chessboard board;
    private GenericBoard genericBoard;
    private List<GenericMove> moves;

    public UCIEntry(){
        super();
    }
    
    public UCIEntry(Engine engine){
        super();
    }

    // "uci"
    @Override
    public void receive(EngineInitializeRequestCommand command) {
        System.out.println("Starting Engine");
        Engine.setup(this);
        this.getProtocol().send(new ProtocolInitializeAnswerCommand("axolotl", "Louis James Mackenzie-Smith"));
    }

    @Override
    protected void quit() {
        System.out.println("Quitting");
        System.exit(1);
    }

    @Override
    public void receive(EngineSetOptionCommand command) {
        System.out.println("This is not possible yet");
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
        Engine.setup(this);
        System.out.println("New Game");
    }

    // ex:
    // position fen N7/P3pk1p/3p2p1/r4p2/8/4b2B/4P1KP/1R6 w - - 0 34
    @Override
    public void receive(EngineAnalyzeCommand command) {
        genericBoard = command.board;
        moves = command.moves;
//        System.out.println("The initial board fen is:\n"+ genericBoard +"\nWith moves: "+moves);
        board = convertGenericBoardToChessboard(genericBoard, moves);
    }

    // go movetime 30000
    @Override
    public void receive(EngineStartCalculatingCommand command) {
        if (command == null){
            return;
        }
        int aiMove = calculatingHelper(command);
        if (aiMove != 0){
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
    }

    private int calculatingHelper(EngineStartCalculatingCommand command) {
        long clock = timeOnClock(command);
        if (clock != 0){
            System.out.println("Search for move, clock time: " + clock);
            return Engine.searchMyTime(board, clock);
        }
        else if (command.getMoveTime() != 0){
            System.out.println("Search for move, fixed time: " + command.getMoveTime());
            return Engine.searchFixedTime(board, command.getMoveTime());
        }
        else {
            int searchDepth = Engine.MAX_DEPTH;
            if (command.getInfinite()){
                return Engine.searchFixedDepth(board, searchDepth);
            }
            else if (command.getDepth() != null){
                searchDepth = command.getDepth();
                System.out.println("Search for move, fixed depth: " + command.getDepth());
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
        System.out.println("Time to stop calculating, aiMove: " + aiMove);
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

    public void sendInformation(ProtocolInformationCommand protocolInformationCommand){
        this.getProtocol().send(protocolInformationCommand);
    }

    public static void main(String[] args) {

        Chessboard board = new Chessboard();
        System.out.println(board);
        int move = Engine.searchFixedDepth(board, 6);
        System.out.println(MoveParser.toString(move));
        
        System.out.println("Starting everything");
        // start the Engine
//        Thread thread = new Thread( new UCIEntry() );
//        thread.start();
    }

}
