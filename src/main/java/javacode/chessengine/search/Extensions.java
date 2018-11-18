package javacode.chessengine.search;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.StackMoveData;

import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;

class Extensions {

    private final Engine engine;

    Extensions(Engine engine) {
        this.engine = engine;
    }

    int extensions(Chessboard board, int ply, boolean boardInCheck){
        /*
        do not extend at root node
         */
        if (ply < 1){
            return 0;
        }

        if (boardInCheck){
            this.engine.statistics.numberOfCheckExtensions++;
            return 1;
        }

        /*
        Passed Pawn Extension:
        extend if pawn was just moved to penultimate rank
        */
        if (board.moveStack.size() > 0) {
            StackMoveData peek = board.moveStack.peek();
            Move previousMove = peek.move;
            long destinationSquareOfPreviousMove = newPieceOnSquare(previousMove.destinationIndex);

            if (board.isWhiteTurn()) {
                if ((destinationSquareOfPreviousMove & BitBoards.RANK_TWO & board.BLACK_PAWNS) != 0) {
                    this.engine.statistics.numberOfPassedPawnExtensions++;
                    return 1;
                }
            } else {
                if ((destinationSquareOfPreviousMove & BitBoards.RANK_SEVEN & board.WHITE_PAWNS) != 0) {
                    this.engine.statistics.numberOfPassedPawnExtensions++;
                    return 1;
                }
            }
        }
        /*
        Singular Reply Extension:
        if only there is only one legal move, searchMyTime deeper as we are forced situation
         */
        if (generateLegalMoves(board, board.isWhiteTurn()).size() == 1){
            return 1;
        }

        return 0;
    }
}
