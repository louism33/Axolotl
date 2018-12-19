package com.github.louism33.axolotl.search;

public class EngineSpecifications {

    public static int THREAD_NUMBER                                  = 2;
    public static int MAX_THREADS                                    = 8;
    public static boolean INFO                                       = true;
    public static int MAX_DEPTH                                      = 18;
    public static boolean ALLOW_TIME_LIMIT                           = true;

    static final int[] ASPIRATION_WINDOWS                            = {10, 100, 1000};
    static final int ASPIRATION_MAX_TRIES                            = ASPIRATION_WINDOWS.length;
    public static int TABLE_SIZE                                     = 16 * 62_500; // 16mb
    public static final int MAX_TABLE_SIZE                           = 16 * 62_500 * 60; // 960mb
}
