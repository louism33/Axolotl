package com.github.louism33.axolotl.main;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EngineTestMateInSix {

    private static final int timeLimit = 30000;

    @Test
    void test1() {
        Chessboard board = new Chessboard("2r3k1/p2R1p2/1p5Q/4N3/7P/4P3/b5PK/5q2 w - - 1 0");
        System.out.println(board);

        int move = Engine.searchFixedTime(board, timeLimit);
        System.out.println(MoveParser.toString(move));

        int moveToWin = MoveParserFromAN.destinationIndex(board, "Nc6");
        int destination = MoveParser.getDestinationIndex(move);
        
        Assert.assertEquals(moveToWin, destination);
    }
    
}
