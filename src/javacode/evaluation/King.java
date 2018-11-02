package javacode.evaluation;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import org.junit.Assert;

import java.util.List;

import static javacode.chessprogram.chess.BitIndexing.getIndexOfAllPieces;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.moveGeneration.PieceMoveKing.singleKingCaptures;

class King {

    private static int KING_PAWN_PROTECT_BONUS = 5;
    private static final int KING_PROTECTOR_SCORE = 20;
    private static final int KING_AGGRESSOR_SCORE = 3;
    
    static int evalKingByTurn(Chessboard board, boolean white) {
        long myKing = white ? board.WHITE_KING : board.BLACK_KING;

        Assert.assertEquals(1, populationCount(myKing));

        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        int score = 0;

        score += kingSafetyBonus(board, white, myKing)
                + kingProtectorAndAggressor(board, white, myKing)
        ;

        return score;
    }
    
    private static int kingSafetyBonus(Chessboard board, boolean white, long myKing){
        int score = 0;
        return score;
    }

    private static int kingProtectorAndAggressor(Chessboard board, boolean white, long myKing){
        List<Integer> indexOfAllPieces = getIndexOfAllPieces(myKing);
        long emptySquares = ~board.ALL_PIECES();
        long myPieces = white ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = white ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        int protectedFriends = 0;
        int threatenedEnemies = 0;
        for (Integer kingIndex : indexOfAllPieces) {
            long king = BitManipulations.newPieceOnSquare(kingIndex);
            long pseudoAttackedOrProtectedByKing = singleKingCaptures(board, king, white, board.ALL_PIECES());
            
            protectedFriends += populationCount(pseudoAttackedOrProtectedByKing & myPieces);
            threatenedEnemies += populationCount(pseudoAttackedOrProtectedByKing & enemyPieces);
        }
        return protectedFriends * KING_PROTECTOR_SCORE
                + threatenedEnemies * KING_AGGRESSOR_SCORE;
    }
}










