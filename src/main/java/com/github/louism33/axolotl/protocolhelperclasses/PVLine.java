package com.github.louism33.axolotl.protocolhelperclasses;

import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;

import java.util.Arrays;

public class PVLine {
    
    private final int score;
    private final int[] pvMoves;

    private PVLine(int score, int[] pvMoves) {
        this.score = score;
        this.pvMoves = pvMoves;
    }

    @Override
    public String toString() {
        return "PVLine{" +
                "score=" + score +
//                ", pvMoves=" + Arrays.toString(pvMoves) +
                '}';
    }

    public static PVLine retrievePVfromTable(Chessboard board) throws IllegalUnmakeException {
        Chessboard initial = new Chessboard(board);
        
        int maxPVLength = 20;
        int[] moves = new int[maxPVLength];
        int i = 0, finalI;
        int nodeScore = 0;

//        System.out.println(Arrays.toString(TranspositionTable.keys));
//        System.out.println(Arrays.toString(TranspositionTable.entries));

//        System.out.println();
//        System.out.println("transposition table entry: " + blub);
//        System.out.println("score: "+nodeScore);
        
        
//        System.out.println(Arrays.toString(MoveParser.toString(board.generateLegalMoves())));
        
        while(i < maxPVLength) {
            long entry = TranspositionTable.retrieveFromTable(board.getZobrist());
            if (entry == 0) {
                break;
            }

            long blub = TranspositionTable.retrieveFromTable(board.getZobrist());
            
            if (i == 0) {
                nodeScore = TranspositionTable.getScore(blub);
            }
            
            int move = TranspositionTable.getMove(entry) & MoveOrderer.MOVE_MASK;
            
            if (verifyMove(board, move)) {
                moves[i] = move;
                board.makeMoveAndFlipTurn(move);
                i++;
            }
            else {
                break;
            }
        }
        finalI = Math.min(i, maxPVLength);
        for (int x = 0; x < finalI; x++){
            board.unMakeMoveAndFlipTurn();
        }

        Assert.assertEquals(initial, board);
        
        return new PVLine(nodeScore, moves);
    }

    public static boolean verifyMove(Chessboard board, int move, int[] moves){
        for (int j = 0; j < moves.length; j++) {
            int possibleMove = moves[j];
            if (possibleMove == 0) {
                break;
            }
            if (move == possibleMove) {
                return true;
            }
        }
        return false;
    }

    
    public static boolean verifyMove(Chessboard board, int move){
        int[] legalMoves = board.generateLegalMoves();
        for (int j = 0; j < legalMoves.length; j++) {
            int possibleMove = legalMoves[j];
            if (possibleMove == 0) {
                break;
            }
            if (move == possibleMove) {
                return true;
            }
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    public int[] getPvMoves() {
        return pvMoves;
    }
}
