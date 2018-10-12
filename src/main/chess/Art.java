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
        String s = "";
        s += "   a b c d e f g h\n";
        s += "  +---------------+\n";
        for (int y = 7; y >= 0; y--) {
            s += (y+1) + " |";
            for (int x = 7; x >= 0; x--) {
                s += pieceByNumberASCII( pieceOnSquare(board, x + y*8) );
                if (x>0) s += " ";
            }
            s += "| " + (y+1);
            s += "\n";
        }
        s += "  +---------------+\n";
        s += "   a b c d e f g h\n";

        return s;
    }


    static String makeMoveToString (int l){
        String binaryString = Integer.toBinaryString(l);
        int numberOfPaddingZeros = 16 - binaryString.length();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numberOfPaddingZeros){
            sb.append("0");
        }

        String temp = sb.toString() + "" + binaryString;

        String answer = temp.substring(0, 6) +"\n" +
                temp.substring(6, 12) + "\n" +
                temp.substring(12, 16);

        return answer;
    }

    public static void printLong(long l){
        for (int y = 0; y < 8; y++) {
            for (int i = 0; i < 8; i++) {
                String s = Long.toBinaryString(l);
                while (s.length() < 64) {
                    s = "0"+s;
                }
                System.out.print(s.charAt(y * 8 + i));
            }
            System.out.println();
        }
        System.out.println("---");
    }

}
