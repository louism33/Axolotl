package bitboards;

public class Pawn {

    static long WHITE_A2 = 0x80800000L;
    static long WHITE_B2 = 0x40400000L;
    static long WHITE_C2 = 0x20200000L;
    static long WHITE_D2 = 0x10100000L;
    static long WHITE_E2 = 0x8080000L;
    static long WHITE_F2 = 0x4040000L;
    static long WHITE_G2 = 0x2020000L;
    static long WHITE_H2 = 0x1010000L;
    static long WHITE_A3 = 0x80000000L;
    static long WHITE_B3 = 0x40000000L;
    static long WHITE_C3 = 0x20000000L;
    static long WHITE_D3 = 0x10000000L;
    static long WHITE_E3 = 0x8000000L;
    static long WHITE_F3 = 0x4000000L;
    static long WHITE_G3 = 0x2000000L;
    static long WHITE_H3 = 0x1000000L;
    static long WHITE_A4 = 0x8000000000L;
    static long WHITE_B4 = 0x4000000000L;
    static long WHITE_C4 = 0x2000000000L;
    static long WHITE_D4 = 0x1000000000L;
    static long WHITE_E4 = 0x800000000L;
    static long WHITE_F4 = 0x400000000L;
    static long WHITE_G4 = 0x200000000L;
    static long WHITE_H4 = 0x100000000L;
    static long WHITE_A5 = 0x800000000000L;
    static long WHITE_B5 = 0x400000000000L;
    static long WHITE_C5 = 0x200000000000L;
    static long WHITE_D5 = 0x100000000000L;
    static long WHITE_E5 = 0x80000000000L;
    static long WHITE_F5 = 0x40000000000L;
    static long WHITE_G5 = 0x20000000000L;
    static long WHITE_H5 = 0x10000000000L;
    static long WHITE_A6 = 0x80000000000000L;
    static long WHITE_B6 = 0x40000000000000L;
    static long WHITE_C6 = 0x20000000000000L;
    static long WHITE_D6 = 0x10000000000000L;
    static long WHITE_E6 = 0x8000000000000L;
    static long WHITE_F6 = 0x4000000000000L;
    static long WHITE_G6 = 0x2000000000000L;
    static long WHITE_H6 = 0x1000000000000L;
    static long WHITE_A7 = 0x8000000000000000L;
    static long WHITE_B7 = 0x4000000000000000L;
    static long WHITE_C7 = 0x2000000000000000L;
    static long WHITE_D7 = 0x1000000000000000L;
    static long WHITE_E7 = 0x800000000000000L;
    static long WHITE_F7 = 0x400000000000000L;
    static long WHITE_G7 = 0x200000000000000L;
    static long WHITE_H7 = 0x100000000000000L;

    static long[] PAWN_MOVE_TABLE_WHITE = {
            0x80800000L,
            0x40400000L,
            0x20200000L,
            0x10100000L,
            0x8080000L,
            0x4040000L,
            0x2020000L,
            0x1010000L,
            0x80000000L,
            0x40000000L,
            0x20000000L,
            0x10000000L,
            0x8000000L,
            0x4000000L,
            0x2000000L,
            0x1000000L,
            0x8000000000L,
            0x4000000000L,
            0x2000000000L,
            0x1000000000L,
            0x800000000L,
            0x400000000L,
            0x200000000L,
            0x100000000L,
            0x800000000000L,
            0x400000000000L,
            0x200000000000L,
            0x100000000000L,
            0x80000000000L,
            0x40000000000L,
            0x20000000000L,
            0x10000000000L,
            0x80000000000000L,
            0x40000000000000L,
            0x20000000000000L,
            0x10000000000000L,
            0x8000000000000L,
            0x4000000000000L,
            0x2000000000000L,
            0x1000000000000L,
            0x8000000000000000L,
            0x4000000000000000L,
            0x2000000000000000L,
            0x1000000000000000L,
            0x800000000000000L,
            0x400000000000000L,
            0x200000000000000L,
            0x100000000000000L,
    };




    static long BLACK_A2 = 0x80L;
    static long BLACK_B2 = 0x40L;
    static long BLACK_C2 = 0x20L;
    static long BLACK_D2 = 0x10L;
    static long BLACK_E2 = 0x8L;
    static long BLACK_F2 = 0x4L;
    static long BLACK_G2 = 0x2L;
    static long BLACK_H2 = 0x1L;
    static long BLACK_A3 = 0x8000L;
    static long BLACK_B3 = 0x4000L;
    static long BLACK_C3 = 0x2000L;
    static long BLACK_D3 = 0x1000L;
    static long BLACK_E3 = 0x800L;
    static long BLACK_F3 = 0x400L;
    static long BLACK_G3 = 0x200L;
    static long BLACK_H3 = 0x100L;
    static long BLACK_A4 = 0x800000L;
    static long BLACK_B4 = 0x400000L;
    static long BLACK_C4 = 0x200000L;
    static long BLACK_D4 = 0x100000L;
    static long BLACK_E4 = 0x80000L;
    static long BLACK_F4 = 0x40000L;
    static long BLACK_G4 = 0x20000L;
    static long BLACK_H4 = 0x10000L;
    static long BLACK_A5 = 0x80000000L;
    static long BLACK_B5 = 0x40000000L;
    static long BLACK_C5 = 0x20000000L;
    static long BLACK_D5 = 0x10000000L;
    static long BLACK_E5 = 0x8000000L;
    static long BLACK_F5 = 0x4000000L;
    static long BLACK_G5 = 0x2000000L;
    static long BLACK_H5 = 0x1000000L;
    static long BLACK_A6 = 0x8000000000L;
    static long BLACK_B6 = 0x4000000000L;
    static long BLACK_C6 = 0x2000000000L;
    static long BLACK_D6 = 0x1000000000L;
    static long BLACK_E6 = 0x800000000L;
    static long BLACK_F6 = 0x400000000L;
    static long BLACK_G6 = 0x200000000L;
    static long BLACK_H6 = 0x100000000L;
    static long BLACK_A7 = 0x808000000000L;
    static long BLACK_B7 = 0x404000000000L;
    static long BLACK_C7 = 0x202000000000L;
    static long BLACK_D7 = 0x101000000000L;
    static long BLACK_E7 = 0x80800000000L;
    static long BLACK_F7 = 0x40400000000L;
    static long BLACK_G7 = 0x20200000000L;
    static long BLACK_H7 = 0x10100000000L;


    static long[] PAWN_MOVE_TABLE_BLACK = {
            0x80L,
            0x40L,
            0x20L,
            0x10L,
            0x8L,
            0x4L,
            0x2L,
            0x1L,
            0x8000L,
            0x4000L,
            0x2000L,
            0x1000L,
            0x800L,
            0x400L,
            0x200L,
            0x100L,
            0x800000L,
            0x400000L,
            0x200000L,
            0x100000L,
            0x80000L,
            0x40000L,
            0x20000L,
            0x10000L,
            0x80000000L,
            0x40000000L,
            0x20000000L,
            0x10000000L,
            0x8000000L,
            0x4000000L,
            0x2000000L,
            0x1000000L,
            0x8000000000L,
            0x4000000000L,
            0x2000000000L,
            0x1000000000L,
            0x800000000L,
            0x400000000L,
            0x200000000L,
            0x100000000L,
            0x808000000000L,
            0x404000000000L,
            0x202000000000L,
            0x101000000000L,
            0x80800000000L,
            0x40400000000L,
            0x20200000000L,
            0x10100000000L,
    };

}
