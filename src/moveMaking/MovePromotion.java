package moveMaking;

import chess.BitManipulations;
import chess.Chessboard;
import chess.Move;

class MovePromotion {

    static void makePromotingMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);


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
