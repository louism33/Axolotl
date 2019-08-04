package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.ArrayList;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static java.lang.Long.numberOfTrailingZeros;

public final class KPK {

    // code adapted from stockfish https://github.com/mcostalba/Stockfish/blob/master/src/bitbase.cpp
    // with assistance from carballo https://github.com/albertoruibal/carballo

    public static void main(String[] args) {
        int num = 0;
        long start = System.currentTimeMillis();

        generateKPKBitbase();
        Assert.assertTrue(pkpReady);

        System.out.println();
        System.out.println("time: " + (System.currentTimeMillis() - start));
        System.out.println();

        for (int i = 0; i < MAX_INDEX; i++) {
            System.out.println(KPKBitbase[i]);
        }
    }

    /**
     * returns true if the player with the pawn will win
     *
     * @param board
     * @return
     */
    static boolean probe(Chessboard board) {
        int whiteKingIndex = Long.numberOfTrailingZeros(board.pieces[WHITE][KING]);
        int blackKingIndex = Long.numberOfTrailingZeros(board.pieces[BLACK][KING]);

        int pawnIndex = 0;
        boolean blackPawn = false;

        final long i = board.pieces[WHITE][PAWN];
        if (i != 0) {
            pawnIndex = numberOfTrailingZeros(i);
        } else {
            blackPawn = true;
            pawnIndex = numberOfTrailingZeros(board.pieces[BLACK][PAWN]);
        }

        int turn = board.turn;

        // Pawn is black
        if (blackPawn) {
            // flip vertical and change colors
            int tmp = whiteKingIndex;
            whiteKingIndex = 63 - blackKingIndex;
            blackKingIndex = 63 - tmp;
            pawnIndex = 63 - pawnIndex;
            turn = 1 - turn;
        }

        if ((pawnIndex & 7) > 3) {
            whiteKingIndex = flipCentre(whiteKingIndex);
            pawnIndex = flipCentre(pawnIndex);
            blackKingIndex = flipCentre(blackKingIndex);
            Assert.assertTrue((pawnIndex & 7) < 4);
        }

        return probe(whiteKingIndex, pawnIndex, blackKingIndex, turn);
    }

    private static boolean probe(int dominantKingIndex, int thePawn, int weakKingIndex, int playerToMove) {
        if (!pkpReady) {
            generateKPKBitbase();
            Assert.assertTrue(pkpReady);
        }

        Assert.assertTrue((thePawn & 7) <= 3);
        int idx = index(playerToMove, weakKingIndex, dominantKingIndex, thePawn);
        final int i = KPKBitbase[idx / 32] & (1 << (idx & 0x1F));
        return i != 0;
    }


    public static boolean probe(int dominantKingIndex, int thePawn, int weakKingIndex, int playerToMove, int winningPlayer) {
        if (!pkpReady) {
            generateKPKBitbase();
            Assert.assertTrue(pkpReady);
        }

        int whiteKing, whitePawn, blackKing, us;

        if (winningPlayer != WHITE) {
            whiteKing = flipUpDown(dominantKingIndex);
            blackKing = flipUpDown(weakKingIndex);
            whitePawn = flipUpDown(thePawn);
        } else {
            whiteKing = dominantKingIndex;
            blackKing = weakKingIndex;
            whitePawn = thePawn;
        }

        if (playerToMove == winningPlayer) {
            us = WHITE;
        } else {
            us = BLACK;
        }

        if ((whitePawn & 7) > 3) {
            whiteKing = flipCentre(whiteKing);
            whitePawn = flipCentre(whitePawn);
            blackKing = flipCentre(blackKing);
            Assert.assertTrue((whitePawn & 7) < 4);
        }
        Assert.assertTrue((whitePawn & 7) < 4);

        return probePKPForWinForWhite(whiteKing, whitePawn, blackKing, us);
    }

    static int flipCentre(int i) {
        return (i / 8) * 8 + 7 - (i & 7);
    }

    static int flipUpDown(int i) {
        return (7 - i / 8) * 8 + (i & 7);
    }

