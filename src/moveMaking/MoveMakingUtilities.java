package moveMaking;

import chess.Chessboard;

class MoveMakingUtilities {

    static void removePieces (Chessboard board, long sourceSquare, long destinationSquare){
        long mask = ~(sourceSquare | destinationSquare);
        board.WHITE_PAWNS &= mask;
        board.WHITE_KNIGHTS &= mask;
        board.WHITE_BISHOPS &= mask;
        board.WHITE_ROOKS &= mask;
        board.WHITE_QUEEN &= mask;
        board.WHITE_KING &= mask;
        board.BLACK_PAWNS &= mask;
        board.BLACK_KNIGHTS &= mask;
        board.BLACK_BISHOPS &= mask;
        board.BLACK_ROOKS &= mask;
        board.BLACK_QUEEN &= mask;
        board.BLACK_KING &= mask;
    }

}
