package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {

    Chessboard board;

    public static Move search (Chessboard board, long timeLimit){
        List<Move> moves = new ArrayList<>();
        Move move = aspirationSearch(board, timeLimit);
        return move;
    }

    private static Move aspirationSearch(Chessboard board, long timeLimit){
        List<Move> possibleMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        List<Integer> scores = new ArrayList<>();

        Move move = IterativeDeepeningDFS.iterativeDeepeningWithAspirationWindows(board, timeLimit);

        return move;
    }

    
    private static Move randomMove (Chessboard board, List<Move> moves){
        Random r = new Random();
        int i = r.nextInt(moves.size());
        return moves.get(i);
    }
}
