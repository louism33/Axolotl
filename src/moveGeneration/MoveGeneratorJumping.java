package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class MoveGeneratorJumping {

    public static List<Move> masterMoveJumping(Chessboard board, boolean white, long legalPushes, long legalCaptures){
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
            long jumpingMoves = PieceMoveJumping.knightMove(board, piece, white, legalPushes, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }

        List<Long> allKings = getAllPieces(king);
        for (Long piece : allKings){
            long jumpingMoves = PieceMoveJumping.kingMove(board, piece, white, legalPushes, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }
        return moves;
    }


}
