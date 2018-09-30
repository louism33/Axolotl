package moveGeneration;

import bitboards.BitBoards;
import chess.BitExtractor;
import chess.Chessboard;

import java.util.List;

class PieceMoveSliding {

    static long bishopSlidingMove(Chessboard board, long piece, boolean white){
        long ALL_PIECES = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES(),
                NORTH_WEST = BitBoards.FILE_A | BitBoards.RANK_EIGHT,
                NORTH_EAST = BitBoards.FILE_H | BitBoards.RANK_EIGHT,
                SOUTH_WEST = BitBoards.FILE_A | BitBoards.RANK_ONE,
                SOUTH_EAST = BitBoards.FILE_H | BitBoards.RANK_ONE;
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & NORTH_WEST) != 0) break;
            temp <<= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & NORTH_EAST) != 0) break;
            temp <<= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            temp >>>= 7;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & SOUTH_WEST) != 0) break;
            temp >>>= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return answer & emptyOfMyPieces;
    }

    static long rookSlidingMove(Chessboard board, long piece, boolean white){
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long answer = 0;
        long temp = piece;
        while (true) {
            if ((temp & BitBoards.FILE_A) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitBoards.FILE_H) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitBoards.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        temp = piece;
        while (true) {
            if ((temp & BitBoards.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return answer & emptyOfMyPieces;
    }

    static long queenSlidingMove(Chessboard board, long piece, boolean white){
        return bishopSlidingMove(board, piece, white) | rookSlidingMove(board, piece, white);
    }


    static long masterAttackTable(Chessboard board, boolean white){
        long ans = 0, bishops, rooks, queens;
        if (white){
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
        }
        else {
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
        }

        List<Long> allBishops = BitExtractor.getAllPieces(bishops);
        for (Long piece : allBishops){
            long slidingMoves = bishopSlidingMove(board, piece, white);
            ans |= slidingMoves;
        }

        List<Long> allRooks = BitExtractor.getAllPieces(rooks);
        for (Long piece : allRooks){
            long slidingMoves = rookSlidingMove(board, piece, white);
            ans |= slidingMoves;
        }

        List<Long> allQueens = BitExtractor.getAllPieces(queens);
        for (Long piece : allQueens){
            long slidingMoves = queenSlidingMove(board, piece, white);
            ans |= slidingMoves;
        }
        return ans;
    }
}
