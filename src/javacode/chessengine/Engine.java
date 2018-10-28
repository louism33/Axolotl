package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.List;
import java.util.Random;

import static javacode.chessengine.IterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows;

public class Engine {

    Chessboard board;

    /*
    max depth only works is time limit is false
     */
    public static final int MAX_DEPTH = 16;
    public static final boolean ALLOW_TIME_LIMIT = true;
    public static final long TIME_LIMIT = 30000;
    
    public static final boolean DEBUG = true;

    public static int numberOfMovesMade = 0;
    public static int numberOfQuiescentMovesMade = 0;
    public static int numberOfEvals = 0;
    public static int numberOfQuiescentEvals = 0;
    public static int numberOfCheckmates = 0;
    public static int numberOfStalemates = 0;
    public static int numberOfFailHighs = 0;
    public static int numberOfNullMoveHits = 0;
    public static int numberOfNullMoveMisses = 0;
    public static int numberOfPVSHits = 0;
    public static int numberOfPVSMisses = 0;
    public static int numberOfLateMovePrunings = 0;
    public static int numberOfLateMoveReductions = 0;
    public static int numberOfLateMoveReductionsHits = 0;
    public static int numberOfLateMoveReductionsMisses = 0;
    public static int numberOfCheckExtensions = 0;
    public static int numberOfPassedPawnExtensions = 0;
    public static int numberOfSuccessfulFutilities = 0;
    public static int numberOfFailedFutilities = 0;
    public static int numberOfSuccessfulRazors = 0;
    public static int numberOfFailedRazors = 0;
    public static int numberOfSuccessfulAspirations = 0;
    public static int numberOfFailedAspirations = 0;

    static final boolean ALLOW_PRINCIPLE_VARIATION_SEARCH   = true;
    static final boolean ALLOW_MATE_DISTANCE_PRUNING        = true;
    static final boolean ALLOW_EXTENSIONS                   = true;
    static final boolean ALLOW_LATE_MOVE_REDUCTIONS         = false;
    static final boolean ALLOW_LATE_MOVE_PRUNING            = false;
    static final boolean ALLOW_NULL_MOVE_PRUNING            = false;
    static final boolean ALLOW_RAZORING                     = false;
    static final boolean ALLOW_FUTILITY_PRUNING             = false;
    static final boolean ALLOW_KILLERS                      = true;
    static final boolean ALLOW_HISTORY_MOVES                = true;
    static final boolean ALLOW_ASPIRATION_WINDOWS           = true;

    private static void allocatedTime(Chessboard board, long timeLimit){

    }
    
    public static ZobristHash zobristHash;
            
    public static Move search (Chessboard board, long timeLimit){
        
//        timeLimit = TIME_LIMIT;
        
        /*
        create hash value of the board, used for lookup in transposition table
         */
        zobristHash = new ZobristHash(board);
        
        long startTime = System.currentTimeMillis();

        Move move = iterativeDeepeningWithAspirationWindows(board, zobristHash, startTime, timeLimit);

        long endTime = System.currentTimeMillis();
        
        if (move == null) {
            move = randomMove(board, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
        }


        if (Engine.DEBUG) {
            System.out.println();
            System.out.println("------");

            System.out.println("Modifications:" +
                    "\nALLOW_PRINCIPLE_VARIATION_SEARCH = "     + ALLOW_PRINCIPLE_VARIATION_SEARCH +
                    "\nALLOW_MATE_DISTANCE_PRUNING = "          + ALLOW_MATE_DISTANCE_PRUNING +
                    "\nALLOW_EXTENSIONS = "                     + ALLOW_EXTENSIONS +
                    "\nALLOW_LATE_MOVE_REDUCTIONS = "           + ALLOW_LATE_MOVE_REDUCTIONS +     
                    "\nALLOW_LATE_MOVE_PRUNING = "              + ALLOW_LATE_MOVE_PRUNING +
                    "\nALLOW_NULL_MOVE_PRUNING = "              + ALLOW_NULL_MOVE_PRUNING +
                    "\nALLOW_RAZORING = "                       + ALLOW_RAZORING +
                    "\nALLOW_FUTILITY_PRUNING = "               + ALLOW_FUTILITY_PRUNING +
                    "\nALLOW_KILLERS = "                        + ALLOW_KILLERS +
                    "\nALLOW_HISTORY_MOVES = "                  + ALLOW_HISTORY_MOVES +
                    "\nALLOW_ASPIRATION_WINDOWS = "             + ALLOW_ASPIRATION_WINDOWS +
                    "");
            System.out.println();
            if (ALLOW_PRINCIPLE_VARIATION_SEARCH) {
                System.out.println("number of PV hits: " + numberOfPVSHits);
                System.out.println("number of PV misses: " + numberOfPVSMisses);
                System.out.println();
            }

            if (ALLOW_EXTENSIONS) {
                System.out.println("number of Check Extensions " + numberOfCheckExtensions);
                System.out.println();
            }
            if (ALLOW_LATE_MOVE_REDUCTIONS) {
                System.out.println("number of late move reductions: " + numberOfLateMoveReductions);
                System.out.println("number of late move reduction hits: " + numberOfLateMoveReductionsHits);
                System.out.println("number of late move reduction misses: " + numberOfLateMoveReductionsMisses);
                System.out.println();
            }
            if (ALLOW_LATE_MOVE_PRUNING) {
                System.out.println("number of late move prunings: " + numberOfLateMovePrunings);
                System.out.println();
            }
            if (ALLOW_NULL_MOVE_PRUNING){
                System.out.println("number of null move hits: " + numberOfNullMoveHits);
                System.out.println("number of null move misses: " + numberOfNullMoveMisses);
                System.out.println();
            }
            if (ALLOW_RAZORING) {
                System.out.println("Number of successful razors: " + numberOfSuccessfulRazors);
                System.out.println("Number of failed razors: " + numberOfFailedRazors);
                System.out.println();
            }
           
            if (ALLOW_FUTILITY_PRUNING) {
                System.out.println("Number of successful futilities: " + numberOfSuccessfulFutilities);
                System.out.println("Number of failed futilities: " + numberOfFailedFutilities);
                System.out.println();
            }

            if (ALLOW_ASPIRATION_WINDOWS) {
                System.out.println("Number of successful aspirations: " + numberOfSuccessfulAspirations);
                System.out.println("Number of failed aspirations: " + numberOfFailedAspirations);
                System.out.println();
            }
        
            System.out.println("------");
            System.out.println("number of moves made: " + numberOfMovesMade);
            System.out.println("number of Q moves made: " + numberOfQuiescentMovesMade);
            System.out.println("total moves: " + (numberOfQuiescentMovesMade + numberOfMovesMade));
            System.out.println("number of evals: " + numberOfEvals);
            System.out.println("number of Q evals: " + numberOfQuiescentEvals);

            System.out.println("number of checkmates found: " + numberOfCheckmates);
            System.out.println("number of stalemates found: " + numberOfStalemates);
            System.out.println("transposition table size: "+ TranspositionTable.getInstance().size());

            System.out.println();
            System.out.println("time taken millis: " + (endTime - startTime));
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
