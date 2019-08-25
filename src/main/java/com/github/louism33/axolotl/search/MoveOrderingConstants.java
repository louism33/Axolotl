package com.github.louism33.axolotl.search;

import static com.github.louism33.chesscore.BoardConstants.*;

public final class MoveOrderingConstants {

    public static final int hashScore = 31;
    public static final int queenCapturePromotionScore = 30;
    public static final int queenQuietPromotionScore = 29;
    public static final int mateKillerScore = 28;
    public static final int neutralCapture = 24; // between 19 and 29
    public static final int captureBaseScore = 10; // between 18 and 29
    public static final int killerOneScore = 17;
    public static final int killerTwoScore = 16;
    public static final int pawnPushToSeven = 15;
    public static final int giveCheckMove = 15;
    public static final int castlingMove = 15;
    public static final int pawnPushToSix = 14;
    public static final int oldKillerScoreOne = 15;
    public static final int maxRootQuietScore = 14;
    public static final int oldKillerScoreTwo = 14;
    public static final int maxNodeQuietScore = 14;
    public static final int knightPromotionScore = 1;
    public static final int uninterestingMoveScore = 0;
    public static final int uninterestingPromotion = 0;

//    public static final int hashScoreNew = 31_003;
//    public static final int queenCapturePromotionScoreNew = 31_002;
//    public static final int queenQuietPromotionScoreNew = 31_001;
//    public static final int captureBaseScoreMVVLVA = 30_000;
//    public static final int evenCaptureScore = captureBaseScoreMVVLVA; 
//    public static final int mateKillerScoreNew = 29_999;
//    public static final int killerOneScoreNew = 29_998;
//    public static final int killerTwoScoreNew = 29_997;
//    public static final int captureMaxScoreSEE = 29_996;
//
//    public static final int absoluteMaxQuietScore = 29_000;
//    public static final int pawnPushToSevenNew = 28_998;
//    // make sure only this score is uneven ((score & 1) != 0), as we use it to store checking state for move
//    public static final int giveCheckMoveNew = 28_997;
//
//    public static final int castlingMoveNew = 28_996;
//    public static final int oldKillerScoreOneNew = 28_996;
//    public static final int pawnPushToSixNew = 28_994;
//    public static final int oldKillerScoreTwoNew = 28_994;
//    public static final int maxNodeQuietScoreNew = 28_992;
//    public static final int knightPromotionScoreNew = 6;
//    public static final int uninterestingMoveScoreNew = 8;
//    public static final int uninterestingPromotionNew = 2;
//    public static final int notALegalMoveScore = -11;
//    public static final int specialRootMoveScore = -4;
//    public static final int unscoredQuietScore = -5;
//    public static final int alreadySearchedScore = -9;
//    public static final int dontSearchMeScore = -10;

    public static final int hashScoreNew = 31_003;
    public static final int queenCapturePromotionScoreNew = 31_002;
    public static final int queenQuietPromotionScoreNew = 31_001;
    public static final int captureBaseScoreMVVLVA = 30_000;
    public static final int evenCaptureScore = captureBaseScoreMVVLVA;
    public static final int mateKillerScoreNew = 29_999;
    public static final int killerOneScoreNew = 29_998;
    public static final int killerTwoScoreNew = 29_997;
//    public static final int captureMaxScoreSEE = 29_996;

    public static final int absoluteMaxQuietScore = 29_000;
    public static final int pawnPushToSevenNew = 28_998;
    // make sure only this score is uneven ((score & 1) != 0), as we use it to store checking state for move
    public static final int giveCheckMoveNew = 28_997;

    public static final int castlingMoveNew = 28_996;
    public static final int oldKillerScoreOneNew = 28_996;
    public static final int pawnPushToSixNew = 28_994;
    public static final int oldKillerScoreTwoNew = 28_994;
    public static final int maxNodeQuietScoreNew = 28_992;
    public static final int uninterestingMoveScoreNew = 1008;
    public static final int captureMaxScoreSEE = 1000;
    public static final int knightPromotionScoreNew = 6;
    public static final int uninterestingPromotionNew = 4;
    public static final int specialRootMoveScore = -4;
//    public static final int unscoredQuietScore = -5;
    public static final int unscoredQuietScore = 1006;
    public static final int alreadySearchedScore = -9;
    public static final int dontSearchMeScore = -10;
    public static final int notALegalMoveScore = -11;
    
    
    static int[][] goodQuietDestinations = new int[2][64];

    //todo consider minusing the source from the score
    private static int[] goodQuietDestinationsWhite = {
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 2, 2, 2, 2, 2, 2, 1,
            1, 2, 3, 4, 4, 3, 2, 1,
            1, 2, 4, 5, 5, 4, 2, 1,
            1, 2, 4, 5, 5, 4, 2, 1,
            1, 2, 3, 3, 3, 3, 2, 1,
            1, 1, 1, 2, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
    };

    static int[] quietsILikeToMove = new int[7];

    static {
        quietsILikeToMove[PAWN] = 3;
        quietsILikeToMove[KNIGHT] = 4;
        quietsILikeToMove[BISHOP] = 4;
        quietsILikeToMove[ROOK] = 2;
        quietsILikeToMove[QUEEN] = 1;
        quietsILikeToMove[KING] = 0;

        goodQuietDestinations[WHITE] = goodQuietDestinationsWhite;

        for (int i = 0; i < 64; i++) {
            int index = (7 - i / 8) * 8 + (i & 7);
            goodQuietDestinations[BLACK][i] = goodQuietDestinationsWhite[index];
        }
    }

}
