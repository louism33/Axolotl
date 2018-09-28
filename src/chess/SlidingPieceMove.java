package chess;

import bitboards.BitBoards;
import bitboards.Rook;

import java.util.ArrayList;
import java.util.List;

public class SlidingPieceMove {

    long attackTable(Chessboard board, long pieces, boolean white){
        long ans = 0;
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(pieces);
        for (int i : indexOfAllPieces){
            long slidingMoves = rookSlidingMove(board, pieces, i, white); //todo only rook
            ans |= slidingMoves;
        }
        return ans;
    }

    List<Move> moveListForPieces (Chessboard board, long pieces, boolean white){
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(pieces);
        List<Move> moves = new ArrayList<>();
        for (int i : indexOfAllPieces){
            long slidingMoves = rookSlidingMove(board, pieces, i, white);
            moves.addAll(movesFromAttackBoard(slidingMoves, i));
        }

        return moves;
    }

    long bishopSlidingMove(Chessboard board, long pieces, int indexOfPiece, boolean white){
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long answer = 0;
        long temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.FILE_A) != 0) break;
            temp <<= 9;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }

        while (true) {
            if ((temp & BitBoards.FILE_A) != 0) break;
            temp >>>= 7;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }




        temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.FILE_H) != 0) break;
            temp <<= 7;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return answer & emptyOfMyPieces;
    }



    long rookSlidingMove(Chessboard board, long pieces, int indexOfPiece, boolean white){
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long answer = 0;
        long temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.FILE_A) != 0) break;
            temp <<= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.FILE_H) != 0) break;
            temp >>>= 1;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.RANK_EIGHT) != 0) break;
            temp <<= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = BitManipulations.newPieceOnSquare(indexOfPiece);
        while (true) {
            if ((temp & BitBoards.RANK_ONE) != 0) break;
            temp >>>= 8;
            answer |= temp;
            if ((temp & allPieces) != 0) break;
        }
        long emptyOfMyPieces = ~((white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES());
        return answer & emptyOfMyPieces;
    }

    List<Move> movesFromAttackBoard (long attackBoard, int source) {
        List<Move> moves = new ArrayList<>();
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(attackBoard);
        for (int i : indexOfAllPieces){
            moves.add(new Move(source, i));
        }
        return moves;
    }


    long rookSlidingMoveWithTable(Chessboard board){
        long allPieces = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long white_rooks = board.WHITE_ROOKS;
        int indexOfFirstPiece = BitIndexing.getIndexOfFirstPiece(white_rooks);
        long[] rookMoveTable = Rook.ROOK_MOVE_TABLE;
        long mask = rookMoveTable[indexOfFirstPiece];
        Art.printLong(mask);
        return 0;
    }

}
