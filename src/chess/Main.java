package chess;

import bitboards.Knight;
import check.CheckUtilities;
import moveGeneration.MoveGeneratorMaster;

import java.util.List;

public class Main {

    /*
    check for enemy pieces before calling generateMove functions
    create true separation of push gen and cap gen

    make and unmake move

    enpassant

    clean up of redundant functions
     */

    public Chessboard board = new Chessboard();

    public static void main(String[] args) {
        new Main();
    }



    Main(){

        System.out.println(Art.boardArt(board));

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, true);
        System.out.println();
        System.out.println("Moves from this position: ");
        System.out.println(moves);
        System.out.println(moves.size());


        long[] knightMoveTable = Knight.KNIGHT_MOVE_TABLE;



//        Move move = moves.get(0);
//        MoveOrganiser.makeMoveMaster(board, move);


//        String s = Art.boardArt(board);
//        System.out.println(Art.boardArt(board));

    }


}
