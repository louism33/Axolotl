package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;

final class EndgameKPK {

    static int evaluateKPK(Chessboard board) {
        // if a draw
        if (!KPK.probe(board)) {
            return 0;
        }

        int winningPlayer = -1;
        int score = 0;

        for (int turn = WHITE; turn <= BLACK; turn++) {
            final long pawn = board.pieces[turn][PAWN];
            if (pawn != 0) {
                winningPlayer = turn;

                score += 6_000;

                int materialScore = 0;
                materialScore += populationCount(board.pieces[turn][PAWN]) * material[P];
                materialScore += populationCount(board.pieces[turn][KNIGHT]) * material[K];
                materialScore += populationCount(board.pieces[turn][BISHOP]) * material[B];
                materialScore += populationCount(board.pieces[turn][ROOK]) * material[R];
                materialScore += populationCount(board.pieces[turn][QUEEN]) * material[Q];
                score += Score.getScore(materialScore, 0);

                if (turn == WHITE) {
                    score += Long.numberOfTrailingZeros(pawn);
                }

            }
        }

        Assert.assertTrue(winningPlayer != -1);
        Assert.assertTrue(Math.abs(score) < CHECKMATE_ENEMY_SCORE_MAX_PLY);
        return board.turn == winningPlayer ? score : -score;
    }


}
