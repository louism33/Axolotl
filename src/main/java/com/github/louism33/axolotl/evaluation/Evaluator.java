package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvalPrintObject.kingSafetyScore;
import static com.github.louism33.axolotl.evaluation.EvalPrintObject.totalScore;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.POSITION_SCORES;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.mobilityScores;
import static com.github.louism33.axolotl.evaluation.Init.*;
import static com.github.louism33.axolotl.evaluation.MoveTable.populateFromMoves;
import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

@SuppressWarnings("ALL")
public class Evaluator {

    static boolean isEndgame = false;

    static int[] whiteThreatsToSquare = new int[64];
    static int[] blackThreatsToSquare = new int[64];

    static void resetThreats(){
        Arrays.fill(whiteThreatsToSquare, 0);
        Arrays.fill(blackThreatsToSquare, 0);
    }

    public static void printEval(Chessboard board, int turn){
        printEval(board, turn, board.generateLegalMoves());
    }
    public static void printEval(Chessboard board, int turn, int[] moves){
        eval(board, moves);
        EvalPrintObject epo = new EvalPrintObject(scoresForEPO);
        System.out.println(epo);
    }

    public static int eval(Chessboard board, int[] moves) {
        Assert.assertTrue(moves != null);

        Arrays.fill(scoresForEPO[WHITE], 0);
        Arrays.fill(scoresForEPO[BLACK], 0);

        init(board, WHITE);
        init(board, BLACK);

        int turn = board.turn;
        
        int score = evalTurn(board, turn) - evalTurn(board, 1 - turn);

        int mks = attackingEnemyKingLookup[turn] >= 0 
                ? attackingEnemyKingLookup[turn]
                : 0;
        
        int myKingSafety = KING_SAFETY_ARRAY[mks];

        int yks = attackingEnemyKingLookup[1 - turn] >= 0
                ? attackingEnemyKingLookup[1 - turn]
                : 0;
        
        int yourKingSafety = KING_SAFETY_ARRAY[yks];
        
        scoresForEPO[turn][kingSafetyScore] -= myKingSafety;
        scoresForEPO[1 - turn][kingSafetyScore] += yourKingSafety;

        score -= myKingSafety;
        score += yourKingSafety;

        scoresForEPO[turn][totalScore] = score;
        scoresForEPO[1 - turn][totalScore] = score;
        return score;
    }

    static int[][] scoresForEPO = new int[2][13];

