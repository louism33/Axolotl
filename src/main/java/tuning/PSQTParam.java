package tuning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class PSQTParam {
    String name;
    int[] values;
    int[] startRecorder;

    List<Integer> onlyDo;
    public static int delta = 4;

    public PSQTParam(String name, int[] values){
        this.name = name;
        this.values = values;
        this.onlyDo = new ArrayList<>();
    }

    public PSQTParam(String name, int[] values, Integer onlyDo){
        this.name = name;
        this.values = values;
        this.onlyDo = Arrays.asList(onlyDo);
    }

    public PSQTParam(String name, int[] values, List<Integer> onlyDo){
        this.name = name;
        this.values = values;
        this.onlyDo = onlyDo;
    }

    public void makeUpChange (int sq) {
        values[sq] += delta;
        values[MIRRORED_LEFT_RIGHT[sq]] += delta;
    }
    public void makeDownChange (int sq) {
        values[sq] -= delta;
        values[MIRRORED_LEFT_RIGHT[sq]] -= delta;
    }


    @Override
    public String toString() {
        return "PSQTParam{" +
                "name='" + name + '\'' +
                ", onlyDo=" + onlyDo + " , delta: " + delta + 
                '}';
    }

    public void printMe() {
        System.out.println(str());
    }

    public String str() {
        String s = "\n"+name+"\n";
        for (int j = 0; j < 64; j++) {
            s += values[j] + ", ";
            if (j > 0 && j % 8 == 7) {
                s += "\n";
            }
        }
        s += "\n";
        return s;
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
