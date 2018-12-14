package standalone;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.*;

public class Temp {
    
    public static void main(String[] args){
        
//        String fen = "rnbqk2r/pppp1ppp/4p3/8/4P3/8/PPPbPPPP/R2QKBNR w KQkq - 0 6";
//        Chessboard board = new Chessboard(fen);

        GenericBoard g = new GenericBoard();

//        EngineAnalyzeCommand e = new EngineAnalyzeCommand()

//        Chessboard board = new Chessboard();
//        int move = Engine.searchFixedTime(board, 1000);
//        System.out.println(MoveParser.toString(move));

        int x = 25;
        
        long piece = BitOperations.newPieceOnSquare(x);

        Art.printLong(piece);
        
        Art.printLong(BitboardResources.blackPawnKillZone[x]);
    }
}
