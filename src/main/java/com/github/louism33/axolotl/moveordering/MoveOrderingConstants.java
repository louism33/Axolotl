package com.github.louism33.axolotl.moveordering;

import com.github.louism33.chesscore.MoveConstants;

public final class MoveOrderingConstants {

    public final static int MOVE_SIZE_LIMIT = MoveConstants.MOVE_UPPER_BOUND;
    public static final int moveScoreOffset = 26;
    public static final int MOVE_SCORE_MASK = 0xfc000000;

    public static final int captureBiasOfLastMovedPiece = 1;

    public static final int hashScore = 31;
    public static final int queenCapturePromotionScore = 30;
    public static final int queenQuietPromotionScore = 29;
    public static final int captureBias = 26; // between 30 and 23
    public static final int mateKillerScore = 25;
    public static final int killerOneScore = 24;
    public static final int killerTwoScore = 23;
    public static final int giveCheckMove = 22;
    public static final int castlingMove = 21;
    public static final int oldKillerScoreOne = 20;
    public static final int oldKillerScoreTwo = 19;
    public static final int maxHistoryMoveScore = 18;
    public static final int knightPromotionScore = 3;
    public static final int uninterestingMove = 2;
    public static final int uninterestingPromotion = 1;

}
