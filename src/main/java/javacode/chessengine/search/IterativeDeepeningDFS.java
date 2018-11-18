package javacode.chessengine.search;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessengine.protocolhelperclasses.PVLine;
import javacode.chessengine.protocolhelperclasses.UCIPrinter;
import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessengine.evaluation.EvaluationConstants.*;
import static javacode.chessengine.timemanagement.TimeAllocator.outOfTime;

class IterativeDeepeningDFS {

    private final Engine engine;
    final AspirationSearch aspirationSearch;
    private final UCIPrinter uciPrinter;

    IterativeDeepeningDFS(Engine engine){
        this.engine = engine;
        Evaluator evaluator = new Evaluator(engine);
        this.aspirationSearch = new AspirationSearch(engine, evaluator);
        this.uciPrinter = new UCIPrinter(this.engine.getUciEntry(), this.engine);
    }

    private boolean stopSearch(boolean outOfTime, int depth, int maxDepth){
        return this.engine.isStopInstruction()
                || (this.engine.getEngineSpecifications().ALLOW_TIME_LIMIT && outOfTime)
                || (!this.engine.getEngineSpecifications().ALLOW_TIME_LIMIT && (depth > maxDepth));
    }

    Move iterativeDeepeningWithAspirationWindows(Chessboard board, ZobristHash zobristHash, long startTime, long timeLimitMillis){
        int aspirationScore = 0;
        int depth = 0;
        boolean outOfTime = false;

        /*
        Iterative Deepening Depth First Search:
        call searchMyTime function at increasing depths, the data we get from lower depths is easily worth it
         */
        while (!stopSearch(outOfTime, depth, this.engine.MAX_DEPTH)){

            int score = this.aspirationSearch.aspirationSearch(board, startTime, timeLimitMillis, zobristHash, depth, aspirationScore);

            // send various info through UCI protocol
            long timeTaken = System.currentTimeMillis() - startTime;
            PVLine pvLine = PVLine.retrievePVfromTable(board, this.aspirationSearch.principleVariationSearch.table);

            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                /*
                stop search when a checkmate has been found, however far away
                 */
                int distanceToCheckmate = CHECKMATE_ENEMY_SCORE - score;
                this.uciPrinter.acceptPVLine(pvLine, depth, true, distanceToCheckmate, timeTaken);

                break;

            } else if (score <= IN_CHECKMATE_SCORE_MAX_PLY){
                /*
                if we are certain to lose, don't search any further
                 */
                this.uciPrinter.acceptPVLine(pvLine, depth, false, 0, timeTaken);

                break;
            }
            else {
                this.uciPrinter.acceptPVLine(pvLine, depth, false, 0, timeTaken);
            }

            if (outOfTime(engine, startTime, timeLimitMillis)) {
                outOfTime = true;
            }

            aspirationScore = score;
            depth++;
        }
        return getAiMove();
    }

    private Move getAiMove() {
        return this.aspirationSearch.getAiMove();
    }

}
