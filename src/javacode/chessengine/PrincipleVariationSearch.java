package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveParser;
import javacode.evaluation.Evaluator;
import javacode.graphicsandui.Art;
import org.junit.Assert;

import java.util.List;

import static javacode.chessengine.Engine.*;
import static javacode.chessengine.EngineMovesAndHash.*;
import static javacode.chessengine.Extensions.extensions;
import static javacode.chessengine.FutilityPruning.*;
import static javacode.chessengine.HistoryMoves.updateHistoryMoves;
import static javacode.chessengine.InternalIterativeDeepening.iidDepthReduction;
import static javacode.chessengine.InternalIterativeDeepening.isIIDAllowedHere;
import static javacode.chessengine.KillerMoves.killerMoves;
import static javacode.chessengine.KillerMoves.mateKiller;
import static javacode.chessengine.KillerMoves.updateKillerMoves;
import static javacode.chessengine.LateMovePruning.isLateMovePruningMoveOkHere;
import static javacode.chessengine.LateMoveReductions.isLateMoveReductionAllowedHere;
import static javacode.chessengine.LateMoveReductions.lateMoveDepthReduction;
import static javacode.chessengine.MoveOrderer.*;
import static javacode.chessengine.NullMovePruning.isNullMoveOkHere;
import static javacode.chessengine.NullMovePruning.nullMoveDepthReduction;
import static javacode.chessengine.QuiescenceSearch.quiescenceSearch;
import static javacode.chessengine.Razoring.*;
import static javacode.chessengine.TranspositionTable.TableObject;
import static javacode.chessengine.TranspositionTable.TableObject.Flag;
import static javacode.chessengine.TranspositionTable.TableObject.Flag.*;
import static javacode.chessengine.TranspositionTable.getInstance;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.chess.Copier.copyMove;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.evaluation.Evaluator.*;
import static javacode.graphicsandui.Art.*;

class PrincipleVariationSearch {

    private static final TranspositionTable table = getInstance();
    private static Move aiMove;

    static int principleVariationSearch(Chessboard board, ZobristHash zobristHash,
                                        long startTime, long timeLimitMillis,
                                        int originalDepth, int depth, int ply,
                                        int alpha, int beta,
                                        int nullMoveCounter, boolean reducedSearch){
        int originalAlpha = alpha;
        int bestScore = SHORT_MINIMUM;
        
        if (HEAVY_DEBUG){
            long testBoardHash = new ZobristHash(board).getBoardHash();
            Assert.assertEquals(testBoardHash, zobristHash.getBoardHash());
        }

        depth += extensions(board, ply);

        Assert.assertTrue(depth >= 0);

        if (ALLOW_TIME_LIMIT) {
            long currentTime = System.currentTimeMillis();
            long timeLeft = startTime + timeLimitMillis - currentTime;
            if (timeLeft < 0) {
                return alpha;
            }
        }
        
        /*
        Quiescent Search:
        if at a leaf node, perform specialised search of captures to avoid horizon effect
         */
        if (depth <= 0){
            if(ALLOW_EXTENSIONS) {
                Assert.assertTrue(!boardInCheck(board, board.isWhiteTurn()));
            }
            return quiescenceSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    alpha, beta);
        }

        Assert.assertTrue(depth >= 1);
        
