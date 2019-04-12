package tuning;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.evaluation.PawnTranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.TexelPosLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MAXIMUM;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MINIMUM;
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

    static List<TexelPosLoader.TexelPos> texelPosList;
    final static int totalThreads = 3;
    static List<ErrorCalculator> calculators = new ArrayList<>();
    
    static ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);


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
        texelPosList = TexelPosLoader.readFile(fenPos);

        for (int i = 0; i < totalThreads; i++) {
            calculators.add(new ErrorCalculator());
        }

        for (int i = 0; i < texelPosList.size(); i++) {
            calculators.get(i % totalThreads).add(texelPosList.get(i));
        }
        
        localOptimise();
    }

    static void localOptimise() throws IOException {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/tuning/results.txt"))) {
            long startTime = System.currentTimeMillis();
            System.out.println("===");

            reset();

            double bestPredictionError = getPredictionErrorMT();
            System.out.println("prediction error takes " + ((System.currentTimeMillis() - startTime) / 1000) + " s to do.");
            double initE = bestPredictionError;
            System.out.println("initial prediction error " + bestPredictionError);

            PSQTParam param = new PSQTParam("psqt king end ", 0);
            //KING-KING for end
            for (int i = 0; i < param.values[WHITE].length; i++) {
                if (!param.onlyDo.contains(i)) {
                    continue;
                }
                param.printOne(i);
            }

            PSQTParam.delta = 5;

            while (PSQTParam.delta >= 1) {

                boolean improved = true;
                int iterations = 0;
                while (improved && iterations < 100) {
                    iterations++;
                    improved = false;
                    System.out.println("++++++++++++++++++++++++++++++++++++++");
                    System.out.println("iteration " + iterations + " with delta " + TexelParam.delta);
                    final long millis = System.currentTimeMillis() - startTime;
                    System.out.println("time " + (millis / 1000) + "s");
                    final String cuteTime = String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    );
                    System.out.println(cuteTime);

                    System.out.println();

                    System.out.println(param);

                    System.out.println();
                    System.out.println("--------------------------------------------");

                    final int[][] values = param.values[WHITE];

                    List<Integer> changedIndexes = new ArrayList<>();
                    
                    for (int p = 0; p < values.length; p++) {
                        changedIndexes.add(0);
                        if (!param.onlyDo.contains(p)) {
                            continue;
                        }

                        param.printOne(p);
                        br.write(param.strOne(p));
                        
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
                            double newPredictionError = getPredictionErrorMT();

                            if (newPredictionError < bestPredictionError) {
                                bestPredictionError = newPredictionError;
                                improved = true;
                                changedIndexes.set(i, changedIndexes.get(i) + 1);
                            } else {
                                param.makeDownChange(p, i);
                                param.makeDownChange(p, i);
                                reset();
                                newPredictionError = getPredictionErrorMT();

                                if (newPredictionError < bestPredictionError) {
                                    bestPredictionError = newPredictionError;
                                    improved = true;
                                    changedIndexes.set(i, changedIndexes.get(i) + 1);
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

                    System.out.println("--- with indexes of changes " + changedIndexes);
                    
                    final long millis2 = System.currentTimeMillis() - startTime;
                    System.out.println("time taken " + (millis2 / 1000) + "s");
                    final String cuteTime2 = String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    );
                    System.out.println(cuteTime2);
                }
                long stopTime = System.currentTimeMillis();
                System.out.println();
                System.out.println("AFTER " + iterations + " iterations: ");
                System.out.println();

                final long millis = System.currentTimeMillis() - startTime;
                System.out.println("total time " + (millis / 1000) + "s");
                final String cuteTime = String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );
                System.out.println(cuteTime);

                reset();
                double finalPredictionError = getPredictionErrorMT();
                System.out.println();
                System.out.println("initial prediction error " + initE);
                System.out.println("final prediction error " + finalPredictionError);
                System.out.println("improvement of " + (initE - finalPredictionError));

                PSQTParam.delta -= 2;
            }
        }
    }



    static double getPredictionErrorMT() {
        List<Future<Double>> list = new ArrayList<>();
        for (int i = 0; i < totalThreads; i++) {
            final Future<Double> submit = executorService.submit(calculators.get(i));
            list.add(submit);
        }

        double totalError = 0;
        
        for (Future<Double> f : list) {
            try {
                totalError += f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return totalError / ((double) texelPosList.size());
    }

    static final double K = -1.35;

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
        PawnTranspositionTable.reset();
//        EngineBetter.resetFull();
    }

}
