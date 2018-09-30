package chess;

import moveGeneration.MoveGeneratorMaster;
import moveMaking.MoveOrganiser;

import java.util.List;

public class Main {


    public Chessboard board = new Chessboard();

    public static void main(String[] args) {
        new Main();
    }






    Main(){

        List<Move> moves = MoveGeneratorMaster.generateMoves(board, true);

        for (int m = 0; m < moves.size(); m++){
            System.out.println(moves.get(m));
        }

        Move move = moves.get(0);
        System.out.println(move);
        MoveOrganiser.moveOrganiserMaster(board, move);



        String s = Art.boardArt(board);
        System.out.println(s);
    }


}
