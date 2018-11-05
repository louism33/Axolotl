package javacode.chessprogram.miscAdmin;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.moveMaking.StackMoveData;

import java.util.Stack;

class BlankBoard {
    
    static Chessboard blankBoard(){
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

    static void printMoveStack(Chessboard board){
        int size = board.moveStack.size();
        Stack<StackMoveData> copyStack = (Stack<StackMoveData>) board.moveStack.clone();
        for (int m = 0; m < size; m++){
            System.out.println(copyStack.pop());
        }
    }
}
