package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KBNK;
import static java.lang.Long.numberOfTrailingZeros;

public class EndgameKBNK {

    public static void makeBlackKingLocations() {
        for (int i = 0; i < 64; i++) {
            weakKingLocationKBNKBlackBishop[i] = weakKingLocationKBNKWhiteBishop[MIRRORED_LEFT_RIGHT[i]];
        }
    }

    private static int[] weakKingLocationKBNKBlackBishop = new int[64];

    public static final int[] weakKingLocationKBNKWhiteBishop = {
            7, 6, 5, 4, 3, 2, 1, 0,
            6, 5, 4, 3, 2, 1, 1, 0,
            5, 4, 3, 2, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 3, 4, 5,
            1, 1, 1, 2, 3, 4, 5, 6,
            0, 1, 2, 3, 4, 5, 6, 7,
    };

    static {
        makeBlackKingLocations();
    }

    public static int manKK = 0, chebKK = 1, psqtFacKBNK = 2, bNearEnemyKMan = 3, bNearEnemyKCheb = 4, bishopNearKnight = 5, nNearEnemyKMan = 6, nNearEnemyKCheb = 7;
    public static int[] bishopKnightNumbers = {
            -3, -18, 20, -5, -10, 0, 0, 0
    };

    public static int evaluateKBNK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KBNK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            long myBishop = board.pieces[turn][BISHOP];
            if (myBishop != 0) {
                score += 6_000;

                final long myKnight = board.pieces[turn][KNIGHT];
                Assert.assertTrue(myKnight != 0);

                boolean whiteBishop = (myBishop & WHITE_COLOURED_SQUARES) != 0;

                // included in order to stop losing pieces for no reason
                int materialScore = 0;
                materialScore += populationCount(board.pieces[turn][PAWN]) * material[P];
                materialScore += populationCount(myKnight) * material[K];
                materialScore += populationCount(board.pieces[turn][BISHOP]) * material[B];
                materialScore += populationCount(board.pieces[turn][ROOK]) * material[R];
                materialScore += populationCount(board.pieces[turn][QUEEN]) * material[Q];
                score += Score.getScore(materialScore, 0);

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);
                final int bi = numberOfTrailingZeros(myBishop);
                final int kn = numberOfTrailingZeros(myKnight);

                score += (bishopKnightNumbers[bishopNearKnight] * manhattanDistance(bi, kn) + bishopKnightNumbers[bishopNearKnight] * chebyshevDistance(bi, kn));

                score += (bishopKnightNumbers[manKK] * manhattanDistance(myKingIndex, enemyKingIndex) + bishopKnightNumbers[chebKK] * chebyshevDistance(myKingIndex, enemyKingIndex));
                
                score += bishopKnightNumbers[psqtFacKBNK] *
                        (whiteBishop ?
                                weakKingLocationKBNKWhiteBishop[enemyKingIndex]
                                : weakKingLocationKBNKBlackBishop[enemyKingIndex]);



                score += (bishopKnightNumbers[bNearEnemyKMan] * manhattanDistance(bi, enemyKingIndex) + bishopKnightNumbers[bNearEnemyKCheb] * chebyshevDistance(bi, enemyKingIndex));

                score += (bishopKnightNumbers[nNearEnemyKMan] * manhattanDistance(kn, enemyKingIndex) + bishopKnightNumbers[nNearEnemyKCheb] * chebyshevDistance(kn, enemyKingIndex));

            }
        }

        Assert.assertTrue(winningPlayer != -1);

        return board.turn == winningPlayer ? score : -score;
    }


}
