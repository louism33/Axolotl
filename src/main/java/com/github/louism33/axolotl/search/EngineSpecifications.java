package com.github.louism33.axolotl.search;

public class EngineSpecifications {
    
    public final boolean INFO_LOG = true;
    public boolean ALLOW_TIME_LIMIT = true;

    public final boolean ALLOW_PRINCIPLE_VARIATION_SEARCH     = true;
    public final boolean ALLOW_ASPIRATION_WINDOWS             = true;

    public final boolean ALLOW_KILLERS                        = true;
    public final boolean ALLOW_MATE_KILLERS                   = true;
    public final boolean ALLOW_HISTORY_MOVES                  = true;

    public final boolean ALLOW_MATE_DISTANCE_PRUNING          = true;
    public final boolean ALLOW_EXTENSIONS                     = true;

    public final boolean ALLOW_INTERNAL_ITERATIVE_DEEPENING   = true;

    public final boolean ALLOW_LATE_MOVE_REDUCTIONS           = true;
    public final boolean ALLOW_LATE_MOVE_PRUNING              = true;
    public final boolean ALLOW_NULL_MOVE_PRUNING              = true;

    public final boolean ALLOW_ALPHA_RAZORING                 = true;
    public final boolean ALLOW_BETA_RAZORING                  = true;
    public final boolean ALLOW_FUTILITY_PRUNING               = true;

    public final boolean ALLOW_SEE_PRUNING                    = true;

    public final boolean ALLOW_QUIESCENCE_SEE_PRUNING         = true;
    public final boolean ALLOW_QUIESCENCE_FUTILITY_PRUNING    = true;
}
