package com.github.louism33.axolotl.evaluation;


import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.PieceMove;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.*;
import static com.github.louism33.chesscore.Square.squareThreatenend;

class Pawns {

    static int evalPawnsByTurn(Chessboard board, boolean white,
                               long myPawns, long myRooks,
                               long enemyPawns,
                               long friends, long enemies,
                               long allPieces) {

        if (myPawns == 0) {
            return 0;
        }

        long fourthRank = white ? RANK_FOUR : RANK_FIVE;
        long fifthRank = white ? RANK_FIVE : RANK_FOUR;
        long sixthRank = white ? RANK_SIX : RANK_THREE;
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
        long lastPawn = BitOperations.getFirstPiece(myPawns);

        int score = 0;

        long enemyBigPieces = white ? enemies ^ board.getBlackPawns() : enemies ^ board.getWhitePawns();

        long myPawnAttacks = PieceMove.masterPawnCapturesTable(board, white, 0, allPieces, myPawns);

        long threatenedSuperCentre = myPawnAttacks & centreFourSquares;
        score += populationCount(threatenedSuperCentre) * PAWN_THREATEN_SUPER_CENTRE;

        long threatenedCentre = myPawnAttacks & (centreNineSquares ^ centreFourSquares);
        score += populationCount(threatenedCentre) * PAWN_THREATEN_CENTRE;

        long protectedPawns = myPawnAttacks & myPawns;
        score += populationCount(protectedPawns) * PAWN_PROTECTED_BY_PAWNS;


        long davidVSGoliath = myPawnAttacks & enemyBigPieces;
        score += populationCount(davidVSGoliath) * PAWN_THREATENS_BIG_THINGS;


        score += populationCount(
                centreFourSquares & myPawns)
                * PAWN_ON_SUPER_CENTRE;

        score += populationCount(
                (centreNineSquares ^ centreFourSquares) & myPawns)
                * PAWN_ON_CENTRE;

        if (white) {
            long blockingEnemyPawns = (myPawns << 8) & enemyPawns;
            score += populationCount(blockingEnemyPawns) * PAWN_BLOCKED;
        } else {
            long blockingEnemyPawns = (myPawns >>> 8) & enemyPawns;
            score += populationCount(blockingEnemyPawns) * PAWN_BLOCKED;
        }

        long pawnsOnSix = myPawns & sixthRank;
        long pawnsOnSeven = myPawns & seventhRank;
        score += BitOperations.populationCount(pawnsOnSix) * PAWN_SIX;
        score += BitOperations.populationCount(pawnsOnSeven) * PAWN_SEVEN;

        long myPawnsEval = myPawns;

        myPawnsEval ^= (pawnsOnSix | pawnsOnSeven);

        while (myPawnsEval != 0) {
            long pawn = BitOperations.getFirstPiece(myPawnsEval);
            score += pawnScore(board, white, pawn, myPawns, myRooks, enemyPawns,
                    allPieces,
                    lastPawn, fourthRank, fifthRank);
            myPawnsEval &= myPawnsEval - 1;
        }

        long advancedPawns = pawnsOnSeven | pawnsOnSix;
        while (advancedPawns != 0) {
            long pawn = BitOperations.getFirstPiece(advancedPawns);
            score += advancedPawnScore(board, white, pawn, myPawns, myRooks, enemyPawns,
                    allPieces,
                    lastPawn, sixthRank, seventhRank);
            advancedPawns &= advancedPawns - 1;
        }

        return score;
    }

