package tuning;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.TexelPosLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.QuiescenceBetter.quiescenceSearch;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.utils.PGNtoFEN.transformPGNFile;

@SuppressWarnings("ALL")
public class PSQTTuner {

    public static void main(String[] args) throws Exception {
        final String dir = System.getProperty("user.dir") + "/tuning";
//        startUpNTransform(dir);

        final String fenPos = dir + "/quiet-labeled.epd";
        new PSQTTuner(fenPos);

//        minimiseK();
//        test();
    }


    static void startUpNTransform(String dir) throws Exception {
        File file = new File(dir + "/pgnExampleFile.pgn");
        final String dest = dir + "/result.fen";

        System.out.println("file at " + file.toString());

        if (!file.exists()) {
            System.out.println("source file does not exist, creating and exiting");
            file.createNewFile();
            return;
        }

        transformPGNFile(file, dest);
    }


    PSQTTuner(String fenPos) throws Exception {
        final List<TexelPosLoader.TexelPos> texelPosList = TexelPosLoader.readFile(fenPos);
        localOptimise(texelPosList);
    }

    static void localOptimise(List<TexelPosLoader.TexelPos> texelPosList) {
        long startTime = System.currentTimeMillis();
        System.out.println("===");


        reset();
        double bestPredictionError = getPredictionError(texelPosList);
        System.out.println("prediction error takes " + ((System.currentTimeMillis() - startTime) / 1000) + " s to do.");
        double initE = bestPredictionError;
        System.out.println("initial prediction error " + bestPredictionError);

        PSQTParam param = new PSQTParam("psqt bish ", BISHOP);
        for (int i = 0; i < param.values[WHITE].length; i++) {
            if (!param.onlyDo.contains(i)) {
                continue;
            }
            param.printOne(i);
        }
        boolean improved = true;
        int iterations = 0;
        while (improved && iterations < 100) {
            iterations++;
            improved = false;
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println("iteration " + iterations);
            System.out.println("time " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
            
            System.out.println();

            System.out.println(param);

            System.out.println();
            System.out.println("++++++++++++++++++++++++++++++++++++++");

            final int[][] values = param.values[WHITE];

            for (int p = 0; p < values.length; p++) {
                if (!param.onlyDo.contains(p)) {
                    continue;
                }

                param.printOne(p);
                System.out.println();

                for (int i = 0; i < 64; i++) {
                    if (p == PAWN) {
                        if (i <= 7) {
                            continue;
                        }
                        if (i >= 56) {
                            continue;
                        }
                    }
                    if (i % 8 > 3) {
                        continue;
                    }
                    param.makeUpChange(p, i);
                    reset();
                    double newPredictionError = getPredictionError(texelPosList);

                    if (newPredictionError < bestPredictionError) {
                        bestPredictionError = newPredictionError;
                        improved = true;
                    } else {
                        param.makeDownChange(p, i);
                        param.makeDownChange(p, i);
                        reset();
                        newPredictionError = getPredictionError(texelPosList);

                        if (newPredictionError < bestPredictionError) {
                            bestPredictionError = newPredictionError;
                            improved = true;
                        } else {
                            param.makeUpChange(p, i);
                        }
                    }
                }

                System.out.println("one value done: ");
                param.printOne(p);
                System.out.println();
            }

            System.out.println("iteration: " + iterations
                    + ", bestPredictionError " + bestPredictionError
                    + ", improvement of: " + (initE - bestPredictionError));
            System.out.println("time taken " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
        }
        long stopTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("AFTER " + iterations + " iterations: ");
        System.out.println();

        System.out.println("total time: " + ((stopTime - startTime) / 1000));
        reset();
        double finalPredictionError = getPredictionError(texelPosList);
        System.out.println();
        System.out.println("initial prediction error " + initE);
        System.out.println("final prediction error " + finalPredictionError);
        System.out.println("improvement of " + (initE - finalPredictionError));
    }


    static double getPredictionError(List<TexelPosLoader.TexelPos> texelPosList) {
        final double N = texelPosList.size();

        double totalError = 0;
        for (int i = 0; i < N; i++) {
            final TexelPosLoader.TexelPos x = texelPosList.get(i);
            final String fen = x.fen;
            final double R = x.score;
//            final double q = getQScore(fen, R);
            final double q = getEvalScore(fen, R);

            final double sigmoid = sigmoid(q);
            final double thisError = Math.pow((R - sigmoid), 2);
            totalError += thisError;
        }
        return totalError / N;
    }


    static double K = -1.35;

    static double sigmoid(double s) {
        double exp = (K * s) / 400d;
        return (1d / (1d + (Math.pow(10, exp))));
    }

    static int getQScore(String fen, double result) {
        final Chessboard board = new Chessboard(fen);
        int turnFac = 1;
        if (board.turn == BLACK) {
            turnFac = -1;
        }
        return turnFac * quiescenceSearch(board, SHORT_MINIMUM, SHORT_MAXIMUM);
    }

    static int getEvalScore(String fen, double result) {
        final Chessboard board = new Chessboard(fen);

        int turnFac = 1;
        if (board.turn == BLACK) {
            turnFac = -1;
        }
        return turnFac * Evaluator.eval(board, board.generateLegalMoves());
    }

    static void reset() {
//        PawnTranspositionTable.reset();
//        EngineBetter.resetFull();
    }

}
