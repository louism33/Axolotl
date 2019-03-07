package com.github.louism33.axolotl.moveordering;

import com.github.louism33.axolotl.search.MoveOrdererBetter;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.moveordering.MoveOrderer.MOVE_MASK;
import static com.github.louism33.axolotl.moveordering.MoveOrderer.getMoveScore;
import static com.github.louism33.chesscore.MoveConstants.MOVE_UPPER_BOUND;

public class MoveOrdererTest {

    @org.junit.jupiter.api.Test
    public void moveScoreTest() {
        Chessboard board = new Chessboard();
        int[] ints = board.generateLegalMoves();
        int move = ints[0];

        int max = 64;
        for (int s = 1; s < max; s++) {
            int scoredMove = MoveOrderer.buildMoveScore(move, s);
            int scoreFromMove = getMoveScore(scoredMove);

            Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(scoredMove));
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
            int max = 64;
            for (int s = 1; s < max; s++) {
                int moveScore = MoveOrderer.buildMoveScore(move, s);
                int scoreFromMove = getMoveScore(moveScore);

                Assert.assertTrue(moveScore > 0);
                Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(moveScore));
                Assert.assertEquals(scoreFromMove, s);
            }
        }
    }

    @org.junit.jupiter.api.Test
    void regularBoard() {
        verifyMoveSizeToDepth(3, new Chessboard());

        verifyMoveSizeToDepth(4, new Chessboard());
    }

    @org.junit.jupiter.api.Test
    void AvoidIllegalEPCapture() {
        verifyMoveSizeToDepth(5, new Chessboard("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"));

        verifyMoveSizeToDepth(5, new Chessboard("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"));
    }


    @Test
    void EPCaptureChecksOpponent() {
        verifyMoveSizeToDepth(5, new Chessboard("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"));

        verifyMoveSizeToDepth(5, new Chessboard("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"));
    }



    private static void verifyMoveSizeToDepth(int depth, Chessboard board) {
        final Chessboard initial = new Chessboard(board);
        Assert.assertEquals(board, initial);

        countFinalNodesAtDepthHelper(board, depth);

        Assert.assertEquals(board, new Chessboard(board));
        Assert.assertEquals(board, initial);
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        int[] moves = board.generateLegalMoves();
        final int maxMoves = moves[moves.length - 1];
        if (depth == 1){
            return maxMoves;
        }

        int[] unscoredMoves = new int[moves.length];
        System.arraycopy(moves, 0, unscoredMoves, 0, moves.length);
        
        if (depth % 2 == 0) {
            MoveOrdererBetter.scoreMoves(moves, board, 0, 0);
        }
        else {
            MoveOrdererBetter.scoreMovesAtRoot(moves, maxMoves, board);
        }
        
        for (int i = 0; i < maxMoves; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            Assert.assertTrue(move >= MOVE_UPPER_BOUND);

            Assert.assertEquals((move & MOVE_MASK), unscoredMoves[i]);
            
            Assert.assertTrue((move & MOVE_MASK) < MOVE_UPPER_BOUND);

            board.makeMoveAndFlipTurn(move);

            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;

            board.unMakeMoveAndFlipTurn();
            Assert.assertEquals(board, new Chessboard(board));

        }
        return temp;
    }

}