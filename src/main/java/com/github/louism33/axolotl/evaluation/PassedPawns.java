package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.Init.*;
import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BoardConstants.*;

public final class PassedPawns {

    static int evalPassedPawnsByTurn(Chessboard board, int turn) {
        int passedPawnScore = 0;

        final long fileWithoutMyPawns = filesWithNoPlayerPawns[turn];
        final long fileWithoutEnemyPawns = filesWithNoPlayerPawns[1 - turn];
        final long openFiles = fileWithoutEnemyPawns & fileWithoutMyPawns;
        final long enemyPawnAttackSpan = pawnTables[1 - turn][SPANS];

        long unblockedPawns = board.pieces[turn][PAWN] & (fileWithoutEnemyPawns | ~enemyPawnAttackSpan);

        long enemies = board.pieces[1 - turn][ALL_COLOUR_PIECES];
        long squaresIProtect = Evaluator.turnThreatensSquares[turn];
        long squaresEnemyThreatens = Evaluator.turnThreatensSquares[1 - turn];
        /*
        passed pawns
         */
        long notHomeRanks = ~(PENULTIMATE_RANKS[1 - turn] | INTERMEDIATE_RANKS[turn]);
        while (unblockedPawns != 0) {
            final long pawn = getFirstPiece(unblockedPawns);
            final int pawnIndex = Long.numberOfTrailingZeros(pawn);
            passedPawnScore |= PAWN_UNBLOCKED;

            if ((pawn & notHomeRanks) != 0) {
                final long stopSq = turn == WHITE ? pawn << 8 : pawn >>> 8;

                if ((stopSq & enemies) == 0) {
                    passedPawnScore += PAWN_OPEN_STOP_SQUARE;
                }

                if ((stopSq & squaresEnemyThreatens) == 0) {
                    passedPawnScore += PAWN_OPEN_STOP_SQUARE;
                }

                long fileBehind = fileForward[1 - turn][pawnIndex];

                if ((fileBehind & (board.pieces[turn][ROOK] | board.pieces[turn][QUEEN])) != 0) {
                    passedPawnScore += ROOK_OR_QUEEN_BEHIND_PP;
                }

            }

            unblockedPawns &= unblockedPawns - 1;
        }

        return passedPawnScore;
    }
}
