package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.pawnFeatures;
import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.*;
import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;

public final class PassedPawns {

    static int evalPassedPawnsByTurn(Chessboard board, int turn, long[] pawnData, long[] turnThreatensSquares) {
        int passedPawnScore = 0;

        final long squaresMyPawnsThreaten = pawnData[CAPTURES + turn];
        final long squaresEnemyPawnsThreaten = pawnData[CAPTURES + 1 - turn];

        final long myPawnAttackSpan = pawnData[SPANS + turn];
        final long enemyPawnAttackSpan = pawnData[SPANS + 1 - turn];

        final boolean whiteToPlay = turn == WHITE;
        final long outpostRanks = whiteToPlay ? (RANK_FOUR | RANK_FIVE | RANK_SIX) : (RANK_FIVE | RANK_FOUR | RANK_THREE);
        final long unthreatenableOutpostSpots = outpostRanks & ~(enemyPawnAttackSpan | squaresEnemyPawnsThreaten);

        final long fileWithoutMyPawns = pawnData[FILE_WITHOUT_MY_PAWNS + turn];
        final long fileWithoutEnemyPawns = pawnData[FILE_WITHOUT_MY_PAWNS + 1 - turn];

        final long openFiles = fileWithoutEnemyPawns & fileWithoutMyPawns;

        long unblockedPawns = board.pieces[turn][PAWN] & (fileWithoutEnemyPawns | ~enemyPawnAttackSpan);

        long friends = board.pieces[turn][ALL_COLOUR_PIECES];
        long enemies = board.pieces[1 - turn][ALL_COLOUR_PIECES];
        long allPieces = friends | enemies;
        long squaresIProtect = turnThreatensSquares[turn];
        long squaresEnemyThreatens = turnThreatensSquares[1 - turn];

        long myPassedPawns = pawnData[PASSED_PAWNS + turn];

        long notHomeRanks = ~(PENULTIMATE_RANKS[1 - turn] | INTERMEDIATE_RANKS[turn]);


        while (myPassedPawns != 0) {
            final long pawn = getFirstPiece(myPassedPawns);
            final int pawnIndex = Long.numberOfTrailingZeros(pawn);
            passedPawnScore |= pawnFeatures[EvaluationConstants.PAWN_UNBLOCKED];

            if ((pawn & notHomeRanks) != 0) {
                final long stopSq = turn == WHITE ? pawn << 8 : pawn >>> 8;

                if ((stopSq & allPieces) == 0) {
                    passedPawnScore += pawnFeatures[EvaluationConstants.PAWN_OPEN_STOP_SQUARE];
                }

                if ((stopSq & squaresEnemyThreatens) == 0) {
                    passedPawnScore += pawnFeatures[EvaluationConstants.PAWN_OPEN_STOP_SQUARE];
                }

                long fileBehind = fileForward[1 - turn][pawnIndex];

                if ((fileBehind & (board.pieces[turn][ROOK] | board.pieces[turn][QUEEN])) != 0) {
                    passedPawnScore += pawnFeatures[EvaluationConstants.ROOK_OR_QUEEN_BEHIND_PP];
                }

            } else {
                // small bonus for passed pawns a long way away from promoting
                passedPawnScore += pawnFeatures[EvaluationConstants.PAWN_YOUNG_PASSED];
            }

            myPassedPawns &= myPassedPawns - 1;
        }

        return passedPawnScore;
    }
}
