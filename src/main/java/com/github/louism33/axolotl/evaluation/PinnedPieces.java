package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static java.lang.Long.numberOfTrailingZeros;

class PinnedPieces {

    static int evalPinnedPieces(Chessboard board) {
        int score = 0;
        long pinnedPieces = board.pinnedPieces;
        
        while (pinnedPieces != 0) {

            Assert.assertTrue(board.pinningPieces != 0);

            final long piece = Long.lowestOneBit(pinnedPieces);
            final int pieceIndex = numberOfTrailingZeros(piece);
            final int pinnedPiece = board.pieceSquareTable[pieceIndex];

            
            
            pinnedPieces &= pinnedPieces - 1;
        }

        return score;
    }
    
}
