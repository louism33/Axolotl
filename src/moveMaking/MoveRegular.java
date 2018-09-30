package moveMaking;

import chess.BitManipulations;
import chess.Chessboard;
import chess.Move;

public class MoveRegular {

    static void makeRegularMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);

        if ((sourcePiece & board.WHITE_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_PAWNS |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_KNIGHTS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_KNIGHTS |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_BISHOPS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_BISHOPS |= destinationPiece;
        }
        else if  ((sourcePiece & board.WHITE_ROOKS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_ROOKS |= destinationPiece;
        }
        else if  ((sourcePiece & board.WHITE_QUEEN) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_QUEEN |= destinationPiece;
        }
        else if  ((sourcePiece & board.WHITE_KING) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_KING |= destinationPiece;
        }

        else if  ((sourcePiece & board.BLACK_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_PAWNS |= destinationPiece;
        }
        else if  ((sourcePiece & board.BLACK_KNIGHTS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_KNIGHTS |= destinationPiece;
        }
        else if  ((sourcePiece & board.BLACK_BISHOPS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_BISHOPS |= destinationPiece;
        }
        else if  ((sourcePiece & board.BLACK_ROOKS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_ROOKS |= destinationPiece;
        }
        else if  ((sourcePiece & board.BLACK_QUEEN) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_QUEEN |= destinationPiece;
        }
        else if  ((sourcePiece & board.BLACK_KING) != 0) {
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_KING |= destinationPiece;
        }
        else {
            throw new RuntimeException("false move");
        }
    }
}
