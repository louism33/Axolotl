package moveGeneration;

import check.CheckChecker;
import check.CheckMoveOrganiser;
import chess.Chessboard;
import chess.Move;

import java.util.List;

public class MoveGeneratorMaster {

    public static List<Move> generateLegalMoves(Chessboard board, boolean whiteTurn) {
        if (CheckChecker.boardInCheck(board, whiteTurn)){
            return CheckMoveOrganiser.evadeCheckMovesMaster(board, whiteTurn);
        }



        //todo
        // still allows moves that leave me in check
        return MoveGeneratorPseudo.generatePseudoMoves(board, whiteTurn);
    }







}
