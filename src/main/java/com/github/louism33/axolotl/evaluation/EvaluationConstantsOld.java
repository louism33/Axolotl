package com.github.louism33.axolotl.evaluation;

public final class EvaluationConstantsOld {

    // general numbers
    public static final int SHORT_MINIMUM = -31000;
    public static final int SHORT_MAXIMUM = 31000;
    public static final int IN_CHECKMATE_SCORE = -30000;
    public static final int CHECKMATE_ENEMY_SCORE = -IN_CHECKMATE_SCORE;
    public static final int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static final int CHECKMATE_ENEMY_SCORE_MAX_PLY = -IN_CHECKMATE_SCORE_MAX_PLY;
    public static final int IN_STALEMATE_SCORE = 0;

    // piece values
//    public static final int PAWN_SCORE                    = 100;
//    public static final int KNIGHT_SCORE                  = 300;
//    public static final int BISHOP_SCORE                  = 330;
//    public static final int ROOK_SCORE                    = 540;
//    public static final int QUEEN_SCORE                   = 950;

    // misc factors
//    static final int MY_TURN_BONUS                        = 8;
//    static final int SPACE                                = 4;

    // pawn valuation
//    static final int PAWN_UNBLOCKED                       = 12;
//    static final int PAWN_OPEN_STOP_SQUARE                = 12;
//    static final int PAWN_ISOLATED                        = 12;
//    static final int PAWN_BACKWARDS                       = 10;
//    static final int ROOK_OR_QUEEN_BEHIND_PP              = 12;
//    static final int PAWN_DOUBLED                         = 10;
//    static final int PAWN_PROTECTED_BY_PAWNS              = 6;
//    static final int PAWN_NEIGHBOURS                      = 2;
//    static final int PAWN_THREATENS_BIG_THINGS            = 8;
//    static final int PAWN_CANDIDATE                       = 6;
//    static final int PAWN_YOUNG_PASSED                    = 4;
    
    // common
//    static final int PIECE_BEHIND_PAWN                    = 10;

    // knights valuation
//    static final int KNIGHT_PAWN_NUMBER_BONUS             = 1;
//    static final int KNIGHT_ATTACK_KING_UNITS             = 2;
//    static final int KNIGHT_ON_OUTPOST_BONUS              = 20;
//    static final int KNIGHT_REACH_OUTPOST_BONUS           = 8;
//    static final int KNIGHT_PROTECTED_PAWN                = 8;
//    static final int KNIGHT_FORK                          = 12;
    
    // bishop valuation
//    static final int BISHOP_COLOUR_PAWNS                  = 2;
//    static final int BISHOP_PROTECTED_PAWN                = 6;
//    static final int BISHOP_ATTACK_KING_UNITS             = 2;
//    static final int BISHOP_DOUBLE                        = 20;
//    static final int BISHOP_PRIME_DIAGONAL                = 10;
//    static final int BISHOP_ON_OUTPOST_BONUS              = 12;
//    static final int BISHOP_REACH_OUTPOST_BONUS           = 6;

    // rook valuation
//    static final int ROOK_ON_SEVENTH_BONUS                = 25;
//    static final int ROOK_OPEN_FILE_BONUS                 = 18;
//    static final int ROOK_ATTACK_KING_UNITS               = 3;
//    static final int ROOK_ON_SEMI_OPEN_FILE_BONUS         = 10;
//    static final int TRAPPED_ROOK                         = 16;
//    static final int ROOKS_ATTACK_UNDEFENDED_PAWNS        = 6;
//    static final int ROOK_BATTERY_SCORE                   = 8;
    
    // queen valuation
//    static final int QUEEN_ATTACK_KING_LOOKUP_UNITS       = 5;
//    static final int MISSING_QUEEN_KING_SAFETY_UNITS      = 4;
    
    // king valuation
//    static final int FRIENDLY_PIECE_NEAR_KING             = 1;
//    static final int KING_NEAR_SEMI_OPEN_FILE_LOOKUP      = 3;

//    static final int[] KING_SAFETY_ARRAY                  =  // -s are good
//            {   
//                    -96, -88, -74, -68, -66, -62, -54, -46, 
//                    -36, -30, -26, -22, -18, -16,  -10, -6,
// /* neutral: 20 */    0,  12,  24,  48,  64,  96, 128, 156,
//                    166, 206, 210, 225, 250, 275, 300, 320,
//                    330, 340, 350, 360, 370, 380, 390, 400,
//                    460, 470, 480, 490, 500, 510, 520, 530,
//                    540, 541, 542, 543, 544, 545, 546, 547,
//                    548, 549, 550, 551, 552, 553, 554, 555,
//            };

}
