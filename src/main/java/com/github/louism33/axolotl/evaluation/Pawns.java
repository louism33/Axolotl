package com.github.louism33.axolotl.evaluation;


import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.*;

class Pawns {

    static int evalPawnsByTurn(Chessboard board, boolean white, 
                               long myPawns, 
                               long enemyPawns) {
        
        if (myPawns == 0) {
            return 0;
        }

        int score = 0;

        score += 0
//                + pawnCentreBonus(board, white, myPawns)
//                + pawnOnOpenFile(board, white, myPawns, enemyPawns)
//                + pawnStructureBonus(board, white, myPawns)
//                + pawnsThreatenBigThings(board, white, myPawns)
//                + pawnsChainBonus(board, white, myPawns)
//                + doublePawnPenalty(board, white, myPawns)
//                + pawnAttackingCentreBonus(board, white, myPawns)
//                + superAdvancedPawn(board, white, myPawns)
//                + backwardsPawn(board, white, myPawns)
//                + blockedPawnPenalty(board, white, myPawns, enemyPawns)
//                + isolatedPawn(board, white, myPawns)
//                + passedPawn(board, white, myPawns, enemyPawns)
        ;

        return score;
    }

//    private static int passedPawn (Chessboard board, boolean white, long myPawns, long enemyPawns) {
//        int score = 0;
//        if (white) {
//            int numberOfAdvancedPawns = populationCount(myPawns & RANK_SIX);
//            if (numberOfAdvancedPawns > 0) {
//                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_SIX);
//                for (Integer pawnIndex : indexOfAllPieces) {
//                    long pawn = newPieceOnSquare(pawnIndex);
//                    long blockingSquare = pawn << 8;
//                    long killerSquareL = pawn << 9;
//                    long killerSquareR = pawn << 7;
//
//                    long spotsToBeEmpty;
//                    if ((pawn & FILE_A) != 0){
//                        spotsToBeEmpty = blockingSquare | killerSquareR;
//                    }
//                    else if ((pawn & FILE_H) != 0){
//                        spotsToBeEmpty = blockingSquare | killerSquareL;
//                    }
//                    else {
//                        spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
//                    }
//
//                    if ((enemyPawns & spotsToBeEmpty) == 0){
//                        score += PAWN_PASSED;
//                    }
//                }
//            }
//        }
//        else {
//            int numberOfAdvancedPawns = populationCount(myPawns & RANK_THREE);
//            if (numberOfAdvancedPawns > 0) {
//                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_THREE);
//                for (Integer pawnIndex : indexOfAllPieces) {
//                    long pawn = newPieceOnSquare(pawnIndex);
//                    long blockingSquare = pawn >>> 8;
//                    long killerSquareL = pawn >>> 9;
//                    long killerSquareR = pawn >>> 7;
//
//                    long spotsToBeEmpty;
//                    if ((pawn & FILE_A) != 0){
//                        spotsToBeEmpty = blockingSquare | killerSquareL;
//                    }
//                    else if ((pawn & FILE_H) != 0){
//                        spotsToBeEmpty = blockingSquare | killerSquareR;
//                    }
//                    else {
//                        spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
//                    }
//
//                    if ((enemyPawns & spotsToBeEmpty) == 0){
//                        score += PAWN_PASSED;
//                    }
//                }
//            }
//        }
//        return score;
//    }


//    private static int superAdvancedPawn (Chessboard board, boolean white, long myPawns){
//        int score = 0;
//        if(white) {
//            int numberOfPawnsNearPromotion = populationCount(myPawns & RANK_SEVEN);
//            
//            /*
//            position score handled elsewhere
//             */
//            if (numberOfPawnsNearPromotion > 0){
//                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_SEVEN);
//                for (Integer pawnIndex : indexOfAllPieces){
//                    long pawn = newPieceOnSquare(pawnIndex);
//                    long pushPromotingSquare = pawn << 8;
//                    long capturePromotingSquareL = pawn << 9;
//                    long capturePromotingSquareR = pawn << 7;
//
//                    if ((pushPromotingSquare & board.allPieces()) == 0){
//                        score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                    }
//
//                    if ((pawn & FILE_A) != 0){
//                        if ((capturePromotingSquareR & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//                    }
//                    else if ((pawn & FILE_H) != 0){
//                        if ((capturePromotingSquareL & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//                    }
//                    else {
//                        if ((capturePromotingSquareR & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//
//                        if ((capturePromotingSquareL & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//                    }
//
//                    if (squareThreatenend(board, true, pushPromotingSquare)){
//                        score += PAWN_P_SQUARE_UNTHREATENED;
//                    }
//
//                    if (squareThreatenend(board, false, pushPromotingSquare)){
//                        score += PAWN_P_SQUARE_SUPPORTED;
//                    }
//
//                    if (squareThreatenend(board, true, pawn)){
//                        score += PAWN_P_UNTHREATENED;
//                    }
//
//                    if (squareThreatenend(board, false, pawn)){
//                        score += PAWN_P_PROTECTED;
//                    }
//                }
//            }
//        }
//        else{
//            int numberOfPawnsNearPromotion = populationCount(myPawns & RANK_TWO);
//            /*
//            position score handled elsewhere
//             */
//
//            if (numberOfPawnsNearPromotion > 0){
//                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_TWO);
//                for (Integer pawnIndex : indexOfAllPieces){
//                    long pawn = newPieceOnSquare(pawnIndex);
//                    long pushPromotingSquare = pawn >>> 8;
//                    long capturePromotingSquareL = pawn >>> 9;
//                    long capturePromotingSquareR = pawn >>> 7;
//
//                    if ((pushPromotingSquare & board.allPieces()) == 0){
//                        score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                    }
//
//                    if ((pawn & FILE_A) != 0){
//                        if ((capturePromotingSquareL & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//                    }
//                    else if ((pawn & FILE_H) != 0){
//                        if ((capturePromotingSquareR & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//                    }
//                    else {
//                        if ((capturePromotingSquareR & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//
//                        if ((capturePromotingSquareL & board.allPieces()) == 0){
//                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
//                        }
//                    }
//
//                    if (squareThreatenend(board, false, pushPromotingSquare)){
//                        score += PAWN_P_SQUARE_UNTHREATENED;
//                    }
//
//                    if (squareThreatenend(board, true, pushPromotingSquare)){
//                        score += PAWN_P_SQUARE_SUPPORTED;
//                    }
//
//                    if (Square.squareThreatenend(board, false, pawn)){
//                        score += PAWN_P_UNTHREATENED;
//                    }
//
//                    if (Square.squareThreatenend(board, true, pawn)){
//                        score += PAWN_P_PROTECTED;
//                    }
//                }
//            }
//        }
//        return score;
//    }

//    private static int backwardsPawn(Chessboard board, boolean white, long myPawns){
//        int score = 0;
//        /*
//        we are only considering the pawn
//         */
//        List<Integer> indexOfAllPieces = BitOperations.getIndexOfAllPieces(myPawns);
//        if (white) {
//            int lastPawnIndex = indexOfAllPieces.get(0);
//            long lastPawn = newPieceOnSquare(lastPawnIndex);
//            long advancedPosition = lastPawn << 8;
//
//            if (squareThreatenend(board, true, lastPawn)){
//                score += PAWN_HANGING_UNDER_THREAT;
//            }
//
//            if (squareThreatenend(board, false, lastPawn)){
//                score += PAWN_HANGING_PROTECTED;
//            }
//
//            if (squareThreatenend(board, true, advancedPosition)){
//                score += PAWN_HANGING;
//            }
//        }
//        else {
//            int lastPawnIndex = indexOfAllPieces.get(indexOfAllPieces.size()-1);
//            long lastPawn = newPieceOnSquare(lastPawnIndex);
//            long advancedPosition = lastPawn >>> 8;
//
//            if (squareThreatenend(board, false, lastPawn)){
//                score += PAWN_HANGING_UNDER_THREAT;
//            }
//
//            if (squareThreatenend(board, true, lastPawn)){
//                score += PAWN_HANGING_PROTECTED;
//            }
//
//            if (squareThreatenend(board, false, advancedPosition)){
//                score += PAWN_HANGING;
//            }
//        }
//        return score;
//    }

