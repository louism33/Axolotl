package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;

import java.util.List;

public class PrincipleVariationSearch {

    private static TranspositionTable table = TranspositionTable.getInstance();
    static int numberOfFinalNegaMax = 0;
    private static Move aiMove;
    
    static int attemptAtFinalNodeCount = 0;

    static int principleVariationSearch(Chessboard board, int originalDepth, int depth, int alpha, int beta){
        int originalAlpha = alpha;

        Move hashMove = null;
        TranspositionTable.TableObject previousTableData = table.get(board);
        if (previousTableData != null) {
            if (previousTableData.getDepth() >= depth) {
                TranspositionTable.TableObject.Flag flag = previousTableData.getFlag();
                int scoreFromTable = previousTableData.getScore();
                hashMove = previousTableData.getMove();
                if (flag == TranspositionTable.TableObject.Flag.EXACT) {
                    if (depth == originalDepth) {
                        System.out.println(" hash : AIMOVE was " + aiMove +" and is now "+ hashMove);
                        aiMove = hashMove;
                    }
                    return scoreFromTable;
                } else if (flag == TranspositionTable.TableObject.Flag.LOWERBOUND) {
                    alpha = Math.max(alpha, scoreFromTable);
                } else if (flag == TranspositionTable.TableObject.Flag.UPPERBOUND) {
                    beta = Math.min(beta, scoreFromTable);
                }
                if (alpha >= beta) {
                    if (depth == originalDepth) {
                        System.out.println("  hash AB cutoff AIMOVE was " + aiMove +" and is now "+ hashMove);
                        aiMove = hashMove;
                        
                    }
                    return scoreFromTable;
                }
            }
        }

        if (depth == 0){
            numberOfFinalNegaMax++;
            attemptAtFinalNodeCount++;
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }

        List<Move> orderedMoves = 
                MoveOrderer.orderMoves(board, hashMove, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));

        // can try hashmove here before rest of generation ?
        
        if (orderedMoves.size() == 0) {
            numberOfFinalNegaMax++;
            return Evaluator.eval(board, board.isWhiteTurn(), orderedMoves);
        }

        Move bestMove = orderedMoves.get(0);

        if (depth == originalDepth){
//            System.out.println(orderedMoves);
            System.out.println("  orig     AIMOVE was " + aiMove +" and is now "+ orderedMoves.get(0));
            aiMove = Copier.copyMove(orderedMoves.get(0));
        }

        int bestScore = alpha;

        for (Move move : orderedMoves){
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);

            int score;

            if (move.equals(orderedMoves.get(0))) {
                score = -principleVariationSearch(board, originalDepth, depth - 1, -beta, -alpha);
            }
            else {
                score = -principleVariationSearch(board, originalDepth, depth - 1, -alpha-1, -alpha);

                if (score > alpha && score < beta){
                    score = -principleVariationSearch(board, originalDepth, depth - 1, -beta, -alpha);
                }
            }

            MoveUnmaker.unMakeMoveMaster(board);

            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
            if (score > alpha) {
                alpha = score;
                if (depth == originalDepth) {
                    System.out.println("   alpha improvvv  AIMOVE was " + aiMove +" and is now "+ move);
                    aiMove = Copier.copyMove(move);
                }
            }
            if (alpha >= beta){
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
        table.put(board,
                new TranspositionTable.TableObject(bestMove, bestScore, depth,
                        flag));


        return bestScore;
    }

    public static Move getAiMove() {
        return aiMove;
    }
}
