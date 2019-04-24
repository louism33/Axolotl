package com.github.louism33.axolotl.evaluation;

public final class Score {

    /*
    0eeeeeee
    eeeeeeee
    0mmmmmmm
    mmmmmmmm
    
    e: endgame
    m: midgame
     */

    public static final int maxAbsScore = 1 << 15;
    public static final int mgScoreMask = 0xffff;

    private static final int scoreBump = 0x8000; // to avoid negative number problems arising from signed ints

    public static int getMGScore(int score) {
        return (short) (score & mgScoreMask);
    }

    public static int getEGScore(int score) {
        return ((score + scoreBump) >> 16);
    }

    public static int bs(int mg, int eg) {
        return (mg + (eg << 16));
    }

    public static int getScore(int score, int percentOfStart) {
        final int m = getMGScore(score);
        final int e = getEGScore(score);
        return ((m * percentOfStart) / 100) + ((e * (100 - percentOfStart)) / 100);
    }

    public static String toPrettyString(int score) {
        return "m: " + getMGScore(score) + ", e: " + getEGScore(score);
    }

    public static void printScore(int score) {
        System.out.println(toPrettyString(score));
    }

}
