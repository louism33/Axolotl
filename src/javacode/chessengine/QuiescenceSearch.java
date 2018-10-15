package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;

import java.util.List;

import static javacode.chessengine.QuiescentSearchUtils.isNodeQuiet;

public class QuiescenceSearch {

    static int numberOfQuiescentEvals = 0;

    static int quiescenceSearch(Chessboard board, int alpha, int beta){
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        int standPatScore = Evaluator.eval(board, board.isWhiteTurn());
        
        if (isNodeQuiet(board, moves)){
            numberOfQuiescentEvals++;
            return standPatScore;
        }
        
        if (moves.size() == 0) {
            numberOfQuiescentEvals++;
            return standPatScore;
        }
        
        if (standPatScore >= beta){
            return beta;
        }

        if (standPatScore > alpha){
            alpha = standPatScore;
        }
        
        List<Move> orderedCaptureMove = MoveOrderer.
                orderMovesQuiescence(board, QuiescentSearchUtils.onlyCaptureMoves(board, moves));

        Move bestMove = orderedCaptureMove.get(0);
        int bestScore = -10000;

        for (Move captureMove : orderedCaptureMove){
            MoveOrganiser.makeMoveMaster(board, captureMove);
            MoveOrganiser.flipTurn(board);

            int score = -quiescenceSearch(board, -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);

            if (score > bestScore){
                bestScore = score;
            }

            if (score > alpha){
                alpha = score;
                if (alpha >= beta){
                    break;
                }
            }
        }


        return bestScore;
    }





}
