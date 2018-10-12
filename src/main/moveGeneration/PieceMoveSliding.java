package main.moveGeneration;

import main.bitboards.BitBoards;
import main.chess.BitExtractor;
import main.chess.Chessboard;

import java.util.List;

public class PieceMoveSliding {

    public static long singleBishopPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleBishopAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleBishopCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleBishopAllMoves(board, piece, white, 0, legalCaptures);
    }

    private static long singleBishopAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
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
            if ((temp & SOUTH_EAST) != 0) break;
            temp >>>= 9;
            answer |= temp;
            if ((temp & ALL_PIECES) != 0) break;
        }
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
//        return answer & emptyOfMyPieces & (legalPushes | legalCaptures);
        return answer & (legalPushes | legalCaptures);
    }


    public static long singleRookPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleRookAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleRookCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleRookAllMoves(board, piece, white, 0, legalCaptures);
    }
    
    private static long singleRookAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
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
//        return answer & emptyOfMyPieces & (legalPushes | legalCaptures);
        return answer & (legalPushes | legalCaptures);
    }

    public static long singleQueenPushes(Chessboard board, long piece, boolean white, long legalPushes){
        return singleQueenAllMoves(board, piece, white, legalPushes, 0);
    }

    public static long singleQueenCaptures(Chessboard board, long piece, boolean white, long legalCaptures){
        return singleQueenAllMoves(board, piece, white, 0, legalCaptures);
    }

    private static long singleQueenAllMoves(Chessboard board, long piece, boolean white, long legalPushes, long legalCaptures){
        return singleBishopAllMoves(board, piece, white, legalPushes, legalCaptures) | singleRookAllMoves(board, piece, white, legalPushes, legalCaptures);
    }


    public static long masterAttackTableSliding(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
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

        List<Long> allBishops = BitExtractor.getAllPieces(bishops, ignoreThesePieces);
        for (Long piece : allBishops){
            ans |= singleBishopPushes(board, piece, white, legalPushes);
            ans |= singleBishopCaptures(board, piece, white, legalCaptures);
        }

        List<Long> allRooks = BitExtractor.getAllPieces(rooks, ignoreThesePieces);
        for (Long piece : allRooks){
            ans |= singleRookPushes(board, piece, white, legalPushes);
            ans |= singleRookCaptures(board, piece, white, legalCaptures);
        }

        List<Long> allQueens = BitExtractor.getAllPieces(queens, ignoreThesePieces);
        for (Long piece : allQueens){
            ans |= singleQueenPushes(board, piece, white, legalPushes);
            ans |= singleQueenCaptures(board, piece, white, legalCaptures);
        }
        return ans;
    }
}
