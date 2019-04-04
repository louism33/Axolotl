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

    static int convertGenericBoardToChessboard(Chessboard board, GenericBoard genericBoard,
                                                      List<GenericMove> moves, int lastMoveMade){
        
        if (genericBoard == null || moves == null || moves.size() == 0){
            return 0;
        }
        
        if (!genericBoard.toString().equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")) {
            board = new Chessboard(genericBoard.toString());
            lastMoveMade = 0;
        }

        for (int i = lastMoveMade; i < moves.size(); i++) {
            GenericMove genericMove = moves.get(i);
            board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMove.toString()));
        }

        return moves.size();
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




















