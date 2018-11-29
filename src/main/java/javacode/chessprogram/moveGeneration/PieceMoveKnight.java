package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.chess.Chessboard;

import java.util.List;

import static javacode.chessprogram.bitboards.Knight.*;
import static javacode.chessprogram.chess.BitExtractor.getAllPieces;
import static javacode.chessprogram.chess.BitIndexing.*;

public class PieceMoveKnight {

    public static long singleKnightTable(Chessboard board, long piece, boolean white, long mask){
        return KNIGHT_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKnights(Chessboard board, boolean white,
                                                long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0, knights;
        if (white){
            knights = board.WHITE_KNIGHTS;
        }
        else {
            knights = board.BLACK_KNIGHTS;
        }

        List<Long> allKnights = getAllPieces(knights, ignoreThesePieces);
        for (Long piece : allKnights){
            ans |= singleKnightTable(board, piece, white, legalPushes | legalCaptures);
        }

        return ans;
    }

}
