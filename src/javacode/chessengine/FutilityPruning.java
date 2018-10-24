package javacode.chessengine;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

class FutilityPruning {
    
    static final int futilityMarginDepthOne = 200;
    
    static boolean isFutilityPruningAllowedHere(Chessboard board, Move move, int depth){
        return depth == 1 
                && !MoveOrderer.moveIsCapture(board, move)
                && !CheckChecker.boardInCheck(board, board.isWhiteTurn())
                ;
    }
    
    
}
