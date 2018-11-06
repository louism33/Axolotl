package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessengine.MoveOrderer.checkingMove;

class FutilityPruning {

    static final int quiescenceFutilityMargin = 200;
    static final int[] futilityMargin = {0, 100, 200, 300, 400, 500, 600};
    private static final int futilityBelowThisDepth = futilityMargin.length;
    
    static boolean isFutilityPruningAllowedHere(Chessboard board, Move move, int depth,
                                                boolean isCaptureMove, boolean promotionMove,
                                                boolean givesCheckMove,
                                                boolean pawnToSix, boolean pawnToSeven){
        return depth < futilityBelowThisDepth
                && !isCaptureMove
                && !promotionMove
                && !givesCheckMove
                && !pawnToSix
                && !pawnToSeven
                ;
    }


    
}
