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
    
    // todo, history?

    private static int[][] mateKillers = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD];
    private static int[][] killerMoves = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD * 2];

    static void setupMoveOrderer() {
        mateKillers = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD];
        killerMoves = new int[NUMBER_OF_THREADS][MAX_DEPTH_HARD * 2];
    }

    static void resetMoveOrderer() {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Arrays.fill(mateKillers[i], 0);
            Arrays.fill(killerMoves[i], 0);
        }
    }

    public static int getMoveScore(int move) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(move > 0);
        }
        return (move & MOVE_SCORE_MASK) >>> moveScoreOffset;
    }

    public static int buildMoveScore(int move, int score) {
        if (MASTER_DEBUG) {
            Assert.assertTrue(move > 0);
            Assert.assertTrue(score > 0);
        }
        return move | (score << moveScoreOffset);
    }

    public static void scoreMovesAtRoot(int[] moves, int numberOfMoves, Chessboard board) {
        long entry = retrieveFromTable(board.zobristHash);
        int hashMove = 0;
        if (entry != 0) {
            hashMove = TranspositionTable.getMove(entry);
        }

        for (int i = 0; i < numberOfMoves; i++) {
            int move = moves[i];
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
                moves[i] = buildMoveScore(move, seeScore(board, move, 0));
            } else if (checkingMove(board, moves[i])) { // checking sets flag on move
                moves[i] = MoveParser.setCheckingMove(moves[i]);
                if (MASTER_DEBUG) {
                    Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                }              
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
                if (MASTER_DEBUG) {
                    Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                }
            } else if (isCastlingMove(move)) {
                moves[i] = buildMoveScore(move, castlingMove);
            } else {
                moves[i] = buildMoveScore(move, quietHeuristicMoveScore(move, board.turn, maxRootQuietScore));
            }
        }

        sortMoves(moves, numberOfMoves);
    }

    static final void sortMoves(int[] moves, int numberOfMoves) {
        Arrays.sort(moves, 0, numberOfMoves);
        for (int i = 0, j = numberOfMoves - 1; i < j; i++, j--) {
            int tmp = moves[i];
            moves[i] = moves[j];
            moves[j] = tmp;
        }
    }

    static final void scoreMoves(int[] moves, Chessboard board, int ply,
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
            firstKiller = killerMoves[whichThread][ply];
            secondKiller = killerMoves[whichThread][ply + 1];
            firstOldKiller = ply >= 2 ? killerMoves[whichThread][ply - 2] : 0;
            secondOldKiller = ply >= 2 ? killerMoves[whichThread][ply - 2 + 1] : 0;
        }

        for (int i = 0; i < maxMoves; i++) {
            moves[i] = moves[i] & MOVE_MASK_WITH_CHECK;
            int move = moves[i];

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
            } else if (captureMove || isEnPassantMove(move)) {
                moves[i] = buildMoveScore(move, seeScore(board, move, whichThread));
            } else if (move == mateKiller) {
                moves[i] = buildMoveScore(move, mateKillerScore);
            } else if (firstKiller == move) {
                moves[i] = buildMoveScore(move, killerOneScore);
            } else if (secondKiller == move) {
                moves[i] = buildMoveScore(move, killerTwoScore);
            } else if (checkingMove(board, moves[i])) { // keeps moves[i] here
                moves[i] = MoveParser.setCheckingMove(moves[i]);
                Assert.assertTrue(MoveParser.isCheckingMove(moves[i]));
                moves[i] = buildMoveScore(moves[i], giveCheckMove);
            } else if (isCastlingMove(move)) {
                moves[i] = buildMoveScore(move, castlingMove);
            } else if (firstOldKiller == move) {
                moves[i] = buildMoveScore(move, oldKillerScoreOne);
            } else if (secondOldKiller == move) {
                moves[i] = buildMoveScore(move, oldKillerScoreTwo);
            } else {
                boolean pawnToSeven = MoveParser.moveIsPawnPushSeven(turn, move);
                if (pawnToSeven) {
                    moves[i] = buildMoveScore(move, pawnPushToSeven);
                    continue;
                }

                boolean pawnToSix = MoveParser.moveIsPawnPushSix(turn, move);
                if (pawnToSix) {
                    moves[i] = buildMoveScore(move, pawnPushToSix);
                    continue;
                }

                moves[i] = buildMoveScore(move, quietHeuristicMoveScore(move, turn, maxNodeQuietScore));
            }

            if (MASTER_DEBUG) {
                Assert.assertTrue(moves[i] != 0);
                Assert.assertTrue(moves[i] >= FIRST_FREE_BIT);
                Assert.assertTrue(moves[i] > MOVE_MASK_WITH_CHECK);
            }
        }
        
        sortMoves(moves, maxMoves);
    }

    private static int seeScore(Chessboard board, int move, int whichThread) {
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
            return neutralCapture + 1 + (see / 200);
        }

        return neutralCapture - 1 + (see / 200);
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


    static void scoreMovesQuiescence(int[] moves, Chessboard board, int whichThread) {
        final int maxMoves = moves[moves.length - 1];
        for (int i = 0; i < maxMoves; i++) { // todo, set quiets to 0?
            int move = moves[i];
            if (move == 0) {
                break;
            }

            if (isCaptureMove(move) || isEnPassantMove(move)) {
                if (isPromotionMove(move) && isPromotionToQueen(move)) {
                    moves[i] = buildMoveScore(move, queenCapturePromotionScore);
                } else {
                    moves[i] = buildMoveScore(move, seeScore(board, move, whichThread));
                }
            } else if (isPromotionMove(move) && (isPromotionToQueen(move))) {
                moves[i] = buildMoveScore(move, queenQuietPromotionScore);
            }
        }
        
        sortMoves(moves, maxMoves);
    }

    private static boolean checkingMove(Chessboard board, int move) { // todo, make cheaper
//        if (true) {
//            return false;
//        }
        
        boolean checkingMove;
        if (MASTER_DEBUG) {
            Assert.assertTrue(move != 0);
        }
        
        board.makeMoveAndFlipTurn(move);
        checkingMove = board.inCheck(board.isWhiteTurn());
        board.unMakeMoveAndFlipTurn();
        
        return checkingMove;
    }

    static void updateKillerMoves(int whichThread, int move, int ply) {
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

    public static int quietHeuristicMoveScore(int move, int turn, int maxScore) {
        int d = getDestinationIndex(move);
        int piece = getMovingPieceInt(move);
        if (piece > WHITE_KING) {
            piece -= 6;
        }
        int score = quietsILikeToMove[piece] * goodQuietDestinations[turn][63 - d];
        if (score > maxScore) {
            return maxScore;
        }

        if (score < uninterestingMove) {
            return uninterestingMove;
        }

        return score;
    }
}

