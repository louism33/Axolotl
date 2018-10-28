package javacode.evaluation;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.check.CheckChecker.numberOfPiecesThatLegalThreatenSquare;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.moveGeneration.PieceMovePawns.masterPawnCapturesTable;

class Pawns {

    private static final int PASSED_PAWN_BONUS = 50;
    private static final int DOUBLE_PAWN_PENALTY = -35;
    private static final int CENTRE_PAWN = 15;
    private static final int SUPER_CENTRE_PAWN = 35;
    private static final int NO_PAWN_BLOCKER_BONUS = 10;
    private static final int OPEN_FILE_BONUS = 40;
    private static final int PAWNS_PROTECTED_BY_PAWNS = 10;
    private static final int DAVID_AND_GOLIATH = 35;
    private static final int BLOCKED_PAWN_PENALTY = -10;
    private static final int PAWN_THREATEN_CENTRE = 10;
    private static final int PAWN_THREATEN_SUPER_CENTRE = 15;

    private static final int PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES = 50;
    private static final int PROTECTED_PAWN_ON_SEVEN = 75;
    private static final int FRIENLDY_ATTACK_PROMOTION_SQUARE = 35;
    private static final int FRIENLDY_PROTECT_PROMOTING_PAWN = 35;
    private static final int ENEMY_NOT_ATTACK_PROMOTION_SQUARE = 35;
    private static final int ENEMY_NOT_ATTACK_PROMOTING_PAWN = 35;

    static int evalPawnsByTurn(Chessboard board, boolean white) {
        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        if (myPawns == 0) {
            return 0;
        }

        int score = 0;

        score += 
                pawnCentreBonus(board, white, myPawns)
                + pawnOnOpenFile(board, white, myPawns, enemyPawns)
                + pawnStructureBonus(board, white, myPawns)
                + pawnsThreatenBigThings(board, white, myPawns)
                + pawnsChainBonus(board, white, myPawns)
                + doublePawnPenalty(board, white, myPawns)
                + pawnAttackingCentreBonus(board, white, myPawns)
                + superAdvancedPawn(board, white, myPawns)
        ;

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
                    long pawn = BitManipulations.newPieceOnSquare(pawnIndex);
                    long pushPromotingSquare = pawn << 8;
                    long capturePromotingSquareL = pawn << 9;
                    long capturePromotingSquareR = pawn << 7;

                    if ((pushPromotingSquare & board.ALL_PIECES()) == 0){
                        score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                    }

                    if ((pawn & FILE_A) != 0){
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }
                    }
                    else if ((pawn & FILE_H) != 0){
                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }
                    }
                    else {
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }

                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }
                    }

                    int enemyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, true, pushPromotingSquare);
                    if (enemyThreatsToPromotionSquare == 0){
                        score += ENEMY_NOT_ATTACK_PROMOTION_SQUARE;
                    }

                    int friendlyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, false, pushPromotingSquare);
                    if (friendlyThreatsToPromotionSquare != 0){
                        score += FRIENLDY_ATTACK_PROMOTION_SQUARE;
                    }

                    int threatsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, true, pawn);
                    if (threatsToPromotingPawn == 0){
                        score += ENEMY_NOT_ATTACK_PROMOTING_PAWN;
                    }

                    int friendsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, false, pawn);
                    if (friendsToPromotingPawn != 0){
                        score += FRIENLDY_PROTECT_PROMOTING_PAWN;
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
                    long pawn = BitManipulations.newPieceOnSquare(pawnIndex);
                    long pushPromotingSquare = pawn >>> 8;
                    long capturePromotingSquareL = pawn >>> 9;
                    long capturePromotingSquareR = pawn >>> 7;

                    if ((pushPromotingSquare & board.ALL_PIECES()) == 0){
                        score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                    }

                    if ((pawn & FILE_A) != 0){
                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }
                    }
                    else if ((pawn & FILE_H) != 0){
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }
                    }
                    else {
                        if ((capturePromotingSquareR & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }

                        if ((capturePromotingSquareL & board.ALL_PIECES()) == 0){
                            score += PAWN_ON_SEVEN_PROMOTION_OPPORTUNITIES;
                        }
                    }

                    int enemyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, false, pushPromotingSquare);
                    if (enemyThreatsToPromotionSquare == 0){
                        score += ENEMY_NOT_ATTACK_PROMOTION_SQUARE;
                    }

                    int friendlyThreatsToPromotionSquare = numberOfPiecesThatLegalThreatenSquare(board, true, pushPromotingSquare);
                    if (friendlyThreatsToPromotionSquare != 0){
                        score += FRIENLDY_ATTACK_PROMOTION_SQUARE;
                    }

                    int threatsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, false, pawn);
                    if (threatsToPromotingPawn == 0){
                        score += ENEMY_NOT_ATTACK_PROMOTING_PAWN;
                    }

                    int friendsToPromotingPawn = numberOfPiecesThatLegalThreatenSquare(board, true, pawn);
                    if (friendsToPromotingPawn != 0){
                        score += FRIENLDY_PROTECT_PROMOTING_PAWN;
                    }
                }
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
                * SUPER_CENTRE_PAWN;
        answer += populationCount(
                (centreNineSquares ^ centreFourSquares) & myPawns)
                * CENTRE_PAWN;
        return answer;
    }

    private static int blockedPawnPenalty(Chessboard board, boolean white, long myPawns, long enemyPawns){
        int fileScore = 0;
        if (white) {
            long blockingEnemyPawns = (myPawns << 8) & enemyPawns;
            return populationCount(blockingEnemyPawns) * BLOCKED_PAWN_PENALTY;
        }
        else {
            long blockingEnemyPawns = (myPawns >>> 8) & enemyPawns;
            return populationCount(blockingEnemyPawns) * BLOCKED_PAWN_PENALTY;
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
                fileScore += NO_PAWN_BLOCKER_BONUS;
                continue;
            }
            if (i == 0) {
                if ((files[i+1] & enemyPawns) == 0) {
                    fileScore += OPEN_FILE_BONUS;
                    continue;
                }
                continue;
            }
            if (i == 7) {
                if ((files[i-1] & enemyPawns) == 0) {
                    fileScore += OPEN_FILE_BONUS;
                    continue;
                }
                continue;
            }

            if (((files[i+1] & enemyPawns) == 0) && ((files[i-1] & enemyPawns) == 0)){
                fileScore += OPEN_FILE_BONUS;
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
        return populationCount(protectedPawns) * DAVID_AND_GOLIATH;
    }

    private static int pawnsChainBonus(Chessboard board, boolean white, long myPawns){
        long protectedPawns = masterPawnCapturesTable(board, white, 0, myPawns);
        return populationCount(protectedPawns) * PAWNS_PROTECTED_BY_PAWNS;
    }

    private static int doublePawnPenalty(Chessboard board, boolean white, long myPawns){
        int fileScore = 0;
        for (long file : FILES) {
            if (populationCount(file & myPawns) > 1){
                fileScore += DOUBLE_PAWN_PENALTY;
            }
        }
        return fileScore;
    }

}
