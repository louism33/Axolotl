package javacode.chessengine;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.evalutation.Evaluator;

import static javacode.chessprogram.check.CheckChecker.*;

class Razoring {
    
    private static final int supportedDepth = 1;
    static final int razorMargin = 201;

    static boolean isRazoringMoveOkHere(Chessboard board, int depth, int alpha){
        return Engine.ALLOW_RAZORING
                && depth == supportedDepth
                && Math.abs(alpha) > Evaluator.IN_CHECKMATE_SCORE_MAX_PLY
                && !boardInCheck(board, board.isWhiteTurn())
                ;
    }
}
