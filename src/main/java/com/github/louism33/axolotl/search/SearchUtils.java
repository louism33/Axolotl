package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

public final class SearchUtils {
 
    // idea from Ethereal
    public static final int[] skipLookup = {2, 2, 2, 4, 4, 3, 2, 5}; // thread t will skip depth skipLookup[t] ...
    public static final int[] skipBy =     {1, 1, 2, 2, 3, 2, 1, 3}; // ... by amount skipBy[t]

    public static final int PANIC_SCORE_DELTA = 100;
    static final int iidDepth = 5;

    // most values here tuned with spsa, using zamar's perl script: https://github.com/zamar/spsa
    public static final int[] futilityMargin = {0, 157, 257, 367, 497, 607};        
    public static final int futilityBelowThisDepth = futilityMargin.length;

    public static final int[] alphaRazorMargin = {0, 275, 486, 561};
    public static final int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    public static final int[] betaRazorMargin = {0, 253, 361, 454, 552, 744, 1034};
    public static final int betaRazorBelowThisDepth = betaRazorMargin.length;

    public static final int[] ASPIRATION_WINDOWS = {25, 50, 100, 200, 1000};
    static final int ASPIRATION_MAX_TRIES = ASPIRATION_WINDOWS.length;

    static int extensions(Chessboard board, int ply, boolean boardInCheck, int[] moves) {
        if (ply < 1) {
            return 0;
        }

        if (boardInCheck) {
            return 1;
        }

        if (board.previousMoveWasPawnPushToSix()) {
            return 1;
        }

        if (board.previousMoveWasPawnPushToSeven()) {
            return 1;
        }

        if (moves != null && MoveParser.numberOfRealMoves(moves) == 1) {
            return 1;
        }

        return 0;
    }

    static boolean isAlphaRazoringMoveOkHere(int depth, int alpha) {
        return depth < alphaRazorBelowThisDepth
                && alpha < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    static boolean isBetaRazoringOkHere(int depth, int staticBoardEval) {
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    static boolean isNullMoveOkHere(Chessboard board, int nullMoveCounter, int depth, int R) {
        // todo, make more efficient
        return nullMoveCounter < 1
                && depth >= 2
                && !maybeInEndgame(board)
                && notJustPawnsLeft(board)
                && !maybeInZugzwang(board);
    }

    public static boolean maybeInEndgame(Chessboard board) { // todo, use specific endgame hash
        return populationCount(board.pieces[board.turn][ALL_COLOUR_PIECES] | board.pieces[1 - board.turn][ALL_COLOUR_PIECES]) < 9;
    }

    public static boolean maybeInZugzwang(Chessboard board) {
        // returns true if you are down to Pawns and King (+1 extra piece)
        final int turn = board.turn;
        return populationCount(board.pieces[turn][ALL_COLOUR_PIECES] ^ (board.pieces[turn][PAWN] | board.pieces[turn][KING])) <= 1;
    }

    static boolean notJustPawnsLeft(Chessboard board) {
        final int turn = board.turn;
        return populationCount(board.pieces[turn][ALL_COLOUR_PIECES] ^ (board.pieces[turn][PAWN] | board.pieces[turn][KING])) != 0;
    }

    static boolean isFutilityPruningAllowedHere(int depth,
                                                boolean promotionMove,
                                                boolean givesCheckMove,
                                                boolean pawnToSix, boolean pawnToSeven, int numberOfMovesSearched) {
        return depth < futilityBelowThisDepth
                && !promotionMove
                && !givesCheckMove
                && !pawnToSix
                && !pawnToSeven
                && numberOfMovesSearched > 5
                ;
    }
}
