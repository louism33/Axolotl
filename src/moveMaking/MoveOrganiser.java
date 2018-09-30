package moveMaking;

import chess.Chessboard;
import chess.Move;

public class MoveOrganiser {

    public static void moveOrganiserMaster(Chessboard board, Move move) {
        if (MoveParser.isSpecialMove(move)){
            if (MoveParser.isCastlingMove(move)) {
                MoveCastling.makeCastlingMove(board, move);
            }
            else if (MoveParser.isEnPassantMove(move)){
                MoveEnPassant.makeEnPassantMove(board, move);
            }
            else if (MoveParser.isPromotionMove(move)){
                MovePromotion.makePromotingMove(board, move);
            }
        }
        else {
            MoveRegular.makeRegularMove(board, move);
        }
    }

}
