package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.chesscore.Chessboard;

class QuiescentSearchUtils {

    /*
    a board is quiet when there are no more captures that can be made
     */
    static boolean isBoardQuiet(Chessboard board, int[] moves){
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (MoveOrderer.moveIsCapture(board, move)) {
                return false;
            }
        }
        return true;
    }
    
}
