package com.github.louism33.axolotl.search;

public class EngineSpecifications {
    
    public final boolean INFO_LOG = true;
    public boolean ALLOW_TIME_LIMIT = true;

    public static final int DEFAULT_TABLE_SIZE                = 1 << 10;
    
    public final boolean ALLOW_PRINCIPLE_VARIATION_SEARCH     = true;
    public final boolean ALLOW_ASPIRATION_WINDOWS             = true;

    public final boolean ALLOW_KILLERS                        = false;
    public final boolean ALLOW_MATE_KILLERS                   = false;
    public final boolean ALLOW_HISTORY_MOVES                  = false;

    public final boolean ALLOW_MATE_DISTANCE_PRUNING          = true;
    public final boolean ALLOW_EXTENSIONS                     = true;

    public final boolean ALLOW_INTERNAL_ITERATIVE_DEEPENING   = false;

    public final boolean ALLOW_LATE_MOVE_REDUCTIONS           = false;
    public final boolean ALLOW_LATE_MOVE_PRUNING              = false;
    public final boolean ALLOW_NULL_MOVE_PRUNING              = false;

    public final boolean ALLOW_ALPHA_RAZORING                 = false;
    public final boolean ALLOW_BETA_RAZORING                  = false;
    public final boolean ALLOW_FUTILITY_PRUNING               = false;

    public final boolean ALLOW_SEE_PRUNING                    = false;

    public final boolean ALLOW_QUIESCENCE_SEE_PRUNING         = false;
    public final boolean ALLOW_QUIESCENCE_FUTILITY_PRUNING    = false;
}
