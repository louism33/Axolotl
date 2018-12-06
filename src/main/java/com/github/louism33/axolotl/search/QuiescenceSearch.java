package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.search.FutilityPruning.quiescenceFutilityMargin;
import static com.github.louism33.axolotl.search.SEEPruning.seeScore;

class QuiescenceSearch {

    /*
    Quiescence Search: 
    special search for captures only
     */
    static int quiescenceSearch(Chessboard board, int alpha, int beta) throws IllegalUnmakeException {

        int[] moves = board.generateLegalMoves();

        /*
        the score we get from not making captures anymore
         */
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
            Engine.statistics.numberOfQuiescentEvals++;
            return standPatScore;
        }

        MoveOrderer.MoveScore[] orderedCaptureMoves = MoveOrderer
                .orderMovesQuiescence(board, board.isWhiteTurn(), moves);

        int numberOfMovesSearched = 0;
        for (int i = 0; i < orderedCaptureMoves.length; i++) {
            final MoveOrderer.MoveScore moveScore = orderedCaptureMoves[i];
            
            if (moveScore == null){
                break;
            }
            
            int loudMove = moveScore.getMove();
            
            boolean captureMove = MoveOrderer.moveIsCapture(board, loudMove);
            boolean promotionMove = MoveParser.isPromotionMove(loudMove);

            Assert.assertTrue(captureMove || promotionMove);
            
            /*
            Quiescence Futility Pruning:
            if this is a particularly low scoring situation skip this move
             */
            if (Engine.getEngineSpecifications().ALLOW_QUIESCENCE_FUTILITY_PRUNING) {
                if (captureMove
                        && quiescenceFutilityMargin
                        + standPatScore
                        + Evaluator.getScoreOfDestinationPiece(board, loudMove)
                        < alpha) {
                    Engine.statistics.numberOfSuccessfulQuiescenceFutilities++;
                    continue;
                } else {
                    Engine.statistics.numberOfFailedQuiescenceFutilities++;
                }
            }

            if (captureMove && Engine.getEngineSpecifications().ALLOW_QUIESCENCE_SEE_PRUNING) {
                int seeScore = seeScore(board, loudMove);
                if (seeScore <= -300) {
                    Engine.statistics.numberOfSuccessfulQuiescentSEEs++;
                    continue;
                }
            }

            board.makeMoveAndFlipTurn(loudMove);
            numberOfMovesSearched++;
            Engine.statistics.numberOfQuiescentMovesMade++;

            int score = -quiescenceSearch(board, -beta, -alpha);

            board.unMakeMoveAndFlipTurn();

            if (score >= beta) {
                Engine.statistics.whichMoveWasTheBestQuiescence[numberOfMovesSearched - 1]++;
                return score;
            }

            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

}
