package chess;

import static chess.BitIndexing.pieceOnSquare;

public class Art {



    static void printPieceOnSquare(Chessboard board, int s){
        printPieceByNumber(pieceOnSquare(board, s));
    }

    static void printPieceByNumber (int s){
        if (s == 0) System.out.println("Empty Square at "+s);

        if (s == 1) System.out.println("White Pawn at "+s);
        if (s == 2) System.out.println("White Knight at "+s);
        if (s == 3) System.out.println("White Bishop at "+s);
        if (s == 4) System.out.println("White Rook at "+s);
        if (s == 5) System.out.println("White Queen at "+s);
        if (s == 6) System.out.println("White King at "+s);

        if (s == 7) System.out.println("Black Pawn at "+s);
        if (s == 8) System.out.println("Black Knight at "+s);
        if (s == 9) System.out.println("Black Bishop at "+s);
        if (s == 10) System.out.println("Black Rook at "+s);
        if (s == 11) System.out.println("Black Queen at "+s);
        if (s == 12) System.out.println("Black King at "+s);
    }

    static String printPieceByNumberASCII (int s){

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
        for (int y=7; y>=0; y--) {
            s += (y+1) + " |";
            for (int x=0; x<8; x++) {
                s += printPieceByNumberASCII( pieceOnSquare(board, x + y*8) );
                if (x<7) s += " ";
            }
            s += "| " + (y+1);
            s += "\n";
        }
        s += "  +---------------+\n";
        s += "   a b c d e f g h\n";
        return s;
    }


    public static String makeMoveToString (int l){
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

    public static String makeIntegerString (int l){
        String s = Integer.toBinaryString(l);
        for (int y = 0; y < 4; y++) {
            for (int i = 0; i < 8; i++) {
                while (s.length() < 32) {
                    s = "0"+s;
                }
            }
        }
        return s;
    }


    public static void printInteger (int l){
        for (int y = 0; y < 4; y++) {
            for (int i = 0; i < 8; i++) {
                String s = Integer.toBinaryString(l);
                while (s.length() < 32) {
                    s = "0"+s;
                }
                System.out.print(s.charAt(y * 8 + i));
            }
            System.out.println();
        }
        System.out.println("---");
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

    public static void printArrayOfLongs(long[] board){
        for (long l : board){
            printLong(l);
            System.out.println("---");
        }
    }


}
