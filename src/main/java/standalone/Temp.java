package standalone;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.Perft;

public class Temp {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
//        int move = Engine.searchFixedTime(board, 120_000);
//        System.out.println(MoveParser.toString(move));
//        System.out.println("NPS:                " + Engine.nps);

        long l = Perft.perftTest(6, board);
        System.out.println(l);
    }
}
