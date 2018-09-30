package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

class MoveGeneratorJumping {

    static List<Move> masterMoveJumping(Chessboard board, boolean white){
        long ans = 0, knights, king;
        List<Move> moves = new ArrayList<>();
        if (white){
            knights = board.WHITE_KNIGHTS;
            king = board.WHITE_KING;
        }
        else {
            knights = board.BLACK_KNIGHTS;
            king = board.BLACK_KING;
        }

        List<Long> allKnights = getAllPieces(knights);
        for (Long piece : allKnights){
            long jumpingMoves = PieceMoveJumping.knightMove(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            System.out.println("    "+indexOfPiece);
            moves.addAll(movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }

        List<Long> allKings = getAllPieces(king);
        for (Long piece : allKings){
            long jumpingMoves = PieceMoveJumping.kingMove(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(movesFromAttackBoard(jumpingMoves, indexOfPiece));
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
