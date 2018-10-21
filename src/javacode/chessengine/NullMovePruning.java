package javacode.chessengine;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

class NullMovePruning {
    
    /*
    Null Move Pruning:
    only perform if not in check, endgame, or position where we would actually like to pass our move (Zugzwang)
     */
    static boolean isNullMoveOkHere(Chessboard board, int depth, int nullMoveDepthReduction){
        return Engine.ALLOW_NULL_MOVE_PRUNING
                && depth >= nullMoveDepthReduction
                && !CheckChecker.boardInCheck(board, board.isWhiteTurn())
                && !maybeInEndgame(board)
                && !maybeInZugzwang(board, board.isWhiteTurn())
                ;
    }

    private static boolean maybeInEndgame(Chessboard board){
        return BitIndexing.populationCount(board.ALL_PIECES()) < 6;
    }

    private static boolean maybeInZugzwang(Chessboard board, boolean white){
        // returns true if you are down to Paws and King (+1 extra piece)
        long myPawns, myKing, allMyPieces;
        if (white){
            allMyPieces = board.ALL_WHITE_PIECES();
            myPawns = board.WHITE_PAWNS;
            myKing = board.WHITE_KING;
        }
        else {
            allMyPieces = board.ALL_BLACK_PIECES();
            myPawns = board.BLACK_PAWNS;
            myKing = board.BLACK_KING;
        }
        return BitIndexing.populationCount(allMyPieces ^ (myPawns | myKing)) <= 1;
    }
}
