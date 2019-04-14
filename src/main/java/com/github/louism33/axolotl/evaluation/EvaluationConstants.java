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
    public static final int[] startMaterial = {100, 411, 427, 597, 1283};
    public static final int[] endMaterial = {100, 295, 319, 536, 1048};
    static int[] material;

    static final int P                  = 0;
    static final int K                  = 1;
    static final int B                  = 2;
    static final int R                  = 3;
    static final int Q                  = 4;

    public static final int[] pinnedPiecesScores = {0, 11, -7, -12, -45, -6, 0};

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
    static final int KNIGHT_ATTACK_KING_UNITS             = 1;
    static final int KNIGHT_ON_OUTPOST_BONUS              = 2;
    static final int KNIGHT_REACH_OUTPOST_BONUS           = 3;
    static final int KNIGHT_PROTECTED_PAWN                = 4;
    static final int KNIGHT_FORK                          = 5;

    public static final int[] startKnightFeatures = {1, 18, 32, 9, 1, 69};
    public static final int[] endKnightFeatures =   {1, 30, 6, -1, 2, 75};
    static int[] knightFeatures;


    // bishop valuation
    static final int BISHOP_COLOUR_PAWNS                  = 0;
    static final int BISHOP_PROTECTED_PAWN                = 1;
    static final int BISHOP_ATTACK_KING_UNITS             = 2;
    static final int BISHOP_DOUBLE                        = 3;
    static final int BISHOP_PRIME_DIAGONAL                = 4;
    static final int BISHOP_ON_OUTPOST_BONUS              = 5;
    static final int BISHOP_REACH_OUTPOST_BONUS           = 6;

    public static final int[] startBishopFeatures = {0, 0, 10, 48, 22, 31, 14};
    public static final int[] endBishopFeatures =   {26, 13, 10, 35, 9, -8, 1};
    public static int[] bishopFeatures;

    // rook valuation
    static final int ROOK_ON_SEVENTH_BONUS                = 0;
    static final int ROOK_OPEN_FILE_BONUS                 = 1;
    static final int ROOK_ATTACK_KING_UNITS               = 2;
    static final int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 3;
    static final int TRAPPED_ROOK                         = 4;
    static final int ROOKS_ATTACK_UNDEFENDED_PAWNS        = 5;
    static final int ROOK_BATTERY_SCORE                   = 6;

    public static final int[] startRookFeatures = {46, 34, 9, 20, 13, 2, 26};
    public static final int[] endRookFeatures =   {46, 8, 22, 7, 13, 2, 26};
    public static int[] rookFeatures;

    public static final int[] startQueenFeatures = {};
    public static final int[] endQueenFeatures =   {};
    public static int[] queenFeatures;
    
    //safety
    static final int QUEEN_ATTACK_KING_LOOKUP_UNITS       = 0;
    static final int MISSING_QUEEN_KING_SAFETY_UNITS      = 1;
    static final int FRIENDLY_PIECE_NEAR_KING             = 2;
    static final int KING_NEAR_SEMI_OPEN_FILE_LOOKUP      = 3;

    public static final int[] kingSafetyMisc = {21, 5, 0, 3};

    public static final int[] KING_SAFETY_ARRAY                  =  // -s are good
            // /* neutral: 20 */
            {
                    -97, -86, -72, -67, -66, -61, -51, -43,
                    -35, -30, -28, -25, -22, -16, -12, -6,
                    2,  13,  25, 48, 65, 96, 128, 156,
                    166, 207, 211, 225, 250, 275, 301, 319,
                    330, 341, 350, 360, 370, 380, 390, 400,
                    461, 470, 480, 491, 500, 510, 520, 530,
                    540, 541, 542, 543, 544, 545, 546, 547,
                    548, 549, 550, 551, 552, 553, 554, 554
            };

}
