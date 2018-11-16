package javacode.chessprogram.moveMaking;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;

import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;

class MoveEnPassant {
    
    static void makeEnPassantMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceAsPieceIndex());
        long destinationPiece = newPieceOnSquare(move.destinationIndex);
        
        if ((destinationPiece & board.ALL_PIECES()) != 0) {
            throw new RuntimeException("EP move Problem");
        }
        
        if ((sourcePiece & board.WHITE_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece >>> 8);
            board.WHITE_PAWNS |= destinationPiece;
        }
        
        else if  ((sourcePiece & board.BLACK_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece << 8);
            board.BLACK_PAWNS |= destinationPiece;
        }
        else {
            throw new RuntimeException("false EP move");
        }
    }


}
