package com.github.louism33.axolotl.evaluation;

import static com.github.louism33.chesscore.BoardConstants.*;

@SuppressWarnings("ALL")
class EvaluatorPositionConstant {

    /*
    thanks to Tomasz Michniewski
    https://www.chessprogramming.org/Simplified_Evaluation_Function
     */
    static final int[] PAWN_POSITION_SCORES_WHITE = {
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 35, 36, 10,  5,  5,
            0,  0,  0, 30, 30,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-25,-25, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
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
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,
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
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20

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


    static final int[][][] POSITION_SCORES = new int[2][7][64];
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


    static final int[][] mobilityScores = new int[4][32]; // no mobility for pawns or kings
    
    static{
        mobilityScores[0] = new int[] { //Knight
                -50, -40, -20, -10, 0, 10, 20, 30, 40, 45 
        };

        mobilityScores[1] = new int[] { //Bishop
                -30, -20, -5, -2,  0, 10, 15,
                 20,  25, 30, 35, 40, 45, 50
        };

        mobilityScores[2] = new int[] { //Rook
                -30, -25, -20, -15, -10, 0, 5, 
                10, 25, 30, 35, 40, 45, 50,
                50
        };

        mobilityScores[3] = new int[] { //Queen
                -20, -15, -10, -5, -3, 0, 3,
                8,  15,  18, 25, 28, 30, 35,
                45,  48,  50, 55, 60, 65, 75,
                80,  85,  90, 95, 100, 105, 120,
        };
    }

    
}
