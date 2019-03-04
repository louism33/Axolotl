package com.github.louism33.axolotl.evaluation;


import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.chesscore.BoardConstants.*;

class Pawns {

    static int evalPawnsByTurn(Chessboard board, boolean white,
                               long myPawns,
                               long enemyPawns,long enemyBishops,
                               long enemies,
                               long allPieces) {

        if (myPawns == 0) {
            return 0;
        }

        long fifthRank = white ? RANK_FIVE : RANK_FOUR;
        long sixthRank = white ? RANK_SIX : RANK_THREE;
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
        long lastPawn = BitOperations.getFirstPiece(myPawns);

        int score = 0;

        long enemyBigPieces = enemies ^ enemyPawns;

        return 0;
//        
//
//        long myPawnAttacks = PieceMove.masterPawnCapturesTable(board, white, 0, allPieces, myPawns);
//
//        long threatenedSuperCentre = myPawnAttacks & centreFourSquares;
//        score += populationCount(threatenedSuperCentre) * PAWN_THREATEN_SUPER_CENTRE;
//
//        long threatenedCentre = myPawnAttacks & (centreNineSquares ^ centreFourSquares);
//        score += populationCount(threatenedCentre) * PAWN_THREATEN_CENTRE;
//
//        long protectedPawns = myPawnAttacks & myPawns;
//        score += populationCount(protectedPawns) * PAWN_PROTECTED_BY_PAWNS;
//
//
//        long davidVSGoliath = myPawnAttacks & enemyBigPieces;
////        score += populationCount(davidVSGoliath) * PAWN_THREATENS_BIG_THINGS;
//
//        long bishopAttacks = myPawnAttacks & enemyBishops;
//        score += populationCount(bishopAttacks) * PAWN_THREATENS_BISHOPS;
//
//        score += populationCount(
//                centreFourSquares & myPawns)
//                * PAWN_ON_SUPER_CENTRE;
//
//        score += populationCount(
//                (centreNineSquares ^ centreFourSquares) & myPawns)
//                * PAWN_ON_CENTRE;
//
//        if (white) {
//            long blockingEnemyPawns = (myPawns << 8) & enemyPawns;
//            score += populationCount(blockingEnemyPawns) * PAWN_BLOCKED;
//        } else {
//            long blockingEnemyPawns = (myPawns >>> 8) & enemyPawns;
//            score += populationCount(blockingEnemyPawns) * PAWN_BLOCKED;
//        }
//
//        long pawnsOnSix = myPawns & sixthRank;
//        long pawnsOnSeven = myPawns & seventhRank;
//
//        long myPawnsEval = myPawns;
//
//        myPawnsEval ^= (pawnsOnSix | pawnsOnSeven);
//
//        while (myPawnsEval != 0) {
//            long pawn = BitOperations.getFirstPiece(myPawnsEval);
//            score += pawnScore(board, white, pawn, myPawns, enemyPawns,
//                    lastPawn, fifthRank);
//            myPawnsEval &= myPawnsEval - 1;
//        }
//
//        return score;
    }

    public static int pawnScore(Chessboard board, boolean white, long pawn,
                                 long myPawns,
                                 long enemyPawns,
                                 long lastPawn, long fifthRank) {

        int score = 0;

        return 0;
//        if ((pawn & lastPawn) != 0 && false) {
//            long advancedPosition = white ? pawn << 8 : pawn >>> 8;
//            if (squareThreatenend(board, true, lastPawn)) {
//                score += PAWN_HANGING_UNDER_THREAT;
//            }
//
//            if (squareThreatenend(board, false, lastPawn)) {
//                score += PAWN_HANGING_PROTECTED;
//            }
//
//            if (squareThreatenend(board, true, advancedPosition)) {
//                score += PAWN_HANGING;
//            }
//        }
//
////        long attacks = PieceMove.singlePawnCaptures(pawn, white, enemies);
//
//        long myOtherPawns = myPawns ^ pawn;
//        long file = Evaluator.getFile(pawn);
//        int index = BitOperations.getIndexOfFirstPiece(pawn);
//        long forwardFile = BitOperations.fileForward(index, white);
//
//
//
//        if ((file & myOtherPawns) != 0) {
//            score += PAWN_DOUBLED;
//        }
//
//        if ((forwardFile & enemyPawns) != 0) {
//            score += PAWN_UNBLOCKED;
//        }
//
//        if ((BitOperations.squareCentredOnIndex(index) & enemyPawns) == 0) {
//            score += PAWN_UNBLOCKED;
//        }
//
//        if ((BitOperations.squareCentredOnIndex(index) & myOtherPawns) == 0) {
//            score += PAWN_ISOLATED;
//        }
//
//
//        long possiblePassedPawn = pawn & fifthRank;
//        if (possiblePassedPawn == 0) {
//            return score;
//        }
//
//        if (white) {
//            long blockingSquare = pawn << 8;
//            long killerSquareL = pawn << 9;
//            long killerSquareR = pawn << 7;
//
//            long spotsToBeEmpty;
//            if ((pawn & FILE_A) != 0) {
//                spotsToBeEmpty = blockingSquare | killerSquareR;
//            } else if ((pawn & FILE_H) != 0) {
//                spotsToBeEmpty = blockingSquare | killerSquareL;
//            } else {
//                spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
//            }
//
//            if ((enemyPawns & spotsToBeEmpty) == 0) {
//                score += PAWN_PASSED;
//            }
//        } else {
//            long blockingSquare = pawn >>> 8;
//            long killerSquareL = pawn >>> 9;
//            long killerSquareR = pawn >>> 7;
//
//            long spotsToBeEmpty;
//            if ((pawn & FILE_A) != 0) {
//                spotsToBeEmpty = blockingSquare | killerSquareL;
//            } else if ((pawn & FILE_H) != 0) {
//                spotsToBeEmpty = blockingSquare | killerSquareR;
//            } else {
//                spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
//            }
//
//            if ((enemyPawns & spotsToBeEmpty) == 0) {
//                score += PAWN_PASSED;
//            }
//
//
//        }
//        return score;
    }

}