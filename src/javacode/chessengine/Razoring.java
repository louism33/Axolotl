package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;

import static javacode.evaluation.Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY;

class Razoring {
    
    static final int[] alphaRazorMargin = {0, 300, 400, 500}; 
    private static final int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    static boolean isAlphaRazoringMoveOkHere(Chessboard board, int depth, int alpha){
        return depth < alphaRazorBelowThisDepth
                ;
    }


    static final int[] betaRazorMargin = {0, 100, 200, 300, 400, 500, 600};
    private static final int betaRazorBelowThisDepth = betaRazorMargin.length;
    
    static boolean isBetaRazoringMoveOkHere(Chessboard board, int depth, int staticBoardEval){
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }
}
