package javacode.chessengine;

import javacode.chessprogram.chess.Move;

class HistoryMoves {
    
    /*
    History Moves:
    one int for every from-square to-square combination. Incremented every time this move is found to produce cutoffs
     */
    static int[][] historyMoves = new int[64][64];
    
    /*
    square the ply as shallower moves get added many times
     */
    static void updateHistoryMoves(Move move, int ply){
        historyMoves[move.getSourceAsPiece()][move.destination] += (ply * ply);
    }
    
    static int historyMoveScore(Move move){
        int maxMoveScoreOfHistory = 250;
        int historyScore = historyMoves[move.getSourceAsPiece()][move.destination];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }
}
