package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.evaluation.EvaluationConstantsOld.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

final class SearchUtils {

//    static final int[] futilityMargin = {0, 180, 250, 350, 450, 550, 650};
    static final int[] futilityMargin = {0, 180, 250, 350, 450};
    public static final int futilityBelowThisDepth = futilityMargin.length;

    static final int[] alphaRazorMargin = {0, 400, 600, 800};
    public static final int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    static final int[] betaRazorMargin = {0, 250, 350, 450, 550, 750, 1000};
    public static final int betaRazorBelowThisDepth = betaRazorMargin.length;

    static int extensions(Chessboard board, int ply, boolean boardInCheck, int[] moves){

        if (ply < 1){
            return 0;
        }

        if (boardInCheck){
            return 1;
        }

        if (board.previousMoveWasPawnPushToSix()){
            return 1;
        }

        if (board.previousMoveWasPawnPushToSeven()){
            return 1;
        }

        if (MoveParser.numberOfRealMoves(moves) == 1){
            return 1;
        }

        return 0;
    }

    static boolean isAlphaRazoringMoveOkHere(int depth, int alpha){
        return depth < alphaRazorBelowThisDepth
                && alpha < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    static boolean isBetaRazoringOkHere(int depth, int staticBoardEval){
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    static boolean isNullMoveOkHere(Chessboard board, int nullMoveCounter, int depth, int R){
        return nullMoveCounter < 2
//                && depth > R
                && !maybeInEndgame(board)
                && notJustPawnsLeft(board)
                && !maybeInZugzwang(board);
    }

    public static boolean maybeInEndgame(Chessboard board){
        return populationCount(board.pieces[board.turn][ALL_COLOUR_PIECES] | board.pieces[1 - board.turn][ALL_COLOUR_PIECES]) < 9;
    }

    public static boolean maybeInZugzwang(Chessboard board){
        // returns true if you are down to Pawns and King (+1 extra piece)
        final int turn = board.turn;
        return populationCount(board.pieces[turn][ALL_COLOUR_PIECES] ^ (board.pieces[turn][PAWN] | board.pieces[turn][KING])) <= 1;
    }

    static boolean notJustPawnsLeft(Chessboard board){
        final int turn = board.turn;
        return populationCount(board.pieces[turn][ALL_COLOUR_PIECES] ^ (board.pieces[turn][PAWN] | board.pieces[turn][KING])) != 0;
    }
    
    static boolean isFutilityPruningAllowedHere(int depth,
                                                boolean promotionMove,
                                                boolean givesCheckMove,
                                                boolean pawnToSix, boolean pawnToSeven, int numberOfMovesSearched){
        return depth < futilityBelowThisDepth
                && !promotionMove
                && !givesCheckMove
                && !pawnToSix
                && !pawnToSeven
                && numberOfMovesSearched > 5
                ;
    }
}
