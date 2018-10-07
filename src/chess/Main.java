package chess;

import bitboards.BitBoards;
import check.CheckMoveOrganiser;
import moveGeneration.MoveGeneratorMaster;
import moveMaking.MoveOrganiser;
import moveMaking.MoveParser;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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


    void plop(){

        Perft.countFinalNodesAtDepth(board, 1);

        Perft.countFinalNodesAtDepth(board, 2);

        Perft.countFinalNodesAtDepth(board, 3);

        Perft.countFinalNodesAtDepth(board, 4);

//        Perft.countFinalNodesAtDepth(board, 5);
    }



    Main(){

        System.out.println(Art.boardArt(board));

        plop();

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, false);
        System.out.println();
        System.out.println("Moves from this position: ");
        System.out.println(moves);
        System.out.println(moves.size());



//        Move move = moves.get(0);
//        MoveOrganiser.makeMoveMaster(board, move);


//        String s = Art.boardArt(board);
//        System.out.println(Art.boardArt(board));

    }


}
