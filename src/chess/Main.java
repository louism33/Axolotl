package chess;

import bitboards.BitBoards;
import check.CheckMoveOrganiser;
import moveGeneration.MoveGeneratorMaster;
import moveMaking.MoveOrganiser;
import moveMaking.MoveParser;

import java.util.List;

public class Main {


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




        Move move = moves.get(0);
        MoveOrganiser.makeMoveMaster(board, move);


        String s = Art.boardArt(board);
        System.out.println(Art.boardArt(board));

    }


}
