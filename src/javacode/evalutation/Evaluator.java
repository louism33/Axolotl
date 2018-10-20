package javacode.evalutation;

import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.List;

public class Evaluator {

    private static final int PAWN_SCORE = 100;
    private static final int KNIGHT_SCORE = 300;
    private static final int BISHOP_SCORE = 300;
    private static final int ROOK_SCORE = 500;
    private static final int QUEEN_SCORE = 900;

    public static final int IN_CHECKMATE_SCORE = -100000;
    private static final int IN_STALEMATE_SCORE = 0;

    public static int numberOfEvals = 0;
    private static int numberOfCheckmates = 0;
    private static int numberOfStalemates = 0;
    
    /*
    draw noticer
    
    material hash
    pawn hash
     */

    public static int eval(Chessboard board, boolean white, List<Move> moves) {
        if (moves.size() == 0){
            if (CheckChecker.boardInCheck(board, white)){
                numberOfCheckmates++;
//                System.out.println(Art.boardArt(board));
                return IN_CHECKMATE_SCORE;
            }
            else {
                numberOfStalemates++;
                return IN_STALEMATE_SCORE;
            }
        }
        else{
            return eval(board, white);
        }
    }

    public static int eval(Chessboard board, boolean white) {
        numberOfEvals++;

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, white);
        if (moves.size() == 0) {
            if (CheckChecker.boardInCheck(board, white)) {
                numberOfCheckmates++;
//                System.out.println(Art.boardArt(board));
                return IN_CHECKMATE_SCORE;
            } else {
                numberOfStalemates++;
                return IN_STALEMATE_SCORE;
            }
        }
        return evalTurn(board, white) - evalTurn(board, !white);
//        return 0;
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
