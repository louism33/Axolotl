package com.github.louism33.axolotl.moveordering;

public class MoveOrderingConstants {

    public final static int MOVE_SIZE_LIMIT = 0x2000000;
    static final int moveScoreOffset = 25;
    static final int MOVE_SCORE_MASK = 0xfe000000;

    static final int MAX_HISTORY_MOVE_SCORE = 48;
    static final int CAPTURE_BIAS = 50;
    static final int CAPTURE_BIAS_LAST_MOVED_PIECE = 1;

    static final int
            hashScore = 63;
    static final int mateKillerScore = 61;
    static final int queenCapturePromotionScore = 60;
    static final int queenQuietPromotionScore = 59;
    static final int killerOneScore = 50;
    static final int killerTwoScore = 49;
    static final int oldKillerScoreOne = 46;
    static final int oldKillerScoreTwo = 45;
    static final int castlingMove = 40;
    static final int giveCheckMove = 47;
    static final int knightPromotionScore = 3;
    static final int uninterestingMove = 2;
    static final int uninterestingPromotion = 1;
}
