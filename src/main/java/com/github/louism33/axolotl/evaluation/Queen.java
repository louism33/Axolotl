package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

class Queen {

    static int evalQueenByTurn(Chessboard board, boolean white,
                               long myRooks, long myQueens,
                               long enemyPawns) {
        
        if (myQueens == 0) {
            return 0;
        }
        return 0;
//        long emptySquares = ~board.allPieces();
//
//        int score = 0;
//
//        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
//        int numberOfQueensOnSeventh = populationCount(myQueens & seventhRank);
//        score += numberOfQueensOnSeventh * QUEEN_ON_SEVENTH_BONUS;
//
//
//        while (myQueens != 0) {
//            long queen = BitOperations.getFirstPiece(myQueens);
//
//            long pseudoAvailableSquares = PieceMove.singleQueenTable(board.allPieces(), white, queen, UNIVERSE);
//
//            score += populationCount(pseudoAvailableSquares & emptySquares) * QUEEN_MOBILITY_SCORE;
//            score += populationCount(pseudoAvailableSquares & myRooks) * QUEEN_PROTECTS_ROOK;
//            score += populationCount(pseudoAvailableSquares & enemyPawns) * QUEEN_HATES_PAWNS;
//
//            myQueens &= myQueens - 1;
//        }
//
//
//        return score;
    }
}
