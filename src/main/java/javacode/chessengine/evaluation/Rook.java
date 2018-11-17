package javacode.chessengine.evaluation;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.moveGeneration.PieceMoveSliding;

import java.util.List;

import static javacode.chessengine.evaluation.EvaluationConstants.*;
import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.moveGeneration.PieceMoveSliding.singleRookCaptures;

class Rook {

    static int evalRookByTurn(Chessboard board, boolean white) {
        long myRooks = white ? board.WHITE_ROOKS : board.BLACK_ROOKS;

        if (myRooks == 0) {
            return 0;
        }

        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        int score = 0;

        score += 
//                unDevelopedRooks(board, white, myRooks)
                + rookOnSeventhRank(board, white, myRooks)
//                + rookMobility(board, white, myRooks)
//                + rookProtectorAndAggressor(board, white, myRooks)
//                + rookOnOpenFile(board, white, myRooks, myPawns, enemyPawns)
//                + rookHelpsQueensAndRooks(board, white, myRooks)
        ;

        return score;
    }

    private static int rookHelpsQueensAndRooks(Chessboard board, boolean white, long myRooks){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myRooks);
        long myQueens = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;
        long emptySquares = ~board.ALL_PIECES();

        int score = 0;
        for (Integer rookIndex : indexOfAllPieces) {
            long queen = BitManipulations.newPieceOnSquare(rookIndex);
            long pseudoAvailableSquares = singleRookCaptures(board, queen, white, myRooks | myQueens);
            score += populationCount(pseudoAvailableSquares) * ROOK_PROTECTS_QUEEN;
        }
        return score;
    }

    private static int rookOnOpenFile(Chessboard board, boolean white, long myRooks, long myPawns, long enemyPawns){
        int fileScore = 0;
        for (long file : BitBoards.FILES){
            if ((file & myPawns) != 0){
                continue;
            }
            if ((file & enemyPawns) != 0){
                fileScore += ROOK_ON_SEMI_OPEN_FILE_BONUS;
                continue;
            }
            fileScore += ROOK_OPEN_FILE_BONUS;
        }
        return fileScore;
    }

    private static int unDevelopedRooks(Chessboard board, boolean white, long myRooks){
        long originalRooks = white ? WHITE_ROOKS : BLACK_ROOKS;
        return populationCount(originalRooks & myRooks)
                * ROOK_NOT_DEVELOPED;
    }


    private static int rookMobility(Chessboard board, boolean white, long myRooks){
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(myRooks);
        long emptySquares = ~board.ALL_PIECES();
        long enemies = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int mobilitySquares = 0;
        for (Integer rookIndex : indexOfAllPieces) {
            long rook = BitManipulations.newPieceOnSquare(rookIndex);
            long pseudoAvailableSquares = PieceMoveSliding.singleRookPushes(board, rook, white, emptySquares);
            mobilitySquares += BitIndexing.populationCount(pseudoAvailableSquares);
        }
        return mobilitySquares * ROOK_MOBILITY_SCORE;
    }

    private static int rookProtectorAndAggressor(Chessboard board, boolean white, long myRooks){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myRooks);
        long emptySquares = ~board.ALL_PIECES();
        long myPieces = white ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int protectedFriends = 0;
        int threatenedEnemies = 0;
        for (Integer rookIndex : indexOfAllPieces) {
            long rook = BitManipulations.newPieceOnSquare(rookIndex);
            long pseudoAttackedOrProtectedByRook = singleRookCaptures(board, rook, white, board.ALL_PIECES());

            protectedFriends += populationCount(pseudoAttackedOrProtectedByRook & myPieces);
            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByRook & enemyPieces);
        }
        return protectedFriends * ROOK_PROTECTOR_SCORE
                + threatenedEnemies * ROOK_AGGRESSOR_SCORE;
    }

    private static int rookOnSeventhRank(Chessboard board, boolean white, long myRooks){
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
        int numberOfRooksOnSeventh = BitIndexing.populationCount(myRooks & seventhRank);

        return numberOfRooksOnSeventh > 1 ? (1 + numberOfRooksOnSeventh) * ROOK_ON_SEVENTH_BONUS
                : numberOfRooksOnSeventh * ROOK_ON_SEVENTH_BONUS;
    }
}
