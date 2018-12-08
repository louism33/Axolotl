package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;

class AspirationSearch {

    private static boolean stopSearch(boolean outOfTime){
        return Engine.isStopInstruction()
                || (Engine.getEngineSpecifications().ALLOW_TIME_LIMIT && outOfTime);
    }

    static int aspirationSearch(Chessboard board, long startTime, long timeLimitMillis,
                         int depth, int aspirationScore) throws IllegalUnmakeException {

        int firstWindow = 100, alpha, beta, alphaFac = 2, betaFac = 2;
        if (Engine.getEngineSpecifications().ALLOW_ASPIRATION_WINDOWS) {
            firstWindow = 100;
            alpha = aspirationScore - firstWindow;
            beta = aspirationScore + firstWindow;
        }
        else {
            alpha = SHORT_MINIMUM;
            beta = SHORT_MAXIMUM;
        }
        int score = aspirationScore;

        boolean outOfTime = false;

        while (!stopSearch(outOfTime)){
            
            /*
            Aspiration Search:
            call main search function with artificially small windows, hoping for more cutoffs
             */
            score = PrincipleVariationSearch.principleVariationSearch(board,
                    startTime, timeLimitMillis,
                    depth, depth, 0, alpha, beta, 0, false);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score;
            }

            if (TimeAllocator.outOfTime(startTime, timeLimitMillis)) {
                outOfTime = true;
            }
            
            /*
            Aspiration Search Miss:
            if score outside of window, widen window and increase speed of widening
             */
            if (Engine.getEngineSpecifications().ALLOW_ASPIRATION_WINDOWS) {
                if (score <= alpha) {
                    alpha = -firstWindow * alphaFac;
                    if (alphaFac >= 4){
                        alpha = SHORT_MINIMUM;
                    }
                    alphaFac *= 2;
                    Engine.statistics.numberOfFailedAspirations++;
                } else if (score >= beta) {
                    beta = firstWindow * betaFac;
                    if (betaFac >= 4){
                        beta = SHORT_MAXIMUM;
                    }
                    betaFac *= 2;
                    Engine.statistics.numberOfFailedAspirations++;
                } else {
                    Engine.statistics.numberOfSuccessfulAspirations++;

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
