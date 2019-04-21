package com.github.louism33.axolotl.evaluation;

import static com.github.louism33.chesscore.BoardConstants.*;

@SuppressWarnings("ALL")
public final class EvaluatorPositionConstant {

    public static boolean ready = false;

    public static final int[] PAWN_START_WHITE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            128, 123, 127, 88, 88, 127, 123, 128,
            20, 46, 54, 59, 59, 54, 46, 20,
            -8, 26, 5, 18, 18, 5, 26, -8,
            -18, 3, 1, 21, 21, 1, 3, -18,
            -9, 15, 4, 11, 11, 4, 15, -9,
            -17, 23, 10, -1, -1, 10, 23, -17,
            0, 0, 0, 0, 0, 0, 0, 0,
    };   
    
    public static final int[] PAWN_END_WHITE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            140, 143, 130, 120, 120, 130, 143, 140,
            92, 89, 59, 40, 40, 59, 89, 92,
            24, 15, -1, -17, -17, -1, 15, 24,
            1, 4, -14, -24, -24, -14, 4, 1,
            -1, 3, -4, -2, -2, -4, 3, -1,
            4, 4, 8, 11, 11, 8, 4, 4,
            0, 0, 0, 0, 0, 0, 0, 0,
    };



    public static final int[] KNIGHT_START_WHITE = {
            -82, -72, -57, -21, -21, -57, -72, -82,
            -66, -48, 13, -13, -13, 13, -48, -66,
            -47, 1, 16, 13, 13, 16, 1, -47,
            -16, 2, 11, 17, 17, 11, 2, -16,
            -32, -3, 0, -1, -1, 0, -3, -32,
            -33, -4, 4, 3, 3, 4, -4, -33,
            -38, -46, -10, 2, 2, -10, -46, -38,
            -62, -21, -37, -21, -21, -37, -21, -62,
    };

    public static final int[] KNIGHT_END_WHITE = {
            -85, -52, -26, -28, -28, -26, -52, -85,
            -37, -12, -23, -12, -12, -23, -12, -37,
            -42, -23, -5, -9, -9, -5, -23, -42,
            -25, -14, -1, -2, -2, -1, -14, -25,
            -33, -17, -1, -5, -5, -1, -17, -33,
            -21, -20, -15, -8, -8, -15, -20, -21,
            -42, -23, -16, -15, -15, -16, -23, -42,
            -41, -40, -13, -15, -15, -13, -40, -41,
    };

    public static final int[] BISHOP_START_WHITE = {
            -51, -34, -34, -40, -40, -34, -34, -51,
            -45, -20, -21, -30, -30, -21, -20, -45,
            -34, 3, -7, -16, -16, -7, 3, -34,
            -23, -10, -6, 17, 17, -6, -10, -23,
            -15, -3, -5, 16, 16, -5, -3, -15,
            -9, 15, 6, 3, 3, 6, 15, -9,
            -11, 10, 10, -4, -4, 10, 10, -11,
            -40, -2, -11, -11, -11, -11, -2, -40,
    };

    public static final int[] BISHOP_END_WHITE = {
            -31, -21, -26, -17, -17, -26, -21, -31,
            -15, -13, -7, -18, -18, -7, -13, -15,
            -9, -16, -13, -17, -17, -13, -16, -9,
            -7, -15, -9, -11, -11, -9, -15, -7,
            -13, -15, -12, -10, -10, -12, -15, -13,
            -15, -12, -9, -4, -4, -9, -12, -15,
            -26, -22, -13, -5, -5, -13, -22, -26,
            -20, -6, -6, -6, -6, -6, -6, -20,
    };

    public static final int[] ROOK_START_WHITE =   {
            25, 20, 13, 26, 26, 13, 20, 25,
            -12, -14, 19, 20, 20, 19, -14, -12,
            -1, 26, 18, 15, 15, 18, 26, -1,
            -17, 2, 12, 19, 19, 12, 2, -17,
            -32, 2, -13, 6, 6, -13, 2, -32,
            -33, -12, -5, -11, -11, -5, -12, -33,
            -37, -10, -11, -1, -1, -11, -10, -37,
            -4, -10, 10, 19, 19, 15, -10, -4,
    };

    public static final int[] ROOK_END_WHITE =   {
            29, 28, 26, 26, 26, 26, 28, 29,
            17, 19, 2, -2, -2, 2, 19, 17,
            18, 15, 9, 9, 9, 9, 15, 18,
            20, 6, 13, -1, -1, 13, 6, 20,
            11, 3, 3, -7, -7, 3, 3, 11,
            4, 3, -12, -10, -10, -12, 3, 4,
            8, -1, -2, -5, -5, -2, -1, 8,
            2, 9, -4, -7, -7, 1, 9, 2,
    };

    public static final int[] QUEEN_START_WHITE =   {
            -9, 7, 21, 25, 25, 21, 7, -9,
            -8, -32, 20, 11, 11, 20, -32, -8,
            19, 18, 14, 20, 20, 14, 18, 19,
            -11, -21, -15, -13, -13, -15, -21, -11,
            -8, -12, -13, -18, -18, -13, -12, -13,
            -17, 7, -11, -9, -9, -11, 2, -17,
            -25, -12, 11, 6, 6, 11, -12, -25,
            -9, -7, -9, 13, 13, -9, -7, -9,
    };

    public static final int[] QUEEN_END_WHITE =   {
            1, 14, 18, 25, 25, 18, 14, 1,
            -2, 25, 27, 29, 29, 27, 25, -2,
            -8, 3, 23, 35, 35, 23, 3, -8,
            19, 29, 31, 34, 34, 31, 29, 19,
            7, 28, 20, 33, 33, 20, 28, 2,
            5, -20, 10, -7, -7, 10, -25, 5,
            -26, -28, -23, -27, -27, -28, -28, -26,
            -46, -35, -38, -31, -31, -38, -35, -46,
    };

    public static final int[] KING_START_WHITE =   {
            -45, -58, -45, -73, -73, -45, -58, -45,
            -35, -52, -50, -71, -71, -50, -52, -35,
            -32, -52, -51, -75, -75, -51, -52, -32,
            -41, -52, -52, -77, -77, -52, -52, -41,
            -59, -35, -44, -81, -81, -44, -35, -59,
            -21, -22, -40, -57, -57, -40, -22, -21,
            40, 29, -38, -64, -64, -38, 29, 40,
            28, 59, -5, -6, -6, -5, 59, 28,
    };

    public static final int[] KING_END_WHITE =   {
            -48, -13, -3, -18, -18, -3, -13, -48,
            -6, 9, 19, 20, 20, 19, 9, -6,
            -1, 14, 42, 25, 25, 42, 14, -1,
            -10, 19, 32, 33, 33, 32, 19, -10,
            -22, 0, 23, 34, 34, 23, 0, -22,
            -26, -1, 13, 22, 22, 13, -1, -26,
            -52, -29, 0, 8, 8, 0, -29, -52,
            -79, -54, -34, -34, -34, -34, -54, -79,
    };
    
    public static int[][][] POSITION_SCORES;
    
    public static void setup() {
        POSITION_SCORES = new int[2][7][64];
        for (int i = 0; i < 64; i++) {
            int index = (7 - i / 8) * 8 + (i & 7);
            POSITION_SCORES[WHITE][PAWN][i] = Score.bs(PAWN_START_WHITE[i], PAWN_END_WHITE[i]);
            POSITION_SCORES[WHITE][KNIGHT][i] = Score.bs(KNIGHT_START_WHITE[i], KNIGHT_END_WHITE[i]);
            POSITION_SCORES[WHITE][BISHOP][i] = Score.bs(BISHOP_START_WHITE[i], BISHOP_END_WHITE[i]);
            POSITION_SCORES[WHITE][ROOK][i] = Score.bs(ROOK_START_WHITE[i], ROOK_END_WHITE[i]);
            POSITION_SCORES[WHITE][QUEEN][i] = Score.bs(QUEEN_START_WHITE[i], QUEEN_END_WHITE[i]);
            POSITION_SCORES[WHITE][KING][i] = Score.bs(KING_START_WHITE[i], KING_END_WHITE[i]);

            POSITION_SCORES[BLACK][PAWN][i] = Score.bs(PAWN_START_WHITE[index], PAWN_END_WHITE[index]);
            POSITION_SCORES[BLACK][KNIGHT][i] = Score.bs(KNIGHT_START_WHITE[index], KNIGHT_END_WHITE[index]);
            POSITION_SCORES[BLACK][BISHOP][i] = Score.bs(BISHOP_START_WHITE[index], BISHOP_END_WHITE[index]);
            POSITION_SCORES[BLACK][ROOK][i] = Score.bs(ROOK_START_WHITE[index], ROOK_END_WHITE[index]);
            POSITION_SCORES[BLACK][QUEEN][i] = Score.bs(QUEEN_START_WHITE[index], QUEEN_END_WHITE[index]);
            POSITION_SCORES[BLACK][KING][i] = Score.bs(KING_START_WHITE[index], KING_END_WHITE[index]);
        }

        ready = true;
    }
    


    public static final int[][] mobilityScores = new int[4][32]; // no mobility for pawns or kings

    static{
        mobilityScores[0] = new int[] { //Knight
                -28, -13, 1, -3, 2, 6, 9, 10,
                5,
        };

        mobilityScores[1] = new int[] { //Bishop
                -28, -24, -7, -2, 4, 8, 12, 12,
                15, 14, 13, 17, 20, 13,
        };

        mobilityScores[2] = new int[] { //Rook
                -28, -16, -10, -6, -4, 1, 7, 11,
                16, 20, 22, 24, 27, 24, 22,
        };

        mobilityScores[3] = new int[] { //Queen
                -17, -4, -13, -10, -7, -1, 1, 3,
                6, 11, 12, 14, 17, 18, 19, 20,
                22, 22, 25, 26, 28, 26, 27, 28,
                28, 29, 32, 27,
        };
    }
}
