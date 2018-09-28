package chess;

public class Chessboard {

    boolean whiteTurn;
    boolean whiteCanCastleK, whiteCanCastleQ, blackCanCastleK, blackCanCastleQ;

    public long WHITE_PAWNS = 0x000000000000FF00L;
    public long WHITE_KNIGHTS = 0x0000000000000042L;
    public long WHITE_BISHOPS = 0x0000000000000024L;
//    public long WHITE_ROOKS = 0x0000000000000081L;

    public long WHITE_ROOKS = 0x0000000010000000L;


    public long WHITE_QUEEN = 0x0000000000000010L;
    public long WHITE_KING = 0x0000000000000008L;

    public long BLACK_PAWNS = 0x00FF000000000000L;
    public long BLACK_KNIGHTS = 0x4200000000000000L;
    public long BLACK_BISHOPS = 0x2400000000000000L;
    public long BLACK_ROOKS = 0x8100000000000000L;
    public long BLACK_QUEEN = 0x1000000000000000L;
    public long BLACK_KING = 0x0800000000000000L;

    long ALL_WHITE_PIECES (){
        return WHITE_PAWNS | WHITE_KNIGHTS | WHITE_BISHOPS | WHITE_ROOKS | WHITE_QUEEN | WHITE_KING;
    }

    long ALL_BLACK_PIECES (){
        return BLACK_PAWNS | BLACK_KNIGHTS | BLACK_BISHOPS | BLACK_ROOKS | BLACK_QUEEN | BLACK_KING;
    }

    long ALL_PIECES (){
        return ALL_WHITE_PIECES() | ALL_BLACK_PIECES();
    }


}
