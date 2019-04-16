package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

public final class EngineSpecifications {

    public static int THREAD_NUMBER                                  = 1;
    public static final int MAX_THREADS                              = 8;
    public static boolean DEBUG                                      = true;
    public static boolean PRINT_EVAL                                 = false;
    public static int MAX_DEPTH                                      = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public final static int ABSOLUTE_MAX_DEPTH                       = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public final static int MAX_DEPTH_HARD                           = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public static boolean ALLOW_TIME_LIMIT                           = true;

    /*
    one mb is 1024 KB
    1024 * 1024 Bytes
     / (8*2), because two longs, one for key and one for entry
     */
    public static int DEFAULT_TABLE_SIZE_MB                          = 128;
    public static final int MIN_TABLE_SIZE_MB                        = 1;
    public static final int MAX_TABLE_SIZE_MB                        = 1024;
    
    public static final int TABLE_SIZE_PER_MB                        = 1024 * 1024 / (8 * 2);
    
    public static int TABLE_SIZE                                     = DEFAULT_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
    public static final int MIN_TABLE_SIZE                           = MIN_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
    public static final int MAX_TABLE_SIZE                           = MAX_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;

    public static int DEFAULT_PAWN_TABLE_SIZE_MB                     = 16;
    public static int PAWN_TABLE_SIZE                                = DEFAULT_PAWN_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
}
