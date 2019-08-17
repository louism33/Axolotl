package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;

import static challenges.Utils.contains;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.MASTER_DEBUG;
import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.*;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KING;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

public final class Quiescence {

    static int selDepth = 0;

    public static int quiescenceSearch(Chessboard board, int alpha, int beta, int whichThread, int ply, int depth) {

        if (whichThread == MASTER_THREAD) {
            selDepth = Math.max(selDepth, ply);
        }

        long checkers = board.getCheckers();

        int standPatScore = SHORT_MINIMUM;

        boolean inCheck = board.inCheckRecorder;

        if (!inCheck) {
            if (MASTER_DEBUG) {
                Assert.assertFalse(board.inCheck());
            }

            standPatScore = Evaluator.eval(board, whichThread);

            if (standPatScore >= beta) {
                return standPatScore;
            }

            if (standPatScore + 1400 < alpha) {
                quiescenceFutility++;
                return alpha;
            }

            if (standPatScore > alpha) {
                alpha = standPatScore;
            }
        }

        if (MASTER_DEBUG) {
            Assert.assertFalse(standPatScore > CHECKMATE_ENEMY_SCORE_MAX_PLY);
        }

        int[] moves = board.generateLegalMoves(checkers);
        
        if (!inCheck) {
            scoreMovesQuiescenceNew(moves, board, ply, whichThread);
        } else {
            // todo consider tableprobe
            scoreMovesNew(moves, board, ply, 0, whichThread);
            // todo consider simply throwing back to pvs
        }

        int numberOfQMovesSearched = 0;
        int[] nextBestMoveIndexAndScore;
        int move;
        int moveScore;
        
        for (int i = 0; i < moves.length; i++) {

            nextBestMoveIndexAndScore = getNextBestMoveIndexAndScore(whichThread, ply, false);

            Assert.assertEquals(moves[moves.length - 1], scores[whichThread][ply][scores[whichThread][ply].length - 1]);

            move = moves[nextBestMoveIndexAndScore[INDEX]];
            moveScore = nextBestMoveIndexAndScore[SCORE];

            if (!inCheck && (moveScore == dontSearchMeScore || moveScore == previouslySearchedScore)) {
                break;
            }
            
//            final int move = moves[i];
            if (move == 0 || moveScore == dontSearchMeScore || moveScore == previouslySearchedScore) {
                break;
            }

//            final int moveScore = getMoveScore(move);
            
            if (!inCheck && moveScore == 0) {
                break;
            }
            final boolean captureMove = MoveParser.isCaptureMove(move);
            final boolean epMove = MoveParser.isEnPassantMove(move);
            final boolean promotionMove = MoveParser.isPromotionMove(move);
            final boolean interestingMove = captureMove || epMove || promotionMove;

            // todo, checking move?
            
            if (MASTER_DEBUG) {
                if (!inCheck && moveScore != 0) {
                    final boolean condition = captureMove || promotionMove || epMove;
                    if (!condition) {
                        System.out.println(board);
                        MoveParser.printMove(move);
                        MoveParser.printMove(moves);
                        System.out.println(Arrays.toString(scores[whichThread][ply]));
                        System.out.println("moveScore: " + moveScore);
                        System.out.println("index: " + nextBestMoveIndexAndScore[INDEX]);
                        System.out.println();
                    }
                    Assert.assertTrue(condition);
                }
            }

            final int loudMove = move & MOVE_MASK_WITH_CHECK;

            if (MASTER_DEBUG) {
//                if (!inCheck) {
//                    if (i == 0) {
//                        Assert.assertTrue(moves[i] >= moves[i + 1]);
//                    } else {
//                        Assert.assertTrue(moves[i] <= moves[i - 1]);
//                        Assert.assertTrue(moves[i] >= moves[i + 1]);
//                    }
//                    Assert.assertTrue(moves[i] > FIRST_FREE_BIT);
//                }

                if (!inCheck) {
                    Assert.assertTrue(captureMove || promotionMove || epMove);
                }
                Assert.assertEquals(MaterialHashUtil.makeMaterialHash(board), board.materialHash);
                Assert.assertEquals(MaterialHashUtil.typeOfEndgame(board), board.typeOfGameIAmIn);
            }

            if (!inCheck) {
                int victimPiece = MoveParser.getVictimPieceInt(move);
                if (epMove) {
                    victimPiece = WHITE_PAWN;
                }
                if (victimPiece > WHITE_KING) {
                    victimPiece -= 6;
                }
                if (!promotionMove && standPatScore + 200 + SEE.scores[victimPiece] < alpha) { //todo, don't do this in endgame
//                if (!promotionMove && standPatScore + 250 + endMaterial[victimPiece - 1] < alpha) { //todo, don't do this in endgame
//                    if (victimPiece == WHITE_PAWN) {
//                        Assert.assertEquals(100, endMaterial[victimPiece - 1]);
//                    }
                    quiescenceDelta++;
                    final boolean condition = MoveParser.isCaptureMove(move) || MoveParser.isEnPassantMove(move);
                    if (!condition) {
                        System.out.println(board);
                        MoveParser.printMove(move);
                        System.out.println("moveScore: " + moveScore);
                        System.out.println();
                    }
                    Assert.assertTrue(condition);
                    continue;
                }
            }

            // idea from stockfish
            boolean evasionPrunable = inCheck
                    && (depth != 0 && numberOfQMovesSearched > 2)
                    && alpha > IN_CHECKMATE_SCORE_MAX_PLY
                    && !interestingMove;

            if (!inCheck || evasionPrunable) {
                if (moveScore < neutralCapture) {
                    if (inCheck) {
                        Assert.assertTrue(!interestingMove);
                        Assert.assertTrue(numberOfQMovesSearched > 0);
                    }
                    quiescenceSEE++;
                    continue;
                }
            }

            board.makeMoveAndFlipTurn(loudMove);
            numberOfQMovesSearched++;
            Engine.numberOfQMovesMade[whichThread]++;

            int score;

            int[] movesCopyForDebug = null;
            int[] scoresCopyForDebug = null;
            if (MASTER_DEBUG) {
                movesCopyForDebug = new int[moves.length];
                final int[] myScores = scores[whichThread][ply];
                scoresCopyForDebug = new int[myScores.length];
                System.arraycopy(moves, 0, movesCopyForDebug, 0, moves.length);
                System.arraycopy(myScores, 0, scoresCopyForDebug, 0, myScores.length);
            }


            
            if (board.isDrawByInsufficientMaterial() // todo , optimise here
                    || (!captureMove && !promotionMove && !epMove &&
                    (board.isDrawByRepetition(1) || board.isDrawByFiftyMoveRule()))) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = -quiescenceSearch(board, -beta, -alpha, whichThread, ply + 1, depth - 1);
            }
            
            if (MASTER_DEBUG) {
                final int[] myScores = scores[whichThread][ply];
                for (int index = 0; index < moves.length; index++) {
                    Assert.assertTrue(contains(moves, movesCopyForDebug[index]));
                    Assert.assertTrue(contains(myScores, scoresCopyForDebug[index]));
                    Assert.assertTrue(contains(movesCopyForDebug, moves[index]));
                    Assert.assertTrue(contains(scoresCopyForDebug, myScores[index]));
                }
            }
            

            board.unMakeMoveAndFlipTurn();

            // todo, timeout?

            if (score > alpha) {
                alpha = score;
                if (alpha >= beta) {
                    return alpha;
                }
            }

        }

        if (inCheck && numberOfQMovesSearched == 0) {
            return IN_CHECKMATE_SCORE + ply;
        }

        return alpha;
    }

}
