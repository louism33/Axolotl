package moveGeneration;

import bitboards.BitBoards;
import bitboards.PawnCaptures;
import chess.BitIndexing;
import chess.Chessboard;

import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class PieceMovePawns {

    public static long pawnPushes(Chessboard board, long piece, boolean white) {
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long HOME_RANK = (white) ? BitBoards.RANK_TWO : BitBoards.RANK_SEVEN;
        long answer = 0;
        long temp = piece;

        if (white) {
            do {
                if ((temp & BitBoards.RANK_EIGHT) != 0) break;
                temp <<= 8;
                if ((temp & allPieces) != 0) break;
                answer |= temp;
            } while (((temp & BitBoards.RANK_THREE) != 0));
        }
        else {
            do {
                if ((temp & BitBoards.RANK_ONE) != 0) break;
                temp >>>= 8;
                if ((temp & allPieces) != 0) break;
                answer |= temp;
            } while (((temp & BitBoards.RANK_SIX) != 0));
        }
        return answer;
    }

    public static long pawnCaptures(Chessboard board, long piece, boolean white) {
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long answer = 0;
        long temp = piece;

        if (white){
            int index = BitIndexing.getIndexOfFirstPiece(piece);
            long l = PawnCaptures.PAWN_CAPTURE_TABLE_WHITE[index];
            answer |= l;
        }
        else{
            long table = 0;
            int index = BitIndexing.getIndexOfFirstPiece(piece);
            long l = PawnCaptures.PAWN_CAPTURE_TABLE_BLACK[index];
            answer |= l;
        }

        long enemyPieces = ((white) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES());
        return answer & enemyPieces;
    }

    public static long masterPawnPushesTable(Chessboard board, boolean white){
        long ans = 0, pawns;
        if (white){
            pawns = board.WHITE_PAWNS;
        }
        else {
            pawns = board.BLACK_PAWNS;
        }
        List<Long> allPawns = getAllPieces(pawns);
        for (Long piece : allPawns){
            ans |= pawnPushes(board, piece, white);

        }
        return ans;
    }

    public static long masterPawnCapturesTable(Chessboard board, boolean white){
        long ans = 0, pawns;
        if (white){
            pawns = board.WHITE_PAWNS;
        }
        else {
            pawns = board.BLACK_PAWNS;
        }
        List<Long> allPawns = getAllPieces(pawns);
        for (Long piece : allPawns){
            ans |= pawnCaptures(board, piece, white);
        }
        return ans;
    }

}
