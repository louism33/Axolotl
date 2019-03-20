package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.POSITION_SCORES;
import static com.github.louism33.axolotl.evaluation.MoveTable.populateFromMoves;
import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.*;
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
    static void populateThreats(Chessboard board, int[] moves){
        populateFromMoves(moves);
//        board.flipTurn();
//        populateFromMoves(board.generateLegalMoves());
//        board.flipTurn();
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

        // basic mobility
        int score = evalTurn(board, board.turn) - evalTurn(board, 1 - board.turn);
        score += moves[moves.length - 1];

//        long pinnedPieces = board.pinnedPieces;
//        score -= populationCount(pinnedPieces) * 15;

//        score += PinnedPieces.evalPinnedPieces(board);

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

        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                final int knightIndex = numberOfTrailingZeros(knight);
                positionScore += POSITION_SCORES[turn][KNIGHT][knightIndex];
            }
            myKnights &= (myKnights - 1);
        }

        while (myBishops != 0){
            final long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                final int bishopIndex = numberOfTrailingZeros(bishop);
                positionScore += POSITION_SCORES[turn][BISHOP][bishopIndex];
            }
            myBishops &= (myBishops - 1);
        }

        while (myRooks != 0){
            final long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                final int rookIndex = numberOfTrailingZeros(rook);
                positionScore += POSITION_SCORES[turn][ROOK][rookIndex];
            }
            myRooks &= (myRooks - 1);
        }

        while (myQueens != 0){
            final long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                final int queenIndex = numberOfTrailingZeros(queen);
                positionScore += POSITION_SCORES[turn][QUEEN][queenIndex];
            }
            myQueens &= (myQueens - 1);
        }

        myPawns = pieces[turn][PAWN] & ~ignoreThesePieces;
        while (myPawns != 0){
            final long pawn = getFirstPiece(myPawns);
            final int pawnIndex = numberOfTrailingZeros(pawn);
            positionScore += POSITION_SCORES[turn][PAWN][pawnIndex];
            myPawns &= myPawns - 1;
        }

        finalScore += positionScore;

        scoresForEPO[turn][EvalPrintObject.positionScore] = positionScore;
        scoresForEPO[turn][EvalPrintObject.totalScore] = finalScore;

        return finalScore;
    }



    public static boolean naiveEndgame (Chessboard board){
        if (isEndgame){
            return true;
        }
        return false;
//        if (board.getWhiteQueen() == 0 && board.getBlackQueen() == 0){
//            isEndgame = true;
//        }
//        else if (board.getWhiteQueen() == 1
//                && populationCount(board.getWhiteKnights()
//                | board.getWhiteBishops()
//                | board.getWhiteRooks()) == 1){
//            isEndgame = true;
//        }
//        else if (board.getBlackQueen() == 1
//                && populationCount(board.getBlackKnights()
//                | board.getBlackBishops()
//                | board.getBlackRooks()) == 1){
//            isEndgame = true;
//        }
//
//        return isEndgame;
    }


}
