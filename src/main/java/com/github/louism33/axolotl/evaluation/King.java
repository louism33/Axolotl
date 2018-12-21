package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.BitboardResources;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KING_HOME_RANK;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KING_PAWN_PROTECT_BONUS;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class King {

    static int evalKingByTurn(Chessboard board, boolean white,
                              long myPawns, long myKing,
                              long allPieces) {

        return 0;
//        
//        Assert.assertEquals(1, populationCount(myKing));
//
//        int score = 0;
//
//        long emptySquares = ~board.allPieces();
//
//        long homeRank = white ? BitboardResources.RANK_ONE : BitboardResources.RANK_EIGHT;
//            
//        long myKingAttacks = PieceMove.masterAttackTableKing(board, white, 0, emptySquares, allPieces, myKing);
//
//        score += BitOperations.populationCount(myKingAttacks & myPawns) * KING_PAWN_PROTECT_BONUS;
//        
//        score += BitOperations.populationCount(myKingAttacks & homeRank) * KING_HOME_RANK;
//
//        return score;
    }

}









