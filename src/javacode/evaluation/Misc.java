package javacode.evaluation;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.PinnedManager;

import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.FILES;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.moveGeneration.PinnedManager.*;

class Misc {

    private static final int MOVE_NUMBER_POINT = 1;
    private static final int BATTERY_SCORE = 25;
    private static final int I_CONTROL_OPEN_FILE = 30;
    private static final int MY_TURN_BONUS = 50;
    private static final int IN_CHECK_PENALTY = -30;

    private static final int BASIC_PINNED_PIECE_PENALTY_KING = -25;
    private static final int QUEEN_IS_PINNED = -150;
    private static final int BASIC_PINNED_PIECE_PENALTY_QUEEN = -25;

    static int evalMiscByTurn(Chessboard board, boolean white, List<Move> moves) {
        int score = 0;
        score += moveNumberScores(board, white, moves)
                + batteryAndFileControl(board, white, moves)
                + myTurnBonus(board, white, moves)
                + inCheckPenalty(board, white, moves)
                + pinnedPiecesPenalty(board, white, moves)
        ;
        return score;
    }

    private static int pinnedPiecesPenalty(Chessboard board, boolean white, List<Move> moves) {
        int score = 0;

        long myKing = white ? board.WHITE_KING : board.BLACK_KING;
        long myQueen = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;

        long pinnedPiecesToKing = whichPiecesArePinned(board, white, myKing);

        score += populationCount(pinnedPiecesToKing) * BASIC_PINNED_PIECE_PENALTY_KING;
        if ((pinnedPiecesToKing & myQueen) != 0){
            score += QUEEN_IS_PINNED;
        }

        long pinnedPiecesToQueen = whichPiecesArePinned(board, white, myQueen);
        score += populationCount(pinnedPiecesToQueen) * BASIC_PINNED_PIECE_PENALTY_QUEEN;

        return score;
    }

    private static int inCheckPenalty(Chessboard board, boolean white, List<Move> moves) {
        return CheckChecker.boardInCheck(board, white) ? IN_CHECK_PENALTY : 0;
    }

    private static int myTurnBonus(Chessboard board, boolean white, List<Move> moves) {
        return (board.isWhiteTurn() == white) ? MY_TURN_BONUS : 0;
    }

    private static int moveNumberScores(Chessboard board, boolean white, List<Move> moves) {
        return moves.size() * MOVE_NUMBER_POINT;
    }

    private static int batteryAndFileControl(Chessboard board, boolean white, List<Move> moves) {
        long myQueens = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;
        long myRooks = white ? board.WHITE_ROOKS : board.BLACK_ROOKS;
        long myBatteryPieces = myQueens | myRooks;
        long enemyPieces = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myRooks);
        long emptySquares = ~board.ALL_PIECES();

        int batteryAndFileControlScore = 0;
        for (long file : FILES) {
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