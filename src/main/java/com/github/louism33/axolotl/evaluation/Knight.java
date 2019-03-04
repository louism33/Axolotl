package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

class Knight {

    static int evalKnightByTurn(Chessboard board, boolean white,
                                long myPawns, long myKnights,
                                long enemyPawns, long enemyRooks, long enemyQueens, long enemyKing) {

        if (myKnights == 0) {
            return 0;
        }
        return 0;
//        
//        long emptySquares = ~board.allPieces();
//        int score = 0;
//
//        while (myKnights != 0){
//            long knight = BitOperations.getFirstPiece(myKnights);
//            int knightIndex = BitOperations.getIndexOfFirstPiece(knight);
//
//            long pseudoAvailableSquares = PieceMove.singleKnightTable(knight, UNIVERSE);
//
//            int bigThreats = populationCount(pseudoAvailableSquares
//                    & (enemyQueens | enemyRooks | enemyKing));
//            if (bigThreats > 0){
//                score += KNIGHT_THREATEN_BIG;
//            }
//            if (bigThreats > 1){
//                score += KNIGHT_FORK;
//            }
//
//            if (((knight & noMansLand) != 0)
//                    && ((knight & boardWithoutEdges) != 0)) {
//
//                if ((knight & (northSouthHighway | FILE_C | FILE_F)) != 0) {
//                    if (white && ((knightIndex > 23))) {
//                        long enemyPawnKillZone = blackPawnKillZone[knightIndex] | blackPawnKillZone[knightIndex + 8];
//                        if ((enemyPawnKillZone & enemyPawns) != 0) {
//                            myKnights &= myKnights - 1;
//                            continue;
//                        }
//                        score += KNIGHT_ADVANCED_BONUS;
//                        if ((((knight >> 7) & myPawns) != 0) || ((knight >> 9) & myPawns) != 0){
//                            score += KNIGHT_OUTPOST_BONUS;
//                        }
//
//                    } else if (!white && knightIndex < 40) {
//                        long enemyPawnKillZone = whitePawnKillZone[knightIndex] | whitePawnKillZone[knightIndex - 8];
//                        if ((enemyPawnKillZone & enemyPawns) != 0) {
//                            myKnights &= myKnights - 1;
//                            continue;
//                        }
//                        score += KNIGHT_ADVANCED_BONUS;
//                        if ((((knight << 7) & myPawns) != 0) || ((knight << 9) & myPawns) != 0){
//                            score += KNIGHT_OUTPOST_BONUS;
//                        }
//                    }
//                }
//            }
//
//            myKnights &= myKnights - 1;
//        }
//
//        return score;
    }

}