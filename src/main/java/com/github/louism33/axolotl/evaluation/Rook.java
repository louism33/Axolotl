package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.BitboardResources;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import java.util.List;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BitboardResources.RANK_SEVEN;
import static com.github.louism33.chesscore.BitboardResources.RANK_TWO;

class Rook {

    static int evalRookByTurn(Chessboard board, boolean white, 
                              long myPawns, long myRooks,
                              long enemyPawns) {

        if (myRooks == 0) {
            return 0;
        }

        int score = 0;

        score += 
//                unDevelopedRooks(board, white, myRooks)
                + rookOnSeventhRank(board, white, myRooks)
//                + rookMobility(board, white, myRooks)
//                + rookProtectorAndAggressor(board, white, myRooks)
                + rookOnOpenFile(board, white, myRooks, myPawns, enemyPawns)
//                + rookHelpsQueensAndRooks(board, white, myRooks)
        ;

        return score;
    }

//    private static int rookHelpsQueensAndRooks(Chessboard board, boolean white, long myRooks){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myRooks);
//        long myQueens = white ? board.getWhiteQueen() : board.getBlackQueen();
//        long emptySquares = ~board.allPieces();
//
//        int score = 0;
//        for (Integer rookIndex : indexOfAllPieces) {
//            long queen = newPieceOnSquare(rookIndex);
//            long pseudoAvailableSquares = PieceMove.singleRookTable(board.allPieces(), white, queen, myRooks | myQueens);
//            score += populationCount(pseudoAvailableSquares) * ROOK_PROTECTS_QUEEN;
//        }
//        return score;
//    }

    private static int rookOnOpenFile(Chessboard board, boolean white, long myRooks, long myPawns, long enemyPawns){
        int fileScore = 0;
        long[] files = BitboardResources.FILES;
        for (int i = 0; i < files.length; i++) {
            long file = files[i];
            if ((file & myPawns) != 0) {
                continue;
            }
            if ((file & enemyPawns) != 0) {
                fileScore += ROOK_ON_SEMI_OPEN_FILE_BONUS;
                continue;
            }
            fileScore += ROOK_OPEN_FILE_BONUS;
        }
        return fileScore;
    }

    private static int unDevelopedRooks(Chessboard board, boolean white, long myRooks){
        long originalRooks = white ? board.getWhiteRooks() : board.getBlackRooks();
        return populationCount(originalRooks & myRooks)
                * ROOK_NOT_DEVELOPED;
    }


//    private static int rookMobility(Chessboard board, boolean white, long myRooks){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myRooks);
//        long emptySquares = ~board.allPieces();
//        long enemies = white ? board.blackPieces() : board.whitePieces();
//
//        int mobilitySquares = 0;
//        for (Integer rookIndex : indexOfAllPieces) {
//            long rook = newPieceOnSquare(rookIndex);
//            long pseudoAvailableSquares = PieceMove.singleRookTable(board.allPieces(), white, rook, emptySquares);
//            mobilitySquares += populationCount(pseudoAvailableSquares);
//        }
//        return mobilitySquares * ROOK_MOBILITY_SCORE;
//    }
//
//    private static int rookProtectorAndAggressor(Chessboard board, boolean white, long myRooks){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myRooks);
//        long emptySquares = ~board.allPieces();
//        long myPieces = white ? board.whitePieces() : board.blackPieces();
//        long enemyPieces = white ? board.blackPieces() : board.whitePieces();
//
//        int protectedFriends = 0;
//        int threatenedEnemies = 0;
//        for (Integer rookIndex : indexOfAllPieces) {
//            long rook = newPieceOnSquare(rookIndex);
//            long pseudoAttackedOrProtectedByRook = PieceMove.singleRookTable(board.allPieces(), white, rook, board.allPieces());
//
//            protectedFriends += populationCount(pseudoAttackedOrProtectedByRook & myPieces);
//            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByRook & enemyPieces);
//        }
//        return protectedFriends * ROOK_PROTECTOR_SCORE
//                + threatenedEnemies * ROOK_AGGRESSOR_SCORE;
//    }

    private static int rookOnSeventhRank(Chessboard board, boolean white, long myRooks){
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
        int numberOfRooksOnSeventh = BitOperations.populationCount(myRooks & seventhRank);

        return numberOfRooksOnSeventh > 1 ? (1 + numberOfRooksOnSeventh) * ROOK_ON_SEVENTH_BONUS
                : numberOfRooksOnSeventh * ROOK_ON_SEVENTH_BONUS;
    }
}
