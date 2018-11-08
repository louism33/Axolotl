package javacode.evaluation;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveParser;

import static javacode.chessprogram.chess.BitIndexing.populationCount;

class MaterialEval {

    private Evaluator evaluator;

    public MaterialEval(Evaluator evaluator) {
        this.evaluator = evaluator;
    }
    
    int getScoreOfDestinationPiece(Chessboard board, Move move){
        long victim = BitManipulations.newPieceOnSquare(move.destinationIndex);
        if (MoveParser.isPromotionToQueen(move)){
            return this.evaluator.QUEEN_SCORE;
        }
        if ((victim & board.WHITE_PAWNS) != 0 || (victim & board.BLACK_PAWNS) != 0){
            return this.evaluator.PAWN_SCORE;
        }
        if ((victim & board.WHITE_KNIGHTS) != 0 || (victim & board.BLACK_KNIGHTS) != 0){
            return this.evaluator.KNIGHT_SCORE;
        }
        if ((victim & board.WHITE_BISHOPS) != 0 || (victim & board.BLACK_BISHOPS) != 0){
            return this.evaluator.BISHOP_SCORE;
        }
        if ((victim & board.WHITE_ROOKS) != 0 || (victim & board.BLACK_ROOKS) != 0){
            return this.evaluator.ROOK_SCORE;
        }
        if ((victim & board.WHITE_QUEEN) != 0 || (victim & board.BLACK_QUEEN) != 0){
            return this.evaluator.QUEEN_SCORE;
        }
        if ((victim & board.WHITE_KING) != 0 || (victim & board.BLACK_KING) != 0){
            System.out.println("Capture of king ???");
            return 0;
        }
        throw new RuntimeException("not a capture move");
    }

    int evalMaterialByTurn(Chessboard board, boolean white){
        int score = 0;
        score += pawnScores(board, white)
                + knightScores(board, white)
                + bishopScores(board, white)
                + rookScores(board, white)
                + queenScores(board, white)
                + kingScores(board, white)
        ;

        return score;
    }
    
    private int pawnScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        int numberOfPawns = populationCount(myPieces);
        return numberOfPawns * this.evaluator.PAWN_SCORE;
    }

    private int knightScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_KNIGHTS : board.BLACK_KNIGHTS;
        int numberOfKnights = populationCount(myPieces);
        return numberOfKnights * this.evaluator.KNIGHT_SCORE;
    }

    private int bishopScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_BISHOPS : board.BLACK_BISHOPS;
        int numberOfBishops = populationCount(myPieces);
        return numberOfBishops * this.evaluator.BISHOP_SCORE;
    }

    private int rookScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_ROOKS : board.BLACK_ROOKS;
        int numberOfRooks = populationCount(myPieces);
        return numberOfRooks * this.evaluator.ROOK_SCORE;
    }

    private int queenScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_QUEEN : board.BLACK_QUEEN;
        int numberOfQueens = populationCount(myPieces);
        return numberOfQueens * this.evaluator.QUEEN_SCORE;
    }

    private int kingScores(Chessboard board, boolean white){
        long myPieces = white ? board.WHITE_KING : board.BLACK_KING;
        int numberOfKings = populationCount(myPieces);
        return numberOfKings * this.evaluator.KING_SCORE;
    }

}
