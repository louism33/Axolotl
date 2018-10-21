package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

class LateMoveReductions {

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  Move move, int depth, int numberOfMovesSearched,
                                                  int lateMoveReduction) {

        return depth >= 3
                && depth >= lateMoveReduction
                && numberOfMovesSearched > 3
//                && !moveIsCapture(board, move)
                ;
    }
    
    
}