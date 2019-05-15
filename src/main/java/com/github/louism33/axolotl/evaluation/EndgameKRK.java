package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KRK;
import static java.lang.Long.numberOfTrailingZeros;

public final class EndgameKRK {


    public static final int[] weakKingLocationKRK = {
            14, 8, 10, 9, 9, 10, 8, 14,
            8, 4, 4, 2, 2, 4, 4, 8,
            10, 4, 2, 6, 6, 2, 4, 10,
            9, 2, 6, 0, 0, 6, 2, 9,
            9, 2, 6, 0, 0, 6, 2, 9,
            10, 4, 2, 6, 6, 2, 4, 10,
            8, 4, 4, 2, 2, 4, 4, 8,
            14, 8, 10, 9, 9, 10, 8, 14,
    };


    public static int manFacRook = 0, chebFacRook = 1, centreFacRook = 2, rookNearEnemyKingMan = 3, rookNearEnemyKingCheb = 4;
    public static int[] rookNumbers = {
            -3, -18, 20, -5, -10
    };

    public static int evaluateKRK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KRK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myRook = board.pieces[turn][ROOK];
            if (myRook != 0) {
                score += 8_000;


                // included in order to stop losing pieces for no reason
                int materialScore = 0;
                materialScore += populationCount(board.pieces[turn][PAWN]) * material[P];
                materialScore += populationCount(board.pieces[turn][KNIGHT]) * material[K];
                materialScore += populationCount(board.pieces[turn][BISHOP]) * material[B];
                materialScore += populationCount(board.pieces[turn][ROOK]) * material[R];
                materialScore += populationCount(board.pieces[turn][QUEEN]) * material[Q];
                score += Score.getScore(materialScore, 0);
                
                
                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);
                final int myRookIndex = numberOfTrailingZeros(myRook);

                score += (rookNumbers[manFacRook] * manhattanDistance(myKingIndex, enemyKingIndex) + rookNumbers[chebFacRook] * chebyshevDistance(myKingIndex, enemyKingIndex));
                score += rookNumbers[centreFacRook] * weakKingLocationKRK[enemyKingIndex];

                score += (rookNumbers[rookNearEnemyKingMan] * manhattanDistance(myRookIndex, enemyKingIndex) + rookNumbers[rookNearEnemyKingCheb] * chebyshevDistance(myRookIndex, enemyKingIndex));
            }
        }

        Assert.assertTrue(winningPlayer != -1);

        Assert.assertTrue(Math.abs(score) < CHECKMATE_ENEMY_SCORE_MAX_PLY);
return board.turn == winningPlayer ? score : -score;
    }

    
}
