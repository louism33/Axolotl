package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;

class Razoring {
    
    public static final int[] alphaRazorMargin = {0, 300, 500, 650}; 
    private static final int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    static boolean isAlphaRazoringMoveOkHere(Chessboard board, int depth, int alpha){
        return depth < alphaRazorBelowThisDepth
                && alpha < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    public static final int[] betaRazorMargin = {0, 150, 250, 350, 450, 650, 750};
    private static final int betaRazorBelowThisDepth = betaRazorMargin.length;
    
    static boolean isBetaRazoringMoveOkHere(Chessboard board, int depth, int staticBoardEval){
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }
}
