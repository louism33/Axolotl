package javacode.evalutation;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;

import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.chess.BitIndexing.*;

public class Pawns {

    private static int PASSED_PAWN_BONUS = 50;
    private static int DOUBLE_PAWN_PENALTY = -35;
    private static int CENTRE_PAWN = 15;
    private static int SUPER_CENTRE_PAWN = 35;

    static int evalPawnsByTurn(Chessboard board, boolean white){
        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;

        if (myPawns == 0){
            return 0;
        }

        int score = 0;

        score += 0

        ;

        return score;
    }

    private static int pawnCentreBonus(Chessboard board, boolean white, long myPawns){
        int answer = 0;

        answer += populationCount(
                centreFourSquares & myPawns) 
                * SUPER_CENTRE_PAWN;

        answer += populationCount(
                (centreNineSquares ^ centreFourSquares) & myPawns)
                * CENTRE_PAWN;

        return answer;
    }


    private static int pawnStructureBonus(Chessboard board, boolean white, long myPawns){
        int score = 0;

        return 0;
    }

    private static int pawnChainBonus(Chessboard board, boolean white, long myPawns){
        int score = 0;

        return 0;
    }

    private static int doublePawnPenalty(Chessboard board, boolean white, long myPawns){
        int score = 0;

        return 0;
    }

    private static int passedPawnBonus(Chessboard board, boolean white, long myPawns){
        int score = 0;

        return 0;
    }
}
