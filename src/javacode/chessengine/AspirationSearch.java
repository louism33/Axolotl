package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.evaluation.Evaluator;

import static javacode.chessengine.Engine.*;
import static javacode.chessengine.PrincipleVariationSearch.principleVariationSearch;
import static javacode.evaluation.Evaluator.IN_CHECKMATE_SCORE_MAX_PLY;

class AspirationSearch {

    static int aspirationSearch(Chessboard board, long startTime, long timeLimitMillis,
                                ZobristHash zobristHash, int depth, int aspirationScore){
        
        int firstWindow = 100, alpha, beta, alphaFac = 2, betaFac = 2;
        if (ALLOW_ASPIRATION_WINDOWS) {
            firstWindow = 201;
            alpha = aspirationScore - firstWindow;
            beta = aspirationScore + firstWindow;
        }
        else {
            alpha = Evaluator.IN_CHECKMATE_SCORE - 1;
            beta = -Evaluator.IN_CHECKMATE_SCORE + 1;
        }
        int score = aspirationScore;

        boolean outOfTime = false;
        
        while (!outOfTime){
            /*
            Aspiration Search:
            call main search function with artificially small windows, hoping for more cutoffs
             */
            score = principleVariationSearch(board, zobristHash, 
                    startTime, timeLimitMillis,
                    depth, depth, 0, alpha, beta, 0, false);

            if (score >= -IN_CHECKMATE_SCORE_MAX_PLY){
                return score;
            }

            if (ALLOW_TIME_LIMIT) {
                long currentTime = System.currentTimeMillis();
                long timeLeft = startTime + timeLimitMillis - currentTime;
                if (timeLeft < 0) {
                    outOfTime = true;
                }
            }
            
            /*
            Aspiration Search Miss:
            if score outside of window, widen window and increase speed of widening
             */
            if (ALLOW_ASPIRATION_WINDOWS) {
                if (score <= alpha) {
                    alpha = -firstWindow * alphaFac;
                    alphaFac *= 2;
                    if (DEBUG) {
                        numberOfFailedAspirations++;
                    }
                } else if (score >= beta) {
                    beta = firstWindow * betaFac;
                    betaFac *= 2;
                    if (DEBUG) {
                        numberOfFailedAspirations++;
                    }
                } else {
                    if (DEBUG) {
                        numberOfSuccessfulAspirations++;
                    }
                    break;
                }
            }
            else {
                return score;
            }
        }
        return score;
    }

}
