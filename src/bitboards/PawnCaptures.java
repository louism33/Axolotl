package bitboards;

import static bitboards.BitBoards.FILES;
import static bitboards.BitBoards.RANKS;

public class PawnCaptures {

    static long WHITE_A2 = 0x400000L;
    static long WHITE_B2 = 0xa00000L;
    static long WHITE_C2 = 0x500000L;
    static long WHITE_D2 = 0x280000L;
    static long WHITE_E2 = 0x140000L;
    static long WHITE_F2 = 0xa0000L;
    static long WHITE_G2 = 0x50000L;
    static long WHITE_H2 = 0x20000L;
    static long WHITE_A3 = 0x40000000L;
    static long WHITE_B3 = 0xa0000000L;
    static long WHITE_C3 = 0x50000000L;
    static long WHITE_D3 = 0x28000000L;
    static long WHITE_E3 = 0x14000000L;
    static long WHITE_F3 = 0xa000000L;
    static long WHITE_G3 = 0x5000000L;
    static long WHITE_H3 = 0x2000000L;
    static long WHITE_A4 = 0x4000000000L;
    static long WHITE_B4 = 0xa000000000L;
    static long WHITE_C4 = 0x5000000000L;
    static long WHITE_D4 = 0x2800000000L;
    static long WHITE_E4 = 0x1400000000L;
    static long WHITE_F4 = 0xa00000000L;
    static long WHITE_G4 = 0x500000000L;
    static long WHITE_H4 = 0x200000000L;
    static long WHITE_A5 = 0x400000000000L;
    static long WHITE_B5 = 0xa00000000000L;
    static long WHITE_C5 = 0x500000000000L;
    static long WHITE_D5 = 0x280000000000L;
    static long WHITE_E5 = 0x140000000000L;
    static long WHITE_F5 = 0xa0000000000L;
    static long WHITE_G5 = 0x50000000000L;
    static long WHITE_H5 = 0x20000000000L;
    static long WHITE_A6 = 0x40000000000000L;
    static long WHITE_B6 = 0xa0000000000000L;
    static long WHITE_C6 = 0x50000000000000L;
    static long WHITE_D6 = 0x28000000000000L;
    static long WHITE_E6 = 0x14000000000000L;
    static long WHITE_F6 = 0xa000000000000L;
    static long WHITE_G6 = 0x5000000000000L;
    static long WHITE_H6 = 0x2000000000000L;
    static long WHITE_A7 = 0x4000000000000000L;
    static long WHITE_B7 = 0xa000000000000000L;
    static long WHITE_C7 = 0x5000000000000000L;
    static long WHITE_D7 = 0x2800000000000000L;
    static long WHITE_E7 = 0x1400000000000000L;
    static long WHITE_F7 = 0xa00000000000000L;
    static long WHITE_G7 = 0x500000000000000L;
    static long WHITE_H7 = 0x200000000000000L;

    static long[] PAWN_MOVE_TABLE_WHITE = {
            0x400000L,
            0xa00000L,
            0x500000L,
            0x280000L,
            0x140000L,
            0xa0000L,
            0x50000L,
            0x20000L,
            0x40000000L,
            0xa0000000L,
            0x50000000L,
            0x28000000L,
            0x14000000L,
            0xa000000L,
            0x5000000L,
            0x2000000L,
            0x4000000000L,
            0xa000000000L,
            0x5000000000L,
            0x2800000000L,
            0x1400000000L,
            0xa00000000L,
            0x500000000L,
            0x200000000L,
            0x400000000000L,
            0xa00000000000L,
            0x500000000000L,
            0x280000000000L,
            0x140000000000L,
            0xa0000000000L,
            0x50000000000L,
            0x20000000000L,
            0x40000000000000L,
            0xa0000000000000L,
            0x50000000000000L,
            0x28000000000000L,
            0x14000000000000L,
            0xa000000000000L,
            0x5000000000000L,
            0x2000000000000L,
            0x4000000000000000L,
            0xa000000000000000L,
            0x5000000000000000L,
            0x2800000000000000L,
            0x1400000000000000L,
            0xa00000000000000L,
            0x500000000000000L,
            0x200000000000000L,
    };

