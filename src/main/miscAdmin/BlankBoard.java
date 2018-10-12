package main.miscAdmin;

import main.chess.Chessboard;

public class BlankBoard {
    
    public static Chessboard blankBoard(){
        
        Chessboard board = new Chessboard();

        board.WHITE_PAWNS = 0;
        board.WHITE_KNIGHTS = 0;
        board.WHITE_BISHOPS = 0;
        board.WHITE_ROOKS = 0;
        board.WHITE_QUEEN = 0;
        board.WHITE_KING = 0;

        board.BLACK_PAWNS = 0;
        board.BLACK_KNIGHTS = 0;
        board.BLACK_BISHOPS = 0;
        board.BLACK_ROOKS = 0;
        board.BLACK_QUEEN = 0;
        board.BLACK_KING = 0;
        
        return board;
        
    }
}
