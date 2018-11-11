package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.evaluation.Evaluator;

import static javacode.evaluation.Evaluator.CHECKMATE_ENEMY_SCORE;
import static javacode.evaluation.Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY;

class IterativeDeepeningDFS {

    private final Engine engine;
    final AspirationSearch aspirationSearch;
    private final Evaluator evaluator;

    IterativeDeepeningDFS(Engine engine){
        this.engine = engine;
        this.evaluator = new Evaluator(engine);
        this.aspirationSearch = new AspirationSearch(engine, evaluator);
    }

    Move iterativeDeepeningWithAspirationWindows(Chessboard board, ZobristHash zobristHash, long startTime, long timeLimitMillis){
        int maxDepth = this.engine.ALLOW_TIME_LIMIT ? 10000 : this.engine.MAX_DEPTH;
        int aspirationScore = 0;
        int depth = 0;
        boolean outOfTime = false;
        
        /*
        Iterative Deepening Depth First Search:
        call searchMyTime function at increasing depths, the data we get from lower depths is easily worth it
         */
        while (!outOfTime && depth < maxDepth){

            if (this.engine.DEBUG && depth > 0) {
                String formattedDepthInfo = String.format("----- depth: %03d, previous best move: %s -----"
                        , depth, this.aspirationSearch.getAiMove());
                System.out.print(formattedDepthInfo);
            }

            int score = this.aspirationSearch.aspirationSearch(board, startTime, timeLimitMillis, zobristHash, depth, aspirationScore);

            if (this.engine.DEBUG && depth > 0) {
                System.out.println(" current best move: " + this.aspirationSearch.getAiMove());
                System.out.println("Current PV: ");
                this.aspirationSearch.principleVariationSearch.retrievePVfromTable(board);
                System.out.println();
            }
            
            /*
            stop search when a checkmate has been found, however far away
             */
            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY){
                if (this.engine.ALLOW_MATE_DISTANCE_PRUNING) {
                    int distanceToCheckmate = CHECKMATE_ENEMY_SCORE - score;
                    System.out.println("Checkmate found in " + distanceToCheckmate + " plies.");
                }
                else{
                    System.out.println("Checkmate found.");
                }
                break;
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

            if (outOfTime){
                System.out.println("Current PV: ");
                this.aspirationSearch.principleVariationSearch.retrievePVfromTable(board);
                System.out.println();
            }

            aspirationScore = score;
            depth++;
        }
        return getAiMove();
    }

    public Move getAiMove() {
        return this.aspirationSearch.getAiMove();
    }

}
