package tuning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TexelMasterParam {

    String name;
    int[] values;
    int[] startRecorder;
    public boolean pst;

    List<Integer> dontChange;
    public static int delta = 3;

    public TexelMasterParam(String name, int[] values){
        this.name = name;
        this.values = values;
        this.dontChange = new ArrayList<>();
        this.startRecorder = new int[values.length];
        System.arraycopy(values, 0, this.startRecorder, 0, values.length);
        this.pst = false;
    }

    public TexelMasterParam(String name, int[] values, boolean pst){
        this.name = name;
        this.values = values;
        this.dontChange = new ArrayList<>();
        this.startRecorder = new int[values.length];
        System.arraycopy(values, 0, this.startRecorder, 0, values.length);
        this.pst = pst;
    }
    
    public TexelMasterParam(String name, int[] values, List<Integer> dontChange){
        this.name = name;
        this.values = values;
        this.dontChange = dontChange;
        this.startRecorder = new int[values.length];
        System.arraycopy(values, 0, this.startRecorder, 0, values.length);
        this.pst = false;
    }

    public TexelMasterParam(String name, int[] values, List<Integer> dontChange, boolean pst){
        this.name = name;
        this.values = values;
        this.dontChange = dontChange;
        this.startRecorder = new int[values.length];
        System.arraycopy(values, 0, this.startRecorder, 0, values.length);
        this.pst = pst;
    }

    boolean ignoreMe(int i) {
        if (pst) {
            if (i % 8 > 3) {
                return true;
            }
        }
        return false;
    }
    
    
    public void makeUpChange (int i) {
        if (pst) {
            values[i] += delta;
            values[MIRRORED_LEFT_RIGHT[i]] += delta;
        } else {
            values[i] += delta;

        }
    }
    public void makeDownChange (int i) {
        if (pst) {
            values[i] -= delta;
            values[MIRRORED_LEFT_RIGHT[i]] -= delta;
        } else {
            values[i] -= delta;
        }
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
        System.out.println(this);
        if (values.length > 8) {
            System.out.println(str());
        } 
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
