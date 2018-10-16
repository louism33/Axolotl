package javacode.chessengine;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;
import java.util.stream.Collectors;

import static javacode.chessengine.MoveOrderer.moveIsCapture;

public class QuiescentSearchUtils {

    static boolean isNodeQuiet(Chessboard board, List<Move> moves){
        for (Move move : moves){
            if(moveIsCapture(board, move)){
                return false;
            }
        }
        return true;
    }
    

    
    
}