    private static int pawnAttackingCentreBonus(Chessboard board, boolean white, long myPawns){
        int score = 0;
        long threatenedSuperCentre = PieceMove.masterPawnCapturesTable(board, white, 0, centreFourSquares, myPawns);
        score += populationCount(threatenedSuperCentre) * PAWN_THREATEN_SUPER_CENTRE;

        long threatenedCentre = PieceMove.masterPawnCapturesTable(board, white, 0, centreNineSquares ^ centreFourSquares, myPawns);
        score += populationCount(threatenedCentre) * PAWN_THREATEN_CENTRE;

        return score;
    }

    private static int pawnCentreBonus(Chessboard board, boolean white, long myPawns){
        int answer = 0;
        answer += populationCount(
                centreFourSquares & myPawns)
                * PAWN_ON_SUPER_CENTRE;
        answer += populationCount(
                (centreNineSquares ^ centreFourSquares) & myPawns)
                * PAWN_ON_CENTRE;
        return answer;
    }

    private static int blockedPawnPenalty(Chessboard board, boolean white, long myPawns, long enemyPawns){
        if (white) {
            long blockingEnemyPawns = (myPawns << 8) & enemyPawns;
            return populationCount(blockingEnemyPawns) * PAWN_BLOCKED;
        }
        else {
            long blockingEnemyPawns = (myPawns >>> 8) & enemyPawns;
            return populationCount(blockingEnemyPawns) * PAWN_BLOCKED;
        }
    }


