package javacode.chessengine.search;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessengine.protocolhelperclasses.PVLine;
import javacode.chessengine.protocolhelperclasses.UCIPrinter;
import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

import static javacode.chessengine.evaluation.Evaluator.*;

public class IterativeDeepeningDFS {

    private Engine engine;
    AspirationSearch aspirationSearch;
    private Evaluator evaluator;
    private UCIPrinter uciPrinter = null;

    IterativeDeepeningDFS(Engine engine){
        this.engine = engine;
        this.evaluator = new Evaluator(engine);
        this.aspirationSearch = new AspirationSearch(engine, evaluator);

        if (this.engine.getUciEntry() != null) {
            this.uciPrinter = new UCIPrinter(this.engine.getUciEntry(), this.engine);
        }
    }

    Move iterativeDeepeningWithAspirationWindows(Chessboard board, ZobristHash zobristHash, long startTime, long timeLimitMillis){
        int maxDepth = this.engine.getEngineSpecifications().ALLOW_TIME_LIMIT ? 10000 : this.engine.MAX_DEPTH;
        int aspirationScore = 0;
        int depth = 0;
        boolean outOfTime = false;
        
        /*
        Iterative Deepening Depth First Search:
        call searchMyTime function at increasing depths, the data we get from lower depths is easily worth it
         */
        while (!this.engine.isStopInstruction() && !outOfTime && depth < maxDepth){

            if (this.engine.INFO_LOG && depth > 0 && this.uciPrinter == null) {
                String formattedDepthInfo = String.format("----- depth: %03d, previous best move: %s -----"
                        , depth, this.aspirationSearch.getAiMove());
                System.out.print(formattedDepthInfo);
            }

            int score = this.aspirationSearch.aspirationSearch(board, startTime, timeLimitMillis, zobristHash, depth, aspirationScore);

            long timeTaken = System.currentTimeMillis() - startTime;
            
            PVLine pvLine = PVLine.retrievePVfromTable(board, this.aspirationSearch.principleVariationSearch.table);
            if (this.engine.INFO_LOG && depth > 0 && this.uciPrinter == null) {
                System.out.println(" current best move: " + this.aspirationSearch.getAiMove());
                System.out.println("Current PV: ");
                
                List<Move> moves = pvLine.getPvMoves();
                int pvScore = pvLine.getScore();
                System.out.println(pvScore +" : " + moves);
                System.out.println();

            }
            else if (this.uciPrinter != null){
                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    int distanceToCheckmate = CHECKMATE_ENEMY_SCORE - score;
                    this.uciPrinter.acceptPVLine(pvLine, depth, true, distanceToCheckmate, timeTaken);
                } else {
                    this.uciPrinter.acceptPVLine(pvLine, depth, false, 0, timeTaken);
                }
            }
            
            /*
            stop search when a checkmate has been found, however far away
             */
            if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY){
                if (this.engine.getEngineSpecifications().ALLOW_MATE_DISTANCE_PRUNING) {
                    int distanceToCheckmate = CHECKMATE_ENEMY_SCORE - score;
                    System.out.println("Checkmate found in " + distanceToCheckmate + " plies.");
                }
                else{
                    System.out.println("Checkmate found.");
                }
                break;
            }

            if (score <= IN_CHECKMATE_SCORE_MAX_PLY){
                if (this.engine.getEngineSpecifications().ALLOW_MATE_DISTANCE_PRUNING) {
                    int distanceToCheckmate = score - IN_CHECKMATE_SCORE;
                    System.out.println("I will lose in " + distanceToCheckmate + " plies.");
                }
                else{
                    System.out.println("I have lost this game.");
                }
                break;
            }

            if (this.engine.getEngineSpecifications().ALLOW_TIME_LIMIT) {
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

            if (outOfTime && this.uciPrinter == null){
                System.out.println("Current PV: ");
                List<Move> moves = pvLine.getPvMoves();
                int pvScore = pvLine.getScore();
                System.out.println(pvScore +" : " + moves);
                System.out.println();

            }
            else if (outOfTime){
//                this.uciPrinter.acceptPVLine(pvLine, depth, false, 0);
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
