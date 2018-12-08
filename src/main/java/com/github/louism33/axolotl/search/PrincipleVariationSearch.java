package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.timemanagement.TimeAllocator;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.moveordering.KillerMoves.mateKiller;
import static com.github.louism33.axolotl.moveordering.KillerMoves.updateKillerMoves;
import static com.github.louism33.axolotl.search.FutilityPruning.futilityMargin;
import static com.github.louism33.axolotl.search.FutilityPruning.isFutilityPruningAllowedHere;
import static com.github.louism33.axolotl.search.InternalIterativeDeepening.iidDepthReduction;
import static com.github.louism33.axolotl.search.InternalIterativeDeepening.isIIDAllowedHere;
import static com.github.louism33.axolotl.search.LateMoveReductions.isLateMoveReductionAllowedHere;
import static com.github.louism33.axolotl.search.LateMoveReductions.lateMoveDepthReduction;
import static com.github.louism33.axolotl.search.NullMovePruning.*;
import static com.github.louism33.axolotl.search.Razoring.*;
import static com.github.louism33.axolotl.search.SEEPruning.seeScore;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.*;

class PrincipleVariationSearch {

    public static final TranspositionTable table = new TranspositionTable();

    static int principleVariationSearch(Chessboard board,
                                 long startTime, long timeLimitMillis,
                                 int originalDepth, int depth, int ply,
                                 int alpha, int beta,
                                 int nullMoveCounter, boolean reducedSearch) throws IllegalUnmakeException {

        boolean boardInCheck = board.inCheck(board.isWhiteTurn());

        if (Engine.getEngineSpecifications().ALLOW_EXTENSIONS) {
            depth += Extensions.extensions(board, ply, boardInCheck);
        }

        Assert.assertTrue(depth >= 0);
        
        if (TimeAllocator.outOfTime(startTime, timeLimitMillis) || Engine.isStopInstruction()) {
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }
        
        /*
        Quiescent Search:
        if at a leaf node, perform specialised search of captures to avoid horizon effect
         */
        if (depth <= 0){
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }

        Assert.assertTrue(depth >= 1);

        /*
        Mate Distance Pruning:
        prefer closer wins and further loses 
         */
        if (Engine.getEngineSpecifications().ALLOW_MATE_DISTANCE_PRUNING){
            alpha = Math.max(alpha, IN_CHECKMATE_SCORE + ply);
            beta = Math.min(beta, -IN_CHECKMATE_SCORE - ply - 1);
            if (alpha >= beta){
                return alpha;
            }
        }
        
        /*
        Transposition Table Lookup:
        if possible, retrieve previously found data from singleton transposition table 
         */
        int hashMove = 0;
        int score;
        long previousTableData = TranspositionTable.retrieveFromTable(board.getZobrist());
        if (previousTableData != 0 && ply > 0 && previousTableData == board.getZobrist()) {
            score = TranspositionTable.getScore(previousTableData);
            hashMove = TranspositionTable.getMove(previousTableData);
            if (TranspositionTable.getDepth(previousTableData) >= depth) {
                int flag = TranspositionTable.getFlag(previousTableData);
                if (flag == EXACT) {
                    Engine.statistics.numberOfExacts++;
                    return score;
                } else if (flag == LOWERBOUND) {
                    Engine.statistics.numberOfLowerBounds++;
                    alpha = Math.max(alpha, score);
                } else if (flag == UPPERBOUND) {
                    Engine.statistics.numberOfUpperBounds++;
                    beta = Math.min(beta, score);
                }
                if (alpha >= beta) {
                    Engine.statistics.numberOfHashBetaCutoffs++;
                    return score;
                }
            }
        }
        
        /*
        Principle Variation:
        only perform certain reductions if we are not in the most likely branch of the tree
         */
        int staticBoardEval = SHORT_MINIMUM;
        boolean thisIsAPrincipleVariationNode = (beta - alpha != 1);
        if (!thisIsAPrincipleVariationNode && !boardInCheck) {
            int[] moves = board.generateLegalMoves();
            staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(), moves);

            if (previousTableData != 0){
                int flag = TranspositionTable.getFlag(previousTableData);
                int tableScore = TranspositionTable.getScore(previousTableData);
                if (flag == EXACT
                        || (flag == UPPERBOUND && tableScore < staticBoardEval)
                        || (flag == LOWERBOUND && tableScore > staticBoardEval)){
                    staticBoardEval = tableScore;
                }
            }
            
            /*
            Beta Razoring:
            if current node has a very high score, return eval
             */
            if (Engine.getEngineSpecifications().ALLOW_BETA_RAZORING){
                if (isBetaRazoringMoveOkHere(board, depth, staticBoardEval)){
                    int specificBetaRazorMargin = betaRazorMargin[depth];
                    if (staticBoardEval - specificBetaRazorMargin >= beta){
                        Engine.statistics.numberOfSuccessfulBetaRazors++;
                        return staticBoardEval;
                    }
                }
            }
            
            
            /*
            Alpha Razoring:
            if current node has a very low score, perform Quiescence search to try to find a cutoff
             */
            if (Engine.getEngineSpecifications().ALLOW_ALPHA_RAZORING){
                if (isAlphaRazoringMoveOkHere(board, depth, alpha)){
                    int specificAlphaRazorMargin = alphaRazorMargin[depth];
                    if (staticBoardEval + specificAlphaRazorMargin < alpha){
                        int qScore = QuiescenceSearch
                                .quiescenceSearch(board,
                                        alpha - specificAlphaRazorMargin,
                                        alpha - specificAlphaRazorMargin + 1);

                        if (qScore + specificAlphaRazorMargin <= alpha){
                            Engine.statistics.numberOfSuccessfulAlphaRazors++;
                            return qScore;
                        }
                        Engine.statistics.numberOfFailedAlphaRazors++;
                    }
                }
            }
            
            /*
            Null Move Pruning:
            if not in dangerous position, forfeit a move and make shallower null window search
             */
            if (Engine.getEngineSpecifications().ALLOW_NULL_MOVE_PRUNING) {
                if (nullMoveCounter < 2 && !reducedSearch && isNullMoveOkHere(board)) {
                    Assert.assertTrue(depth >= 1);
                    Assert.assertTrue(alpha < beta);

                    board.makeNullMoveAndFlipTurn();

                    int reducedDepth = depth - nullMoveDepthReduction(depth) - 1;

                    int nullScore = reducedDepth <= 0 ?

                            -QuiescenceSearch.quiescenceSearch(board,
                                    -beta,
                                    -beta + 1)

                            : -principleVariationSearch(board, startTime, timeLimitMillis,
                            originalDepth, reducedDepth, ply + 1,
                            -beta, -beta + 1, nullMoveCounter + 1, true);

                    board.unMakeNullMoveAndFlipTurn();

                    if (nullScore >= beta) {

                        if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                            nullScore = beta;
                        }

                        Engine.statistics.numberOfNullMoveHits++;
                        return nullScore;
                    }
                    Engine.statistics.numberOfNullMoveMisses++;
                }
            }
        }
        
        /*
        Move Ordering:
        place moves most likely to cause cutoffs at the front of the move list (hashmoves, killers, captures)
         */
        int[] moves;
        if (previousTableData == 0) {
            /*
            Internal Iterative Deepening:
            when no hashtable entry, pv node and not endgame, perform shallower search to add a good move to table
             */
            if (Engine.getEngineSpecifications().ALLOW_INTERNAL_ITERATIVE_DEEPENING){
                if (isIIDAllowedHere(board, depth, reducedSearch, thisIsAPrincipleVariationNode)){
                    Engine.statistics.numberOfIIDs++;
                    int reducedIIDDepth = depth - iidDepthReduction - 1;

                    principleVariationSearch(board,
                            startTime, timeLimitMillis, originalDepth,
                            reducedIIDDepth, ply,
                            alpha, beta, nullMoveCounter, true);

//                    previousTableData = table.get(board.getBoardHash());
                    previousTableData = TranspositionTable.retrieveFromTable(board.getZobrist());
                    if (previousTableData == 0){
                        Engine.statistics.numberOfFailedIIDs++;
                    }
                    else {
                        Engine.statistics.numberOfSuccessfulIIDs++;
                    }
                }
            }
        }

        if (previousTableData != 0) {
            Engine.statistics.numberOfSearchesWithHash++;
            moves = board.generateLegalMoves();
            MoveOrderer.scoreMoves(moves, board, board.isWhiteTurn(), ply, hashMove);

        }
        else{
            Engine.statistics.numberOfSearchesWithoutHash++;
            moves = board.generateLegalMoves();
            MoveOrderer.scoreMoves(moves, board, board.isWhiteTurn(), ply, 0);
        }

        int originalAlpha = alpha;
        int bestScore = SHORT_MINIMUM;
        int bestMove = 0;
        
        /*
        iterate through fully legal moves
         */
        int realMoves = MoveParser.numberOfRealMoves(moves);
        Ints.sortDescending(moves, 0, realMoves);
        int numberOfMovesSearched = 0;
        for (int i = 0; i < moves.length; i++) {

            if (moves[i] == 0) {
                break;
            }
            
            int move = moves[i];
            
            if (i == 0){
                Assert.assertTrue(move >= moves[i+1]);
            } else {
                Assert.assertTrue(move <= moves[i - 1]);
                Assert.assertTrue(move >= moves[i + 1]);
            }

            boolean captureMove = MoveParser.isCaptureMove(move);
            boolean promotionMove = MoveParser.isPromotionMove(move);
            boolean givesCheckMove = MoveOrderer.checkingMove(board, move);
            boolean pawnToSix = MoveParser.moveIsPawnPushSix(move);
            boolean pawnToSeven = MoveParser.moveIsPawnPushSeven(move);

            if (!maybeInEndgame(board)
                    && (MoveParser.isPromotionToKnight(move) ||
                    MoveParser.isPromotionToBishop(move) ||
                    MoveParser.isPromotionToRook(move))) {
                continue;
            }

            /*
            Can we prune this move?
             */
            if (!thisIsAPrincipleVariationNode && !boardInCheck && numberOfMovesSearched > 0) {
                if (!captureMove) {
                    /*
                    Late Move Pruning:
                    before making move, see if we can prune this move
                     */
                    if (Engine.getEngineSpecifications().ALLOW_LATE_MOVE_PRUNING) {
                        if (bestScore < CHECKMATE_ENEMY_SCORE_MAX_PLY
                                && !onlyPawnsLeftForPlayer(board, board.isWhiteTurn())) {
                            if (!promotionMove
                                    && !givesCheckMove
                                    && !pawnToSix
                                    && !pawnToSeven
                                    && depth <= 4
                                    && numberOfMovesSearched >= depth * 3 + 4) {

                                Engine.statistics.numberOfLateMovePrunings++;
                                continue;
                            }
                        }
                    }
                
                    /*
                    (Extended) Futility Pruning:
                    if score + margin smaller than alpha, skip this move
                     */
                    if (Engine.getEngineSpecifications().ALLOW_FUTILITY_PRUNING) {
                        if (isFutilityPruningAllowedHere(board, move, depth,
                                promotionMove, givesCheckMove, pawnToSix, pawnToSeven)) {

                            if (staticBoardEval == SHORT_MINIMUM) {
                                staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(),
                                        board.generateLegalMoves());
                            }

                            int futilityScore = staticBoardEval + futilityMargin[depth];

                            if (futilityScore <= alpha) {
                                Engine.statistics.numberOfSuccessfulFutilities++;
                                if (futilityScore > bestScore) {
                                    bestScore = futilityScore;
                                }
                                continue;
                            } else {
                                Engine.statistics.numberOfFailedFutilities++;
                            }
                        }
                    }
                } else {
                    /*
                    Static Exchange Evaluation:
                    if an exchange promises a large loss of material skip it, more if further from root
                     */
                    if (Engine.getEngineSpecifications().ALLOW_SEE_PRUNING) {
                        if (depth <= 5) {
                            int seeScore = seeScore(board, move);
                            if (seeScore < -100 * depth) {
                                Engine.statistics.numberOfSuccessfulSEEs++;
                                continue;
                            }
                        }
                    }
                }
            }

            board.makeMoveAndFlipTurn(move);
            numberOfMovesSearched++;
            Engine.statistics.numberOfMovesMade++;

            boolean enemyInCheck = board.inCheck(board.isWhiteTurn());

            if (board.drawByRepetition(board.isWhiteTurn())) {
                score = IN_STALEMATE_SCORE;
            } else { 
                /*
                score now above alpha, therefore if no special cases apply, we do a full search
                 */
                score = alpha + 1;

                /*
                Late Move Reductions:
                search later ordered safer moves to a lower depth
                 */
                if (Engine.getEngineSpecifications().ALLOW_LATE_MOVE_REDUCTIONS
                        && isLateMoveReductionAllowedHere(board, move, depth,
                        numberOfMovesSearched, reducedSearch, captureMove,
                        givesCheckMove, promotionMove, pawnToSix, pawnToSeven)) {

                    Engine.statistics.numberOfLateMoveReductions++;
                
                    /*
                    lower depth search
                     */
                    int lowerDepth = depth - lateMoveDepthReduction(depth) - 1;

                    score = -principleVariationSearch
                            (board, startTime, timeLimitMillis,
                                    originalDepth, lowerDepth, ply + 1,
                                    -alpha - 1, -alpha, 0, true);

                    /*
                    if a lower move seems good, full depth research
                     */
                    if (score > alpha) {
                        score = -principleVariationSearch(board,
                                startTime, timeLimitMillis,
                                originalDepth, depth - 1, ply + 1,
                                -alpha - 1, -alpha, 0, false);

                        Engine.statistics.numberOfLateMoveReductionsMisses++;
                    } else {
                        Engine.statistics.numberOfLateMoveReductionsHits++;
                    }
                }
            
                /*
                Principle Variation Search:
                moves that are not favourite (PV) are searched with a null window
                 */
                else if (Engine.getEngineSpecifications().ALLOW_PRINCIPLE_VARIATION_SEARCH
                        && numberOfMovesSearched > 1) {

                    score = -principleVariationSearch(board,
                            startTime, timeLimitMillis,
                            originalDepth, depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0, reducedSearch);
                
                    /*
                    if this line of play would improve our score, do full re-search (implemented slightly lower down)
                     */
                    if (score > alpha) {
                        Engine.statistics.numberOfPVSMisses++;
                    } else {
                        Engine.statistics.numberOfPVSHits++;
                    }
                }

                /*
                always search PV node fully + full re-search of moves that showed promise
                 */
                if (score > alpha) {
                    score = -principleVariationSearch(board,
                            startTime, timeLimitMillis,
                            originalDepth, depth - 1, ply + 1,
                            -beta, -alpha, 0, false);
                }
            }

            board.unMakeMoveAndFlipTurn();
            /*
            record score and move if better than previous ones
             */
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, score);
                if (ply == 0) {
//                    aiMove = move;
                    Engine.aiMove = move;
                }
            }

            /*
            Alpha Beta Pruning:
            represents a situation which is too good, or too bad, and will not occur in normal play, so stop searching further
             */
            if (alpha >= beta) {
                Engine.statistics.statisticsFailHigh(ply, numberOfMovesSearched, move);

                if (!MoveParser.isCaptureMove(move)) {
                    /*
                    Killer Moves:
                    record this cutoff move, because we will try out in sister nodes
                     */
                    if (Engine.getEngineSpecifications().ALLOW_KILLERS) {
                        updateKillerMoves(move, ply);
                    }
                    if (Engine.getEngineSpecifications().ALLOW_HISTORY_MOVES) {
                        MoveOrderer.updateHistoryMoves(move, ply);
                    }

                    if (Engine.getEngineSpecifications().ALLOW_MATE_KILLERS) {
                        if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY) {
                            mateKiller[ply] = move;
                        }
                    }
                }
                break;
            }
        }

        if (numberOfMovesSearched == 0) {
            if (boardInCheck) {
                Engine.statistics.numberOfCheckmates++;
                return IN_CHECKMATE_SCORE + ply;
            }
            else {
                Engine.statistics.numberOfStalemates++;
                return IN_STALEMATE_SCORE;
            }
        }

        /*
        Transposition Tables:
        add information to table with flag based on the node type
         */
        int flag;
        if (bestScore <= originalAlpha){
            flag = UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = LOWERBOUND;
        } else {
            flag = EXACT;
        }

        TranspositionTable.addToTable(board.getZobrist(), 
                TranspositionTable.buildTableEntry(bestMove, bestScore, depth, flag, ply));
        
        return bestScore;
    }

//    static int getAiMove() {
//        return aiMove;
//    }

}
