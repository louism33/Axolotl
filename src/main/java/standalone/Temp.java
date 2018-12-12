package standalone;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.Perft;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.github.louism33.axolotl.moveordering.MoveOrderer.*;
import static com.github.louism33.axolotl.search.Engine.flips;

public class Temp {
    
    public static int biggy = 0;
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        System.out.println(board);
//        int move = Engine.searchFixedTime(board, 10_000);
//        int move = Engine.searchFixedDepth(board, 5);
//        System.out.println("best move is    "+ MoveParser.toString(move));

        System.out.println("Amount of flipflopping: " + Engine.flipflop);
        System.out.println("Realistic flip flop   : " + Engine.realisticflipflop);

        System.out.println();
        System.out.println(flips);
        System.out.println();
        System.out.println(MoveParser.toString(biggy) +"    "+biggy);
        Art.printLong(biggy);

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
