package bitboards;

public class Queen {

    static long A1 = 0x8182848890a0c07fL;
    static long B1 = 0x404142444850e0bfL;
    static long C1 = 0x2020212224a870dfL;
    static long D1 = 0x10101011925438efL;
    static long E1 = 0x8080888492a1cf7L;
    static long F1 = 0x404844424150efbL;
    static long G1 = 0x2824222120a07fdL;
    static long H1 = 0x81412111090503feL;
    static long A2 = 0x82848890a0c07fc0L;
    static long B2 = 0x4142444850e0bfe0L;
    static long C2 = 0x20212224a870df70L;
    static long D2 = 0x101011925438ef38L;
    static long E2 = 0x80888492a1cf71cL;
    static long F2 = 0x4844424150efb0eL;
    static long G2 = 0x824222120a07fd07L;
    static long H2 = 0x412111090503fe03L;
    static long A3 = 0x848890a0c07fc0a0L;
    static long B3 = 0x42444850e0bfe050L;
    static long C3 = 0x212224a870df70a8L;
    static long D3 = 0x1011925438ef3854L;
    static long E3 = 0x888492a1cf71c2aL;
    static long F3 = 0x844424150efb0e15L;
    static long G3 = 0x4222120a07fd070aL;
    static long H3 = 0x2111090503fe0305L;
    static long A4 = 0x8890a0c07fc0a090L;
    static long B4 = 0x444850e0bfe05048L;
    static long C4 = 0x2224a870df70a824L;
    static long D4 = 0x11925438ef385492L;
    static long E4 = 0x88492a1cf71c2a49L;
    static long F4 = 0x4424150efb0e1524L;
    static long G4 = 0x22120a07fd070a12L;
    static long H4 = 0x11090503fe030509L;
    static long A5 = 0x90a0c07fc0a09088L;
    static long B5 = 0x4850e0bfe0504844L;
    static long C5 = 0x24a870df70a82422L;
    static long D5 = 0x925438ef38549211L;
    static long E5 = 0x492a1cf71c2a4988L;
    static long F5 = 0x24150efb0e152444L;
    static long G5 = 0x120a07fd070a1222L;
    static long H5 = 0x90503fe03050911L;
    static long A6 = 0xa0c07fc0a0908884L;
    static long B6 = 0x50e0bfe050484442L;
    static long C6 = 0xa870df70a8242221L;
    static long D6 = 0x5438ef3854921110L;
    static long E6 = 0x2a1cf71c2a498808L;
    static long F6 = 0x150efb0e15244484L;
    static long G6 = 0xa07fd070a122242L;
    static long H6 = 0x503fe0305091121L;
    static long A7 = 0xc07fc0a090888482L;
    static long B7 = 0xe0bfe05048444241L;
    static long C7 = 0x70df70a824222120L;
    static long D7 = 0x38ef385492111010L;
    static long E7 = 0x1cf71c2a49880808L;
    static long F7 = 0xefb0e1524448404L;
    static long G7 = 0x7fd070a12224282L;
    static long H7 = 0x3fe030509112141L;
    static long A8 = 0x7fc0a09088848281L;
    static long B8 = 0xbfe0504844424140L;
    static long C8 = 0xdf70a82422212020L;
    static long D8 = 0xef38549211101010L;
    static long E8 = 0xf71c2a4988080808L;
    static long F8 = 0xfb0e152444840404L;
    static long G8 = 0xfd070a1222428202L;
    static long H8 = 0xfe03050911214181L;

    public static long[] QUEEN_MOVE_TABLE = {
            0x8182848890a0c07fL,
            0x404142444850e0bfL,
            0x2020212224a870dfL,
            0x10101011925438efL,
            0x8080888492a1cf7L,
            0x404844424150efbL,
            0x2824222120a07fdL,
            0x81412111090503feL,
            0x82848890a0c07fc0L,
            0x4142444850e0bfe0L,
            0x20212224a870df70L,
            0x101011925438ef38L,
            0x80888492a1cf71cL,
            0x4844424150efb0eL,
            0x824222120a07fd07L,
            0x412111090503fe03L,
            0x848890a0c07fc0a0L,
            0x42444850e0bfe050L,
            0x212224a870df70a8L,
            0x1011925438ef3854L,
            0x888492a1cf71c2aL,
            0x844424150efb0e15L,
            0x4222120a07fd070aL,
            0x2111090503fe0305L,
            0x8890a0c07fc0a090L,
            0x444850e0bfe05048L,
            0x2224a870df70a824L,
            0x11925438ef385492L,
            0x88492a1cf71c2a49L,
            0x4424150efb0e1524L,
            0x22120a07fd070a12L,
            0x11090503fe030509L,
            0x90a0c07fc0a09088L,
            0x4850e0bfe0504844L,
            0x24a870df70a82422L,
            0x925438ef38549211L,
            0x492a1cf71c2a4988L,
            0x24150efb0e152444L,
            0x120a07fd070a1222L,
            0x90503fe03050911L,
            0xa0c07fc0a0908884L,
            0x50e0bfe050484442L,
            0xa870df70a8242221L,
            0x5438ef3854921110L,
            0x2a1cf71c2a498808L,
            0x150efb0e15244484L,
            0xa07fd070a122242L,
            0x503fe0305091121L,
            0xc07fc0a090888482L,
            0xe0bfe05048444241L,
            0x70df70a824222120L,
            0x38ef385492111010L,
            0x1cf71c2a49880808L,
            0xefb0e1524448404L,
            0x7fd070a12224282L,
            0x3fe030509112141L,
            0x7fc0a09088848281L,
            0xbfe0504844424140L,
            0xdf70a82422212020L,
            0xef38549211101010L,
            0xf71c2a4988080808L,
            0xfb0e152444840404L,
            0xfd070a1222428202L,
            0xfe03050911214181L,
    };






}
