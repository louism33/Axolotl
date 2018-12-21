package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.*;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.Bishop.evalBishopByTurn;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_STALEMATE_SCORE;
import static com.github.louism33.axolotl.evaluation.King.evalKingByTurn;
import static com.github.louism33.axolotl.evaluation.Knight.evalKnightByTurn;
import static com.github.louism33.axolotl.evaluation.MaterialEval.evalMaterialByTurn;
import static com.github.louism33.axolotl.evaluation.Misc.evalMiscByTurn;
import static com.github.louism33.axolotl.evaluation.MoveTable.*;
import static com.github.louism33.axolotl.evaluation.PassedPawns.*;
import static com.github.louism33.axolotl.evaluation.Pawns.evalPawnsByTurn;
import static com.github.louism33.axolotl.evaluation.PositionEval.evalPositionByTurn;
import static com.github.louism33.axolotl.evaluation.Queen.evalQueenByTurn;
import static com.github.louism33.axolotl.evaluation.Rook.evalRookByTurn;
import static com.github.louism33.chesscore.BitOperations.getIndexOfFirstPiece;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BitboardResources.*;

public class Evaluator {

    static boolean isEndgame = false;

    static int[] whiteThreatsToSquare = new int[64];
    static int[] blackThreatsToSquare = new int[64];

    static void resetThreats(){
        Arrays.fill(whiteThreatsToSquare, 0);
        Arrays.fill(blackThreatsToSquare, 0);
    }
    static void populateThreats(Chessboard board, int[] moves){
        populateFromMoves(moves);
        board.flipTurn();
        populateFromMoves(board.generateLegalMoves());
        board.flipTurn();
    }

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
        if (isEndgame){
            return true;
        }
        if (board.getWhiteQueen() == 0 && board.getBlackQueen() == 0){
            isEndgame = true;
        }
        else if (board.getWhiteQueen() == 1
                && populationCount(board.getWhiteKnights()
                | board.getWhiteBishops()
                | board.getWhiteRooks()) == 1){
            isEndgame = true;
        }
        else if (board.getBlackQueen() == 1
                && populationCount(board.getBlackKnights()
                | board.getBlackBishops()
                | board.getBlackRooks()) == 1){
            isEndgame = true;
        }

