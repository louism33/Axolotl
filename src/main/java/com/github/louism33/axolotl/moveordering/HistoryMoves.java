package com.github.louism33.axolotl.moveordering;

import com.github.louism33.chesscore.MoveParser;

class HistoryMoves {
    
    /*
    History Moves:
    one int for every from-square to-square combination. Incremented every time this move is found to produce cutoffs
     */
    private static final int[][] historyMoves = new int[64][64];
    
    /*
    square the ply as shallower moves get added many times
     */
    public static void updateHistoryMoves(int move, int ply){
        historyMoves[MoveParser.getSourceIndex(move)][MoveParser.getDestinationIndex(move)] += (ply * ply);
    }

    public static int historyMoveScore(int move){
        int maxMoveScoreOfHistory = MoveOrderingConstants.MAX_HISTORY_MOVE_SCORE;
        int historyScore = historyMoves[MoveParser.getSourceIndex(move)][MoveParser.getDestinationIndex(move)];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }
}
