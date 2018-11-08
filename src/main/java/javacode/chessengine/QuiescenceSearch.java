package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evaluation.Evaluator;

import java.util.List;

import static javacode.chessengine.FutilityPruning.quiescenceFutilityMargin;
import static javacode.chessengine.SEEPruning.seeScore;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;

class QuiescenceSearch {

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
    special searchMyTime for captures only
     */
    int quiescenceSearch(Chessboard board, ZobristHash zobristHash,
                                long startTime, long timeLimitMillis,
                                int alpha, int beta){

        List<Move> moves = generateLegalMoves(board, board.isWhiteTurn());

        /*
        the score we get from not making captures anymore
         */
        int standPatScore = this.evaluator.evalWithoutCM(board, board.isWhiteTurn(), moves);

        if (standPatScore > this.evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY){
            System.out.println("checkmate in QSearch ????? " + standPatScore);
        }

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
            if (this.engine.DEBUG) {
                this.engine.statistics.numberOfQuiescentEvals++;
            }
            return standPatScore;
        }

        List<Move> orderedCaptureMoves = this.moveOrderer.orderMovesQuiescence(board, board.isWhiteTurn(), moves);

        int numberOfMovesSearched = 0;
        for (Move captureMove : orderedCaptureMoves){

            /*
            Quiescence Futility Pruning:
            if this is a particularly low scoring situation skip this move
             */
            if (this.engine.ALLOW_QUIESCENCE_FUTILITY_PRUNING){
                if (quiescenceFutilityMargin
                        + standPatScore
                        + this.evaluator.getScoreOfDestinationPiece(board, captureMove)
                        < alpha){
                    continue;
                }
            }
            
            if (this.engine.ALLOW_QUIESCENCE_SEE_PRUNING){
                int seeScore = seeScore(board, captureMove, evaluator);
                if (seeScore <= -300) {
                    this.engine.statistics.numberOfSuccessfulQuiescentSEEs++;
                    continue;
                }
            }
            
            
            MoveOrganiser.makeMoveMaster(board, captureMove);
            MoveOrganiser.flipTurn(board);
            numberOfMovesSearched++;

            if (this.engine.DEBUG){
                this.engine.statistics.numberOfQuiescentMovesMade++;
            }

            int score = -quiescenceSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);

            if (score >= beta){
                if (this.engine.DEBUG){
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
