package javacode.chessengine.search;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.search.NullMovePruning.maybeInEndgame;
import static javacode.chessengine.search.NullMovePruning.onlyPawnsLeftForPlayer;

class InternalIterativeDeepening {

    public static final int iidDepthReduction = 2;

    static boolean isIIDAllowedHere(Chessboard board, int depth, boolean reducedSearch, boolean thisIsAPrincipleVariationNode){
        return !reducedSearch
                && thisIsAPrincipleVariationNode
                && depth > iidDepthReduction + 1
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
                && !maybeInEndgame(board)
                ;
    }
}
