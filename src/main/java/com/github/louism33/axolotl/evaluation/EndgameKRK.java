package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.chebyshevDistance;
import static com.github.louism33.chesscore.BitOperations.manhattanDistance;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KRK;
import static java.lang.Long.numberOfTrailingZeros;

public class EndgameKRK {


    public static final int[] centreManhattanDistanceR = {
            9, 8, 10, 9, 9, 10, 8, 9,
            8, 4, 4, 2, 2, 4, 4, 8,
            10, 4, 2, 1, 1, 2, 4, 10,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            10, 4, 2, 1, 1, 2, 4, 10,
            8, 4, 4, 2, 2, 4, 4, 8,
            9, 8, 10, 9, 9, 10, 8, 9,
    };


    public static int manFacRook = 0, chebFacRook = 1, centreFacRook = 2;
    public static int[] rookNumbers = {
            -3, -21, 10,
    };

    public static int evaluateKRK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KRK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myRook = board.pieces[turn][ROOK];
            if (myRook != 0) {
                score += 1000;

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);

                score += (rookNumbers[manFacRook] * manhattanDistance(myKingIndex, enemyKingIndex) + rookNumbers[chebFacRook] * chebyshevDistance(myKingIndex, enemyKingIndex));
                score += rookNumbers[centreFacRook] * centreManhattanDistanceR[numberOfTrailingZeros(enemyKing)];
            }
        }

        Assert.assertTrue(winningPlayer != -1);

        return board.turn == winningPlayer ? score : -score;
    }

    
}
