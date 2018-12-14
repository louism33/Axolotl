package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.util.List;

class UCIBoardParser {
    /*
    position startpos moves b1c3 d7d5 g1f3 d5d4 c3b5 b8c6 e2e3 e7e5 e3d4 e5d4 d1e2 c8e6 e1d1 a7a6 b5c7 d8c7 f3g5 e8d7 g5e6 f7e6 e2e1 f8d6 f1d3 g8f6 e1g1 c6b4
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     */

    static GenericColor convertMyColourToGenericColour(boolean white) {
        if (white){
            return GenericColor.WHITE;
        }
        else {
            return GenericColor.BLACK;
        }
    }
    
    static GenericMove convertMyMoveToGenericMove(int move){
        GenericMove genericMove = null;
        try {
            genericMove = new GenericMove(MoveParser.toString(move));
        } catch (IllegalNotationException e) {
            System.out.println("Problem with: " +move);
            e.printStackTrace();
        }
        return genericMove;
    }

    static Chessboard convertGenericBoardToChessboard(GenericBoard genericBoard, List<GenericMove> moves){
        if (genericBoard == null || moves == null){
            return null;
        }
        Chessboard board = new Chessboard(genericBoard.toString());
        for (GenericMove genericMove : moves){

            board.makeMoveAndFlipTurn(MoveParser.newMove(board, genericMove.toString()));
            System.out.println(genericMove);
            System.out.println(board);
        }
        return board;
    }

}




















