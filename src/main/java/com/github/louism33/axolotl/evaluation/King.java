package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.*;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.MoveTable.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;

class King {

    /*
    thanks to Ed Schroeder
    http://www.top-5000.nl/authors/rebel/chess840.htm
     */
    static int evalKingByTurn(Chessboard board, boolean white,
                              long myPawns, long myKing,
                              long friends, long enemies,
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

        boolean them = !white;
        
        Assert.assertEquals(1, populationCount(myKing));

        int myKingIndex = BitOperations.getIndexOfFirstPiece(myKing);

        long square = BitOperations.squareCentredOnIndex(myKingIndex);

        long row = Square.getRow(myKingIndex);
        
        if (white){
            long nextRow = row << 8;
            dotSquares = square & (~nextRow);
            oSquares = square & nextRow;
            xSquares = oSquares << 8;
        }
        else {
            long nextRow = row >>> 8;
            dotSquares = square & (~nextRow);
            oSquares = square & nextRow;
            xSquares = oSquares >>> 8;
        }
        
        // xSquares
        // measure just pressure
        while (xSquares != 0) {
            int sq = BitOperations.getIndexOfFirstPiece(xSquares);
            kingSafetyLookupCounter += getTotalAttacksToSquare(them, sq);
            if (numberOfAttacksBy(them, sq, white ? MoveParser.WHITE_QUEEN : MoveParser.BLACK_QUEEN) != 0){
                kingSafetyLookupCounter++;
            }
            xSquares &= xSquares - 1;
        }

        
        // dotSquares
        // measure pressure and defence
        while (dotSquares != 0) {
            long sq = BitOperations.getFirstPiece(dotSquares);
            int index = BitOperations.getIndexOfFirstPiece(dotSquares);
            int totalAttacksToSquare = getTotalAttacksToSquare(them, index);
            kingSafetyLookupCounter += totalAttacksToSquare;
            if (((sq & friends) == 0) && getTotalAttacksToSquare(white, index) == 0){
                kingSafetyLookupCounter++;
            }
            dotSquares &= dotSquares - 1;
        }

        // oSquares
        // measure pressure defence and defending pieces
        while (oSquares != 0) {
            long sq = BitOperations.getFirstPiece(oSquares);
            int index = BitOperations.getIndexOfFirstPiece(oSquares);
            int totalAttacksToSquare = MoveTable.getTotalAttacksToSquare(them, index);
            kingSafetyLookupCounter += totalAttacksToSquare;
            if (MoveTable.getTotalAttacksToSquare(white, index) == 0){
                kingSafetyLookupCounter++;
            }
            if ((sq & friends) == 0){
                kingSafetyLookupCounter++;
            }
            oSquares &= oSquares - 1;
        }

        return kingSafetyLookupCounter >= KING_SAFETY_ARRAY.length ? - 700 
                : -KING_SAFETY_ARRAY[kingSafetyLookupCounter];
    }
    
}









