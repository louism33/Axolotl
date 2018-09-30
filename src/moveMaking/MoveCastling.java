package moveMaking;

import chess.BitManipulations;
import chess.Chessboard;
import chess.Move;

class MoveCastling {
    //todo
    static void makeCastlingMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);

        throw new RuntimeException("unfinished ");
    }
}
