package com.github.louism33.axolotl.moveordering;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.Test;

import static com.github.louism33.axolotl.moveordering.MoveOrderer.getMoveScore;

public class MoveOrdererTest {

    @Test
    public void moveScore() {
        Chessboard board = new Chessboard();
        int[] ints = board.generateCleanLegalMoves();
        int move = ints[0];
        
        int max = 64;
        for (int s = 0; s < max; s++) {
            int moveScore = MoveOrderer.buildMoveScore(move, s);
            int scoreFromMove = getMoveScore(moveScore);
            
//            System.out.println("before: " + MoveParser.toString(move) + ", after: " +MoveParser.toString(moveScore));
//            System.out.println("score to encode: "+s);
//            System.out.println("score from move: "+ scoreFromMove);
//            System.out.println(moveScore);
//            Art.printLong(moveScore);
            
            Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(moveScore));
            Assert.assertEquals(scoreFromMove, s);
        }
    }
}