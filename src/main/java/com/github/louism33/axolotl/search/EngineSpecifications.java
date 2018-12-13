package com.github.louism33.axolotl.search;

public class EngineSpecifications {

    public static int MAX_DEPTH                                      = 14;
    public static boolean ALLOW_TIME_LIMIT                           = true;

    public static final int[] ASPIRATION_WINDOWS                     = {10, 100, 1000};
    public static final int ASPIRATION_MAX_TRIES                     = ASPIRATION_WINDOWS.length;
}
