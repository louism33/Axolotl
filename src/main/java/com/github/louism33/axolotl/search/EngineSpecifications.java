package com.github.louism33.axolotl.search;

public final class EngineSpecifications {

    public static int THREAD_NUMBER                                  = 1;
    public static final int MAX_THREADS                              = 8;
    public static boolean DEBUG                                      = false;
    public static boolean PRINT                                      = false;
    public static int MAX_DEPTH                                      = 62;
    public static int ABSOLUTE_MAX_DEPTH                             = 62;
    public static final int MAX_DEPTH_HARD                           = 62;
    public static boolean ALLOW_TIME_LIMIT                           = true;

    static final int[] ASPIRATION_WINDOWS                            = {50, 100, 1000};
    static final int ASPIRATION_MAX_TRIES                            = ASPIRATION_WINDOWS.length;
    /*
    one mb is 1024 KB
    1024 * 1024 Bytes
     / (8*2), because two longs, one for key and one for entry
     */
    public static final int TABLE_SIZE_PER_MB                        = 1024 * 1024 / (8 * 2);
    public static int TABLE_SIZE                                     = 16 * TABLE_SIZE_PER_MB; // 16mb
    public static final int MIN_TABLE_SIZE                           = TABLE_SIZE_PER_MB; // 1mb
    public static final int MAX_TABLE_SIZE                           = 64 * TABLE_SIZE_PER_MB; // 960mb
}
