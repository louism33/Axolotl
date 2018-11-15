package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.NullMovePruning.maybeInEndgame;
import static javacode.chessengine.NullMovePruning.onlyPawnsLeftForPlayer;

class InternalIterativeDeepening {

    static final int iidDepthReduction = 2;

    static boolean isIIDAllowedHere(Chessboard board, int depth, boolean reducedSearch, boolean thisIsAPrincipleVariationNode){
        return true
                && !reducedSearch
                && thisIsAPrincipleVariationNode
                && depth > iidDepthReduction + 1
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
                && !maybeInEndgame(board)
                ;
    }
}
