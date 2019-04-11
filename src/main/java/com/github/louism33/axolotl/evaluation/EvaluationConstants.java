package com.github.louism33.axolotl.evaluation;

public final class EvaluationConstants {

    // general numbers
    public static int SHORT_MINIMUM = -31000;
    public static int SHORT_MAXIMUM = 31000;
    public static int IN_CHECKMATE_SCORE = -30000;
    public static int CHECKMATE_ENEMY_SCORE = -IN_CHECKMATE_SCORE;
    public static int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static int CHECKMATE_ENEMY_SCORE_MAX_PLY = -IN_CHECKMATE_SCORE_MAX_PLY;
    public static int IN_STALEMATE_SCORE = 0;

    // piece values
    public static int[] material = {100, 308, 341, 533, 979};

    static final int P                  = 0;
    static final int K                  = 1;
    static final int B                  = 2;
    static final int R                  = 3;
    static final int Q                  = 4;

    public static int[] pinnedPiecesScores = {0, -3, -6, -5, -12, -25, 0};
    
    // misc factors
    static final int MY_TURN_BONUS                        = 0;
    static final int SPACE                                = 1;
    // common
    static final int PIECE_BEHIND_PAWN                    = 2;

    public static final int[] miscFeatures = {8, 0, 15};
    
    // pawn valuation
    static int PAWN_UNBLOCKED                       = 0;
    static int PAWN_OPEN_STOP_SQUARE                = 1;
    static int PAWN_ISOLATED                        = 2;
    static int PAWN_BACKWARDS                       = 3;
    static int ROOK_OR_QUEEN_BEHIND_PP              = 4;
    static int PAWN_DOUBLED                         = 5;
    static int PAWN_PROTECTED_BY_PAWNS              = 6;
    static int PAWN_NEIGHBOURS                      = 7;
    static int PAWN_THREATENS_BIG_THINGS            = 8;
    static int PAWN_CANDIDATE                       = 9;
    static int PAWN_YOUNG_PASSED                    = 10;

    public static int[] pawnFeatures = {7, 7, 0, 2, 4, 0, 0, 0, 6, 3, 0};

    // knights valuation
    static int KNIGHT_PAWN_NUMBER_BONUS             = 0;
    static int KNIGHT_ATTACK_KING_UNITS             = 1;
    static int KNIGHT_ON_OUTPOST_BONUS              = 2;
    static int KNIGHT_REACH_OUTPOST_BONUS           = 3;
    static int KNIGHT_PROTECTED_PAWN                = 4;
    static int KNIGHT_FORK                          = 5;

    public static int[] knightFeatures = {2, 4, 13, 4, 1, 17};


    //    // bishop valuation
    static int BISHOP_COLOUR_PAWNS                  = 0;
    static int BISHOP_PROTECTED_PAWN                = 1;
    static int BISHOP_ATTACK_KING_UNITS             = 2;
    static int BISHOP_DOUBLE                        = 3;
    static int BISHOP_PRIME_DIAGONAL                = 4;
    static int BISHOP_ON_OUTPOST_BONUS              = 5;
    static int BISHOP_REACH_OUTPOST_BONUS           = 6;

    public static int[] bishopFeatures = {0, 0, 10, 34, 9, 5, 1};

        // rook valuation
    static int ROOK_ON_SEVENTH_BONUS                = 0;
    static int ROOK_OPEN_FILE_BONUS                 = 1;
    static int ROOK_ATTACK_KING_UNITS               = 2;
    static int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 3;
    static int TRAPPED_ROOK                         = 4;
    static int ROOKS_ATTACK_UNDEFENDED_PAWNS        = 5;
    static int ROOK_BATTERY_SCORE                   = 6;

    public static int[] rookFeatures = {32, 15, 9, 14, -1, 2, 19};
    
    //safety
    static int QUEEN_ATTACK_KING_LOOKUP_UNITS       = 0;
    static int MISSING_QUEEN_KING_SAFETY_UNITS      = 1;
    static int FRIENDLY_PIECE_NEAR_KING             = 2;
    static int KING_NEAR_SEMI_OPEN_FILE_LOOKUP      = 3;

    public static int[] kingSafetyMisc = {8, 5, 0, 3};
    
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
