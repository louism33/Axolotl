package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;

class MaterialEval {

    static int getScoreOfDestinationPiece(Chessboard board, int move){
        long victim = BitOperations.newPieceOnSquare(MoveParser.getDestinationIndex(move));
        if (MoveParser.isPromotionToQueen(move)){
            return QUEEN_SCORE;
        }
        if ((victim & board.getWhitePawns()) != 0 || (victim & board.getBlackPawns()) != 0){
            return PAWN_SCORE;
        }
        if ((victim & board.getWhiteKnights()) != 0 || (victim & board.getBlackKnights()) != 0){
            return KNIGHT_SCORE;
        }
        if ((victim & board.getWhiteBishops()) != 0 || (victim & board.getBlackBishops()) != 0){
            return BISHOP_SCORE;
        }
        if ((victim & board.getWhiteRooks()) != 0 || (victim & board.getBlackRooks()) != 0){
            return ROOK_SCORE;
        }
        if ((victim & board.getWhiteQueen()) != 0 || (victim & board.getBlackQueen()) != 0){
            return QUEEN_SCORE;
        }
        if ((victim & board.getWhiteKing()) != 0 || (victim & board.getBlackKing()) != 0){
            System.out.println("Capture of king ???");
            return 0;
        }
        throw new RuntimeException("not a capture move");
    }

    static int evalMaterialByTurn(Chessboard board,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing){
        int score = 0;

        score += BitOperations.populationCount(myPawns) * PAWN_SCORE;
        score += BitOperations.populationCount(myKnights) * KNIGHT_SCORE;
        score += BitOperations.populationCount(myBishops) * BISHOP_SCORE;
        score += BitOperations.populationCount(myRooks) * ROOK_SCORE;
        score += BitOperations.populationCount(myQueens) * QUEEN_SCORE;

        return score;
    }

}
