package main.chess;

import static main.chess.BitIndexing.pieceOnSquare;

public class Art {

    private static String pieceByNumberASCII(int s){
        if (s == 1) return ("P");
        if (s == 2) return ("N");
        if (s == 3) return ("B");
        if (s == 4) return ("R");
        if (s == 5) return ("Q");
        if (s == 6) return ("K");

        if (s == 7) return ("p");
        if (s == 8) return ("n");
        if (s == 9) return ("b");
        if (s == 10) return ("r");
        if (s == 11) return ("q");
        if (s == 12) return ("k");
        else return (".");
    }

    public static String boardArt (Chessboard board) {
        StringBuilder s = new StringBuilder();
        s.append("   a b c d e f g h\n");
        s.append("  +---------------+\n");
        for (int y = 7; y >= 0; y--) {
            s.append(y + 1).append(" |");
            for (int x = 7; x >= 0; x--) {
                s.append(pieceByNumberASCII(pieceOnSquare(board, x + y * 8)));
                if (x>0) s.append(" ");
            }
            s.append("| ").append(y + 1);
            s.append("\n");
        }
        s.append("  +---------------+\n");
        s.append("   a b c d e f g h\n");

        return s.toString();
    }


    static String makeMoveToString (int l){
        String binaryString = Integer.toBinaryString(l);
        int numberOfPaddingZeros = 16 - binaryString.length();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numberOfPaddingZeros){
            sb.append("0");
        }

        String temp = sb.toString() + "" + binaryString;
        return temp.substring(0, 6) +"\n" +
                temp.substring(6, 12) + "\n" +
                temp.substring(12, 16);
    }

    public static void printLong(long l){
        for (int y = 0; y < 8; y++) {
            for (int i = 0; i < 8; i++) {
                StringBuilder s = new StringBuilder(Long.toBinaryString(l));
                while (s.length() < 64) {
                    s.insert(0, "0");
                }
                System.out.print(s.charAt(y * 8 + i));
            }
            System.out.println();
        }
        System.out.println("---");
    }

}
