package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MINIMUM;

public class ChessThread extends Thread{

    Chessboard board;
    int[] rootMoves;
    int aiMoveScore = SHORT_MINIMUM;
    int threadIndex;
    long startTime;
    long timeLimitMillis;

    public void setTime(long startTime, long timeLimitMillis) {
        this.startTime = startTime;
        this.timeLimitMillis = timeLimitMillis;
    }


    void putAIMoveFirst(int aiMove, int aiMoveScore) {
        ChessThread chessThread = (ChessThread) Thread.currentThread();

        if (rootMoves[0] == aiMove) {
            return;
        }

        System.arraycopy(rootMoves, 0, rootMoves, 1, 
                Ints.indexOf(rootMoves, aiMove));
        
        rootMoves[0] = aiMove;
        this.aiMoveScore = aiMoveScore;
    }


    public ChessThread(String name, int index){
        this.setName(name);
        this.threadIndex = index;
    }

    public Chessboard getBoard() {
        return board;
    }

    public void setBoard(Chessboard board, int[] rootMoves) {
        this.board = board;
        this.rootMoves = rootMoves;
    }

    @Override
    public synchronized void start() {
        super.start();

    }

    @Override
    synchronized public void run() {
        Assert.assertTrue(board != null);
        try {
//            System.out.println("Hi I am " + this.getName() + ", id " + this.threadIndex);
//            System.out.println("root moves: ");
//            MoveParser.printMoves(rootMoves);
            
            Engine.iterativeDeepeningWithAspirationWindows(this.board, this.rootMoves, this.startTime, this.timeLimitMillis);

            Engine.stopNow = true;
            
            Engine.lock.notify();
            
//            System.out.println("Now I am done");
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
                '}';
    }
}
