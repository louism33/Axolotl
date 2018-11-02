package javacode.chessengine;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.StackMoveData;

import static javacode.chessengine.Engine.*;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;

public class Extensions {

    static int extensions(Chessboard board, int ply){
        if (!ALLOW_EXTENSIONS){
            return 0;
        }

        if (ply < 1){
            return 0;
        }

        if (boardInCheck(board, board.isWhiteTurn())){
            if (DEBUG){
                numberOfCheckExtensions++;
            }
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
                    if (DEBUG) {
                        numberOfPassedPawnExtensions++;
                    }
                    return 1;
                }
            } else {
                if ((destinationSquareOfPreviousMove & board.WHITE_PAWNS & BitBoards.RANK_SEVEN) != 0) {
                    if (DEBUG) {
                        numberOfPassedPawnExtensions++;
                    }
                    return 1;
                }
            }
        }
        /*
        Singular Reply Extension:
        if only there is only one legal move, search deeper as we are forced situation
        */
        if (generateLegalMoves(board, board.isWhiteTurn()).size() == 1){
            return 1;
        }

        return 0;
    }
}
