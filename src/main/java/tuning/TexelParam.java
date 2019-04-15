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
}
