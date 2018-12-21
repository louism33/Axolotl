package standalone;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

class Temp {
    
    public static void main (String[] args){
//        Chessboard board = new Chessboard();
        Chessboard board = new Chessboard("8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -");
        
        Engine.setup();
        Engine.giveThreadsBoard(board);

        System.out.println(board);

//        int move = Engine.searchFixedDepth(board, 20);
//        int move = Engine.searchFixedTime(board, 60000, false);

        System.out.println("///////////////////////////");
        System.out.println("FINAL MOVE");
//        System.out.println(MoveParser.toString(move));

        System.out.println();
        System.out.println();

        System.out.println(Evaluator.eval(board, board.isWhiteTurn(), board.generateLegalMoves()));
        Evaluator.printEval(board);
    }
}
