package main.moveGeneration;

import main.chess.BitIndexing;
import main.chess.Chessboard;
import main.chess.Move;

import java.util.ArrayList;
import java.util.List;

import static main.chess.BitExtractor.getAllPieces;

public class MoveGeneratorPawns {

    public static List<Move> masterPawnPushes(Chessboard board, boolean white,
                                              long ignoreThesePieces, long legalPushes){
        return masterMovePawns(board, white, ignoreThesePieces, legalPushes, 0);
    }


    public static List<Move> masterPawnCaptures(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalCaptures){
        return masterMovePawns(board, white, ignoreThesePieces, 0, legalCaptures);
    }

    public static List<Move> masterMovePawns(Chessboard board, boolean white,
                                             long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, pawns;
        List<Move> moves = new ArrayList<>();
        if (white){
            pawns = board.WHITE_PAWNS;
        }
        else {
            pawns = board.BLACK_PAWNS;
        }

        List<Long> allPawns = getAllPieces(pawns, ignoreThesePieces);
        for (Long piece : allPawns){
            long pawnMoves = PieceMovePawns.singlePawnPushes(board, piece, white, legalPushes);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece));
        }

        for (Long piece : allPawns){
            long pawnMoves = PieceMovePawns.singlePawnCaptures(board, piece, white, legalCaptures);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece));
        }
        return moves;
    }

}
