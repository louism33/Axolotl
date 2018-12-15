package standalone;

import com.fluxchess.jcpi.models.GenericBoard;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

public class Temp {
    
    public static void main(String[] args){
        
//        String fen = "rnbqk2r/pppp1ppp/4p3/8/4P3/8/PPPbPPPP/R2QKBNR w KQkq - 0 6";
//        Chessboard board = new Chessboard(fen);

        GenericBoard g = new GenericBoard();

//        EngineAnalyzeCommand e = new EngineAnalyzeCommand()

        Chessboard board = new Chessboard();
//        int move = Engine.searchFixedTime(board, 1000);
//        System.out.println(MoveParser.toString(move));

        int a = 2047;
        int b = 1024;

        System.out.println(a % b);
        System.out.println(-a % b);
        System.out.println((((-a % b) + b) % b));
        System.out.println(a % -b);
        System.out.println(-a % -b);
    }
}