    private final static int evalTurn(Chessboard board, int turn){
        //please generate moves before calling this
        final long[][] pieces = board.pieces;

        long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        final long friends, enemies;

        myPawns = pieces[turn][PAWN];
        myKnights = pieces[turn][KNIGHT];
        myBishops = pieces[turn][BISHOP];
        myRooks = pieces[turn][ROOK];
        myQueens = pieces[turn][QUEEN];
        myKing = pieces[turn][KING];

        enemyPawns = pieces[1 - turn][PAWN];
        enemyKnights = pieces[1 - turn][KNIGHT];
        enemyBishops = pieces[1 - turn][BISHOP];
        enemyRooks = pieces[1 - turn][ROOK];
        enemyQueens = pieces[1 - turn][QUEEN];
        enemyKing = pieces[1 - turn][KING];

        friends = pieces[turn][ALL_COLOUR_PIECES];
        enemies = pieces[1 - turn][ALL_COLOUR_PIECES];

        Assert.assertTrue(friends != 0);
        Assert.assertTrue(enemies != 0);

        final long allPieces = friends | enemies;
        long pinnedPieces = board.pinnedPieces;
        boolean inCheck = board.inCheckRecorder;

        int finalScore = 0, materialScore = 0;

        materialScore += BitOperations.populationCount(myPawns) * PAWN_SCORE;
        materialScore += BitOperations.populationCount(myKnights) * KNIGHT_SCORE;
        materialScore += BitOperations.populationCount(myBishops) * BISHOP_SCORE;
        materialScore += BitOperations.populationCount(myRooks) * ROOK_SCORE;
        materialScore += BitOperations.populationCount(myQueens) * QUEEN_SCORE;

        scoresForEPO[turn][EvalPrintObject.materialScore] = materialScore;

        finalScore += materialScore;

        long ignoreThesePieces = 0; // maybe pins

        int positionScore = 0;
        int mobilityScore = 0;

        long squaresMyPawnsThreaten = pawnTables[turn][CAPTURES];

        long squaresEnemyPawnsThreaten = pawnTables[1 - turn][CAPTURES];
        long enemyPawnShifts = turn == WHITE ? enemyPawns >>> 8 : enemyPawns << 8;
        long myBlockedPawns = enemyPawnShifts & myPawns;
        long myBackwardsPawns = myPawns & (PENULTIMATE_RANKS[1 - turn] | INTERMEDIATE_RANKS[turn]);

        long safeMobSquares = ~(myKing | myQueens | myBackwardsPawns | myBlockedPawns | squaresEnemyPawnsThreaten);

        long enemyKingBigArea = kingBigArea[1 - turn];
        long enemyKingSmallArea = kingSmallArea[1 - turn];

        long myKingBigArea = kingBigArea[turn];
        long myKingSmallArea = kingSmallArea[turn];

        int defendingMyKingLookup = 0;

        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                final int knightIndex = numberOfTrailingZeros(knight);
                positionScore += POSITION_SCORES[turn][KNIGHT][63 - knightIndex];

                final long table = KNIGHT_MOVE_TABLE[knightIndex] & safeMobSquares;

                mobilityScore += mobilityScores[KNIGHT - 2][populationCount(table)];

                if ((knight & enemyKingBigArea) != 0) {
                    attackingEnemyKingLookup[turn] += 2;
                }

                attackingEnemyKingLookup[turn] += populationCount(table & enemyKingSmallArea);
            }
            myKnights &= (myKnights - 1);
        }

        while (myBishops != 0){
            final long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                final int bishopIndex = numberOfTrailingZeros(bishop);
                positionScore += POSITION_SCORES[turn][BISHOP][63 - bishopIndex];

                final long table = singleBishopTable(allPieces, bishopIndex, UNIVERSE) & safeMobSquares;

                mobilityScore += mobilityScores[BISHOP - 2][populationCount(table)];

                if ((bishop & enemyKingBigArea) != 0) {
                    attackingEnemyKingLookup[turn] += 2;
                }

                attackingEnemyKingLookup[turn] += populationCount(table & enemyKingSmallArea);
            }
            myBishops &= (myBishops - 1);
        }

        while (myRooks != 0){
            final long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                final int rookIndex = numberOfTrailingZeros(rook);
                positionScore += POSITION_SCORES[turn][ROOK][63 - rookIndex];

                final long table = singleRookTable(allPieces, rookIndex, UNIVERSE) & safeMobSquares;

                mobilityScore += mobilityScores[ROOK - 2][populationCount(table)];

                if ((rook & enemyKingBigArea) != 0) {
                    attackingEnemyKingLookup[turn] += 3;
                }

                attackingEnemyKingLookup[turn] += populationCount(table & enemyKingSmallArea);
            }
            myRooks &= (myRooks - 1);
        }

        while (myQueens != 0){
            final long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                final int queenIndex = numberOfTrailingZeros(queen);
                positionScore += POSITION_SCORES[turn][QUEEN][63 - queenIndex];

                final long table = singleQueenTable(allPieces, queenIndex, UNIVERSE) & safeMobSquares;

                mobilityScore += mobilityScores[QUEEN - 2][populationCount(table)];

                if ((queen & enemyKingBigArea) != 0) {
                    attackingEnemyKingLookup[turn] += 4;
                }

                attackingEnemyKingLookup[turn] += populationCount(table & enemyKingSmallArea);
            }
            myQueens &= (myQueens - 1);
        }

        attackingEnemyKingLookup[turn] += populationCount(squaresMyPawnsThreaten & enemyKingSmallArea);

        myPawns = pieces[turn][PAWN] & ~ignoreThesePieces;
        attackingEnemyKingLookup[1 - turn] -= populationCount(myPawns & myKingSmallArea);

        while (myPawns != 0){
            final long pawn = getFirstPiece(myPawns);
            final int pawnIndex = numberOfTrailingZeros(pawn);
            positionScore += POSITION_SCORES[turn][PAWN][63 - pawnIndex];

            if ((pawn & enemyKingBigArea) != 0) {
                attackingEnemyKingLookup[turn] += 1;
            }

            myPawns &= myPawns - 1;
        }

        if (board.pieces[turn][QUEEN] == 0) {
            attackingEnemyKingLookup[turn] -= 2;
        }

        finalScore += positionScore;
        finalScore += mobilityScore;

        scoresForEPO[turn][EvalPrintObject.mobilityScore] = mobilityScore;
        scoresForEPO[turn][EvalPrintObject.positionScore] = positionScore;

        return finalScore;
    }

}
