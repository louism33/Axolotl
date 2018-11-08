package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.evaluation.Evaluator;

class Razoring {
    
    static final int[] alphaRazorMargin = {0, 300, 400, 500}; 
    private static final int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    static boolean isAlphaRazoringMoveOkHere(Chessboard board, Evaluator evaluator, int depth, int alpha){
        return depth < alphaRazorBelowThisDepth
                && alpha < evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }


    static final int[] betaRazorMargin = {0, 100, 200, 300, 400, 500, 600};
    private static final int betaRazorBelowThisDepth = betaRazorMargin.length;
    
    static boolean isBetaRazoringMoveOkHere(Chessboard board, Evaluator evaluator, int depth, int staticBoardEval){
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }
}
