package javacode.chessengine.search;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessengine.timemanagement.TimeAllocator;
import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessengine.evaluation.EvaluationConstants.*;

class AspirationSearch {

    private final Engine engine;
    final PrincipleVariationSearch principleVariationSearch;

    AspirationSearch(Engine engine, Evaluator evaluator){
        this.engine = engine;
        this.principleVariationSearch = new PrincipleVariationSearch(engine, evaluator);
    }

    private boolean stopSearch(boolean outOfTime){
        return this.engine.isStopInstruction()
                || (this.engine.getEngineSpecifications().ALLOW_TIME_LIMIT && outOfTime);
    }

    int aspirationSearch(Chessboard board, long startTime, long timeLimitMillis,
                         ZobristHash zobristHash, int depth, int aspirationScore){

        int firstWindow = 100, alpha, beta, alphaFac = 2, betaFac = 2;
        if (this.engine.getEngineSpecifications().ALLOW_ASPIRATION_WINDOWS) {
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
            score = this.principleVariationSearch.principleVariationSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    depth, depth, 0, alpha, beta, 0, false);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score;
            }

            if (TimeAllocator.outOfTime(engine, startTime, timeLimitMillis)) {
                outOfTime = true;
            }
            
            /*
            Aspiration Search Miss:
            if score outside of window, widen window and increase speed of widening
             */
            if (this.engine.getEngineSpecifications().ALLOW_ASPIRATION_WINDOWS) {
                if (score <= alpha) {
                    alpha = -firstWindow * alphaFac;
                    if (alphaFac >= 4){
                        alpha = SHORT_MINIMUM;
                    }
                    alphaFac *= 2;
                    this.engine.statistics.numberOfFailedAspirations++;
                } else if (score >= beta) {
                    beta = firstWindow * betaFac;
                    if (betaFac >= 4){
                        beta = SHORT_MAXIMUM;
                    }
                    betaFac *= 2;
                    this.engine.statistics.numberOfFailedAspirations++;
                } else {
                    this.engine.statistics.numberOfSuccessfulAspirations++;

                    break;
                }
            }
            else {
                return score;
            }
        }
        return score;
    }

    Move getAiMove() {
        return this.principleVariationSearch.getAiMove();
    }

}
