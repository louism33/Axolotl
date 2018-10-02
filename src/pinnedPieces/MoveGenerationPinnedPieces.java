package pinnedPieces;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGenerationUtilities;
import moveGeneration.PieceMoveSliding;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class MoveGenerationPinnedPieces {

    public static List<Move> masterMovePinned (Chessboard board, boolean white, long legalPushes, long legalCaptures){
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
            long slidingMoves = PieceMoveSliding.bishopSlidingMove(board, piece, white, legalPushes, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allRooks = getAllPieces(rooks);
        for (Long piece : allRooks){
            long slidingMoves = PieceMoveSliding.rookSlidingMove(board, piece, white, legalPushes, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        List<Long> allQueens = getAllPieces(queens);
        for (Long piece : allQueens){
            long slidingMoves = PieceMoveSliding.queenSlidingMove(board, piece, white, legalPushes, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(slidingMoves, indexOfPiece));
        }
        return moves;
    }

}
