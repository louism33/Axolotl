package javacode.chessengine.search;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.search.NullMovePruning.maybeInEndgame;
import static javacode.chessengine.search.NullMovePruning.onlyPawnsLeftForPlayer;

public class InternalIterativeDeepening {

    public static int iidDepthReduction = 2;

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
