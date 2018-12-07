package com.github.louism33.axolotl.moveordering;

class MoveOrderingConstants {

    /*
    01111110
    xxxxxxxx
    xxxxxxxx
    xxxxxxxx
     */
    private static int bestMoveScorePossible = 63;
    static int moveScoreOffset = 25;
    static int MOVE_SCORE_MASK = 0xfe000000;
    
    static final int MAX_HISTORY_MOVE_SCORE = 90;
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
            giveCheckMove = 0,
            oldKillerScoreOne = 48,
            oldKillerScoreTwo = 47,
            castlingMove = 40,
            knightPromotionScore = 2,
            uninterestingMove = 1,
            uninterestingPromotion = 0;
}
