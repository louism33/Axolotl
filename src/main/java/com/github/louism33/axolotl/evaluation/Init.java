package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static java.lang.Long.numberOfTrailingZeros;

final class Init {

    private static long bulkPawnPushes(long pawns, int turn, long legalPushes, long allPieces) {
        final long possiblePawnSinglePushes = turn == WHITE ? pawns << 8 : pawns >>> 8;
        final long intermediateRank = INTERMEDIATE_RANKS[turn];
        final long possibleDoubles = (((possiblePawnSinglePushes & intermediateRank & ~allPieces) ));
        return (possiblePawnSinglePushes | (turn == WHITE ? possibleDoubles << 8 : possibleDoubles >>> 8))
                & legalPushes & ~allPieces;
    }


    static final int PUSHES = 0, CAPTURES = 1, SPANS = 2, DOUBLE_CAPTURES = 3;
    static final long[][] pawnTables = new long[2][4];
    static final long[] filesWithNoPlayerPawns = new long[2];
    static final long[] kingSafetyArea = new long[2];
    static final int[] attackingEnemyKingLookup = new int[2];

    static void init(Chessboard board, int turn) {
        long allPieces = board.allPieces();
        long ps = board.pieces[turn][PAWN];
        pawnTables[turn][PUSHES] = bulkPawnPushes(ps, turn, UNIVERSE, allPieces);
        long allPawnCaptures = 0, allPawnSpans = 0, filesWithPawns = 0, pawnDoubleCaptures = 0;
        long pawns = ps;
        while (pawns != 0) {
            final long pawn = getFirstPiece(pawns);
            int pawnIndex = numberOfTrailingZeros(pawn);

            filesWithPawns |= FILES[pawnIndex % 8];

            long captureTable = singlePawnCaptures(pawn, turn, UNIVERSE);
            pawnDoubleCaptures |= (captureTable & allPawnCaptures);
            allPawnCaptures |= captureTable;

            while (captureTable != 0) {
                allPawnSpans |= fileForward[turn][numberOfTrailingZeros(captureTable)];
                captureTable &= captureTable - 1;
            }
            
            pawns &= pawns - 1;
        }
        
        filesWithNoPlayerPawns[turn] = ~filesWithPawns;
        
        pawnTables[turn][SPANS] = allPawnSpans;
        pawnTables[turn][CAPTURES] = allPawnCaptures;
        pawnTables[turn][DOUBLE_CAPTURES] = pawnDoubleCaptures;


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
        kingSafetyArea[turn] = sq;
        attackingEnemyKingLookup[turn] = 16; 
    }
}
