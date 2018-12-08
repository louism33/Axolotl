package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.search.NullMovePruning.maybeInEndgame;
import static com.github.louism33.axolotl.search.NullMovePruning.onlyPawnsLeftForPlayer;

class InternalIterativeDeepening {

    static final int iidDepthReduction = 2;

    static boolean isIIDAllowedHere(Chessboard board, int depth, boolean reducedSearch, boolean thisIsAPrincipleVariationNode){
        return !reducedSearch
                && thisIsAPrincipleVariationNode
                && depth > iidDepthReduction + 1
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
                && !maybeInEndgame(board)
                ;
    }
}
