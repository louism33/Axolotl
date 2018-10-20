package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

import static javacode.chessengine.MoveOrderer.moveIsCapture;

class QuiescentSearchUtils {

    static boolean isBoardQuiet(Chessboard board, List<Move> moves){
        for (Move move : moves){
            if(moveIsCapture(board, move)){
                return false;
            }
        }
        return true;
    }
    

    
    
}
