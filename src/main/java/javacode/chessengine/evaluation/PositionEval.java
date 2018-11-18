package javacode.chessengine.evaluation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessengine.evaluation.EvaluatorPositionConstant.*;

class PositionEval {

    static int evalPositionByTurn(Chessboard board, boolean white, boolean naiveEndgame){
        long pawns, knights, bishops, rooks, queens, kings;
        if (white){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
            kings = board.WHITE_KING;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
            kings = board.BLACK_KING;
        }

        return piecePositionScores(pawns, white, PAWN_POSITION_SCORES)
                + piecePositionScores(knights, white, KNIGHT_POSITION_SCORES)
                + piecePositionScores(bishops, white, BISHOP_POSITION_SCORES)
                + piecePositionScores(rooks, white, ROOK_POSITION_SCORES)
                + piecePositionScores(queens, white, QUEEN_POSITION_SCORES)
                + piecePositionScores(kings, white, naiveEndgame ? KING_POSITION_SCORES_END : KING_POSITION_SCORES_START)
                ;
    }

    private static int piecePositionScores(long pieces, boolean white, int[] whichArray){
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(pieces);
        int answer = 0;
        for (Integer piece : indexOfAllPieces){
            if (white){
                piece = 63 - piece;
            }
            answer += whichArray[piece];
        }
        return answer;
    }

   
}
