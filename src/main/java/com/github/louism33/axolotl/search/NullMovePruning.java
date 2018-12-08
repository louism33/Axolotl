package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.chesscore.BitOperations.populationCount;

class NullMovePruning {
    
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

    static boolean maybeInEndgame(Chessboard board){
        return populationCount(board.allPieces()) < 9;
    }

    static boolean maybeInZugzwang(Chessboard board, boolean white){
        // returns true if you are down to Pawns and King (+1 extra piece)
        long myPawns, myKing, allMyPieces;
        if (white){
            allMyPieces = board.whitePieces();
            myPawns = board.getWhitePawns();
            myKing = board.getWhiteKing();
        }
        else {
            allMyPieces = board.blackPieces();
            myPawns = board.getBlackPawns();
            myKing = board.getBlackKing();
        }
        return populationCount(allMyPieces ^ (myPawns | myKing)) <= 1;
    }

    static boolean onlyPawnsLeftForPlayer(Chessboard board, boolean white){
        long myPawns, myKing, allMyPieces;
        if (white){
            allMyPieces = board.whitePieces();
            myPawns = board.getWhitePawns();
            myKing = board.getWhiteKing();
        }
        else {
            allMyPieces = board.blackPieces();
            myPawns = board.getBlackPawns();
            myKing = board.getBlackKing();
        }
        return populationCount(allMyPieces ^ (myPawns | myKing)) == 0;
    }
    
    

}
