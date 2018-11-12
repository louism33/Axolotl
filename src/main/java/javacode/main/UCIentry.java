package javacode.main;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import javacode.chessengine.Engine;
import javacode.chessengine.PVLine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

import static javacode.main.UCIBoardParser.*;

public class UCIentry extends AbstractEngine {

    private Engine engine;
    private Chessboard board;
    private GenericBoard genericBoard;
    private List<GenericMove> moves;
    GenericMove bestMove;

    private UCIentry(){
        super();
    }

    // "uci"
    @Override
    public void receive(EngineInitializeRequestCommand command) {
        System.out.println("Starting Engine");
        this.getProtocol().send(new ProtocolInitializeAnswerCommand("Engine", "Louis James Mackenzie-Smith"));
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
        engine = new Engine();
        moves = null;
        board = null;
        genericBoard = null;
        System.out.println("New Game");
    }

    // ex:
    // position fen N7/P3pk1p/3p2p1/r4p2/8/4b2B/4P1KP/1R6 w - - 0 34
    @Override
    public void receive(EngineAnalyzeCommand command) {
        this.engine = new Engine();
        genericBoard = command.board;
        moves = command.moves;
        System.out.println("The board fen is:\n"+ genericBoard +"\nWith moves: "+moves);

        if (board != null){
            board = convertGenericBoardToChessboardDelta(board, moves);
        }
        
        board = convertGenericBoardToChessboard(genericBoard, moves);
    }

    // go movetime 30000
    @Override
    public void receive(EngineStartCalculatingCommand command) {
        calculatingHelper(command);

        Move aiMove = engine.bestMove();

        final PVLine pv = engine.getPV(board);
        final int score = pv.getScore();
        final List<Move> pvMoves = pv.getPvMoves();

        System.out.println(aiMove);
        
        if (aiMove != null){
            this.getProtocol().send(
                    new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
        }
    }

    
    private void calculatingHelper(EngineStartCalculatingCommand command) {
        long clock = timeOnClock(command);

        if (board == null) {
            board = convertGenericBoardToChessboard(genericBoard, null);
        }

        if (clock != 0){
            System.out.println("Search for move, clock time: " + clock);
            engine.searchMyTime(board, clock);
        }
        else if (command.getMoveTime() != 0){
            System.out.println("Search for move, fixed time: " + command.getMoveTime());
            engine.searchFixedTime(board, command.getMoveTime());
        }
        else {
            int searchDepth = engine.MAX_DEPTH;
            if (command.getInfinite()){

            }
            else if (command.getDepth() != null){
                searchDepth = command.getDepth();
            }
            System.out.println("Search for move, fixed depth: " + command.getDepth());
            engine.searchFixedDepth(board, searchDepth);
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
        Move aiMove = engine.getAiMove();
        this.getProtocol().send(new ProtocolBestMoveCommand(convertMyMoveToGenericMove(aiMove), null));
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        System.out.println("I don't know how to ponder :(");
    }

    public static void main(String[] args) {
        System.out.println("Starting everything");
        // start the Engine
        Thread thread = new Thread( new UCIentry() );
        thread.start();
    }

}
