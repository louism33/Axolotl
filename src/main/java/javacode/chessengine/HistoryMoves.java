package javacode.chessengine;

import javacode.chessprogram.chess.Move;

import java.util.Collections;

import static javacode.chessengine.MoveOrderer.*;

class HistoryMoves {
    
    /*
    History Moves:
    one int for every from-square to-square combination. Incremented every time this move is found to produce cutoffs
     */
    private static final int[][] historyMoves = new int[64][64];
    
    /*
    square the ply as shallower moves get added many times
     */
    static void updateHistoryMoves(Move move, int ply){
        historyMoves[move.getSourceAsPieceIndex()][move.destinationIndex] += (ply * ply);
    }

    static int historyMoveScore(Move move){
        int maxMoveScoreOfHistory = MAX_HISTORY_MOVE_SCORE;
        int historyScore = historyMoves[move.getSourceAsPieceIndex()][move.destinationIndex];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }
}
