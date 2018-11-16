package javacode.chessengine.search;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

public class NullMovePruning {
    
    static int nullMoveDepthReduction(int depth){
        return 2;
    }
    
    /*
    Null Move Pruning:
    only perform if not in check, endgame, or position where we would actually like to pass our move (Zugzwang)
     */
    static boolean isNullMoveOkHere(Chessboard board){
        return !maybeInEndgame(board)
                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())
                ;
    }

    public static boolean maybeInEndgame(Chessboard board){
        return BitIndexing.populationCount(board.ALL_PIECES()) < 9;
    }

    static boolean maybeInZugzwang(Chessboard board, boolean white){
        // returns true if you are down to Pawns and King (+1 extra piece)
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

    public static boolean onlyPawnsLeftForPlayer(Chessboard board, boolean white){
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
        return BitIndexing.populationCount(allMyPieces ^ (myPawns | myKing)) == 0;
    }
    
    

}
