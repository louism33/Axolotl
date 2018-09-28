package chess;

import bitboards.WhichTable;

import java.util.List;

public class MoveGenerator {


    MoveGenerator(Chessboard board){




    }

    long allMovesPieces(Chessboard board, long[] pieces, boolean white){
        long table = 0;
        for (long piece : pieces) {
            long table1 = tableForPiece(board, piece);
            table |= table1;
        }
        return table;
    }


    long tableForPiece(Chessboard board, long pieces){
        long table = 0;
        List<Integer> allPieces = BitIndexing.getIndexOfAllPieces(pieces);
        for (Integer piece : allPieces) {
            long l1 = WhichTable.whichTable(board, pieces)[piece];
            table |= l1;
        }
        return table;
    }





    List<Integer> generateAllPseudoMoves(){





        return null;
    }

}
