package com.github.louism33.axolotl.search;

public class EngineSpecifications {
    
    public static final boolean INFO_LOG = true;
    public static int MAX_DEPTH = 12;
    public static boolean ALLOW_TIME_LIMIT = true;

    public static final int DEFAULT_TABLE_SIZE                = 1 << 15;
    
    public static final boolean ALLOW_PRINCIPLE_VARIATION_SEARCH     = true;
    public static final boolean ALLOW_ASPIRATION_WINDOWS             = true;

    public static final boolean ALLOW_KILLERS                        = false;
    public static final boolean ALLOW_MATE_KILLERS                   = false;
    public static final boolean ALLOW_HISTORY_MOVES                  = false;

    public static final boolean ALLOW_MATE_DISTANCE_PRUNING          = true;
    public static final boolean ALLOW_EXTENSIONS                     = true;

    public static final boolean ALLOW_INTERNAL_ITERATIVE_DEEPENING   = false;

    public static final boolean ALLOW_LATE_MOVE_REDUCTIONS           = false;
    public static final boolean ALLOW_LATE_MOVE_PRUNING              = false;
    public static final boolean ALLOW_NULL_MOVE_PRUNING              = false;

    public static final boolean ALLOW_ALPHA_RAZORING                 = false;
    public static final boolean ALLOW_BETA_RAZORING                  = false;
    public static final boolean ALLOW_FUTILITY_PRUNING               = false;

    public static final boolean ALLOW_SEE_PRUNING                    = false;

    public static final boolean ALLOW_QUIESCENCE_SEE_PRUNING         = false;
    public static final boolean ALLOW_QUIESCENCE_FUTILITY_PRUNING    = false;
}
