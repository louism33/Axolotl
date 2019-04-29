package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.chesscore.BoardConstants.*;
import static java.lang.Long.numberOfTrailingZeros;

public class EvaluatorEndgame {

    public static int evaluateKPK(Chessboard board, int strongerTurn) {
        return 0;
    }

    private static final int[] weakKingLocations = {
            -400, -300, -300, -300, -300, -300, -300, -400,
            -300, -250, -200, -200, -200, -200, -250, -300,
            -300, -200, -100, -100, -100, -100, -200, -300,
            -300, -200, -100,    0,    0, -100, -200, -300,
            -300, -200, -100,    0,    0, -100, -200, -300,
            -300, -200, -100, -100, -100, -100, -200, -300,
            -300, -250, -200, -200, -200, -200, -250, -300,
            -400, -300, -300, -300, -300, -300, -300, -400,
    };

    private static final int[] strongKingLocations = {
            -40, -30, -30, -30, -30, -30, -30, -40,
            -30, -25, -20, -20, -20, -20, -25, -30,
            -30,  30,  40,  40,  40,  40,  30, -30,
            -30, -20,  40,   0,   0,  40, -20, -30,
            -30, -20,  40,   0,   0,  40, -20, -30,
            -30,  30,  40,  40,  40,  40,  30, -30,
            -30, -25, -20, -20, -20, -20, -25, -30,
            -40, -30, -30, -30, -30, -30, -30, -40,
    };

    public static int evaluateKRKorKQK(Chessboard board) {
        int score = 0, winningPlayer = -1;

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long myRook = board.pieces[turn][ROOK];
            final long myQueen = board.pieces[turn][QUEEN];
            if (myRook != 0 || myQueen != 0) {
                score += 1000;

                winningPlayer = turn;
                long myKing = board.pieces[turn][KING];
                long enemyKing = board.pieces[1 - turn][KING];
                final int myKingIndex = numberOfTrailingZeros(myKing);
                final int enemyKingIndex = numberOfTrailingZeros(enemyKing);

                final int i = 16 * manhattanDistance(myKingIndex, enemyKingIndex) + 8 * chebyshevDistance(myKingIndex, enemyKingIndex);
                score -= i;

                score -= weakKingLocations[numberOfTrailingZeros(enemyKing)];
////                score += strongKingLocations[63 - myKingIndex];

//                score += chebyshevDistance(enemyKingIndex, numberOfTrailingZeros(myRook)) * 10;
//
//                score += strongKingLocations[numberOfTrailingZeros(myQueen)];
//                
//                final long enemyKingPseudo = KING_MOVE_TABLE[enemyKingIndex];
//
//                long myPseudo = myRook != 0
//                        ? PieceMove.singleRookTable(0, numberOfTrailingZeros(myRook), UNIVERSE)
//                        : PieceMove.singleQueenTable(0, numberOfTrailingZeros(myQueen), UNIVERSE);
//
//                score += (populationCount(myPseudo & enemyKingPseudo)) * 10;
////                score -= weakKingLocations[63 - enemyKingIndex];
            }
        }

//        Assert.assertTrue(winningPlayer != -1);
//        System.out.println("turn : " + (board.turn == WHITE) + ", score " + (board.turn == winningPlayer ? score : -score));
        return board.turn == winningPlayer ? score : -score;
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
