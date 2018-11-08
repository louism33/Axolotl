package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

class QuiescentSearchUtils {

    private MoveOrderer moveOrderer;
    
    QuiescentSearchUtils(MoveOrderer moveOrderer) {
        this.moveOrderer = moveOrderer;
    }
    /*
    a board is quiet when there are no more captures that can be made
     */
    boolean isBoardQuiet(Chessboard board, List<Move> moves){
        for (Move move : moves){
            if(this.moveOrderer.moveIsCapture(board, move)){
                return false;
            }
        }
        return true;
    }
    
}
