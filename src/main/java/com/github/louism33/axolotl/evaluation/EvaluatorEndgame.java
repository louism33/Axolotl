package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvalPrintObject.turn;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.POSITION_SCORES;
import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static java.lang.Long.lowestOneBit;
import static java.lang.Long.numberOfTrailingZeros;

public class EvaluatorEndgame {

    public static int evaluateKPK(Chessboard board, int strongerTurn) {
        return 0;
    }

    private static final int[] kingLocations = {
            -400, -300, -300, -300, -300, -300, -300, -400,
            -300, -250, -200, -200, -200, -200, -250, -300,
            -300, -200, -100, -100, -100, -100, -200, -300,
            -300, -200, -100,    0,    0, -100, -200, -300,
            -300, -200, -100,    0,    0, -100, -200, -300,
            -300, -200, -100, -100, -100, -100, -200, -300,
            -300, -250, -200, -200, -200, -200, -250, -300,
            -400, -300, -300, -300, -300, -300, -300, -400,
    };

    public static int evaluateKRKorKQK(Chessboard board, int strongerTurn) {
        int turn = board.turn;
        int score = 0;
        
        long myKing = board.pieces[turn][KING];
        while (myKing != 0) {
            final int kingIndex = numberOfTrailingZeros(myKing);
            score += kingLocations[63 - kingIndex];

            myKing &= myKing - 1;
        }

        long enemyKing = board.pieces[1 - turn][KING];
        while (enemyKing != 0) {
            final int kingIndex = numberOfTrailingZeros(enemyKing);
            score -= kingLocations[63 - kingIndex];

            enemyKing &= enemyKing - 1;
        }

        if (board.pieces[turn][ROOK] != 0) {
            score += 5000;
        }


        if (board.pieces[turn][QUEEN] != 0) {
            score += 10000;
        }
        
        return score;
    }
}
