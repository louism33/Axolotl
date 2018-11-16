package javacode.chessengine.evaluation;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import org.junit.Assert;

import java.util.List;

import static javacode.chessengine.evaluation.Bishop.evalBishopByTurn;
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

    private Engine engine;
    private MaterialEval materialEval;
    
    public Evaluator(Engine engine){
        this.engine = engine;
        this.materialEval = new MaterialEval(this);
    }

    public int PAWN_SCORE = 100;
    public int KNIGHT_SCORE = 300;
    public int BISHOP_SCORE = 310;
    public int ROOK_SCORE = 500;
    public int QUEEN_SCORE = 900;
    public int KING_SCORE = 3000;

    public int SHORT_MINIMUM = -31000;
    public int SHORT_MAXIMUM = 31000;
    public static int IN_CHECKMATE_SCORE = -30000;
    public static int CHECKMATE_ENEMY_SCORE = -IN_CHECKMATE_SCORE;
    public static int IN_CHECKMATE_SCORE_MAX_PLY = IN_CHECKMATE_SCORE + 100;
    public static int CHECKMATE_ENEMY_SCORE_MAX_PLY = -IN_CHECKMATE_SCORE_MAX_PLY;
    public static int IN_STALEMATE_SCORE = 0;

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
