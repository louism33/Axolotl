package moveGeneration;

import bitboards.King;
import bitboards.Knight;
import chess.BitExtractor;
import chess.BitIndexing;
import chess.Chessboard;

import java.util.List;

class PieceMoveJumping {

    static long knightMove(Chessboard board, long piece, boolean white){
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);
        long l = Knight.KNIGHT_MOVE_TABLE[index];
        table |= l;
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return table & emptyOfMyPieces;
    }

    static long kingMove(Chessboard board, long piece, boolean white){
        long table = 0;
        int index = BitIndexing.getIndexOfFirstPiece(piece);

        long l1 = King.KING_MOVE_TABLE[index];
        table |= l1;
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return table & emptyOfMyPieces;
    }

    static long masterAttackTable(Chessboard board, boolean white){
        long ans = 0, knights, king;
        if (white){
            knights = board.WHITE_KNIGHTS;
            king = board.WHITE_KING;
        }
        else {
            knights = board.BLACK_KNIGHTS;
            king = board.BLACK_KING;
        }

        List<Long> allKnights = BitExtractor.getAllPieces(knights);
        for (Long piece : allKnights){
            long jumpingMoves = knightMove(board, piece, white);
            ans |= jumpingMoves;
        }

        List<Long> allKings = BitExtractor.getAllPieces(king);
        for (Long piece : allKings){
            long jumpingMoves = kingMove(board, piece, white);
            ans |= jumpingMoves;
        }


        return ans;
    }
}
