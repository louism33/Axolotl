package bitboards;

public class Bishop {

    static long A1 = 0x102040810204000L;
    static long B1 = 0x102040810a000L;
    static long C1 = 0x10204885000L;
    static long D1 = 0x182442800L;
    static long E1 = 0x8041221400L;
    static long F1 = 0x804020110a00L;
    static long G1 = 0x80402010080500L;
    static long H1 = 0x8040201008040200L;
    static long A2 = 0x204081020400040L;
    static long B2 = 0x102040810a000a0L;
    static long C2 = 0x1020488500050L;
    static long D2 = 0x18244280028L;
    static long E2 = 0x804122140014L;
    static long F2 = 0x804020110a000aL;
    static long G2 = 0x8040201008050005L;
    static long H2 = 0x4020100804020002L;
    static long A3 = 0x408102040004020L;
    static long B3 = 0x2040810a000a010L;
    static long C3 = 0x102048850005088L;
    static long D3 = 0x1824428002844L;
    static long E3 = 0x80412214001422L;
    static long F3 = 0x804020110a000a11L;
    static long G3 = 0x4020100805000508L;
    static long H3 = 0x2010080402000204L;
    static long A4 = 0x810204000402010L;
    static long B4 = 0x40810a000a01008L;
    static long C4 = 0x204885000508804L;
    static long D4 = 0x182442800284482L;
    static long E4 = 0x8041221400142241L;
    static long F4 = 0x4020110a000a1120L;
    static long G4 = 0x2010080500050810L;
    static long H4 = 0x1008040200020408L;
    static long A5 = 0x1020400040201008L;
    static long B5 = 0x810a000a0100804L;
    static long C5 = 0x488500050880402L;
    static long D5 = 0x8244280028448201L;
    static long E5 = 0x4122140014224180L;
    static long F5 = 0x20110a000a112040L;
    static long G5 = 0x1008050005081020L;
    static long H5 = 0x804020002040810L;
    static long A6 = 0x2040004020100804L;
    static long B6 = 0x10a000a010080402L;
    static long C6 = 0x8850005088040201L;
    static long D6 = 0x4428002844820100L;
    static long E6 = 0x2214001422418000L;
    static long F6 = 0x110a000a11204080L;
    static long G6 = 0x805000508102040L;
    static long H6 = 0x402000204081020L;
    static long A7 = 0x4000402010080402L;
    static long B7 = 0xa000a01008040201L;
    static long C7 = 0x5000508804020100L;
    static long D7 = 0x2800284482010000L;
    static long E7 = 0x1400142241800000L;
    static long F7 = 0xa000a1120408000L;
    static long G7 = 0x500050810204080L;
    static long H7 = 0x200020408102040L;
    static long A8 = 0x40201008040201L;
    static long B8 = 0xa0100804020100L;
    static long C8 = 0x50880402010000L;
    static long D8 = 0x28448201000000L;
    static long E8 = 0x14224180000000L;
    static long F8 = 0xa112040800000L;
    static long G8 = 0x5081020408000L;
    static long H8 = 0x2040810204080L;

    public static long[] BISHOP_MOVE_TABLE = {
            0x102040810204000L,
            0x102040810a000L,
            0x10204885000L,
            0x182442800L,
            0x8041221400L,
            0x804020110a00L,
            0x80402010080500L,
            0x8040201008040200L,
            0x204081020400040L,
            0x102040810a000a0L,
            0x1020488500050L,
            0x18244280028L,
            0x804122140014L,
            0x804020110a000aL,
            0x8040201008050005L,
            0x4020100804020002L,
            0x408102040004020L,
            0x2040810a000a010L,
            0x102048850005088L,
            0x1824428002844L,
            0x80412214001422L,
            0x804020110a000a11L,
            0x4020100805000508L,
            0x2010080402000204L,
            0x810204000402010L,
            0x40810a000a01008L,
            0x204885000508804L,
            0x182442800284482L,
            0x8041221400142241L,
            0x4020110a000a1120L,
            0x2010080500050810L,
            0x1008040200020408L,
            0x1020400040201008L,
            0x810a000a0100804L,
            0x488500050880402L,
            0x8244280028448201L,
            0x4122140014224180L,
            0x20110a000a112040L,
            0x1008050005081020L,
            0x804020002040810L,
            0x2040004020100804L,
            0x10a000a010080402L,
            0x8850005088040201L,
            0x4428002844820100L,
            0x2214001422418000L,
            0x110a000a11204080L,
            0x805000508102040L,
            0x402000204081020L,
            0x4000402010080402L,
            0xa000a01008040201L,
            0x5000508804020100L,
            0x2800284482010000L,
            0x1400142241800000L,
            0xa000a1120408000L,
            0x500050810204080L,
            0x200020408102040L,
            0x40201008040201L,
            0xa0100804020100L,
            0x50880402010000L,
            0x28448201000000L,
            0x14224180000000L,
            0xa112040800000L,
            0x5081020408000L,
            0x2040810204080L,
    };



}
