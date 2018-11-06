package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveParser;
import javacode.chessprogram.moveMaking.StackMoveData;
import javacode.evaluation.Evaluator;
import javacode.graphicsandui.Art;
import org.junit.Assert;

import java.util.List;
import java.util.Stack;

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
import static javacode.chessengine.SEEPruning.*;
import static javacode.chessengine.TranspositionTable.TableObject;
import static javacode.chessengine.TranspositionTable.TableObject.Flag;
import static javacode.chessengine.TranspositionTable.TableObject.Flag.*;
import static javacode.chessengine.TranspositionTable.getInstance;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.chess.Copier.copyMove;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.evaluation.Evaluator.*;

public class PrincipleVariationSearch {

    public static final TranspositionTable table = getInstance();
    private static Move aiMove;
    static boolean timeUp = false;

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

        /*
        Quiescent Search:
        if at a leaf node, perform specialised search of captures to avoid horizon effect
         */
        if (depth <= 0){
            if(ALLOW_EXTENSIONS && originalDepth != 0) {
                Assert.assertTrue(!boardInCheck(board, board.isWhiteTurn()));
            }
            return quiescenceSearch(board, zobristHash,
                    startTime, timeLimitMillis,
                    alpha, beta);
        }

        Assert.assertTrue(depth >= 1);

