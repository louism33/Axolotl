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
                
        
        miscFeatures = new int[startMiscFeatures.length];
        for (int i = 0; i < startMiscFeatures.length; i++) {
            miscFeatures[i] = Score.bs(startMiscFeatures[i], endMiscFeatures[i]);
        }
        
        ready = true;
    }
    
    // piece values
    public static final int[] startMaterial = {100, 456, 502, 642, 1393};
    public static final int[] endMaterial = {100, 295, 324, 576, 1128};
    static int[] material;

    static final int P                  = 0;
    static final int K                  = 1;
    static final int B                  = 2;
    static final int R                  = 3;
    static final int Q                  = 4;

    public static final int[] pinnedPiecesScores = {0, 26, -22, -27, -75, -121, 0};

    // misc factors
    static final int MY_TURN_BONUS                        = 0;
    static final int SPACE                                = 1;
    // common
    static final int PIECE_BEHIND_PAWN                    = 2;

    public static final int[] startMiscFeatures = {8, 2, 15};
    public static final int[] endMiscFeatures =   {2, 1, 15};
    public static int[] miscFeatures;

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

    public static final int[] startPawnFeatures = {-3, 3, -5, 2, 17, 5, 0, 0, 19, -2, -6};
    public static final int[] endPawnFeatures =   {1, 28, 0, -3, 8, -5, 0, 0, -20, -7, 2};
    static int[] pawnFeatures;
    
    // knights valuation
    static final int KNIGHT_PAWN_NUMBER_BONUS             = 0;
    static final int kEMPTY                                = 1;
    static final int KNIGHT_ON_OUTPOST_BONUS              = 2;
    static final int KNIGHT_REACH_OUTPOST_BONUS           = 3;
    static final int KNIGHT_PROTECTED_PAWN                = 4;
    static final int KNIGHT_FORK                          = 5;

    public static final int[] startKnightFeatures = {1, 29, 45, 20, 6, 116};
    public static final int[] endKnightFeatures =   {3, 28, 19, 7, 0, 140};
    static int[] knightFeatures;


    // bishop valuation
    static final int BISHOP_COLOUR_PAWNS                  = 0;
    static final int BISHOP_PROTECTED_PAWN                = 1;
    static final int BEMPTY                                = 2;
    static final int BISHOP_DOUBLE                        = 3;
    static final int BISHOP_PRIME_DIAGONAL                = 4;
    static final int BISHOP_ON_OUTPOST_BONUS              = 5;
    static final int BISHOP_REACH_OUTPOST_BONUS           = 6;

    public static final int[] startBishopFeatures = {-2, -5, 25, 27, 27, 36, 9};
    public static final int[] endBishopFeatures =   {0, 8, 18, 63, 2, 12, 12};
    public static int[] bishopFeatures;

    // rook valuation
    static final int ROOK_ON_SEVENTH_BONUS                = 0;
    static final int ROOK_OPEN_FILE_BONUS                 = 1;
    static final int EMPTY                                = 2;

    static final int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 3;
    static final int TRAPPED_ROOK                         = 4;
    static final int ROOKS_ATTACK_UNDEFENDED_PAWNS        = 5;
    static final int ROOK_BATTERY_SCORE                   = 6;

    public static final int[] startRookFeatures = {36, 59, 19, 20, 13, 2, 26};
    public static final int[] endRookFeatures =   {5, 17, 22, 7, -46, 2, 11};
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
                    155, 159, 173, 178, 179, 184, 194, 192,
                    190, 190, 192, 195, 193, 189, 193, 189,
                    202, 193, 205, 198, 205, 206, 208, 206,
                    211, 207, 216, 210, 215, 210, 221, 219,
                    215, 221, 225, 230, 235, 240, 230, 245,
                    236, 240, 245, 251, 255, 265, 275, 285,
                    295, 296, 297, 298, 299, 300, 301, 302,
                    303, 304, 305, 306, 307, 308, 309, 309,
            };

}
