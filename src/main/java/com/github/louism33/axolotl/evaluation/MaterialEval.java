package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;

class MaterialEval {

    static int evalMaterialByTurn(long myPawns, long myKnights, long myBishops, long myRooks, long myQueens){
        int score = 0;

        score += BitOperations.populationCount(myPawns) * PAWN_SCORE;
        score += BitOperations.populationCount(myKnights) * KNIGHT_SCORE;
        score += BitOperations.populationCount(myBishops) * BISHOP_SCORE;
        score += BitOperations.populationCount(myRooks) * ROOK_SCORE;
        score += BitOperations.populationCount(myQueens) * QUEEN_SCORE;

        return score;
    }

}
