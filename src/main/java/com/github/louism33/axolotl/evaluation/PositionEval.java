package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.*;

class PositionEval {

    static int evalPositionByTurn(Chessboard board, boolean white, boolean naiveEndgame){
        long pawns, knights, bishops, rooks, queens, kings;
        if (white){
            pawns = board.getWhitePawns();
            knights = board.getWhiteKnights();
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
            kings = board.getWhiteKing();
        }
        else {
            pawns = board.getBlackPawns();
            knights = board.getBlackKnights();
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
            kings = board.getBlackKing();
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
        int answer = 0;

        while (pieces != 0){
            int piece = BitOperations.getIndexOfFirstPiece(pieces);
            if (white){
                piece = 63 - piece;
            }
            answer += whichArray[piece];
            pieces &= pieces - 1;
        }

        return answer;
    }


}
