package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class EvalKRK {
    
    @Test
    void KRK() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/8/KRk5");
        final int eval = Evaluator.eval(board, board.generateLegalMoves());
        Assert.assertTrue(eval > 3000);
    }

    @Test
    void KQK() {
        Chessboard board = new Chessboard("8/8/8/8/8/8/8/KQk5");
        final int eval = Evaluator.eval(board, board.generateLegalMoves());
        System.out.println(eval);
        Assert.assertTrue(eval > 3000);
    }
}
