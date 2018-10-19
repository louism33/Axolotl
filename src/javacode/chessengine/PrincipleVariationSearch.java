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

        Move hashMove;
        TranspositionTable.TableObject previousTableData = table.get(board);
        boardLookup:
        if (previousTableData != null) {
            if (previousTableData.getDepth() >= depth) {
                TranspositionTable.TableObject.Flag flag = previousTableData.getFlag();
                int scoreFromTable = previousTableData.getScore();
                hashMove = previousTableData.getMove();
                List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
                if (!moves.contains(hashMove)){
                    break boardLookup;
                }
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
            return Evaluator.eval(board, board.isWhiteTurn());
//            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }

        List<Move> orderedMoves;
        
        loop:
        if (previousTableData != null) {
            Move moveFromHash = previousTableData.getMove();
            orderedMoves = MoveOrderer.orderMoves(board, board.isWhiteTurn());

//            System.out.println("hashmove: "+moveFromHash);
            
            if (!orderedMoves.contains(moveFromHash)){
                break loop;
            }
            
            orderedMoves.remove(moveFromHash);
            orderedMoves.add(0, moveFromHash);
        }
        else {
            orderedMoves = MoveOrderer.orderMoves(board, board.isWhiteTurn());
        }

        // can try hashmove here before rest of generation ?
        
        if (orderedMoves.size() == 0) {
            return Evaluator.eval(board, board.isWhiteTurn(), orderedMoves);
        }

        Move bestMove = Copier.copyMove(orderedMoves.get(0));

        if (depth == originalDepth){
//            System.out.println("before search aimove was " + aiMove +"  is now " + orderedMoves.get(0));
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
                bestMove = Copier.copyMove(move);
            }
            if (score > alpha) {
                alpha = score;
                if (depth == originalDepth) {
//                    Assert.assertTrue(orderedMoves.contains(aiMove));

//                    System.out.println("      after search aimove was " + aiMove +"  is now " + move);
                    aiMove = Copier.copyMove(move);
                }
            }

//            if (score == Evaluator.IN_CHECKMATE_SCORE){
//                return score;
//            }
            
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

    static Move getAiMove() {
        return aiMove;
    }
}
