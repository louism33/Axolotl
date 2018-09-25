package chess;

import bitboards.Bishop;
import bitboards.BitBoards;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    /*





    15 14 13 12 11 10 9  8
    7  6  5  4  3  2  1  0
     */

    Main(){

        int indexOfFirstPiece = BitManipulations.extractPieceIndexOrganiser(BitBoards.WHITE_ROOKS);

        System.out.println(indexOfFirstPiece);

    }


    static void printLong(long l){
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
    }

    static void printArrayOfLongs(long[] board){
        for (long l : board){
            printLong(l);
            System.out.println("---");
        }
    }


}
