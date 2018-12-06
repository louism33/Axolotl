package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

class Extensions {

    static int extensions(Chessboard board, int ply, boolean boardInCheck){
        /*
        do not extend at root node
         */
        if (ply < 1){
            return 0;
        }

        if (boardInCheck){
            Engine.statistics.numberOfCheckExtensions++;
            return 1;
        }

        /*
        Passed Pawn Extension:
        extend if pawn was just moved to penultimate rank
        */
//        if (board.moveStack.size() > 0) {
//            StackDataParser peek = board.moveStack.peek();
//            int previousMove = peek.move;
//            long destinationSquareOfPreviousMove = newPieceOnSquare(previousMove.destinationIndex);
//
//            if (board.isWhiteTurn()) {
//                if ((destinationSquareOfPreviousMove & BitBoards.RANK_TWO & board.getBlackPawns()) != 0) {
//                    this.engine.statistics.numberOfPassedPawnExtensions++;
//                    return 1;
//                }
//            } else {
//                if ((destinationSquareOfPreviousMove & BitBoards.RANK_SEVEN & board.getWhitePawns()) != 0) {
//                    this.engine.statistics.numberOfPassedPawnExtensions++;
//                    return 1;
//                }
//            }
//        }
        /*
        Singular Reply Extension:
        if only there is only one legal move, searchMyTime deeper as we are forced situation
         */
        if (board.generateLegalMoves().length == 1){
            return 1;
        }

        return 0;
    }
}
