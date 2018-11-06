package javacode.evaluation;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.chess.BitManipulations.squareCentredOnIndexNaive;
import static javacode.chessprogram.moveGeneration.PieceMoveKnight.singleKnightCaptures;
import static javacode.chessprogram.moveGeneration.PieceMoveKnight.singleKnightPushes;
import static javacode.chessprogram.moveGeneration.PieceMovePawns.masterPawnCapturesTable;

class Knight {

    private static final int KNIGHT_OUTPOST_BONUS = 25;
    private static final int KNIGHT_MOBILITY_SCORE = 3;
    private static final int KNIGHT_PROTECTOR_SCORE = 4;
    private static final int KNIGHT_AGGRESSOR_SCORE = 10;
    private static final int UNDEVELOPED_KNIGHT_PENALTY = -35;

    static int evalKnightByTurn(Chessboard board, boolean white) {
        long myKnights = white ? board.WHITE_KNIGHTS : board.BLACK_KNIGHTS;

        if (myKnights == 0) {
            return 0;
        }

        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        int score = 0;

        score += 
                unDevelopedKnights(board, white, myKnights)
                + knightOutpostBonus(board, white, myKnights, enemyPawns)
                + knightMobility(board, white, myKnights)
                + knightProtectorAndAggressor(board, white, myKnights)
        ;

        return score;
    }

    private static int unDevelopedKnights(Chessboard board, boolean white, long myKnights){
        long originalKnights = white ? WHITE_KNIGHTS : BLACK_KNIGHTS;
        return populationCount(originalKnights & myKnights)
                * UNDEVELOPED_KNIGHT_PENALTY;
    }

    private static int knightMobility(Chessboard board, boolean white, long myKnights){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKnights);
        long emptySquares = ~board.ALL_PIECES();
        long enemies = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int mobilitySquares = 0;
        for (Integer knightIndex : indexOfAllPieces) {
            long knight = BitManipulations.newPieceOnSquare(knightIndex);
            long pseudoAvailableSquares = singleKnightPushes(board, knight, white, emptySquares);
            mobilitySquares += populationCount(pseudoAvailableSquares);
        }
        return mobilitySquares * KNIGHT_MOBILITY_SCORE;
    }

    private static int knightProtectorAndAggressor(Chessboard board, boolean white, long myKnights){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKnights);
        long emptySquares = ~board.ALL_PIECES();
        long myPieces = white ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int protectedFriends = 0;
        int threatenedEnemies = 0;
        for (Integer knightIndex : indexOfAllPieces) {
            long knight = BitManipulations.newPieceOnSquare(knightIndex);
            long pseudoAttackedOrProtectedByKnight = singleKnightCaptures(board, knight, white, board.ALL_PIECES());

            protectedFriends += populationCount(pseudoAttackedOrProtectedByKnight & myPieces);
            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByKnight & enemyPieces);
        }
        return protectedFriends * KNIGHT_PROTECTOR_SCORE
                + threatenedEnemies * KNIGHT_AGGRESSOR_SCORE;
    }

    private static int knightOutpostBonus(Chessboard board, boolean white, long myKnights, long enemyPawns) {
        int score = 0;

        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKnights);
        for (Integer knightIndex : indexOfAllPieces) {
            long knight = BitManipulations.newPieceOnSquare(knightIndex);
            
            /*
            only consider outpost if they are in middle four ranks, and not on edges
             */
            if (((knight & noMansLand) == 0)
                    && ((knight & boardWithoutEdges) == 0)){
                continue;
            }
            
            /*
            if in centre files, only consider outpost if no enemy pawns can quickly threaten our knight
             */
            if ((knight & northSouthHighway) != 0) {
                if (white) {
                    if ((((knight << 7) & enemyPawns) != 0)
                            || (((knight << 9) & enemyPawns) != 0)
                            || (((knight << 17) & enemyPawns) != 0)
                            || ((knight << 18) & enemyPawns) != 0) {
                        continue;
                    }
                } else {
                    if ((((knight >>> 7) & enemyPawns) != 0)
                            || (((knight >>> 9) & enemyPawns) != 0)
                            || (((knight >>> 17) & enemyPawns) != 0)
                            || ((knight >>> 18) & enemyPawns) != 0) {
                        continue;
                    }
                }
            }

            long ignoreThesePieces = ~squareCentredOnIndexNaive(knightIndex);
            long pawnDefendingKnight = masterPawnCapturesTable(board, white, ignoreThesePieces, knight);
            if (pawnDefendingKnight != 0) {
                score += KNIGHT_OUTPOST_BONUS;
            }
        }
        return score;
    }

}