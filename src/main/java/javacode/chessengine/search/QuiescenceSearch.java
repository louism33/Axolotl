package javacode.chessengine.search;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessengine.moveordering.MoveOrderer;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveParser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import org.junit.Assert;

import java.util.List;

import static javacode.chessengine.evaluation.Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static javacode.chessengine.search.FutilityPruning.quiescenceFutilityMargin;
import static javacode.chessengine.search.SEEPruning.seeScore;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;

public class QuiescenceSearch {
    private Engine engine;
    private MoveOrderer moveOrderer;
    private QuiescentSearchUtils quiescentSearchUtils;
    private Evaluator evaluator;

    QuiescenceSearch(Engine engine, MoveOrderer moveOrderer, Evaluator evaluator){
        this.engine = engine;
        this.moveOrderer = moveOrderer;
        this.evaluator = evaluator;
        this.quiescentSearchUtils = new QuiescentSearchUtils(moveOrderer);
    }
    /*
    Quiescence Search: 
    special search for captures only
     */
    int quiescenceSearch(Chessboard board, int alpha, int beta){

        List<Move> moves = generateLegalMoves(board, board.isWhiteTurn());

        /*
        the score we get from not making captures anymore
         */
        int standPatScore = this.evaluator.evalWithoutCM(board, board.isWhiteTurn(), moves);

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
        if (this.quiescentSearchUtils.isBoardQuiet(board, moves) || moves.size() == 0){
            if (this.engine.INFO_LOG) {
                this.engine.statistics.numberOfQuiescentEvals++;
            }
            return standPatScore;
        }

        List<Move> orderedCaptureMoves = this.moveOrderer
                .orderMovesQuiescence(board, board.isWhiteTurn(), moves);

        int numberOfMovesSearched = 0;
        for (Move loudMove : orderedCaptureMoves){
            boolean captureMove = this.moveOrderer.moveIsCapture(board, loudMove);
            boolean promotionMove = MoveParser.isPromotionMove(loudMove);

            Assert.assertTrue(captureMove || promotionMove);
            
            /*
            Quiescence Futility Pruning:
            if this is a particularly low scoring situation skip this move
             */
            if (this.engine.getEngineSpecifications().ALLOW_QUIESCENCE_FUTILITY_PRUNING){
                if (captureMove
                        && quiescenceFutilityMargin
                        + standPatScore
                        + this.evaluator.getScoreOfDestinationPiece(board, loudMove)
                        < alpha){
                    continue;
                }
            }

            if (captureMove && this.engine.getEngineSpecifications().ALLOW_QUIESCENCE_SEE_PRUNING){
                int seeScore = seeScore(board, loudMove, evaluator);
                if (seeScore <= -300) {
                    this.engine.statistics.numberOfSuccessfulQuiescentSEEs++;
                    continue;
                }
            }

            MoveOrganiser.makeMoveMaster(board, loudMove);
            MoveOrganiser.flipTurn(board);
            numberOfMovesSearched++;

            if (this.engine.INFO_LOG){
                this.engine.statistics.numberOfQuiescentMovesMade++;
            }

            int score = -quiescenceSearch(board, -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);

            if (score >= beta){
                if (this.engine.INFO_LOG){
                    this.engine.statistics.whichMoveWasTheBestQuiescence[numberOfMovesSearched-1]++;
                }
                return score;
            }

            if (score > alpha){
                alpha = score;
            }
        }
        return alpha;
    }

}