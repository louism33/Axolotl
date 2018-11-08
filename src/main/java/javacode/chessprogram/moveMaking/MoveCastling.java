package javacode.chessprogram.moveMaking;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;

class MoveCastling {

    static void makeCastlingMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceAsPieceIndex());
        if ((sourcePiece & BitBoards.WHITE_KING) != 0){
            if (move.destinationIndex == 1){
                long originalRook = newPieceOnSquare(0);
                long newRook = newPieceOnSquare(move.destinationIndex + 1);
                long newKing = newPieceOnSquare(move.destinationIndex);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.WHITE_KING |= newKing;
                board.WHITE_ROOKS |= newRook;
                board.whiteCanCastleK = board.whiteCanCastleQ = false;
            }
            else if (move.destinationIndex == 5){
                long originalRook = newPieceOnSquare(7);
                long newRook = newPieceOnSquare(move.destinationIndex - 1);
                long newKing = newPieceOnSquare(move.destinationIndex);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.WHITE_KING |= newKing;
                board.WHITE_ROOKS |= newRook;
                board.whiteCanCastleK = board.whiteCanCastleQ = false;
            }
        }

        else if ((sourcePiece & BitBoards.BLACK_KING) != 0){
            if (move.destinationIndex == 57){
                long originalRook = newPieceOnSquare(56);
                long newRook = newPieceOnSquare(move.destinationIndex + 1);
                long newKing = newPieceOnSquare(move.destinationIndex);
                MoveMakingUtilities.removePieces(board, sourcePiece, originalRook);
                board.BLACK_KING |= newKing;
                board.BLACK_ROOKS |= newRook;
                board.blackCanCastleK = board.blackCanCastleQ = false;
            }
            else if (move.destinationIndex == 61){
                long originalRook = newPieceOnSquare(63);
                long newRook = newPieceOnSquare(move.destinationIndex - 1);
                long newKing = newPieceOnSquare(move.destinationIndex);
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
        if (move.getSourceAsPieceIndex() == 0 || move.destinationIndex == 0){
            board.whiteCanCastleK = false;
        }
        if (move.getSourceAsPieceIndex() == 3 || move.destinationIndex == 3){
            board.whiteCanCastleK = false;
            board.whiteCanCastleQ = false;
        }
        if (move.getSourceAsPieceIndex() == 7 || move.destinationIndex == 7){
            board.whiteCanCastleQ = false;
        }

        if (move.getSourceAsPieceIndex() == 56 || move.destinationIndex == 56){
            board.blackCanCastleK = false;
        }
        if (move.getSourceAsPieceIndex() == 59 || move.destinationIndex == 59){
            board.blackCanCastleK = false;
            board.blackCanCastleQ = false;
        }
        if (move.getSourceAsPieceIndex() == 63 || move.destinationIndex == 63){
            board.blackCanCastleQ = false;
        }
    }
}
