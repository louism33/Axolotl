package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.NullMovePruning.onlyPawnsLeftForPlayer;

public class InternalIterativeDeepening {

    static int iidDepthReduction = 2;

    static boolean isIIDAllowedHere(Chessboard board, int depth, boolean reducedSearch, boolean thisIsAPrincipleVariationNode){
        return true
//                && !reducedSearch
                && thisIsAPrincipleVariationNode
                && depth > iidDepthReduction + 1
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
//                && !maybeInEndgame(board)
//                && !maybeInZugzwang(board, board.isWhiteTurn())
                ;
    }
}
