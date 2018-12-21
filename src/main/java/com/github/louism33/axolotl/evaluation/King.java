package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.*;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class King {

    /*
    thanks to Ed Schroeder
    http://www.top-5000.nl/authors/rebel/chess840.htm
     */
    static int evalKingByTurn(Chessboard board, boolean white,
                              long myPawns, long myKing,
                              long allPieces) {

        /*
        00000
        0xxx0
        0ooo0
        0.K.0
        0...0
         */
        int kingSafetyLookupCounter = 0;
        
        long dotSquares, oSquares, xSquares;
        
        Assert.assertEquals(1, populationCount(myKing));

        int myKingIndex = BitOperations.getIndexOfFirstPiece(myKing);

        long square = BitOperations.squareCentredOnIndex(myKingIndex);

        long row = Square.getRow(myKingIndex);
        
        if (white){
            long nextRow = row << 8;
            long nextNextRow = row << 16;
            dotSquares = square & (~nextRow);
            oSquares = square & nextRow;
            xSquares = oSquares << 8;
        }
        else {
            long nextRow = row >>> 8;
            long nextNextRow = row >>> 16;
            dotSquares = square & (~nextRow);
            oSquares = square & nextRow;
            xSquares = oSquares >>> 8;
        }

        // xSquares
        // measure just pressure
        
        // dotSquares
        // measure pressure and defence
        
        // oSquares
        // measure pressure defence and defending pieces

        return KING_SAFETY_ARRAY[kingSafetyLookupCounter];
    }
    
}