        /*
        Mate Distance Pruning:
        prefer closer wins and further loses 
         */
        if (ALLOW_MATE_DISTANCE_PRUNING){
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
        Move hashMove = null;
        TableObject previousTableData = table.get(zobristHash.getBoardHash());
        int score;
        if (previousTableData != null) {
            if (previousTableData.getDepth() >= depth) {
                Flag flag = previousTableData.getFlag();
                score = previousTableData.getScore();
                hashMove = previousTableData.getMove();
                if (flag == EXACT) {
                    numberOfExacts++;
                    if (ply == 0){
                        aiMove = copyMove(hashMove);
                    }
                    return score;
                } else if (flag == LOWERBOUND) {
                    numberOfLowerBounds++;
                    alpha = Math.max(alpha, score);
                } else if (flag == UPPERBOUND) {
                    numberOfUpperBounds++;
                    beta = Math.min(beta, score);
                }
                if (alpha >= beta) {
                    numberOfHashBetaCutoffs++;
                    if (ply == 0){
                        aiMove = copyMove(hashMove);
                    }
                    return score;
                }
            }
        }
        
        /*
        Principle Variation:
        only perform certain reductions if we are not in the most likely branch of the tree
         */
        int staticBoardEval = SHORT_MINIMUM;
        final boolean thisIsAPrincipleVariationNode = beta - alpha != 1;
        final boolean boardInCheck = boardInCheck(board, board.isWhiteTurn());
        if (!thisIsAPrincipleVariationNode && !boardInCheck) {

            List<Move> moves = generateLegalMoves(board, board.isWhiteTurn());
            staticBoardEval = eval(board, board.isWhiteTurn(), moves);
            
            /*
            Beta Razoring:
            if current node has a very high score, return eval
             */
            if (ALLOW_BETA_RAZORING){
                if (isBetaRazoringMoveOkHere(board, depth, staticBoardEval)){
                    final int specificBetaRazorMargin = betaRazorMargin[depth];
                    if (staticBoardEval - specificBetaRazorMargin >= beta){
                        if (DEBUG){
                            numberOfSuccessfulBetaRazors++;
                        }
                        return staticBoardEval;
                    }
                }
            }
            
            
            /*
            Alpha Razoring:
            if current node has a very low score, perform Quiescence search to try to find a cutoff
             */
            if (ALLOW_ALPHA_RAZORING){
                if (isAlphaRazoringMoveOkHere(board, depth, alpha)){
                    final int specificAlphaRazorMargin = alphaRazorMargin[depth];
                    if (staticBoardEval + specificAlphaRazorMargin < alpha){
                        final int qScore = quiescenceSearch(board, zobristHash, startTime, timeLimitMillis,
                                alpha - specificAlphaRazorMargin, alpha - specificAlphaRazorMargin + 1);

                        if (qScore + specificAlphaRazorMargin <= alpha){
                            if (DEBUG){
                                numberOfSuccessfulAlphaRazors++;
                            }
                            return qScore;
                        }
                        else if (DEBUG){
                            numberOfFailedAlphaRazors++;
                        }
                    }
                }
            }
            
            /*
            Null Move Pruning:
            if not in dangerous position, forfeit a move and make shallower null window search
             */
            if (ALLOW_NULL_MOVE_PRUNING) {
                if (nullMoveCounter < 2 && isNullMoveOkHere(board)) {
                    Assert.assertTrue(depth >= 1);
                    Assert.assertTrue(alpha < beta);

                    makeNullMove(board, zobristHash);

                    int reducedDepth = depth - nullMoveDepthReduction(depth) - 1;

                    int nullScore = reducedDepth <= 0 ?

                            -quiescenceSearch(board, zobristHash, startTime, timeLimitMillis,
                                    -beta, -beta + 1)

                            : -principleVariationSearch(board, zobristHash, startTime, timeLimitMillis,
                            originalDepth, reducedDepth, ply + 1,
                            -beta, -beta + 1, nullMoveCounter + 1, true);

                    unMakeNullMove(board, zobristHash);

                    if (nullScore >= beta) {

                        if (nullScore > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                            nullScore = beta;
                        }

                        if (DEBUG) {
                            numberOfNullMoveHits++;
                        }
                        return nullScore;
                    }
                    if (DEBUG) {
                        numberOfNullMoveMisses++;
                    }
                }
            }
        }
        
        /*
        Move Ordering:
        place moves most likely to cause cutoffs at the front of the move list (hashmoves, killers, captures)
         */
        List<Move> orderedMoves;
        if (hashMove != null) {
            orderedMoves = orderedMoves(board, board.isWhiteTurn(), ply, hashMove);
            if (DEBUG) {
                numberOfSearchesWithHash++;
            }
        }
        else {
            if (DEBUG) {
                numberOfSearchesWithoutHash++;
            }
            
            /*
            Internal Iterative Deepening:
            when no move can be retrieved from hashtable, perform shallower search to add a good move to table
             */
            Move moveFromIID = null;
            if (ALLOW_INTERNAL_ITERATIVE_DEEPENING){
                if (isIIDAllowedHere(board, depth, reducedSearch, thisIsAPrincipleVariationNode)){
                    if (DEBUG){
                        numberOfIIDs++;
                    }

                    principleVariationSearch(board, zobristHash,
                            startTime, timeLimitMillis, originalDepth,
                            depth - iidDepthReduction - 1, ply,
                            alpha, beta, nullMoveCounter, true);


                    TableObject tableObjectFromIID = table.get(zobristHash.getBoardHash());
                    if (tableObjectFromIID != null) {
                        moveFromIID = tableObjectFromIID.getMove();
                    }
                }
            }
            orderedMoves = orderedMoves(board, board.isWhiteTurn(), ply, moveFromIID);
        }
        
        /*
        see if checkmate or stalemate
         */
        if (orderedMoves.size() == 0) {
            if (boardInCheck) {
                if (DEBUG) {
                    numberOfCheckmates++;
                }
                return IN_CHECKMATE_SCORE + ply;
            }
            else {
                if (DEBUG) {
                    numberOfStalemates++;
                }
                return IN_STALEMATE_SCORE;
            }
        }

        int eval = eval(board, board.isWhiteTurn(), orderedMoves);
        
        /*
        Iterative Deepening
        first move should always be winner of search at lower depth. Unless we find a better move, this one is returned
         */
        Move bestMove = copyMove(orderedMoves.get(0));
        if (ply == 0){
            aiMove = copyMove(orderedMoves.get(0));
        }
        
        /*
        iterate through fully legal moves
         */
        int numberOfMovesSearched = 0;

        for (Move move : orderedMoves){
            // consider getting this from move orderer
            boolean captureMove = moveIsCapture(board, move);
            boolean promotionMove = MoveParser.isPromotionMove(move);
            boolean givesCheckMove = checkingMove(board, move);

            if (!thisIsAPrincipleVariationNode && !boardInCheck && numberOfMovesSearched > 1) {
                /*
                Late Move Pruning:
                before making move, see if we can prune this move
                 */
                if (ALLOW_LATE_MOVE_PRUNING) {
                    if (isLateMovePruningMoveOkHere(board, bestScore)) {
                        if (!moveIsCapture(board, move)
                                && !givesCheckMove
                                && !moveWillBeAdvancedPawnPushMove(board, move)) {

                            if (DEBUG) {
                                numberOfLateMovePrunings++;
                            }
                            continue;
                        }
                    }
                }
                
                /*
                (Extended) Futility Pruning:
                if score + margin smaller than alpha, skip this move
                 */
                if (ALLOW_FUTILITY_PRUNING) {
                    if (isFutilityPruningAllowedHere(board, move, depth, captureMove)) {
                        final int futilityScore = eval + futilityMargin[depth];
                        if (futilityScore <= alpha) {
                            if (DEBUG) {
                                numberOfSuccessfulFutilities++;
                            }
                            if (futilityScore > bestScore) {
                                bestScore = futilityScore;
                            }
                            continue;
                        } else if (DEBUG) {
                            numberOfFailedFutilities++;
                        }
                    }
                }

            }

            makeMoveAndHashUpdate(board, move, zobristHash);
            numberOfMovesSearched++;

            boolean enemyInCheck = boardInCheck(board, board.isWhiteTurn());
            Assert.assertTrue(givesCheckMove == enemyInCheck);
            
            /*
            score now above alpha, therefore if no special cases apply, we do a full search
             */
            score = alpha + 1;

            if (DEBUG){
                numberOfMovesMade++;
            }

            /*
            Late Move Reductions:
            search later ordered safer moves to a lower depth
             */
            if (ALLOW_LATE_MOVE_REDUCTIONS && isLateMoveReductionAllowedHere(board, move, depth,
                    numberOfMovesSearched, reducedSearch, captureMove, promotionMove)) {
                if (DEBUG) {
                    numberOfLateMoveReductions++;
                }
                
                /*
                lower depth search
                */
                int lowerDepth = depth - lateMoveDepthReduction(depth) - 1;
                score = lowerDepth <= 0 ? quiescenceSearch(board, zobristHash,
                        startTime, timeLimitMillis,
                        -alpha - 1, -alpha)

                        : -principleVariationSearch
                        (board, zobristHash, startTime, timeLimitMillis,
                                originalDepth, lowerDepth, ply + 1,
                                -alpha - 1, -alpha, 0, true);

               /*
               if a lower move seems good, full depth research
               */
                if (score > alpha){
                    score = -principleVariationSearch(board, zobristHash,
                            startTime, timeLimitMillis,
                            originalDepth, depth - 1, ply + 1,
                            -alpha - 1, -alpha, 0, false);

                    if (DEBUG) {
                        numberOfLateMoveReductionsMisses++;
                    }
                }
                else if (DEBUG) {
                    numberOfLateMoveReductionsHits++;
                }
            }
             
            
            /*
            Principle Variation Search:
            moves that are not favourite (PV) are searched with a null window
            */
            else if (ALLOW_PRINCIPLE_VARIATION_SEARCH && numberOfMovesSearched > 1){

                score = -principleVariationSearch(board, zobristHash,
                        startTime, timeLimitMillis,
                        originalDepth, depth - 1, ply + 1,
                        -alpha - 1, -alpha, 0, reducedSearch);
                
                /*
                if this line of play would improve our score, do full re-search (implemented slightly lower down)
                 */
                if (score > alpha && DEBUG) {
                    numberOfPVSMisses++;
                }
                else{
                    numberOfPVSHits++;
                }
            }


            /*
            always search PV node fully + full re-search of moves that showed promise
            */
            if (score > alpha) {
                score = -principleVariationSearch(board, zobristHash,
                        startTime, timeLimitMillis,
                        originalDepth, depth - 1, ply + 1, -beta, -alpha, 0, false);
            }

            UnMakeMoveAndHashUpdate(board, zobristHash);

            /*
            record score and move if better than previous ones
            */
            if (score > bestScore){
                bestScore = score;
                bestMove = copyMove(move);
            }
            if (score > alpha) {
                alpha = score;
                if (ply == 0){
                    Assert.assertTrue(orderedMoves.contains(aiMove));
                    aiMove = copyMove(move);
                }
            }

            /*
            Alpha Beta Pruning:
            represents a situation which is too good, or too bad, and will not occur in normal play, so stop searching further
            */
            if (alpha >= beta){

                if (DEBUG) {
                    if (numberOfMovesSearched - 1 < whichMoveWasTheBest.length) {
                        whichMoveWasTheBest[numberOfMovesSearched - 1]++;
                    }

                    if (move.equals(mateKiller[ply])){
                        numberOfVictoriousMaters++;
                    }

                    if (move.equals(killerMoves[ply][0])){
                        numberOfVictoriousKillersOne++;
                    }
                    if (move.equals(killerMoves[ply][1])){
                        numberOfVictoriousKillersTwo++;
                    }

                    if (ply > 1) {
                        if (move.equals(killerMoves[ply - 2][0])) {
                            numberOfVeteranVictoriousKillersOne++;
                        }
                        if (move.equals(killerMoves[ply - 2][1])) {
                            numberOfVeteranVictoriousKillersTwo++;
                        }
                    }

                    if (DEBUG) {
                        numberOfFailHighs++;
                    }


                }
                if (!moveIsCapture(board, move)){
                    /*
                    Killer Moves:
                    record this cutoff move, because we will try out in sister nodes
                     */
                    if (ALLOW_KILLERS) {
                        updateKillerMoves(move, ply);
                    }
                    if (ALLOW_HISTORY_MOVES) {
                        updateHistoryMoves(move, ply);
                    }

                    if (ALLOW_MATE_KILLERS){
                        if (alpha > Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY){
                            mateKiller[ply] = move;
                        }
                    }
                }
                break;
            }
        }

        /*
        Transposition Tables:
        add information to table with flag based on the node type
         */
        Flag flag;
        if (bestScore <= originalAlpha){
            flag = UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = LOWERBOUND;
        } else {
            flag = EXACT;
        }
        table.put(zobristHash.getBoardHash(),
                new TableObject(bestMove, bestScore, depth,
                        flag));

        return bestScore;
    }



    static Move getAiMove() {
        return aiMove;
    }
}
