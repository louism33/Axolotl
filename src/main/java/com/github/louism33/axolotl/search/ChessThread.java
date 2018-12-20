package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIPrinter;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;

class ChessThread extends Thread{

    private Chessboard board;
    int aiMoveScore = SHORT_MINIMUM;
    private final int threadIndex;
    private long startTime;
    private long timeLimitMillis;

    public void setTime(long startTime, long timeLimitMillis) {
        this.startTime = startTime;
        this.timeLimitMillis = timeLimitMillis;
    }

    public ChessThread(String name, int index){
        this.setName(name);
        this.threadIndex = index;
    }

    public Chessboard getBoard() {
        return board;
    }

    public void setBoard(Chessboard board) {
        this.board = board;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    synchronized public void run() {
        Assert.assertTrue(board != null);
        try {
            int depth = threadIndex % 2; // start threads at different indexes to separate them
            int aspirationScore = 0;

            int alpha;
            int beta;
            int alphaAspirationAttempts = 0;
            int betaAspirationAttempts = 0;

            alpha = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[alphaAspirationAttempts];
            beta = aspirationScore + EngineSpecifications.ASPIRATION_WINDOWS[betaAspirationAttempts];

            int score;

            everything:
            while (depth < MAX_DEPTH
                    && !stopSearch(startTime, timeLimitMillis)) {

                depth++;

                depths[threadIndex]++;

//                System.out.println("depths: " + Arrays.toString(depths) + " my d: " + depth + ", my id: " + threadIndex);
                
                if (depth > 6){
//                    Engine.threadDepthIncrement(depth);
//                    if (depth < Engine.deepestThread){
//                        depth++;
//                    }
                }
                
                

                int previousAi = rootMoves[0];

                while (true) {

                    score = principleVariationSearch(board, rootMoves,  depth, 0,
                            alpha, beta, 0, startTime, timeLimitMillis,
                            threadIndex);

                    if (stopSearch(startTime, timeLimitMillis)){
                        break;
                    }

                    if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                        break everything;
                    }

                    if (score <= alpha) {
                        alphaAspirationAttempts++;
                        if (alphaAspirationAttempts + 1 >= ASPIRATION_MAX_TRIES){
                            alpha = SHORT_MINIMUM;
                        }
                        else {
                            alpha = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[alphaAspirationAttempts];
                        }
                    } else if (score >= beta) {
                        betaAspirationAttempts++;
                        if (betaAspirationAttempts + 1 >= ASPIRATION_MAX_TRIES){
                            beta = SHORT_MAXIMUM;
                        }
                        else {
                            beta = aspirationScore - EngineSpecifications.ASPIRATION_WINDOWS[betaAspirationAttempts];
                        }
                    } else {
                        break;
                    }
                }

                if (INFO
                        && depth > 8 
                        && this.threadIndex == 0
                        && rootMoves[0] != previousAi
                ){
                    System.out.println("depths: " + Arrays.toString(depths));
                    UCIPrinter.sendInfoCommand(board, rootMoves[0], Engine.aiMoveScore, depth);
                }

                aspirationScore = score;

                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    break;
                }
            }

            if (INFO && this.threadIndex == 0){
                UCIPrinter.sendInfoCommand(board, rootMoves[0], Engine.aiMoveScore, depth);
            }

            Engine.increment();

        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ChessThread{" +
                "threadIndex=" + threadIndex +
                ", aiMove=" + MoveParser.toString(rootMoves[0]) +
                ", moves =" + Arrays.toString(MoveParser.toString(rootMoves)) +
                '}';
    }
}
