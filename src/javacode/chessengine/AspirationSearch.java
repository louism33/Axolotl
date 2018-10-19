package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.PrincipleVariationSearch.principleVariationSearch;

class AspirationSearch {
    
    static int aspirationSearch(Chessboard board, long timeLimit, int depth, int aspirationScore){
        int firstWindow = 100;
        int alpha = aspirationScore - firstWindow, alphaFac = 2;
        int beta = aspirationScore + firstWindow, betaFac = 2;
        int score;
        
        for (;;) {
            
            score = principleVariationSearch(board, depth, depth, alpha, beta);

            if (score <= alpha) {
                alpha = -firstWindow * alphaFac;
                alphaFac *= 2;
            }
            else if (score >= beta) {
                beta = firstWindow * betaFac;
                betaFac *= 2;
            }
            else {
                break;
            }
        }
        
        return score;
        
    }
    
}
