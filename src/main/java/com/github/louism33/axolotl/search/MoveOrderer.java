package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveConstants;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MAXIMUM;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.SHORT_MINIMUM;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.retrieveFromTable;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;

public final class MoveOrderer {

    static int[][][] scores = new int[NUMBER_OF_THREADS][Chessboard.MAX_DEPTH_AND_ARRAY_LENGTH][128];
    static int[] rootScores = new int[128];
    static int[][] rootCount = new int[NUMBER_OF_THREADS][128];
    static final int rootHashCount = 0x7fffffff;

    static int[][] mateKillers = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD];
    static int[][] killerMoves = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD * 2];

    private static int[][][] historyMoveScores = new int[2][64][64]; // todo, make thread specific?

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
            rootScores = new int[128];
            rootCount = new int[NUMBER_OF_THREADS][128];
        }
        resetMoveOrderer();
        readyMoveOrderer = true;
    }

    static void resetMoveOrderer() {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Arrays.fill(mateKillers[i], 0);
            Arrays.fill(killerMoves[i], 0);
            Arrays.fill(rootCount[i], 0);

            for (int j = 0; j < scores[i].length; j++) {
                Arrays.fill(scores[i][j], 0);
            }
        }
        for (int i = 0; i < 64; i++) {
            Arrays.fill(historyMoveScores[BLACK][i], uninterestingMoveScoreNew);
            Arrays.fill(historyMoveScores[WHITE][i], uninterestingMoveScoreNew);
        }
    }


    static void scoreMovesAtRootNewFirst(int[] moves, int numberOfMoves, Chessboard board) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(killerMoves[0][0] == 0);
            Assert.assertTrue(killerMoves[0][1] == 0);
            Assert.assertTrue(mateKillers[0][1] == 0);
        }

        long entry = retrieveFromTable(board.zobristHash);
        int hashMove = 0;
        if (entry != 0) {
            hashMove = TranspositionTable.getMove(entry);
        }
        
        Arrays.fill(rootScores, 0);
        rootScores[rootScores.length - 1] = numberOfMoves;

        for (int i = 0; i < numberOfMoves; i++) {
            final int move = moves[i];
            if (move == 0) {
                break;
            }

            if (move == hashMove) {
                rootScores[i] = hashScoreNew;
                continue;
            }
            
            board.makeMoveAndFlipTurn(move);
            rootScores[i] = Quiescence.quiescenceSearch(board, SHORT_MINIMUM, SHORT_MAXIMUM, 0, 1, 0);
            board.unMakeMoveAndFlipTurn();
//            System.out.println(MoveParser.toString(move ) + ",  " + rootScores[i]);
            
        }
        reverseInsertionSort(moves, rootScores, numberOfMoves);
        Arrays.fill(rootScores, 0); // we do not use movescores at root outside of this method
    }

    // todo, consider getting total moves made from here
    // sorts root moves by how many makeMoves they had in the last iteration (size of sub-tree)
    static void scoreMovesAtRootNewInNode(int[] moves, int whichThread, int numberOfMoves) {
        final int[] count = rootCount[whichThread];
        count[0] = rootHashCount;
        reverseInsertionSort(moves, count, numberOfMoves);
        Arrays.fill(count, 0); // reset this for each iteration
    }


    static void scoreMovesAtRootNew(int[] moves, int numberOfMoves, Chessboard board) {
        // todo, check every move for givesCheck()
        // todo, see on every move / cap
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

        Arrays.fill(rootScores, 0);
        rootScores[rootScores.length - 1] = numberOfMoves;

        for (int i = 0; i < numberOfMoves; i++) {
            final int move = moves[i];
            if (move == 0) {
                break;
            }

            if (move == hashMove) {
                rootScores[i] = hashScoreNew;
            } else if (isPromotionToQueen(move)) {
                if (isCaptureMove(move)) {
                    rootScores[i] = queenCapturePromotionScoreNew;
                } else {
                    rootScores[i] = queenQuietPromotionScoreNew;
                }
            } else if (isPromotionToKnight(move)) {
                rootScores[i] = knightPromotionScoreNew;
            } else if (isPromotionToBishop(move) || isPromotionToRook(move)) {
                rootScores[i] = uninterestingPromotionNew;
            } else if (isCaptureMove(move) || isEnPassantMove(move)) {
                final int score = seeScoreRoot(board, move, 0);
                rootScores[i] = score;
            } else if (board.moveGivesCheck(move)) {
                rootScores[i] = giveCheckMoveNew;
            } else if (isCastlingMove(move)) {
                rootScores[i] = castlingMoveNew;
            } else {

                if (MoveParser.moveIsPawnPushSeven(board.turn, move)) {
                    rootScores[i] = pawnPushToSevenNew;
                    continue;
                }

                if (MoveParser.moveIsPawnPushSix(board.turn, move)) {
                    rootScores[i] = pawnPushToSixNew;
                    continue;
                }

                final int score = quietHeuristicMoveScore(move, board.turn, absoluteMaxQuietScore);
                rootScores[i] = score;
            }
        }

        reverseInsertionSort(moves, rootScores, numberOfMoves);
        Arrays.fill(rootScores, 0); // we do not use movescores at root outside of this method
    }

    static void scoreMovesNewWithoutQuiets(final int[] moves, final Chessboard board, int ply,
                                           int hashMove, int whichThread) {
        int maxMoves = moves[moves.length - 1];

        int mateKiller = 0;
        int firstKiller = 0;
        int secondKiller = 0;

        if (ply < MAX_DEPTH_HARD) {
            mateKiller = mateKillers[whichThread][ply];
            firstKiller = killerMoves[whichThread][ply * 2];
            secondKiller = killerMoves[whichThread][ply * 2 + 1];
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
            } else if (captureMove || isEnPassantMove(move)) {
                final int score = getMVVLVAScore(move);
                scores[whichThread][ply][i] = score;
            } else if (move == mateKiller) {
                scores[whichThread][ply][i] = mateKillerScoreNew; // todo, as we try these and killers at specific time, we don't really need to score them
            } else if (firstKiller == move) {
                scores[whichThread][ply][i] = killerOneScoreNew;
            } else if (secondKiller == move) {
                scores[whichThread][ply][i] = killerTwoScoreNew;
            } else {
                scores[whichThread][ply][i] = unscoredQuietScore;
            }

            if (MASTER_DEBUG) {
                Assert.assertTrue(moves[i] != 0);
            }
        }
    }

    static void scoreQuietMoves(final int[] moves, final Chessboard board, int ply, int whichThread) {
        int maxMoves = moves[moves.length - 1];
        int turn = board.turn;

        int firstOldKiller = 0;
        int secondOldKiller = 0;

        if (ply < MAX_DEPTH_HARD) {
            firstOldKiller = ply >= 2 ? killerMoves[whichThread][ply * 2 - 4] : 0;
            secondOldKiller = ply >= 2 ? killerMoves[whichThread][ply * 2 - 4 + 1] : 0;
        }

        int move;
        Assert.assertTrue(scores[whichThread][ply][scores[whichThread][ply].length - 1] == maxMoves);

        for (int i = 0; i < maxMoves; i++) {
            moves[i] = moves[i] & MOVE_MASK_WITH_CHECK;
            move = moves[i];

            if (move == 0) {
                break;
            }

            if (scores[whichThread][ply][i] == alreadySearchedScore) {
                continue;
            }

            Assert.assertTrue(!isCaptureMove(move) || (isPromotionMove(move) && !isPromotionToQueen(move)));
            Assert.assertTrue(!isPromotionToQueen(move));
            Assert.assertTrue(!isEnPassantMove(move));

            // todo, keep checking state somewhere
            if (isPromotionToKnight(move)) {
                scores[whichThread][ply][i] = knightPromotionScoreNew;
            } else if (isPromotionMove(move)) {
                Assert.assertTrue(!isPromotionToQueen(move));
                scores[whichThread][ply][i] = uninterestingPromotionNew;
            } else if (board.moveGivesCheck(move)) {
                scores[whichThread][ply][i] = giveCheckMoveNew;
            } else if (isCastlingMove(move)) {
                scores[whichThread][ply][i] = castlingMoveNew;
            } else if (firstOldKiller == move) {
                scores[whichThread][ply][i] = oldKillerScoreOneNew;
            } else if (secondOldKiller == move) {
                scores[whichThread][ply][i] = oldKillerScoreTwoNew;
            } else if (MoveParser.moveIsPawnPushSeven(turn, move)) {
                scores[whichThread][ply][i] = pawnPushToSevenNew;
            } else if (MoveParser.moveIsPawnPushSix(turn, move)) {
                scores[whichThread][ply][i] = pawnPushToSixNew;
            } else {
                scores[whichThread][ply][i] = getHistoryScore(move, turn);
            }

            if (MASTER_DEBUG) {
                Assert.assertTrue(moves[i] != 0);
            }
        }


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
                final int score = getMVVLVAScore(move);
                scores[whichThread][ply][i] = score;
            } else if (move == mateKiller) {
                scores[whichThread][ply][i] = mateKillerScoreNew; // todo, as we try these and killers at specific time, we don't really need to score them
            } else if (firstKiller == move) {
                scores[whichThread][ply][i] = killerOneScoreNew;
            } else if (secondKiller == move) {
                scores[whichThread][ply][i] = killerTwoScoreNew;
            } else if (board.moveGivesCheck(move)) {
                scores[whichThread][ply][i] = giveCheckMoveNew; // todo, use phases to calculate this later (If at all)
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
        scores[whichThread][ply][moveIndex] = captureMaxScoreSEE + seeScore;
    }

    static void setCaptureToEqualCapture(int moveIndex, int whichThread, int ply) {
        Assert.assertTrue(evenCaptureScore > captureMaxScoreSEE);
        scores[whichThread][ply][moveIndex] = evenCaptureScore;
    }

    static int[] getNextBestMoveIndexAndScore(int whichThread, int ply) {
        // do not use at root, moves should be in order of desirability
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
            if (myScore == hashScoreNew) {
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

    static int[] getNextBestMoveIndexAndScoreNoChange(int whichThread, int ply) {
        // do not use at root, moves should be in order of desirability
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
            if (myScore == hashScoreNew) {
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
//        myScores[index] = alreadySearchedScore;

        return myReturn;
    }

    static void setMoveScoreToSearched(int whichThread, int ply, int index) {
        scores[whichThread][ply][index] = alreadySearchedScore;
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

        final int see = SEE.getSEE(board, move, whichThread);
        if (see >= 0) {
            return captureBaseScoreMVVLVA + see;
        }

        return captureMaxScoreSEE + see;
    }


    public static int getScoreOfMove(int[] moves, int move, int whichThread, int ply) {
        if (ply == 0) {
            return specialRootMoveScore; //we do not maintain score at root
        }
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

    private static int getHistoryScore(int move, int turn) {
        Assert.assertTrue(historyMoveScores[turn][getSourceIndex(move)][MoveParser.getDestinationIndex(move)] >= uninterestingMoveScoreNew);
        Assert.assertTrue(historyMoveScores[turn][getSourceIndex(move)][MoveParser.getDestinationIndex(move)] <= maxNodeQuietScoreNew);
        // we use the lowest bit to record check state, so it must not be set here
        Assert.assertTrue((historyMoveScores[turn][getSourceIndex(move)][MoveParser.getDestinationIndex(move)] & 1) == 0);
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

}
