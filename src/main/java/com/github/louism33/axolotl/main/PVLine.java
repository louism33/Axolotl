package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.chesscore.Chessboard;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.axolotl.search.MoveOrdererBetter.MOVE_MASK;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.getMove;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.retrieveFromTable;

public final class PVLine {

    public static final int maxPVLength = 10;

    public static List<GenericMove> retrievePV(Chessboard board) {
        Chessboard dummyBoard = new Chessboard(board);

        List<GenericMove> pvMoves = new ArrayList<>(maxPVLength);

        int i = 0;

        while(i < maxPVLength) {
            long entry = retrieveFromTable(dummyBoard.zobristHash);
            if (entry == 0) {
                break;
            }

            int move = getMove(entry) & MOVE_MASK;

            try {
                dummyBoard.makeMoveAndFlipTurn(move);
            } catch (Exception ignored) {
                break;
            }

            pvMoves.add(UCIBoardParser.convertMyMoveToGenericMove(move));
            i++;
        }
        
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
