package main.utils;

import main.chess.Art;
import main.chess.Chessboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBoard {

    public static void printBoards (Chessboard[] bs){
        for (int i = 0; i < bs.length; i++) {
            Chessboard b = bs[i];
            System.out.println("--------- " + i + " -------------");
            System.out.println("\n--- " + i + " ---");
            String s = Art.boardArt(bs[i]);
            System.out.println(s);
        }
    }
    
    public static Chessboard[] boardForTests (){
        int num = 18;
        Chessboard[] bs = new Chessboard[num];
        for (int i = 0; i < num; i ++) {
            if (i > 0 && i % 3 == 0) {
                bs[i] = RandomBoard.makeRandomBoard(i * 100);
            }
            else {
                bs[i] = RandomBoard.makeRandomBoard(i);
            }
        }
        return bs;
    }
    
    private static List<Long> randomLongs(Random r, int num){
        List<Long> ans = new ArrayList<>();
        int i = 0;
        while (i < 12){
            int j = r.nextInt(63);
            double pow = Math.pow(2, j);
            long l = (long) pow;

            if (ans.contains(l)) {
                continue;
            }
            ans.add(l);
            i++;
        }
        return ans;
    }

    private static Chessboard makeRandomBoard(long seed){
        Chessboard board = new Chessboard();
        Random r = new Random(seed);
        int ii = 12;
        List<Long> longs = randomLongs(r, ii);

        board.WHITE_PAWNS = longs.get(0);
        board.WHITE_KNIGHTS = longs.get(1);
        board.WHITE_BISHOPS = longs.get(2);
        board.WHITE_ROOKS = longs.get(3);
        board.WHITE_QUEEN = longs.get(4);
        board.WHITE_KING = longs.get(5);

        board.BLACK_PAWNS = longs.get(6);
        board.BLACK_KNIGHTS = longs.get(7);
        board.BLACK_BISHOPS = longs.get(8);
        board.BLACK_ROOKS = longs.get(9);
        board.BLACK_QUEEN = longs.get(10);
        board.BLACK_KING = longs.get(11);

        return board;
    }


}
