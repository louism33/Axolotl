package moveGeneration;

import bitboards.King;
import chess.BitIndexing;
import chess.Chessboard;

import java.util.List;

import static chess.BitExtractor.getAllPieces;

public class PieceMoveKing {


    public static long singleKingPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleKingAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleKingCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleKingAllMoves(board, piece, white, 0, legalCaptures);
    }

    public static long singleKingAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);

        long l1 = King.KING_MOVE_TABLE[index];
        table |= l1;
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return table & emptyOfMyPieces & (legalPushes | legalCaptures);
    }

    public static long masterAttackTableKing(Chessboard board, boolean white, long legalPushes, long legalCaptures){
        long ans = 0, king;
        if (white){
            king = board.WHITE_KING;
        }
        else {
            king = board.BLACK_KING;
        }

        List<Long> allKings = getAllPieces(king);
        for (Long piece : allKings){
            ans |= singleKingAllMoves(board, piece, white, legalPushes, legalCaptures);
        }
        return ans;
    }


}
