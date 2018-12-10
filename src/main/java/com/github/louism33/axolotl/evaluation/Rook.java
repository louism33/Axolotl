package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.*;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.Evaluator.getFile;
import static com.github.louism33.axolotl.evaluation.Evaluator.getRow;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.*;
import static com.github.louism33.chesscore.BitboardResources.FILES;

class Rook {

    static int evalRookByTurn(Chessboard board, boolean white,
                              long myPawns, long myRooks, long myQueen,
                              long enemyPawns,
                              long friends, long enemies, long allPieces) {


        long originalRooks = white ? board.getWhiteRooks() : board.getBlackRooks();
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;

        int score = 0;

        score += (BitOperations.populationCount(seventhRank & myRooks) * ROOK_ON_SEVENTH_BONUS);

        while(myRooks != 0){
            long rook = BitOperations.getFirstPiece(myRooks);
            score += rookScore(rook, myPawns, myRooks, myQueen, enemyPawns, friends, enemies, allPieces, originalRooks, seventhRank);
            myRooks &= myRooks - 1;
        }

        return score;
    }

    private static int rookScore(long rook, long myPawns, long myRooks, long myQueen,
                                 long enemyPawns,
                                 long friends, long enemyPieces, 
                                 long allPieces,
                                 long originalRooks, long seventhRank){
        int score = 0;
        long file = getFile(rook);
        long row = getRow(rook);
        if ((file & myPawns) == 0) {
            score += ROOK_ON_SEMI_OPEN_FILE_BONUS;
        }

        if ((file & enemyPieces) == 0) {
            score += I_CONTROL_OPEN_FILE;
        }
        
        if ((file & enemyPawns) == 0) {
            score += ROOK_OPEN_FILE_BONUS;
        }

        if ((file & (myRooks - rook)) != 0) {
            score += BATTERY_SCORE;
        }

        if ((row & (myRooks - rook)) != 0) {
            score += ROOK_SAME_ROW;
        }

        if ((file & myQueen) != 0) {
            score += BATTERY_SCORE;
        }

        if ((originalRooks & rook) != 0) {
            score += ROOK_NOT_DEVELOPED;
        }

        int mobilitySquares = BitOperations.populationCount(PieceMove.singleRookTable(allPieces, true, rook, UNIVERSE));
        score += mobilitySquares * ROOK_MOBILITY_SCORE;

        return score;
    }

}
