package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.NullMovePruning.onlyPawnsLeftForPlayer;
import static javacode.evaluation.Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static javacode.evaluation.Evaluator.IN_CHECKMATE_SCORE_MAX_PLY;

public class LateMovePruning {

    static boolean isLateMovePruningMoveOkHere(Chessboard board, int bestScore){
        return bestScore < CHECKMATE_ENEMY_SCORE_MAX_PLY
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
                ;
    }
}
