package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveConstants;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.retrieveFromTable;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;

public final class MoveOrderer {
    
    static int[][][] scores = new int[NUMBER_OF_THREADS][Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH][128];

    static int[][] mateKillers = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD];
    static int[][] killerMoves = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD * 2];

    private static int[][][] historyMoveScores = new int[2][64][64];

    static final int INDEX = 0, SCORE = 1;
    private static int[][] returnArray = new int[NUMBER_OF_THREADS][2];

    private static boolean readyMoveOrderer = false;

    static void setupMoveOrderer(boolean force) {
        if (force || !readyMoveOrderer) {
            mateKillers = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD];
            killerMoves = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD * 2];
            scores = new int[NUMBER_OF_THREADS][Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH][128];
            returnArray = new int[NUMBER_OF_THREADS][2];
            historyMoveScores = new int[2][64][64];
        }
        resetMoveOrderer();
        readyMoveOrderer = true;
    }

    static void resetMoveOrderer() {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Arrays.fill(mateKillers[i], 0);
            Arrays.fill(killerMoves[i], 0);

            for (int j = 0; j < scores[i].length; j++) {
                Arrays.fill(scores[i][j], 0);
            }
        }
        for (int i = 0; i < 64; i++) {
            Arrays.fill(historyMoveScores[BLACK][i], uninterestingMoveScoreNew);
            Arrays.fill(historyMoveScores[WHITE][i], uninterestingMoveScoreNew);
        }
    }

    static void scoreMovesAtRoot(int[] moves, int numberOfMoves, Chessboard board) {
        long entry = retrieveFromTable(board.zobristHash);
        int hashMove = 0;
        if (entry != 0) {
            hashMove = TranspositionTable.getMove(entry);
        }

        if (MASTER_DEBUG) {
            Assert.assertTrue(killerMoves[0][0] == 0);
            Assert.assertTrue(killerMoves[0][1] == 0);
            Assert.assertTrue(mateKillers[0][1] == 0);
        }

//        final int[] myScores = scores[MASTER_THREAD][0];
//        Arrays.fill(myScores, 0);
//        myScores[myScores.length - 1] = numberOfMoves;

        for (int i = 0; i < numberOfMoves; i++) {
            final int move = moves[i];
            if (move == 0) {
                break;
            }

            if (move == hashMove) {
                moves[i] = buildMoveScore(move, hashScore);
            } else if (isPromotionToQueen(move)) {
                if (isCaptureMove(move)) {
                    moves[i] = buildMoveScore(move, queenCapturePromotionScore);
                } else {
                    moves[i] = buildMoveScore(move, queenQuietPromotionScore);
                }
            } else if (isPromotionToKnight(move)) {
                moves[i] = buildMoveScore(move, knightPromotionScore);
            } else if (isPromotionToBishop(move) || isPromotionToRook(move)) {
                moves[i] = buildMoveScore(move, uninterestingPromotion);
            } else if (isCaptureMove(move) || isEnPassantMove(move)) {
                moves[i] = buildMoveScore(move, seeScoreRoot(board, move, 0));
            } else if (board.moveGivesCheck(move)) {
                // checking sets flag on move
                moves[i] = MoveParser.setCheckingMove(moves[i]);
                Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
                Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
            } else if (isCastlingMove(move)) {
                moves[i] = buildMoveScore(move, castlingMove);
            } else {
                moves[i] = buildMoveScore(move, quietHeuristicMoveScore(move, board.turn, maxRootQuietScore));
            }
        }

        sortMoves(moves, numberOfMoves);
    }

    static void scoreMovesNew(final int[] moves, final Chessboard board, int ply,
                              int hashMove, int whichThread) {
        int maxMoves = moves[moves.length - 1];
        int turn = board.turn;

        int mateKiller = 0;
        int firstKiller = 0;
        int secondKiller = 0;
        int firstOldKiller = 0;
        int secondOldKiller = 0;

        if (ply < MAX_DEPTH_HARD) {
            mateKiller = mateKillers[whichThread][ply];
            firstKiller = killerMoves[whichThread][ply * 2];
            secondKiller = killerMoves[whichThread][ply * 2 + 1];
            firstOldKiller = ply >= 2 ? killerMoves[whichThread][ply * 2 - 4] : 0;
            secondOldKiller = ply >= 2 ? killerMoves[whichThread][ply * 2 - 4 + 1] : 0;
        }

        int move;
        Arrays.fill(scores[whichThread][ply], dontSearchMeScore);
        scores[whichThread][ply][scores[whichThread][ply].length - 1] = maxMoves;
        for (int i = 0; i < maxMoves; i++) {
            moves[i] = moves[i] & MOVE_MASK_WITH_CHECK;
            move = moves[i];

            if (move == 0) {
                break;
            }
            if (MASTER_DEBUG) {
                Assert.assertTrue(move != 0);
                Assert.assertTrue(move < MoveConstants.FIRST_FREE_BIT);
                Assert.assertTrue(hashMove < MoveConstants.FIRST_FREE_BIT);
            }

            final boolean captureMove = isCaptureMove(move);

            if (move == hashMove) {
                scores[whichThread][ply][i] = hashScoreNew;
            } else if (isPromotionToQueen(move)) {
                if (captureMove) {
                    scores[whichThread][ply][i] = queenCapturePromotionScoreNew;
                } else {
                    scores[whichThread][ply][i] = queenQuietPromotionScoreNew;
                }
            } else if (isPromotionToKnight(move)) {
                scores[whichThread][ply][i] = knightPromotionScoreNew;
            } else if (isPromotionToBishop(move) || isPromotionToRook(move)) {
                scores[whichThread][ply][i] = uninterestingPromotionNew;
            } else if (captureMove || isEnPassantMove(move)) {
//                final int score = seeScore(board, move, whichThread);
                // todo
                final int score = getMVVLVAScore(move);

                scores[whichThread][ply][i] = score;
            } else if (move == mateKiller) {
                scores[whichThread][ply][i] = mateKillerScoreNew; // todo, as we try these and killers at specific time, we don't really need to score them
            } else if (firstKiller == move) {
                scores[whichThread][ply][i] = killerOneScoreNew;
            } else if (secondKiller == move) {
                scores[whichThread][ply][i] = killerTwoScoreNew;
            } else if (board.moveGivesCheck(move)) {
                scores[whichThread][ply][i] = giveCheckMoveNew;
            } else if (isCastlingMove(move)) {
                scores[whichThread][ply][i] = castlingMoveNew;
            } else if (firstOldKiller == move) {
                scores[whichThread][ply][i] = oldKillerScoreOneNew;
            } else if (secondOldKiller == move) {
                scores[whichThread][ply][i] = oldKillerScoreTwoNew;
            } else {
                if (MoveParser.moveIsPawnPushSeven(turn, move)) {
                    scores[whichThread][ply][i] = pawnPushToSevenNew;
                    continue;
                }

                if (MoveParser.moveIsPawnPushSix(turn, move)) {
                    scores[whichThread][ply][i] = pawnPushToSixNew;
                    continue;
                }

//                scores[whichThread][ply][i] = quietHeuristicMoveScoreNew(move, turn, maxNodeQuietScoreNew);
                scores[whichThread][ply][i] = getHistoryScore(move, turn);
            }

            if (MASTER_DEBUG) {
                Assert.assertTrue(moves[i] != 0);
            }
        }
    }

    static void scoreMovesQuiescenceNew(int[] moves, int ply, int whichThread) {
        final int maxMoves = moves[moves.length - 1];

        Arrays.fill(scores[whichThread][ply], dontSearchMeScore);
        scores[whichThread][ply][scores[whichThread][ply].length - 1] = maxMoves;
        for (int i = 0; i < maxMoves; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            if (isCaptureMove(move) || isEnPassantMove(move)) {
                if (isPromotionMove(move) && isPromotionToQueen(move)) {
                    scores[whichThread][ply][i] = queenCapturePromotionScoreNew;
                } else {
                    final int score = getMVVLVAScore(move);
                    scores[whichThread][ply][i] = score;
                }
            } else if (isPromotionMove(move) && (isPromotionToQueen(move))) {
                scores[whichThread][ply][i] = queenQuietPromotionScoreNew;
            } else if (MASTER_DEBUG) {
                Assert.assertEquals(0, getMoveScore(move));
            }
        }
    }

    private static void sortMoves(int[] moves, int numberOfMoves) { 
        Arrays.sort(moves, 0, numberOfMoves);
        for (int i = 0, j = numberOfMoves - 1; i < j; i++, j--) {
            int tmp = moves[i];
            moves[i] = moves[j];
            moves[j] = tmp;
        }
    }

    private static void reverseInsertionSort(int[] moves, int[] scores, int numberOfMoves) {
        for (int j = 1; j < numberOfMoves; j++) {
            int move = moves[j];
            int score = scores[j];
            int i = j - 1;
            while ((i > -1) && (scores[i] < score)) {
                moves[i + 1] = moves[i];
                scores[i + 1] = scores[i];
                i--;
            }
            moves[i + 1] = move;
            scores[i + 1] = score;
        }
    }

    static void invalidateHashMove(int[] moves, int hashMove, int whichThread, int ply) {
        for (int i = 0; i < moves[moves.length - 1]; i++) {
            if (moves[i] == hashMove) {
                scores[whichThread][ply][i] = alreadySearchedScore;
                return;
            }
        }
    }

    static void setCaptureToLosingCapture(int moveIndex, int seeScore, int whichThread, int ply) {
        Assert.assertTrue(seeScore < 0);
        Assert.assertTrue(seeScore >= -1000);
        Assert.assertTrue(seeScore >= -900);
        // alreadySearchedScore because we have just retrieved it from getNextBestMoveIndexAndScore()
        Assert.assertTrue(scores[whichThread][ply][moveIndex] == alreadySearchedScore);
        scores[whichThread][ply][moveIndex] = captureMaxScoreSEE + seeScore;
    }

    static void setCaptureToEqualCapture(int moveIndex, int whichThread, int ply) {
        // alreadySearchedScore because we have just retrieved it from getNextBestMoveIndexAndScore()
        Assert.assertTrue(evenCaptureScore > captureMaxScoreSEE);
        Assert.assertTrue(scores[whichThread][ply][moveIndex] == alreadySearchedScore);
        scores[whichThread][ply][moveIndex] = evenCaptureScore;
    }

    static int[] getNextBestMoveIndexAndScore(int whichThread, int ply) {
        // remove when rootsort is better
        Assert.assertTrue(ply != 0);

        final int[] myScores = scores[whichThread][ply];
        int max = myScores[0];
        int index = 0;

        final int totalScores = myScores[myScores.length - 1];

        if (totalScores <= 0) {
            final int[] myReturn = returnArray[whichThread];
            myReturn[INDEX] = 0;
            myReturn[SCORE] = dontSearchMeScore;
            return myReturn;
        }

        Assert.assertTrue(totalScores > 0);

        for (int i = 1; i < totalScores; i++) {
            final int myScore = myScores[i];
            if (myScore == hashScore) {
                myScores[i] = alreadySearchedScore;
                Assert.fail();
                continue;
            }
            if (max < myScore) {
                max = myScore;
                index = i;
            }
        }
        final int[] myReturn = returnArray[whichThread];
        myReturn[INDEX] = index;
        myReturn[SCORE] = myScores[index];
        myScores[index] = alreadySearchedScore;

        return myReturn;
    }

    static int getMVVLVAScoreHelper(int move) {
        final int victimPiece = getVictimPieceInt(move);
        if (victimPiece == NO_PIECE) {
            Assert.assertTrue(MoveParser.isEnPassantMove(move));
            return 99;
        }
        return scoreVictimForMVVLVA(victimPiece) - scoreAggressorForMVVLVA(getMovingPieceInt(move));
    }

    private static int scoreVictimForMVVLVA(int piece) {
        Assert.assertTrue(piece != NO_PIECE);

        switch (piece) {
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return 1000;

            case WHITE_ROOK:
            case BLACK_ROOK:
                return 500;

            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return 300;

            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                return 300;

            case WHITE_PAWN:
            case BLACK_PAWN:
                return 100;

            default:
                throw new RuntimeException("couldn't understand piece");
        }
    }

    private static int scoreAggressorForMVVLVA(int piece) {
        Assert.assertTrue(piece != NO_PIECE);

        switch (piece) {
            case WHITE_KING:
            case BLACK_KING:
                return 6;

            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return 5;

            case WHITE_ROOK:
            case BLACK_ROOK:
                return 4;

            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return 3;

            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                return 2;

            case WHITE_PAWN:
            case BLACK_PAWN:
                return 1;

            default:
                throw new RuntimeException("couldn't understand piece");
        }
    }

    private static int getMVVLVAScore(int move) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(move != 0);
        }

        Assert.assertTrue(getMVVLVAScoreHelper(move) > 0);
        Assert.assertTrue(getMVVLVAScoreHelper(move) >= 100 - 6);
        Assert.assertTrue(getMVVLVAScoreHelper(move) <= 1000 - 1);

        return captureBaseScoreMVVLVA + getMVVLVAScoreHelper(move);
    }

    private static int seeScoreRoot(Chessboard board, int move, int whichThread) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(move != 0);
        }

        int sourceScore = scoreByPiece(move, getMovingPieceInt(move));
        int destinationScore = isEnPassantMove(move) ? 1 : scoreByPiece(move, getVictimPieceInt(move));
        if (destinationScore > sourceScore) { // straight winning capture
            return neutralCapture + destinationScore - sourceScore;
        }

        final int see = SEE.getSEE(board, move, whichThread) / 100;
        if (see == 0) {
            return neutralCapture;
        }
        if (see > 0) {
            Assert.assertTrue(neutralCapture + 1 + (see / 200) < neutralCapture + 5);
            return neutralCapture + 1 + (see / 200);
        }

        return neutralCapture - 1 + (see / 200);
    }

    public static int getScoreOfMove(int[] moves, int move, int whichThread, int ply) {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == move) {
                return scores[whichThread][ply][i];
            }
        }
        throw new RuntimeException(MoveParser.toString(move) + " is not in the moves provided: " + MoveParser.toString(moves));
    }


    static int getMoveScore(int move) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(move > 0);
        }
        return (move & MOVE_SCORE_MASK) >>> moveScoreOffset;
    }

    static int buildMoveScore(int move, int score) {
        if (score == 0) {
            return move;
        }
        if (MASTER_DEBUG) {
            Assert.assertTrue(move > 0);
            Assert.assertTrue(score > 0);
        }
        return move | (score << moveScoreOffset);
    }

    private static int scoreByPiece(int move, int piece) {
        switch (piece) {
            case NO_PIECE:
                return 0;
            case WHITE_PAWN:
            case BLACK_PAWN:
                return 1;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return 2;
            case WHITE_ROOK:
            case BLACK_ROOK:
                return 3;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return 4;
            case WHITE_KING:
            case BLACK_KING:
                return 5;
            default:
                throw new RuntimeException("score by piece problem " + move);
        }
    }

    private static int getHistoryScore(int move, int turn) {
        Assert.assertTrue(historyMoveScores[turn][getSourceIndex(move)][MoveParser.getDestinationIndex(move)] >= uninterestingMoveScoreNew);
        Assert.assertTrue(historyMoveScores[turn][getSourceIndex(move)][MoveParser.getDestinationIndex(move)] <= maxNodeQuietScoreNew);
        return historyMoveScores[turn][getSourceIndex(move)][MoveParser.getDestinationIndex(move)];
    }


    static int quietHeuristicMoveScore(int move, int turn, int maxScore) {
        int d = getDestinationIndex(move);
        int piece = getMovingPieceInt(move);
        if (piece > WHITE_KING) {
            piece -= 6;
        }
        int score = quietsILikeToMove[piece] * goodQuietDestinations[turn][63 - d];
        if (score > maxScore) {
            return maxScore;
        }

        if (score < uninterestingMoveScore) {
            return uninterestingMoveScore;
        }

        return score;
    }
    

    static void updateKillerMoves(int whichThread, int move, int ply) {
        ply = ply * 2; // we store two killers per ply, at pos ply and ply+1
        if (MASTER_DEBUG) {
            Assert.assertTrue(move < MoveConstants.FIRST_FREE_BIT);
            Assert.assertTrue(move != 0);
            Assert.assertTrue(ply >= 0);
        }

        if (move != killerMoves[whichThread][ply]) {
            if (killerMoves[whichThread][ply] != 0) {
                killerMoves[whichThread][ply + 1] = killerMoves[whichThread][ply];
            }
            killerMoves[whichThread][ply] = move;
        }
    }

    static void updateMateKillerMoves(int whichThread, int move, int ply) {
        mateKillers[whichThread][ply] = move;
    }

    static void updateHistoryMoves(int move, int depth, int turn) {
        final int[] history = historyMoveScores[turn][getSourceIndex(move)];
        final int destinationIndex = getDestinationIndex(move);
        Assert.assertTrue(history[destinationIndex] >= uninterestingMoveScoreNew);
        history[destinationIndex] = Math.min(maxNodeQuietScoreNew, history[destinationIndex] + (depth << 1));
    }


    public static void printMovesAndScores(int[] moves, int whichThread, int ply) {
        printMovesAndScores(moves, scores, whichThread, ply);
    }

    public static void printMovesAndScores(int[] moves, int[][][] scores, int whichThread, int ply) {
        MoveParser.printMove(moves);
        System.out.println(Arrays.toString(scores[whichThread][ply]));
        for (int i = 0; i < numberOfRealMoves(moves); i++) {
            System.out.println(MoveParser.toString(moves[i]) + ", move number: " + i + ", score: " + (scores[whichThread][ply][i]));
        }
    }

    public static void printMovesAndScoresRoot(int[] moves) {
        MoveParser.printMove(moves);
        for (int i = 0; i < numberOfRealMoves(moves); i++) {
            System.out.println(MoveParser.toString(moves[i]) + ", move number: " + i + ", score: " + getMoveScore(moves[i]));
        }
    }
}
