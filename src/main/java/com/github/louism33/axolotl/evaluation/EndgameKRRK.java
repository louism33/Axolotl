package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.material;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.*;
import static java.lang.Long.numberOfTrailingZeros;

public class EndgameKRRK {

    public static final int[] weakKingLocationKRRK = {
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

        int[] allowedGames = {KQQK, KQRK, KRRK};

        Assert.assertTrue(contains(allowedGames, board.typeOfGameIAmIn));

        for (int turn = WHITE; turn <= BLACK; turn++) {
            long myPieces = board.pieces[turn][ROOK] | board.pieces[turn][QUEEN];
            if (myPieces != 0) {

                score += 10_000;

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

                score += (rookRookNumbers[manFacRookRook] * manhattanDistance(myKingIndex, enemyKingIndex) + rookRookNumbers[chebFacRookRook] * chebyshevDistance(myKingIndex, enemyKingIndex));

                score += rookRookNumbers[centreFacRookRook] * weakKingLocationKRRK[numberOfTrailingZeros(enemyKing)];

                while (myPieces != 0) {

                    int r = numberOfTrailingZeros(myPieces);

                    score += (rookRookNumbers[rookRookNearEnemyKingMan] * manhattanDistance(r, enemyKingIndex) + rookRookNumbers[rookRookNearEnemyKingCheb] * chebyshevDistance(r, enemyKingIndex));

                    myPieces &= myPieces - 1;
                }
            }
        }

        Assert.assertTrue(winningPlayer != -1);

        return board.turn == winningPlayer ? score : -score;
    }
}
