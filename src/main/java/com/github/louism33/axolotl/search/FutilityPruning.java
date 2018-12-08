package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

class FutilityPruning {

    static final int quiescenceFutilityMargin = 200;
    
    static final int[] futilityMargin = {0, 150, 250, 350, 450, 550, 650};
    private static final int futilityBelowThisDepth = futilityMargin.length;
    
    static boolean isFutilityPruningAllowedHere(Chessboard board, int move, int depth,
                                                boolean promotionMove,
                                                boolean givesCheckMove,
                                                boolean pawnToSix, boolean pawnToSeven){
        return depth < futilityBelowThisDepth
                && !promotionMove
                && !givesCheckMove
                && !pawnToSix
                && !pawnToSeven
                ;
    }


    
}
