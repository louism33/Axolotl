package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class MoveGeneratorKing {

//    public static List<Move> masterMoveKing(Chessboard board, boolean white,
//                                            long ignoreThesePieces, long legalPushes, long legalCaptures){
//        long ans = 0, king;
//        List<Move> moves = new ArrayList<>();
//        if (white){
//            king = board.WHITE_KING;
//        }
//        else {
//            king = board.BLACK_KING;
//        }
//
//        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
//        for (Long piece : allKings){
//            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
//
//            long kingPushes = PieceMoveKing.singleKingPushes(board, piece, white, legalPushes);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(kingPushes, indexOfPiece));
//
//            long kingCaptures = PieceMoveKing.singleKingCaptures(board, piece, white, legalCaptures);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(kingCaptures, indexOfPiece));
//        }
//        return moves;
//    }

//    public static List<Move> masterKingCaptures(Chessboard board, boolean white,
//                                                long ignoreThesePieces, long legalCaptures){
//        long ans = 0, king;
//        List<Move> moves = new ArrayList<>();
//        if (white){
//            king = board.WHITE_KING;
//        }
//        else {
//            king = board.BLACK_KING;
//        }
//
//        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
//        for (Long piece : allKings){
//            long jumpingMoves = PieceMoveKing.singleKingCaptures(board, piece, white, legalCaptures);
//            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
//        }
//        return moves;
//    }


//    public static List<Move> masterKingPushes(Chessboard board, boolean white,
//                                              long ignoreThesePieces, long legalPushes){
//        long ans = 0, king;
//        List<Move> moves = new ArrayList<>();
//        if (white){
//            king = board.WHITE_KING;
//        }
//        else {
//            king = board.BLACK_KING;
//        }
//
//        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
//        for (Long piece : allKings){
//            long jumpingMoves = PieceMoveKing.singleKingPushes(board, piece, white, legalPushes);
//            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
//            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
//        }
//        return moves;
//    }
}
