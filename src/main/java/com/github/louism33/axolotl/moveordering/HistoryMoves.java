package com.github.louism33.axolotl.moveordering;

import static com.github.louism33.axolotl.moveordering.MoveOrderingConstants.MAX_HISTORY_MOVE_SCORE;
import static com.github.louism33.chesscore.MoveParser.getDestinationIndex;
import static com.github.louism33.chesscore.MoveParser.getSourceIndex;

class HistoryMoves {
    
    /*
    History Moves:
    one int for every from-square to-square combination. Incremented every time this move is found to produce cutoffs
     */
    private static final int[][] historyMoves = new int[64][64];
    
    /*
    increase the score as shallower moves get added many times
     */
    public static void updateHistoryMoves(int move, int ply){
        historyMoves[getSourceIndex(move)][getDestinationIndex(move)] += (2 * ply);
    }

    public static int historyMoveScore(int move){
        int maxMoveScoreOfHistory = MAX_HISTORY_MOVE_SCORE;
        int historyScore = historyMoves[getSourceIndex(move)][getDestinationIndex(move)];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }
}
