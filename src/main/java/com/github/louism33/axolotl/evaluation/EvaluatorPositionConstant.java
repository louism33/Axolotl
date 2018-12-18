package com.github.louism33.axolotl.evaluation;

class EvaluatorPositionConstant {

    static final int[] PAWN_POSITION_SCORES = {
              0,   0,  0,  0,  0,  0,  0,  0,
            150, 120,150,200,200,150,120,150,
             12,  56, 66,120,120, 66, 56, 12,
            -16,  14, -2, 30, 30, -2, 14,-16,
              0,   0, 15, 29, 30,-10,-10,-5,
             15,  20,  10,20, 20,  0, 10, 0,
             16,  11, 10,-22,-23, 10, 11, 11,
              0,   0,  0,  0,  0,  0,  0,  0,
    };

    static final int[] KNIGHT_POSITION_SCORES = {
            -30,  -20, -20,-10,-10, -25, -20, -20,
             -20,  30,  12, 10, 10,  12,  30, -20,
              14,  24,  18, 36, 36,  18,  24,  14,
              18,  42,  40, 50, 50,  40,  42,  18,
               8,  36,  36, 36, 36,  36,  36,   8,
               8,  20,  20, 40, 40,  20,  20,   8,
             -10,   0,  24, 30, 30,  24,   0, -10,
             -30, -14,  -4, 20, 20,  -4, -15, -30,
    };

    static final int[] BISHOP_POSITION_SCORES = {
            -10,-15,  0,  0,  0,  0,-15,-10,
            -15, -8,  0, 10, 10,  0, -8,-15,
             40, 48, 42, 34, 34, 42, 48, 40,
             28, 34, 40, 58, 58, 40, 34, 28,
             28, 34, 36, 62, 62, 36, 34, 28,
             10, 24, 30, 30, 30, 30, 24, 15,
              0, 20, 10, 10, 10, 10, 20,  0,
            -30,-20,-25,-10,-10,-25,-20,-30,
    };

    static final int[] ROOK_POSITION_SCORES =   {
            -10, 0,  0, 0, 0,  0,  0,  0,
             10,10, 10,10,10, 10, 10, 10,
              5, 5,  5, 5, 5,  5,  5,  5,
              5, 5,  5, 5, 5,  5,  5,  5,
              5, 5,  5, 5, 5,  5,  5,  5,
              5, 5,  5, 5, 5,  5,  5,  5,
              5, 5,  5, 5, 5,  5,  5,  5,
            -20, 0,  10, 5,10, 15,  0,-20,
    };

    static final int[] QUEEN_POSITION_SCORES =   {
            0, 0,  0,  0, 0,  0,  0,  0,
           10, 10,10, 10,10, 10, 10, 10,
            5, 5,  5,  5, 5,  5,  5,  5,
            5, 5,  5,  5, 5,  5,  5,  5,
            5, 5,  5,  5, 5,  5,  5,  5,
            5, 5,  5,  5, 5,  5,  5,  5,
            5, 5,  5,  5, 5,  5,  5,  5, 
          -10,-5, -5, -10,-5,-5, -5, -5,

    };

    static final int[] KING_POSITION_SCORES_START =   {
            -50, -50,  -50,  -50, -50,  -50,  -50,  -50,
            -50, -50,  -50,  -50, -50,  -50,  -50,  -50,
            -50, -50,  -50,  -50, -50,  -50,  -50,  -50,
            -50, -50,  -50,  -50, -50,  -50,  -50,  -50,
            -50, -50,  -50,  -50, -50,  -50,  -50,  -50,
            -30, -20,  -20,  -30, -30,  -20,  -20,  -30,
            -20, -10,  -10,  -20, -20,  -10,  -10,  -10,
              0,   0,    0,    0, -10,  10,    30,   10,

    };

    static final int[] KING_POSITION_SCORES_END =   {
            -20, -20,-20,-20, -20,-20,-20,-20,
            -10, -10,-10,-10, -10,-10,-10,-10,
              0,   0,  0,  0,   0,  0,  0,  0,
             10,  10, 10, 10,  10, 10, 10, 10,
             10,  10, 10, 10,  10, 10, 10, 10,
              0,   0,  0,  0,   0,  0,  0,  0,
            -10, -10,-10,-10, -10,-10,-10,-10,
            -20, -20,-20,-20, -20,-20,-20,-20,
    };
}
