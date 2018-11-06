package javacode.evaluation;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.RANK_SEVEN;
import static javacode.chessprogram.bitboards.BitBoards.RANK_TWO;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.moveGeneration.PieceMoveSliding.singleQueenCaptures;
import static javacode.chessprogram.moveGeneration.PieceMoveSliding.singleQueenPushes;

class Queen {

    private static final int QUEEN_ON_SEVENTH_BONUS = 15;
    private static final int QUEEN_MOBILITY_SCORE = 1;
    private static final int QUEEN_PROTECTOR_SCORE = 2;
    private static final int QUEEN_AGGRESSOR_SCORE = 5;
    private static final int QUEEN_PROTECTS_ROOK = 10;

    static int evalQueenByTurn(Chessboard board, boolean white) {
        long myQueens = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;

        if (myQueens == 0) {
            return 0;
        }

        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        int score = 0;

        score += queenOnSeventhRank(board, white, myQueens)
                + queenMobility(board, white, myQueens)
                + queenProtectorAndAggressor(board, white, myQueens)
                + queensHelpRooksAndQueens(board, white, myQueens)
        ;

        return score;
    }

    private static int queensHelpRooksAndQueens(Chessboard board, boolean white, long myQueens){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myQueens);
        long myRooks = white ? board.WHITE_ROOKS : board.BLACK_ROOKS;
        long emptySquares = ~board.ALL_PIECES();

        int score = 0;
        for (Integer queenIndex : indexOfAllPieces) {
            long queen = BitManipulations.newPieceOnSquare(queenIndex);
            long pseudoAvailableSquares = singleQueenPushes(board, queen, white, myRooks | myQueens);
            score += populationCount(pseudoAvailableSquares) * QUEEN_PROTECTS_ROOK;
        }
        return score;
    }

    private static int queenMobility(Chessboard board, boolean white, long myQueens){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myQueens);
        long emptySquares = ~board.ALL_PIECES();

        int mobilitySquares = 0;
        for (Integer queenIndex : indexOfAllPieces) {
            long queen = BitManipulations.newPieceOnSquare(queenIndex);
            long pseudoAvailableSquares = singleQueenPushes(board, queen, white, emptySquares);
            mobilitySquares += populationCount(pseudoAvailableSquares);
        }
        return mobilitySquares * QUEEN_MOBILITY_SCORE;
    }

    private static int queenProtectorAndAggressor(Chessboard board, boolean white, long myQueens){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myQueens);
        long emptySquares = ~board.ALL_PIECES();
        long myPieces = white ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int protectedFriends = 0;
        int threatenedEnemies = 0;
        for (Integer queenIndex : indexOfAllPieces) {
            long queen = BitManipulations.newPieceOnSquare(queenIndex);
            long pseudoAttackedOrProtectedByQueen = singleQueenCaptures(board, queen, white, board.ALL_PIECES());

            protectedFriends += populationCount(pseudoAttackedOrProtectedByQueen & myPieces);
            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByQueen & enemyPieces);
        }
        return protectedFriends * QUEEN_PROTECTOR_SCORE
                + threatenedEnemies * QUEEN_AGGRESSOR_SCORE;
    }


    private static int queenOnSeventhRank(Chessboard board, boolean white, long myQueens){
        long seventhRank = white ? RANK_SEVEN : RANK_TWO;
        int numberOfQueensOnSeventh = populationCount(myQueens & seventhRank);
        return numberOfQueensOnSeventh * QUEEN_ON_SEVENTH_BONUS;
    }
}
