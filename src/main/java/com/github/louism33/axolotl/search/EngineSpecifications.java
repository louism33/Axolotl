package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

public final class EngineSpecifications {

    public static boolean SPSA = false;
    public static boolean MASTER_DEBUG = false;

    public static int NUMBER_OF_THREADS = 1;
    public static final int DEFAULT_THREAD_NUMBER = 1;
    public static final int MAX_THREADS = 8;
    public static boolean PRINT_PV = true;
    public static boolean DEBUG = false;
    public static boolean PRINT_EVAL = false;
    public final static int ABSOLUTE_MAX_DEPTH = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;
    public final static int MAX_DEPTH_HARD = Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH - 2;

    /*
    one mb is 1024 KB
    1024 * 1024 Bytes / 64
     */
    public static int DEFAULT_TABLE_SIZE_MB = 128;
    public static final int MIN_TABLE_SIZE_MB = 1;
    public static final int MAX_TABLE_SIZE_MB = 1024;

    public static final int TABLE_SIZE_PER_MB = 1024 * 1024 / 64; // bits in a mb div by long

    public static int TABLE_SIZE_MB = 128;
    public static int TABLE_SIZE = DEFAULT_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
    public static final int MIN_TABLE_SIZE = MIN_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
    public static final int MAX_TABLE_SIZE = MAX_TABLE_SIZE_MB * TABLE_SIZE_PER_MB;
}
