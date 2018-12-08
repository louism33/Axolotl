package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.*;

class Bishop {

    static int evalBishopByTurn(Chessboard board, boolean white, 
                                long myPawns, long myBishops, 
                                long enemyPawns){
        
        if (myBishops == 0){
            return 0;
        }

        int score = 0;
        if (populationCount(myBishops) == 1){
            long bishopSquares = ((WHITE_COLOURED_SQUARES & myBishops) != 0) ?
                    WHITE_COLOURED_SQUARES : BLACK_COLOURED_SQUARES;

            score += bishopEnemyPawnColourScore(myBishops, enemyPawns, bishopSquares)
                    + bishopFriendlyPawnColourScore(board, white, myBishops, myPawns, bishopSquares)
            ;
        }

        score += unDevelopedBishops(board, white, myBishops)
//                + bishopMobility(board, white, myBishops)
//                + bishopProtectorAndAggressor(board, white, myBishops)
                + doubleBishopScore(myBishops)
//                + bishopOutpostBonus(board, white, myBishops, enemyPawns)
                + primeDiagonals(board, white, myBishops)
        ;

        return score;
    }

    private static int unDevelopedBishops(Chessboard board, boolean white, long myBishops){
        long originalBishops = white ? board.getWhiteBishops() : board.getBlackBishops();
        return BitOperations.populationCount(originalBishops & myBishops)
                * BISHOP_UNDEVELOPED_PENALTY;
    }

    private static int primeDiagonals(Chessboard board, boolean white, long myBishops){
        return populationCount(myBishops & (DIAGONAL_SW_NE | DIAGONAL_NW_SE)) * BISHOPS_PRIME_DIAGONAL_BONUS;
    }

//    private static int bishopMobility(Chessboard board, boolean white, long myBishops){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myBishops);
//        long emptySquares = ~board.allPieces();
//        long enemies = white ? board.blackPieces() : board.whitePieces();
//
//        int mobilitySquares = 0;
//        for (Integer bishopIndex : indexOfAllPieces) {
//            long bishop = newPieceOnSquare(bishopIndex);
//            long pseudoAvailableSquares = PieceMove.singleBishopTable(board.allPieces(), white, bishop, emptySquares);
//            mobilitySquares += populationCount(pseudoAvailableSquares);
//        }
//        return mobilitySquares * BISHOP_MOBILITY_SCORE;
//    }

//    private static int bishopProtectorAndAggressor(Chessboard board, boolean white, long myBishops){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myBishops);
//        long emptySquares = ~board.allPieces();
//        long myPieces = white ? board.whitePieces() : board.blackPieces();
//        long enemyPieces = white ? board.blackPieces() : board.whitePieces();
//
//        int protectedFriends = 0;
//        int threatenedEnemies = 0;
//        for (Integer bishopIndex : indexOfAllPieces) {
//            long bishop = newPieceOnSquare(bishopIndex);
//            long pseudoAttackedOrProtectedByBishop = PieceMove.singleBishopTable(board.allPieces(), white, bishop, board.allPieces());
//
//            protectedFriends += populationCount(pseudoAttackedOrProtectedByBishop & myPieces);
//            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByBishop & enemyPieces);
//        }
//        return protectedFriends * BISHOP_PROTECTOR_SCORE
//                + threatenedEnemies * BISHOP_AGGRESSOR_SCORE;
//    }

//    private static int bishopOutpostBonus(Chessboard board, boolean white, long myBishops, long enemyPawns) {
//        int score = 0;
//
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myBishops);
//        for (int i = 0; i < indexOfAllPieces.size(); i++) {
//            Integer bishopIndex = indexOfAllPieces.get(i);
//            long bishop = newPieceOnSquare(bishopIndex);
//            
//            /*
//            only consider outpost if they are in middle four ranks, and not on edges
//             */
//            if (((bishop & noMansLand) == 0)
//                    && ((bishop & boardWithoutEdges) == 0)) {
//                continue;
//            }
//            
//            /*
//            if in centre files, only consider outpost if no enemy pawns can quickly threaten our bishop
//             */
//            if ((bishop & northSouthHighway) != 0) {
//                if (white) {
//                    if ((((bishop << 7) & enemyPawns) != 0)
//                            || (((bishop << 9) & enemyPawns) != 0)
//                            || (((bishop << 17) & enemyPawns) != 0)
//                            || ((bishop << 18) & enemyPawns) != 0) {
//                        continue;
//                    }
//                } else {
//                    if ((((bishop >>> 7) & enemyPawns) != 0)
//                            || (((bishop >>> 9) & enemyPawns) != 0)
//                            || (((bishop >>> 17) & enemyPawns) != 0)
//                            || ((bishop >>> 18) & enemyPawns) != 0) {
//                        continue;
//                    }
//                }
//            }
//
//            long ignoreThesePieces = ~BitOperations.squareCentredOnIndex(bishopIndex);
//            long pawnDefendingBishop = PieceMove.masterPawnCapturesTable(board, white, ignoreThesePieces, 
//                    white ? board.getWhitePawns() : board.getBlackPawns(), bishop);
//            if (pawnDefendingBishop != 0) {
//                score += BISHOP_OUTPOST_BONUS;
//            }
//        }
//        return score;
//    }



    private static int doubleBishopScore (long bishops){
        return populationCount(bishops) > 1 ? BISHOP_DOUBLE_BONUS : 0;
    }

    private static int bishopEnemyPawnColourScore(long myBishop, long enemyPawns, long bishopSquares){
        return (populationCount(enemyPawns & ~bishopSquares)
                - populationCount(enemyPawns & bishopSquares))
                * BISHOP_PER_ENEMY_PAWN_ON_COLOUR;
    }

    private static int bishopFriendlyPawnColourScore(Chessboard board, boolean white,
                                                     long myBishop, long friendlyPawns, long bishopSquares){


        /*
        if not winning, prefer my pawns on same square as my bishop
         */
        int score = (populationCount(friendlyPawns & bishopSquares)
                - populationCount(friendlyPawns & ~bishopSquares));

        /*
        if winning, prefer bishop mobility over pawn protection
         */
        if (iAmWinningUgly(board, white)){
            score *= -1;
        }
        return score * BISHOP_PER_FRIENDLY_PAWN_ON_COLOUR;

    }

    private static boolean iAmWinningUgly (Chessboard board, boolean white){
        /*
        rough guide to whether I am ahead or not
         */
        if (white){
            return populationCount(board.whitePieces()) > board.blackPieces() + 3;
        }
        else {
            return populationCount(board.blackPieces()) > board.whitePieces() + 3;
        }
    }

}
