package main.chess;

import main.bitboards.King;
import main.bitboards.Knight;
import main.bitboards.PawnCaptures;
import main.moveGeneration.MoveGeneratorMaster;
import main.moveMaking.MoveOrganiser;
import main.moveMaking.MoveUnmaker;
import main.moveMaking.StackMoveData;
import main.utils.RandomBoard;

import java.util.List;

public class Main {

    /*
    create true separation of push gen and cap gen

    clean up of redundant functions
     */


    public static void main(String[] args) {
        new Main();
    }




    static int num = 11;
    static Chessboard[] bs = new Chessboard[num];

    Main(){

        Chessboard board = new Chessboard();
        System.out.println(Art.boardArt(board));


        moveNStuff(board, 7);

        moveNStuff(board, 8);

        moveNStuff(board, 6);

        moveNStuff(board, 7);
        
        Chessboard copyBoard = Copier.copyBoard(board, board.isWhiteTurn(), false);

        moveNStuff(board, 0);

        
        MoveUnmaker.unMakeMoveMaster(board);

        System.out.println("--------------------------------");
        System.out.println(board.equals(copyBoard));
        System.out.println("--------------------------------");

        System.out.println(Art.boardArt(copyBoard));
        
        System.out.println(Art.boardArt(board));
        System.out.println(board.moveStack.peek());
    }




    static void moveNStuff (Chessboard board, int i){
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
      
        Move move = moves.get(i);
        System.out.println(moves +" " + moves.size()) ;
        System.out.println(move);
        MoveOrganiser.makeMoveMaster(board, move);
        MoveOrganiser.flipTurn(board);
        System.out.println(Art.boardArt(board));
        StackMoveData peek = board.moveStack.peek();
        System.out.println(peek);
        System.out.println(peek.board.isWhiteTurn());
    }

}



