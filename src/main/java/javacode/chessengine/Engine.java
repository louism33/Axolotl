package javacode.chessengine;

import com.sun.javadoc.AnnotationDesc;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.evaluation.Evaluator;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static javacode.chessengine.IterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows;

public class Engine {

    Chessboard board;
    
    public static final boolean HEAVY_DEBUG = false;
    public static final boolean DEBUG = true;
    public static Statistics statistics = new Statistics();
    
    /*
    max depth only works is time limit is false
     */
    public static final int MAX_DEPTH = 15;
    public static final boolean ALLOW_TIME_LIMIT = true;
    public static final long TIME_LIMIT = 30000;

    static final boolean ALLOW_PRINCIPLE_VARIATION_SEARCH   = true;
    static final boolean ALLOW_MATE_DISTANCE_PRUNING        = true;
    static final boolean ALLOW_EXTENSIONS                   = true;
    static final boolean ALLOW_LATE_MOVE_REDUCTIONS         = true; 
    static final boolean ALLOW_LATE_MOVE_PRUNING            = false;
    static final boolean ALLOW_NULL_MOVE_PRUNING            = true;
    static final boolean ALLOW_ALPHA_RAZORING               = false; 
    static final boolean ALLOW_BETA_RAZORING                = false; 
    static final boolean ALLOW_FUTILITY_PRUNING             = false;
    static final boolean ALLOW_QUIESCENCE_FUTILITY_PRUNING  = false; // todo
    static final boolean ALLOW_KILLERS                      = true;
    static final boolean ALLOW_MATE_KILLERS                 = true;
    static final boolean ALLOW_HISTORY_MOVES                = true;
    static final boolean ALLOW_ASPIRATION_WINDOWS           = true;
    static final boolean ALLOW_INTERNAL_ITERATIVE_DEEPENING = true; 
    static final boolean ALLOW_SEE_PRUNING                  = false; // todo

    public static ZobristHash zobristHash;
    
    public static void resetEngine(){
        statistics = new Statistics();
    }

    public static Move search (Chessboard board, long timeLimit){
        PrincipleVariationSearch.timeUp = false;
        
        /*
        create hash value of the board, used for lookup in transposition table
         */
        zobristHash = new ZobristHash(board);
        
        
        Chessboard copyBoard;
        ZobristHash copyHash;    
        if (HEAVY_DEBUG) {
            copyBoard = Copier.copyBoard(board, board.isWhiteTurn(), false);
            copyHash = new ZobristHash(copyBoard);
            Assert.assertTrue(copyHash.equals(zobristHash));
            Assert.assertTrue(copyBoard.equals(board));
        }
        
        
        long startTime = System.currentTimeMillis();

        Move move = iterativeDeepeningWithAspirationWindows(board, zobristHash, startTime, timeLimit);

        long endTime = System.currentTimeMillis();

        if (move == null) {
            move = randomMove(board, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
        }

        if (HEAVY_DEBUG) {
            Assert.assertTrue(copyHash.equals(zobristHash));
            Assert.assertTrue(copyBoard.equals(board));
        }
        
        if (Engine.DEBUG) {
            statistics.printStatistics();
            System.out.println("time taken millis: " + (endTime - startTime));
            System.out.println("------");

            System.out.println("Moves per second: " + 
                    ((1000 * (statistics.numberOfMovesMade + statistics.numberOfQuiescentMovesMade)) / (endTime - startTime)));
            System.out.println("------");

            System.out.println("best move: " + PrincipleVariationSearch.getAiMove());
            System.out.println("------");
        }

        return move;
    }

    private static Move randomMove (Chessboard board, List<Move> moves){
        Random r = new Random();
        int i = r.nextInt(moves.size());
        return moves.get(i);
    }

    
}
