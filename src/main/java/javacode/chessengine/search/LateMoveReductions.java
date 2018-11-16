package javacode.chessengine.search;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

public class LateMoveReductions {
    
    public static int lateMoveDepthReduction(int depth){
        return 2;
    }

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  Move move, int depth, int numberOfMovesSearched, boolean reducedSearch,
                                                  boolean wasPromotionMove, boolean wasCaptureMove,
                                                  boolean givesCheckMove, boolean pawnToSix, boolean pawnToSeven) {

        return depth > 3
                && (depth - lateMoveDepthReduction(depth) - 1) >= 2
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