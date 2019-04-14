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
            96, 91, 95, 56, 56, 95, 91, 96,
            46, 51, 43, 51, 51, 43, 51, 46,
            -3, 29, 5, 17, 17, 5, 29, -3,
            -13, 3, -5, 19, 19, -5, 3, -13,
            -7, 18, -1, 2, 2, -1, 18, -7,
            -16, 17, -10, -18, -18, -10, 17, -16,
            0, 0, 0, 0, 0, 0, 0, 0,
    };   
    
    public static final int[] PAWN_END_WHITE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            108, 111, 98, 88, 88, 98, 111, 108,
            68, 62, 62, 48, 48, 62, 62, 68,
            20, 19, 8, -4, -4, 8, 19, 20,
            0, 2, -10, -9, -9, -10, 2, 0,
            -4, 5, -6, -1, -1, -6, 5, -4,
            -3, 7, 8, -3, -3, 8, 7, -3,
            0, 0, 0, 0, 0, 0, 0, 0,
    };



    public static final int[] KNIGHT_START_WHITE = {
            -50, -41, -25, -35, -35, -25, -41, -50,
            -35, -22, 6, -12, -12, 6, -22, -35,
            -42, 8, 10, 21, 21, 10, 8, -42,
            -27, 5, 15, 15, 15, 15, 5, -27,
            -30, 0, 15, 19, 19, 15, 0, -30,
            -29, 4, 10, 20, 20, 10, 4, -29,
            -40, -20, 0, -1, -1, 0, -20, -40,
            -56, -32, -29, -30, -30, -29, -32, -56,
    };

    public static final int[] KNIGHT_END_WHITE = {
            -53, -39, -30, -30, -30, -30, -39, -53,
            -40, -19, 9, -9, -9, 9, -19, -40,
            -42, 8, 10, 21, 21, 10, 8, -42,
            -30, 5, 15, 15, 15, 15, 5, -30,
            -36, 0, 15, 19, 19, 15, 0, -36,
            -29, 1, 7, 20, 20, 7, 1, -29,
            -40, -23, 0, 2, 2, 0, -23, -40,
            -51, -32, -29, -30, -30, -29, -32, -51,
    };

    public static final int[] BISHOP_START_WHITE = {
            -22, -13, -5, -15, -15, -5, -13, -22,
            -16, -1, 2, -6, -6, 2, -1, -16,
            -5, 2, 5, 10, 10, 5, 2, -5,
            -10, 5, 6, 10, 10, 6, 5, -10,
            -1, 0, 7, 13, 13, 7, 0, -1,
            -10, 9, 10, 10, 10, 10, 9, -10,
            -8, 8, 4, -2, -2, 4, 8, -8,
            -26, -13, -12, -12, -12, -12, -13, -26,
    };

    public static final int[] BISHOP_END_WHITE = {
            -22, -13, -5, -15, -15, -5, -13, -22,
            -16, -1, 2, -6, -6, 2, -1, -16,
            -5, 2, 5, 10, 10, 5, 2, -5,
            -10, 5, 6, 10, 10, 6, 5, -10,
            -4, 0, 7, 10, 10, 7, 0, -4,
            -10, 9, 10, 10, 10, 10, 9, -10,
            -11, 2, 4, 4, 4, 4, 2, -11,
            -26, -13, -9, -9, -9, -9, -13, -26,
    };

    public static final int[] ROOK_START_WHITE =   {
            1, -4, 0, -2, -2, 0, -4, 1,
            6, 14, 11, 10, 10, 11, 14, 6,
            -5, 0, 1, 0, 0, 1, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 1, 1, 0, 0, -5,
            -8, 0, 0, 0, 0, 0, 0, -8,
            -8, 0, 0, 0, 0, 0, 0, -8,
            -1, 0, -1, 8, 8, 4, 0, -1,
    };

    public static final int[] ROOK_END_WHITE =   {
            1, 2, 0, 1, 1, 0, 2, 1,
            6, 11, 14, 10, 10, 14, 11, 6,
            -5, 3, 1, 0, 0, 1, 3, -5,
            -2, 0, 3, 0, 0, 3, 0, -2,
            -5, 0, 0, 1, 1, 0, 0, -5,
            -8, 0, 0, 0, 0, 0, 0, -8,
            -8, 0, 3, 0, 0, 3, 0, -8,
            -10, -3, 2, 5, 5, 7, -3, -10,
    };

    public static final int[] QUEEN_START_WHITE =   {
            -17, -7, -7, -4, -4, -7, -7, -17,
            -10, -3, 1, 0, 0, 1, -3, -10,
            -10, -4, 8, 6, 6, 8, -4, -10,
            -5, -3, 5, 5, 5, 5, -3, -5,
            -3, 3, 4, 5, 5, 4, 3, -8,
            -10, 2, 5, 5, 5, 5, -3, -10,
            -4, 0, 4, 6, 6, 4, 0, -4,
            -19, -10, -10, 6, 6, -10, -10, -19,
    };

    public static final int[] QUEEN_END_WHITE =   {
            -20, -10, -10, -4, -4, -10, -10, -20,
            -10, 0, 1, 0, 0, 1, 0, -10,
            -10, -1, 5, 6, 6, 5, -1, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 4, 5, 5, 4, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 6, 0, 0, 1, 0, -10,
            -19, -10, -10, -3, -3, -10, -10, -19,
    };

    public static final int[] KING_START_WHITE =   {
            -52, -87, -73, -99, -99, -73, -87, -52,
            -60, -81, -79, -100, -100, -79, -81, -60,
            -60, -81, -80, -99, -99, -80, -81, -60,
            -60, -80, -80, -100, -100, -80, -80, -60,
            -50, -60, -60, -81, -81, -60, -60, -50,
            -25, -50, -50, -50, -50, -50, -50, -25,
            20, 20, -35, -38, -38, -35, 20, 20,
            39, 65, 24, -14, -14, 24, 65, 39
    };

    public static final int[] KING_END_WHITE =   {
            -55, -40, -30, -20, -20, -30, -40, -55,
            -25, -20, -10, 0, 0, -10, -20, -25,
            -30, -15, 20, 30, 30, 20, -15, -30,
            -30, -10, 30, 40, 40, 30, -10, -30,
            -30, -10, 30, 40, 40, 30, -10, -30,
            -30, -10, 20, 30, 30, 20, -10, -30,
            -30, -30, 0, 0, 0, 0, -30, -30,
            -50, -30, -30, -30, -30, -30, -30, -50,
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
