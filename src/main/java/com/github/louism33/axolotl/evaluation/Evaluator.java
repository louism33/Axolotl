package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.BitOperations;
import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.evaluation.Bishop.evalBishopByTurn;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.King.evalKingByTurn;
import static com.github.louism33.axolotl.evaluation.Knight.evalKnightByTurn;
import static com.github.louism33.axolotl.evaluation.MaterialEval.evalMaterialByTurn;
import static com.github.louism33.axolotl.evaluation.Misc.evalMiscByTurn;
import static com.github.louism33.axolotl.evaluation.Pawns.evalPawnsByTurn;
import static com.github.louism33.axolotl.evaluation.PositionEval.evalPositionByTurn;
import static com.github.louism33.axolotl.evaluation.Queen.evalQueenByTurn;
import static com.github.louism33.axolotl.evaluation.Rook.evalRookByTurn;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitboardResources.FILES;
import static com.github.louism33.chesscore.BitboardResources.ROWS;

public class Evaluator {

    public static int eval(Chessboard board, boolean white, int[] moves) {
        if (moves == null){
            moves = board.generateLegalMoves();
        }

        if (moves.length == 0){
            if (board.inCheck(white)) {
                return IN_CHECKMATE_SCORE;
            }
            else {
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

            long pinnedPieces = board.pinnedPieces;
            boolean inCheck = board.inCheckRecorder;

            return evalHelper(moves, board, white,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces,
                    pinnedPieces, inCheck);
        }
    }

    public static int evalNOCM(Chessboard board, boolean white, int[] moves) {
        if (moves == null){
            moves = board.generateLegalMoves();
        }

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

        long pinnedPieces = board.pinnedPieces;
        boolean inCheck = board.inCheckRecorder;

        return evalHelper(moves, board, white,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces,
                pinnedPieces, inCheck);
    }

    private static boolean naiveEndgame (Chessboard board){
        return BitOperations.populationCount(board.allPieces()) < 8;
    }

    private static int evalHelper(int[] moves, Chessboard board, boolean white,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces,
                                  long pinnedPieces, boolean inCheck) {

        return evalTurn(moves, board, white,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces,
                pinnedPieces, inCheck) -

                evalTurn(moves, board, !white,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        myPawns, myRooks, myQueens, myKing,
                        enemies, friends, allPieces,
                        pinnedPieces, inCheck);
    }

    private static int evalTurn(int[] moves, Chessboard board, boolean white,
                                long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                long enemyPawns, long enemyRooks, long enemyQueens, long enemyKing,
                                long enemies, long friends, long allPieces,
                                long pinnedPieces, boolean inCheck){

        int score = 0;
        score +=
                evalMaterialByTurn(
                        myPawns, myKnights, myBishops, myRooks, myQueens)

                        + evalPositionByTurn(board, white, naiveEndgame(board))

                        + evalPawnsByTurn(board, white, myPawns, enemyPawns,
                        enemies, allPieces)

                        + evalKnightByTurn(board, white,
                        myPawns, myKnights,
                        enemyPawns, enemyRooks, enemyQueens, enemyKing
                )

                        + evalBishopByTurn(board, white,
                        myPawns, myBishops,
                        enemyPawns,
                        friends
                )

                        + evalRookByTurn(board, white, myPawns, myRooks, myQueens, enemyPawns, enemies, allPieces)

                        + evalQueenByTurn(board, white,
                        myRooks, myQueens,
                        enemyPawns
                )

                        + evalKingByTurn(board, white,
                        myPawns, myKing,
                        allPieces
                )

                        + evalMiscByTurn(board, white, moves, pinnedPieces, inCheck)
        ;
        return score;
    }


    public static long getRow(long piece){
        return ROWS[getIndexOfFirstPiece(piece)  / 8];
    }

    public static long getFile(long piece) {
        return FILES[getIndexOfFirstPiece(piece) % 8];
    }

}
