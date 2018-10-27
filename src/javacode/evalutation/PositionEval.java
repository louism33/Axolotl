package javacode.evalutation;

import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import java.util.List;

public class PositionEval {
    
    static int evalPositionByTurn(Chessboard board, boolean white){
        long pawns, knights, bishops, rooks, queens;
        if (white){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
        }
        
        return piecePositionScores(pawns, pawnpos)
                + piecePositionScores(knights, knightpos)
                + piecePositionScores(bishops, bishoppos)
                + piecePositionScores(rooks, rookpos)
                + piecePositionScores(queens, queenpos)
                ;
    }
    
    private static int piecePositionScores(long pieces, int[] whichArray){
        List<Integer> indexOfAllPieces = BitIndexing.getIndexOfAllPieces(pieces);
        int answer = 0;
        for (Integer piece : indexOfAllPieces){
            answer += whichArray[piece];
        }
        return answer;
    }

    private static int pawnpos[] = {
            0,   0,  0,  0,  0,  0,  0,  0,
            5,  10, 10,-20,-20, 10, 10,  5,
            5,   5,  5,  0,  0, -5,  0,  5,
            5,   5,  5, 20, 20,  0,  0,  0,
            5,   5, 10, 25, 25, 10,  5,  5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0,   0,  0,  0,  0,  0,  0,  0,};
    
    private static int knightpos[] = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -30,  5, 10, 15, 15, 11,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50,};

    private static int bishoppos[] = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,};

    private static int rookpos[] =   {
            0,   0,  0,  5,  5,  0,  0,  0,
            5,   0,  0,  0,  0,  0,  0,  5,
            5,   0,  0,  0,  0,  0,  0,  5,
            10,  5,  5,  5,  5,  5,  5, 10,
            10,  0,  0,  0,  0,  0,  0,  0,
            -5,  0,  0,  0,  0,  0,  0, -5,
            5,  10, 10, 10, 10, 10, 10,  5,
            0,   0,  0,  0,  0,  0,  0,  0};

    private static int queenpos[] =   {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -10,  5,  5,  5,  5,  5,  0,-10,
            0,    0,  5,  5,  5,  5,  0, -5,
            -5,   0,  5,  5,  5,  5,  0, -5,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20,};

    private static int kingposstart[] =   {
            20, 30, 10,  0,  0, 20, 30, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30};

    private static int kingposend[] =   {
            -50,-30,-30,-30,-30,-30,-30,-50,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -50,-40,-30,-20,-20,-30,-40,-50};
}
