package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KING_PAWN_PROTECT_BONUS;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class King {

    static int evalKingByTurn(int[] moves, Chessboard board, boolean white,
                              long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                              long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                              long enemies, long friends, long allPieces,
                              long pinnedPieces, boolean inCheck) {

        Assert.assertEquals(1, populationCount(myKing));

        int score = 0;

        long emptySquares = ~board.allPieces();

        long myKingAttacks = PieceMove.masterAttackTableKing(board, white, 0, emptySquares, allPieces, myKing);

        score += BitOperations.populationCount(myKingAttacks & myPawns) * KING_PAWN_PROTECT_BONUS;

        return score;
    }

}









