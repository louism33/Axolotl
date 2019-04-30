package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KQK;
import static java.lang.Long.numberOfTrailingZeros;

public class EvaluatorEndgame {

    public static int evaluateKPK(Chessboard board, int strongerTurn) {
        return 0;
    }

    private static final int[] centreManhattanDistance = {
            6, 5, 4, 3, 3, 4, 5, 6,
            5, 4, 3, 2, 2, 3, 4, 5,
            4, 3, 2, 1, 1, 2, 3, 4,
            3, 2, 1, 0, 0, 1, 2, 3,
            3, 2, 1, 0, 0, 1, 2, 3,
            4, 3, 2, 1, 1, 2, 3, 4,
            5, 4, 3, 2, 2, 3, 4, 5,
            6, 5, 4, 3, 3, 4, 5, 6
    };

    private static final int[] centreManhattanDistanceQ = {
            9, 9, 9, 9, 9, 9, 9, 9,
            9, 4, 3, 2, 2, 3, 4, 9,
            9, 3, 2, 1, 1, 2, 3, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 2, 1, 0, 0, 1, 2, 9,
            9, 3, 2, 1, 1, 2, 3, 9,
            9, 4, 3, 2, 2, 3, 4, 9,
            9, 9, 9, 9, 9, 9, 9, 9
    };

    public static int manFacRook = 78, chebFacRook = 78, centreFacRook = 124;
    
    public static int evaluateKRK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KQK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myQueen = board.pieces[turn][QUEEN];
            if (myQueen != 0) {
                score += 2000;

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);

                score -= (manFacRook * manhattanDistance(myKingIndex, enemyKingIndex) + chebFacRook * chebyshevDistance(myKingIndex, enemyKingIndex));
                score += centreFacRook * centreManhattanDistanceQ[numberOfTrailingZeros(enemyKing)];
            }
        }

        return board.turn == winningPlayer ? score : -score;
    }

    
    public static int manFacQueen = 78, chebFacQueen = 78, centreFacQueen = 124;
    
    public static int evaluateKQK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        Assert.assertEquals(KQK, board.typeOfGameIAmIn);

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myQueen = board.pieces[turn][QUEEN];
            if (myQueen != 0) {
                score += 2000;

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);

                score -= (manFacQueen * manhattanDistance(myKingIndex, enemyKingIndex) + chebFacQueen * chebyshevDistance(myKingIndex, enemyKingIndex));
                score += centreFacQueen * centreManhattanDistanceQ[numberOfTrailingZeros(enemyKing)];
            }
        }

        return board.turn == winningPlayer ? score : -score;
    }



    public static int evaluateKRRK(Chessboard board) {
        int score = 0, winningPlayer = -1;
        return 0;
    }

    public static int chebyshevDistance(int index1, int index2) {
        final int file1 = index1 & 7;
        final int file2 = index2 & 7;
        final int rank1 = index1 / 8;
        final int rank2 = index2 / 8;
        return Math.min(Math.abs(rank1 - rank2), Math.abs(file1 - file2)) + Math.abs(Math.abs(rank1 - rank2) - Math.abs(file1 - file2));
    }


    public static int manhattanDistance(int index1, int index2) {
        final int file1 = index1 & 7;
        final int file2 = index2 & 7;
        final int rank1 = index1 / 8;
        final int rank2 = index2 / 8;
        return Math.abs(rank1 - rank2) + Math.abs(file1 - file2);
    }
}
