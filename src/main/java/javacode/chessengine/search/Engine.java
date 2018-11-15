package javacode.chessengine;

import javacode.chessengine.protocolutils.PVLine;
import javacode.chessengine.timemanagement.TimeAllocator;
import javacode.chessengine.utilities.Statistics;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessengine.main.UCIEntry;
import org.junit.Assert;

import java.util.List;
import java.util.Random;

public class Engine {

    private UCIEntry uciEntry;
    private Chessboard testBoard;
    private ZobristHash testHash;
    
    Chessboard board;
    public Statistics statistics;
    private IterativeDeepeningDFS iterativeDeepeningDFS;
    public ZobristHash zobristHash;
    private TimeAllocator timeAllocator;

    private boolean setup = false;

    public final boolean HEAVY_DEBUG = false;
    
    public final boolean INFO_LOG = true;
    public final boolean HEAVY_INFO_LOG = false;

    /*
    max depth only works is time limit is false
     */
    public int MAX_DEPTH = 100;
    public boolean ALLOW_TIME_LIMIT = true;

    public final boolean ALLOW_PRINCIPLE_VARIATION_SEARCH   = true;
    public final boolean ALLOW_ASPIRATION_WINDOWS           = true;

    public final boolean ALLOW_KILLERS               = true;
    public final boolean ALLOW_MATE_KILLERS          = true;
    public final boolean ALLOW_HISTORY_MOVES         = true;

    public final boolean ALLOW_MATE_DISTANCE_PRUNING        = true;
    public final boolean ALLOW_EXTENSIONS                   = true;

    public final boolean ALLOW_INTERNAL_ITERATIVE_DEEPENING = false;

    public final boolean ALLOW_LATE_MOVE_REDUCTIONS         = false;
    public final boolean ALLOW_LATE_MOVE_PRUNING            = true;
    public final boolean ALLOW_NULL_MOVE_PRUNING            = true;

    public final boolean ALLOW_ALPHA_RAZORING               = true;
    public final boolean ALLOW_BETA_RAZORING                = true;
    public final boolean ALLOW_FUTILITY_PRUNING             = true;

    public final boolean ALLOW_SEE_PRUNING                  = true;

    public final boolean ALLOW_QUIESCENCE_SEE_PRUNING       = false;
    public final boolean ALLOW_QUIESCENCE_FUTILITY_PRUNING  = false;
    
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

    public Move searchFixedDepth (Chessboard board, int depth){
        ALLOW_TIME_LIMIT = false;
        MAX_DEPTH = depth;
        return searchFixedTime(board, 0);
    }

    public Move searchMyTime (Chessboard board, long maxTime){
        ALLOW_TIME_LIMIT = true;
        
        if (maxTime < 5000){
            return searchFixedDepth(board, 2);
        }
        if (maxTime < 1000){
            return searchFixedDepth(board, 1);
        }
        long timeLimit = timeAllocator.allocateTime(board, maxTime);
        return searchFixedTime(board, timeLimit);
    }

    public Move searchFixedTime(Chessboard board, long maxTime){
        if (!setup){
            setup();
        }
        
        testBoard = Copier.copyBoard(board, board.isWhiteTurn(), false);
        
        /*
        create hash value of the board, used for lookup in transposition table
         */
        zobristHash = new ZobristHash(board);

        testHash = zobristHash;
        
        Chessboard copyBoard;
        ZobristHash copyHash;
        if (HEAVY_DEBUG) {
            copyBoard = Copier.copyBoard(board, board.isWhiteTurn(), false);
            copyHash = new ZobristHash(copyBoard);
            Assert.assertTrue(copyHash.equals(zobristHash));
            Assert.assertTrue(copyBoard.equals(board));
        }

        long startTime = System.currentTimeMillis();
        
        this.PLY_STOP_TIME = maxTime / 2;

        final List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        if (moves.size() == 1){
            return moves.get(0);
        }
        
        Move move = this.iterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows(board, zobristHash, startTime, maxTime);

        long endTime = System.currentTimeMillis();

        if (move == null){
            return randomMove(board, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
        }

        if (HEAVY_DEBUG) {
            Assert.assertTrue(copyHash.equals(zobristHash));
            Assert.assertTrue(copyBoard.equals(board));
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
}
