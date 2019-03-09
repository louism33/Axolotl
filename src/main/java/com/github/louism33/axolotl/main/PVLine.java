package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.moveordering.MoveOrderer;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class PVLine {

    public static final int maxPVLength = 20;

    public static List<GenericMove> retrievePV(Chessboard board) {
        Chessboard initial = new Chessboard(board);

        List<GenericMove> pvMoves = new ArrayList<>(maxPVLength);

        int i = 0, finalI;

        while(i < maxPVLength) {
            long entry = TranspositionTable.retrieveFromTable(board.zobristHash);
            if (entry == 0) {
                break;
            }

            int move = TranspositionTable.getMove(entry) & MoveOrderer.MOVE_MASK;

            if (PVLine.verifyMove(move, board.generateLegalMoves())) {
                pvMoves.add(UCIBoardParser.convertMyMoveToGenericMove(move));
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

        Assert.assertEquals(board, initial);
        return pvMoves;
    }

    public static boolean verifyMove(int move, int[] moves){
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
}
