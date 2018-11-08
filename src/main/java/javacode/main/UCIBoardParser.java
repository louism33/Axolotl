package javacode.main;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.graphicsandui.Art;

import java.util.List;

public class UCIBoardParser {
    
    public static void main (String[] args){
        Chessboard board = new Chessboard();
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        convertMyMoveToGenericMove(moves.get(0));
    }
    
    static GenericMove convertMyMoveToGenericMove(Move move){
        GenericMove genericMove = null;
        try {
            genericMove = new GenericMove(move.toString());
        } catch (IllegalNotationException e) {
            e.printStackTrace();
        }
        return genericMove;
    }

    static Chessboard convertGenericBoardToChessboard(GenericBoard genericBoard){
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN(genericBoard.toString());
        System.out.println(Art.boardArt(chessboard));
        return chessboard;
    }
}




















