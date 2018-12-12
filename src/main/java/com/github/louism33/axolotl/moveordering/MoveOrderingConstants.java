package com.github.louism33.axolotl.moveordering;

public class MoveOrderingConstants {

    /*
    01111110
    xxxxxxxx
    xxxxxxxx
    xxxxxxxx
     */
    private static int bestMoveScorePossible = 63;
    public final static int MOVE_SIZE_LIMIT = 0x2000000;
    static int moveScoreOffset = 25;
    static int MOVE_SCORE_MASK = 0xfe000000;

    static final int MAX_HISTORY_MOVE_SCORE = 48;
    static final int CAPTURE_BIAS = 50;
    static final int CAPTURE_BIAS_LAST_MOVED_PIECE = 1;

    static final int
            hashScore = 63,
            aiScore = 62,
            mateKillerScore = 61,
            queenCapturePromotionScore = 60,
            queenQuietPromotionScore = 59,
            killerOneScore = 50,
            killerTwoScore = 49,
            oldKillerScoreOne = 46,
            oldKillerScoreTwo = 45,
            castlingMove = 40,
            giveCheckMove = 47,
            knightPromotionScore = 3,
            uninterestingMove = 2,
            uninterestingPromotion = 1;
}
