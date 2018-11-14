package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.evaluation.Evaluator;

class AspirationSearch {

    private final Engine engine;
    final PrincipleVariationSearch principleVariationSearch;
    private final Evaluator evaluator;
    private UCIPrinter uciPrinter;

    AspirationSearch(Engine engine, Evaluator evaluator){
        this.engine = engine;
        this.evaluator = evaluator;
        this.principleVariationSearch = new PrincipleVariationSearch(engine, evaluator);
    }
    
    int aspirationSearch(Chessboard board, long startTime, long timeLimitMillis,
                                ZobristHash zobristHash, int depth, int aspirationScore){

        int firstWindow = 100, alpha, beta, alphaFac = 2, betaFac = 2;
        if (this.engine.ALLOW_ASPIRATION_WINDOWS) {
            firstWindow = 100;
            alpha = aspirationScore - firstWindow;
            beta = aspirationScore + firstWindow;
        }
        else {
            alpha = this.evaluator.SHORT_MINIMUM;
            beta = this.evaluator.SHORT_MAXIMUM;
        }
        int score = aspirationScore;

        boolean outOfTime = false;
        
        while (!this.engine.isStopInstruction() && !outOfTime){
            
            /*
            Aspiration Search:
            call main search function with artificially small windows, hoping for more cutoffs
             */
            score = this.principleVariationSearch.principleVariationSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    depth, depth, 0, alpha, beta, 0, false);

            if (score >= this.evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score;
            }

            if (this.engine.ALLOW_TIME_LIMIT) {
                long currentTime = System.currentTimeMillis();
                long maxTime = startTime + timeLimitMillis;
                long timeLeft = maxTime - currentTime;
                if (timeLeft < 0) {
                    outOfTime = true;
                }
                if (timeLeft < this.engine.PLY_STOP_TIME) {
                    // not enough time to search another ply
                    outOfTime = true;
                }
            }
            
            /*
            Aspiration Search Miss:
            if score outside of window, widen window and increase speed of widening
             */
            if (this.engine.ALLOW_ASPIRATION_WINDOWS) {
                if (score <= alpha) {
                    alpha = -firstWindow * alphaFac;
                    if (alphaFac >= 4){
                        alpha = this.evaluator.SHORT_MINIMUM;
                    }
                    alphaFac *= 2;
                    if (this.engine.INFO_LOG) {
                        this.engine.statistics.numberOfFailedAspirations++;
                    }
                } else if (score >= beta) {
                    beta = firstWindow * betaFac;
                    if (betaFac >= 4){
                        beta = this.evaluator.SHORT_MAXIMUM;
                    }
                    betaFac *= 2;
                    if (this.engine.INFO_LOG) {
                        this.engine.statistics.numberOfFailedAspirations++;
                    }
                } else {
                    if (this.engine.INFO_LOG) {
                        this.engine.statistics.numberOfSuccessfulAspirations++;
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

    public Move getAiMove() {
        return this.principleVariationSearch.getAiMove();
    }

}
