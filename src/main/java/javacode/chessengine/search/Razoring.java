package javacode.chessengine.search;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessprogram.chess.Chessboard;

import static javacode.chessengine.evaluation.Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY;

public class Razoring {
    
    public static int[] alphaRazorMargin = {0, 300, 500, 650}; 
    private static int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    static boolean isAlphaRazoringMoveOkHere(Chessboard board, Evaluator evaluator, int depth, int alpha){
        return depth < alphaRazorBelowThisDepth
                && alpha < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    public static int[] betaRazorMargin = {0, 150, 250, 350, 450, 650, 750};
    private static int betaRazorBelowThisDepth = betaRazorMargin.length;
    
    static boolean isBetaRazoringMoveOkHere(Chessboard board, Evaluator evaluator, int depth, int staticBoardEval){
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }
}
