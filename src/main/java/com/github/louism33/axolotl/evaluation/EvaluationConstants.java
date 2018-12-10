package com.github.louism33.axolotl.evaluation;

public class EvaluationConstants {

    // index
    static final int EMPTY = 0;
    static final int PAWN = 1;
    static final int KNIGHT = 2;
    static final int BISHOP = 3;
    static final int ROOK = 4;
    static final int QUEEN = 5;
    static final int KING = 6;
    
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
    public static final int KNIGHT_SCORE                  = 300;
    public static final int BISHOP_SCORE                  = 310;
    public static final int ROOK_SCORE                    = 500;
    public static final int QUEEN_SCORE                   = 900;
    public static final int KING_SCORE                    = 3000;

    // misc factors
    static final int[] PINNED_PIECES = {0, -10, -25, -25, -50, -100, 0};
    static final int MOVE_NUMBER_POINT                    = 1; // /10
    static final int BATTERY_SCORE                        = 10;
    static final int I_CONTROL_OPEN_FILE                  = 10;
    static final int MY_TURN_BONUS                        = 10;
    static final int IN_CHECK_PENALTY                     = -15;
    static final int BASIC_PINNED_PIECE_PENALTY_KING      = -15;
    static final int QUEEN_IS_PINNED                      = -10;
    static final int BASIC_PINNED_PIECE_PENALTY_QUEEN     = -10;
    static int MY_SPACE                                   = 2;

    // pawn valuation
    static final int PAWN_PASSED                          = 15;
    static final int PAWN_DOUBLED                         = -10;
    static final int PAWN_ON_CENTRE                       = 5;
    static final int PAWN_ON_SUPER_CENTRE                 = 10;
    static final int PAWN_UNBLOCKED                       = 10;
    static final int PAWN_ON_OPEN_FILE                    = 10;
    static final int PAWN_PROTECTED_BY_PAWNS              = 5;
    static final int PAWN_THREATENS_BIG_THINGS            = 10;
    static final int PAWN_BLOCKED                         = -5;
    static final int PAWN_THREATEN_CENTRE                 = 5;
    static final int PAWN_THREATEN_SUPER_CENTRE           = 10;
    // backward pawns
    static final int PAWN_ISOLATED                        = -15;
    static final int PAWN_HANGING                         = -15;
    static final int PAWN_HANGING_PROTECTED               = 10;
    static final int PAWN_HANGING_UNDER_THREAT            = -25;
    // promoting pawns
    static final int PAWN_SEVEN                           = 25;
    static final int PAWN_SIX                             = 15;
    static final int PAWN_SEVEN_PROMOTION_POSSIBLE        = 35;
    static final int PAWN_P_SQUARE_SUPPORTED              = 25;
    static final int PAWN_P_SQUARE_UNTHREATENED           = 25;
    static final int PAWN_P_PROTECTED                     = 50;
    static final int PAWN_P_UNTHREATENED                  = 25;
    
    // knights valuation
    static final int KNIGHT_OUTPOST_BONUS                 = 20;
    static final int KNIGHT_ADVANCED_BONUS                = 5;
    static final int KNIGHT_MOBILITY_SCORE                = 3;
    static final int KNIGHT_THREATEN_BIG                  = 10;
    static final int KNIGHT_FORK                          = 35;
    static final int KNIGHT_PROTECTOR_SCORE               = 4;
    static final int KNIGHT_AGGRESSOR_SCORE               = 6;
    static final int KNIGHT_UNDEVELOPED_PENALTY           = -20;
    
    // bishop valuation
    static final int BISHOP_PER_ENEMY_PAWN_ON_COLOUR      = -3;
    static final int BISHOP_PER_FRIENDLY_PAWN_ON_COLOUR   = -2;
    static final int BISHOP_DOUBLE_BONUS                  = 15;
    static final int BISHOP_OUTPOST_BONUS                 = 18;
    static final int BISHOP_ADVANCED_BONUS                = 5;
    static final int BISHOP_MOBILITY_SCORE                = 1;
    static final int BISHOP_PROTECTOR_SCORE               = 2;
    static final int BISHOP_AGGRESSOR_SCORE               = 5;
    static final int BISHOP_UNDEVELOPED_PENALTY           = -20;
    static final int BISHOPS_PRIME_DIAGONAL_BONUS         = 20;

    // rook valuation
    static final int ROOK_ON_SEVENTH_BONUS                = 25;
    static final int ROOK_MOBILITY_SCORE                  = 1;
    static final int ROOK_PROTECTOR_SCORE                 = 2;
    static final int ROOK_AGGRESSOR_SCORE                 = 2;
    static final int ROOK_NOT_DEVELOPED                   = -5;
    static final int ROOK_OPEN_FILE_BONUS                 = 15;
    static final int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 10;
    static final int ROOK_PROTECTS_QUEEN                  = 5;
    static final int ROOK_SAME_ROW                      = 5;
    
    // queen valuation
    static final int QUEEN_ON_SEVENTH_BONUS               = 10;
    static final int QUEEN_MOBILITY_SCORE                 = 1;
    static final int QUEEN_PROTECTOR_SCORE                = 2;
    static final int QUEEN_AGGRESSOR_SCORE                = 5;
    static final int QUEEN_PROTECTS_ROOK                  = 10;
    static final int QUEEN_HATES_PAWNS                    = 10;
    
    // king valuation
    static int KING_PAWN_PROTECT_BONUS              = 5;
    static final int KING_PROTECTOR_SCORE                 = 1;
    static final int KING_AGGRESSOR_SCORE                 = 1;

}
