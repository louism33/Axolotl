package main.moveGeneration;

import main.chess.BitIndexing;
import main.chess.Chessboard;
import main.chess.Move;

import java.util.ArrayList;
import java.util.List;

import static main.chess.BitExtractor.getAllPieces;

class MoveGeneratorKnight {

    static List<Move> masterKnightCaptures(Chessboard board, boolean white,
                                                  long ignoreThesePieces, long legalCaptures){
        long ans = 0, knights;
        List<Move> moves = new ArrayList<>();
        if (white){
            knights = board.WHITE_KNIGHTS;
        }
        else {
            knights = board.BLACK_KNIGHTS;
        }

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            long jumpingMoves = PieceMoveKnight.singleKnightCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }

        return moves;
    }

    static List<Move> masterKnightPushes(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes){
        long ans = 0, knights;
        List<Move> moves = new ArrayList<>();
        if (white){
            knights = board.WHITE_KNIGHTS;
        }
        else {
            knights = board.BLACK_KNIGHTS;
        }

        List<Long> allUnpinnedKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allUnpinnedKnights){
            long jumpingMoves = PieceMoveKnight.singleKnightPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(jumpingMoves, indexOfPiece));
        }
        return moves;
    }

}
