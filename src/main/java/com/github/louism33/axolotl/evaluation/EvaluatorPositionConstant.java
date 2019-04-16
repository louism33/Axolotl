package com.github.louism33.axolotl.evaluation;

import static com.github.louism33.chesscore.BoardConstants.*;

@SuppressWarnings("ALL")
public final class EvaluatorPositionConstant {

    /*
    thanks to Tomasz Michniewski
    https://www.chessprogramming.org/Simplified_Evaluation_Function
     */

    public static boolean ready = false;

    public static final int[] PAWN_START_WHITE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            123, 118, 122, 83, 83, 122, 118, 123,
            25, 51, 55, 60, 60, 55, 51, 25,
            -9, 26, 5, 20, 20, 5, 26, -9,
            -16, 3, 1, 22, 22, 1, 3, -16,
            -10, 15, 2, 8, 8, 2, 15, -10,
            -16, 20, 8, -6, -6, 8, 20, -16,
            0, 0, 0, 0, 0, 0, 0, 0,
    };   
    
    public static final int[] PAWN_END_WHITE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            135, 138, 125, 115, 115, 125, 138, 135,
            95, 89, 62, 45, 45, 62, 89, 95,
            26, 16, 2, -16, -16, 2, 16, 26,
            0, 5, -13, -24, -24, -13, 5, 0,
            -1, 2, -3, -1, -1, -3, 2, -1,
            3, 4, 8, 12, 12, 8, 4, 3,
            0, 0, 0, 0, 0, 0, 0, 0,
    };



    public static final int[] KNIGHT_START_WHITE = {
            -77, -68, -52, -23, -23, -52, -68, -77,
            -62, -49, 12, -12, -12, 12, -49, -62,
            -51, -1, 13, 9, 9, 13, -1, -51,
            -18, 2, 9, 18, 18, 9, 2, -18,
            -36, -3, 0, -2, -2, 0, -3, -36,
            -32, -2, 7, 5, 5, 7, -2, -32,
            -40, -47, -9, 2, 2, -9, -47, -40,
            -65, -23, -38, -24, -24, -38, -23, -65,
    };

    public static final int[] KNIGHT_END_WHITE = {
            -80, -51, -27, -27, -27, -27, -51, -80,
            -37, -10, -18, -9, -9, -18, -10, -37,
            -45, -19, -5, -6, -6, -5, -19, -45,
            -27, -16, 0, -3, -3, 0, -16, -27,
            -33, -18, -3, -5, -5, -3, -18, -33,
            -23, -17, -11, -4, -4, -11, -17, -23,
            -43, -23, -15, -13, -13, -15, -23, -43,
            -42, -41, -14, -15, -15, -14, -41, -42,
    };

    public static final int[] BISHOP_START_WHITE = {
            -46, -34, -29, -39, -39, -29, -34, -46,
            -40, -22, -22, -30, -30, -22, -22, -40,
            -29, 2, -7, -14, -14, -7, 2, -29,
            -22, -10, -6, 13, 13, -6, -10, -22,
            -16, -3, -2, 16, 16, -2, -3, -16,
            -7, 15, 4, 4, 4, 4, 15, -7,
            -11, 8, 10, -2, -2, 10, 8, -11,
            -41, -1, -12, -12, -12, -12, -1, -41,
    };

    public static final int[] BISHOP_END_WHITE = {
            -34, -22, -29, -18, -18, -29, -22, -34,
            -16, -13, -7, -21, -21, -7, -13, -16,
            -8, -16, -16, -14, -14, -16, -16, -8,
            -7, -16, -9, -14, -14, -9, -16, -7,
            -13, -15, -14, -14, -14, -14, -15, -13,
            -16, -15, -11, -5, -5, -11, -15, -16,
            -26, -22, -14, -5, -5, -14, -22, -26,
            -23, -7, -6, -6, -6, -6, -7, -23,
    };

    public static final int[] ROOK_START_WHITE =   {
            25, 17, 12, 22, 22, 12, 17, 25,
            -9, -10, 20, 19, 19, 20, -10, -9,
            1, 24, 19, 15, 15, 19, 24, 1,
            -14, 3, 12, 18, 18, 12, 3, -14,
            -29, 0, -12, 4, 4, -12, 0, -29,
            -32, -12, -6, -12, -12, -6, -12, -32,
            -32, -12, -12, -3, -3, -12, -12, -32,
            -4, -9, 11, 17, 17, 16, -9, -4,
    };

    public static final int[] ROOK_END_WHITE =   {
            25, 26, 24, 22, 22, 24, 26, 25,
            21, 20, 5, 1, 1, 5, 20, 21,
            19, 15, 7, 6, 6, 7, 15, 19,
            19, 6, 12, -3, -3, 12, 6, 19,
            10, 3, 3, -8, -8, 3, 3, 10,
            4, 3, -12, -9, -9, -12, 3, 4,
            7, 0, -3, -6, -6, -3, 0, 7,
            2, 9, -4, -7, -7, 1, 9, 2,
    };

    public static final int[] QUEEN_START_WHITE =   {
            -8, 5, 17, 20, 20, 17, 5, -8,
            -7, -27, 22, 12, 12, 22, -27, -7,
            14, 17, 17, 24, 24, 17, 17, 14,
            -11, -18, -13, -10, -10, -13, -18, -11,
            -9, -9, -11, -13, -13, -11, -9, -14,
            -16, 8, -10, -7, -7, -10, 3, -16,
            -28, -15, 10, 3, 3, 10, -15, -28,
            -10, -10, -13, 12, 12, -13, -10, -10,
    };

    public static final int[] QUEEN_END_WHITE =   {
            -2, 11, 14, 20, 20, 14, 11, -2,
            -4, 21, 25, 24, 24, 25, 21, -4,
            -4, 2, 20, 30, 30, 20, 2, -4,
            19, 24, 29, 29, 29, 29, 24, 19,
            6, 24, 19, 29, 29, 19, 24, 1,
            5, -19, 11, -7, -7, 11, -24, 5,
            -25, -24, -18, -24, -24, -23, -24, -25,
            -43, -34, -34, -27, -27, -34, -34, -43,
    };

    public static final int[] KING_START_WHITE =   {
            -49, -63, -49, -78, -78, -49, -63, -49,
            -39, -57, -55, -76, -76, -55, -57, -39,
            -36, -57, -56, -78, -78, -56, -57, -36,
            -42, -56, -56, -79, -79, -56, -56, -42,
            -59, -36, -45, -81, -81, -45, -36, -59,
            -22, -26, -38, -59, -59, -38, -26, -22,
            41, 29, -35, -62, -62, -35, 29, 41,
            27, 59, 0, -8, -8, 0, 59, 27,
    };

    public static final int[] KING_END_WHITE =   {
            -49, -16, -6, -17, -17, -6, -16, -49,
            -7, 4, 14, 21, 21, 14, 4, -7,
            -6, 9, 41, 24, 24, 41, 9, -6,
            -9, 14, 33, 34, 34, 33, 14, -9,
            -21, 2, 24, 34, 34, 24, 2, -21,
            -24, 2, 14, 24, 24, 14, 2, -24,
            -51, -30, 0, 9, 9, 0, -30, -51,
            -74, -54, -36, -33, -33, -36, -54, -74,
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
                -24, -9, 6, -2, 3, 3, 4, 5, 5
        };

        mobilityScores[1] = new int[] { //Bishop
                -23, -19, -12, -5, 0, 3, 7, 8, 11, 12, 14, 14, 15, 17
        };

        mobilityScores[2] = new int[] { //Rook
                -23, -15, -10, -1, -4, -3, 3, 6, 11, 16, 18, 19, 25, 26, 24
        };

        mobilityScores[3] = new int[] { //Queen
                -17, -9, -8, -5, -2, 4, 6, 7, 8, 11, 11, 10, 12, 13, 14, 15, 17, 17, 20, 21, 23, 21, 22, 23, 23, 28, 27, 27
        };
    }
}
