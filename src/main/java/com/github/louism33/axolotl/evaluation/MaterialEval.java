package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.populationCount;

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

    static int evalMaterialByTurn(Chessboard board, boolean white){
        int score = 0;
        score += 
                pawnScores(board, white)
                + knightScores(board, white)
                + bishopScores(board, white)
                + rookScores(board, white)
                + queenScores(board, white)
                + kingScores(board, white)
        ;

        return score;
    }
    
    private static int pawnScores(Chessboard board, boolean white){
        long myPieces = white ? board.getWhitePawns() : board.getBlackPawns();
        int numberOfPawns = populationCount(myPieces);
        return numberOfPawns * PAWN_SCORE;
    }

    private static int knightScores(Chessboard board, boolean white){
        long myPieces = white ? board.getWhiteKnights() : board.getBlackKnights();
        int numberOfKnights = populationCount(myPieces);
        return numberOfKnights * KNIGHT_SCORE;
    }

    private static int bishopScores(Chessboard board, boolean white){
        long myPieces = white ? board.getWhiteBishops() : board.getBlackBishops();
        int numberOfBishops = populationCount(myPieces);
        return numberOfBishops * BISHOP_SCORE;
    }

    private static int rookScores(Chessboard board, boolean white){
        long myPieces = white ? board.getWhiteRooks() : board.getBlackRooks();
        int numberOfRooks = populationCount(myPieces);
        return numberOfRooks * ROOK_SCORE;
    }

    private static int queenScores(Chessboard board, boolean white){
        long myPieces = white ? board.getWhiteQueen() : board.getBlackQueen();
        int numberOfQueens = populationCount(myPieces);
        return numberOfQueens * QUEEN_SCORE;
    }

    private static int kingScores(Chessboard board, boolean white){
        long myPieces = white ? board.getWhiteKing() : board.getBlackKing();
        int numberOfKings = populationCount(myPieces);
        return numberOfKings * KING_SCORE;
    }

}
