package javacode.evaluation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.chess.BitManipulations.squareCentredOnIndexNaive;
import static javacode.chessprogram.moveGeneration.PieceMovePawns.masterPawnCapturesTable;
import static javacode.chessprogram.moveGeneration.PieceMoveSliding.singleBishopCaptures;
import static javacode.chessprogram.moveGeneration.PieceMoveSliding.singleBishopPushes;

class Bishop {

    private static final int PER_ENEMY_PAWN_COLOUR_MODIFIER = 5;
    private static final int PER_FRIENDLY_PAWN_COLOUR_MODIFIER = 3;
    private static final int DOUBLE_BISHOP_BONUS = 15;
    private static final int BISHOP_OUTPOST_BONUS = 20;
    private static final int BISHOP_MOBILITY_SCORE = 1;
    private static final int BISHOP_PROTECTOR_SCORE = 2;
    private static final int BISHOP_AGGRESSOR_SCORE = 5;
    private static final int UNDEVELOPED_BISHOP_PENALTY = -20;
    private static final int PRIME_DIAGONAL_BONUS = 20;

    static int evalBishopByTurn(Chessboard board, boolean white){
        long myBishops = white ? board.WHITE_BISHOPS : board.BLACK_BISHOPS;
        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        if (myBishops == 0){
            return 0;
        }

        int score = 0;
        if (populationCount(myBishops) == 1){
            long bishopSquares = ((WHITE_SQUARES & myBishops) != 0) ?
                    WHITE_SQUARES : BLACK_SQUARES;

            score += bishopEnemyPawnColourScore(myBishops, enemyPawns, bishopSquares)
                    + bishopFriendlyPawnColourScore(board, white, myBishops, myPawns, bishopSquares)
            ;
        }

        score += unDevelopedBishops(board, white, myBishops)
                + bishopMobility(board, white, myBishops)
                + bishopProtectorAndAggressor(board, white, myBishops)
                + doubleBishopScore(myBishops)
                + bishopOutpostBonus(board, white, myBishops, enemyPawns)
                + primeDiagonals(board, white, myBishops)
        ;

        return score;
    }

    private static int unDevelopedBishops(Chessboard board, boolean white, long myBishops){
        long originalBishops = white ? WHITE_BISHOPS : BLACK_BISHOPS;
        return BitIndexing.populationCount(originalBishops & myBishops)
                * UNDEVELOPED_BISHOP_PENALTY;
    }

    private static int primeDiagonals(Chessboard board, boolean white, long myBishops){
        return populationCount(myBishops & (DIAGONAL_SW_NE | DIAGONAL_NW_SE)) * PRIME_DIAGONAL_BONUS;
    }

    private static int bishopMobility(Chessboard board, boolean white, long myBishops){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myBishops);
        long emptySquares = ~board.ALL_PIECES();
        long enemies = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int mobilitySquares = 0;
        for (Integer bishopIndex : indexOfAllPieces) {
            long bishop = BitManipulations.newPieceOnSquare(bishopIndex);
            long pseudoAvailableSquares = singleBishopPushes(board, bishop, white, emptySquares);
            mobilitySquares += populationCount(pseudoAvailableSquares);
        }
        return mobilitySquares * BISHOP_MOBILITY_SCORE;
    }

    private static int bishopProtectorAndAggressor(Chessboard board, boolean white, long myBishops){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myBishops);
        long emptySquares = ~board.ALL_PIECES();
        long myPieces = white ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int protectedFriends = 0;
        int threatenedEnemies = 0;
        for (Integer bishopIndex : indexOfAllPieces) {
            long bishop = BitManipulations.newPieceOnSquare(bishopIndex);
            long pseudoAttackedOrProtectedByBishop = singleBishopCaptures(board, bishop, white, board.ALL_PIECES());

            protectedFriends += populationCount(pseudoAttackedOrProtectedByBishop & myPieces);
            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByBishop & enemyPieces);
        }
        return protectedFriends * BISHOP_PROTECTOR_SCORE
                + threatenedEnemies * BISHOP_AGGRESSOR_SCORE;
    }

    private static int bishopOutpostBonus(Chessboard board, boolean white, long myBishops, long enemyPawns) {
        int score = 0;

        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myBishops);
        for (Integer bishopIndex : indexOfAllPieces) {
            long bishop = newPieceOnSquare(bishopIndex);
            
            /*
            only consider outpost if they are in middle four ranks, and not on edges
             */
            if (((bishop & noMansLand) == 0)
                    && ((bishop & boardWithoutEdges) == 0)){
                continue;
            }
            
            /*
            if in centre files, only consider outpost if no enemy pawns can quickly threaten our bishop
             */
            if ((bishop & northSouthHighway) != 0) {
                if (white) {
                    if ((((bishop << 7) & enemyPawns) != 0)
                            || (((bishop << 9) & enemyPawns) != 0)
                            || (((bishop << 17) & enemyPawns) != 0)
                            || ((bishop << 18) & enemyPawns) != 0) {
                        continue;
                    }
                } else {
                    if ((((bishop >>> 7) & enemyPawns) != 0)
                            || (((bishop >>> 9) & enemyPawns) != 0)
                            || (((bishop >>> 17) & enemyPawns) != 0)
                            || ((bishop >>> 18) & enemyPawns) != 0) {
                        continue;
                    }
                }
            }

            long ignoreThesePieces = ~squareCentredOnIndexNaive(bishopIndex);
            long pawnDefendingBishop = masterPawnCapturesTable(board, white, ignoreThesePieces, bishop);
            if (pawnDefendingBishop != 0) {
                score += BISHOP_OUTPOST_BONUS;
            }
        }
        return score;
    }



    private static int doubleBishopScore (long bishops){
        return populationCount(bishops) > 1 ? DOUBLE_BISHOP_BONUS : 0;
    }

    private static int bishopEnemyPawnColourScore(long myBishop, long enemyPawns, long bishopSquares){
        return (populationCount(enemyPawns & ~bishopSquares)
                - populationCount(enemyPawns & bishopSquares))
                * PER_ENEMY_PAWN_COLOUR_MODIFIER;
    }

    private static int bishopFriendlyPawnColourScore(Chessboard board, boolean white,
                                                     long myBishop, long friendlyPawns, long bishopSquares){


        /*
        if not winning, prefer my pawns on same square as my bishop
         */
        int score = (populationCount(friendlyPawns & bishopSquares)
                - populationCount(friendlyPawns & ~bishopSquares));

        /*
        if winning, prefer bishop mobility over pawn protection
         */
        if (iAmWinningUgly(board, white)){
            score *= -1;
        }
        return score * PER_FRIENDLY_PAWN_COLOUR_MODIFIER;

    }

    private static boolean iAmWinningUgly (Chessboard board, boolean white){
        /*
        rough guide to whether I am ahead or not
         */
        if (white){
            return populationCount(board.ALL_WHITE_PIECES()) > board.ALL_BLACK_PIECES() + 3;
        }
        else {
            return populationCount(board.ALL_BLACK_PIECES()) > board.ALL_WHITE_PIECES() + 3;
        }
    }

}
