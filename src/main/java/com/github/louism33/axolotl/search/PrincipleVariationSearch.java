//package com.github.louism33.axolotl.search;
//
//import com.github.louism33.axolotl.evaluation.Evaluator;
//import com.github.louism33.axolotl.moveordering.MoveOrderer;
//import com.github.louism33.axolotl.helper.protocolhelperclasses.PVLine;
//import com.github.louism33.axolotl.helper.timemanagement.TimeAllocator;
//import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
//import com.github.louism33.chesscore.Chessboard;
//import com.github.louism33.chesscore.IllegalUnmakeException;
//import com.github.louism33.chesscore.MoveParser;
//import com.google.common.primitives.Ints;
//import org.junit.Assert;
//
//import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
//import static com.github.louism33.axolotl.moveordering.KillerMoves.mateKiller;
//import static com.github.louism33.axolotl.moveordering.KillerMoves.updateKillerMoves;
//import static com.github.louism33.axolotl.search.FutilityPruning.futilityMargin;
//import static com.github.louism33.axolotl.search.FutilityPruning.isFutilityPruningAllowedHere;
//import static com.github.louism33.axolotl.search.InternalIterativeDeepening.iidDepthReduction;
//import static com.github.louism33.axolotl.search.InternalIterativeDeepening.isIIDAllowedHere;
//import static com.github.louism33.axolotl.search.LateMoveReductions.isLateMoveReductionAllowedHere;
//import static com.github.louism33.axolotl.search.LateMoveReductions.lateMoveDepthReduction;
//import static com.github.louism33.axolotl.search.NullMovePruning.*;
//import static com.github.louism33.axolotl.search.Razoring.*;
//import static com.github.louism33.axolotl.search.SEEPruning.seeScore;
//import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;
//
//class PrincipleVariationSearch {
//
//                if (false && nullMoveCounter < 2 && !reducedSearch && isNullMoveOkHere(board)
//                    && (depth - nullMoveDepthReduction(depth) - 1 > 0)) {
//
//        Assert.assertTrue(alpha < beta);
//
//        board.makeNullMoveAndFlipTurn();
//
//        int reducedDepth = depth - nullMoveDepthReduction(depth) - 1;
//
//        int nullScore = -principleVariationSearch(board, startTime, timeLimitMillis,
//                originalDepth, reducedDepth, ply + 1,
//                -beta, -beta + 1, nullMoveCounter + 1, true);
//
//        board.unMakeNullMoveAndFlipTurn();
//
//        if (nullScore >= beta) {
//            if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
//                nullScore = beta;
//            }
//            return nullScore;
//        }
//    }
//}
//
//}
