package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

class LateMoveReductions {
    
    static int lateMoveDepthReduction(int depth){
        return 2 + depth / 3;
    }

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  Move move, int depth, int numberOfMovesSearched, boolean reducedSearch,
                                                  boolean wasPromotionMove, boolean wasCaptureMove,
                                                  boolean givesCheckMove, boolean pawnToSix, boolean pawnToSeven) {

        return depth > 3
                && !reducedSearch
                && numberOfMovesSearched > 2
                && !wasPromotionMove
                && !wasCaptureMove
                && !givesCheckMove
                && !pawnToSix
                && !pawnToSeven
                ;
    }
    
    
}