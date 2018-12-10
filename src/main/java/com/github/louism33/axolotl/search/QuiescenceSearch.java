package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;

class QuiescenceSearch {

    static int quiescenceSearch(Chessboard board, int alpha, int beta) throws IllegalUnmakeException {

        int[] moves = board.generateLegalMoves();

        int standPatScore = Evaluator.eval(board, board.isWhiteTurn(), moves);

        Assert.assertFalse(standPatScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);

        if (standPatScore >= beta){
            return standPatScore;
        }

        if (standPatScore > alpha){
            alpha = standPatScore;
        }

        /*
        no more captures to make or no more moves at all
         */
        if (QuiescentSearchUtils.isBoardQuiet(board, moves) || moves.length == 0){
            Statistics.numberOfQuiescentEvals++;
            return standPatScore;
        }

        MoveOrderer.scoreMovesQuiescence(moves, board, board.isWhiteTurn());

        int numberOfMovesSearched = 0;
        for (int i = 0; i < moves.length; i++) {
            
            if (moves[i] == 0){
                break;
            }
            
            int loudMove = moves[i];
            
            boolean captureMove = MoveParser.isCaptureMove(loudMove);
            boolean promotionMove = MoveParser.isPromotionMove(loudMove);

            Assert.assertTrue(captureMove || promotionMove);
            
            board.makeMoveAndFlipTurn(loudMove);
            numberOfMovesSearched++;
            Engine.quiescentMovesMade++;

            int score = -quiescenceSearch(board, -beta, -alpha);

            board.unMakeMoveAndFlipTurn();

            if (score >= beta) {
//                Statistics.whichMoveWasTheBestQuiescence[numberOfMovesSearched - 1]++;
                return score;
            }

            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

}
