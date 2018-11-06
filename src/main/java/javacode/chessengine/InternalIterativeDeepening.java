package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.NullMovePruning.maybeInEndgame;
import static javacode.chessengine.NullMovePruning.maybeInZugzwang;

public class InternalIterativeDeepening {

    static int iidDepthReduction = 2;
    
    static boolean isIIDAllowedHere(Chessboard board, int depth, boolean reducedSearch, boolean thisIsAPrincipleVariationNode){
        return !reducedSearch
                && thisIsAPrincipleVariationNode
                && depth > iidDepthReduction
                && !maybeInEndgame(board)
                && !maybeInZugzwang(board, board.isWhiteTurn())
                ;
    }
}
