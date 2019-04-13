package tuning;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.TexelPosLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.QuiescenceBetter.quiescenceSearch;
import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.utils.PGNtoFEN.transformPGNFile;

@SuppressWarnings("ALL")
public class TexelTuner {

    public static void main(String[] args) throws Exception {
        final String dir = System.getProperty("user.dir")+ "/tuning";
//        startUpNTransform(dir);

        final String fenPos = dir + "/quiet-labeled.epd";
        new TexelTuner(fenPos);

//        minimiseK();
//        test();
    }
    
    static List<TexelPosLoader.TexelPos> texelPosList;
    final static int totalThreads = 1; // static variables make race conditions in multi :(
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

    TexelTuner(String fenPos) throws Exception {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tuning/texelLog", true))) {

            long startTime = System.currentTimeMillis();
            
            bw.write("\n\n\n NEW");
            bw.flush();
            System.out.println("===");
            reset();
            double bestPredictionError = getPredictionErrorMT();
            System.out.println("prediction error takes " + ((System.currentTimeMillis() - startTime) / 1000) + " s to do.");
            final double initE = bestPredictionError;
            System.out.println("initial prediction error " + bestPredictionError);

            List<TexelParam> bestParams = getParams();
            int n = bestParams.size();

            TexelParam.delta = 13;

            while (TexelParam.delta >= 1) {

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

                    for (TexelParam t : bestParams) {
                        System.out.println(t);
                        bw.write("\n"+t + "\n");
                        bw.flush();
                    }

                    System.out.println();
                    System.out.println("-----------------------------------------");

                    for (int p = 0; p < n; p++) {
                        final TexelParam texelParam = bestParams.get(p);
                        for (int i = 0; i < texelParam.values.length; i++) {
                            if (texelParam.dontChange.contains(i)) {
                                continue;
                            }

                            texelParam.makeUpChange(i);
                            reset();
                            double newPredictionError = getPredictionErrorMT();

                            if (newPredictionError < bestPredictionError) {
                                bestPredictionError = newPredictionError;
                                improved = true;
                            } else {
                                texelParam.makeDownChange(i);
                                texelParam.makeDownChange(i);
                                reset();
                                newPredictionError = getPredictionErrorMT();

                                if (newPredictionError < bestPredictionError) {
                                    bestPredictionError = newPredictionError;
                                    improved = true;
                                } else {
                                    texelParam.makeUpChange(i);
                                }
                            }
                        }
                    }

                    System.out.println("iteration: " + iterations
                            + ", initial pred error  " + initE 
                            + ", bestPredictionError " + bestPredictionError
                            + ", improvement of: " + (initE - bestPredictionError));
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
                final String x = "AFTER " + iterations + " iterations with delta " + TexelParam.delta + " : ";
                bw.write(x);
                bw.flush();
                System.out.println(x);
                
                System.out.println();
                System.out.println("best params: ");
                for (TexelParam t : bestParams) {
                    final String name = t.name + "\n" 
                            + "started at: " + Arrays.toString(t.startRecorder) + "\n" +
                            "ended at:   " + Arrays.toString(t.values);
                    System.out.println(name);
                    bw.write("\n"+name + "\n");
                    bw.flush();
                }
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
                System.out.println("final prediction error   " + finalPredictionError);
                System.out.println("improvement of " + (initE - finalPredictionError));

                TexelParam.delta -= 2;
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

    static void minimiseK() throws Exception {

        final List<TexelPosLoader.TexelPos> texelPosList = TexelPosLoader.readFile("tuning/quiet-labeled.epd");
        
        final double N = texelPosList.size();

        double bestK = 0;
        double bestError = 10000000;

        double ori = -1.3;
        double oriE = 0;
        double step = 0.01;
        K = ori;

        System.out.println("total pos: " + N);
        
        while (K >= -1.41) {
            System.out.println("testing with K=" + K);
            double totalError = 0;
            for (int i = 0; i < N; i++) {
                final TexelPosLoader.TexelPos x = texelPosList.get(i);
                final String fen = x.fen;
                final double R = x.score;
                final double q = getEvalScore(fen, R);

                final double sigmoid = sigmoid(q);
                final double thisError = Math.pow((R - sigmoid), 2);
                totalError += thisError;
            }
            double thisError = totalError / N;
            System.out.println("error: " + thisError);
            
            if (K == ori) {
                oriE = thisError;
            }
            
            if (thisError < bestError) {
                bestError = thisError;
                bestK = K;
                System.out.println("new best K " + K);
            }
            K = K - step;
        }

        System.out.println("original K " + (ori));
        System.out.println("original E " + (oriE));
        
        System.out.println("best K found: " + bestK);
        System.out.println("with Error " + bestError);
        
        
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
        EvaluatorPositionConstant.setup();
        EvaluationConstants.setup();
//        PawnTranspositionTable.reset();
//        EngineBetter.resetFull();
    }

    static List<TexelParam> getParams() {
        List<TexelParam> texelParams = new ArrayList<>();

//        texelParams.add(new TexelParam("early material scores", startMaterial, Arrays.asList(0)));
//        texelParams.add(new TexelParam("late material scores", endMaterial, Arrays.asList(0)));
        
//        texelParams.add(new TexelParam("pinned pieces scores", pinnedPiecesScores, Arrays.asList(0, 6))); // would this apply in Q pos?
        
//        texelParams.add(new TexelParam("misc features", miscFeatures, Arrays.asList(0)));
        
//        texelParams.add(new TexelParam("pawn features start", startPawnFeatures));
//        texelParams.add(new TexelParam("pawn features end", endPawnFeatures));
        
//        texelParams.add(new TexelParam("knight features start", startKnightFeatures));
//        texelParams.add(new TexelParam("knight features end", endKnightFeatures));
        
        texelParams.add(new TexelParam("bishop features start", startBishopFeatures));
        texelParams.add(new TexelParam("bishop features end", endBishopFeatures));
        
        texelParams.add(new TexelParam("rook features start", startRookFeatures));
        texelParams.add(new TexelParam("rook features end", endRookFeatures));
        
//        texelParams.add(new TexelParam("kingSafetyMisc features", kingSafetyMisc));
        
//        texelParams.add(new TexelParam("King safety", EvaluationConstants.KING_SAFETY_ARRAY));
        
//        texelParams.add(new TexelParam("Knight mob", EvaluatorPositionConstant.mobilityScores[0]));
//        texelParams.add(new TexelParam("Bishop mob", EvaluatorPositionConstant.mobilityScores[1]));
//        texelParams.add(new TexelParam("Rook mob", EvaluatorPositionConstant.mobilityScores[2]));
//        texelParams.add(new TexelParam("Queen mob", EvaluatorPositionConstant.mobilityScores[3]));

        return texelParams;
    }
    
    
}
