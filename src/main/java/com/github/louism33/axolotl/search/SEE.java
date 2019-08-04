package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.Utils.squareDirectlyAttackedBy;
import static com.github.louism33.chesscore.Utils.xRayToSquare;
import static java.lang.Long.lowestOneBit;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class SEE {

    static final int[] scores = {0, 100, 325, 350, 500, 900, 10000, 100, 325, 350, 500, 900, 10000};

    private static int[][] gainBackend;
    private static boolean readySEE = false;

    static void setupSEE(boolean force) {
        if (force || !readySEE) {
            gainBackend = new int[EngineSpecifications.NUMBER_OF_THREADS][32];
        }
        readySEE = true;
    }
    
    // todo, consider special case for pinned pieces
    static int getSEE(Chessboard board, int move, int whichThread) {
        Assert.assertTrue(isCaptureMove(move) || isEnPassantMove(move));
        final int[] gain = gainBackend[whichThread]; 
        int d = 0;
        final int destinationIndex = getDestinationIndex(move);
        final int sourceIndex = getSourceIndex(move);
        long fromSet = newPieceOnSquare(sourceIndex);

        final long friends = board.pieces[board.turn][ALL_COLOUR_PIECES];
        final long enemies = board.pieces[1 - board.turn][ALL_COLOUR_PIECES];
        long occupancy = friends | enemies;

        long attacks = squareDirectlyAttackedBy(board, destinationIndex);
        if (!isEnPassantMove(move)) {
            gain[d] = scores[getVictimPieceInt(move)];
        } else {
            gain[d] = scores[WHITE_PAWN];
        }
        int mover;

        do {
            d++;
            mover = board.pieceSquareTable[numberOfTrailingZeros(fromSet)];
            gain[d] = scores[mover] - gain[d - 1];
            if (max(-gain[d - 1], gain[d]) < 0) {
//                break; // this causes incorrect values...
            }
            attacks ^= fromSet;
            occupancy ^= fromSet;

            if (mover != WHITE_KNIGHT && mover != BLACK_KNIGHT) {
                final long ways = xRayToSquare(board.pieces, occupancy, lowestOneBit(fromSet), destinationIndex);
                attacks |= ways;
            }
            fromSet = getLeastValuablePiece(board.pieceSquareTable, attacks, (d & 1) == 0 ? friends : enemies);
        } while (fromSet != 0);

        while (--d != 0) {
            gain[d - 1] = -max(-gain[d - 1], gain[d]);
        }

        return gain[0];
    }

    private static long getLeastValuablePiece(int[] pieceSquareTable, long attackTable, long friends) {
        long myPeople = attackTable & friends;
        int weakestAttackerScore = 999;
        long weakestAttacker = 0;

        while (myPeople != 0) {
            final int a = pieceSquareTable[numberOfTrailingZeros(myPeople)];
            if (a < weakestAttackerScore) {
                weakestAttacker = lowestOneBit(myPeople);
            }
            weakestAttackerScore = min(weakestAttackerScore, a);
            if (weakestAttackerScore == 100) {
                return weakestAttacker;
            }
            myPeople &= myPeople - 1;
        }
        return weakestAttacker;
    }

}
