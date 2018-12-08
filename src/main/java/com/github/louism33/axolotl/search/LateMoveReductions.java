package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

class LateMoveReductions {
    
    static int lateMoveDepthReduction(int depth){
        return 2;
    }

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  int move, int depth, int numberOfMovesSearched, boolean reducedSearch,
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