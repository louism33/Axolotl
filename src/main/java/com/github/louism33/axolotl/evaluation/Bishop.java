package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

class Bishop {

    static int evalBishopByTurn(Chessboard board, boolean white,
                                long myPawns, long myBishops,
                                long enemyPawns,
                                long friends){

        if (myBishops == 0){
            return 0;
        }

        int score = 0;

        long emptySquares = ~board.allPieces();

        long bishopSquares = ((WHITE_COLOURED_SQUARES & myBishops) != 0) ?
                WHITE_COLOURED_SQUARES : BLACK_COLOURED_SQUARES;

        if (BitOperations.populationCount(myBishops) == 1){
            score += BitOperations.populationCount(bishopSquares & enemyPawns) * BISHOP_PER_ENEMY_PAWN_ON_COLOUR;
            score += BitOperations.populationCount(bishopSquares & friends) * BISHOP_PER_FRIENDLY_PAWN_ON_COLOUR;
        }
        else {
            score += BISHOP_DOUBLE_BONUS;
        }

        long originalBishops = white ? board.getWhiteBishops() : board.getBlackBishops();
        score += BitOperations.populationCount(originalBishops & myBishops)
                * BISHOP_UNDEVELOPED_PENALTY;

        score += populationCount(myBishops & (DIAGONAL_SW_NE | DIAGONAL_NW_SE)) * BISHOPS_PRIME_DIAGONAL_BONUS;


        while (myBishops != 0){
            long bishop = BitOperations.getFirstPiece(myBishops);

            long pseudoAvailableSquares = PieceMove.singleBishopTable(board.allPieces(), white, bishop, emptySquares);
            score += populationCount(pseudoAvailableSquares) * BISHOP_MOBILITY_SCORE;



            if (((bishop & noMansLand) != 0)
                    && ((bishop & boardWithoutEdges) != 0)) {

                if ((bishop & (northSouthHighway | FILE_C | FILE_F)) != 0) {
                    if (white) {
                        if ((((bishop << 7) & enemyPawns) != 0)
                                || (((bishop << 9) & enemyPawns) != 0)
                                || (((bishop << 17) & enemyPawns) != 0)
                                || ((bishop << 18) & enemyPawns) != 0) {
                            myBishops &= myBishops - 1;
                            continue;
                        }
                        score += BISHOP_ADVANCED_BONUS;
                        if ((((bishop >> 7) & myPawns) != 0) || ((bishop >> 9) & myPawns) != 0){
                            score += BISHOP_OUTPOST_BONUS;
                        }

                    } else {
                        if ((((bishop >>> 7) & enemyPawns) != 0)
                                || (((bishop >>> 9) & enemyPawns) != 0)
                                || (((bishop >>> 17) & enemyPawns) != 0)
                                || ((bishop >>> 18) & enemyPawns) != 0) {
                            myBishops &= myBishops - 1;
                            continue;
                        }
                        score += BISHOP_ADVANCED_BONUS;
                        if ((((bishop << 7) & myPawns) != 0) || ((bishop << 9) & myPawns) != 0){
                            score += BISHOP_OUTPOST_BONUS;
                        }
                    }
                }
            }
            myBishops &= myBishops - 1;
        }

        return score;
    }


}
