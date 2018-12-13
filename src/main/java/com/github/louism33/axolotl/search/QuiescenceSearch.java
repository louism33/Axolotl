package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.moveordering.MoveOrderingConstants;
import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;

class QuiescenceSearch {

    static int quiescenceSearch(Chessboard board, int alpha, int beta) throws IllegalUnmakeException {

        int[] moves = board.generateLegalMoves();

        int standPatScore = EvaluationConstants.SHORT_MINIMUM;

        if (!board.inCheck(board.isWhiteTurn())){
//            standPatScore = Evaluator.eval(board, board.isWhiteTurn(), moves);
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
            MoveOrderer.scoreMovesQuiescence(moves, board, board.isWhiteTurn());
            int realMoves = MoveParser.numberOfRealMoves(moves);
            Ints.sortDescending(moves, 0, realMoves);
        }
        else {
            MoveOrderer.scoreMoves(moves, board, board.isWhiteTurn(), 0, 0);
            int realMoves = MoveParser.numberOfRealMoves(moves);
            Ints.sortDescending(moves, 0, realMoves);
        }
        
        
        int numberOfMovesSearched = 0;
        for (int i = 0; i < moves.length; i++) {

            if (moves[i] == 0){
                break;
            }

            int loudMoveScore = MoveOrderer.getMoveScore(moves[i]);

            if (i == 0) {
                Assert.assertTrue(moves[i] >= moves[i + 1]);
            } else {
                Assert.assertTrue(moves[i] <= moves[i - 1]);
                Assert.assertTrue(moves[i] >= moves[i + 1]);
            }

            if (moves[i] < MoveOrderingConstants.MOVE_SIZE_LIMIT) {
                System.out.println(MoveParser.toString(moves[i]) + "    " + moves[i] + "    score: " + loudMoveScore);
                Art.printLong(moves[i]);
                Art.printLong(MoveOrderingConstants.MOVE_SIZE_LIMIT);
                Assert.assertTrue(moves[i] > MoveOrderingConstants.MOVE_SIZE_LIMIT);
            }

            int loudMove = moves[i] & MoveOrderer.MOVE_MASK;

            if (loudMove > MoveOrderingConstants.MOVE_SIZE_LIMIT) {
                System.out.println(MoveParser.toString(loudMove) + "    " + loudMove);
                Art.printLong(loudMove);
                Art.printLong(MoveOrderingConstants.MOVE_SIZE_LIMIT);
                Assert.assertTrue(loudMove < MoveOrderingConstants.MOVE_SIZE_LIMIT);
            }

            boolean captureMove = MoveParser.isCaptureMove(loudMove);
            boolean promotionMove = MoveParser.isPromotionMove(loudMove);

            if (!inCheck) {
            Assert.assertTrue(captureMove || promotionMove);
            }

            board.makeMoveAndFlipTurn(loudMove);
            numberOfMovesSearched++;
            Engine.quiescentMovesMade++;


            board.flipTurn();
            Assert.assertFalse(board.inCheck(board.isWhiteTurn()));
            board.flipTurn();

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