    private static int pawnOnOpenFile(Chessboard board, boolean white, long myPawns, long enemyPawns){
        int fileScore = 0;
        long[] files = FILES;
        for (int i = 0; i < files.length; i++) {
            long file = files[i];
            if ((file & myPawns) != 0) {
                continue;
            }
            if ((file & enemyPawns) != 0) {
                fileScore += PAWN_UNBLOCKED;
                continue;
            }
            if (i == 0) {
                if ((files[i+1] & enemyPawns) == 0) {
                    fileScore += PAWN_ON_OPEN_FILE;
                    continue;
                }
                continue;
            }
            if (i == 7) {
                if ((files[i-1] & enemyPawns) == 0) {
                    fileScore += PAWN_ON_OPEN_FILE;
                    continue;
                }
                continue;
            }

            if (((files[i+1] & enemyPawns) == 0) && ((files[i-1] & enemyPawns) == 0)){
                fileScore += PAWN_ON_OPEN_FILE;
            }
        }
        return fileScore;
    }

    private static int pawnStructureBonus(Chessboard board, boolean white, long myPawns){
        int score = 0;

        return 0;
    }

    private static int isolatedPawn(Chessboard board, boolean white, long myPawns){
        int score = 0;

        return 0;
    }

    private static int pawnsThreatenBigThings(Chessboard board, boolean white, long myPawns){
        long enemyBigPieces = white ? board.blackPieces() ^ board.getBlackPawns()
                : board.whitePieces() ^ board.getWhitePawns();
        long protectedPawns = PieceMove.masterPawnCapturesTable(board, white, 0, enemyBigPieces, myPawns);
        return populationCount(protectedPawns) * PAWN_THREATENS_BIG_THINGS;
    }

    private static int pawnsChainBonus(Chessboard board, boolean white, long myPawns){
        long protectedPawns = PieceMove.masterPawnCapturesTable(board, white, 0, myPawns, myPawns);
        return populationCount(protectedPawns) * PAWN_PROTECTED_BY_PAWNS;
    }

    private static int doublePawnPenalty(Chessboard board, boolean white, long myPawns){
        int fileScore = 0;
        for (int i = 0; i < FILES.length; i++) {
            long file = FILES[i];
            if (populationCount(file & myPawns) > 1) {
                fileScore += PAWN_DOUBLED;
            }
        }
        return fileScore;
    }

}
