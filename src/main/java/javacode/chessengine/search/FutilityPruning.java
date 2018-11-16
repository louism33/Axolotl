package javacode.chessengine.search;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

public class FutilityPruning {

    static int quiescenceFutilityMargin = 200;
    
    public static int[] futilityMargin = {0, 100, 200, 300, 400, 500, 600};
    private static int futilityBelowThisDepth = futilityMargin.length;
    
    static boolean isFutilityPruningAllowedHere(Chessboard board, Move move, int depth,
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
