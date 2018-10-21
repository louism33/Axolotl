package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.PrincipleVariationSearch.principleVariationSearch;

class AspirationSearch {
    
    static int aspirationSearch(Chessboard board, long timeLimit, ZobristHash zobristHash, int depth, int aspirationScore){
        int firstWindow = 100,
                alpha = aspirationScore - firstWindow,
                beta = aspirationScore + firstWindow,
                alphaFac = 2,
                betaFac = 2,
                score;
        
        for (;;) {
            /*
            Aspiration Search:
            call main search function with artificially small windows, hoping for more cutoffs
             */
            score = principleVariationSearch(board, zobristHash, depth, depth, alpha, beta, 0);
            
            /*
            Aspiration Search Miss:
            if score outside of window, widen window and increase speed of widening
             */
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
