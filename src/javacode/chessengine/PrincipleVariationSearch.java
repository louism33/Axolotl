package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;
import org.junit.Assert;

import java.util.List;

import static javacode.chessengine.MoveOrderer.killerMoves;

class PrincipleVariationSearch {

    private static final TranspositionTable table = TranspositionTable.getInstance();
    static int numberOfFinalNegaMax = 0;
    private static Move aiMove;
    
    static int attemptAtFinalNodeCount = 0;

    static int principleVariationSearch(Chessboard board, ZobristHash zobristHash, int originalDepth, int depth, int alpha, int beta){
        int originalAlpha = alpha;
        int ply = originalDepth - depth;

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
                        aiMove = Copier.copyMove(hashMove);
                    }
                    return scoreFromTable;
                } else if (flag == TranspositionTable.TableObject.Flag.LOWERBOUND) {
                    alpha = Math.max(alpha, scoreFromTable);
                } else if (flag == TranspositionTable.TableObject.Flag.UPPERBOUND) {
                    beta = Math.min(beta, scoreFromTable);
                }
                if (alpha >= beta) {
                    if (depth == originalDepth) {
                        aiMove = Copier.copyMove(hashMove);
                        
                    }
                    return scoreFromTable;
                }
            }
        }

        if (depth == 0){
            numberOfFinalNegaMax++;
            attemptAtFinalNodeCount++;
//            return Evaluator.eval(board, board.isWhiteTurn());
            return QuiescenceSearch.quiescenceSearch(board, zobristHash, alpha, beta);
        }

        List<Move> orderedMoves;

        loop:
        if (previousTableData != null) {
            hashMove = previousTableData.getMove();
            orderedMoves = MoveOrderer.orderedMoves(board, board.isWhiteTurn(), ply, hashMove);
        }
        else {
            orderedMoves = MoveOrderer.orderedMoves(board, board.isWhiteTurn(), ply, null);
        }

        if (orderedMoves.size() == 0) {
            return Evaluator.eval(board, board.isWhiteTurn(), orderedMoves);
        }

        Move bestMove = Copier.copyMove(orderedMoves.get(0));

        if (depth == originalDepth){
            aiMove = Copier.copyMove(orderedMoves.get(0));
        }

        int bestScore = alpha;

        for (Move move : orderedMoves){
            zobristHash.zobristStack.push(zobristHash.getBoardHash());
            zobristHash.updateHash(board, move, false);
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);

            int score;

            if (move.equals(orderedMoves.get(0))) {
                score = -principleVariationSearch(board, zobristHash, originalDepth, depth - 1, -beta, -alpha);
            }
            else {
                score = -principleVariationSearch(board, zobristHash, originalDepth, depth - 1, -alpha - 1, -alpha);

                if (score > alpha && score < beta){
                    score = -principleVariationSearch(board, zobristHash, originalDepth, depth - 1, -beta, -alpha);
                }
            }

            zobristHash.setBoardHash(zobristHash.zobristStack.pop());
            MoveUnmaker.unMakeMoveMaster(board);

            if (score > bestScore){
                bestScore = score;
                bestMove = Copier.copyMove(move);
            }
            if (score > alpha) {
                alpha = score;
                if (depth == originalDepth) {
                    Assert.assertTrue(orderedMoves.contains(aiMove));
                    aiMove = Copier.copyMove(move);
                }
            }

            if (alpha >= beta){
                if (!MoveOrderer.moveIsCapture(board, move)){
                    updateKillerMoves(move, originalDepth, depth);
                }
                break;
            }
        }

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
    
    private static void updateKillerMoves(Move move, int originalDepth, int ply){
        // shift killers to the right
        if (killerMoves[ply].length - 2 + 1 >= 0)
            System.arraycopy(killerMoves[ply], 0, killerMoves[ply], 1, killerMoves[ply].length - 2 + 1);
        killerMoves[ply][0] = move;
        
    }

    static Move getAiMove() {
        return aiMove;
    }
}
