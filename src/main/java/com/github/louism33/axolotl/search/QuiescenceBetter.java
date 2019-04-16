package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MINIMUM;
import static com.github.louism33.axolotl.search.MoveOrdererBetter.*;
import static com.github.louism33.chesscore.MoveConstants.FIRST_FREE_BIT;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

public final class QuiescenceBetter {

    public static int quiescenceSearch(Chessboard board, int alpha, int beta){

        int[] moves = board.generateLegalMoves();
        
        int standPatScore = SHORT_MINIMUM;

        boolean inCheck = board.inCheckRecorder;
        
        if (!inCheck){
            standPatScore = Evaluator.eval(board, moves);

            if (standPatScore >= beta){
                return standPatScore;
            }

            if (standPatScore > alpha){
                alpha = standPatScore;
            }
        }

        Assert.assertFalse(standPatScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);

        if (!inCheck) {
            scoreMovesQuiescence(moves, board);
            final int realMoves = MoveParser.numberOfRealMoves(moves);
            Ints.sortDescending(moves, 0, realMoves);
        }
        else {
            scoreMoves(moves, board, 0, 0);
            final int realMoves = MoveParser.numberOfRealMoves(moves);
            Ints.sortDescending(moves, 0, realMoves);
        }

        for (int i = 0; i < moves.length; i++) {

            final int move = moves[i];
            if (move == 0){
                break;
            }

            final int loudMoveScore = getMoveScore(move);

            final boolean captureMove = MoveParser.isCaptureMove(move);
            final boolean promotionMove = MoveParser.isPromotionMove(move);
            
            if (!inCheck && loudMoveScore != 0) {
                Assert.assertTrue(captureMove || promotionMove);
            }
            
            if (!inCheck && loudMoveScore == 0) {
                break;
            }

            final int loudMove = move & MOVE_MASK_WITH_CHECK;
            if (!inCheck) {
                if (i == 0) {
                    Assert.assertTrue(moves[i] >= moves[i + 1]);
                } else {
                    Assert.assertTrue(moves[i] <= moves[i - 1]);
                    Assert.assertTrue(moves[i] >= moves[i + 1]);
                }
                Assert.assertTrue(moves[i] > FIRST_FREE_BIT);
            }

            if (!inCheck) {
                Assert.assertTrue(captureMove || promotionMove);
            }

            board.makeMoveAndFlipTurn(loudMove);
            Engine.numberOfQMovesMade[0]++;

            int score = -quiescenceSearch(board, -beta, -alpha);

            board.unMakeMoveAndFlipTurn();

            if (score > alpha) {
                alpha = score;
                if (alpha >= beta) {
                    return alpha;
                }
            }

        }
        return alpha;
    }

}

