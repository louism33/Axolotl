package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.evalutation.Evaluator;

import java.util.List;

class IterativeDeepeningDFS {

    private static TranspositionTable table = TranspositionTable.getInstance();

    static Move iterativeDeepeningWithAspirationWindows(Chessboard board, long timeLimit){
        int maxDepth = 10;

        int aspirationScore = 0;
        
        List<Move> rootMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());


        for (int depth = 0; depth < maxDepth; depth++){
//            System.out.println("---- depth: " + depth + " ----");

            int score = AspirationSearch.aspirationSearch(board, timeLimit, depth, aspirationScore);

            if (score == Evaluator.IN_CHECKMATE_SCORE){
                System.out.println(PrincipleVariationSearch.getAiMove());
                System.out.println();
            }

            if (score == -Evaluator.IN_CHECKMATE_SCORE){
                System.out.println("    " + PrincipleVariationSearch.getAiMove());
                System.out.println();
                break;
            }
            
            
            aspirationScore = score;
            
        }

        boolean debug = false;
        if (debug) {
            System.out.println();
            System.out.println("------");
            System.out.println(PrincipleVariationSearch.getAiMove());
            System.out.println("number of evals: " + PrincipleVariationSearch.numberOfFinalNegaMax);
            System.out.println("number of Qevals: " + QuiescenceSearch.numberOfQuiescentEvals);
            System.out.println("attempt at node number: " + PrincipleVariationSearch.attemptAtFinalNodeCount);
            System.out.println("total evals: " +
                    (PrincipleVariationSearch.numberOfFinalNegaMax + QuiescenceSearch.numberOfQuiescentEvals));
            System.out.println();
            System.out.println("total of calls to eval(): " + (Evaluator.numberOfEvals));
            System.out.println("------");

//        System.out.println(table);

        }

        return PrincipleVariationSearch.getAiMove();
    }







}
