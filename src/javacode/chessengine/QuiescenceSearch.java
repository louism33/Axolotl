package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;

import java.util.List;

import static javacode.chessengine.Engine.ALLOW_TIME_LIMIT;
import static javacode.chessengine.Engine.DEBUG;
import static javacode.chessengine.Engine.whichMoveWasTheBestQuiescence;
import static javacode.chessengine.MoveOrderer.orderMovesQuiescence;
import static javacode.chessengine.QuiescentSearchUtils.isBoardQuiet;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.evaluation.Evaluator.IN_CHECKMATE_SCORE_MAX_PLY;
import static javacode.evaluation.Evaluator.eval;

class QuiescenceSearch {

    /*
    Quiescence Search: 
    special search for captures only
     */
    static int quiescenceSearch(Chessboard board, ZobristHash zobristHash,
                                long startTime, long timeLimitMillis,
                                int alpha, int beta){
        
        List<Move> moves = generateLegalMoves(board, board.isWhiteTurn());

        /*
        the score we get from not making captures anymore
         */
        int standPatScore = eval(board, board.isWhiteTurn(), moves);

        if (ALLOW_TIME_LIMIT) {
            long currentTime = System.currentTimeMillis();
            long timeLeft = startTime + timeLimitMillis - currentTime;
            if (timeLeft < 0) {
                return standPatScore;
            }
        }
        
        if (standPatScore > -IN_CHECKMATE_SCORE_MAX_PLY){
            return standPatScore;
        }
        
        if (standPatScore >= beta){
            return beta;
        }

        if (standPatScore > alpha){
            alpha = standPatScore;
        }
        
        /*
        no more captures to make or no more moves at all
         */
        if (isBoardQuiet(board, moves) || moves.size() == 0){
            if (Engine.DEBUG) {
                Engine.numberOfQuiescentEvals++;
            }
            return standPatScore;
        }

        List<Move> orderedCaptureMoves = orderMovesQuiescence(board, board.isWhiteTurn(), moves);

        int numberOfMovesSearched = 0;
        for (Move captureMove : orderedCaptureMoves){
            MoveOrganiser.makeMoveMaster(board, captureMove);
            MoveOrganiser.flipTurn(board);
            numberOfMovesSearched++;

            if (Engine.DEBUG){
                Engine.numberOfQuiescentMovesMade++;
            }

            int score = -quiescenceSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);

            if (score >= beta){
                if (DEBUG){
                    whichMoveWasTheBestQuiescence[numberOfMovesSearched-1]++;
                }
                return beta;
            }

            if (score > alpha){
                alpha = score;
            }
        }
        return alpha;
    }


}
