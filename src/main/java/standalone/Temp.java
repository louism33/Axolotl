package standalone;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

class Temp {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        
        Engine.setup();
        Engine.giveThreadsBoard(board);

        int move = Engine.searchFixedDepth(board, 8);
//        int move = Engine.searchFixedTime(board, 1000, false);

        System.out.println("///////////////////////////");
        System.out.println("FINAL MOVE");
        System.out.println(MoveParser.toString(move));
    }
}