    private static int pawnScore(Chessboard board, boolean white, long pawn,
                                 long myPawns, long myRooks,
                                 long enemyPawns,
                                 long allPieces,
                                 long lastPawn, long fourthRank, long fifthRank) {

        int score = 0;

        if ((pawn & lastPawn) != 0) {
            long advancedPosition = white ? pawn << 8 : pawn >>> 8;
            if (squareThreatenend(board, true, lastPawn)) {
                score += PAWN_HANGING_UNDER_THREAT;
            }

            if (squareThreatenend(board, false, lastPawn)) {
                score += PAWN_HANGING_PROTECTED;
            }

            if (squareThreatenend(board, true, advancedPosition)) {
                score += PAWN_HANGING;
            }
        }

        long myOtherPawns = myPawns ^ pawn;
        long file = Evaluator.getFile(pawn);
        int index = BitOperations.getIndexOfFirstPiece(pawn);

        if ((file & myOtherPawns) != 0) {
            score += PAWN_DOUBLED;
        }

        // todo, only forwards
        if ((file & enemyPawns) != 0) {
            score += PAWN_UNBLOCKED;
        }

        if ((BitOperations.squareCentredOnIndex(index) & enemyPawns) == 0) {
            score += PAWN_UNBLOCKED;
        }

        if ((BitOperations.squareCentredOnIndex(index) & myOtherPawns) == 0) {
            score += PAWN_ISOLATED;
        }


        long possiblePassedPawn = pawn & fifthRank;
        if (possiblePassedPawn == 0) {
            return score;
        }

        if (white) {
            long blockingSquare = pawn << 8;
            long killerSquareL = pawn << 9;
            long killerSquareR = pawn << 7;

            long spotsToBeEmpty;
            if ((pawn & FILE_A) != 0) {
                spotsToBeEmpty = blockingSquare | killerSquareR;
            } else if ((pawn & FILE_H) != 0) {
                spotsToBeEmpty = blockingSquare | killerSquareL;
            } else {
                spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
            }

            if ((enemyPawns & spotsToBeEmpty) == 0) {
                score += PAWN_PASSED;
            }
        } else {
            long blockingSquare = pawn >>> 8;
            long killerSquareL = pawn >>> 9;
            long killerSquareR = pawn >>> 7;

            long spotsToBeEmpty;
            if ((pawn & FILE_A) != 0) {
                spotsToBeEmpty = blockingSquare | killerSquareL;
            } else if ((pawn & FILE_H) != 0) {
                spotsToBeEmpty = blockingSquare | killerSquareR;
            } else {
                spotsToBeEmpty = blockingSquare | killerSquareL | killerSquareR;
            }

            if ((enemyPawns & spotsToBeEmpty) == 0) {
                score += PAWN_PASSED;
            }


        }
        return score;
    }

    private static int advancedPawnScore(Chessboard board, boolean white, long advancedPawn,
                                         long myPawns, long myRooks,
                                         long enemyPawns,
                                         long allPieces,
                                         long lastPawn, long sixthRank, long seventhRank) {

        int score = 0;

        if ((advancedPawn & sixthRank) != 0) {
            return PAWN_SIX;
        }

        if (white) {
            long pushPromotingSquare = advancedPawn << 8;
            long capturePromotingSquareL = advancedPawn << 9;
            long capturePromotingSquareR = advancedPawn << 7;

            if ((pushPromotingSquare & allPieces) == 0) {
                score += PAWN_SEVEN_PROMOTION_POSSIBLE;
            }

            if ((advancedPawn & FILE_A) != 0) {
                if ((capturePromotingSquareR & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }
            } else if ((advancedPawn & FILE_H) != 0) {
                if ((capturePromotingSquareL & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }
            } else {
                if ((capturePromotingSquareR & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }

                if ((capturePromotingSquareL & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }
            }

            if (squareThreatenend(board, true, pushPromotingSquare)) {
                score += PAWN_P_SQUARE_UNTHREATENED;
            }

            if (squareThreatenend(board, false, pushPromotingSquare)) {
                score += PAWN_P_SQUARE_SUPPORTED;
            }

            if (squareThreatenend(board, true, advancedPawn)) {
                score += PAWN_P_UNTHREATENED;
            }

            if (squareThreatenend(board, false, advancedPawn)) {
                score += PAWN_P_PROTECTED;
            }
        } else {
            long pushPromotingSquare = advancedPawn >>> 8;
            long capturePromotingSquareL = advancedPawn >>> 9;
            long capturePromotingSquareR = advancedPawn >>> 7;

            if ((pushPromotingSquare & board.allPieces()) == 0) {
                score += PAWN_SEVEN_PROMOTION_POSSIBLE;
            }

            if ((advancedPawn & FILE_A) != 0) {
                if ((capturePromotingSquareL & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }
            } else if ((advancedPawn & FILE_H) != 0) {
                if ((capturePromotingSquareR & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }
            } else {
                if ((capturePromotingSquareR & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }

                if ((capturePromotingSquareL & board.allPieces()) == 0) {
                    score += PAWN_SEVEN_PROMOTION_POSSIBLE;
                }
            }

            if (squareThreatenend(board, false, pushPromotingSquare)) {
                score += PAWN_P_SQUARE_UNTHREATENED;
            }

            if (squareThreatenend(board, true, pushPromotingSquare)) {
                score += PAWN_P_SQUARE_SUPPORTED;
            }

            if (squareThreatenend(board, false, advancedPawn)) {
                score += PAWN_P_UNTHREATENED;
            }

            if (squareThreatenend(board, true, advancedPawn)) {
                score += PAWN_P_PROTECTED;
            }
        }
        return score;
    }

}