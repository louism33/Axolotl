package javacode.evalutation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.List;

import static javacode.chessengine.Engine.*;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;

public class Evaluator {

    private static final int PAWN_SCORE = 100;
    private static final int KNIGHT_SCORE = 300;
    private static final int BISHOP_SCORE = 300;
    private static final int ROOK_SCORE = 500;
    private static final int QUEEN_SCORE = 900;

    public static final int IN_CHECKMATE_SCORE = -100000;
    public static final int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static final int IN_STALEMATE_SCORE = 0;

    public static int eval(Chessboard board, boolean white, List<Move> moves) {
        if (moves == null){
            moves = MoveGeneratorMaster.generateLegalMoves(board, white);
        }
        
        if (DEBUG) {
            numberOfEvals++;
        }

        if (moves.size() == 0){
            if (boardInCheck(board, white)) {
                if (DEBUG) {
                    numberOfCheckmates++;
                }
                return IN_CHECKMATE_SCORE;
            }
            else {
                if (DEBUG) {
                    numberOfStalemates++;
                }
                return IN_STALEMATE_SCORE;
            }
        }
        else{
            return evalHelper(board, white);
        }
    }

    private static int evalHelper(Chessboard board, boolean white) {
        return evalTurn(board, white) - evalTurn(board, !white);
    }

    private static int evalTurn (Chessboard board, boolean white){
        int score = 0;
        score += pawnScores(board, white)
                +knightScores(board, white)
                +bishopScores(board, white)
                +rookScores(board, white)
                +queenScores(board, white)
        ;

        return score;
    }

    /*
    material scores
     */
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
