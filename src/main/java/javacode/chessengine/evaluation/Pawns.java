package javacode.chessengine.evaluation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessengine.evaluation.EvaluationConstants.*;
import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.check.CheckChecker.numberOfPiecesThatLegalThreatenSquare;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.moveGeneration.PieceMovePawns.masterPawnCapturesTable;

class Pawns {

    static int evalPawnsByTurn(Chessboard board, boolean white) {
        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

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
                + superAdvancedPawn(board, white, myPawns)
//                + backwardsPawn(board, white, myPawns)
//                + blockedPawnPenalty(board, white, myPawns, enemyPawns)
//                + isolatedPawn(board, white, myPawns)
                + passedPawn(board, white, myPawns, enemyPawns)
        ;

        return score;
    }

    private static int passedPawn (Chessboard board, boolean white, long myPawns, long enemyPawns) {
        int score = 0;
        if (white) {
            int numberOfAdvancedPawns = populationCount(myPawns & RANK_SIX);
            if (numberOfAdvancedPawns > 0) {
                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_SIX);
                for (Integer pawnIndex : indexOfAllPieces) {
                    long pawn = newPieceOnSquare(pawnIndex);
                    long blockingSquare = pawn << 8;
                    long killerSquareL = pawn << 9;
                    long killerSquareR = pawn << 7;

                    long spotsToBeEmpty;
                    if ((pawn & FILE_A) != 0){
                        spotsToBeEmpty = blockingSquare | killerSquareR;
                    }
                    else if ((pawn & FILE_H) != 0){
                        spotsToBeEmpty = blockingSquare | killerSquareL;
                    }
                    else {
                        spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
                    }

                    if ((enemyPawns & spotsToBeEmpty) == 0){
                        score += PAWN_PASSED;
                    }
                }
            }
        }
        else {
            int numberOfAdvancedPawns = populationCount(myPawns & RANK_THREE);
            if (numberOfAdvancedPawns > 0) {
                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_THREE);
                for (Integer pawnIndex : indexOfAllPieces) {
                    long pawn = newPieceOnSquare(pawnIndex);
                    long blockingSquare = pawn >>> 8;
                    long killerSquareL = pawn >>> 9;
                    long killerSquareR = pawn >>> 7;

                    long spotsToBeEmpty;
                    if ((pawn & FILE_A) != 0){
                        spotsToBeEmpty = blockingSquare | killerSquareL;
                    }
                    else if ((pawn & FILE_H) != 0){
                        spotsToBeEmpty = blockingSquare | killerSquareR;
                    }
                    else {
                        spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
                    }

                    if ((enemyPawns & spotsToBeEmpty) == 0){
                        score += PAWN_PASSED;
                    }
                }
            }
        }
        return score;
    }


    private static int superAdvancedPawn (Chessboard board, boolean white, long myPawns){
        int score = 0;
        if(white) {
            int numberOfPawnsNearPromotion = populationCount(myPawns & RANK_SEVEN);
            
            /*
            position score handled elsewhere
             */
            if (numberOfPawnsNearPromotion > 0){
                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_SEVEN);
                for (Integer pawnIndex : indexOfAllPieces){
                    long pawn = newPieceOnSquare(pawnIndex);
                    long pushPromotingSquare = pawn << 8;
                    long capturePromotingSquareL = pawn << 9;
                    long capturePromotingSquareR = pawn << 7;

                    if ((pushPromotingSquare & board.ALL_PIECES()) == 0){
                        score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                    }

                    if ((pawn & FILE_A) != 0){
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }
                    }
                    else if ((pawn & FILE_H) != 0){
                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }
                    }
                    else {
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }

                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }
                    }

                    int enemyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, true, pushPromotingSquare);
                    if (enemyThreatsToPromotionSquare == 0){
                        score += PAWN_P_SQUARE_UNTHREATENED;
                    }

                    int friendlyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, false, pushPromotingSquare);
                    if (friendlyThreatsToPromotionSquare != 0){
                        score += PAWN_P_SQUARE_SUPPORTED;
                    }

                    int threatsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, true, pawn);
                    if (threatsToPromotingPawn == 0){
                        score += PAWN_P_UNTHREATENED;
                    }

                    int friendsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, false, pawn);
                    if (friendsToPromotingPawn != 0){
                        score += PAWN_P_PROTECTED;
                    }
                }
            }
        }
        else{
            int numberOfPawnsNearPromotion = populationCount(myPawns & RANK_TWO);
            /*
            position score handled elsewhere
             */

            if (numberOfPawnsNearPromotion > 0){
                List<Integer> indexOfAllPieces = getIndexOfAllPieces(myPawns & RANK_TWO);
                for (Integer pawnIndex : indexOfAllPieces){
                    long pawn = newPieceOnSquare(pawnIndex);
                    long pushPromotingSquare = pawn >>> 8;
                    long capturePromotingSquareL = pawn >>> 9;
                    long capturePromotingSquareR = pawn >>> 7;

                    if ((pushPromotingSquare & board.ALL_PIECES()) == 0){
                        score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                    }

                    if ((pawn & FILE_A) != 0){
                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }
                    }
                    else if ((pawn & FILE_H) != 0){
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }
                    }
                    else {
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }

                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                        }
                    }

                    int enemyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, false, pushPromotingSquare);
                    if (enemyThreatsToPromotionSquare == 0){
                        score += PAWN_P_SQUARE_UNTHREATENED;
                    }

                    int friendlyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, true, pushPromotingSquare);
                    if (friendlyThreatsToPromotionSquare != 0){
                        score += PAWN_P_SQUARE_SUPPORTED;
                    }

                    int threatsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, false, pawn);
                    if (threatsToPromotingPawn == 0){
                        score += PAWN_P_UNTHREATENED;
                    }

                    int friendsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, true, pawn);
                    if (friendsToPromotingPawn != 0){
                        score += PAWN_P_PROTECTED;
                    }
                }
            }
        }
        return score;
    }

    private static int backwardsPawn(Chessboard board, boolean white, long myPawns){
        int score = 0;
        /*
        we are only considering the pawn
         */
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(myPawns);
        if (white) {
            int lastPawnIndex = indexOfAllPieces.get(0);
            long lastPawn = newPieceOnSquare(lastPawnIndex);
            long advancedPosition = lastPawn << 8;

            int threatsToBackwardsPawn = numberOfPiecesThatLegalThreatenSquare(board, true, lastPawn);
            if (threatsToBackwardsPawn != 0){
                score += PAWN_HANGING_UNDER_THREAT;
            }

            int friendsToBackwardsPawn = numberOfPiecesThatLegalThreatenSquare(board, false, lastPawn);
            if (friendsToBackwardsPawn != 0){
                score += PAWN_HANGING_PROTECTED;
            }


            int threatsToMoveOutOfBack = numberOfPiecesThatLegalThreatenSquare(board, true, advancedPosition);
            if (threatsToMoveOutOfBack != 0){
                score += PAWN_HANGING;
            }
        }
        else {
            int lastPawnIndex = indexOfAllPieces.get(indexOfAllPieces.size()-1);
            long lastPawn = newPieceOnSquare(lastPawnIndex);
            long advancedPosition = lastPawn >>> 8;

            int threatsToBackwardsPawn = numberOfPiecesThatLegalThreatenSquare(board, false, lastPawn);
            if (threatsToBackwardsPawn != 0){
                score += PAWN_HANGING_UNDER_THREAT;
            }

            int friendsToBackwardsPawn = numberOfPiecesThatLegalThreatenSquare(board, true, lastPawn);
            if (friendsToBackwardsPawn != 0){
                score += PAWN_HANGING_PROTECTED;
            }

            int threatsToMoveOutOfBack = numberOfPiecesThatLegalThreatenSquare(board, false, advancedPosition);
            if (threatsToMoveOutOfBack != 0){
                score += PAWN_HANGING;
            }
        }
        return score;
    }

    private static int pawnAttackingCentreBonus(Chessboard board, boolean white, long myPawns){
        int score = 0;
        long threatenedSuperCentre = masterPawnCapturesTable(board, white, 0, centreFourSquares);
        score += populationCount(threatenedSuperCentre) * PAWN_THREATEN_SUPER_CENTRE;

        long threatenedCentre = masterPawnCapturesTable(board, white, 0, centreNineSquares ^ centreFourSquares);
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
        long enemyBigPieces = white ? board.ALL_BLACK_PIECES() ^ board.BLACK_PAWNS
                : board.ALL_WHITE_PIECES() ^ board.WHITE_PAWNS;
        long protectedPawns = masterPawnCapturesTable(board, white, 0, enemyBigPieces);
        return populationCount(protectedPawns) * PAWN_THREATENS_BIG_THINGS;
    }

    private static int pawnsChainBonus(Chessboard board, boolean white, long myPawns){
        long protectedPawns = masterPawnCapturesTable(board, white, 0, myPawns);
        return populationCount(protectedPawns) * PAWN_PROTECTED_BY_PAWNS;
    }

    private static int doublePawnPenalty(Chessboard board, boolean white, long myPawns){
        int fileScore = 0;
        for (long file : FILES) {
            if (populationCount(file & myPawns) > 1){
                fileScore += PAWN_DOUBLED;
            }
        }
        return fileScore;
    }

}
