package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveParser;

import java.util.ArrayList;
import java.util.List;

import static javacode.chessprogram.chess.BitExtractor.getAllPieces;

public class MoveGeneratorPromotion {

    public static List<Move> generatePromotionMoves(Chessboard board, boolean white,
                                                    long ignoreThesePieces, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(generatePromotionPushes(board, white, ignoreThesePieces, legalPushes));
        moves.addAll(generatePromotionCaptures(board, white, ignoreThesePieces, legalCaptures));
        return moves;
    }

    private static List<Move> generatePromotionPushes(Chessboard board, boolean white, 
                                                     long ignoreThesePieces, long legalPushes){
        List<Move> moves = new ArrayList<>();
        long legalPieces = ~ignoreThesePieces;

        if (white){
            long PENULTIMATE_RANK = BitBoards.RANK_SEVEN;
            long promotablePawns = board.WHITE_PAWNS & PENULTIMATE_RANK & legalPieces;
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, white, (BitBoards.RANK_EIGHT & legalPushes));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
                        Move move = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece).get(0);
                        move.move |= MoveParser.PROMOTION_MASK;
                        moves.addAll(promotingMovesByPiece(move));
                    }
                }
            }
        }

        else {
            long PENULTIMATE_RANK = BitBoards.RANK_TWO;
            long promotablePawns = board.BLACK_PAWNS & PENULTIMATE_RANK & legalPieces;
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, white, (BitBoards.RANK_ONE & legalPushes));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);

                        Move move = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece).get(0);
                        move.move |= MoveParser.PROMOTION_MASK;
                        moves.addAll(promotingMovesByPiece(move));
                    }
                }
            }
        }

        return moves;
    }

    private static List<Move> generatePromotionCaptures(Chessboard board, boolean white,
                                                       long ignoreThesePieces, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        long legalPieces = ~ignoreThesePieces;

        if (white){
            long PENULTIMATE_RANK = BitBoards.RANK_SEVEN;
            long promotablePawns = board.WHITE_PAWNS & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitBoards.RANK_EIGHT & board.ALL_BLACK_PIECES();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, white, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece);

                        for (Move move : unflaggedCaptures) {
                            move.move |= MoveParser.PROMOTION_MASK;
                            moves.addAll(promotingMovesByPiece(move));
                        }
                    }
                }
            }
        }

        else {
            long PENULTIMATE_RANK = BitBoards.RANK_TWO;
            long promotablePawns = board.BLACK_PAWNS & PENULTIMATE_RANK & legalPieces;
            long promotionCaptureSquares = BitBoards.RANK_ONE & board.ALL_WHITE_PIECES();
            if ((promotablePawns) != 0) {
                List<Long> allPromotablePawns = getAllPieces(promotablePawns, 0);
                for (long piece : allPromotablePawns) {
                    long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, white, (promotionCaptureSquares & legalCaptures));
                    if (pawnMoves != 0) {
                        int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
                        List<Move> unflaggedCaptures = MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece);

                        for (Move move : unflaggedCaptures) {
                            move.move |= MoveParser.PROMOTION_MASK;
                            moves.addAll(promotingMovesByPiece(move));
                        }
                    }
                }
            }
        }

        return moves;
    }




    private static List<Move> promotingMovesByPiece(Move move){
        List<Move> moves = new ArrayList<>();

        Move moveK = Copier.copyMove(move);
        moveK.move |= MoveParser.KNIGHT_PROMOTION_MASK;
        moves.add(moveK);

        Move moveB = Copier.copyMove(move);
        moveB.move |= MoveParser.BISHOP_PROMOTION_MASK;
        moves.add(moveB);

        Move moveR = Copier.copyMove(move);
        moveR.move |= MoveParser.ROOK_PROMOTION_MASK;
        moves.add(moveR);

        Move moveQ = Copier.copyMove(move);
        moveQ.move |= MoveParser.QUEEN_PROMOTION_MASK;
        moves.add(moveQ);

        return moves;
    }
}
