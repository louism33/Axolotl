package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.chebyshevDistance;
import static com.github.louism33.chesscore.BitOperations.manhattanDistance;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KQK;
import static java.lang.Long.numberOfTrailingZeros;

public class EndgameKQK {

    public static final int[] centreManhattanDistanceQ = {
            9, 9, 9, 9, 9, 9, 9, 9,
            9, 4, 3, 2, 2, 3, 4, 9,
            9, 3, 2, 1, 1, 2, 3, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 3, 2, 1, 1, 2, 3, 9,
            9, 4, 3, 2, 2, 3, 4, 9,
            9, 9, 9, 9, 9, 9, 9, 9,
    };

    public static int manFacQueen = 0, chebFacQueen = 1, centreFacQueen = 2;
    public static int[] queenNumbers = {
            -229, -29, 44,
    };
    
    public static int evaluateKQK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KQK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myQueen = board.pieces[turn][QUEEN];
            if (myQueen != 0) {
                score += 3000;

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);

                score += (queenNumbers[manFacQueen] * manhattanDistance(myKingIndex, enemyKingIndex) + queenNumbers[chebFacQueen] * chebyshevDistance(myKingIndex, enemyKingIndex));
                score += queenNumbers[centreFacQueen] * centreManhattanDistanceQ[numberOfTrailingZeros(enemyKing)];
            }
        }

        Assert.assertTrue(winningPlayer != -1);
        return board.turn == winningPlayer ? score : -score;
    }
}
