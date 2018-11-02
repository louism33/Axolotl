package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessengine.MoveOrderer.checkingMove;

class FutilityPruning {
    
    static final int[] futilityMargin = {0, 100, 200, 300, 400, 500, 600};
    private static final int futilityBelowThisDepth = futilityMargin.length;
    
    static boolean isFutilityPruningAllowedHere(Chessboard board, Move move, int depth, boolean wasCaptureMove){
        return depth < futilityBelowThisDepth
                && !wasCaptureMove
                && !checkingMove(board, move)
                ;
    }
    
}
