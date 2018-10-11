//package main.bitboards;
//
//public class Rook {
//
//    static long A1 = 0x1010101010101feL;
//    static long B1 = 0x2020202020202fdL;
//    static long C1 = 0x4040404040404fbL;
//    static long D1 = 0x8080808080808f7L;
//    static long E1 = 0x10101010101010efL;
//    static long F1 = 0x20202020202020dfL;
//    static long G1 = 0x40404040404040bfL;
//    static long H1 = 0x808080808080807fL;
//    static long A2 = 0x10101010101fe01L;
//    static long B2 = 0x20202020202fd02L;
//    static long C2 = 0x40404040404fb04L;
//    static long D2 = 0x80808080808f708L;
//    static long E2 = 0x101010101010ef10L;
//    static long F2 = 0x202020202020df20L;
//    static long G2 = 0x404040404040bf40L;
//    static long H2 = 0x8080808080807f80L;
//    static long A3 = 0x101010101fe0101L;
//    static long B3 = 0x202020202fd0202L;
//    static long C3 = 0x404040404fb0404L;
//    static long D3 = 0x808080808f70808L;
//    static long E3 = 0x1010101010ef1010L;
//    static long F3 = 0x2020202020df2020L;
//    static long G3 = 0x4040404040bf4040L;
//    static long H3 = 0x80808080807f8080L;
//    static long A4 = 0x1010101fe010101L;
//    static long B4 = 0x2020202fd020202L;
//    static long C4 = 0x4040404fb040404L;
//    static long D4 = 0x8080808f7080808L;
//    static long E4 = 0x10101010ef101010L;
//    static long F4 = 0x20202020df202020L;
//    static long G4 = 0x40404040bf404040L;
//    static long H4 = 0x808080807f808080L;
//    static long A5 = 0x10101fe01010101L;
//    static long B5 = 0x20202fd02020202L;
//    static long C5 = 0x40404fb04040404L;
//    static long D5 = 0x80808f708080808L;
//    static long E5 = 0x101010ef10101010L;
//    static long F5 = 0x202020df20202020L;
//    static long G5 = 0x404040bf40404040L;
//    static long H5 = 0x8080807f80808080L;
//    static long A6 = 0x101fe0101010101L;
//    static long B6 = 0x202fd0202020202L;
//    static long C6 = 0x404fb0404040404L;
//    static long D6 = 0x808f70808080808L;
//    static long E6 = 0x1010ef1010101010L;
//    static long F6 = 0x2020df2020202020L;
//    static long G6 = 0x4040bf4040404040L;
//    static long H6 = 0x80807f8080808080L;
//    static long A7 = 0x1fe010101010101L;
//    static long B7 = 0x2fd020202020202L;
//    static long C7 = 0x4fb040404040404L;
//    static long D7 = 0x8f7080808080808L;
//    static long E7 = 0x10ef101010101010L;
//    static long F7 = 0x20df202020202020L;
//    static long G7 = 0x40bf404040404040L;
//    static long H7 = 0x807f808080808080L;
//    static long A8 = 0xfe01010101010101L;
//    static long B8 = 0xfd02020202020202L;
//    static long C8 = 0xfb04040404040404L;
//    static long D8 = 0xf708080808080808L;
//    static long E8 = 0xef10101010101010L;
//    static long F8 = 0xdf20202020202020L;
//    static long G8 = 0xbf40404040404040L;
//    static long H8 = 0x7f80808080808080L;
//
//    public static long[] ROOK_MOVE_TABLE = {
//            0x1010101010101feL,
//            0x2020202020202fdL,
//            0x4040404040404fbL,
//            0x8080808080808f7L,
//            0x10101010101010efL,
//            0x20202020202020dfL,
//            0x40404040404040bfL,
//            0x808080808080807fL,
//            0x10101010101fe01L,
//            0x20202020202fd02L,
//            0x40404040404fb04L,
//            0x80808080808f708L,
//            0x101010101010ef10L,
//            0x202020202020df20L,
//            0x404040404040bf40L,
//            0x8080808080807f80L,
//            0x101010101fe0101L,
//            0x202020202fd0202L,
//            0x404040404fb0404L,
//            0x808080808f70808L,
//            0x1010101010ef1010L,
//            0x2020202020df2020L,
//            0x4040404040bf4040L,
//            0x80808080807f8080L,
//            0x1010101fe010101L,
//            0x2020202fd020202L,
//            0x4040404fb040404L,
//            0x8080808f7080808L,
//            0x10101010ef101010L,
//            0x20202020df202020L,
//            0x40404040bf404040L,
//            0x808080807f808080L,
//            0x10101fe01010101L,
//            0x20202fd02020202L,
//            0x40404fb04040404L,
//            0x80808f708080808L,
//            0x101010ef10101010L,
//            0x202020df20202020L,
//            0x404040bf40404040L,
//            0x8080807f80808080L,
//            0x101fe0101010101L,
//            0x202fd0202020202L,
//            0x404fb0404040404L,
//            0x808f70808080808L,
//            0x1010ef1010101010L,
//            0x2020df2020202020L,
//            0x4040bf4040404040L,
//            0x80807f8080808080L,
//            0x1fe010101010101L,
//            0x2fd020202020202L,
//            0x4fb040404040404L,
//            0x8f7080808080808L,
//            0x10ef101010101010L,
//            0x20df202020202020L,
//            0x40bf404040404040L,
//            0x807f808080808080L,
//            0xfe01010101010101L,
//            0xfd02020202020202L,
//            0xfb04040404040404L,
//            0xf708080808080808L,
//            0xef10101010101010L,
//            0xdf20202020202020L,
//            0xbf40404040404040L,
//            0x7f80808080808080L,
//    };
//
//
//
//
//
//}
