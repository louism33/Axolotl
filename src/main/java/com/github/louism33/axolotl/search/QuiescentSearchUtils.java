package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

class QuiescentSearchUtils {

    /*
    a board is quiet when there are no more captures that can be made
     */
    static boolean isBoardQuiet(Chessboard board, int[] moves){
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0){
                break;
            }
            if (MoveParser.isCaptureMove(move)) {
                return false;
            }
        }
        return true;
    }
    
}
