package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class MoveGeneratorKing {

    public static List<Move> masterMoveKing(Chessboard board, boolean white,
                                            long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, king;
        List<Move> moves = new ArrayList<>();
        if (white){
            king = board.WHITE_KING;
        }
        else {
            king = board.BLACK_KING;
        }

        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
        for (Long piece : allKings){
            long kingMoves = PieceMoveKing.singleKingAllMoves(board, piece, white, legalPushes, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(kingMoves, indexOfPiece));
        }
        return moves;
    }

    public static List<Move> masterKingCaptures(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalCaptures){
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

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            long jumpingMoves = PieceMoveKnight.singleKnightCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }

        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
        for (Long piece : allKings){
            long jumpingMoves = PieceMoveKing.singleKingCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }
        return moves;
    }


    public static List<Move> masterKingPushes(Chessboard board, boolean white,
                                              long ignoreThesePieces, long legalPushes){
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

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            long jumpingMoves = PieceMoveKnight.singleKnightPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }

        List<Long> allKings = getAllPieces(king, ignoreThesePieces);
        for (Long piece : allKings){
            long jumpingMoves = PieceMoveKing.singleKingPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }
        return moves;
    }
}
