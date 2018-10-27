package javacode.chessengine;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessengine.MoveOrderer.moveIsCapture;

class LateMoveReductions {
    
    static int lateMoveDepthReduction(int ply){
        return 2 + ply / 3;
    }

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  Move move, int ply, int numberOfMovesSearched) {

        return ply >= 2
                && numberOfMovesSearched > 2
                && !moveIsCapture(board, move)
                && !CheckChecker.boardInCheck(board, board.isWhiteTurn())
                ;
    }
    
    
}