        if (ALLOW_TIME_LIMIT) {
            long currentTime = System.currentTimeMillis();
            long timeLeft = startTime + timeLimitMillis - currentTime;
            if (timeLeft < 0) {
                int eval = eval(board, board.isWhiteTurn(), MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
                timeUp = true;
                return eval;
            }
        }
        
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
        TableObject previousTableData = table.get(zobristHash.getBoardHash());
        int score;
        if (previousTableData != null) {
            score = previousTableData.getScore();
            if (previousTableData.getDepth() >= depth) {
                Flag flag = previousTableData.getFlag();
                score = previousTableData.getScore();
                if (flag == EXACT) {
                    statistics.numberOfExacts++;
                    return score;
                } else if (flag == LOWERBOUND) {
                    statistics.numberOfLowerBounds++;
                    alpha = Math.max(alpha, score);
                } else if (flag == UPPERBOUND) {
                    statistics.numberOfUpperBounds++;
                    beta = Math.min(beta, score);
                }
                if (alpha >= beta) {
                    statistics.numberOfHashBetaCutoffs++;
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

            if(previousTableData != null){
                if (previousTableData.getFlag() == EXACT
                        || (previousTableData.getFlag() == UPPERBOUND && previousTableData.getScore() < staticBoardEval)
                        || (previousTableData.getFlag() == LOWERBOUND && previousTableData.getScore() > staticBoardEval)){
                    staticBoardEval = previousTableData.getScore();
                }
            }
            
            /*
            Beta Razoring:
            if current node has a very high score, return eval
             */
            if (ALLOW_BETA_RAZORING){
                if (isBetaRazoringMoveOkHere(board, depth, staticBoardEval)){
                    final int specificBetaRazorMargin = betaRazorMargin[depth];
                    if (staticBoardEval - specificBetaRazorMargin >= beta){
                        if (DEBUG){
                            statistics.numberOfSuccessfulBetaRazors++;
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
                                statistics.numberOfSuccessfulAlphaRazors++;
                            }
                            return qScore;
                        }
                        else if (DEBUG){
                            statistics.numberOfFailedAlphaRazors++;
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
                            statistics.numberOfNullMoveHits++;
                        }
                        return nullScore;
                    }
                    if (DEBUG) {
                        statistics.numberOfNullMoveMisses++;
                    }
                }
            }
        }
        
        /*
        Move Ordering:
        place moves most likely to cause cutoffs at the front of the move list (hashmoves, killers, captures)
         */
        List<Move> orderedMoves;
        if (previousTableData == null) {
            /*
            Internal Iterative Deepening:
            when no hashtable entry, pv node and not endgame, perform shallower search to add a good move to table
             */
            if (ALLOW_INTERNAL_ITERATIVE_DEEPENING){
                if (isIIDAllowedHere(board, depth, reducedSearch, thisIsAPrincipleVariationNode)){
                    if (DEBUG){
                        statistics.numberOfIIDs++;
                    }

                    int preIIDTableSize = table.size();

                    int iidScore = principleVariationSearch(board, zobristHash,
                            startTime, timeLimitMillis, originalDepth,
                            depth - iidDepthReduction - 1, ply,
                            alpha, beta, nullMoveCounter, true);

                    int postIIDTableSize = table.size();

                    System.out.println("post bigger than pre? "+(postIIDTableSize > preIIDTableSize));
                    previousTableData = table.get(zobristHash.getBoardHash());
                }
            }
        }

        if (previousTableData != null) {
            Move hashMove = previousTableData.getMove();

            orderedMoves = orderedMoves(board, board.isWhiteTurn(), ply, hashMove, aiMove);

            if (DEBUG) {
                statistics.numberOfSearchesWithHash++;
            }
        }
        else{
            if (DEBUG) {
                statistics.numberOfSearchesWithoutHash++;
            }
            orderedMoves = orderedMoves(board, board.isWhiteTurn(), ply, null, aiMove);
        }

        int eval = eval(board, board.isWhiteTurn(), orderedMoves);

        if (ALLOW_TIME_LIMIT) {
            long currentTime = System.currentTimeMillis();
            long timeLeft = startTime + timeLimitMillis - currentTime;
            if (timeLeft < 0) {
                timeUp = true;
                return eval;
            }
        }

        Move bestMove = null;
        
        /*
        iterate through fully legal moves
         */
        int numberOfMovesSearched = 0;
        for (Move move : orderedMoves){
            // consider getting this from move orderer
            boolean captureMove = moveIsCapture(board, move);
            boolean promotionMove = MoveParser.isPromotionMove(move);
            boolean givesCheckMove = checkingMove(board, move);
            boolean pawnToSix = moveWillBePawnPushSix(board, move);
            boolean pawnToSeven = false;
            if (!pawnToSix){
                pawnToSeven = moveWillBePawnPushSeven(board, move);
            }

            if (!thisIsAPrincipleVariationNode && !boardInCheck && numberOfMovesSearched > 1) {
                /*
                Late Move Pruning:
                before making move, see if we can prune this move
                 */
                if (ALLOW_LATE_MOVE_PRUNING) {
                    if (isLateMovePruningMoveOkHere(board, bestScore)) {
                        if (!captureMove
                                && !promotionMove
                                && !givesCheckMove
                                && !pawnToSix
                                && !pawnToSeven
                                && numberOfMovesSearched > depth * 3 + 3) {

                            if (DEBUG) {
                                statistics.numberOfLateMovePrunings++;
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
                    if (isFutilityPruningAllowedHere(board, move, depth,
                            captureMove, promotionMove, givesCheckMove, pawnToSix, pawnToSeven)) {
                        final int futilityScore = eval + futilityMargin[depth];
                        if (futilityScore <= alpha) {
                            if (DEBUG) {
                                statistics.numberOfSuccessfulFutilities++;
                            }
                            if (futilityScore > bestScore) {
                                bestScore = futilityScore;
                            }
                            continue;
                        } else if (DEBUG) {
                            statistics.numberOfFailedFutilities++;
                        }
                    }
                }
                
                /*
                Static Exchange Evaluation:
                if an exchange promises a large loss of material skip it, more if further from root
                 */
                if (ALLOW_SEE_PRUNING){
                    if (isSeePruningMoveOkHere(board, bestScore)){
                        continue;
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
                statistics.numberOfMovesMade++;
            }

            /*
            Late Move Reductions:
            search later ordered safer moves to a lower depth
             */
            if (ALLOW_LATE_MOVE_REDUCTIONS && isLateMoveReductionAllowedHere(board, move, depth,
                    numberOfMovesSearched, reducedSearch, captureMove, givesCheckMove, promotionMove, pawnToSix, pawnToSeven)) {
                if (DEBUG) {
                    statistics.numberOfLateMoveReductions++;
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
                        statistics.numberOfLateMoveReductionsMisses++;
                    }
                }
                else if (DEBUG) {
                    statistics.numberOfLateMoveReductionsHits++;
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
                    statistics.numberOfPVSMisses++;
                }
                else{
                    statistics.numberOfPVSHits++;
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

                    System.out.println("\ntu: "+timeUp+", AI move was: "+aiMove+", will now be: "+move+", score is: "+ score);

                    if (!timeUp) {
                        aiMove = copyMove(move);
                    }

                }
            }

            /*
            Alpha Beta Pruning:
            represents a situation which is too good, or too bad, and will not occur in normal play, so stop searching further
             */
            if (alpha >= beta){

                if (DEBUG) {
                    if (numberOfMovesSearched - 1 < statistics.whichMoveWasTheBest.length) {
                        statistics.whichMoveWasTheBest[numberOfMovesSearched - 1]++;
                    }

                    if (move.equals(mateKiller[ply])){
                        statistics.numberOfVictoriousMaters++;
                    }

                    if (move.equals(killerMoves[ply][0])){
                        statistics.numberOfVictoriousKillersOne++;
                    }
                    if (move.equals(killerMoves[ply][1])){
                        statistics.numberOfVictoriousKillersTwo++;
                    }

                    if (ply > 1) {
                        if (move.equals(killerMoves[ply - 2][0])) {
                            statistics.numberOfVeteranVictoriousKillersOne++;
                        }
                        if (move.equals(killerMoves[ply - 2][1])) {
                            statistics.numberOfVeteranVictoriousKillersTwo++;
                        }
                    }

                    if (DEBUG) {
                        statistics.numberOfFailHighs++;
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
                        if (alpha > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                            mateKiller[ply] = move;
                        }
                    }
                }
                break;
            }
        }

        if (numberOfMovesSearched == 0) {
            if (boardInCheck) {
                if (DEBUG) {
                    statistics.numberOfCheckmates++;
                }

                return IN_CHECKMATE_SCORE + ply;
            }
            else {
                if (DEBUG) {
                    statistics.numberOfStalemates++;
                }
                return IN_STALEMATE_SCORE;
            }
        }
        
        
        //todo add ply, for mate scores, and age
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

    public static void setAiMove(Move aiMove) {
        PrincipleVariationSearch.aiMove = aiMove;
    }

    public static Move getAiMove() {
        return aiMove;
    }

}
