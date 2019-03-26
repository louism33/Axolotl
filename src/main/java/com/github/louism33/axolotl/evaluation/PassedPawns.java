package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BoardConstants.*;

public class PassedPawns {

    static int evalPassedPawnsByTurn(Chessboard board, boolean white,
                                 long myPawns, long allPieces,
                                 long enemies) {



        int score = 0;

        long sixthRank = white ? RANK_SIX : RANK_THREE;
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;

        long pawnsOnSix = myPawns & sixthRank;
        long pawnsOnSeven = myPawns & seventhRank;

        long advancedPawns = pawnsOnSeven | pawnsOnSix;
        while (advancedPawns != 0) {
            long pawn = BitOperations.getFirstPiece(advancedPawns);
            score += PassedPawns.advancedPawnScore(board, white, pawn,
                    myPawns, allPieces,
                    enemies, sixthRank);
            advancedPawns &= advancedPawns - 1;
        }

        return score;
    }



    public static int advancedPawnScore(Chessboard board, boolean white, long advancedPawn,
                                 long myPawns, long allPieces,
                                 long enemies, long sixthRank) {

        int score = 0;

        int index = Long.numberOfTrailingZeros(advancedPawn);

        long forwardFile = BitOperations.fileForward(index, white);
        long squareOnMe = BitOperations.squareCentredOnIndex(index);

        if ((advancedPawn & sixthRank) != 0) {
            score += BitOperations.populationCount(squareOnMe & myPawns) * PAWN_SIX_FRIENDS;
            if ((forwardFile & enemies) == 0){
                score += PAWN_SIX_EMPTY_FILE_FORWARD;
            }
            return score;
        }

        if (white) {
            long pushPromotingSquare = advancedPawn << 8;
            long capturePromotingSquareL = advancedPawn << 9;
            long capturePromotingSquareR = advancedPawn << 7;

            score += BitOperations.populationCount(squareOnMe & myPawns) * PAWN_SEVEN_FRIENDS;


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

//            if (squareThreatenend(board, true, pushPromotingSquare)) {
//                score += PAWN_P_SQUARE_UNTHREATENED;
//            }
//
//            if (squareThreatenend(board, false, pushPromotingSquare)) {
//                score += PAWN_P_SQUARE_SUPPORTED;
//            }
//
//            if (squareThreatenend(board, true, advancedPawn)) {
//                score += PAWN_P_UNTHREATENED;
//            }
//
//            if (squareThreatenend(board, false, advancedPawn)) {
//                score += PAWN_P_PROTECTED;
//            }
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

//            if (squareThreatenend(board, false, pushPromotingSquare)) {
//                score += PAWN_P_SQUARE_UNTHREATENED;
//            }
//
//            if (squareThreatenend(board, true, pushPromotingSquare)) {
//                score += PAWN_P_SQUARE_SUPPORTED;
//            }
//
//            if (squareThreatenend(board, false, advancedPawn)) {
//                score += PAWN_P_UNTHREATENED;
//            }
//
//            if (squareThreatenend(board, true, advancedPawn)) {
//                score += PAWN_P_PROTECTED;
//            }
        }
        return score;
    }
}
