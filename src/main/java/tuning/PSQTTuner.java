//package tuning;
//
//import com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant;
//import com.github.louism33.utils.TexelPosLoader;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.*;
//import static com.github.louism33.utils.PGNtoFEN.transformPGNFile;
//
//@SuppressWarnings("ALL")
//public class PSQTTuner {
//
//    public static void main(String[] args) throws Exception {
//        final String dir = System.getProperty("user.dir") + "/tuning";
////        startUpNTransform(dir);
//
//        final String fenPos = dir + "/quiet-labeled.epd";
//        new PSQTTuner(fenPos);
//
////        minimiseK();
////        test();
//    }
//
//    static List<TexelPosLoader.TexelPos> texelPosList;
//    final static int totalThreads = 2;
//    static List<ErrorCalculator> calculators = new ArrayList<>();
//
//    static ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
//
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
//
//    PSQTTuner(String fenPos) throws Exception {
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
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tuning/psqtTexelLog", true))) {
//            long startTime = System.currentTimeMillis();
//            System.out.println("===");
//
//            reset();
//
//            double bestPredictionError = getPredictionErrorMT();
//            System.out.println("prediction error takes " + ((System.currentTimeMillis() - startTime) / 1000) + " s to do.");
//            final double initE = bestPredictionError;
//            System.out.println("initial prediction error " + bestPredictionError);
//
//            List<PSQTParam> params = new ArrayList<>();
//            
//            params.add(new PSQTParam("psqt pawn start ", PAWN_START_WHITE));
//            params.add(new PSQTParam("psqt pawn end ", PAWN_END_WHITE));
//            params.add(new PSQTParam("psqt knight start ", KNIGHT_START_WHITE));
//            params.add(new PSQTParam("psqt knight end ", KNIGHT_END_WHITE));
//            params.add(new PSQTParam("psqt bish start ", BISHOP_START_WHITE));
//            params.add(new PSQTParam("psqt bish end ", BISHOP_END_WHITE));
//            params.add(new PSQTParam("psqt rook start ", ROOK_START_WHITE));
//            params.add(new PSQTParam("psqt rook end ", ROOK_END_WHITE));
//            params.add(new PSQTParam("psqt queen start ", QUEEN_START_WHITE));
//            params.add(new PSQTParam("psqt queen end ", QUEEN_END_WHITE));
//            params.add(new PSQTParam("psqt king start ", KING_START_WHITE));
//            params.add(new PSQTParam("psqt king end ", KING_END_WHITE));
//
//
//            PSQTParam.delta = 3;
//
//            while (PSQTParam.delta >= 1) {
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
//                    System.out.println(niceTime(millis));
//                    System.out.println();
//                    System.out.println("--------------------------------------------");
//
//                    for (PSQTParam param : params) {
//                        List<Integer> changedIndexes = new ArrayList<>();
//                        System.out.println(param);
//                        param.printMe();
//                        bw.write("\n\nNEW\n");
//                        bw.flush();
//                        bw.write(param.str());
//                        bw.flush();
//
//                        System.out.println();
//
//                        for (int i = 0; i < 64; i++) {
//                            changedIndexes.add(0);
//                            if (param.values == PAWN_START_WHITE
//                                    || param.values == PAWN_END_WHITE) {
//                                if (i <= 7) {
//                                    continue;
//                                }
//                                if (i >= 56) {
//                                    continue;
//                                }
//                            }
//                            if (i % 8 > 3) {
//                                continue;
//                            }
//                            param.makeUpChange(i);
//                            reset();
//                            double newPredictionError = getPredictionErrorMT();
//
//                            if (newPredictionError < bestPredictionError) {
//                                bestPredictionError = newPredictionError;
//                                improved = true;
//                                changedIndexes.set(i, changedIndexes.get(i) + 1);
//                            } else {
//                                param.makeDownChange(i);
//                                param.makeDownChange(i);
//                                reset();
//                                newPredictionError = getPredictionErrorMT();
//
//                                if (newPredictionError < bestPredictionError) {
//                                    bestPredictionError = newPredictionError;
//                                    improved = true;
//                                    changedIndexes.set(i, changedIndexes.get(i) + 1);
//                                } else {
//                                    param.makeUpChange(i);
//                                }
//                            }
//                        }
//
//                        System.out.println("one value done: ");
//                        param.printMe();
//                        System.out.println("--- with indexes of changes " + changedIndexes);
//                        System.out.println("initial prediction error   " + initE
//                                         + "\ncurrent prediction error:  " + bestPredictionError);
//
//                    }
//
//
//                    System.out.println("iteration: " + iterations
//                            +", initial prediction error " + initE
//                            + "\nbestPredictionError " + bestPredictionError
//                            + ", improvement of: " + (initE - bestPredictionError));
//
//                    final long millis2 = System.currentTimeMillis() - startTime;
//                    System.out.println("time taken " + (millis2 / 1000) + "s");
//                    System.out.println(niceTime(millis2));
//                    
//                }
//                long stopTime = System.currentTimeMillis();
//                System.out.println();
//                System.out.println("AFTER " + iterations + " iterations: ");
//                System.out.println();
//
//            }
//
//            final long millis2 = System.currentTimeMillis() - startTime;
//            System.out.println("total time " + (millis2 / 1000) + "s");
//            final String cuteTime2 = String.format("%d min, %d sec",
//                    TimeUnit.MILLISECONDS.toMinutes(millis2),
//                    TimeUnit.MILLISECONDS.toSeconds(millis2) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis2))
//            );
//            System.out.println(cuteTime2);
//
//            reset();
//            double finalPredictionError = getPredictionErrorMT();
//            System.out.println();
//            System.out.println("initial prediction error " + initE);
//            System.out.println("final prediction error " + finalPredictionError);
//            System.out.println("improvement of " + (initE - finalPredictionError));
//
//            PSQTParam.delta -= 2;
//        }
//    }
//
//
//    static String niceTime(long millis) {
//        return String.format("%d min, %d sec",
//                TimeUnit.MILLISECONDS.toMinutes(millis),
//                TimeUnit.MILLISECONDS.toSeconds(millis) -
//                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//        );
//    }
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
//        return totalError / ((double) texelPosList.size());
//    }
//
//    static void reset() {
//        EvaluatorPositionConstant.setup();
////        EvaluationConstants.setup();
//    }
//
//}
