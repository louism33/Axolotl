package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;
import org.junit.Assert;

import java.util.List;

import static javacode.chessengine.Engine.*;
import static javacode.chessengine.FutilityPruning.futilityMarginDepthOne;
import static javacode.chessengine.FutilityPruning.isFutilityPruningAllowedHere;
import static javacode.chessengine.HistoryMoves.*;
import static javacode.chessengine.KillerMoves.updateKillerMoves;
import static javacode.chessengine.LateMoveReductions.isLateMoveReductionAllowedHere;
import static javacode.chessengine.MoveOrderer.*;
import static javacode.chessengine.NullMovePruning.isNullMoveOkHere;
import static javacode.chessengine.Razoring.*;
import static javacode.chessprogram.check.CheckChecker.boardInCheck;
import static javacode.chessprogram.chess.Copier.copyMove;
import static javacode.chessprogram.moveMaking.MoveOrganiser.flipTurn;
import static javacode.chessprogram.moveMaking.MoveOrganiser.makeMoveMaster;
import static javacode.evalutation.Evaluator.*;

class PrincipleVariationSearch {

    private static final TranspositionTable table = TranspositionTable.getInstance();
    private static Move aiMove;

    static int principleVariationSearch(Chessboard board, ZobristHash zobristHash,
                                        int originalDepth, int depth, int alpha, int beta,
                                        int nullMoveCounter){
        int originalAlpha = alpha;

        int currentDistanceFromRoot = originalDepth - depth;
        depth += extensions(board, currentDistanceFromRoot);

        
        /*
        Quiescent Search:
        if at a leaf node, perform specialised search of captures to avoid horizon effect
         */
        if (depth <= 0){
            return QuiescenceSearch.quiescenceSearch(board, zobristHash, alpha, beta);
        }
        
        /*
        Mate Distance Pruning:
        prefer closer wins and further loses 
         */
        if (ALLOW_MATE_DISTANCE_PRUNING){
            alpha = Math.max(alpha, IN_CHECKMATE_SCORE + currentDistanceFromRoot);
            beta = Math.min(beta, -IN_CHECKMATE_SCORE - currentDistanceFromRoot - 1);
            if (alpha >= beta){
                System.out.println("        alpha "+alpha);
                return alpha;
            }
        }

        int score;
        
        /*
        Transposition Table Lookup:
        if possible, retrieve previously found data from singleton transposition table 
         */
        Move hashMove;
        TranspositionTable.TableObject previousTableData = table.get(zobristHash.getBoardHash());
        boardLookup:
        if (previousTableData != null) {
            if (previousTableData.getDepth() >= depth) {
                TranspositionTable.TableObject.Flag flag = previousTableData.getFlag();
                int scoreFromTable = previousTableData.getScore();
                hashMove = previousTableData.getMove();
                if (flag == TranspositionTable.TableObject.Flag.EXACT) {
                    if (depth == originalDepth) {
                        aiMove = copyMove(hashMove);
                    }
                    return scoreFromTable;
                } else if (flag == TranspositionTable.TableObject.Flag.LOWERBOUND) {
                    alpha = Math.max(alpha, scoreFromTable);
                } else if (flag == TranspositionTable.TableObject.Flag.UPPERBOUND) {
                    beta = Math.min(beta, scoreFromTable);
                }
                if (alpha >= beta) {
                    if (depth == originalDepth) {
                        aiMove = copyMove(hashMove);

                    }
                    return scoreFromTable;
                }
            }
        }
        
        /*
        Principle Variation:
        only perform certain reductions if we are not in the most likely branch of the tree
         */
        final boolean thisIsAPrincipleVariationNode = beta - alpha != 1;
        if (!thisIsAPrincipleVariationNode && !boardInCheck(board, board.isWhiteTurn())) {

            int staticBoardEval = Evaluator.eval(board, board.isWhiteTurn(), MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));

            /*
            Razoring:
            if current node has a very low score, perform Quiescence search to try to find a cutoff
            */
            if (ALLOW_RAZORING){
                if (isRazoringMoveOkHere(board, depth, alpha)){
                    if (staticBoardEval + razorMargin < alpha){
                        final int qScore = QuiescenceSearch.quiescenceSearch(board, zobristHash,
                                alpha - razorMargin, alpha - razorMargin + 1);
                        if (qScore +razorMargin <= alpha){
                            if (DEBUG){
                                numberOfSuccessfulRazors++;
                            }
                            return qScore;
                        }
                        else if (DEBUG){
                            numberOfFailedRazors++;
                        }

                    }
                }
            }
            
            /*
            Fail High Reduction:
            
             */
            
            
            /*
            Null Move Pruning:
            if not in dangerous position, forfeit a move and make shallower null window search
            */
            int nullMoveDepthReduction = 2; // + depth / 3;
            if (nullMoveCounter < 2 && isNullMoveOkHere(board, depth, nullMoveDepthReduction)) {
                flipTurn(board);

                score = depth - nullMoveDepthReduction <= 0 ?
                        -QuiescenceSearch.quiescenceSearch(board, zobristHash, -beta, -beta + 1)
                        : -principleVariationSearch(board, zobristHash, originalDepth,
                        depth - nullMoveDepthReduction, -beta, -beta + 1, nullMoveCounter + 1);

                flipTurn(board);

                if (score >= beta) {
                    if (DEBUG) {
                        numberOfNullMoveHits++;
                    }
                    return score;
                }
                if (DEBUG) {
                    numberOfNullMoveMisses++;
                }
            }

        }
        
