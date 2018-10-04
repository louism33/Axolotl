package bitboards;

public class BitBoards {

    static long ALL_WHITE_PIECES (){
        long ans = 0;
        for (long l : WHITE_PIECES) ans |= l;
        return ans;
    }

    static long ALL_BLACK_PIECES (){
        long ans = 0;
        for (long l : BLACK_PIECES) ans |= l;
        return ans;
    }



    public static long WHITE_PAWNS = 0x000000000000FF00L;
    public static long WHITE_KNIGHTS = 0x0000000000000042L;
    public static long WHITE_BISHOPS = 0x0000000000000024L;
    public static long WHITE_ROOKS = 0x0000000000000081L;
    public static long WHITE_QUEEN = 0x0000000000000010L;
    public static long WHITE_KING = 0x0000000000000008L;

    public static long BLACK_PAWNS = 0x00FF000000000000L;
    public static long BLACK_KNIGHTS = 0x4200000000000000L;
    public static long BLACK_BISHOPS = 0x2400000000000000L;
    public static long BLACK_ROOKS = 0x8100000000000000L;
    public static long BLACK_QUEEN = 0x1000000000000000L;
    public static long BLACK_KING = 0x0800000000000000L;

    public static long[] WHITE_PIECES = {
            0x000000000000FF00L, //Pawns
            0x0000000000000042L, //Knights
            0x0000000000000024L, //Bishops
            0x0000000000000081L, //Rooks
            0x0000000000000010L, //Queen
            0x0000000000000008L, //King
    };

    public static long[] BLACK_PIECES = {
            0x00FF000000000000L, //Pawns
            0x4200000000000000L, //Knights
            0x2400000000000000L, //Bishops
            0x8100000000000000L, //Rooks
            0x1000000000000000L, //Queen
            0x0800000000000000L, //King
    };

    static long WHITE_SQUARES = 0x5555555555555555L;
    static long BLACK_SQUARES = 0xAAAAAAAAAAAAAAAAL;

    static long CASTLE_WHITE_KING_SQUARES = 0x0000000000000006L;
    static long CASTLE_WHITE_QUEEN_SQUARES = 0x0000000000000070L;
    static long CASTLE_BLACK_KING_SQUARES = 0x0600000000000000L;
    static long CASTLE_BLACK_QUEEN_SQUARES = 0x7000000000000000L;

    public static long RANK_ONE = 0x00000000000000FFL;
    public static long RANK_TWO = 0x000000000000FF00L;
    public static long RANK_THREE = 0x0000000000FF0000L;
    public static long RANK_FOUR = 0x00000000FF000000L;
    public static long RANK_FIVE = 0x000000FF00000000L;
    public static long RANK_SIX = 0x0000FF0000000000L;
    public static long RANK_SEVEN = 0x00FF000000000000L;
    public static long RANK_EIGHT = 0xFF00000000000000L;

    public static long FILE_H = 0x0101010101010101L;
    public static long FILE_G = 0x0202020202020202L;
    public static long FILE_F = 0x0404040404040404L;
    public static long FILE_E = 0x0808080808080808L;
    public static long FILE_D = 0x1010101010101010L;
    public static long FILE_C = 0x2020202020202020L;
    public static long FILE_B = 0x4040404040404040L;
    public static long FILE_A = 0x8080808080808080L;


    public static long NORTH_WEST = FILE_A | RANK_EIGHT;
    public static long NORTH_EAST = FILE_H | RANK_EIGHT;
    public static long  SOUTH_WEST = FILE_A | RANK_ONE;
    public static long SOUTH_EAST = FILE_H | RANK_ONE;




    public static long[] RANKS = {
            0x00000000000000FFL, // 1
            0x000000000000FF00L, // 2
            0x0000000000FF0000L, // 3
            0x00000000FF000000L, // 4
            0x000000FF00000000L, // 5
            0x0000FF0000000000L, // 6
            0x00FF000000000000L, // 7
            0xFF00000000000000L, // 8
    };

    public static long[] FILES = {
            0x0101010101010101L, // H
            0x0202020202020202L, // G
            0x0404040404040404L, // F
            0x0808080808080808L, // E
            0x1010101010101010L, // D
            0x2020202020202020L, // C
            0x4040404040404040L, // B
            0x8080808080808080L, // A
    };

    static long centreFourSquares = (RANKS[3]^RANKS[4]) & (FILES[3]^FILES[4]);

    static long centreNineSquares = (RANKS[2]^RANKS[3]^RANKS[4]^RANKS[5]) & (FILES[2]^FILES[3]^FILES[4]^FILES[5]);


}
