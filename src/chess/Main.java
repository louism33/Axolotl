package chess;

import moveGeneration.MoveGeneratorMaster;

import java.util.List;

public class Main {


    public Chessboard board = new Chessboard();

    public static void main(String[] args) {
        new Main();
    }






    Main(){

        plop(board);

        System.out.println(Art.boardArt(board));

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, true);

        System.out.println(moves);
        System.out.println(moves.size());

//        Move move = moves.get(0);
//        System.out.println(move);
////        MoveOrganiser.makeMoveMaster(board, move);
//        String s = Art.boardArt(board);
//        System.out.println(Art.boardArt(board));
//
//        List<Move> movesW = CheckMoveOrganiser.kingLegalMovesOnly(board, true);
//        System.out.println(movesW);
//

//        String ss = Art.boardArt(board);
//        System.out.println(ss);
    }

    void plop(Chessboard board){


    }

}
