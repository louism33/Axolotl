package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;

final class Init {

    private static long bulkPawnPushes(long pawns, int turn, long legalPushes, long allPieces) {
        final long possiblePawnSinglePushes = turn == WHITE ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = INTERMEDIATE_RANKS[turn];
        final long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (turn == WHITE ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }


    static final int PUSHES = 0, CAPTURES = 1;
    static final long[][] pawnTables = new long[2][2];
    static final long[] kingBigArea = new long[2];
    static final long[] kingSmallArea = new long[2];
    static final int[] attackingEnemyKingLookup = new int[2];

    static void init(Chessboard board, int turn) {
        long allPieces = board.allPieces();
        long ps = board.pieces[turn][PAWN];
        pawnTables[turn][PUSHES] = bulkPawnPushes(ps, turn, UNIVERSE, allPieces);
        long allPawnCaptures = 0;
        long pawns = ps;
        while (pawns != 0) {
            final long pawn = getFirstPiece(pawns);
            long captureTable = singlePawnCaptures(pawn, turn, UNIVERSE);
            allPawnCaptures |= captureTable;
            pawns &= pawns - 1;
        }
        pawnTables[turn][CAPTURES] = allPawnCaptures;

        kingSmallArea[turn] = BitOperations.squareCentredOnIndex(Long.numberOfTrailingZeros(board.pieces[turn][KING]));
        kingBigArea[turn] = BitOperations.bigSquareCentredOnIndex(Long.numberOfTrailingZeros(board.pieces[turn][KING]));
        attackingEnemyKingLookup[turn] = 0;
        
    }

}
