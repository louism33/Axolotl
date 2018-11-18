package javacode.chessengine.evaluation;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.List;

import static javacode.chessengine.evaluation.Bishop.evalBishopByTurn;
import static javacode.chessengine.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE;
import static javacode.chessengine.evaluation.EvaluationConstants.IN_STALEMATE_SCORE;
import static javacode.chessengine.evaluation.King.evalKingByTurn;
import static javacode.chessengine.evaluation.Knight.evalKnightByTurn;
import static javacode.chessengine.evaluation.Misc.evalMiscByTurn;
import static javacode.chessengine.evaluation.Pawns.evalPawnsByTurn;
import static javacode.chessengine.evaluation.PositionEval.evalPositionByTurn;
import static javacode.chessengine.evaluation.Queen.evalQueenByTurn;
import static javacode.chessengine.evaluation.Rook.evalRookByTurn;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;

public class Evaluator {

    private final Engine engine;
    private final MaterialEval materialEval;

    public Evaluator(Engine engine){
        this.engine = engine;
        this.materialEval = new MaterialEval(this);
    }

    public int lazyEval(Chessboard board, boolean white) {
        return lazyEvalHelper(board, white);
    }

    public int evalWithoutCM(Chessboard board, boolean white, List<Move> moves) {
        return evalHelper(board, white, moves);
    }

    public int eval(Chessboard board, boolean white, List<Move> moves) {
        if (moves == null){
            moves = generateLegalMoves(board, white);
        }

        this.engine.statistics.numberOfEvals++;

        if (moves.size() == 0){
            if (boardInCheck(board, white)) {
                this.engine.statistics.numberOfCheckmates++;
                return IN_CHECKMATE_SCORE;
            }
            else {
                this.engine.statistics.numberOfStalemates++;
                return IN_STALEMATE_SCORE;
            }
        }
        else{
            return evalHelper(board, white, moves);
        }
    }

    private boolean naiveEndgame (Chessboard board){
        return BitIndexing.populationCount(board.ALL_PIECES()) < 8;
    }

    private int evalHelper(Chessboard board, boolean white, List<Move> moves) {
        return evalTurn(board, white, moves) - evalTurn(board, !white, moves);
    }

    private int evalTurn (Chessboard board, boolean white, List<Move> moves){
        int score = 0;
        score +=
                this.materialEval.evalMaterialByTurn(board, white)

                        + evalPositionByTurn(board, white, naiveEndgame(board))
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

    private int lazyEvalHelper(Chessboard board, boolean white) {
        return this.materialEval.evalMaterialByTurn(board, white) - this.materialEval.evalMaterialByTurn(board, !white);
    }

    public int getScoreOfDestinationPiece(Chessboard board, Move move){
        return this.materialEval.getScoreOfDestinationPiece(board, move);
    }


}
