package com.github.louism33.axolotl.moveordering;

class MoveOrderingConstants {

    static final int MAX_HISTORY_MOVE_SCORE = 90;
    static final int CAPTURE_BIAS = 100;
    static final int CAPTURE_BIAS_LAST_MOVED_PIECE = 5;

    static final int
            hashScore = 127,
            aiScore = 126,
            mateKillerScore = 125,
            queenPromotionScore = 109,
            killerOneScore = 102,
            killerTwoScore = 101,
            giveCheckMove = 100,
            oldKillerScoreOne = 99,
            oldKillerScoreTwo = 98,
            castlingMove = 10,
            knightPromotionScore = 2,
            uninterestingMove = 1,
            uninterestingPromotion = 0;
}