    static long BLACK_A2 = 0x40L;
    static long BLACK_B2 = 0xa0L;
    static long BLACK_C2 = 0x50L;
    static long BLACK_D2 = 0x28L;
    static long BLACK_E2 = 0x14L;
    static long BLACK_F2 = 0xaL;
    static long BLACK_G2 = 0x5L;
    static long BLACK_H2 = 0x2L;
    static long BLACK_A3 = 0x4000L;
    static long BLACK_B3 = 0xa000L;
    static long BLACK_C3 = 0x5000L;
    static long BLACK_D3 = 0x2800L;
    static long BLACK_E3 = 0x1400L;
    static long BLACK_F3 = 0xa00L;
    static long BLACK_G3 = 0x500L;
    static long BLACK_H3 = 0x200L;
    static long BLACK_A4 = 0x400000L;
    static long BLACK_B4 = 0xa00000L;
    static long BLACK_C4 = 0x500000L;
    static long BLACK_D4 = 0x280000L;
    static long BLACK_E4 = 0x140000L;
    static long BLACK_F4 = 0xa0000L;
    static long BLACK_G4 = 0x50000L;
    static long BLACK_H4 = 0x20000L;
    static long BLACK_A5 = 0x40000000L;
    static long BLACK_B5 = 0xa0000000L;
    static long BLACK_C5 = 0x50000000L;
    static long BLACK_D5 = 0x28000000L;
    static long BLACK_E5 = 0x14000000L;
    static long BLACK_F5 = 0xa000000L;
    static long BLACK_G5 = 0x5000000L;
    static long BLACK_H5 = 0x2000000L;
    static long BLACK_A6 = 0x4000000000L;
    static long BLACK_B6 = 0xa000000000L;
    static long BLACK_C6 = 0x5000000000L;
    static long BLACK_D6 = 0x2800000000L;
    static long BLACK_E6 = 0x1400000000L;
    static long BLACK_F6 = 0xa00000000L;
    static long BLACK_G6 = 0x500000000L;
    static long BLACK_H6 = 0x200000000L;
    static long BLACK_A7 = 0x400000000000L;
    static long BLACK_B7 = 0xa00000000000L;
    static long BLACK_C7 = 0x500000000000L;
    static long BLACK_D7 = 0x280000000000L;
    static long BLACK_E7 = 0x140000000000L;
    static long BLACK_F7 = 0xa0000000000L;
    static long BLACK_G7 = 0x50000000000L;
    static long BLACK_H7 = 0x20000000000L;


    static long[] PAWN_MOVE_TABLE_BLACK = {
            0x40L,
            0xa0L,
            0x50L,
            0x28L,
            0x14L,
            0xaL,
            0x5L,
            0x2L,
            0x4000L,
            0xa000L,
            0x5000L,
            0x2800L,
            0x1400L,
            0xa00L,
            0x500L,
            0x200L,
            0x400000L,
            0xa00000L,
            0x500000L,
            0x280000L,
            0x140000L,
            0xa0000L,
            0x50000L,
            0x20000L,
            0x40000000L,
            0xa0000000L,
            0x50000000L,
            0x28000000L,
            0x14000000L,
            0xa000000L,
            0x5000000L,
            0x2000000L,
            0x4000000000L,
            0xa000000000L,
            0x5000000000L,
            0x2800000000L,
            0x1400000000L,
            0xa00000000L,
            0x500000000L,
            0x200000000L,
            0x400000000000L,
            0xa00000000000L,
            0x500000000000L,
            0x280000000000L,
            0x140000000000L,
            0xa0000000000L,
            0x50000000000L,
            0x20000000000L,
    };


}
