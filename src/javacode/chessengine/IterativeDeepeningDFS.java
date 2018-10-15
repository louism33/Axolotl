package javacode.chessengine;

import javacode.chessengine.TranspositionTable.TableObject;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import javacode.evalutation.Evaluator;

import java.util.List;

class IterativeDeepeningDFS {

    private static Move aiMove;
    
    private static TranspositionTable table = TranspositionTable.getInstance();
    
    static Move idDFS (Chessboard board, long timeLimit){
        int maxDepth = 5;
        int bestMoveScore = -10000;
        
        int alpha = -10000;
        int beta = 10000;

        List<Move> rootMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        
        Move bestMove = rootMoves.get(0);
        
        for (int depth = 0; depth < maxDepth; depth++){
            System.out.println("---- "+ depth);
            int score = negaMax(board, depth, depth, alpha, beta);
        }

        /*
        add winning move to front of moves somwhere
         */
        
        System.out.println();
        System.out.println("------");
        System.out.println(bestMove);
        System.out.println("number of evals: "+ numberOfEvals);
        System.out.println("number of Qevals: "+ QuiescenceSearch.numberOfQuiescentEvals);
        System.out.println("------");
        
        return aiMove;
    }

    static int numberOfEvals = 0;
    
    private static int negaMax(Chessboard board, int originalDepth, int depth, int alpha, int beta){
        // find game in table
        
        int originalAlpha = alpha;
        
        TableObject previousTableData = table.get(board);
        if (previousTableData != null){
            System.out.println(previousTableData);
            TableObject.Flag flag = previousTableData.getFlag();
            int scoreFromTable = previousTableData.getScore();
            Move moveFromTable = previousTableData.getMove();
            if (flag == TableObject.Flag.EXACT){
                System.out.println("        " + moveFromTable);
                if (depth == originalDepth){
                    aiMove = moveFromTable;
                }
                return scoreFromTable;
            }
            else if (flag == TableObject.Flag.LOWERBOUND){
                System.out.println("        LOWER");
                alpha = Math.max(alpha, scoreFromTable);
            }
            else if (flag == TableObject.Flag.UPPERBOUND){
                System.out.println("        UPPER");
                beta = Math.min(beta, scoreFromTable);
            }
            if (alpha >= beta){
                if (depth == originalDepth){
                    aiMove = moveFromTable;
                }
                return scoreFromTable;
            }
        }

//        System.out.println("missed table");
        
        if (depth == 0){
            numberOfEvals++;
            return QuiescenceSearch.quiescenceSearch(board, alpha, beta);
        }
        
        
        List<Move> orderedMoves = MoveOrderer.
                orderMoves(board, MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()));
        
        if (orderedMoves.size() == 0) {
            numberOfEvals++;
            return Evaluator.eval(board, board.isWhiteTurn());
        }
        
        Move bestMove = orderedMoves.get(0);
        
        if (depth == originalDepth){
            aiMove = Copier.copyMove(orderedMoves.get(0));
        }
        
        
        int bestScore = alpha;
        
        for (Move move : orderedMoves){
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            
            int score = -negaMax(board, originalDepth, depth-1, -beta, -alpha);

            MoveUnmaker.unMakeMoveMaster(board);
            
            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
            
            if (score > alpha){
                alpha = score;
                if (depth == originalDepth){
                    aiMove = Copier.copyMove(move);
                }
                if (alpha >= beta){
                    break;
                }
            }
        }
        TableObject.Flag flag;
        
        if (bestScore <= originalAlpha){
            flag = TableObject.Flag.UPPERBOUND;
        } else if (bestScore >= beta) {
            flag = TableObject.Flag.LOWERBOUND;
        } else {
            flag = TableObject.Flag.EXACT;
        }
        
        table.put(board, 
                new TableObject(bestMove, bestScore, depth, 
                        flag));
        return bestScore;
    }
    
    
    
    
    
}
