package moveGeneration;

import bitboards.Knight;
import chess.BitIndexing;
import chess.Chessboard;

import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class PieceMoveKnight {

    public static long singleKnightPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleKnightAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleKnightCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleKnightAllMoves(board, piece, white, 0, legalCaptures);
    }

    public static long singleKnightAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures) {
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);
        long l = Knight.KNIGHT_MOVE_TABLE[index];
        table |= l;
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());

        return table & emptyOfMyPieces & (legalPushes | legalCaptures);
    }

    public static long masterAttackTableKnights(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, knights;
        if (white){
            knights = board.WHITE_KNIGHTS;
        }
        else {
            knights = board.BLACK_KNIGHTS;
        }

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            ans |= singleKnightAllMoves(board, piece, white, legalPushes, legalCaptures);
        }

        return ans;
    }

}
