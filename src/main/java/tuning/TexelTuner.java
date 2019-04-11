package tuning;

import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant;
import com.github.louism33.axolotl.evaluation.PawnTranspositionTable;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.TexelPosLoader;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.QuiescenceBetter.*;
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

        List<TexelParam> bestParams = getParams();
        int n = bestParams.size();
        boolean improved = true;
        int iterations = 0;
        while (improved && iterations < 100) {
            iterations++;
            improved = false;
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println("iteration " + iterations);
            System.out.println("time " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
            System.out.println();

            for (TexelParam t : bestParams) {
                System.out.println(t);
            }
            
            System.out.println();
            System.out.println("++++++++++++++++++++++++++++++++++++++");

            for (int p = 0; p < n; p++) {
                final TexelParam texelParam = bestParams.get(p);
                for (int i = 0; i < texelParam.values.length; i++) {
                    if (texelParam.dontChange.contains(i)) {
                        continue;
                    }

                    texelParam.makeUpChange(i);
                    reset();
                    double newPredictionError = getPredictionError(texelPosList);

                    if (newPredictionError < bestPredictionError) {
                        bestPredictionError = newPredictionError;
                        improved = true;
                    } else {
                        texelParam.makeDownChange(i); 
                        texelParam.makeDownChange(i);
                        reset();
                        newPredictionError = getPredictionError(texelPosList);

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
                    + ", bestPredictionError " + bestPredictionError 
                    + ", improvement of: " + (initE - bestPredictionError));
            System.out.println("time taken " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
        }
        long stopTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("AFTER " + iterations + " iterations: ");
        System.out.println();
        System.out.println("best params: ");
        for (TexelParam t : bestParams) {
            System.out.println(t.name);
            System.out.println("started at: " + Arrays.toString(t.startRecorder));
            System.out.println("ended at:   " + Arrays.toString(t.values));
        }
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

//        PawnTranspositionTable.reset();
        
        int turnFac = 1;
        if (board.turn == BLACK) {
            turnFac = -1;
        }
        return turnFac * Evaluator.eval(board, board.generateLegalMoves());
    }

    static void test() throws Exception {
        final List<TexelPosLoader.TexelPos> texelPosList = TexelPosLoader.readFile("tuning/test.fen");
        localOptimise(texelPosList);
    }
    static void reset() {
//        PawnTranspositionTable.reset();
//        EngineBetter.resetFull();
    }

    static List<TexelParam> getParams() {
        List<TexelParam> texelParams = new ArrayList<>();

        texelParams.add(new TexelParam("material scores", material, Arrays.asList(0)));
        
//        texelParams.add(new TexelParam("pinned pieces scores", pinnedPiecesScores)); // would this apply in Q pos?
        
        texelParams.add(new TexelParam("misc features", miscFeatures, Arrays.asList(0)));
        
        texelParams.add(new TexelParam("pawn features", pawnFeatures));
        texelParams.add(new TexelParam("knight features", knightFeatures));
        texelParams.add(new TexelParam("bishop features", bishopFeatures));
        texelParams.add(new TexelParam("rook features", rookFeatures));
        
        texelParams.add(new TexelParam("kingSafetyMisc features", kingSafetyMisc));
        
//        texelParams.add(new TexelParam("King safety", EvaluationConstants.KING_SAFETY_ARRAY));
        
//        texelParams.add(new TexelParam("Knight mob", EvaluatorPositionConstant.mobilityScores[0]));
//        texelParams.add(new TexelParam("Bishop mob", EvaluatorPositionConstant.mobilityScores[1]));
//        texelParams.add(new TexelParam("Rook mob", EvaluatorPositionConstant.mobilityScores[2]));
//        texelParams.add(new TexelParam("Queen mob", EvaluatorPositionConstant.mobilityScores[3]));

        return texelParams;
    }
    
    
}
