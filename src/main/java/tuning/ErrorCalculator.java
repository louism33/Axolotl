package tuning;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.TexelPosLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MAXIMUM;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MINIMUM;
import static com.github.louism33.axolotl.search.QuiescenceBetter.quiescenceSearch;
import static com.github.louism33.chesscore.BoardConstants.BLACK;

public final class ErrorCalculator implements Callable<Double> {

    private final List<TexelPosLoader.TexelPos> texelPosList;
    private Chessboard board;
    
    
    public ErrorCalculator() {
        texelPosList = new ArrayList<>();
    }
    
    public void add(TexelPosLoader.TexelPos pos) {
        texelPosList.add(pos);
    }
    
    
    @Override
    public Double call() {
        final int N = texelPosList.size();

        double totalError = 0;
        for (int i = 0; i < N; i++) {
            final TexelPosLoader.TexelPos x = texelPosList.get(i);
            final String fen = x.fen;
            final double R = x.score;
            final double q = getEvalScore(fen);

            final double sigmoid = sigmoid(q);
            final double thisError = Math.pow((R - sigmoid), 2);
            totalError += thisError;
        }
        return totalError;
    }

    
    private int getEvalScore(String fen) {
        board = new Chessboard(fen);

        int turnFac = 1;
        if (board.turn == BLACK) {
            turnFac = -1;
        }
        return turnFac * Evaluator.eval(board, board.generateLegalMoves());
    }

    private final static double K = -1.35;
    
    private static double sigmoid(double s) {
        double exp = (K * s) / 400d;
        return (1d / (1d + (Math.pow(10, exp))));
    }


    int getQScore(String fen, double result) {
        final Chessboard board = new Chessboard(fen);
        int turnFac = 1;
        if (board.turn == BLACK) {
            turnFac = -1;
        }
        return turnFac * quiescenceSearch(board, SHORT_MINIMUM, SHORT_MAXIMUM);
    }

    int getEvalScore(String fen, double result) {
        final Chessboard board = new Chessboard(fen);

        int turnFac = 1;
        if (board.turn == BLACK) {
            turnFac = -1;
        }
        return turnFac * Evaluator.eval(board, board.generateLegalMoves());
    }
}
