package tuning;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant;
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
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.*;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.KING_END_WHITE;
import static com.github.louism33.utils.PGNtoFEN.transformPGNFile;

@SuppressWarnings("ALL")
public class TexelMasterTuner {

    public static void main(String[] args) throws Exception {
        final String dir = System.getProperty("user.dir")+ "/tuning";
//        startUpNTransform(dir);

        final String fenPos = dir + "/quiet-labeled.epd";
        new TexelMasterTuner(fenPos);
    }

    private static List<TexelPosLoader.TexelPos> texelPosList;
    final static int totalThreads = 2; 
    private final static List<ErrorCalculator> calculators = new ArrayList<>();

    private final static ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

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

    TexelMasterTuner(String fenPos) throws Exception {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tuning/mastertexelLog", true))) {

            long startTime = System.currentTimeMillis();
            
            bw.write("\n\n\n NEW");
            bw.flush();
            System.out.println("===");
            reset();
            double bestPredictionError = getPredictionErrorMT();
            System.out.println("prediction error takes " + ((System.currentTimeMillis() - startTime) / 1000) + " s to do.");
            System.out.println("to be precise: " + (System.currentTimeMillis() - startTime));
            final double initE = bestPredictionError;
            System.out.println("initial prediction error " + bestPredictionError);

            List<TexelMasterParam> bestParams = getParams();
            int n = bestParams.size();
            TexelMasterParam.delta = 1;
            System.out.println("Training " + bestParams.size() + " params with initial delta " + TexelMasterParam.delta);

            while (TexelMasterParam.delta >= 1) {

                boolean improved = true;
                int iterations = 0;
                while (improved && iterations < 100) {
                    iterations++;
                    improved = false;
                    System.out.println("++++++++++++++++++++++++++++++++++++++");
                    System.out.println("iteration " + iterations + " with delta " + TexelMasterParam.delta);
                    final long millis = System.currentTimeMillis() - startTime;
                    System.out.println("time " + (millis / 1000) + "s");
                    System.out.println(niceTime( System.currentTimeMillis() - startTime));
                    System.out.println();

                    for (TexelMasterParam t : bestParams) {
                        System.out.println(t);
                        bw.write("\n"+t + "\n");
                        bw.flush();
                    }

                    System.out.println();
                    System.out.println("-----------------------------------------");

                    for (int p = 0; p < n; p++) {
                        List<Integer> changedIndexes = new ArrayList<>();
                        final TexelMasterParam param = bestParams.get(p);
                        
                        String s = "Currently tuning: " +  param;
                        if (param.pst) {
                            param.printMe();
                        }
                        
                        for (int i = 0; i < param.values.length; i++) {
                            changedIndexes.add(0);
                            if (param.dontChange.contains(i)) {
                                continue;
                            }

                            param.makeUpChange(i);
                            reset();
                            double newPredictionError = getPredictionErrorMT();

                            if (newPredictionError < bestPredictionError) {
                                bestPredictionError = newPredictionError;
                                improved = true;
                                changedIndexes.set(i, changedIndexes.get(i) + 1);
                            } else {
                                param.makeDownChange(i);
                                param.makeDownChange(i);
                                reset();
                                newPredictionError = getPredictionErrorMT();

                                if (newPredictionError < bestPredictionError) {
                                    bestPredictionError = newPredictionError;
                                    improved = true;
                                    changedIndexes.set(i, changedIndexes.get(i) + 1);
                                } else {
                                    param.makeUpChange(i);
                                }
                            }
                        }
                        System.out.println("one value done: ");
                        param.printMe();
                        System.out.println("--- with indexes of changes " + changedIndexes);
                    }

                    System.out.println("looped though all params. Improved is " + improved);
                    System.out.println(niceError(iterations, initE, bestPredictionError));
                    System.out.println(niceTime(System.currentTimeMillis() - startTime));
                }
                
                
                long stopTime = System.currentTimeMillis();
                System.out.println();
                final String x = "AFTER " + iterations + " iterations with delta " + TexelMasterParam.delta + " : ";
                bw.write(x);
                bw.flush();
                System.out.println(x);
                
                
                System.out.println();
                System.out.println("best params: ");
                for (TexelMasterParam t : bestParams) {
                    final String name = t.name + "\n" 
                            + "started at: " + Arrays.toString(t.startRecorder) + "\n" +
                            "ended at:   " + Arrays.toString(t.values);
                    System.out.println(name);
                    bw.write("\n"+name + "\n");
                    bw.flush();
                }

                System.out.println(niceTime(System.currentTimeMillis() - startTime));
                reset();
                System.out.println(niceError(iterations, initE, getPredictionErrorMT()));
                TexelMasterParam.delta -= 2;
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

        final int size = texelPosList.size();
        return totalError / ((double) size);
    }

    static void reset() {
        EvaluatorPositionConstant.setup();
        EvaluationConstants.setup();
    }

    static List<TexelMasterParam> getParams() {
        List<TexelMasterParam> params = new ArrayList<>();

        params.add(new TexelMasterParam("early material scores", startMaterial, Arrays.asList(0)));
        params.add(new TexelMasterParam("late material scores", endMaterial, Arrays.asList(0)));
        params.add(new TexelMasterParam("pinned pieces scores", pinnedPiecesScores, Arrays.asList(0, 6)));


        params.add(new TexelMasterParam("pawn features start", startPawnFeatures));
        params.add(new TexelMasterParam("pawn features end", endPawnFeatures));
        params.add(new TexelMasterParam("knight features start", startKnightFeatures));
        params.add(new TexelMasterParam("knight features end", endKnightFeatures));
        params.add(new TexelMasterParam("bishop features start", startBishopFeatures));
        params.add(new TexelMasterParam("bishop features end", endBishopFeatures));
        params.add(new TexelMasterParam("rook features start", startRookFeatures));
        params.add(new TexelMasterParam("rook features end", endRookFeatures));

        params.add(new TexelMasterParam("King safety", KING_SAFETY_ARRAY));
        
        params.add(new TexelMasterParam("Knight mob", EvaluatorPositionConstant.mobilityScores[0]));
        params.add(new TexelMasterParam("Bishop mob", EvaluatorPositionConstant.mobilityScores[1]));
        params.add(new TexelMasterParam("Rook mob", EvaluatorPositionConstant.mobilityScores[2]));
        params.add(new TexelMasterParam("Queen mob", EvaluatorPositionConstant.mobilityScores[3]));

        
        //pst
        params.add(new TexelMasterParam("psqt pawn start ", PAWN_START_WHITE, 
                Arrays.asList(0,1,2,3,4,5,6,7, 56, 57, 58, 59, 60, 61, 62, 63), true));
        params.add(new TexelMasterParam("psqt pawn end ", PAWN_END_WHITE,
                Arrays.asList(0,1,2,3,4,5,6,7, 56, 57, 58, 59, 60, 61, 62, 63), true));
        params.add(new TexelMasterParam("psqt knight start ", KNIGHT_START_WHITE, true));
        params.add(new TexelMasterParam("psqt knight end ", KNIGHT_END_WHITE, true));
        params.add(new TexelMasterParam("psqt bish start ", BISHOP_START_WHITE, true));
        params.add(new TexelMasterParam("psqt bish end ", BISHOP_END_WHITE, true));
        params.add(new TexelMasterParam("psqt rook start ", ROOK_START_WHITE, true));
        params.add(new TexelMasterParam("psqt rook end ", ROOK_END_WHITE, true));
        params.add(new TexelMasterParam("psqt queen start ", QUEEN_START_WHITE, true));
        params.add(new TexelMasterParam("psqt queen end ", QUEEN_END_WHITE, true));
        params.add(new TexelMasterParam("psqt king start ", KING_START_WHITE, true));
        params.add(new TexelMasterParam("psqt king end ", KING_END_WHITE, true));





        // often unsafe
//        params.add(new TexelMasterParam("misc features", miscFeatures, Arrays.asList(0, 1)));
//        params.add(new TexelMasterParam("kingSafetyMisc features", kingSafetyMisc, Arrays.asList(0, 1))); // careful
        return params;
    }

    static String niceTime(long millis) {
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
    
    static String niceError(int iterations, double initE, double bestPredictionError) {
        return "iteration: " + iterations
                + ", initial pred error  " + initE
                + ", bestPredictionError " + bestPredictionError
                + ", improvement of: " + (initE - bestPredictionError);
    }
}
