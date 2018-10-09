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

//    public static List<Move> masterMovePinned (Chessboard board, boolean white,
//                                               long ignoreThesePieces, long legalPushes, long legalCaptures){
//        long ans = 0, bishops, rooks, queens;
//        List<Move> moves = new ArrayList<>();
//        if (white){
//            bishops = board.WHITE_BISHOPS;
//            rooks = board.WHITE_ROOKS;
//            queens = board.WHITE_QUEEN;
//        }
//        else {
//            bishops = board.BLACK_BISHOPS;
//            rooks = board.BLACK_ROOKS;
//            queens = board.BLACK_QUEEN;
//        }
//
//        List<Long> allBishops = getAllPieces(bishops, ignoreThesePieces);
//        for (Long piece : allBishops){
//            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
//
//            long bishopPushes = PieceMoveSliding.singleBishopPushes(board, piece, white, legalPushes);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(bishopPushes, indexOfPiece));
//
//            long bishopCaptures = PieceMoveSliding.singleBishopCaptures(board, piece, white, legalCaptures);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(bishopCaptures, indexOfPiece));
//        }
//
//        List<Long> allRooks = getAllPieces(rooks, ignoreThesePieces);
//        for (Long piece : allRooks){
//            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
//
//            long rookPushes = PieceMoveSliding.singleRookPushes(board, piece, white, legalPushes);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(rookPushes, indexOfPiece));
//
//            long rookCaptures = PieceMoveSliding.singleRookCaptures(board, piece, white, legalCaptures);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(rookCaptures, indexOfPiece));
//        }
//        List<Long> allQueens = getAllPieces(queens, ignoreThesePieces);
//        for (Long piece : allQueens){
//            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
//
//            long queenPushes = PieceMoveSliding.singleQueenPushes(board, piece, white, legalPushes);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(queenPushes, indexOfPiece));
//
//            long queenCaptures = PieceMoveSliding.singleQueenCaptures(board, piece, white, legalCaptures);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(queenCaptures, indexOfPiece));
//        }
//        return moves;
//    }

}
