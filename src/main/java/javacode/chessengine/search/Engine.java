package javacode.chessengine.search;

import javacode.chessengine.main.UCIEntry;
import javacode.chessengine.protocolhelperclasses.PVLine;
import javacode.chessengine.timemanagement.TimeAllocator;
import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessengine.utilities.Statistics;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import org.junit.Assert;

import java.util.List;
import java.util.Random;

public class Engine {

    private UCIEntry uciEntry;
    private EngineSpecifications engineSpecifications = new EngineSpecifications();
    
    Chessboard board;
    public Statistics statistics;
    private IterativeDeepeningDFS iterativeDeepeningDFS;
    private ZobristHash zobristHash;
    private TimeAllocator timeAllocator = new TimeAllocator();

    private boolean setup = false;

    public boolean HEAVY_DEBUG = false;
    
    public boolean INFO_LOG = true;
    private boolean HEAVY_INFO_LOG = false;

    /*
    max depth only works is time limit is false
     */
    public int MAX_DEPTH = 10;

    
    long PLY_STOP_TIME;
    
    private boolean stopInstruction;
    
    private void setup(){
        statistics = new Statistics(this);
        iterativeDeepeningDFS = new IterativeDeepeningDFS(this);
        stopInstruction = false;
        setup = true;
    }

    public void resetEngine(){
        statistics = new Statistics(this);
        stopInstruction = false;
    }

    public Move stop(){
        return iterativeDeepeningDFS.getAiMove();
    }

    public Move bestMove(){
        return iterativeDeepeningDFS.getAiMove();
    }

    public Move searchFixedDepth(Chessboard board, int depth){
        engineSpecifications.ALLOW_TIME_LIMIT = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0);
    }

    public Move searchMyTime (Chessboard board, long maxTime){
        engineSpecifications.ALLOW_TIME_LIMIT = true;
        
        if (maxTime < 1000){
            return searchFixedDepth(board, 1);
        }
        if (maxTime < 5000){
            return searchFixedDepth(board, 2);
        }
        long timeLimit = timeAllocator.allocateTime(board, maxTime);
        return searchFixedTime(board, timeLimit);
    }

    public Move searchFixedTime(Chessboard board, long maxTime){
        if (!setup){
            setup();
        }

        /*
        create hash value of the board, used for lookup in transposition table
         */
        zobristHash = new ZobristHash(board);

        long startTime = System.currentTimeMillis();
        
        this.PLY_STOP_TIME = maxTime / 2;

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        if (moves.size() == 1){
            return moves.get(0);
        }
        
        Move move = this.iterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows(board, zobristHash, startTime, maxTime);

        long endTime = System.currentTimeMillis();

        if (move == null){
            return randomMove(board, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
        }

        System.out.println("Table size:");
        System.out.println(this.iterativeDeepeningDFS.aspirationSearch.principleVariationSearch.table.size());
        
        
        if (HEAVY_INFO_LOG){
            statistics.printStatistics();
        }
        
        if (INFO_LOG) {
            long time = endTime - startTime;
            System.out.println("time taken millis: " + time);
            System.out.println("------");

            if (time > 0) {
                System.out.println("Moves per second: " +
                        ((1000 * (statistics.numberOfMovesMade + statistics.numberOfQuiescentMovesMade)) / time));
                System.out.println("------");
            }
            
            System.out.println("best move: " + move);
            System.out.println("------");
        }
        
        return move;
    }

    private static Move randomMove (Chessboard board, List<Move> moves){
        Random r = new Random();
        int i = r.nextInt(moves.size());
        return moves.get(i);
    }
    
    public Move getAiMove(){
        return this.iterativeDeepeningDFS.aspirationSearch.getAiMove();
    }

    public PVLine getPV (Chessboard board){
        return PVLine.retrievePVfromTable(board,
                this.iterativeDeepeningDFS.aspirationSearch.principleVariationSearch.table);
    }

    public Engine() {
        this.stopInstruction = false;
        this.timeAllocator = new TimeAllocator();
    }

    public Engine(UCIEntry uciEntry) {
        this.uciEntry = uciEntry;
        this.stopInstruction = false;
    }

    public UCIEntry getUciEntry() {
        return uciEntry;
    }

    public boolean isStopInstruction() {
        return stopInstruction;
    }

    public void setStopInstruction(boolean stopInstruction) {
        this.stopInstruction = stopInstruction;
    }

    public EngineSpecifications getEngineSpecifications() {
        return engineSpecifications;
    }
}
