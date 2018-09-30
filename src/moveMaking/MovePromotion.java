package moveMaking;

import chess.BitManipulations;
import chess.Chessboard;
import chess.Move;

class MovePromotion {

    //todo
    static void makePromotingMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);

    }

}
