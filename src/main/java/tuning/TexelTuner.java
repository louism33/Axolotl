//package tuning;
//
//import com.github.louism33.axolotl.evaluation.EvaluationConstants;
//import com.github.louism33.utils.TexelPosLoader;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//import static com.github.louism33.axolotl.evaluation.EvaluationConstants.KING_SAFETY_ARRAY;
//import static com.github.louism33.axolotl.evaluation.EvaluationConstants.startMaterial;
//import static com.github.louism33.utils.PGNtoFEN.transformPGNFile;
//
//@SuppressWarnings("ALL")
//public class TexelTuner {
//
//    public static void main(String[] args) throws Exception {
//        final String dir = System.getProperty("user.dir")+ "/tuning";
////        startUpNTransform(dir);
//
//        final String fenPos = dir + "/quiet-labeled.epd";
//        new TexelTuner(fenPos);
//    }
//
//    private static List<TexelPosLoader.TexelPos> texelPosList;
//    final static int totalThreads = 2; 
//    private final static List<ErrorCalculator> calculators = new ArrayList<>();
//
//    private final static ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
//
//    static void startUpNTransform(String dir) throws Exception {
//        File file = new File(dir + "/pgnExampleFile.pgn");
//        final String dest = dir + "/result.fen";
//
//        System.out.println("file at " + file.toString());
//
//        if (!file.exists()) {
//            System.out.println("source file does not exist, creating and exiting");
//            file.createNewFile();
//            return;
//        }
//
//        transformPGNFile(file, dest);
//    }
//
//    TexelTuner(String fenPos) throws Exception {
//        texelPosList = TexelPosLoader.readFile(fenPos);
//
//        for (int i = 0; i < totalThreads; i++) {
//            calculators.add(new ErrorCalculator());
//        }
//
//        for (int i = 0; i < texelPosList.size(); i++) {
//            calculators.get(i % totalThreads).add(texelPosList.get(i));
//        }
//
//        localOptimise();
//    }
//
//    static void localOptimise() throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tuning/texelLog", true))) {
//
//            long startTime = System.currentTimeMillis();
//            
//            bw.write("\n\n\n NEW");
//            bw.flush();
//            System.out.println("===");
//            reset();
//            double bestPredictionError = getPredictionErrorMT();
//            System.out.println("prediction error takes " + ((System.currentTimeMillis() - startTime) / 1000) + " s to do.");
//            System.out.println("to be precise: " + (System.currentTimeMillis() - startTime));
//            final double initE = bestPredictionError;
//            System.out.println("initial prediction error " + bestPredictionError);
//
//
//            if (true) {
//                return;
//            }
//            
//            List<TexelParam> bestParams = getParams();
//            int n = bestParams.size();
//
//            TexelParam.delta = 5;
//
//            while (TexelParam.delta >= 1) {
//
//                boolean improved = true;
//                int iterations = 0;
//                while (improved && iterations < 100) {
//                    iterations++;
//                    improved = false;
//                    System.out.println("++++++++++++++++++++++++++++++++++++++");
//                    System.out.println("iteration " + iterations + " with delta " + TexelParam.delta);
//                    final long millis = System.currentTimeMillis() - startTime;
//                    System.out.println("time " + (millis / 1000) + "s");
//                    final String cuteTime = String.format("%d min, %d sec",
//                            TimeUnit.MILLISECONDS.toMinutes(millis),
//                            TimeUnit.MILLISECONDS.toSeconds(millis) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//                    );
//                    System.out.println(cuteTime);
//                    System.out.println();
//
//                    for (TexelParam t : bestParams) {
//                        System.out.println(t);
//                        if (t.values == KING_SAFETY_ARRAY) {
//                            t.printMe();
//                        }
//                        bw.write("\n"+t + "\n");
//                        bw.flush();
//                    }
//
//                    System.out.println();
//                    System.out.println("-----------------------------------------");
//
//                    for (int p = 0; p < n; p++) {
//                        final TexelParam texelParam = bestParams.get(p);
//                        for (int i = 0; i < texelParam.values.length; i++) {
//                            if (texelParam.dontChange.contains(i)) {
//                                continue;
//                            }
//
//                            texelParam.makeUpChange(i);
//                            reset();
//                            double newPredictionError = getPredictionErrorMT();
//
//                            if (newPredictionError < bestPredictionError) {
//                                bestPredictionError = newPredictionError;
//                                improved = true;
//                            } else {
//                                texelParam.makeDownChange(i);
//                                texelParam.makeDownChange(i);
//                                reset();
//                                newPredictionError = getPredictionErrorMT();
//
//                                if (newPredictionError < bestPredictionError) {
//                                    bestPredictionError = newPredictionError;
//                                    improved = true;
//                                } else {
//                                    texelParam.makeUpChange(i);
//                                }
//                            }
//                        }
//                    }
//
//                    System.out.println("iteration: " + iterations
//                            + ", initial pred error  " + initE 
//                            + ", bestPredictionError " + bestPredictionError
//                            + ", improvement of: " + (initE - bestPredictionError));
//                    final long millis2 = System.currentTimeMillis() - startTime;
//                    System.out.println("time taken " + (millis2 / 1000) + "s");
//                    final String cuteTime2 = String.format("%d min, %d sec",
//                            TimeUnit.MILLISECONDS.toMinutes(millis),
//                            TimeUnit.MILLISECONDS.toSeconds(millis) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//                    );
//                    System.out.println(cuteTime2);
//                }
//                long stopTime = System.currentTimeMillis();
//                System.out.println();
//                final String x = "AFTER " + iterations + " iterations with delta " + TexelParam.delta + " : ";
//                bw.write(x);
//                bw.flush();
//                System.out.println(x);
//                
//                System.out.println();
//                System.out.println("best params: ");
//                for (TexelParam t : bestParams) {
//                    final String name = t.name + "\n" 
//                            + "started at: " + Arrays.toString(t.startRecorder) + "\n" +
//                            "ended at:   " + Arrays.toString(t.values);
//                    System.out.println(name);
//                    bw.write("\n"+name + "\n");
//                    bw.flush();
//                }
//                final long millis = System.currentTimeMillis() - startTime;
//                System.out.println("total time " + (millis / 1000) + "s");
//                final String cuteTime = String.format("%d min, %d sec",
//                        TimeUnit.MILLISECONDS.toMinutes(millis),
//                        TimeUnit.MILLISECONDS.toSeconds(millis) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//                );
//                System.out.println(cuteTime);
//                reset();
//                double finalPredictionError = getPredictionErrorMT();
//                System.out.println();
//                System.out.println("initial prediction error " + initE);
//                System.out.println("final prediction error   " + finalPredictionError);
//                System.out.println("improvement of " + (initE - finalPredictionError));
//
//                TexelParam.delta -= 2;
//            }
//        }
//    }
//
//
//    static double getPredictionErrorMT() {
//        List<Future<Double>> list = new ArrayList<>();
//        for (int i = 0; i < totalThreads; i++) {
//            final Future<Double> submit = executorService.submit(calculators.get(i));
//            list.add(submit);
//        }
//
//        double totalError = 0;
//
//        for (Future<Double> f : list) {
//            try {
//                totalError += f.get();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        final int size = texelPosList.size();
//        System.out.println("size is " + size);
//        return totalError / ((double) size);
//    }
//
//    static void reset() {
////        EvaluatorPositionConstant.setup();
//        EvaluationConstants.setup();
//    }
//
//    static List<TexelParam> getParams() {
//        List<TexelParam> texelParams = new ArrayList<>();
//
//        texelParams.add(new TexelParam("early material scores", startMaterial, Arrays.asList(0)));
////        texelParams.add(new TexelParam("late material scores", endMaterial, Arrays.asList(0)));
////        texelParams.add(new TexelParam("pinned pieces scores", pinnedPiecesScores, Arrays.asList(0, 6)));
//
//
////        texelParams.add(new TexelParam("pawn features start", startPawnFeatures));
////        texelParams.add(new TexelParam("pawn features end", endPawnFeatures));
////        texelParams.add(new TexelParam("knight features start", startKnightFeatures));
////        texelParams.add(new TexelParam("knight features end", endKnightFeatures));
////        texelParams.add(new TexelParam("bishop features start", startBishopFeatures));
////        texelParams.add(new TexelParam("bishop features end", endBishopFeatures));
////        texelParams.add(new TexelParam("rook features start", startRookFeatures));
////        texelParams.add(new TexelParam("rook features end", endRookFeatures));
//
////        texelParams.add(new TexelParam("King safety", KING_SAFETY_ARRAY));
//
////        texelParams.add(new TexelParam("Knight mob", EvaluatorPositionConstant.mobilityScores[0]));
////        texelParams.add(new TexelParam("Bishop mob", EvaluatorPositionConstant.mobilityScores[1]));
////        texelParams.add(new TexelParam("Rook mob", EvaluatorPositionConstant.mobilityScores[2]));
////        texelParams.add(new TexelParam("Queen mob", EvaluatorPositionConstant.mobilityScores[3]));
//
//        // often unsafe
////        texelParams.add(new TexelParam("misc features", miscFeatures, Arrays.asList(0, 1)));
////        texelParams.add(new TexelParam("kingSafetyMisc features", kingSafetyMisc, Arrays.asList(0, 1))); // careful
//        return texelParams;
//    }
//    
//    
//}
