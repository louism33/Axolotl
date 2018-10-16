package javacode.evalutation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import java.util.Random;

public class Evaluator {

    private static final int PAWN_SCORE = 100;
    private static final int KNIGHT_SCORE = 300;
    private static final int BISHOP_SCORE = 300;
    private static final int ROOK_SCORE = 500;
    private static final int QUEEN_SCORE = 900;

    public static int numberOfEvals = 0;

    public static int eval(Chessboard board, boolean white){
        numberOfEvals++;
        return new Random().nextInt(100);
//        return evalTurn(board, white) - evalTurn(board, !white);
    }

    private static int evalTurn (Chessboard board, boolean white){
        int score = 0;
//        score += pawnScores(board, white)
//                +knightScores(board, white)
//                +bishopScores(board, white)
//                +rookScores(board, white)
//                +queenScores(board, white)
//        ;

        return score;
    }

    private static int pawnScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        int numberOfPawns = BitIndexing.populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

    private static int knightScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_KNIGHTS : board.BLACK_KNIGHTS;
        int numberOfPawns = BitIndexing.populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

    private static int bishopScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_BISHOPS : board.BLACK_BISHOPS;
        int numberOfPawns = BitIndexing.populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

    private static int rookScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_ROOKS : board.BLACK_ROOKS;
        int numberOfPawns = BitIndexing.populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

    private static int queenScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;
        int numberOfPawns = BitIndexing.populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

}
