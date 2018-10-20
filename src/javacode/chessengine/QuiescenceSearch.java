package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;

import java.util.List;

import static javacode.chessengine.QuiescentSearchUtils.isBoardQuiet;

class QuiescenceSearch {

    static int numberOfQuiescentEvals = 0;

    static int quiescenceSearch(Chessboard board, ZobristHash zobristHash, int alpha, int beta){
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        int standPatScore = Evaluator.eval(board, board.isWhiteTurn());
        
        if (isBoardQuiet(board, moves)){
            numberOfQuiescentEvals++;
            return standPatScore;
        }
        
        if (moves.size() == 0) {
            numberOfQuiescentEvals++;
            return Evaluator.eval(board, board.isWhiteTurn(), moves);
        }
        
        if (standPatScore >= beta){
            return beta;
        }

        if (standPatScore > alpha){
            alpha = standPatScore;
        }
        
        List<Move> orderedCaptureMove = MoveOrderer.orderMovesQuiescence(board, board.isWhiteTurn());

        Move bestMove = orderedCaptureMove.get(0);
        int bestScore = -10000; // = alpha

        for (Move captureMove : orderedCaptureMove){
            zobristHash.updateHash(board, captureMove, false);


            zobristHash.zobristStack.push(zobristHash.getBoardHash());
            zobristHash.updateHash(board, captureMove, false);
            MoveOrganiser.makeMoveMaster(board, captureMove);
            MoveOrganiser.flipTurn(board);

            int score = -quiescenceSearch(board, zobristHash, -beta, -alpha);


            zobristHash.setBoardHash(zobristHash.zobristStack.pop());
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
