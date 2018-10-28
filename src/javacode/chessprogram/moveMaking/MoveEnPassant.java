package javacode.chessprogram.moveMaking;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.graphicsandui.Art;

import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;

class MoveEnPassant {
    
    static void makeEnPassantMove(Chessboard board, Move move){
        long sourcePiece = newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = newPieceOnSquare(move.destination);
        
        if ((destinationPiece & board.ALL_PIECES()) != 0) {
            System.out.println("EP MOVE PROBLEM!");
            System.out.println("source");
            Art.printLong(sourcePiece);
            System.out.println("destination");
            Art.printLong(destinationPiece);
            System.out.println("all pieces");
            Art.printLong(board.ALL_PIECES());
            System.out.println(Art.boardArt(board));
            System.out.println(move);
            System.out.println("white move: "+board.isWhiteTurn());
            
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
