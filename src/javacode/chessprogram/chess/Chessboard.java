package javacode.chessprogram.chess;

import javacode.chessprogram.moveMaking.StackMoveData;

import java.util.Objects;
import java.util.Stack;

public class Chessboard {

    public Stack<StackMoveData> moveStack = new Stack<>();

    private boolean whiteTurn = true;
    public boolean whiteCanCastleK = true, whiteCanCastleQ = true, blackCanCastleK = true, blackCanCastleQ = true;

    public long WHITE_PAWNS = 0x000000000000FF00L;
    public long WHITE_KNIGHTS = 0x0000000000000042L;
    public long WHITE_BISHOPS = 0x0000000000000024L;
    public long WHITE_ROOKS = 0x0000000000000081L;
    public long WHITE_QUEEN = 0x0000000000000010L;
    public long WHITE_KING = 0x0000000000000008L;

    public long BLACK_PAWNS = 0x00FF000000000000L;
    public long BLACK_KNIGHTS = 0x4200000000000000L;
    public long BLACK_BISHOPS = 0x2400000000000000L;
    public long BLACK_ROOKS = 0x8100000000000000L;
    public long BLACK_QUEEN = 0x1000000000000000L;
    public long BLACK_KING = 0x0800000000000000L;

    public long ALL_WHITE_PIECES (){
        return WHITE_PAWNS | WHITE_KNIGHTS | WHITE_BISHOPS | WHITE_ROOKS | WHITE_QUEEN | WHITE_KING;
    }

    public long ALL_BLACK_PIECES (){
        return BLACK_PAWNS | BLACK_KNIGHTS | BLACK_BISHOPS | BLACK_ROOKS | BLACK_QUEEN | BLACK_KING;
    }

    public long ALL_PIECES (){
        return ALL_WHITE_PIECES() | ALL_BLACK_PIECES();
    }


    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chessboard that = (Chessboard) o;
        return whiteTurn == that.whiteTurn &&
                whiteCanCastleK == that.whiteCanCastleK &&
                whiteCanCastleQ == that.whiteCanCastleQ &&
                blackCanCastleK == that.blackCanCastleK &&
                blackCanCastleQ == that.blackCanCastleQ &&
                WHITE_PAWNS == that.WHITE_PAWNS &&
                WHITE_KNIGHTS == that.WHITE_KNIGHTS &&
                WHITE_BISHOPS == that.WHITE_BISHOPS &&
                WHITE_ROOKS == that.WHITE_ROOKS &&
                WHITE_QUEEN == that.WHITE_QUEEN &&
                WHITE_KING == that.WHITE_KING &&
                BLACK_PAWNS == that.BLACK_PAWNS &&
                BLACK_KNIGHTS == that.BLACK_KNIGHTS &&
                BLACK_BISHOPS == that.BLACK_BISHOPS &&
                BLACK_ROOKS == that.BLACK_ROOKS &&
                BLACK_QUEEN == that.BLACK_QUEEN &&
                BLACK_KING == that.BLACK_KING &&
                Objects.equals(moveStack, that.moveStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moveStack, whiteTurn, whiteCanCastleK, whiteCanCastleQ, blackCanCastleK, blackCanCastleQ, WHITE_PAWNS, WHITE_KNIGHTS, WHITE_BISHOPS, WHITE_ROOKS, WHITE_QUEEN, WHITE_KING, BLACK_PAWNS, BLACK_KNIGHTS, BLACK_BISHOPS, BLACK_ROOKS, BLACK_QUEEN, BLACK_KING);
    }
}
