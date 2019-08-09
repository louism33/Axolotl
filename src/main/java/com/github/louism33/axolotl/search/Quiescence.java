

package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.MASTER_DEBUG;
import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.neutralCapture;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KING;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.MoveConstants.FIRST_FREE_BIT;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

public final class Quiescence {

    static int selDepth = 0;

    public static int quiescenceSearch(Chessboard board, int alpha, int beta, int whichThread, int ply, int depth) {

        if (whichThread == MASTER_THREAD) {
            selDepth = Math.max(selDepth, ply);
        }

        long checkers = board.getCheckers();

//        int[] moves = board.generateLegalMoves(checkers);

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
            scoreMovesQuiescence(moves, board, whichThread);
        } else {
            // todo consider tableprobe
            scoreMoves(moves, board, ply, 0, whichThread, false);
        }

        int numberOfQMovesSearched = 0;

        for (int i = 0; i < moves.length; i++) {

            final int move = moves[i];
            if (move == 0) {
                break;
            }

            final int loudMoveScore = getMoveScore(move);
            if (!inCheck && loudMoveScore == 0) {
                break;
            }
            final boolean captureMove = MoveParser.isCaptureMove(move);
            final boolean epMove = MoveParser.isEnPassantMove(move);
            final boolean promotionMove = MoveParser.isPromotionMove(move);
            final boolean interestingMove = captureMove || epMove || promotionMove;

            if (MASTER_DEBUG) {
                if (!inCheck && loudMoveScore != 0) {
                    Assert.assertTrue(captureMove || promotionMove || epMove);
                }
            }

            final int loudMove = move & MOVE_MASK_WITH_CHECK;

            if (MASTER_DEBUG) {
                if (!inCheck) {
                    if (i == 0) {
                        Assert.assertTrue(moves[i] >= moves[i + 1]);
                    } else {
                        Assert.assertTrue(moves[i] <= moves[i - 1]);
                        Assert.assertTrue(moves[i] >= moves[i + 1]);
                    }
                    Assert.assertTrue(moves[i] > FIRST_FREE_BIT);
                }

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
                    Assert.assertTrue(MoveParser.isCaptureMove(move) || MoveParser.isEnPassantMove(move));
                    continue;
                }
            }

            // idea from stockfish
            boolean evasionPrunable = inCheck
                    && (depth != 0 && numberOfQMovesSearched > 2)
                    && alpha > IN_CHECKMATE_SCORE_MAX_PLY
                    && !interestingMove;

            if (!inCheck || evasionPrunable) {
                if (loudMoveScore < neutralCapture) {
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

            if (board.isDrawByInsufficientMaterial()
                    || (!captureMove && !promotionMove && !epMove &&
                    (board.isDrawByRepetition(1) || board.isDrawByFiftyMoveRule()))) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = -quiescenceSearch(board, -beta, -alpha, whichThread, ply + 1, depth - 1);
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



/**
package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.Engine.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.MASTER_DEBUG;
import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.neutralCapture;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KING;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.MoveConstants.FIRST_FREE_BIT;
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
            scoreMovesQuiescence(moves, board, whichThread);
        } else {
            // todo consider tableprobe
            scoreMoves(moves, board, ply, 0, whichThread, false);
        }

        int numberOfQMovesSearched = 0;

        for (int i = 0; i < moves.length; i++) {

            final int move = moves[i];
            if (move == 0) {
                break;
            }

            final int loudMoveScore = getMoveScore(move);
            if (!inCheck && loudMoveScore == 0) {
                break;
            }
            final boolean captureMove = MoveParser.isCaptureMove(move);
            final boolean epMove = MoveParser.isEnPassantMove(move);
            final boolean promotionMove = MoveParser.isPromotionMove(move);
            final boolean interestingMove = captureMove || epMove || promotionMove;

            // todo, checking move?
            
            if (MASTER_DEBUG) {
                if (!inCheck && loudMoveScore != 0) {
                    Assert.assertTrue(captureMove || promotionMove || epMove);
                }
            }

            final int loudMove = move & MOVE_MASK_WITH_CHECK;

            if (MASTER_DEBUG) {
                if (!inCheck) {
                    if (i == 0) {
                        Assert.assertTrue(moves[i] >= moves[i + 1]);
                    } else {
                        Assert.assertTrue(moves[i] <= moves[i - 1]);
                        Assert.assertTrue(moves[i] >= moves[i + 1]);
                    }
                    Assert.assertTrue(moves[i] > FIRST_FREE_BIT);
                }

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
                    Assert.assertTrue(MoveParser.isCaptureMove(move) || MoveParser.isEnPassantMove(move));
                    continue;
                }
            }

            // idea from stockfish
            boolean evasionPrunable = inCheck
                    && (depth != 0 && numberOfQMovesSearched > 2)
                    && alpha > IN_CHECKMATE_SCORE_MAX_PLY
                    && !interestingMove;

            if (!inCheck || evasionPrunable) {
                if (loudMoveScore < neutralCapture) {
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

            if (board.isDrawByInsufficientMaterial()
                    || (!captureMove && !promotionMove && !epMove &&
                    (board.isDrawByRepetition(1) || board.isDrawByFiftyMoveRule()))) {
                score = IN_STALEMATE_SCORE;
            } else {
                score = -quiescenceSearch(board, -beta, -alpha, whichThread, ply + 1, depth - 1);
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

*/