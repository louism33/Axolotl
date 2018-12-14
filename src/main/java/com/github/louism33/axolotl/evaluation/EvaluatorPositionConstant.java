package com.github.louism33.axolotl.evaluation;

class EvaluatorPositionConstant {

    static final int[] PAWN_POSITION_SCORES = {
              0,   0,  0,  0,  0,  0,  0,  0,
            150, 150,150,200,200,150,150,150,
             12,  56, 66, 80, 80, 66, 56, 12,
            -16,  14, -2, 30, 30, -2, 14,-16,
            -32, -28,-12, 20, 25,-12,-28,-32,
            -30, -20,-16,-16,-16,-10, 15,-10,
            10,   10, 10,-10,-10, 10, 10, 10,
              0,   0,  0,  0,  0,  0,  0,  0,
    };

    static final int[] KNIGHT_POSITION_SCORES = {
            -214,-112,-130,-34,-34,-130,-112,-214,
             -80, -62,   2,-34,-34,   2, -62, -80,
             -24,  44,  18, 36, 36,  18,  44, -24,
              18,  42,  38, 50, 50,  38,  42,  18,
               8,  36,  36, 36, 36,  36,  36,   8,
               8,  38,  34, 40, 40,  34,  38,   8,
               0,   0,  24, 36, 36,  24,   0,   0,
             -30,   6,  -4, 20, 20,  -4,   6, -30,
    };

    static final int[] BISHOP_POSITION_SCORES = {
            -18, 12,-92,-84,-84,-92, 12,-18,
            -44, -8,  0,-18,-18,  0, -8,-44,
            40, 48, 42, 34, 34, 42, 48, 40,
            28, 34, 40, 58, 58, 40, 34, 28,
            28, 34, 36, 62, 62, 36, 34, 28,
            36, 54, 50, 40, 40, 50, 54, 36,
            36, 60, 48, 42, 42, 48, 60, 36,
            8,  32, 30, 48, 48, 30, 32,  8,
    };

    static final int[] ROOK_POSITION_SCORES =   {
            -36,-26,-72, -4, -4,-72,-26,-36,
            -36,-20, 14, 22, 22, 14,-20,-36,
            -28,  2, -2, -8, -8, -2,  2,-28,
            -40,-22, 10,  6,  6, 10,-22,-40,
            -44,-14,-22,  2,  2,-22,-14,-44,
            -38,-12, -2, -4, -4, -2,-12,-38,
            -48, -2, -6, 10, 10, -6, -2,-48,
            -10,-12,  0, 14, 14,  0,-12,-10,
    };

    static final int[] QUEEN_POSITION_SCORES =   {
            -72,-44,-78,-66,-66,-78,-44,-72,
            -38,-88,-70,-82,-82,-70,-88,-38,
            -8, -38,-44,-64,-64,-44,-38, -8,
            -38,-44,-46,-60,-60,-46,-44,-38,
            -24,-34,-22,-30,-30,-22,-34,-24,
            -8,  10,-14,-10,-10,-14, 10, -8,
            -4,  12, 26, 20, 20, 26, 12, -4,
            14,   6, 12, 24, 24, 12,  6, 14,
    };

    static final int[] KING_POSITION_SCORES_START =   {
            20, 30, 10,  0,  0, 20, 30, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30
    };

    static final int[] KING_POSITION_SCORES_END =   {
            -50,-30,-30,-30,-30,-30,-30,-50,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -50,-40,-30,-20,-20,-30,-40,-50
    };
}
