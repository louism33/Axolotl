package javacode.chessengine;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessprogram.moveMaking.MoveParser.isPromotionMove;

class LateMoveReductions {
    
    static int lateMoveDepthReduction(int ply){
        return 2;// + ply / 3;
    }

    static boolean isLateMoveReductionAllowedHere(Chessboard board,
                                                  Move move, int depth, int numberOfMovesSearched, boolean reducedSearch) {

        return depth > 2
                && !reducedSearch
                && numberOfMovesSearched > 3
                && !(isPromotionMove(move))
//                && !moveIsCapture(board, move)
                && !CheckChecker.boardInCheck(board, board.isWhiteTurn())
                ;
    }
    
    
}