package moveGeneration;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class MoveGeneratorPawns {

    public static List<Move> masterMovePawns(Chessboard board, boolean white){
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
            long pawnMoves = PieceMovePawns.pawnPushes(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece));
        }

        for (Long piece : allPawns){
            long pawnMoves = PieceMovePawns.pawnCaptures(board, piece, white);
            int indexOfPiece = BitIndexing.getIndexOfFirstPiece(piece);
            moves.addAll(MoveGenerationUtilities.movesFromAttackBoard(pawnMoves, indexOfPiece));
        }
        return moves;
    }



}
