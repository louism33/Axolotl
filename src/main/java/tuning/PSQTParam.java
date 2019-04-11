package tuning;

import com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;

@SuppressWarnings("ALL")
public class PSQTParam {
    String name;
    int[][][] values;
    int[] startRecorder;

    List<Integer> onlyDo;
    int delta = 1;

    public PSQTParam(String name){
        this.name = name;
        this.values = EvaluatorPositionConstant.POSITION_SCORES;
        this.onlyDo = new ArrayList<>();
    }

    public PSQTParam(String name, Integer onlyDo){
        this.name = name;
        this.values = EvaluatorPositionConstant.POSITION_SCORES;
        this.onlyDo = Arrays.asList(onlyDo);
    }

    public PSQTParam(String name, List<Integer> onlyDo){
        this.name = name;
        this.values = EvaluatorPositionConstant.POSITION_SCORES;
        this.onlyDo = onlyDo;
    }

    public void makeUpChange (int piece, int sq) {
        values[WHITE][piece][sq] += delta;
        values[WHITE][piece][MIRRORED_LEFT_RIGHT[sq]] += delta;
        
        values[BLACK][piece][MIRRORED_UP_DOWN[sq]] += delta;
        values[BLACK][piece][MIRRORED_LEFT_RIGHT[MIRRORED_UP_DOWN[sq]]] += delta;
    }
    public void makeDownChange (int piece, int sq) {
        values[WHITE][piece][sq] -= delta;
        values[WHITE][piece][MIRRORED_LEFT_RIGHT[sq]] -= delta;

        values[BLACK][piece][MIRRORED_UP_DOWN[sq]] -= delta;
        values[BLACK][piece][MIRRORED_LEFT_RIGHT[MIRRORED_UP_DOWN[sq]]] -= delta;
    }


    @Override
    public String toString() {
        return "PSQTParam{" +
                "name='" + name + '\'' +
                ", onlyDo=" + onlyDo +
                '}';
    }

    public void printOne(int p) {
        System.out.println();
        for (int j = 0; j < 64; j++) {
            System.out.print(values[BLACK][p][MIRRORED_UP_DOWN[j]] + ", ");
            if (j > 0 && j % 8 == 7) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        PSQTParam p = new PSQTParam("ssss");
        p.printOne(1);
        
    }

    public static final int[] MIRRORED_LEFT_RIGHT = new int[64];
    static {
        for (int i = 0; i < 64; i++) {
            MIRRORED_LEFT_RIGHT[i] = (i / 8) * 8 + 7 - (i & 7);
        }
    }

    public static final int[] MIRRORED_UP_DOWN = new int[64];
    static {
        for (int i = 0; i < 64; i++) {
            MIRRORED_UP_DOWN[i] = (7 - i / 8) * 8 + (i & 7);
        }
    }
}
