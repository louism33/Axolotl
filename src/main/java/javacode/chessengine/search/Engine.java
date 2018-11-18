package javacode.chessengine.search;

import javacode.chessengine.main.UCIEntry;
import javacode.chessengine.timemanagement.TimeAllocator;
import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessengine.utilities.Statistics;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import org.junit.Assert;

import java.util.List;

import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;

public class Engine {

    public final Statistics statistics = new Statistics(this);
    private final boolean HEAVY_INFO_LOG = false;
    public int MAX_DEPTH = 12;
    
    private UCIEntry uciEntry;
    private final EngineSpecifications engineSpecifications = new EngineSpecifications();
    
    private final IterativeDeepeningDFS iterativeDeepeningDFS;
    private final TimeAllocator timeAllocator = new TimeAllocator();
    
    private boolean stopInstruction;
    private boolean setup = false;

    public Engine() {
        this.stopInstruction = false;
        this.iterativeDeepeningDFS = new IterativeDeepeningDFS(this);
    }

    public Engine(UCIEntry uciEntry) {
        this.uciEntry = uciEntry;
        this.stopInstruction = false;
        this.iterativeDeepeningDFS = new IterativeDeepeningDFS(this);
    }
    
    private void setup(){
        stopInstruction = false;
        setup = true;
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
        ZobristHash zobristHash = new ZobristHash(board);

        long startTime = System.currentTimeMillis();
        
        List<Move> moves = generateLegalMoves(board, board.isWhiteTurn());
        
        if (moves.size() == 1){
            return moves.get(0);
        }
        
        Move move = this.iterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows
                (board, zobristHash, startTime, maxTime);

        Assert.assertNotNull(move);
        
        if (HEAVY_INFO_LOG){
            statistics.printStatistics();
        }
        
        long endTime = System.currentTimeMillis();
            statistics.infoLog(endTime, startTime, move);
        
        return move;
    }

    public Move getAiMove(){
        return this.iterativeDeepeningDFS.aspirationSearch.getAiMove();
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
