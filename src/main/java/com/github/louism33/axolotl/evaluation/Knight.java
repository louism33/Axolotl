package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KNIGHT_UNDEVELOPED_PENALTY;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class Knight {

    static int evalKnightByTurn(Chessboard board, boolean white, 
                                long myPawns, long myKnights,
                                long enemyPawns) {
        
        if (myKnights == 0) {
            return 0;
        }

        int score = 0;

        score += 
                unDevelopedKnights(board, white, myKnights)
//                + knightOutpostBonus(board, white, myPawns, myKnights, enemyPawns)
//                + knightMobility(board, white, myKnights)
//                + knightProtectorAndAggressor(board, white, myKnights)
        ;

        return score;
    }

    private static int unDevelopedKnights(Chessboard board, boolean white, long myKnights){
        long originalKnights = white ? board.getWhiteKnights() : board.getBlackKnights();
        return populationCount(originalKnights & myKnights)
                * KNIGHT_UNDEVELOPED_PENALTY;
    }

//    private static int knightMobility(Chessboard board, boolean white, long myKnights){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKnights);
//        long emptySquares = ~board.allPieces();
//        long enemies = white ? board.blackPieces() : board.whitePieces();
//
//        int mobilitySquares = 0;
//        for (Integer knightIndex : indexOfAllPieces) {
//            long knight = newPieceOnSquare(knightIndex);
//            long pseudoAvailableSquares = singleKnightTable(knight, emptySquares);
//            mobilitySquares += populationCount(pseudoAvailableSquares);
//        }
//        return mobilitySquares * KNIGHT_MOBILITY_SCORE;
//    }

//    private static int knightProtectorAndAggressor(Chessboard board, boolean white, long myKnights){
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKnights);
//        long emptySquares = ~board.allPieces();
//        long myPieces = white ? board.whitePieces() : board.blackPieces();
//        long enemyPieces = white ? board.blackPieces() : board.whitePieces();
//
//        int protectedFriends = 0;
//        int threatenedEnemies = 0;
//        for (Integer knightIndex : indexOfAllPieces) {
//            long knight = newPieceOnSquare(knightIndex);
//            long pseudoAttackedOrProtectedByKnight = singleKnightTable(knight, UNIVERSE);
//
//            protectedFriends += populationCount(pseudoAttackedOrProtectedByKnight & myPieces);
//            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByKnight & enemyPieces);
//        }
//        return protectedFriends * KNIGHT_PROTECTOR_SCORE
//                + threatenedEnemies * KNIGHT_AGGRESSOR_SCORE;
//    }

//    private static int knightOutpostBonus(Chessboard board, boolean white, long myPawns, long myKnights, long enemyPawns) {
//        int score = 0;
//
//        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKnights);
//        for (int i = 0; i < indexOfAllPieces.size(); i++) {
//            Integer knightIndex = indexOfAllPieces.get(i);
//            long knight = newPieceOnSquare(knightIndex);
//            
//            /*
//            only consider outpost if they are in middle four ranks, and not on edges
//             */
//            if (((knight & noMansLand) == 0)
//                    && ((knight & boardWithoutEdges) == 0)) {
//                continue;
//            }
//            
//            /*
//            if in centre files, only consider outpost if no enemy pawns can quickly threaten our knight
//             */
//            if ((knight & northSouthHighway) != 0) {
//                if (white) {
//                    if ((((knight << 7) & enemyPawns) != 0)
//                            || (((knight << 9) & enemyPawns) != 0)
//                            || (((knight << 17) & enemyPawns) != 0)
//                            || ((knight << 18) & enemyPawns) != 0) {
//                        continue;
//                    }
//                } else {
//                    if ((((knight >>> 7) & enemyPawns) != 0)
//                            || (((knight >>> 9) & enemyPawns) != 0)
//                            || (((knight >>> 17) & enemyPawns) != 0)
//                            || ((knight >>> 18) & enemyPawns) != 0) {
//                        continue;
//                    }
//                }
//            }
//
//            long ignoreThesePieces = ~BitOperations.squareCentredOnIndex(knightIndex);
//            long pawnDefendingKnight = PieceMove.masterPawnCapturesTable(board, white, ignoreThesePieces, knight, myPawns);
//            if (pawnDefendingKnight != 0) {
//                score += KNIGHT_OUTPOST_BONUS;
//            }
//        }
//        return score;
//    }

}