package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evaluation.Evaluator;

import java.util.List;

import static javacode.chessengine.Engine.ALLOW_TIME_LIMIT;
import static javacode.chessengine.MoveOrderer.orderMovesQuiescence;
import static javacode.chessengine.QuiescentSearchUtils.isBoardQuiet;

class QuiescenceSearch {

    /*
    Quiescence Search: 
    special search for captures only
     */
    static int quiescenceSearch(Chessboard board, ZobristHash zobristHash,
                                long startTime, long timeLimitMillis,
                                int alpha, int beta){
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        /*
        the score we get from not making captures anymore
         */
        int standPatScore = Evaluator.eval(board, board.isWhiteTurn(), moves);

        if (ALLOW_TIME_LIMIT) {
            long currentTime = System.currentTimeMillis();
            long timeLeft = startTime + timeLimitMillis - currentTime;
            if (timeLeft < 0) {
                return standPatScore;
            }
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

        if (standPatScore >= beta){
            return beta;
        }

        if (standPatScore > alpha){
            alpha = standPatScore;
        }

        List<Move> orderedCaptureMove = orderMovesQuiescence(board, board.isWhiteTurn(), moves);

        for (Move captureMove : orderedCaptureMove){
            MoveOrganiser.makeMoveMaster(board, captureMove);
            MoveOrganiser.flipTurn(board);

            if (Engine.DEBUG){
                Engine.numberOfQuiescentMovesMade++;
            }

            int score = -quiescenceSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);

            if (score >= beta){
                return beta;
            }

            if (score > alpha){
                alpha = score;
            }
        }
        return alpha;
    }


}
