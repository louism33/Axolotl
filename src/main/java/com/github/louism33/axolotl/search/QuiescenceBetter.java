package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;

public class QuiescenceBetter {

    static int quiescenceSearchBetter(Chessboard board, int alpha, int beta){

        int[] moves = new int[128];
        
        final int[] m = board.generateLegalMoves();
        System.arraycopy(m, 0, moves, 0, 128);
        
        int standPatScore = EvaluationConstants.SHORT_MINIMUM;

        if (!board.inCheck(board.isWhiteTurn())){
            standPatScore = Evaluator.evalNOCM(board, board.isWhiteTurn(), moves);

            if (standPatScore >= beta){
                return standPatScore;
            }

            if (standPatScore > alpha){
                alpha = standPatScore;
            }
        }

        boolean inCheck = board.inCheck(board.isWhiteTurn());

        Assert.assertFalse(standPatScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);

        if (!inCheck) {
            MoveOrdererBetter.scoreMovesQuiescence(moves, board);
            int realMoves = MoveParser.numberOfRealMoves(moves);
            Ints.sortDescending(moves, 0, realMoves);
        }
        else {
            MoveOrdererBetter.scoreMoves(moves, board, 0, 0);
            int realMoves = MoveParser.numberOfRealMoves(moves);
            Ints.sortDescending(moves, 0, realMoves);
        }


        int numberOfMovesSearched = 0;
        for (int i = 0; i < moves.length; i++) {

            if (moves[i] == 0){
                break;
            }

            int loudMoveScore = MoveOrdererBetter.getMoveScore(moves[i]);

//            if (i == 0) {
//                Assert.assertTrue(moves[i] >= moves[i + 1]);
//            } else {
//                Assert.assertTrue(moves[i] <= moves[i - 1]);
//                Assert.assertTrue(moves[i] >= moves[i + 1]);
//            }
//
//
//            Assert.assertTrue(moves[i] > MoveOrderingConstants.MOVE_SIZE_LIMIT);


            int loudMove = moves[i] & MoveOrdererBetter.MOVE_MASK;

            boolean captureMove = MoveParser.isCaptureMove(loudMove);
            boolean promotionMove = MoveParser.isPromotionMove(loudMove);

//            if (!inCheck) {
//                Assert.assertTrue(captureMove || promotionMove);
//            }

            board.makeMoveAndFlipTurn(loudMove);
            numberOfMovesSearched++;
            EngineBetter.numberOfQMovesMade[0]++;

            int score = -quiescenceSearchBetter(board, -beta, -alpha);

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