        return isEndgame;
    }
    
    private static void pt(int[] threats){
        for (int i = 0; i < threats.length; i++){
            System.out.print(String.format("% 5d ", threats[i]));
            if (i % 8 == 7 && i != 0){
                System.out.println();
            }
        }
    }

    private static int evalHelper(int[] moves, Chessboard board, boolean white,
                                  long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                  long enemyPawns, long enemyKnights, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                  long enemies, long friends, long allPieces,
                                  long pinnedPieces, boolean inCheck) {

        resetThreats();
        populateThreats(board, moves);

        return evalTurn(moves, board, white,
                myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                enemyPawns, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                enemies, friends, allPieces,
                pinnedPieces, inCheck) -

                evalTurn(moves, board, !white,
                        enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                        myPawns, myBishops, myRooks, myQueens, myKing,
                        friends, enemies, allPieces,
                        pinnedPieces, inCheck);
    }

    private static int evalTurn(int[] moves, Chessboard board, boolean white,
                                long myPawns, long myKnights, long myBishops, long myRooks, long myQueens, long myKing,
                                long enemyPawns, long enemyBishops, long enemyRooks, long enemyQueens, long enemyKing,
                                long enemies, long friends, long allPieces,
                                long pinnedPieces, boolean inCheck){

        int score = 0;
        score +=
                evalMaterialByTurn(
                        myPawns, myKnights, myBishops, myRooks, myQueens)

                        + evalPositionByTurn(board, white, naiveEndgame(board))

                        + evalPassedPawnsByTurn(board, white, myPawns, allPieces, enemies)

                        + evalPawnsByTurn(board, white, myPawns, enemyPawns, enemyBishops,
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
                        friends, enemies,
                        allPieces
                )

                        + evalMiscByTurn(board, white, moves, pinnedPieces, friends, inCheck)
        ;
        return score;
    }


    static long getRow(long piece){
        return ROWS[getIndexOfFirstPiece(piece)  / 8];
    }

    static long getFile(long piece) {
        return FILES[getIndexOfFirstPiece(piece) % 8];
    }





    public static void printEval(Chessboard board){
        System.out.println(evalToString(board, board.generateLegalMoves()));
    }

    public static String evalToString(Chessboard board, int[] moves) {
        int whiteMat = evalMaterialByTurn(
                board.getWhitePawns(), board.getWhiteKnights(),
                board.getWhiteBishops(), board.getWhiteRooks(), board.getWhiteQueen());
        int blackMat = evalMaterialByTurn(
                board.getBlackPawns(), board.getBlackKnights(),
                board.getBlackBishops(), board.getBlackRooks(), board.getBlackQueen());

        int whitePos = evalPositionByTurn(board, true, naiveEndgame(board));
        int blackPos = evalPositionByTurn(board, false, naiveEndgame(board));

        int whitePawns = evalPawnsByTurn(board, true,
                board.getWhitePawns(),
                board.getBlackPawns(), board.getBlackBishops(),
                board.blackPieces(), board.allPieces());
        int blackPawns = evalPawnsByTurn(board, false,
                board.getBlackPawns(),
                board.getWhitePawns(), board.getWhiteBishops(),
                board.whitePieces(), board.allPieces());

        int whiteKnight = evalKnightByTurn(board, true,
                board.getWhitePawns(), board.getWhiteKnights(),
                board.getBlackPawns(), board.getBlackRooks(), board.getBlackQueen(), board.getBlackKing());
        int blackKnight = evalKnightByTurn(board, false,
                board.getBlackPawns(), board.getBlackKnights(),
                board.getWhitePawns(), board.getWhiteRooks(), board.getWhiteQueen(), board.getWhiteKing());

        int whiteBishop = evalBishopByTurn(board, true,
                board.getWhitePawns(), board.getWhiteBishops(),
                board.getBlackPawns(),
                board.whitePieces());
        int blackBishop = evalBishopByTurn(board, false,
                board.getBlackPawns(), board.getBlackBishops(),
                board.getWhitePawns(),
                board.blackPieces());

        int whiteRook = evalRookByTurn(board, true,
                board.getWhitePawns(), board.getWhiteRooks(),
                board.getWhiteQueen(), board.getBlackPawns(),
                board.getBlackPawns(), board.allPieces());
        int blackRook = evalRookByTurn(board, false,
                board.getBlackPawns(), board.getBlackRooks(),
                board.getBlackQueen(), board.getWhitePawns(),
                board.getWhitePawns(), board.allPieces());

        int whiteQueen = evalQueenByTurn(board, true,
                board.getWhiteRooks(), board.getWhiteQueen(),
                board.getBlackPawns());
        int blackQueen = evalQueenByTurn(board, false,
                board.getBlackRooks(), board.getBlackQueen(),
                board.getWhitePawns());

        int whiteKing = evalKingByTurn(board, true,
                board.getWhitePawns(), board.getWhiteKing(),
                board.whitePieces(), board.blackPieces(),
                board.allPieces());
        int blackKing = evalKingByTurn(board, false,
                board.getBlackPawns(), board.getBlackKing(),
                board.blackPieces(), board.whitePieces(),
                board.allPieces());

        int miscWhite = evalMiscByTurn(board, true, moves,
                board.pinnedPiecesToSquareBitBoard(true, Square.squareFromSingleBitboard(board.getWhiteKing())),
                board.whitePieces(), board.inCheck(true));
        int miscBlack = evalMiscByTurn(board, false, moves,
                board.pinnedPiecesToSquareBitBoard(false, Square.squareFromSingleBitboard(board.getBlackKing())),
                board.blackPieces(), board.inCheck(false));

        int whitePassedPawns = evalPassedPawnsByTurn(board, true,
                board.getWhitePawns(), board.allPieces(), board.blackPieces());
        int blackPassedPawns = evalPassedPawnsByTurn(board, false,
                board.getBlackPawns(), board.allPieces(), board.whitePieces());

        String evalString = "   Aspect    |     White     |     Black     |    Total  \n" +
                "             |               |               |           \n" +
                "-------------+---------------+---------------+-----------\n" +

                String.format("  Material   |     % 5d     |     % 5d     |    % 5d\n",
                        whiteMat, blackMat, (whiteMat - blackMat)) +

                String.format("  Position   |     % 5d     |     % 5d     |    % 5d\n",
                        whitePos, blackPos, (whitePos - blackPos)) +

                String.format("   Pawns     |     % 5d     |     % 5d     |    % 5d\n",
                        whitePawns, blackPawns, (whitePawns - blackPawns)) +

                String.format("  Knights    |     % 5d     |     % 5d     |    % 5d\n",
                        whiteKnight, blackKnight, (whiteKnight - blackKnight)) +

                String.format("  Bishops    |     % 5d     |     % 5d     |    % 5d\n",
                        whiteBishop, blackBishop, (whiteBishop - blackBishop)) +

                String.format("   Rooks     |     % 5d     |     % 5d     |    % 5d\n",
                        whiteRook, blackRook, (whiteRook - blackRook)) +

                String.format("  Queens     |     % 5d     |     % 5d     |    % 5d\n",
                        whiteQueen, blackQueen, (whiteQueen - blackQueen)) +

                String.format("    King     |     % 5d     |     % 5d     |    % 5d\n",
                        whiteKing, blackKing, (whiteKing - blackKing)) +

                String.format("    Misc     |     % 5d     |     % 5d     |    % 5d\n",
                        miscWhite, miscBlack, (miscWhite - miscBlack)) +

                String.format(" PassedPawns |     % 5d     |     % 5d     |    % 5d\n",
                        whitePassedPawns, blackPassedPawns, (whitePassedPawns - blackPassedPawns))
                + (naiveEndgame(board) ? "\nWe are in the endgame." : "\nWe are not in the endgame.") +
                "\nFrom white's point of view the score is:              "
                +((whiteMat - blackMat)
                + (whitePos - blackPos)
                + (whitePawns - blackPawns)
                + (whiteBishop - blackBishop)
                + (whiteRook - blackRook)
                + (whiteQueen - blackQueen)
                + (whiteKing - blackKing)
                + (miscWhite - miscBlack)
                + (whitePassedPawns - blackPassedPawns));

        return evalString;
    }
}
