package javacode.evalutation;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

public class Misc {
    
    private static final int MOVE_NUMBER_POINT = 1;

    static int evalMiscByTurn(Chessboard board, boolean white, List<Move> moves) {
        int score = 0;
        score += moveNumberScores(board, white, moves)
                ;
        return score;
    }

    private static int moveNumberScores(Chessboard board, boolean white, List<Move> moves) {
        return moves.size() * MOVE_NUMBER_POINT;
    }
}
