package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.chesscore.BoardConstants.*;
import static java.lang.Long.numberOfTrailingZeros;

final class Init {

    static long kingSafetyArea(Chessboard board, int turn) {
        final long myKing = board.pieces[turn][KING];
        final int kingIndex = numberOfTrailingZeros(myKing);
        long sq = BitOperations.squareCentredOnIndex(kingIndex);
        if ((myKing & FILE_A) != 0) {
            sq |= (sq >>> 1);
        }
        else if ((myKing & FILE_H) != 0) {
            sq |= (sq << 1);
        }

        if (turn == WHITE) {
            sq |= (sq << 8);
        } else {
            sq |= (sq >>> 8);
        }

        sq &= ~myKing;
        return sq;
    }
}
