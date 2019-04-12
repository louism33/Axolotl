package tuning;

import com.github.louism33.utils.TexelPosLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static tuning.PSQTTuner.getEvalScore;
import static tuning.PSQTTuner.sigmoid;

public class ErrorCalculator implements Callable<Double> {

    private List<TexelPosLoader.TexelPos> texelPosList;

    public ErrorCalculator() {
        texelPosList = new ArrayList<>();
    }
    
    public ErrorCalculator(List<TexelPosLoader.TexelPos> texelPosList) {
        this.texelPosList = texelPosList;
    }

    public void add(TexelPosLoader.TexelPos pos) {
        texelPosList.add(pos);
    }
    
    
    @Override
    public Double call() {
        return getE(texelPosList);
    }

    private static double getE(List<TexelPosLoader.TexelPos> texelPosList) {
        final double N = texelPosList.size();

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
        return totalError;
    }


}
