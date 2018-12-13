package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class SearchUtils {

    static final int quiescenceFutilityMargin = 200;

    static final int[] futilityMargin = {0, 150, 250, 350, 450, 550, 650};
    private static final int futilityBelowThisDepth = futilityMargin.length;

    static final int[] alphaRazorMargin = {0, 300, 500, 650};
    private static final int alphaRazorBelowThisDepth = alphaRazorMargin.length;

    static final int[] betaRazorMargin = {0, 150, 250, 350, 450, 650, 750};
    private static final int betaRazorBelowThisDepth = betaRazorMargin.length;

    static int extensions(Chessboard board, int ply, boolean boardInCheck, int[] moves){

        if (ply < 1){
            return 0;
        }

        if (boardInCheck){
            Statistics.numberOfCheckExtensions++;
            return 1;
        }

        if (board.previousMoveWasPawnPushToSix()){
            Statistics.numberOfPassedPawnExtensions++;
            return 1;
        }

        if (board.previousMoveWasPawnPushToSeven()){
            Statistics.numberOfPassedPawnExtensions++;
            return 1;
        }

        if (MoveParser.numberOfRealMoves(moves) == 1){
            return 1;
        }

        return 0;
    }

    static boolean isAlphaRazoringMoveOkHere(Chessboard board, int depth, int alpha){
        return depth < alphaRazorBelowThisDepth
                && alpha < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    static boolean isBetaRazoringOkHere(Chessboard board, int depth, int staticBoardEval){
        return depth < betaRazorBelowThisDepth
                && staticBoardEval < CHECKMATE_ENEMY_SCORE_MAX_PLY
                ;
    }

    static int nullMoveDepthReduction(int depth){
        return 2;
    }

    static boolean isNullMoveOkHere(Chessboard board, int nullMoveCounter, int depth, int R){
        return nullMoveCounter < 2
                && depth > R
                && !maybeInEndgame(board)
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
                && !maybeInZugzwang(board, board.isWhiteTurn());
    }

    private static boolean maybeInEndgame(Chessboard board){
        return populationCount(board.allPieces()) < 9;
    }

    private static boolean maybeInZugzwang(Chessboard board, boolean white){
        // returns true if you are down to Pawns and King (+1 extra piece)
        long myPawns, myKing, allMyPieces;
        if (white){
            allMyPieces = board.whitePieces();
            myPawns = board.getWhitePawns();
            myKing = board.getWhiteKing();
        }
        else {
            allMyPieces = board.blackPieces();
            myPawns = board.getBlackPawns();
            myKing = board.getBlackKing();
        }
        return populationCount(allMyPieces ^ (myPawns | myKing)) <= 1;
    }

    static boolean onlyPawnsLeftForPlayer(Chessboard board, boolean white){
        long myPawns, myKing, allMyPieces;
        if (white){
            allMyPieces = board.whitePieces();
            myPawns = board.getWhitePawns();
            myKing = board.getWhiteKing();
        }
        else {
            allMyPieces = board.blackPieces();
            myPawns = board.getBlackPawns();
            myKing = board.getBlackKing();
        }
        return populationCount(allMyPieces ^ (myPawns | myKing)) == 0;
    }
    
    static boolean isFutilityPruningAllowedHere(Chessboard board, int move, int depth,
                                                boolean promotionMove,
                                                boolean givesCheckMove,
                                                boolean pawnToSix, boolean pawnToSeven, int numberOfMovesSearched){
        return depth < futilityBelowThisDepth
                && !promotionMove
                && !givesCheckMove
                && !pawnToSix
                && !pawnToSeven
                && numberOfMovesSearched > 2
                ;
    }


    static int lateMoveDepthReduction(int depth, int moveScore){
        return 2;
    }
    
}
