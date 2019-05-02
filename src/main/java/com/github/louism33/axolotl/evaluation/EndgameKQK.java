package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.material;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KQK;
import static java.lang.Long.numberOfTrailingZeros;

public class EndgameKQK {

    public static final int[] weakKingLocationKQK = {
            14, 14, 14, 4, 4, 14, 14, 14,
            14, 3, 3, 2, 2, 3, 3, 14,
            14, 3, 2, 1, 1, 2, 3, 14,
            4, 2, 1, 0, 0, 1, 2, 4,
            4, 2, 1, 0, 0, 1, 2, 4,
            14, 3, 2, 1, 1, 2, 3, 14,
            14, 3, 3, 2, 2, 3, 3, 14,
            14, 14, 14, 4, 4, 14, 14, 14,
    };

    public static int manFacQueen = 0, chebFacQueen = 1, centreFacQueen = 2, queenNearKingMan = 3, queenNearKingCheb = 4;
    public static int[] queenNumbers = {
            -224, -24, 44, 0, -5
    };
    
    public static int evaluateKQK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KQK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myQueen = board.pieces[turn][QUEEN];
            if (myQueen != 0) {
                score += 9_000;

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
                final int myQueenIndex = numberOfTrailingZeros(myQueen);
                
                score += (queenNumbers[manFacQueen] * manhattanDistance(myKingIndex, enemyKingIndex) + queenNumbers[chebFacQueen] * chebyshevDistance(myKingIndex, enemyKingIndex));
                score += queenNumbers[centreFacQueen] * weakKingLocationKQK[enemyKingIndex];

                score += (queenNumbers[queenNearKingMan] * manhattanDistance(myQueenIndex, enemyKingIndex) + queenNumbers[queenNearKingCheb] * chebyshevDistance(myQueenIndex, enemyKingIndex));
            }
        }

        Assert.assertTrue(winningPlayer != -1);
        return board.turn == winningPlayer ? score : -score;
    }
}
