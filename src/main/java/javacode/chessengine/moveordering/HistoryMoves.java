package javacode.chessengine.moveordering;

import javacode.chessprogram.chess.Move;

class HistoryMoves {
    
    private final MoveOrderer moveOrderer;

    public HistoryMoves(MoveOrderer moveOrderer) {
        this.moveOrderer = moveOrderer;
    }

    /*
    History Moves:
    one int for every from-square to-square combination. Incremented every time this move is found to produce cutoffs
     */
    private final int[][] historyMoves = new int[64][64];
    
    /*
    square the ply as shallower moves get added many times
     */
    public void updateHistoryMoves(Move move, int ply){
        historyMoves[move.getSourceAsPieceIndex()][move.destinationIndex] += (ply * ply);
    }

    public int historyMoveScore(Move move){
        int maxMoveScoreOfHistory = MoveOrderingConstants.MAX_HISTORY_MOVE_SCORE;
        int historyScore = historyMoves[move.getSourceAsPieceIndex()][move.destinationIndex];
        return historyScore > maxMoveScoreOfHistory ? maxMoveScoreOfHistory : historyScore;
    }
}
