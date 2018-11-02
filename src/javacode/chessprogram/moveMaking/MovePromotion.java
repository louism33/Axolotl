package javacode.chessprogram.moveMaking;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessprogram.chess.BitManipulations.*;

class MovePromotion {

    static void makePromotingMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceAsPieceIndex());
        long destinationPiece = newPieceOnSquare(move.destinationIndex);

        if ((sourcePiece & board.WHITE_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.KNIGHT_PROMOTION_MASK){
                board.WHITE_KNIGHTS |= destinationPiece;
            }
            else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.BISHOP_PROMOTION_MASK){
                board.WHITE_BISHOPS |= destinationPiece;
            }
            else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.ROOK_PROMOTION_MASK){
                board.WHITE_ROOKS |= destinationPiece;
            }
            else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.QUEEN_PROMOTION_MASK){
                board.WHITE_QUEEN |= destinationPiece;
            }
        }

        else if ((sourcePiece & board.BLACK_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.KNIGHT_PROMOTION_MASK){
                board.BLACK_KNIGHTS |= destinationPiece;
            }
            else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.BISHOP_PROMOTION_MASK){
                board.BLACK_BISHOPS |= destinationPiece;
            }
            else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.ROOK_PROMOTION_MASK){
                board.BLACK_ROOKS |= destinationPiece;
            }
            else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.QUEEN_PROMOTION_MASK){
                board.BLACK_QUEEN |= destinationPiece;
            }
        }

    }
}
