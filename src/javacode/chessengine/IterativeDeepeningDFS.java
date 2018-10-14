package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;
import javacode.graphicsandui.Art;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class IterativeDeepeningDFS {

    static Move idDFS (Chessboard board, long timeLimit){
        int maxDepth = 2;
        int bestMoveScore = -10000;
        
        int alpha = -10000;
        int beta = 10000;

        List<Move> rootMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        Move bestMove = rootMoves.get(0);
        int bestScore = -10000;
        
        for (int depth = 0; depth < maxDepth; depth++){
            System.out.println("---- "+ depth);

            int score = negaMax(board, depth, alpha, beta);

//            System.out.println(score);
        }

        System.out.println();
        System.out.println("------");
        System.out.println(bestMove);
        System.out.println("------");
        
        return bestMove;
    }

    private static int negaMax(Chessboard board, int depth, int alpha, int beta){
        
        if (depth == 0){
            if (board.moveStack.size() > 0) {
                MoveUnmaker.unMakeMoveMaster(board);
            }
            return Evaluator.eval(board, board.isWhiteTurn());
        }

        
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        Move bestMove = moves.get(0);
        int bestScore = -10000;
        for (Move move : moves){
            
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            
            int score = -negaMax(board, depth-1, -beta, -alpha);
            
            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
        }

        if (board.moveStack.size() > 0) {
            MoveUnmaker.unMakeMoveMaster(board);
        }
        return bestScore;
    }
    
    
    
    
    
}
