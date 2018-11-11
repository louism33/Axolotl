package javacode.main;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.graphicsandui.Art;

import java.util.List;

import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.chessprogram.moveMaking.MoveOrganiser.flipTurn;
import static javacode.chessprogram.moveMaking.MoveOrganiser.makeMoveMaster;

class UCIBoardParser {
    
    public static GenericMove convertMyMoveToGenericMove(Move move){
        GenericMove genericMove = null;
        try {
            genericMove = new GenericMove(move.toString());
        } catch (IllegalNotationException e) {
            System.out.println("Problem with :" +move);
            e.printStackTrace();
        }
        return genericMove;
    }

    static Chessboard convertGenericBoardToChessboard(GenericBoard genericBoard, List<GenericMove> moves){
        Chessboard board = FenParser.makeBoardBasedOnFEN(genericBoard.toString());
//        System.out.println(Art.boardArt(board));
//        System.out.println("Now making moves: "+ moves);
        for (GenericMove genericMove : moves){
            makeMoveMaster(board, moveFromGenericMove(board, genericMove));
            flipTurn(board);
        }
        System.out.println(Art.boardArt(board));
        
        return board;
    }

    static Chessboard convertGenericBoardToChessboardDelta(Chessboard board, List<GenericMove> moves){
        System.out.println(Art.boardArt(board));

        for (int i = moves.size() - 2; i < moves.size(); i++) {
            GenericMove genericMove = moves.get(i);
            makeMoveMaster(board, moveFromGenericMove(board, genericMove));
            flipTurn(board);
        }
        return board;
    }
    
    //avoid move gen if possible
    private static Move moveFromGenericMove(Chessboard board, GenericMove genericMove){
        String s = genericMove.toString();
        List<Move> moves = generateLegalMoves(board, board.isWhiteTurn());
        Move move = null;
        
        for (Move aMoveArray : moves) {
            if (s.equals(aMoveArray.toString())) {
                move = aMoveArray;
                break;
            }
        }
        return move;
    }
}




















