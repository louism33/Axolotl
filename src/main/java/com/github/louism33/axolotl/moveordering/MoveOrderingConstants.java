package com.github.louism33.axolotl.moveordering;

public class MoveOrderingConstants {

    public final static int MOVE_SIZE_LIMIT = 0x2000000;
    public static final int moveScoreOffset = 25;
    public static final int MOVE_SCORE_MASK = 0xfe000000;

    public static final int MAX_HISTORY_MOVE_SCORE = 48;
    public static final int CAPTURE_BIAS = 50;
    public static final int CAPTURE_BIAS_LAST_MOVED_PIECE = 1;

    public static final int hashScore = 63;
    public static final int mateKillerScore = 61;
    public static final int queenCapturePromotionScore = 60;
    public static final int queenQuietPromotionScore = 59;
    public static final int killerOneScore = 50;
    public static final int killerTwoScore = 49;
    public static final int oldKillerScoreOne = 46;
    public static final int oldKillerScoreTwo = 45;
    public static final int castlingMove = 40;
    public static final int giveCheckMove = 47;
    public static final int knightPromotionScore = 3;
    public static final int uninterestingMove = 2;
    public static final int uninterestingPromotion = 1;
    
    
}
