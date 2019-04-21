package com.github.louism33.axolotl.search;

import static com.github.louism33.chesscore.BoardConstants.*;

public final class MoveOrderingConstants {

    public static final int captureBiasOfLastMovedPiece = 1;

    public static final int hashScore = 31;
    public static final int queenCapturePromotionScore = 30;
    public static final int queenQuietPromotionScore = 29;
    public static final int mateKillerScore = 25;
    public static final int neutralCapture = 24; // between 19 and 29
    public static final int killerOneScore = 24;
    public static final int killerTwoScore = 23;
    public static final int pawnPushToSeven = 23;
    public static final int giveCheckMove = 22;
    public static final int castlingMove = 21;
    public static final int pawnPushToSix = 21;
    public static final int oldKillerScoreOne = 20;
    public static final int maxRootQuietScore = 20;
    public static final int oldKillerScoreTwo = 19;
    public static final int maxNodeQuietScore = 18;
    public static final int knightPromotionScore = 4;
    public static final int uninterestingMove = 2;
    public static final int uninterestingPromotion = 1;

    static int[][] goodQuietDestinations = new int[2][64];

    //todo consider minusing the source from the score
    private static int[] goodQuietDestinationsWhite = {
            0, 0, 0, 0, 0, 0, 0, 0,
            1, 2, 2, 2, 2, 2, 2, 1,
            1, 2, 3, 4, 4, 3, 2, 1,
            1, 2, 4, 5, 5, 4, 2, 1,
            1, 2, 4, 5, 5, 4, 2, 1,
            1, 2, 3, 3, 3, 3, 2, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
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
