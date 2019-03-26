package com.github.louism33.axolotl.transpositiontable;

public class TranspositionTableConstants {

    // 7 free bits
    /*
    00000001
    11111111
    11111111
    11111111
    11111111
    11111111
    11111111
    11111111
     */

    public static final int LOWERBOUND = 0;
    public static final int EXACT = 1;
    public static final int UPPERBOUND = 2;

    public static final long TT_MOVE_MASK = 0xffffffffL;

    public static final long SCORE_CLEANER = 0xffffL;
    public static final long SCORE_MASK = 0xffff00000000L;
    public static final int scoreOffset = 32;

    static final int twoFifteen = 1 << 15;
    static final int twoSixteen = 1 << 16;

    public static final long DEPTH_MASK = 0x7f000000000000L;
    public static final int depth_offset = 48;

    public static final long FLAG_MASK = 0x180000000000000L;
    public static final int flagOffset = 55;

}
