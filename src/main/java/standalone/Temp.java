package standalone;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.Perft;

public class Temp {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        System.out.println(board);
//        int move = Engine.searchFixedTime(board, 10_000);
        int move = Engine.searchFixedDepth(board, 2);
        System.out.println("best move is    "+ MoveParser.toString(move));
//        System.out.println(MoveParser.toString(move));
//        System.out.println("NPS:                " + Engine.nps);

//        long l = Perft.perftTest(6, board);
//        System.out.println(l);
    }
}
