package javacode.chessprogram.moveMaking;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.graphicsandui.Art;

class MoveEnPassant {
    
    static void makeEnPassantMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);
        
        if ((destinationPiece & board.ALL_PIECES()) != 0) {
            System.out.println(Art.boardArt(board));
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
