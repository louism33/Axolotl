package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.jupiter.api.Test;

@SuppressWarnings("All")
public class EvaluatorTest {

    @Test
    void a() {
        Chessboard board = new Chessboard();
        
        thing(board);
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));


        thing(board);
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
    }

    @Test void b(){
        Chessboard board = new Chessboard("8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -");
        thing(board);
    }

    static void thing(Chessboard board){
        System.out.println(board);
        int[] moves = board.generateLegalMoves();
//        int eval = Evaluator.eval(board, moves);
//        System.out.println("eval: " + eval);

        Evaluator.printEval(board, board.turn, moves);
    }

}
