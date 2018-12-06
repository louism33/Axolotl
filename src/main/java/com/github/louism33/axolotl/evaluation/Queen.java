package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.QUEEN_ON_SEVENTH_BONUS;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.RANK_SEVEN;
import static com.github.louism33.chesscore.BitboardResources.RANK_TWO;

class Queen {

    static int evalQueenByTurn(Chessboard board, boolean white, long myRooks, long myQueen) {
        long myQueens = white ? board.getWhiteQueen() : board.getBlackQueen();

        if (myQueens == 0) {
            return 0;
        }

        int score = 0;

        score += queenOnSeventhRank(board, white, myQueens)
//                + queenMobility(board, white, myQueens)
//                + queenProtectorAndAggressor(board, white, myQueens)
//                + queensHelpRooksAndQueens(board, white, myQueens)
        ;

        return score;
    }

//    private static int queensHelpRooksAndQueens(Chessboard board, boolean white, long myQueens){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myQueens);
//        long myRooks = white ? board.getWhiteRooks() : board.getBlackRooks();
//        long emptySquares = ~board.allPieces();
//
//        int score = 0;
//        for (Integer queenIndex : indexOfAllPieces) {
//            long queen = BitOperations.newPieceOnSquare(queenIndex);
//            long pseudoAvailableSquares = PieceMove.singleQueenTable(board.allPieces(), white, queen, myRooks | myQueens);
//            score += populationCount(pseudoAvailableSquares) * QUEEN_PROTECTS_ROOK;
//        }
//        return score;
//    }

//    private static int queenMobility(Chessboard board, boolean white, long myQueens){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myQueens);
//        long emptySquares = ~board.allPieces();
//
//        int mobilitySquares = 0;
//        for (Integer queenIndex : indexOfAllPieces) {
//            long queen = BitOperations.newPieceOnSquare(queenIndex);
//            long pseudoAvailableSquares = PieceMove.singleQueenTable(board.allPieces(), white, queen, emptySquares);
//            mobilitySquares += populationCount(pseudoAvailableSquares);
//        }
//        return mobilitySquares * QUEEN_MOBILITY_SCORE;
//    }
//
//    private static int queenProtectorAndAggressor(Chessboard board, boolean white, long myQueens){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myQueens);
//        long emptySquares = ~board.allPieces();
//        long myPieces = white ? board.whitePieces() : board.blackPieces();
//        long enemyPieces = white ? board.blackPieces() : board.whitePieces();
//
//        int protectedFriends = 0;
//        int threatenedEnemies = 0;
//        for (Integer queenIndex : indexOfAllPieces) {
//            long queen = BitOperations.newPieceOnSquare(queenIndex);
//            long pseudoAttackedOrProtectedByQueen = PieceMove.singleQueenTable(board.allPieces(), white, queen, board.allPieces());
//
//            protectedFriends += populationCount(pseudoAttackedOrProtectedByQueen & myPieces);
//            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByQueen & enemyPieces);
//        }
//        return protectedFriends * QUEEN_PROTECTOR_SCORE
//                + threatenedEnemies * QUEEN_AGGRESSOR_SCORE;
//    }


    private static int queenOnSeventhRank(Chessboard board, boolean white, long myQueens){
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
        int numberOfQueensOnSeventh = populationCount(myQueens & seventhRank);
        return numberOfQueensOnSeventh * QUEEN_ON_SEVENTH_BONUS;
    }
}
