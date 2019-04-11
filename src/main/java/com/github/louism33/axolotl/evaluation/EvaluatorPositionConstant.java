package com.github.louism33.axolotl.evaluation;

import static com.github.louism33.chesscore.BoardConstants.*;

@SuppressWarnings("ALL")
public final class EvaluatorPositionConstant {

    /*
    thanks to Tomasz Michniewski
    https://www.chessprogramming.org/Simplified_Evaluation_Function
     */
    static final int[] PAWN_POSITION_SCORES_WHITE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            86, 86, 76, 74, 74, 76, 86, 86,
            43, 43, 46, 48, 48, 46, 43, 43,
            15, 19, 8, 6, 6, 8, 19, 15,
            -3, 5, -5, 4, 4, -5, 5, -3,
            -4, 8, -1, -1, -1, -1, 8, -4,
            -8, 12, 3, -16, -16, 3, 12, -8,
            0, 0, 0, 0, 0, 0, 0, 0,
    };

    static final int[] KNIGHT_POSITION_SCORES_WHITE = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-35,-30,-30,-30,-30,-35,-50,
    };

    static final int[] BISHOP_POSITION_SCORES_WHITE = {
            -20, -9, -10, -10, -10, -10, -9, -20,
            -11, 0, 1, 0, 0, 1, 0, -11,
            -10, 1, 5, 10, 10, 5, 1, -10,
            -10, 5, 5, 10, 10, 5, 5, -10,
            0, 0, 8, 10, 10, 8, 0, 0,
            -10, 10, 10, 10, 10, 10, 10, -10,
            -10, 4, -1, -1, -1, -1, 4, -10,
            -20, -10, -16, -10, -10, -16, -10, -20,
    };

    static final int[] ROOK_POSITION_SCORES_WHITE =   {
             0,  0,  0,  0,  0,  0,  0,  0,
             5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
             0,  0,  0,  5,  5,  5,  0,  0
    };

    static final int[] QUEEN_POSITION_SCORES_WHITE =   {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
             -5,  0,  5,  5,  5,  5,  0, -5,
              0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20

    };

    static final int[] KING_POSITION_SCORES_START_WHITE =   {
            -60,-80,-80,-100,-100,-80,-80,-60,
            -60,-80,-80,-100,-100,-80,-80,-60,
            -60,-80,-80,-100,-100,-80,-80,-60,
            -60,-80,-80,-100,-100,-80,-80,-60,
            -50,-60,-60,-80,-80,-60,-60,-50,
            -25,-50,-50,-50,-50,-50,-50,-25,
            20, 20, -35,-35,-35,-35, 20, 20,
            40, 60, 25,-25, -25, 25, 60, 40
    };

    static final int[] KING_POSITION_SCORES_END_WHITE =   {
            -50,-40,-30,-20,-20,-30,-40,-50,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -50,-30,-30,-30,-30,-30,-30,-50
    };
    
    static final int[] PAWN_POSITION_SCORES_BLACK = new int[64];
    static final int[] KNIGHT_POSITION_SCORES_BLACK = new int[64];
    static final int[] BISHOP_POSITION_SCORES_BLACK = new int[64];
    static final int[] ROOK_POSITION_SCORES_BLACK = new int[64];
    static final int[] QUEEN_POSITION_SCORES_BLACK = new int[64];
    static final int[] KING_POSITION_SCORES_START_BLACK = new int[64];
    static final int[] KING_POSITION_SCORES_END_BLACK = new int[64];


    public static final int[][][] POSITION_SCORES = new int[2][7][64];
    static {

        for (int i = 0; i < 64; i++) {
            int index = (7 - i / 8) * 8 + (i & 7);
            PAWN_POSITION_SCORES_BLACK[i] = PAWN_POSITION_SCORES_WHITE[index];
            KNIGHT_POSITION_SCORES_BLACK[i] = KNIGHT_POSITION_SCORES_WHITE[index];
            BISHOP_POSITION_SCORES_BLACK[i] = BISHOP_POSITION_SCORES_WHITE[index];
            ROOK_POSITION_SCORES_BLACK[i] = ROOK_POSITION_SCORES_WHITE[index];
            QUEEN_POSITION_SCORES_BLACK[i] = QUEEN_POSITION_SCORES_WHITE[index];
            KING_POSITION_SCORES_START_BLACK[i] = KING_POSITION_SCORES_START_WHITE[index];
            KING_POSITION_SCORES_END_BLACK[i] = KING_POSITION_SCORES_END_WHITE[index];
        }
        
        
        POSITION_SCORES[WHITE][PAWN] = PAWN_POSITION_SCORES_WHITE;
        POSITION_SCORES[WHITE][KNIGHT] = KNIGHT_POSITION_SCORES_WHITE;
        POSITION_SCORES[WHITE][BISHOP] = BISHOP_POSITION_SCORES_WHITE;
        POSITION_SCORES[WHITE][ROOK] = ROOK_POSITION_SCORES_WHITE;
        POSITION_SCORES[WHITE][QUEEN] = QUEEN_POSITION_SCORES_WHITE;
        POSITION_SCORES[WHITE][KING] = KING_POSITION_SCORES_START_WHITE;
        POSITION_SCORES[WHITE][KING-KING] = KING_POSITION_SCORES_END_WHITE;

        POSITION_SCORES[BLACK][PAWN] = PAWN_POSITION_SCORES_BLACK;
        POSITION_SCORES[BLACK][KNIGHT] = KNIGHT_POSITION_SCORES_BLACK;
        POSITION_SCORES[BLACK][BISHOP] = BISHOP_POSITION_SCORES_BLACK;
        POSITION_SCORES[BLACK][ROOK] = ROOK_POSITION_SCORES_BLACK;
        POSITION_SCORES[BLACK][QUEEN] = QUEEN_POSITION_SCORES_BLACK;
        POSITION_SCORES[BLACK][KING] = KING_POSITION_SCORES_START_BLACK;
        POSITION_SCORES[BLACK][KING-KING] = KING_POSITION_SCORES_END_BLACK;
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
