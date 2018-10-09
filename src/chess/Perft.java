package chess;

import moveGeneration.MoveGeneratorMaster;
import moveMaking.MoveOrganiser;

import java.util.List;

public class Perft {

/*
20
400
8902
197281
4865609
 */


    public static void main(String[] args) {
        new Perft();
    }

    Perft(){
        plop();
    }

    void plop(){

        Chessboard board = new Chessboard();

        String s = Art.boardArt(board);
        System.out.println(s);
        System.out.println("-----------------------------------");

        int maxD = 4;
        for (int depth = 1; depth <= maxD; depth++) {
            countFinalNodesAtDepth(board, depth);
            System.out.println();
//            countTotalNodesAtDepth(board, depth);
            System.out.println("-----");
        }
    }



    public static void countTotalNodesAtDepth(Chessboard board, int depth){
        long i = Perft.countMovesAtDepthHelper(board, depth) - 1;
        System.out.println("Total Nodes at Depth " + depth + ": " + i);
    }

    public static void countFinalNodesAtDepth(Chessboard board, int depth) {
        numberOfCaptures = 0;

        long ii = Perft.countFinalNodesAtDepthHelper(board, depth);
        System.out.println("Final Nodes at Depth " + depth + ": " + ii);
        System.out.println("--previous checks: " + MoveGeneratorMaster.numberOfChecks);
        System.out.println("--checkmates: " + MoveGeneratorMaster.numberOfCheckMates);
        System.out.println("--stalemates: " + MoveGeneratorMaster.numberOfStaleMates);
        System.out.println("--captures: " + numberOfCaptures);



    }




    public static int numberOfCaptures = 0;

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;

        if (depth == 0){
            return 1;
        }

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        if (depth == 1){
            return moves.size();
        }

        for (Move move : moves) {
            Chessboard babyBoard = Copier.chessboardCopier(board, board.isWhiteTurn(), false);

            long enemies = babyBoard.isWhiteTurn() ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

            int destination = move.destination;
            long destSquare = BitManipulations.newPieceOnSquare(destination);

//            Art.printLong(enemies);
//            Art.printLong(destSquare);
//            System.out.println("xxx");
            boolean b = (destSquare & enemies) != 0;
            if (b) {
                numberOfCaptures++;
            }

            MoveOrganiser.makeMoveMaster(babyBoard, move);
            babyBoard.setWhiteTurn(!board.isWhiteTurn());

            long movesAtDepth = countFinalNodesAtDepthHelper(babyBoard, depth - 1);
            temp += movesAtDepth;
        }

        return temp;
    }


    private static long countMovesAtDepthHelper(Chessboard board, int depth){
        long temp = 1;

        if (depth == 0){
            return 1;
        }

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        for (Move move : moves) {
            Chessboard babyBoard = Copier.chessboardCopier(board, board.isWhiteTurn(), false);
            MoveOrganiser.makeMoveMaster(babyBoard, move);

            babyBoard.setWhiteTurn(!board.isWhiteTurn());

            long movesAtDepth = countMovesAtDepthHelper(babyBoard, depth - 1);

            temp += movesAtDepth;
        }

        return temp ;
    }

}
