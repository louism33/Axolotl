package standalone;

import com.github.louism33.chesscore.Chessboard;

import java.util.List;

class Temp {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        System.out.println(board);
//        int move = Engine.searchFixedTime(board, 10_000);
//        int move = Engine.searchFixedDepth(board, 5);
//        System.out.println("best move is    "+ MoveParser.toString(move));


        System.out.println(Integer.toBinaryString(8));
        System.out.println(Integer.toBinaryString(-8));
        System.out.println(8 & -8);

    }
    
    
    public static void flipTest(List<List<String>> flip){
        System.out.println("FLIP TEST : ");
        for (int i = 1; i < flip.size(); i ++){

            List<String> prevFlip = flip.get(i - 1);
            List<String> thisFlip = flip.get(i);

            if (!thisFlip.get(0).equals(prevFlip.get(1))){
                System.out.println("ERROR");
                System.out.println(prevFlip);
                System.out.println(thisFlip);
//                Assert.assertEquals(thisFlip.get(0), prevFlip.get(1));
            }
        }
    }
}
