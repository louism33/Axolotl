package javacode.evaluation;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.graphicsandui.Art;

import java.util.List;

import static javacode.chessengine.Engine.*;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.evaluation.Bishop.evalBishopByTurn;
import static javacode.evaluation.King.evalKingByTurn;
import static javacode.evaluation.Knight.evalKnightByTurn;
import static javacode.evaluation.MaterialEval.evalMaterialByTurn;
import static javacode.evaluation.Misc.evalMiscByTurn;
import static javacode.evaluation.Pawns.evalPawnsByTurn;
import static javacode.evaluation.PositionEval.evalPositionByTurn;
import static javacode.evaluation.Queen.evalQueenByTurn;
import static javacode.evaluation.Rook.evalRookByTurn;

public class Evaluator {

    static final int PAWN_SCORE = 100;
    static final int KNIGHT_SCORE = 300;
    static final int BISHOP_SCORE = 300;
    static final int ROOK_SCORE = 500;
    static final int QUEEN_SCORE = 900;
    static final int KING_SCORE = 3000;

    public static final int SHORT_MINIMUM = -31000;
    public static final int SHORT_MAXIMUM = 31000;
    public static final int IN_CHECKMATE_SCORE = -30000;
    public static final int CHECKMATE_ENEMY_SCORE = -IN_CHECKMATE_SCORE;
    public static final int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static final int CHECKMATE_ENEMY_SCORE_MAX_PLY = -IN_CHECKMATE_SCORE_MAX_PLY;
    public static final int IN_STALEMATE_SCORE = 0;


    public static int evalWithoutCM(Chessboard board, boolean white, List<Move> moves) {
        return evalHelper(board, white, moves);
    }
    
    public static int eval(Chessboard board, boolean white, List<Move> moves) {
        if (moves == null){
            moves = generateLegalMoves(board, white);
        }

        if (DEBUG) {
            statistics.numberOfEvals++;
        }

        if (moves.size() == 0){
            if (boardInCheck(board, white)) {
                if (DEBUG) {
                    statistics.numberOfCheckmates++;
                }
                return IN_CHECKMATE_SCORE;
            }
            else {
                if (DEBUG) {
                    statistics.numberOfStalemates++;
                }
                return IN_STALEMATE_SCORE;
            }
        }
        else{
            return evalHelper(board, white, moves);
        }
    }

    private static int evalHelper(Chessboard board, boolean white, List<Move> moves) {
        return evalTurn(board, white, moves) - evalTurn(board, !white, moves);
    }
    
    public static void main (String[] args){
//        Chessboard board = FenParser.makeBoardBasedOnFEN("2r3k1/p2R1p2/1p5Q/4N3/7P/4P3/b5PK/5q2 w - - 1 0");
        Chessboard board = new Chessboard();
        System.out.println(Art.boardArt(board));

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        int evalTurnW = evalTurn(board, board.isWhiteTurn(), moves);
        System.out.println(evalTurnW);

        int evalTurnB = evalTurn(board, !board.isWhiteTurn(), moves);
        System.out.println(evalTurnB);

        int x = evalHelper(board, board.isWhiteTurn(), moves);
        System.out.println(x);



    }

    private static int evalTurn (Chessboard board, boolean white, List<Move> moves){
        int score = 0;
        score += 
                evalMaterialByTurn(board, white)
                + evalPositionByTurn(board, white)
                + evalPawnsByTurn(board, white)
                + evalBishopByTurn(board, white)
                + evalKnightByTurn(board, white)
                + evalRookByTurn(board, white)
                + evalQueenByTurn(board, white)
                + evalKingByTurn(board, white)
                + evalMiscByTurn(board, white, moves)
        ;
        return score;
    }


}
