package chess;

import java.util.List;

public class Main {


    public Chessboard board = new Chessboard();

    public static void main(String[] args) {
        new Main();
    }

    Main(){

        SlidingPieceMove s = new SlidingPieceMove();

        long l = s.attackTable(board, board.WHITE_ROOKS, true);
        List<Move> moves = s.moveListForPieces(board, board.WHITE_ROOKS, true);

        System.out.println("-----moves:");
        Art.printLong(l);

        System.out.println(moves);
    }

    void plop (){



    }


}
