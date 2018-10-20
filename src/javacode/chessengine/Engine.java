package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {

    Chessboard board;
    
    static final boolean DEBUG = false;

    private static void allocatedTime(Chessboard board, long timeLimit){
        
    }
    
    public static Move search (Chessboard board, long timeLimit){
        List<Move> moves = new ArrayList<>();
        
        ZobristHash zobristHash = new ZobristHash(board);
        
        Move move = IterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows(board, zobristHash, timeLimit);

        if (move == null) {
            move = randomMove(board, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
        }
        return move;
    }

    private static Move randomMove (Chessboard board, List<Move> moves){
        Random r = new Random();
        int i = r.nextInt(moves.size());
        return moves.get(i);
    }
}
