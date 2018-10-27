package javacode.evalutation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import static javacode.evalutation.Evaluator.*;

class MaterialEval {

    static int evalMaterialByTurn(Chessboard board, boolean white){
        int score = 0;
        score += pawnScores(board, white)
                +knightScores(board, white)
                +bishopScores(board, white)
                +rookScores(board, white)
                +queenScores(board, white)
        ;

        return score;
    }
    
    private static int pawnScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        int numberOfPawns = BitIndexing.populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

    private static int knightScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_KNIGHTS : board.BLACK_KNIGHTS;
        int numberOfKnights = BitIndexing.populationCount(myPieces);
        return numberOfKnights * KNIGHT_SCORE;
    }

    private static int bishopScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_BISHOPS : board.BLACK_BISHOPS;
        int numberOfBishops = BitIndexing.populationCount(myPieces);
        return numberOfBishops * BISHOP_SCORE;
    }

    private static int rookScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_ROOKS : board.BLACK_ROOKS;
        int numberOfRooks = BitIndexing.populationCount(myPieces);
        return numberOfRooks * ROOK_SCORE;
    }

    private static int queenScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;
        int numberOfQueens = BitIndexing.populationCount(myPieces);
        return numberOfQueens * QUEEN_SCORE;
    }

}
