package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

public class EndgameKPK {

    public static int evaluateKPK(Chessboard board, int strongerTurn) {
        return 0;
    }

    private static final int[] centreManhattanDistance = {
            6, 5, 4, 3, 3, 4, 5, 6,
            5, 4, 3, 2, 2, 3, 4, 5,
            4, 3, 2, 1, 1, 2, 3, 4,
            3, 2, 1, 0, 0, 1, 2, 3,
            3, 2, 1, 0, 0, 1, 2, 3,
            4, 3, 2, 1, 1, 2, 3, 4,
            5, 4, 3, 2, 2, 3, 4, 5,
            6, 5, 4, 3, 3, 4, 5, 6
    };

}
