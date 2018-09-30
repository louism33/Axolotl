package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

class MoveGeneratorSliding {

    static List<Move> masterMoveSliding (Chessboard board, boolean white){
        long ans = 0, bishops, rooks, queens;
        List<Move> moves = new ArrayList<>();
        if (white){
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
        }
        else {
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
        }

        List<Long> allBishops = getAllPieces(bishops);
        for (Long piece : allBishops){
            long slidingMoves = PieceMoveSliding.bishopSlidingMove(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allRooks = getAllPieces(rooks);
        for (Long piece : allRooks){
            long slidingMoves = PieceMoveSliding.rookSlidingMove(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allQueens = getAllPieces(queens);
        for (Long piece : allQueens){
            long slidingMoves = PieceMoveSliding.queenSlidingMove(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        return moves;
    }

    static List<Move> movesFromAttackBoard (long attackBoard, int source) {
        List<Move> moves = new ArrayList<>();
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces){
            moves.add(new Move(source, i));
        }
        return moves;
    }

}
