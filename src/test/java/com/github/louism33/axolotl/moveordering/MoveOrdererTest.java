package com.github.louism33.axolotl.moveordering;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import static com.github.louism33.axolotl.search.MoveOrdererBetter.buildMoveScore;
import static com.github.louism33.axolotl.search.MoveOrdererBetter.getMoveScore;

public class MoveOrdererTest {

    @org.junit.jupiter.api.Test
    public void moveScoreTest() {
        Chessboard board = new Chessboard();
        int[] ints = board.generateLegalMoves();
        int move = ints[0];

        int max = MoveOrderingConstants.hashScore;
        for (int s = 1; s < max; s++) {
            int moveScore = buildMoveScore(move, s);
            int scoreFromMove = getMoveScore(moveScore);

            Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(moveScore));
            Assert.assertEquals(scoreFromMove, s);
        }
    }


    @org.junit.jupiter.api.Test
    public void positiveMovesTest() {
        Chessboard board = new Chessboard("r2r2k1/pp2bppp/2p1p3/4qb1P/8/1BP1BQ2/PP3PP1/2KR3R b - - 0 1");

        int[] moves = board.generateLegalMoves();
        for (int m = 0; m < moves.length; m++){
            int move = moves[m];
            if (move == 0){
                break;
            }
            int max = MoveOrderingConstants.hashScore;
            for (int s = 1; s < max; s++) {
                int moveScore = buildMoveScore(move, s);
                int scoreFromMove = getMoveScore(moveScore);

                Assert.assertTrue(moveScore > 0);
                Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(moveScore));
                Assert.assertEquals(scoreFromMove, s);
            }
        }
    }

}