    private static boolean probePKPForWinForWhite(int wksq, int wpsq, int bksq, int us) {
        if (!pkpReady) {
            generateKPKBitbase();
            Assert.assertTrue(pkpReady);
        }
        Assert.assertTrue((wpsq & 7) <= 3);
        int idx = index(us, bksq, wksq, wpsq);
        final int i = KPKBitbase[idx / 32] & (1 << (idx & 0x1F));

        Assert.assertTrue(populationCount(i) <= 1);
        return i != 0;
    }

    // There are 24 possible pawn squares: files A to D and ranks from 2 to 7.
    // Positions with the pawn on files E to H will be mirrored before probing.
    private final static int MAX_INDEX = 2 * 24 * 64 * 64; // stm * psq * wksq * bksq = 196608

    // Each uint32_t stores results of 32 positions, one per bit
    private final static int[] KPKBitbase = new int[MAX_INDEX / 32];

    // A KPK bitbase index is an integer in [0, IndexMax] range
    //
    // Information is mapped in a way that minimizes the number of iterations:
    //
    // bit  0- 5: white king square (from SQ_A1 to SQ_H8)
    // bit  6-11: black king square (from SQ_A1 to SQ_H8)
    // bit    12: side to move (WHITE or BLACK)
    // bit 13-14: white pawn file (from FILE_A to FILE_D)
    // bit 15-17: white pawn RANK_7 - rank (from RANK_7 - RANK_7 to RANK_7 - RANK_2)
    public static int index(int us, int bksq, int wksq, int psq) {
        return wksq | (bksq << 6) | (us << 12) | ((psq & 7) << 13) | ((6 - (psq / 8)) << 15);
    }

    private static int INVALID = 0,
            UNKNOWN = 1,
            DRAW = 2,
            WIN = 4;


    private static int make_square(int file, int rank) {
        Assert.assertTrue(file <= 3);
        Assert.assertTrue(rank >= 1);
        Assert.assertTrue(rank <= 7);
        return (file & 7) + (rank - 1) * 8;
    }

    private static class KPKPosition {
        int us;
        int[] ksq = new int[2];
        int psq;
        int result;


        KPKPosition() {
        }


        KPKPosition(int idx) {
            ksq[WHITE] = (int) ((idx >>> 0) & 0x3F);
            ksq[BLACK] = (int) ((idx >>> 6) & 0x3F);

            us = (int) ((idx >>> 12) & 0x01);

            final int f = (idx >>> 13) & 0x03;
            final int r = ((6 - (idx >>> 15)) & 0x7) << 3;
            psq = f + r;

            Assert.assertTrue(psq >= 8);
            Assert.assertTrue(psq < 56);

            // Check if two pieces are on the same square or if a king can be captured
            if (chebyshevDistance(ksq[WHITE], ksq[BLACK]) <= 1
                    || ksq[WHITE] == psq
                    || ksq[BLACK] == psq
                    || (us == WHITE && ((PAWN_CAPTURE_TABLE_WHITE[psq] & newPieceOnSquare(ksq[BLACK])) != 0))) {
                result = INVALID;
            }


            // Immediate win if a pawn can be promoted without getting captured
            else {
                final int promSquareIndex = psq + 8;
                final long promotionSquare = newPieceOnSquare(promSquareIndex);
                final boolean whiteKingProtectsPromSquare = (KING_MOVE_TABLE[ksq[WHITE]] & promotionSquare) != 0;
                final boolean pawnOnSix = (psq / 8) == 6;
                final boolean whiteKingDoesNotBlockPromSquare = ksq[WHITE] != promSquareIndex;
                final boolean blackKingDoesNotBlockPromSquare = ksq[BLACK] != promSquareIndex;
                final boolean blackKingDoesNotThreatenPromSquare = (KING_MOVE_TABLE[ksq[BLACK]] & newPieceOnSquare(promSquareIndex)) == 0;

                if (us == WHITE
                        && (pawnOnSix && whiteKingDoesNotBlockPromSquare && blackKingDoesNotBlockPromSquare)
                        && ((blackKingDoesNotThreatenPromSquare || whiteKingProtectsPromSquare))) {

                    result = WIN;
                }

                // Immediate draw if it is a stalemate or a king captures undefended pawn
                else {
                    final long pawn = newPieceOnSquare(psq);
                    final long blackKingPseudo = KING_MOVE_TABLE[ksq[BLACK]];
                    final boolean blackThreatensPawn = (blackKingPseudo & pawn) != 0;
                    final long whiteKingPseudo = KING_MOVE_TABLE[ksq[WHITE]];
                    final boolean whiteDoesNotProtectPawn = (whiteKingPseudo & pawn) == 0;
                    final long whitePawnAttacks = PAWN_CAPTURE_TABLE_WHITE[psq];
                    final long freeSquares = ~(whiteKingPseudo | whitePawnAttacks);
                    final long blackKingLegalMoves = blackKingPseudo & freeSquares;

                    if (us == BLACK &&
                            ((blackThreatensPawn && whiteDoesNotProtectPawn) || (blackKingLegalMoves == 0))) {

                        result = DRAW;
                    }

                    // Position will be classified later
                    else {
                        result = UNKNOWN;
                    }
                }
            }

        }

