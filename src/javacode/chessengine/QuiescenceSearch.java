package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;

import java.util.List;

import static javacode.chessengine.Engine.*;
import static javacode.chessengine.FutilityPruning.*;
import static javacode.chessengine.MoveOrderer.orderMovesQuiescence;
import static javacode.chessengine.QuiescentSearchUtils.isBoardQuiet;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.*;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.evaluation.Evaluator.*;

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
        int standPatScore = evalWithoutCM(board, board.isWhiteTurn(), moves);

        if (standPatScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
            System.out.println("checkmate in QSearch ????? " + standPatScore);
        }

        if (standPatScore >= beta){
            return standPatScore;
        }

        if (standPatScore > alpha){
            alpha = standPatScore;
        }

        if (ALLOW_TIME_LIMIT) {
            long currentTime = System.currentTimeMillis();
            long timeLeft = startTime + timeLimitMillis - currentTime;
            if (timeLeft < 0) {
                System.out.println("Time up in Q search, score is: "+standPatScore);
                return standPatScore;
            }
        }
        
        /*
        no more captures to make or no more moves at all
         */
        if (isBoardQuiet(board, moves) || moves.size() == 0){
            if (Engine.DEBUG) {
                statistics.numberOfQuiescentEvals++;
            }
            return standPatScore;
        }

        List<Move> orderedCaptureMoves = orderMovesQuiescence(board, board.isWhiteTurn(), moves);

        int numberOfMovesSearched = 0;
        for (Move captureMove : orderedCaptureMoves){

            if (ALLOW_QUIESCENCE_FUTILITY_PRUNING){
                if (quiescenceFutilityMargin
                        + standPatScore
                        < alpha){
                    /*
                    add lazy eval for move
                     */
//                    continue;
                }
            }
            
            
            MoveOrganiser.makeMoveMaster(board, captureMove);
            MoveOrganiser.flipTurn(board);
            numberOfMovesSearched++;

            if (Engine.DEBUG){
                statistics.numberOfQuiescentMovesMade++;
            }

            int score = -quiescenceSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);

            if (score >= beta){
                if (DEBUG){
                    statistics.whichMoveWasTheBestQuiescence[numberOfMovesSearched-1]++;
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
