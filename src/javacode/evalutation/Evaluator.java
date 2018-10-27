package javacode.evalutation;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.List;

import static javacode.chessengine.Engine.*;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.evalutation.Bishop.evalBishopByTurn;
import static javacode.evalutation.King.evalKingByTurn;
import static javacode.evalutation.Knight.evalKnightByTurn;
import static javacode.evalutation.MaterialEval.evalMaterialByTurn;
import static javacode.evalutation.Misc.*;
import static javacode.evalutation.PositionEval.evalPositionByTurn;
import static javacode.evalutation.Queen.evalQueenByTurn;
import static javacode.evalutation.Rook.evalRookByTurn;

public class Evaluator {

    static final int PAWN_SCORE = 100;
    static final int KNIGHT_SCORE = 300;
    static final int BISHOP_SCORE = 300;
    static final int ROOK_SCORE = 500;
    static final int QUEEN_SCORE = 900;

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
            return evalHelper(board, white, moves);
        }
    }

    private static int evalHelper(Chessboard board, boolean white, List<Move> moves) {
        return evalTurn(board, white, moves) - evalTurn(board, !white, moves);
    }

    private static int evalTurn (Chessboard board, boolean white, List<Move> moves){
        int score = 0;
        score += evalMaterialByTurn(board, white)
                + evalPositionByTurn(board, white)

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
