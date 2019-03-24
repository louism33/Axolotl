package com.github.louism33.axolotl.evaluation;

import com.fluxchess.jcpi.models.IllegalNotationException;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.jupiter.api.Test;

@SuppressWarnings("All")
public class EvaluatorTest {

    @Test
    void printEval() throws IllegalNotationException {
        Chessboard board = new Chessboard();

        printNStuff(board);

        System.out.println();
        System.out.println();
        System.out.println();
        
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
//        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
//        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b8c6"));
//        printNStuff(board);
    }



    static void printNStuff(Chessboard board){
        System.out.println(board);
        int[] moves = board.generateLegalMoves();
        Evaluator.printEval(board, board.turn, moves);
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
    }

   
}
