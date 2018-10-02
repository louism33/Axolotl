package check;

import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGeneratorPseudo;

import java.util.ArrayList;
import java.util.List;

public class CheckMoveOrganiser {

    public static List<Move> evadeCheckMovesMaster(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        if (CheckChecker.boardInDoubleCheck(board, white)) {
            return KingLegalMoves.kingLegalMovesOnly(board, white);
        }

        return allLegalCheckEscapeMoves(board, white);
    }


    static List<Move> allLegalCheckEscapeMoves(Chessboard board, boolean white) {
        List<Move> moves = new ArrayList<>();

//        List<Move> kingLegalMoves = KingLegalMoves.kingLegalMovesOnly(board, white);
//        moves.addAll(kingLegalMoves);


        //todo
        long blockingSquaresMask = 0;
        long checkingPieceMask = 0;

        MoveGeneratorPseudo.generatePseudoMoves(board, white, blockingSquaresMask, checkingPieceMask);



        return moves;
    }



}
