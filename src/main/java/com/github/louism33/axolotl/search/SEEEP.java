package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.Utils.squareDirectlyAttackedBy;
import static com.github.louism33.chesscore.Utils.xRayToSquare;
import static java.lang.Long.lowestOneBit;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class SEEEP {

    private static final int[] scores = {0, 100, 325, 350, 500, 900, 10000, 100, 325, 350, 500, 900, 10000};

    // todo, consider special case for pinned pieces
    // todo, EP moves
    public static final int getSEE(Chessboard board, int move) {
        Assert.assertTrue(isCaptureMove(move) || isEnPassantMove(move));
        final int[] gain = new int[32];
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

//        System.out.println(Arrays.toString(gain));
        
        do {
            d++;
            mover = board.pieceSquareTable[numberOfTrailingZeros(fromSet)];
            gain[d] = scores[mover] - gain[d - 1];

            System.out.println(Arrays.toString(gain));
            
            if (max(-gain[d - 1], gain[d]) < 0) {
                break;
            }
            attacks ^= fromSet;
            occupancy ^= fromSet;

            if (mover != WHITE_KNIGHT && mover != BLACK_KNIGHT) {
                final long ways = xRayToSquare(board.pieces, occupancy, lowestOneBit(fromSet), destinationIndex);
                attacks |= ways;
            }
            fromSet = getLeastValuablePiece(board.pieceSquareTable, attacks, (d & 1) == 0 ? friends : enemies);
        } while (fromSet != 0);

        System.out.println(Arrays.toString(gain));
        System.out.println("d is " +d);
        
        while (--d != 0) {
            gain[d - 1] = -max(-gain[d - 1], gain[d]);
        }

        return gain[0];
    }

    private static final long getLeastValuablePiece(int[] pieceSquareTable, long attackTable, long friends) {
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
