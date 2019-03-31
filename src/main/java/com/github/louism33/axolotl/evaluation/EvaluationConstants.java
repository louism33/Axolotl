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

    // piece values
    public static final int PAWN_SCORE                    = 100;
    public static final int KNIGHT_SCORE                  = 320;
    public static final int BISHOP_SCORE                  = 330;
    public static final int ROOK_SCORE                    = 500;
    public static final int QUEEN_SCORE                   = 900;

    // misc factors
    static final int[] PINNED_PIECES = {0, -10, -25, -25, -50, -90, 0};
    static final int MOVE_NUMBER_POINT                    = 1; // /10
    static final int I_CONTROL_OPEN_FILE                  = 25;
    static final int MY_TURN_BONUS                        = 25;
    static final int CENTRE_PIECE                         = 10;
    static final int IN_CHECK_PENALTY                     = -15;
    static final int SPACE                                = 2;

    // pawn valuation
    static final int PAWN_UNBLOCKED                       = 12;
    static final int PASSED_NEIGHBOUR                     = 22;
    static final int PAWN_OPEN_STOP_SQUARE                = 12;
    static final int PAWN_PASSED                          = 15;
    static final int ROOK_OR_QUEEN_BEHIND_PP              = 12;
    static final int PAWN_DOUBLED                         = -10;
    static final int PAWN_ON_CENTRE                       = 10;
    static final int PAWN_ON_SUPER_CENTRE                 = 15;
    static final int PAWN_PROTECTED_BY_PAWNS              = 4;
    static final int PAWN_THREATENS_BIG_THINGS            = 8;
    static final int PAWN_THREATENS_BISHOPS               = 10;
    static final int PAWN_BLOCKED                         = -5;
    static final int PAWN_THREATEN_CENTRE                 = 5;
    static final int PAWN_THREATEN_SUPER_CENTRE           = 10;
    // backward pawns
    static final int PAWN_ISOLATED                        = -15;
    static final int PAWN_HANGING                         = -15;
    static final int PAWN_HANGING_PROTECTED               = 10;
    static final int PAWN_HANGING_UNDER_THREAT            = -25;
    // promoting pawns
    static final int STRONG_DEFENCE_PRESENCE              = -2;
    static final int STRONG_PRESENCE_FORWARD              = 7;
    static final int PAWN_SIX_EMPTY_FILE_FORWARD          = 40;
    static final int PAWN_SIX_FRIENDS                     = 15;
    static final int PAWN_SEVEN_FRIENDS                   = 25;
    static final int PAWN_SEVEN_PROMOTION_POSSIBLE        = 35;
    static final int PAWN_P_SQUARE_SUPPORTED              = 25;
    static final int PAWN_P_SQUARE_UNTHREATENED           = 25;
    static final int PAWN_P_PROTECTED                     = 50;
    static final int PAWN_P_UNTHREATENED                  = 25;
    
    // common
    static final int PIECE_BEHIND_PAWN                    = 10;

    // knights valuation
    static final int KNIGHT_PAWN_NUMBER_BONUS             = 1;
    static final int KNIGHT_ATTACK_KING_UNITS             = 2;
    static final int KNIGHT_ON_OUTPOST_BONUS              = 20;
    static final int KNIGHT_REACH_OUTPOST_BONUS           = 8;
    static final int KNIGHT_PROTECTED_PAWN                = 8;
    static final int KNIGHT_THREATEN_BIG                  = 10;
    static final int KNIGHT_FORK                          = 35;
    
    // bishop valuation
    static final int BISHOP_COLOUR_PAWNS                  = 1;
    static final int BISHOP_PROTECTED_PAWN                = 6;
    static final int BISHOP_ATTACK_KING_UNITS             = 2;
    static final int BISHOP_DOUBLE                        = 25;
    static final int BISHOP_PRIME_DIAGONAL                = 10;
    static final int BISHOP_ON_OUTPOST_BONUS              = 12;
    static final int BISHOP_REACH_OUTPOST_BONUS           = 6;

    // rook valuation
    static final int ROOK_ON_SEVENTH_BONUS                = 25;
    static final int ROOK_OPEN_FILE_BONUS                 = 18;
    static final int ROOK_ATTACK_KING_UNITS               = 3;
    static final int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 10;
    static final int TRAPPED_ROOK                         = 16;
    static final int ROOKS_ATTACK_UNDEFENDED_PAWNS = 6;
    static final int ROOK_BATTERY_SCORE                   = 8;
    
    // queen valuation
    static final int QUEEN_ON_SEVENTH_BONUS               = 10;
    static final int QUEEN_ATTACK_KING_UNITS              = 5;
    static final int QUEEN_PROTECTS_ROOK                  = 10;
    static final int QUEEN_HATES_PAWNS                    = 10;
    
    // king valuation
    static final int KING_NEAR_SEMI_OPEN_FILE_LOOKUP      = 2;
    static final int[] KING_SAFETY_ARRAY                  =  // - are good
            {   
                    -40, -39, -38, -37, -36, -35, -34, -32, 
                    -26, -22, -18, -12, -10,  -8,  -4,  -2,
 /*neutral: 16 */     0,  12,   24, 48,  64,  96, 128, 256,
                    300, 310,  320,330, 340, 350, 360, 370,
                    380, 390, 400, 410, 420, 430, 440, 450,
                    460, 470, 480, 490, 500, 510, 520, 530,
                    540, 541, 542, 543, 544, 545, 546, 547,
                    548, 549, 550, 551, 552, 553, 554, 555,
            };
}
