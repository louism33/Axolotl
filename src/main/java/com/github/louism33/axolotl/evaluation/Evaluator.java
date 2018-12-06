package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.utilities.Statistics;
import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.Bishop.evalBishopByTurn;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_STALEMATE_SCORE;
import static com.github.louism33.axolotl.evaluation.King.evalKingByTurn;
import static com.github.louism33.axolotl.evaluation.Knight.evalKnightByTurn;
import static com.github.louism33.axolotl.evaluation.MaterialEval.*;
import static com.github.louism33.axolotl.evaluation.Misc.evalMiscByTurn;
import static com.github.louism33.axolotl.evaluation.Pawns.evalPawnsByTurn;
import static com.github.louism33.axolotl.evaluation.PositionEval.evalPositionByTurn;
import static com.github.louism33.axolotl.evaluation.Queen.evalQueenByTurn;
import static com.github.louism33.axolotl.evaluation.Rook.evalRookByTurn;

public class Evaluator {

    public static int lazyEval(Chessboard board, boolean white) {
        return lazyEvalHelper(board, white);
    }


    public static int eval(Chessboard board, boolean white, int[] moves) {
        if (moves == null){
            moves = board.generateLegalMoves();
        }

//        statistics.numberOfEvals++;

        if (moves.length == 0){
            if (board.inCheck(white)) {
//                statistics.numberOfCheckmates++;
                return IN_CHECKMATE_SCORE;
            }
            else {
//                statistics.numberOfStalemates++;
                return IN_STALEMATE_SCORE;
            }
        }
        else{

            long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
            long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
            long friends, enemies;

            if (white){
                myPawns = board.getWhitePawns();
                myKnights = board.getWhiteKnights();
                myBishops = board.getWhiteBishops();
                myRooks = board.getWhiteRooks();
                myQueens = board.getWhiteQueen();
                myKing = board.getWhiteKing();

                enemyPawns = board.getBlackPawns();
                enemyKnights = board.getBlackKnights();
                enemyBishops = board.getBlackBishops();
                enemyRooks = board.getBlackRooks();
                enemyQueens = board.getBlackQueen();
                enemyKing = board.getBlackKing();

                friends = board.whitePieces();
                enemies = board.blackPieces();
            }
            else {
                myPawns = board.getBlackPawns();
                myKnights = board.getBlackKnights();
                myBishops = board.getBlackBishops();
                myRooks = board.getBlackRooks();
                myQueens = board.getBlackQueen();
                myKing = board.getBlackKing();

                enemyPawns = board.getWhitePawns();
                enemyKnights = board.getWhiteKnights();
                enemyBishops = board.getWhiteBishops();
                enemyRooks = board.getWhiteRooks();
                enemyQueens = board.getWhiteQueen();
                enemyKing = board.getWhiteKing();

                friends = board.blackPieces();
                enemies = board.whitePieces();
            }

            long allPieces = friends | enemies;


            return evalHelper(moves, board, white,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);
        }
    }

    private static boolean naiveEndgame (Chessboard board){
        return BitOperations.populationCount(board.allPieces()) < 8;
    }

    private static int evalHelper(int[] moves, Chessboard board, boolean white,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces) {
        
        return evalTurn(moves, board, white,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces) -
                
                evalTurn(moves, board, white,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                        enemies, friends, allPieces);
    }

    private static int evalTurn (int[] moves, Chessboard board, boolean white,
                                 long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                 long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                 long enemies, long friends, long allPieces){
        
        int score = 0;
        score +=
                evalMaterialByTurn(board, white)

                        + evalPositionByTurn(board, white, naiveEndgame(board))
                        + evalPawnsByTurn(board, white, myPawns, enemyPawns)
                        + evalKnightByTurn(board, white, myPawns, myKnights, enemyPawns)
                        + evalBishopByTurn(board, white, myPawns, myBishops, enemyPawns)
                        + evalRookByTurn(board, white, myPawns, myRooks, enemyPawns)
                        + evalQueenByTurn(board, white, myPawns, enemyPawns)
                        + evalKingByTurn(board, white, myKing, enemies, friends, allPieces)
                        + evalMiscByTurn(board, white, moves)
        ;
        return score;
    }

    private static int lazyEvalHelper(Chessboard board, boolean white) {
        return evalMaterialByTurn(board, white) - evalMaterialByTurn(board, !white);
    }

    public static int getScoreOfDestinationPiece(Chessboard board, int move){
        return MaterialEval.getScoreOfDestinationPiece(board, move);
    }


}
