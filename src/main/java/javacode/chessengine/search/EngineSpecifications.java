package javacode.chessengine.search;

public class EngineSpecifications {
    
    EngineSpecifications(){
        
    }

    public boolean ALLOW_TIME_LIMIT = true;

    public boolean ALLOW_PRINCIPLE_VARIATION_SEARCH   = true;
    public boolean ALLOW_ASPIRATION_WINDOWS           = true;

    public boolean ALLOW_KILLERS               = true;
    public boolean ALLOW_MATE_KILLERS          = true;
    public boolean ALLOW_HISTORY_MOVES         = true;

    public boolean ALLOW_MATE_DISTANCE_PRUNING        = true;
    public boolean ALLOW_EXTENSIONS                   = true;

    public boolean ALLOW_INTERNAL_ITERATIVE_DEEPENING = false;

    public boolean ALLOW_LATE_MOVE_REDUCTIONS         = false;
    public boolean ALLOW_LATE_MOVE_PRUNING            = true;
    public boolean ALLOW_NULL_MOVE_PRUNING            = true;

    public boolean ALLOW_ALPHA_RAZORING               = true;
    public boolean ALLOW_BETA_RAZORING                = true;
    public boolean ALLOW_FUTILITY_PRUNING             = true;

    public boolean ALLOW_SEE_PRUNING                  = true;

    public boolean ALLOW_QUIESCENCE_SEE_PRUNING       = false;
    public boolean ALLOW_QUIESCENCE_FUTILITY_PRUNING  = false;
}
