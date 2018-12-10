package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.*;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.FILES;

class Misc {

    static int evalMiscByTurn(Chessboard board, boolean white, int[] moves, long pinnedPieces, boolean inCheck) {
        int score = 0;
        
        if (board.isWhiteTurn() == white){
            score += MY_TURN_BONUS;
            score += (MoveParser.numberOfRealMoves(moves) * MOVE_NUMBER_POINT) / 10;

        }
        
        while (pinnedPieces != 0){
            long pinnedPiece = BitOperations.getFirstPiece(pinnedPieces);
            int ordinal = Piece.pieceOnSquare(board, pinnedPiece).ordinal();
            score += ordinal > 6 ? PINNED_PIECES[ordinal - 6] : PINNED_PIECES[ordinal];
            pinnedPieces &= pinnedPieces -1;
        }
        
        if (inCheck){
            score += IN_CHECK_PENALTY;
        }

        return score;
    }


}