        /*
        Move Ordering:
        place moves most likely to cause cutoffs at the front of the move list (hashmoves, killers, captures)
         */
        List<Move> orderedMoves;
        loop:
        if (previousTableData != null) {
            hashMove = previousTableData.getMove();
            orderedMoves = orderedMoves(board, board.isWhiteTurn(), currentDistanceFromRoot, hashMove);
        }
        else {
//            System.out.println(currentDistanceFromRoot);
            orderedMoves = orderedMoves(board, board.isWhiteTurn(), currentDistanceFromRoot, null);
        }
        
        /*
        see if checkmate or stalemate
         */
        if (orderedMoves.size() == 0) {
            return eval(board, board.isWhiteTurn(), orderedMoves);
        }

        /*
        Iterative Deepening
        first move should always be winner of search at lower depth. Unless we find a better move, this one is returned
         */
        Move bestMove = copyMove(orderedMoves.get(0));
        if (depth == originalDepth){
            aiMove = copyMove(orderedMoves.get(0));
        }
        
        /*
        iterate through fully legal moves
         */
        int bestScore = -1000000;
        int numberOfMovesSearched = 0;
        for (Move move : orderedMoves){

            /*
            Pruning:
            before making move, see if we can prune
             */
            if (ALLOW_FUTILITY_PRUNING){
                if (isFutilityPruningAllowedHere(board, move, depth)){
                    if (eval(board, board.isWhiteTurn(), orderedMoves) + futilityMarginDepthOne <= alpha){
                        if (Engine.DEBUG){
                            numberOfSuccessfulFutilities++;
                        }
                        continue;
                    }
                    else if (Engine.DEBUG){
                        numberOfFailedFutilities++;
                    }
                }

            }


            zobristHash.zobristStack.push(zobristHash.getBoardHash());
            zobristHash.updateHash(board, move);
            makeMoveMaster(board, move);
            flipTurn(board);
            numberOfMovesSearched++;

            
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
            int lateMoveDepthReduction = 2 + depth / 3;
            if (ALLOW_LATE_MOVE_REDUCTIONS &&
                    isLateMoveReductionAllowedHere(board, move, depth, numberOfMovesSearched, lateMoveDepthReduction)) {
                if (DEBUG) {
                    numberOfLateMoveReductions++;
                }
                
                /*
                lower depth search
                 */
                score = -principleVariationSearch
                        (board, zobristHash, originalDepth, depth - lateMoveDepthReduction, -alpha - 1, -alpha, 0);

                /*
                if a lower move seems good, full depth research
                 */
                if (score > alpha){
                    score = -principleVariationSearch(board, zobristHash, originalDepth, depth - 1, -alpha - 1, -alpha, 0);
                    if (DEBUG) {
                        numberOfLateMoveReductionsMisses++;
                    }
                }
                else if (DEBUG) {
                    numberOfLateMoveReductionsMisses++;
                }
            }  
            
            /*
            Principle Variation Search:
            moves that are not favourite (PV) are searched with a null window
             */
            else if (ALLOW_PRINCIPLE_VARIATION_SEARCH && numberOfMovesSearched > 1){
                score = -principleVariationSearch(board, zobristHash, originalDepth, 
                        depth - 1, -alpha - 1, -alpha, 0);
                
                /*
                if this line of play would improve our score, do full re-search (implemented slightly lower down (DRY))
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
                score = -principleVariationSearch(board, zobristHash, originalDepth,
                        depth - 1, -beta, -alpha, 0);
            }

            zobristHash.setBoardHash(zobristHash.zobristStack.pop());
            MoveUnmaker.unMakeMoveMaster(board);

            /*
            record score and move if better than previous ones
             */
            if (score > bestScore){
                bestScore = score;
                bestMove = copyMove(move);
            }
            if (score > alpha) {
                alpha = score;
                if (depth == originalDepth) {
                    Assert.assertTrue(orderedMoves.contains(aiMove));
                    aiMove = copyMove(move);
                }
            }

            /*
            Alpha Beta Pruning:
            represents a situation which is too good, or too bad, and will not occur in normal play, so stop searching further
             */
            if (alpha >= beta){

                if(DEBUG){
                    numberOfFailHighs++;
                }

                if (!moveIsCapture(board, move)){
                    /*
                    Killer Moves:
                    record this cutoff move, because we will try out in sister nodes
                     */
                    if (ALLOW_KILLERS) {
                        updateKillerMoves(move, currentDistanceFromRoot);
                    }
                    if (ALLOW_HISTORY_MOVES) {
                        updateHistoryMoves(move, currentDistanceFromRoot);
                    }
                }
                break;
            }
        }


        if (numberOfMovesSearched == 0){
            if (boardInCheck(board, board.isWhiteTurn())) {
                if (DEBUG) {
                    numberOfCheckmates++;
                }
                return IN_CHECKMATE_SCORE + currentDistanceFromRoot;
            }
            else {
                if (DEBUG) {
                    numberOfStalemates++;
                }
                return IN_STALEMATE_SCORE;
            }
        }
        
        
        
    /*
    Transposition Tables:
    add information to table with flag based on the node type
     */
        TranspositionTable.TableObject.Flag flag;
        if (bestScore <= originalAlpha){
            flag = TranspositionTable.TableObject.Flag.UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = TranspositionTable.TableObject.Flag.LOWERBOUND;
        } else {
            flag = TranspositionTable.TableObject.Flag.EXACT;
        }
        table.put(zobristHash.getBoardHash(),
                new TranspositionTable.TableObject(bestMove, bestScore, depth,
                        flag));

        return bestScore;
    }

    private static int extensions(Chessboard board, int ply){
        if (!ALLOW_EXTENSIONS){
            return 0;
        }

        /*
        only extend if we are not so close to the root that messing with depth will cause a problem for aiMove
         */
        if (ply <= 1){
            return 0;
        }

        if (boardInCheck(board, board.isWhiteTurn())){
            if (DEBUG){
                numberOfCheckExtensions++;
            }
            return 1;
        }
        return 0;
    }

    static Move getAiMove() {
        return aiMove;
    }
}
