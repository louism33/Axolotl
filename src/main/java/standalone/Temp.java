package standalone;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.Perft;

import java.util.Arrays;

import static com.github.louism33.axolotl.moveordering.MoveOrderer.*;

public class Temp {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        System.out.println(board);
//        int move = Engine.searchFixedTime(board, 10_000);
        int move = Engine.searchFixedDepth(board, 5);
        System.out.println("best move is    "+ MoveParser.toString(move));

        System.out.println("Amount of flipflopping: " + Engine.flipflop);
        System.out.println("Realistic flip flop   : " + Engine.realisticflipflop);
        
    }
}
