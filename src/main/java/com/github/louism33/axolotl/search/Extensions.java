package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.Chessboard;

class Extensions {

    static int extensions(Chessboard board, int ply, boolean boardInCheck){
        
        if (ply < 1){
            return 0;
        }

        if (boardInCheck){
            Statistics.numberOfCheckExtensions++;
            return 1;
        }

        if (board.previousMoveWasPawnPushToSeven()){
            Statistics.numberOfPassedPawnExtensions++;
            return 1;
        }
      
        if (board.generateLegalMoves().length == 1){
            return 1;
        }

        return 0;
    }
}
