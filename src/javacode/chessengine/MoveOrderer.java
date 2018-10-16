package javacode.chessengine;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class MoveOrderer {
    
    static Move[] killerMoves = new Move[2];

    static List<Move> orderMoves (Chessboard board, Move hashMove, List<Move> moves){
        List<Move> orderedMoves = orderMovesHelper(board, moves);

        if (hashMove != null) {
            Assert.assertTrue(orderedMoves.contains(hashMove));

            orderedMoves.remove(hashMove);
            orderedMoves.add(0, hashMove);

            Assert.assertTrue(orderedMoves.get(0).equals(hashMove));
        }

        return moves;
    }

    static List<Move> orderMovesHelper (Chessboard board, List<Move> moves) {
        List<Move> quietMoves = new ArrayList<>();
        List<Move> captureMoves = new ArrayList<>();
        for (Move move : moves){
            if(moveIsCapture(board, move)){
                captureMoves.add(move);
            }
            else {
                quietMoves.add(move);
            }
        }
        List<Move> finalMovesInOrder = new ArrayList<>(captureMoves);
        finalMovesInOrder.addAll(quietMoves);
        return finalMovesInOrder;
    }

    static List<Move> orderMovesQuiescence (Chessboard board, List<Move> moves){
        for (Move move : moves){
            Assert.assertTrue(moveIsCapture(board, move));
        }
        return moves;
    }
    
    void leastValuableAttackerMostValuableDefender(Chessboard board, List<Move> moves){
        
    }


    static boolean moveIsCapture(Chessboard board, Move move){
        long ENEMY_PIECES = board.isWhiteTurn() ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        long destinationSquare = BitManipulations.newPieceOnSquare(move.destination);
        return (destinationSquare & ENEMY_PIECES) != 0;
    }


    static List<Move> onlyCaptureMoves(Chessboard board, List<Move> moves){
        return moves.stream().filter(move -> moveIsCapture(board, move)).collect(Collectors.toList());
    }
}
