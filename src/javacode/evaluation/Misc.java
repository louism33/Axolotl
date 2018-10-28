package javacode.evaluation;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.FILES;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;

class Misc {

    private static final int MOVE_NUMBER_POINT = 1;
    private static final int BATTERY_SCORE = 25;
    private static final int I_CONTROL_OPEN_FILE = 30;
    private static final int MY_TURN_BONUS = 0;

    static int evalMiscByTurn(Chessboard board, boolean white, List<Move> moves) {
        int score = 0;
        score += moveNumberScores(board, white, moves)
                + batteryAndFileControl(board, white, moves)
//                + myTurnBonus(board, white, moves)
        ;
        return score;
    }

    private static int myTurnBonus(Chessboard board, boolean white, List<Move> moves) {
//        System.out.println(white);
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