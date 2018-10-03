package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class MoveGeneratorPawns {

    public static List<Move> masterPawnPushes(Chessboard board, boolean white, long legalPushes){
        return masterMovePawns(board, white, legalPushes, 0);
    }


    public static List<Move> masterPawnCaptures(Chessboard board, boolean white, long legalCaptures){
        return masterMovePawns(board, white, 0, legalCaptures);
    }

    public static List<Move> masterMovePawns(Chessboard board, boolean white, long legalPushes, long legalCaptures){
        long ans = 0, pawns;
        List<Move> moves = new ArrayList<>();
        if (white){
            pawns = board.WHITE_PAWNS;
        }
        else {
            pawns = board.BLACK_PAWNS;
        }

        List<Long> allPawns = getAllPieces(pawns);
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
