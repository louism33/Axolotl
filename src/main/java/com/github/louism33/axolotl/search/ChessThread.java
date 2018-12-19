package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.ASPIRATION_MAX_TRIES;
import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_DEPTH;

public class ChessThread extends Thread{

    Chessboard board;
    //    int[] rootMoves;
    int aiMoveScore = SHORT_MINIMUM;
    int threadIndex;
    long startTime;
    long timeLimitMillis;

    public void setTime(long startTime, long timeLimitMillis) {
        this.startTime = startTime;
        this.timeLimitMillis = timeLimitMillis;
    }


//    void putAIMoveFirst(int aiMove, int aiMoveScore) {
//        ChessThread chessThread = (ChessThread) Thread.currentThread();
//
//        if (rootMoves[0] == aiMove) {
//            return;
//        }
//
//        System.arraycopy(this.rootMoves, 0, this.rootMoves, 1,
//                Ints.indexOf(rootMoves, aiMove));
//
//        rootMoves[0] = aiMove;
//        this.aiMoveScore = aiMoveScore;
//    }


    public ChessThread(String name, int index){
        this.setName(name);
        this.threadIndex = index;
    }

    public Chessboard getBoard() {
        return board;
    }

    public void setBoardAndRootMoves(Chessboard board) {
        this.board = board;
//        this.rootMoves = new int[rootMoves.length];
//        System.arraycopy(rootMoves, 0, this.rootMoves, 0, rootMoves.length);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    synchronized public void run() {
        Assert.assertTrue(board != null);
        try {
            int depth = 0;
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

                System.out.println("Depth: " + depth);

                int previousAi = rootMoves[0];


                while (true) {

                    score = principleVariationSearch(board, rootMoves,  depth, 0,
                            alpha, beta, 0, false, startTime, timeLimitMillis,
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

//            if (EngineSpecifications.INFO && depth > 6 && rootMoves[0][0] != previousAi){
//                UCIPrinter.sendInfoCommand(board, rootMoves[0][0], aiMoveScore, depth);
//            }

                aspirationScore = score;

                if (score >= CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                    break;
                }
            }

//        ChessThread thread = (ChessThread) Thread.currentThread();

//        if (INFO){
//            UCIPrinter.sendInfoCommand(board, rootMoves[0], aiMoveScore, depth);
//        }








            Engine.stopNow = true;

            Engine.HACK++;

            System.out.println("Now I am done");
//            System.out.println("score: " + aiMoveScore);
//            MoveParser.printMoves(rootMoves);

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
