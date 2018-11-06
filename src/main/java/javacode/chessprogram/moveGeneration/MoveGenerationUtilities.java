package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerationUtilities {

    public static List<Move> movesFromAttackBoard(long attackBoard, int source) {
        List<Move> moves = new ArrayList<>();
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
        return moves;
    }

    static List<Move> movesFromAttackBoardLong(long attackBoard, long longSource) {
        List<Move> moves = new ArrayList<>();
        int source = BitIndexing.getIndexOfFirstPiece(longSource);
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces) {
            moves.add(new Move(source, i));
        }
        return moves;
    }

}