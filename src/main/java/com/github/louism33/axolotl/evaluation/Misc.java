package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.FILES;

class Misc {

    static int evalMiscByTurn(Chessboard board, boolean white, int[] moves) {
        int score = 0;
        score += 
                moveNumberScores(board, white, moves)
                + batteryAndFileControl(board, white, moves)
                + myTurnBonus(board, white, moves)
                + inCheckPenalty(board, white, moves)
                + pinnedPiecesPenalty(board, white, moves)
        ;
        return score;
    }

    private static int pinnedPiecesPenalty(Chessboard board, boolean white, int[] moves) {
        int score = 0;

//        long myKing = white ? board.getWhiteKing() : board.getBlackKing();
//        long myQueen = white ? board.getWhiteQueen() : board.getBlackQueen();
//
//        long pinnedPiecesToKing = whichPiecesArePinned(board, white, myKing);
//
//        score += populationCount(pinnedPiecesToKing) * BASIC_PINNED_PIECE_PENALTY_KING;
//        if ((pinnedPiecesToKing & myQueen) != 0){
//            score += QUEEN_IS_PINNED;
//        }
//
//        long pinnedPiecesToQueen = whichPiecesArePinned(board, white, myQueen);
//        score += populationCount(pinnedPiecesToQueen) * BASIC_PINNED_PIECE_PENALTY_QUEEN;

        return score;
    }

    private static int inCheckPenalty(Chessboard board, boolean white, int[] moves) {
        return board.inCheck(white) ? IN_CHECK_PENALTY : 0;
    }

    private static int myTurnBonus(Chessboard board, boolean white, int[] moves) {
        return (board.isWhiteTurn() == white) ? MY_TURN_BONUS : 0;
    }

    private static int moveNumberScores(Chessboard board, boolean white, int[] moves) {
        return white == board.isWhiteTurn() ? (moves.length * MOVE_NUMBER_POINT) / 20 : 0;
    }

    private static int batteryAndFileControl(Chessboard board, boolean white, int[] moves) {
        long myQueens = white ? board.getWhiteQueen() : board.getBlackQueen();
        long myRooks = white ? board.getWhiteRooks() : board.getBlackRooks();
        long myBatteryPieces = myQueens | myRooks;
        long enemyPieces = white ? board.blackPieces() : board.whitePieces();

        int batteryAndFileControlScore = 0;
        for (int i = 0; i < FILES.length; i++) {
            long file = FILES[i];
            if ((file & myBatteryPieces) == 0) {
                continue;
            }
            if ((file & enemyPieces) == 0) {
                batteryAndFileControlScore += I_CONTROL_OPEN_FILE;
            }

            if (populationCount(file & myBatteryPieces) > 1) {
                batteryAndFileControlScore += BATTERY_SCORE;
            }
        }
        return batteryAndFileControlScore;
    }
}