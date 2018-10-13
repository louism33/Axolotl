package main.moveMaking;

import main.bitboards.BitBoards;
import main.chess.BitManipulations;
import main.chess.Chessboard;
import main.chess.Move;

class MoveCastling {

    static void makeCastlingMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        if ((sourcePiece & BitBoards.WHITE_KING) != 0){
            if (move.destination == 1){
                long originalRook = BitManipulations.newPieceOnSquare(0);
                long newRook = BitManipulations.newPieceOnSquare(move.destination + 1);
                long newKing = BitManipulations.newPieceOnSquare(move.destination);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.WHITE_KING |= newKing;
                board.WHITE_ROOKS |= newRook;
                board.whiteCanCastleK = board.whiteCanCastleQ = false;
            }
            else if (move.destination == 5){
                long originalRook = BitManipulations.newPieceOnSquare(7);
                long newRook = BitManipulations.newPieceOnSquare(move.destination - 1);
                long newKing = BitManipulations.newPieceOnSquare(move.destination);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.WHITE_KING |= newKing;
                board.WHITE_ROOKS |= newRook;
                board.whiteCanCastleK = board.whiteCanCastleQ = false;
            }
        }

        else if ((sourcePiece & BitBoards.BLACK_KING) != 0){
            if (move.destination == 57){
                long originalRook = BitManipulations.newPieceOnSquare(56);
                long newRook = BitManipulations.newPieceOnSquare(move.destination + 1);
                long newKing = BitManipulations.newPieceOnSquare(move.destination);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.BLACK_KING |= newKing;
                board.BLACK_ROOKS |= newRook;
                board.blackCanCastleK = board.blackCanCastleQ = false;
            }
            else if (move.destination == 61){
                long originalRook = BitManipulations.newPieceOnSquare(63);
                long newRook = BitManipulations.newPieceOnSquare(move.destination - 1);
                long newKing = BitManipulations.newPieceOnSquare(move.destination);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.BLACK_KING |= newKing;
                board.BLACK_ROOKS |= newRook;
                board.blackCanCastleK = board.blackCanCastleQ = false;
            }
        }
        else {
            throw new RuntimeException("Incorrect call to castling move");
        }
    }

    static void castleFlagManager (Chessboard board, Move move){

        // disable relevant castle flag whenever a piece moves into the relevant square.
        if (move.getSourceAsPiece() == 0 || move.destination == 0){
            board.whiteCanCastleK = false;
        }
        if (move.getSourceAsPiece() == 3 || move.destination == 3){
            board.whiteCanCastleK = false;
            board.whiteCanCastleQ = false;
        }
        if (move.getSourceAsPiece() == 7 || move.destination == 7){
            board.whiteCanCastleQ = false;
        }

        if (move.getSourceAsPiece() == 56 || move.destination == 56){
            board.blackCanCastleK = false;
        }
        if (move.getSourceAsPiece() == 59 || move.destination == 59){
            board.blackCanCastleK = false;
            board.blackCanCastleQ = false;
        }
        if (move.getSourceAsPiece() == 63 || move.destination == 63){
            board.blackCanCastleQ = false;
        }
    }
}
