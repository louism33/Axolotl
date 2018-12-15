package standalone;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

class Temp {
    
    public static void main(String[] args){
//        Chessboard board = new Chessboard("k7/3b4/K7/8/8/8/3B4/8 w - -");
        
        Chessboard board = new Chessboard();
        System.out.println(board);
        
        
        int i = Engine.searchFixedDepth(board, 2);
        System.out.println(MoveParser.toString(i));

        int eval = Evaluator.eval(board, board.isWhiteTurn(), board.generateLegalMoves());

        System.out.println("eval: " + eval);
    }
}
