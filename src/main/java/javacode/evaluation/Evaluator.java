package javacode.evaluation;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import org.junit.Assert;

import java.util.List;

import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.evaluation.Bishop.evalBishopByTurn;
import static javacode.evaluation.Knight.evalKnightByTurn;
import static javacode.evaluation.Misc.evalMiscByTurn;
import static javacode.evaluation.Pawns.evalPawnsByTurn;
import static javacode.evaluation.PositionEval.evalPositionByTurn;
import static javacode.evaluation.Queen.evalQueenByTurn;
import static javacode.evaluation.Rook.evalRookByTurn;

public class Evaluator {

    private final Engine engine;
    private final MaterialEval materialEval;
    
    public Evaluator(Engine engine){
        this.engine = engine;
        this.materialEval = new MaterialEval(this);
    }

    public final int PAWN_SCORE = 100;
    public final int KNIGHT_SCORE = 300;
    public final int BISHOP_SCORE = 310;
    public final int ROOK_SCORE = 500;
    public final int QUEEN_SCORE = 900;
    public final int KING_SCORE = 3000;

    public final int SHORT_MINIMUM = -31000;
    public final int SHORT_MAXIMUM = 31000;
    public static final int IN_CHECKMATE_SCORE = -30000;
    public static final int CHECKMATE_ENEMY_SCORE = -IN_CHECKMATE_SCORE;
    public static final int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static final int CHECKMATE_ENEMY_SCORE_MAX_PLY = -IN_CHECKMATE_SCORE_MAX_PLY;
    public static final int IN_STALEMATE_SCORE = 0;

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

        if (this.engine.INFO_LOG) {
//            this.engine.statistics.numberOfEvals++;
        }

        if (moves.size() == 0){
            if (boardInCheck(board, white)) {
                if (this.engine.INFO_LOG) {
                    this.engine.statistics.numberOfCheckmates++;
                }
                return IN_CHECKMATE_SCORE;
            }
            else {
                if (this.engine.INFO_LOG) {
                    this.engine.statistics.numberOfStalemates++;
                }
                return IN_STALEMATE_SCORE;
            }
        }
        else{
            if (this.engine.HEAVY_DEBUG){
                Assert.assertEquals(evalHelper(board, white, moves), -evalHelper(board, !white, moves));
            }

            Assert.assertEquals(evalHelper(board, white, moves), -evalHelper(board, !white, moves));
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
//                + evalKingByTurn(board, white)
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
