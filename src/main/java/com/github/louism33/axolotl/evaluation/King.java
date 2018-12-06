package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;
import org.junit.Assert;

import java.util.List;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KING_AGGRESSOR_SCORE;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KING_PROTECTOR_SCORE;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class King {

    static int evalKingByTurn(Chessboard board, boolean white, 
                              long myKing, 
                              long enemies, long friends, long allPieces) {

        Assert.assertEquals(1, populationCount(myKing));

        int score = 0;

        score += kingSafetyBonus(board, white, myKing)
                + kingProtectorAndAggressor(board, white, myKing, friends, enemies, allPieces)
        ;

        return score;
    }

    private static int kingSafetyBonus(Chessboard board, boolean white, long myKing){
        int score = 0;
        return score;
    }

    private static int kingProtectorAndAggressor(Chessboard board, boolean white, 
                                                 long myKing, 
                                                 long friends, long enemies, long allPieces){
        
        int kingIndex = BitOperations.getIndexOfFirstPiece(myKing);
        
        int protectedFriends = 0;
        int threatenedEnemies = 0;
        
        long king = BitOperations.newPieceOnSquare(kingIndex);
        long pseudoAttackedOrProtectedByKing = PieceMove.singleKingTable(king, board.allPieces());

        protectedFriends += populationCount(pseudoAttackedOrProtectedByKing & friends);
        threatenedEnemies += populationCount(pseudoAttackedOrProtectedByKing & enemies);

        return protectedFriends * KING_PROTECTOR_SCORE
                + threatenedEnemies * KING_AGGRESSOR_SCORE;
    }
}










