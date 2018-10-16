package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.evalutation.Evaluator;

import java.util.List;

import static javacode.chessengine.PrincipleVariationSearch.principleVariationSearch;

class IterativeDeepeningDFS {

    private static TranspositionTable table = TranspositionTable.getInstance();

    static Move iterativeDeepeningWithAspirationWindows(Chessboard board, long timeLimit){
        int maxDepth = 7;

        int firstWindow = 10000;

        int alpha = -firstWindow, alphaFac = 2;
        int beta = firstWindow, betaFac = 2;

        List<Move> rootMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        Move bestMove = rootMoves.get(0);

        for (int depth = 0; depth < maxDepth; depth++){
            System.out.println("---- depth: " + depth + " ----");
            for (;;) {

                int score = principleVariationSearch(board, depth, depth, alpha, beta);

                if (score <= alpha) {
                    System.out.println("Missed Alpha");
                    alpha = -firstWindow * alphaFac;
                    alphaFac++;
                }
                else if (score >= beta) {
                    System.out.println("Missed Beta");
                    beta = firstWindow * betaFac;
                    betaFac++;
                }
                else {
                    break;
                }
            }
        }

        System.out.println();
        System.out.println("------");
        System.out.println(bestMove);
        System.out.println("number of evals: "+ PrincipleVariationSearch.numberOfFinalNegaMax);
        System.out.println("number of Qevals: "+ QuiescenceSearch.numberOfQuiescentEvals);
        System.out.println("attempt at node number: "+ PrincipleVariationSearch.attemptAtFinalNodeCount);
        System.out.println("total evals: " + 
                (PrincipleVariationSearch.numberOfFinalNegaMax + QuiescenceSearch.numberOfQuiescentEvals));
        System.out.println();
        System.out.println("total of calls to eval(): " + (Evaluator.numberOfEvals));
        System.out.println("------");

        return PrincipleVariationSearch.getAiMove();
    }

    





}
