package com.github.louism33.axolotl.search;

public final class MoveOrderingConstants {

    public static final int hashScoreNew = 31_003;
    public static final int queenCapturePromotionScoreNew = 31_002;
    public static final int queenQuietPromotionScoreNew = 31_001;
    public static final int captureBaseScoreMVVLVA = 30_000;
    public static final int evenCaptureScore = captureBaseScoreMVVLVA;
    public static final int mateKillerScoreNew = 29_999;
    public static final int killerOneScoreNew = 29_998;
    public static final int killerTwoScoreNew = 29_997;

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
    public static final int unscoredQuietScore = 1006;
    public static final int alreadySearchedScore = -9;
    public static final int dontSearchMeScore = -10;
    public static final int notALegalMoveScore = -11;
    
}
