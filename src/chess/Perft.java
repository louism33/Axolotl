package chess;

import moveGeneration.MoveGeneratorMaster;
import moveMaking.MoveOrganiser;

import java.util.List;

public class Perft {

    public static void countMovesAtDepth(Chessboard board, int depth){
        int i = Perft.countMovesAtDepthHelper(board, depth) - 1;
        System.out.println("Total Nodes at Depth " + depth + ": ");
        System.out.println(i);
    }

    public static void countFinalNodesAtDepth(Chessboard board, int depth) {
        int ii = Perft.countFinalNodesAtDepthHelper(board, depth);
        System.out.println("Final Nodes at Depth " + depth + ": ");
        System.out.println(ii);
    }




    private static int countFinalNodesAtDepthHelper(Chessboard board, int depth){
        int temp = 0;

        if (depth == 0){
            return 1;
        }

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        for (Move move : moves) {
            Chessboard babyBoard = Copier.chessboardCopier(board, board.isWhiteTurn(), false);
            MoveOrganiser.makeMoveMaster(babyBoard, move);
            babyBoard.setWhiteTurn(!board.isWhiteTurn());
            int movesAtDepth = countFinalNodesAtDepthHelper(babyBoard, depth - 1);
            temp += movesAtDepth;
        }

        return temp;
    }


    private static int countMovesAtDepthHelper(Chessboard board, int depth){
        int temp = 1;

        if (depth == 0){
            return 1;
        }

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());

        for (Move move : moves) {
            Chessboard babyBoard = Copier.chessboardCopier(board, board.isWhiteTurn(), false);
            MoveOrganiser.makeMoveMaster(babyBoard, move);

            babyBoard.setWhiteTurn(!board.isWhiteTurn());

            int movesAtDepth = countMovesAtDepthHelper(babyBoard, depth - 1);

//            System.out.println("--- " + movesAtDepth);

            temp += movesAtDepth;
        }

        return temp ;
    }

}
