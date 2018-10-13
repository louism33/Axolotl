package main.bitboards;

public class BitBoards {

    public static final long WHITE_PAWNS = 0x000000000000FF00L;
    public static final long WHITE_KNIGHTS = 0x0000000000000042L;
    public static final long WHITE_BISHOPS = 0x0000000000000024L;
    public static final long WHITE_ROOKS = 0x0000000000000081L;
    public static final long WHITE_QUEEN = 0x0000000000000010L;
    public static final long WHITE_KING = 0x0000000000000008L;

    public static final long BLACK_PAWNS = 0x00FF000000000000L;
    public static final long BLACK_KNIGHTS = 0x4200000000000000L;
    public static final long BLACK_BISHOPS = 0x2400000000000000L;
    public static final long BLACK_ROOKS = 0x8100000000000000L;
    public static final long BLACK_QUEEN = 0x1000000000000000L;
    public static final long BLACK_KING = 0x0800000000000000L;

    static long WHITE_SQUARES = 0x5555555555555555L;
    static long BLACK_SQUARES = 0xAAAAAAAAAAAAAAAAL;

    static long CASTLE_WHITE_KING_SQUARES = 0x0000000000000006L;
    static long CASTLE_WHITE_QUEEN_SQUARES = 0x0000000000000070L;
    static long CASTLE_BLACK_KING_SQUARES = 0x0600000000000000L;
    static long CASTLE_BLACK_QUEEN_SQUARES = 0x7000000000000000L;

    public static final long RANK_ONE = 0x00000000000000FFL;
    public static final long RANK_TWO = 0x000000000000FF00L;
    public static final long RANK_THREE = 0x0000000000FF0000L;
    public static final long RANK_FOUR = 0x00000000FF000000L;
    public static final long RANK_FIVE = 0x000000FF00000000L;
    public static final long RANK_SIX = 0x0000FF0000000000L;
    public static final long RANK_SEVEN = 0x00FF000000000000L;
    public static final long RANK_EIGHT = 0xFF00000000000000L;

    public static final long FILE_H = 0x0101010101010101L;
    public static final long FILE_G = 0x0202020202020202L;
    public static final long FILE_F = 0x0404040404040404L;
    public static final long FILE_E = 0x0808080808080808L;
    public static final long FILE_D = 0x1010101010101010L;
    public static final long FILE_C = 0x2020202020202020L;
    public static final long FILE_B = 0x4040404040404040L;
    public static final long FILE_A = 0x8080808080808080L;

    public static final long NORTH_WEST = FILE_A | RANK_EIGHT;
    public static final long NORTH_EAST = FILE_H | RANK_EIGHT;
    public static final long SOUTH_WEST = FILE_A | RANK_ONE;
    public static final long SOUTH_EAST = FILE_H | RANK_ONE;

    public static final long NORTH_WEST_CORNER = FILE_A & RANK_EIGHT;
    public static final long NORTH_EAST_CORNER = FILE_H & RANK_EIGHT;
    public static final long SOUTH_WEST_CORNER = FILE_A & RANK_ONE;
    public static final long SOUTH_EAST_CORNER = FILE_H & RANK_ONE;

    public static long centreFourSquares = (RANK_FOUR | RANK_FIVE) & (FILE_D | FILE_E);
    public static long centreNineSquares = (RANK_THREE | RANK_FOUR | RANK_FIVE | RANK_SIX) & 
            (FILE_C |FILE_D | FILE_E | FILE_F);

    public static final long whiteCastleKingEmpties = 0x0000000000000006L;
    public static final long whiteCastleQueenEmpties = 0x0000000000000070L;

    public static final long blackCastleKingEmpties = 0x0600000000000000L;
    public static final long blackCastleQueenEmpties = 0x7000000000000000L;

    public static final long whiteCastleQueenUnthreateneds = 0x0000000000000030L;
    public static final long blackCastleQueenUnthreateneds = 0x3000000000000000L;
}
