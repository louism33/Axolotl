package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.*;

class Knight {

    static int evalKnightByTurn(int[] moves, Chessboard board, boolean white,
                                long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                long enemies, long friends, long allPieces,
                                long pinnedPieces, boolean inCheck) {

        if (myKnights == 0) {
            return 0;
        }
        long emptySquares = ~board.allPieces();
        int score = 0;

        long originalKnights = white ? board.getWhiteKnights() : board.getBlackKnights();
        score += populationCount(originalKnights & myKnights)
                * KNIGHT_UNDEVELOPED_PENALTY;


        while (myKnights != 0){
            long knight = BitOperations.getFirstPiece(myKnights);

            long pseudoAvailableSquares = PieceMove.singleKnightTable(knight, UNIVERSE);
            score += populationCount(pseudoAvailableSquares & emptySquares) * KNIGHT_MOBILITY_SCORE;

            int bigThreats = populationCount(pseudoAvailableSquares
                    & (enemyQueens | enemyRooks | enemyKing));
            if (bigThreats > 0){
                score += KNIGHT_THREATEN_BIG;
            }
            if (bigThreats > 1){
                score += KNIGHT_FORK;
            }

            if (((knight & noMansLand) != 0)
                    && ((knight & boardWithoutEdges) != 0)) {

                if ((knight & (northSouthHighway | FILE_C | FILE_F)) != 0) {
                    if (white) {
                        if ((((knight << 7) & enemyPawns) != 0)
                                || (((knight << 9) & enemyPawns) != 0)
                                || (((knight << 17) & enemyPawns) != 0)
                                || ((knight << 18) & enemyPawns) != 0) {
                            myKnights &= myKnights - 1;
                            continue;
                        }
                        score += KNIGHT_ADVANCED_BONUS;
                        if ((((knight >> 7) & myPawns) != 0) || ((knight >> 9) & myPawns) != 0){
                            score += KNIGHT_OUTPOST_BONUS;
                        }
                        
                    } else {
                        if ((((knight >>> 7) & enemyPawns) != 0)
                                || (((knight >>> 9) & enemyPawns) != 0)
                                || (((knight >>> 17) & enemyPawns) != 0)
                                || ((knight >>> 18) & enemyPawns) != 0) {
                            myKnights &= myKnights - 1;
                            continue;
                        }
                        score += KNIGHT_ADVANCED_BONUS;
                        if ((((knight << 7) & myPawns) != 0) || ((knight << 9) & myPawns) != 0){
                            score += KNIGHT_OUTPOST_BONUS;
                        }
                    }
                }
            }

            myKnights &= myKnights - 1;
        }

        return score;
    }

}