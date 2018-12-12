package com.github.louism33.axolotl.helper.protocolhelperclasses;

import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import org.junit.Assert;

public class PVLine {

    private static int maxPVLength = 20;
    private static int nodeScore;
    static int nps;
    private static int[] pvMoves = new int[maxPVLength];

    public static void retrievePV(Chessboard board) throws IllegalUnmakeException {
        Chessboard initial = new Chessboard(board);
       
        int i = 0, finalI;

        while(i < maxPVLength) {
            long entry = TranspositionTable.retrieveFromTable(board.getZobrist());
            if (entry == 0) {
                break;
            }

            if (i == 0) {
                nodeScore = TranspositionTable.getScore(entry);
                System.out.println("nodeScore: " + nodeScore);
            }

            int move = TranspositionTable.getMove(entry) & MoveOrderer.MOVE_MASK;

            if (verifyMove(board, move)) {
                pvMoves[i] = move;
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
        
        Engine.calculateNPS();
        
        Assert.assertEquals(board, initial);
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

    public static int getNodeScore() {
        return nodeScore;
    }

    public static int[] getPvMoves() {
        return pvMoves;
    }

    public static long getNps() {
        return Engine.nps;
    }
}
