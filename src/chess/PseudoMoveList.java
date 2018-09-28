package chess;

import bitboards.WhichTable;

import java.util.ArrayList;
import java.util.List;

public class PseudoMoveList {



    static List<Move> pseudoAllPieceTables(Chessboard board, boolean white){
        List<Move> allMoves = new ArrayList<>(); //todo, no captures, but this will not be used anyway
        if (white){
            allMoves.addAll(pseudoPieceTables(board, board.WHITE_PAWNS));
            allMoves.addAll(pseudoPieceTables(board, board.WHITE_KNIGHTS));
            allMoves.addAll(pseudoPieceTables(board, board.WHITE_BISHOPS));
            allMoves.addAll(pseudoPieceTables(board, board.WHITE_ROOKS));
            allMoves.addAll(pseudoPieceTables(board, board.WHITE_QUEEN));
            allMoves.addAll(pseudoPieceTables(board, board.WHITE_KING));
        }
        else {
            allMoves.addAll(pseudoPieceTables(board, board.BLACK_PAWNS));
            allMoves.addAll(pseudoPieceTables(board, board.BLACK_KNIGHTS));
            allMoves.addAll(pseudoPieceTables(board, board.BLACK_BISHOPS));
            allMoves.addAll(pseudoPieceTables(board, board.BLACK_ROOKS));
            allMoves.addAll(pseudoPieceTables(board, board.BLACK_QUEEN));
            allMoves.addAll(pseudoPieceTables(board, board.BLACK_KING));
        }
        return allMoves;
    }

    static List<Move> pseudoPieceTables(Chessboard board, long pieces){
        List<Integer> allPieces = BitIndexing.getIndexOfAllPieces(pieces);
        List<Move> allMoves = new ArrayList<>();
        for (Integer piece : allPieces) {
            List<Integer> allTargets = BitIndexing.getIndexOfAllPieces(WhichTable.whichTable(board, pieces)[piece]);
            for (Integer target : allTargets) {
//                allMoves.add(new Move(piece, target));
            }
        }
        return allMoves;
    }
}