        public int getResult() {
            return result;
        }

        int classify(ArrayList<KPKPosition> db) {
            return classify(us, db);
        }


        int classify(int turn, ArrayList<KPKPosition> db) {
            // White to move: If one move leads to a position classified as WIN, the result
            // of the current position is WIN. If all moves lead to positions classified
            // as DRAW, the current position is classified as DRAW, otherwise the current
            // position is classified as UNKNOWN.
            //
            // Black to move: If one move leads to a position classified as DRAW, the result
            // of the current position is DRAW. If all moves lead to positions classified
            // as WIN, the position is classified as WIN, otherwise the current position is
            // classified as UNKNOWN.

            Assert.assertTrue(result != 0);

            int r = INVALID;
            long b = KING_MOVE_TABLE[turn == WHITE ? ksq[WHITE] : ksq[BLACK]];

            int i = 0, ii = populationCount(b);

            while (b != 0) {
                i++;
                final int lsb = numberOfTrailingZeros(b);
                final int otherRes = (turn == WHITE)
                        ? db.get(index(BLACK, ksq[BLACK], lsb, psq)).result
                        : db.get(index(WHITE, lsb, ksq[WHITE], psq)).result;
                r |= otherRes;

                b &= b - 1;
            }

            Assert.assertEquals(i, ii);

            if (turn == WHITE) {
                if ((psq / 8) < 6) {      // Single push
                    r |= db.get(index(BLACK, ksq[BLACK], ksq[WHITE], psq + 8)).result;
                }

                if ((psq / 8) == 1   // Double push
                        && psq + 8 != ksq[WHITE]
                        && psq + 8 != ksq[BLACK]) {

                    r |= db.get(index(BLACK, ksq[BLACK], ksq[WHITE], psq + 16)).result;
                }
            }

            if (turn == WHITE) {
                return result = (r & WIN) != 0 ? WIN : (r & UNKNOWN) != 0 ? UNKNOWN : DRAW;
            } else {
                return result = (r & DRAW) != 0 ? DRAW : (r & UNKNOWN) != 0 ? UNKNOWN : WIN;
            }

        }

    }


    private static boolean pkpReady = false;

    public static void generateKPKBitbase() {
        if (pkpReady) {
            return;
        }
        ArrayList<KPKPosition> db = new ArrayList<>(MAX_INDEX);

        int idx;
        boolean repeat = true;

        // Initialize db with known win / draw positions
        for (idx = 0; idx < MAX_INDEX; ++idx) {
            db.add(new KPKPosition(idx));
        }

        // Iterate through the positions until none of the unknown positions can be
        // changed to either wins or draws (15 cycles needed).
        while (repeat) {
            for (repeat = false, idx = 0; idx < MAX_INDEX; ++idx) { 
                repeat |= ((db.get(idx).result == UNKNOWN) && (db.get(idx).classify(db) != UNKNOWN));
            }
        }

        // Map 32 results into one KPKBitbase[] entry
        for (idx = 0; idx < MAX_INDEX; ++idx) {
            final int result = db.get(idx).result;
            if (result == WIN) {
                KPKBitbase[idx / 32] |= 1 << (idx & 0x1F);
            }
        }
        pkpReady = true;
    }

//    static {
//        generateKPKBitbase();
//        Assert.assertTrue(pkpReady);
//    }

}


