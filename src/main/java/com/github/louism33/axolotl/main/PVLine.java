package com.github.louism33.axolotl.main;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.getMove;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.retrieveFromTable;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

public final class PVLine {

    public static final int maxPVLength = 10;

    public static List<String> retrievePV(Chessboard board) {
        List<String> pvMoves = new ArrayList<>(maxPVLength); 

        int i = 0;

        while (i < maxPVLength) {
            long entry = retrieveFromTable(board.zobristHash);
            if (entry == 0) {
                break;
            }

            int move = getMove(entry) & MOVE_MASK_WITH_CHECK;

            board.makeMoveAndFlipTurn(move);

            pvMoves.add(MoveParser.toString(move));
            i++;
        }

        for (int j = i; j > 0; j--) {
            board.unMakeMoveAndFlipTurn();
        }

        return pvMoves;
    }

    private static int[] pv = new int[maxPVLength + 1];

    public static int[] getPV(Chessboard board) {
        Arrays.fill(pv, 0);

        int i = 0;

        while (i < maxPVLength) {
            long entry = retrieveFromTable(board.zobristHash);
            if (entry == 0) {
                break;
            }

            int move = getMove(entry) & MOVE_MASK_WITH_CHECK;

            board.makeMoveAndFlipTurn(move);

            pv[i] = move;
            i++;
        }

        pv[maxPVLength] = i;

        for (int j = i; j > 0; j--) {
            board.unMakeMoveAndFlipTurn();
        }

        return pv;
    }

    public static boolean verifyMove(int move, int[] moves) {
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
