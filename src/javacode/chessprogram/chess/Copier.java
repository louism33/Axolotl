package javacode.chessprogram.chess;

import javacode.chessprogram.moveMaking.MoveParser;
import javacode.chessprogram.moveMaking.StackMoveData;

import java.util.Stack;

public class Copier {

    public static Chessboard copyBoard(Chessboard board, boolean white, boolean ignoreMyKing){
        Chessboard newBoard = new Chessboard();
        
        newBoard.moveStack = (Stack< StackMoveData >) board.moveStack.clone();
        
        newBoard.WHITE_PAWNS = board.WHITE_PAWNS;
        newBoard.WHITE_KNIGHTS = board.WHITE_KNIGHTS;
        newBoard.WHITE_BISHOPS = board.WHITE_BISHOPS;
        newBoard.WHITE_ROOKS = board.WHITE_ROOKS;
        newBoard.WHITE_QUEEN = board.WHITE_QUEEN;
        newBoard.WHITE_KING = board.WHITE_KING;

        newBoard.BLACK_PAWNS = board.BLACK_PAWNS;
        newBoard.BLACK_KNIGHTS = board.BLACK_KNIGHTS;
        newBoard.BLACK_BISHOPS = board.BLACK_BISHOPS;
        newBoard.BLACK_ROOKS = board.BLACK_ROOKS;
        newBoard.BLACK_QUEEN = board.BLACK_QUEEN;
        newBoard.BLACK_KING = board.BLACK_KING;

        if (ignoreMyKing && white) {
            newBoard.WHITE_KING = 0;
        }
        if (ignoreMyKing && !white) {
            newBoard.BLACK_KING = 0;
        }

        newBoard.whiteCanCastleK = board.whiteCanCastleK;
        newBoard.blackCanCastleK = board.blackCanCastleK;
        newBoard.whiteCanCastleQ = board.whiteCanCastleQ;
        newBoard.blackCanCastleQ = board.blackCanCastleQ;

        newBoard.setWhiteTurn(board.isWhiteTurn());

        return newBoard;
    }


    public static Move copyMove (Move move) {
        Move copyMove = new Move(move.getSourceAsPieceIndex(), move.destinationIndex);

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
