package javacode.chessengine.main;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

import static javacode.chessengine.protocolhelperclasses.UCIBoardParser.convertGenericBoardToChessboard;
import static javacode.chessengine.protocolhelperclasses.UCIBoardParser.convertMyMoveToGenericMove;

public class UCIEntry extends AbstractEngine {

    private Engine engine;
    private Chessboard board;
    private GenericBoard genericBoard;
    private List<GenericMove> moves;

    private UCIEntry(){
        super();
    }

    // "uci"
    @Override
    public void receive(EngineInitializeRequestCommand command) {
        System.out.println("Starting Engine");
        this.getProtocol().send(new ProtocolInitializeAnswerCommand("Axolotl-v1.2", "Louis James Mackenzie-Smith"));
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
        engine = new Engine(this);
        moves = null;
        board = null;
        genericBoard = null;
        System.out.println("New Game");

    }

    // ex:
    // position fen N7/P3pk1p/3p2p1/r4p2/8/4b2B/4P1KP/1R6 w - - 0 34
    @Override
    public void receive(EngineAnalyzeCommand command) {
        this.engine = new Engine(this);
        genericBoard = command.board;
        moves = command.moves;
        System.out.println("The initial board fen is:\n"+ genericBoard +"\nWith moves: "+moves);
        board = convertGenericBoardToChessboard(genericBoard, moves);
    }

    // go movetime 30000
    @Override
    public void receive(EngineStartCalculatingCommand command) {
        Move aiMove = calculatingHelper(command);
        System.out.println(aiMove);
        if (aiMove != null){
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
    }

    private Move calculatingHelper(EngineStartCalculatingCommand command) {
        long clock = timeOnClock(command);
        Move aiMove;
        if (engine == null){
            engine = new Engine(this);
        }
        if (board == null) {
            board = convertGenericBoardToChessboard(genericBoard, null);
        }

        if (clock != 0){
            System.out.println("Search for move, clock time: " + clock);
            aiMove = engine.searchMyTime(board, clock);
        }
        else if (command.getMoveTime() != 0){
            System.out.println("Search for move, fixed time: " + command.getMoveTime());
            aiMove = engine.searchFixedTime(board, command.getMoveTime());
        }
        else {
            int searchDepth = engine.MAX_DEPTH;
            if (command.getInfinite()){
                return engine.searchFixedDepth(board, engine.MAX_DEPTH);
            }
            else if (command.getDepth() != null){
                searchDepth = command.getDepth();
            }
            System.out.println("Search for move, fixed depth: " + command.getDepth());
            aiMove = engine.searchFixedDepth(board, searchDepth);
        }
        return aiMove;
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
        Move aiMove = engine.getAiMove();
        System.out.println("Time to stop calculating, aiMove: " + aiMove);
        if (aiMove != null) {
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
        engine.setStopInstruction(true);
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        System.out.println("I don't know how to ponder :(");
    }

    public void sendInformation(ProtocolInformationCommand protocolInformationCommand){
        this.getProtocol().send(protocolInformationCommand);
    }

    public static void main(String[] args) {
        System.out.println("Starting everything");
        // start the Engine
        Thread thread = new Thread( new UCIEntry() );
        thread.start();
    }

}
