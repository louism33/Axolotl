package chess;


import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class Main {




    public static void main(String[] args) {
        new Main();
    }






    Main(){


        Moves m = new Moves();


//        long attackTables = allPieceAttackTables(BLACK_PIECES);
//        printLong(attackTables);

//        long pawnsAttackTables = allPawnsAttackTables(WHITE_PIECES, true);
//        long pawnsAttackTables = allPawnsAttackTables(BLACK_PIECES, false);
//        printLong(pawnsAttackTables);

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
