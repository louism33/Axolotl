package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;

import java.util.List;

public final class UCIBoardParser {

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
        for (int i = 0; i < moves.size(); i++) {
            GenericMove genericMove = moves.get(i);
            board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMove.toString()));
        }
        return board;
    }

    public static int[] convertGenericMovesToMyMoves(Chessboard board, List<GenericMove> genMoves) {
        int size = genMoves.size();
        int[] moves = new int[size + 1];
        for (int i = 0; i < size; i++) {
            moves[i] = MoveParserFromAN.buildMoveFromLAN(board, genMoves.get(i).toString());
        }

        moves[size] = size;
        
        return moves;
    }
}




















