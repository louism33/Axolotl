package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.PawnTranspositionTable;
import com.github.louism33.chesscore.Chessboard;

public final class EngineSpecifications {
    
    public static boolean SPSA                                       = false;

    public static int NUMBER_OF_THREADS                              = 1;
    public static int DEFAULT_THREAD_NUMBER                          = 1;
    public static final int MAX_THREADS                              = 8;
    public static boolean PRINT_PV                                   = true;
    public static boolean DEBUG                                      = false;
    public static boolean PRINT_EVAL                                 = false;
    public static int MAX_DEPTH                                      = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public final static int ABSOLUTE_MAX_DEPTH                       = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public final static int MAX_DEPTH_HARD                           = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public static boolean ALLOW_TIME_LIMIT                           = true;

    /*
    one mb is 1024 KB
    1024 * 1024 Bytes
     / (64 * 2), because two longs, one for key and one for entry
     */
    public static int DEFAULT_TABLE_SIZE_MB                          = 128;
    public static final int MIN_TABLE_SIZE_MB                        = 1;
    public static final int MAX_TABLE_SIZE_MB                        = 1024;
    
    public static final int TABLE_SIZE_PER_MB                        = 1024 * 1024 / (64 * 2);

    public static int TABLE_SIZE                                     = DEFAULT_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
    public static final int MIN_TABLE_SIZE                           = MIN_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
    public static final int MAX_TABLE_SIZE                           = MAX_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;

    private static final int PAWN_TABLE_SIZE_PER_MB                  = (1024 * 1024) / (64 * (PawnTranspositionTable.ENTRIES_PER_KEY + 1));
    public static int DEFAULT_PAWN_TABLE_SIZE_MB                     = 1; 
    public static int PAWN_TABLE_SIZE                                = DEFAULT_PAWN_TABLE_SIZE_MB * PAWN_TABLE_SIZE_PER_MB;
}
