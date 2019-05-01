package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.chebyshevDistance;
import static com.github.louism33.chesscore.BitOperations.manhattanDistance;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KRRK;
import static java.lang.Long.numberOfTrailingZeros;

public class EndgameKRRK {

    public static final int[] centreManhattanDistanceRR = {
            9, 8, 9, 9, 9, 9, 8, 9,
            8, 4, 4, 2, 2, 4, 4, 8,
            9, 4, 2, 1, 1, 2, 4, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 4, 2, 1, 1, 2, 4, 9,
            8, 4, 4, 2, 2, 4, 4, 8,
            9, 8, 9, 9, 9, 9, 8, 9,
    };
    
    
    public static int manFacRookRook = 0, chebFacRookRook = 1, centreFacRookRook = 2, rookRookNearEnemyKingCheb = 3, rookRookNearEnemyKingMan = 4;
    public static int[] rookRookNumbers = {
            -3, -20, 10, -11, -10
    };
    
    public static int evaluateKRRK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KRRK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            long myRooks = board.pieces[turn][ROOK];
            if (myRooks != 0) {

                Assert.assertEquals(2, BitOperations.populationCount(myRooks));

                score += 2000;

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);

                score += (rookRookNumbers[manFacRookRook] * manhattanDistance(myKingIndex, enemyKingIndex) + rookRookNumbers[chebFacRookRook] * chebyshevDistance(myKingIndex, enemyKingIndex));

                score += rookRookNumbers[centreFacRookRook] * centreManhattanDistanceRR[numberOfTrailingZeros(enemyKing)];

                while (myRooks != 0) {

                    int r = numberOfTrailingZeros(myRooks);

                    score += (rookRookNumbers[rookRookNearEnemyKingMan] * manhattanDistance(r, enemyKingIndex) + rookRookNumbers[rookRookNearEnemyKingCheb] * chebyshevDistance(r, enemyKingIndex));

                    myRooks &= myRooks - 1;
                }
            }
        }

        Assert.assertTrue(winningPlayer != -1);

        return board.turn == winningPlayer ? score : -score;
    }
}
