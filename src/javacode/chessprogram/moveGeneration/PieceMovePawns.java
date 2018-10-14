package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.bitboards.PawnCaptures;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessprogram.chess.BitExtractor.getAllPieces;

public class PieceMovePawns {

    static long singlePawnPushes(Chessboard board, long piece, boolean white, long legalPushes) {
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long HOME_RANK = (white) ? BitBoards.RANK_TWO : BitBoards.RANK_SEVEN;
        long answer = 0;
        long temp = piece;

        // promotion moves are handled elsewhere
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
        return answer & legalPushes;
    }

    public static long singlePawnCaptures(Chessboard board, long piece, boolean white, long legalCaptures) {
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long answer = 0;

        if (piece == 0) {
            return 0;
        }
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

        return answer & legalCaptures;
    }

    static long masterPawnCapturesTable(Chessboard board, boolean white,
                                               long ignoreThesePieces, long legalCaptures){
        long ans = 0, pawns;
        if (white){
            pawns = board.WHITE_PAWNS;
        }
        else {
            pawns = board.BLACK_PAWNS;
        }
        List<Long> allPawns = getAllPieces(pawns, ignoreThesePieces);
        for (Long piece : allPawns){
            ans |= singlePawnCaptures(board, piece, white, legalCaptures);
        }
        return ans;
    }

}
