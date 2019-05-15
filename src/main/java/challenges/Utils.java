package challenges;

public final class Utils {

    public static boolean contains(int[] ints, int target) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String[] ints, String target) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i].equals(target)) {
                return true;
            }
        }
        return false;
    }
}
