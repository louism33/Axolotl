package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;

import java.util.List;

class UCIBoardParser {

    static GenericColor convertMyColourToGenericColour(boolean white) {
        if (white){
            return GenericColor.WHITE;
        }
        else {
            return GenericColor.BLACK;
        }
    }
    
    public static GenericMove convertMyMoveToGenericMove(int move){
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
            board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMove.toString()));
        }
        return board;
    }

}




















