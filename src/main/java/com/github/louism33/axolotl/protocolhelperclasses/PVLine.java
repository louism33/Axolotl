package com.github.louism33.axolotl.protocolhelperclasses;

import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;

import java.util.ArrayList;
import java.util.List;

public class PVLine {
    
    private final int score;
    private final List<Integer> pvMoves;

    private PVLine(int score, List<Integer> pvMoves) {
        this.score = score;
        this.pvMoves = pvMoves;
    }

    @Override
    public String toString() {
        return "PVLine{" +
                "score=" + score +
                ", pvMoves=" + pvMoves +
                '}';
    }

    public static PVLine retrievePVfromTable(Chessboard board, TranspositionTable table) throws IllegalUnmakeException {
        Chessboard copyBoard = new Chessboard(board);
        List<Integer> moves = new ArrayList<>();
        int i = 1;
        int nodeScore = 0;

        while(i < 50) {
            TranspositionTable.TableObject tableObject = table.get(copyBoard.getBoardHash());
            if (tableObject == null) {
                break;
            }
            int score = tableObject.getScore(i-1);
            if (i == 1){
                nodeScore = score;
            }
            int move = tableObject.getMove();
            moves.add(move);
            copyBoard.makeMoveAndFlipTurn(move);
            i++;
        }

        return new PVLine(nodeScore, moves);
    }


    public int getScore() {
        return score;
    }

    public List<Integer> getPvMoves() {
        return pvMoves;
    }
}
