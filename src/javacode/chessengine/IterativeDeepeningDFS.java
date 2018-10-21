package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.evalutation.Evaluator;

import java.util.List;

class IterativeDeepeningDFS {

    private static TranspositionTable table = TranspositionTable.getInstance();

    static Move iterativeDeepeningWithAspirationWindows(Chessboard board, ZobristHash zobristHash, long timeLimit){
        int maxDepth = 14;
        int aspirationScore = 0;
        
        List<Move> rootMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        /*
        Iterative Deepening Depth First Search:
        call search function at increasing depths, the data we get from lower depths is easily worth it
         */
        for (int depth = 0; depth < maxDepth; depth++){
            System.out.println("---- depth: " + depth + " ----");
            System.out.println("---- current best move: " + PrincipleVariationSearch.getAiMove() + " ----");
            System.out.println();
            
            int score = AspirationSearch.aspirationSearch(board, timeLimit, zobristHash, depth, aspirationScore);

            /*
            stop search when a checkmate has been found, however far away
             */
            if (score >= -Evaluator.IN_CHECKMATE_SCORE_MAY_PLY){
                break;
            }
            
            aspirationScore = score;
        }

        return PrincipleVariationSearch.getAiMove();
    }


}
