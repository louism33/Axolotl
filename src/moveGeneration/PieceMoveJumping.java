package moveGeneration;

import bitboards.King;
import bitboards.Knight;
import chess.BitIndexing;
import chess.Chessboard;

import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class PieceMoveJumping {

    public static long knightMove(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);
        long l = Knight.KNIGHT_MOVE_TABLE[index];
        table |= l;
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return table & emptyOfMyPieces & legalPushes & legalCaptures;
    }

    public static long kingMove(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);

        long l1 = King.KING_MOVE_TABLE[index];
        table |= l1;
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return table & emptyOfMyPieces & legalPushes & legalCaptures;
    }

    public static long masterAttackTableJumping(Chessboard board, boolean white, long legalPushes, long legalCaptures){
        long ans = 0, knights, king;
        if (white){
            knights = board.WHITE_KNIGHTS;
            king = board.WHITE_KING;
        }
        else {
            knights = board.BLACK_KNIGHTS;
            king = board.BLACK_KING;
        }

        List<Long> allKnights = getAllPieces(knights);
        for (Long piece : allKnights){
            ans |= knightMove(board, piece, white, legalPushes, legalCaptures);
        }

        List<Long> allKings = getAllPieces(king);
        for (Long piece : allKings){
            ans |= PieceMoveJumping.kingMove(board, piece, white, legalPushes, legalCaptures);
        }
        return ans;
    }

}
