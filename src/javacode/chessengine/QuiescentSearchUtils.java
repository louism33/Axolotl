package javacode.chessengine;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;
import java.util.stream.Collectors;

public class QuiescentSearchUtils {

    static boolean moveIsCapture(Chessboard board, Move move){
        long ENEMY_PIECES = board.isWhiteTurn() ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        long destinationSquare = BitManipulations.newPieceOnSquare(move.destination);
        return (destinationSquare & ENEMY_PIECES) != 0;
    }

    static boolean isNodeQuiet(Chessboard board, List<Move> moves){
        for (Move move : moves){
            if(moveIsCapture(board, move)){
                return false;
            }
        }
        return true;
    }
    
    static List<Move> onlyCaptureMoves(Chessboard board, List<Move> moves){
        return moves.stream().filter(move -> moveIsCapture(board, move)).collect(Collectors.toList());
    }
    
    
}
