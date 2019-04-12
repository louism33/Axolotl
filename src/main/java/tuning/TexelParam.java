package tuning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TexelParam {

    String name;
    int[] values;
    int[] startRecorder;

    List<Integer> dontChange;
    public static int delta = 3;

    public TexelParam(String name, int[] values){
        this.name = name;
        this.values = values;
        this.dontChange = new ArrayList<>();
        this.startRecorder = new int[values.length];
        System.arraycopy(values, 0, this.startRecorder, 0, values.length);
    }
    
    public TexelParam(String name, int[] values, List<Integer> dontChange){
        this.name = name;
        this.values = values;
        this.dontChange = dontChange;
        this.startRecorder = new int[values.length];
        System.arraycopy(values, 0, this.startRecorder, 0, values.length);
    }
    
    public void makeUpChange (int i) {
        values[i] += delta;
    }
    public void makeDownChange (int i) {
        values[i] -= delta;
    }

    @Override
    public String toString() {
        return "TexelParam{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(values) +
                "\n delta: " + delta + 
                '}';
    }
}
