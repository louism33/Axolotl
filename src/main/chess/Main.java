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
import java.util.Random;

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


        Chessboard copy1 = Copier.copyBoard(board, board.isWhiteTurn(), false);


        moveNStuff(board, 13);
        moveNStuff(board, 12);
        moveNStuff(board, 22);
        moveNStuff(board, 7);
        
        
        
        moveNStuff(board, 0);
//        moveNStuff(board, 4);
//        moveNStuff(board, 10);
//        moveNStuff(board, 17);



        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        unMoveNStuff(board);

        System.out.println("Same as before ? " + board.equals(copy1));
        System.out.println();
        

    }

    static void unMoveNStuff(Chessboard board){
        int size = board.moveStack.size();
        for (int undo = 0; undo < size; undo++) {
            MoveUnmaker.unMakeMoveMaster(board);
//            System.out.println(Art.boardArt(board));
        }
    }

  

    static void moveNStuffRandom (Chessboard board, int totalRandoms){
        for (int r = 0; r < totalRandoms; r++) {
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
            Random rand = new Random();
                    
            Move move = moves.get(rand.nextInt(moves.size()));
            
            System.out.println(moves + " " + moves.size());
            System.out.println(move);
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            System.out.println(Art.boardArt(board));
            StackMoveData peek = board.moveStack.peek();
            System.out.println(peek);
//            System.out.println("Peek turn white : " + peek.whiteTurn);
        }
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
//        System.out.println("Peek turn white : " + peek.whiteTurn);
    }




    void castlingTest(){
        Chessboard board = new Chessboard();
        System.out.println(Art.boardArt(board));


        Chessboard copy1 = Copier.copyBoard(board, board.isWhiteTurn(), false);


        moveNStuff(board, 13);
        moveNStuff(board, 12);
        moveNStuff(board, 5);
        moveNStuff(board, 7);
        moveNStuff(board, 10);
        moveNStuff(board, 4);

        //castling
        moveNStuff(board, 0);
        moveNStuff(board, 0);

        moveNStuff(board, 7);
        moveNStuff(board, 12);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        unMoveNStuff(board);

        System.out.println("Same as before ? " + board.equals(copy1));
        System.out.println();
    }
}



