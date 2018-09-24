package chess;

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

    static long northOne (long l){
        return l << 8;
    }

    static long southOne (long l){
        return l >> 8;
    }



    static long[] WHITE_PIECES = {
            0x000000000000FF00L, //Pawns
            0x0000000000000042L, //Knights
            0x0000000000000024L, //Bishops
            0x0000000000000081L, //Rooks
            0x0000000000000010L, //Queen
            0x0000000000000008L, //King
    };

    static long[] BLACK_PIECES = {
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

    static long[] RANKS = {
            0x00000000000000FFL, // 1
            0x000000000000FF00L, // 2
            0x0000000000FF0000L, // 3
            0x00000000FF000000L, // 4
            0x000000FF00000000L, // 5
            0x0000FF0000000000L, // 6
            0x00FF000000000000L, // 7
            0xFF00000000000000L, // 8
    };

    static long[] FILES = {
            0x8080808080808080L, // A
            0x4040404040404040L, // B
            0x2020202020202020L, // C
            0x1010101010101010L, // D
            0x0808080808080808L, // E
            0x0404040404040404L, // F
            0x0202020202020202L, // G
            0x0101010101010101L, // H
    };

    static long centreFourSquares = (RANKS[3]^RANKS[4]) & (FILES[3]^FILES[4]);

    static long centreNineSquares = (RANKS[2]^RANKS[3]^RANKS[4]^RANKS[5]) & (FILES[2]^FILES[3]^FILES[4]^FILES[5]);


}
