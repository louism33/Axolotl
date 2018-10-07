package moveMaking;

import chess.Chessboard;
import chess.Move;

public class MoveMakingUtilities {

    static void removePieces (Chessboard board, long sourceSquare, long destinationSquare){
        long mask = ~(sourceSquare | destinationSquare);
        board.WHITE_PAWNS &= mask;
        board.WHITE_KNIGHTS &= mask;
        board.WHITE_BISHOPS &= mask;
        board.WHITE_ROOKS &= mask;
        board.WHITE_QUEEN &= mask;
        board.WHITE_KING &= mask;
        board.BLACK_PAWNS &= mask;
        board.BLACK_KNIGHTS &= mask;
        board.BLACK_BISHOPS &= mask;
        board.BLACK_ROOKS &= mask;
        board.BLACK_QUEEN &= mask;
        board.BLACK_KING &= mask;
    }

    public static Move copyMove (Move move) {
        Move copyMove = new Move(move.getSourceAsPiece(), move.destination);

        if ((move.move & MoveParser.SPECIAL_MOVE_MASK) != 0) {
            if ((move.move & MoveParser.SPECIAL_MOVE_MASK) == MoveParser.CASTLING_MASK) {
                copyMove.move |= MoveParser.CASTLING_MASK;
            } else if ((move.move & MoveParser.SPECIAL_MOVE_MASK) == MoveParser.ENPASSANT_MASK) {
                copyMove.move |= MoveParser.ENPASSANT_MASK;
            } else if ((move.move & MoveParser.SPECIAL_MOVE_MASK) == MoveParser.PROMOTION_MASK) {
                copyMove.move |= MoveParser.PROMOTION_MASK;

                if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.KNIGHT_PROMOTION_MASK) {
                    copyMove.move |= MoveParser.KNIGHT_PROMOTION_MASK;
                } else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.BISHOP_PROMOTION_MASK) {
                    copyMove.move |= MoveParser.BISHOP_PROMOTION_MASK;
                } else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.ROOK_PROMOTION_MASK) {
                    copyMove.move |= MoveParser.ROOK_PROMOTION_MASK;
                } else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.QUEEN_PROMOTION_MASK) {
                    copyMove.move |= MoveParser.QUEEN_PROMOTION_MASK;
                }
            }
        }
        return copyMove;
    }


}
