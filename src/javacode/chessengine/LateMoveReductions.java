package javacode.chessengine;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessengine.MoveOrderer.moveIsCapture;

class LateMoveReductions {

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  Move move, int depth, int numberOfMovesSearched,
                                                  int lateMoveReduction) {

        return depth >= 3
                && depth >= lateMoveReduction
                && numberOfMovesSearched > 3
                && !moveIsCapture(board, move)
                && !CheckChecker.boardInCheck(board, board.isWhiteTurn())
                ;
    }
    
    
}