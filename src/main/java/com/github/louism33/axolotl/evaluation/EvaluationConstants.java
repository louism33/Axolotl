package com.github.louism33.axolotl.evaluation;

public final class EvaluationConstants {

    // general numbers
    public static final int SHORT_MINIMUM = -31000;
    public static final int SHORT_MAXIMUM = 31000;
    public static final int IN_CHECKMATE_SCORE = -30000;
    public static final int CHECKMATE_ENEMY_SCORE = -IN_CHECKMATE_SCORE;
    public static final int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static final int CHECKMATE_ENEMY_SCORE_MAX_PLY = -IN_CHECKMATE_SCORE_MAX_PLY;
    public static final int IN_STALEMATE_SCORE = 0;

    public static boolean ready = false;
    
    public static void setup() {
        material = new int[startMaterial.length];
        for (int i = 0; i < startMaterial.length; i++) {
            material[i] = Score.bs(startMaterial[i], endMaterial[i]);
        }

        pawnFeatures = new int[startPawnFeatures.length];
        for (int i = 0; i < startPawnFeatures.length; i++) {
            pawnFeatures[i] = Score.bs(startPawnFeatures[i], endPawnFeatures[i]);
        }

        knightFeatures = new int[startKnightFeatures.length];
        for (int i = 0; i < startKnightFeatures.length; i++) {
            knightFeatures[i] = Score.bs(startKnightFeatures[i], endKnightFeatures[i]);
        }

        bishopFeatures = new int[startBishopFeatures.length];
        for (int i = 0; i < startBishopFeatures.length; i++) {
            bishopFeatures[i] = Score.bs(startBishopFeatures[i], endBishopFeatures[i]);
        }

        rookFeatures = new int[startRookFeatures.length];
        for (int i = 0; i < startRookFeatures.length; i++) {
            rookFeatures[i] = Score.bs(startRookFeatures[i], endRookFeatures[i]);
        }
        
        ready = true;
    }
    
    // piece values
    public static final int[] startMaterial = {100, 486, 502, 672, 1418};
    public static final int[] endMaterial = {100, 300, 329, 581, 1153};
    static int[] material;

    static final int P                  = 0;
    static final int K                  = 1;
    static final int B                  = 2;
    static final int R                  = 3;
    static final int Q                  = 4;

    public static final int[] pinnedPiecesScores = {0, 31, -22, -32, -90, -121, 0};

    // misc factors
    static final int MY_TURN_BONUS                        = 0;
    static final int SPACE                                = 1;
    // common
    static final int PIECE_BEHIND_PAWN                    = 2;

    public static final int[] miscFeatures = {8, 2, 15};

    // pawn valuation
    static final int PAWN_UNBLOCKED                       = 0;
    static final int PAWN_OPEN_STOP_SQUARE                = 1;
    static final int PAWN_ISOLATED                        = 2;
    static final int PAWN_BACKWARDS                       = 3;
    static final int ROOK_OR_QUEEN_BEHIND_PP              = 4;
    static final int PAWN_DOUBLED                         = 5;
    static final int PAWN_PROTECTED_BY_PAWNS              = 6;
    static final int PAWN_NEIGHBOURS                      = 7;
    static final int PAWN_THREATENS_BIG_THINGS            = 8;
    static final int PAWN_CANDIDATE                       = 9;
    static final int PAWN_YOUNG_PASSED                    = 10;

    public static final int[] startPawnFeatures = {-13, 13, 0, 2, -3, 0, 0, 0, 19, 3, -6};
    public static final int[] endPawnFeatures =   {26, 13, 0, -3, 23, 0, 0, 0, -20, 3, -13};
    static int[] pawnFeatures;
    
    // knights valuation
    static final int KNIGHT_PAWN_NUMBER_BONUS             = 0;
    static final int kEMPTY                                = 1;
    static final int KNIGHT_ON_OUTPOST_BONUS              = 2;
    static final int KNIGHT_REACH_OUTPOST_BONUS           = 3;
    static final int KNIGHT_PROTECTED_PAWN                = 4;
    static final int KNIGHT_FORK                          = 5;

    public static final int[] startKnightFeatures = {1, 29, 40, 20, 1, 71};
    public static final int[] endKnightFeatures =   {3, 28, 9, 2, 5, 85};
    static int[] knightFeatures;


    // bishop valuation
    static final int BISHOP_COLOUR_PAWNS                  = 0;
    static final int BISHOP_PROTECTED_PAWN                = 1;
    static final int BEMPTY                                = 2;
    static final int BISHOP_DOUBLE                        = 3;
    static final int BISHOP_PRIME_DIAGONAL                = 4;
    static final int BISHOP_ON_OUTPOST_BONUS              = 5;
    static final int BISHOP_REACH_OUTPOST_BONUS           = 6;

    public static final int[] startBishopFeatures = {-2, -5, 25, 62, 27, 46, 14};
    public static final int[] endBishopFeatures =   {0, 8, 18, 48, 2, 7, 12};
    public static int[] bishopFeatures;

    // rook valuation
    static final int ROOK_ON_SEVENTH_BONUS                = 0;
    static final int ROOK_OPEN_FILE_BONUS                 = 1;
    static final int EMPTY                                = 2;

    static final int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 3;
    static final int TRAPPED_ROOK                         = 4;
    static final int ROOKS_ATTACK_UNDEFENDED_PAWNS        = 5;
    static final int ROOK_BATTERY_SCORE                   = 6;

    public static final int[] startRookFeatures = {51, 49, 19, 15, 13, 2, 26};
    public static final int[] endRookFeatures =   {50, 22, 22, 17, 4, 7, 26};
    public static int[] rookFeatures;

    public static final int[] startQueenFeatures = {};
    public static final int[] endQueenFeatures =   {};
    public static int[] queenFeatures;
    
    //safety
    static final int STARTING_PENALTY                     = 0;
    static final int MISSING_QUEEN_KING_SAFETY_UNITS      = 1;
    static final int PINNED_PIECES_KING_SAFETY_LOOKUP     = 2;
    static final int KING_NEAR_SEMI_OPEN_FILE_LOOKUP      = 3;
    static final int NUMBER_OF_ATTACKS_FACTOR             = 4;

    public static final int[] kingSafetyMisc = {10, 5, 2, 3, 2};

    static final int KNIGHT_ATTACK_KING_UNITS             = 0;
    static final int BISHOP_ATTACK_KING_UNITS             = 1;
    static final int ROOK_ATTACK_KING_UNITS               = 2;
    static final int QUEEN_ATTACK_KING_LOOKUP_UNITS       = 3;
    public static final int[] kingAttacksValues = {3, 3, 4, 5};

    public static final int[] KING_SAFETY_ARRAY                  =  // -s are good
            {
                    100, 104, 118, 123, 124, 129, 139, 137,
                    150, 155, 152, 155, 158, 154, 158, 149,
                    167, 158, 175, 168, 180, 181, 183, 186,
                    191, 182, 191, 190, 195, 190, 201, 199,
                    195, 206, 210, 220, 225, 235, 225, 240,
                    271, 280, 290, 301, 310, 320, 330, 340,
                    350, 351, 352, 353, 354, 355, 356, 357,
                    358, 359, 360, 361, 362, 363, 364, 364,
            };

}
