package challenges;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MateTest {

    private static final int timeLimit = 5_000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < checkmatePositions.length; i++) {
            String pos = checkmatePositions[i];
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject =
                    ExtendedPositionDescriptionParser.parseEDPPosition(pos);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getBestPrettyMoves();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public MateTest(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getFullString());

        int[] winningMoves = EPDObject.getBestMoves();
        EngineSpecifications.INFO = false;
        final Chessboard board = EPDObject.getBoard();
        int move = EngineBetter.searchFixedTime(board, timeLimit);

        Assert.assertTrue(Utils.contains(winningMoves, move));

    }

    private static final String positions = "" +
            "3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - - 1 0 bm Rxe6+; \n" +
            "1rb2k2/pp3ppQ/7q/2p1n1N1/2p5/2N5/P3BP1P/K2R4 w - - 1 0 bm Qg8+; \n" +
            "4r3/5p1k/2p1nBpp/q2p4/P1bP4/2P1R2Q/2B2PPP/6K1 w - - 1 0 bm Qxh6+; \n" +
            "2r3k1/6pp/4pp2/3bp3/1Pq5/3R1P2/r1PQ2PP/1K1RN3 b - - 0 1 bm Ra1+;\n" +
            "6R1/5r1k/p6b/1pB1p2q/1P6/5rQP/5P1K/6R1 w - - 1 0 bm Rh8+; \n" +
            "r5q1/pp1b1kr1/2p2p2/2Q5/2PpB3/1P4NP/P4P2/4RK2 w - - 1 0 bm Bg6+; \n" +
            "2r1kb1r/p2b1ppp/3p4/Q2Np1B1/4P2P/8/PP4P1/4KB1n w k - 1 0 bm Qd8+; \n" +
            "5rk1/ppp2pbp/3p2p1/1q6/4r1P1/1NP1B3/PP2nPP1/R2QR2K b - - 0 1 bm Qh5+; \n" +
            "r2q1bk1/5n1p/2p3pP/p7/3Br3/1P3PQR/P5P1/2KR4 w - - 1 0 bm Qxg6+; \n" +
            "2b5/k2n1p2/p2q4/5R1B/2p4P/P1b5/KPQ1R3/6r1 b - - 0 1 bm Qxa3+; \n" +
            "4Q3/r4ppk/3p3p/4pPbB/2P1P3/1q5P/6P1/3R3K w - - 1 0 bm Bg6+; \n" +
            "rn5r/p4pp1/3n3p/qB1k4/3P4/4P3/PP2NPPP/R4K1R w - - 1 0 bm Nf4+; \n" +
            "r2r2k1/pp2bppp/2p1p3/4qb1P/8/1BP1BQ2/PP3PP1/2KR3R b - - 0 1 bm Qxc3+; \n" +
            "5R2/6k1/3K4/p6r/1p1NB3/1P4r1/8/8 w - - 1 0 bm Ne6+; \n" +
            "5r2/1qp2pp1/bnpk3p/4NQ2/2P5/1P5P/5PP1/4R1K1 w - - 1 0 bm Nxf7+; \n" +
            "3nk1r1/1pq4p/p3PQpB/5p2/2r5/8/P4PPP/3RR1K1 w - - 1 0 bm Rxd8+; \n" +
            "5rk1/5ppp/pq6/1r3n2/2Q2P2/1P1N4/P1P1R1PP/4R2K b - - 0 1 bm Ng3+; \n" +
            "1k3r2/4R1Q1/p2q1r2/8/2p1Bb2/5R2/pP5P/K7 w - - 1 0 bm Re8+; \n" +
            "2k1r2r/ppp3p1/3b4/3pq2b/7p/2NP1P2/PPP2Q1P/R5RK b - - 0 1 bm Bxf3+; \n" +
            "3k4/1p3Bp1/p5r1/2b5/P3P1N1/5Pp1/1P1r4/2R4K b - - 0 1 bm Rh2+; \n" +
            "6k1/1r4np/pp1p1R1B/2pP2p1/P1P5/1n5P/6P1/4R2K w - - 1 0 bm Re8+; \n" +
            "8/p2q1p1k/4pQp1/1p1b2Bp/7P/8/5PP1/6K1 w - - 1 0 bm Bh6; \n" +
            "r7/6R1/ppkqrn1B/2pp3p/P6n/2N5/8/1Q1R1K2 w - - 1 0 bm Qb5+; \n" +
            "r2q1k1r/3bnp2/p1n1pNp1/3pP1Qp/Pp1P4/2PB4/5PPP/R1B2RK1 w - - 1 0 bm Qh6+; \n" +
            "6rk/1r2pR1p/3pP1pB/2p1p3/P6Q/P1q3P1/7P/5BK1 w - - 1 0 bm Rxh7+; \n" +
            "1r2Rr2/3P1p1k/5Rpp/qp6/2pQ4/7P/5PPK/8 w - - 1 0 bm Rxf7+; \n" +
            "r4rk1/5Rbp/p1qN2p1/P1n1P3/8/1Q3N1P/5PP1/5RK1 w - - 1 0 bm Rxf8+; \n" +
            "7R/3r4/8/3pkp1p/5N1P/b3PK2/5P2/8 w - - 1 0 bm Rh6; \n" +
            "8/1R3p2/3rk2p/p2p2p1/P2P2P1/3B1PN1/5K1P/r7 w - - 1 0 bm Bf5+; \n" +
            "8/5prk/p5rb/P3N2R/1p1PQ2p/7P/1P3RPq/5K2 w - - 1 0 bm Rxh6+; \n" +
            "rqb2bk1/3n2pr/p1pp2Qp/1p6/3BP2N/2N4P/PPP3P1/2KR3R w - - 1 0 bm Qe6+; \n" +
            "1Q6/r3R2p/k2p2pP/p1q5/Pp4P1/5P2/1PP3K1/8 w - - 1 0 bm Rxa7+; \n" +
            "N5k1/5p2/6p1/6Pp/4bb1P/P5r1/7K/2R3R1 b - - 0 1 bm Rg2+; \n" +
            "3R4/3Q1p2/q1rn2kp/4p3/4P3/2N3P1/5P1P/6K1 w - - 1 0 bm Qg4+; \n" +
            "6R1/2k2P2/1n5r/3p1p2/3P3b/1QP2p1q/3R4/6K1 b - - 0 1 bm Qh1+; \n" +
            "5r2/7p/3R4/p3pk2/1p2N2p/1P2BP2/6PK/4r3 w - - 1 0 bm g4+; \n" +
            "r1b1kb1r/pppp1ppp/5q2/4n3/3KP3/2N3PN/PPP4P/R1BQ1B1R b kq - 0 1 bm f8c5+; \n" +
            "r3k2r/ppp2Npp/1b5n/4p2b/2B1P2q/BQP2P2/P5PP/RN5K w kq - 1 0 bm Bb5+; \n" +
            "r1b3kr/ppp1Bp1p/1b6/n2P4/2p3q1/2Q2N2/P4PPP/RN2R1K1 w - - 1 0 bm Qxh8+; \n" +
            "r2n1rk1/1ppb2pp/1p1p4/3Ppq1n/2B3P1/2P4P/PP1N1P1K/R2Q1RN1 b - - 0 1 bm Qxf2+; \n" +
            "3q1r1k/2p4p/1p1pBrp1/p2Pp3/2PnP3/5PP1/PP1Q2K1/5R1R w - - 1 0 bm Rxh7+; \n" +
            "4r1k1/5ppp/p2p4/4r3/1pNn4/1P6/1PPK2PP/R3R3 b - - 0 1 bm Nf3+; \n" +
            "k2r3r/p3Rppp/1p4q1/1P1b4/3Q1B2/6N1/PP3PPP/6K1 w - - 1 0 bm Rxa7+; \n" +
            "r1b5/5p2/5Npk/p1pP2q1/4P2p/1PQ2R1P/6P1/6K1 w - - 1 0 bm Ng8+; \n" +
            "r1b2rk1/1p3pb1/2p3p1/p1B5/P3N3/1B1Q1Pn1/1PP3q1/2KR3R w - - 1 0 bm Nf6+; \n" +
            "r1bq1rk1/p3b1np/1pp2ppQ/3nB3/3P4/2NB1N1P/PP3PP1/3R1RK1 w - - 1 0 bm Ng5; \n" +
            "rk5r/2p3pp/p1p5/4N3/4P3/2q4P/P4PP1/R2Q2K1 w - - 1 0 bm Rb1+; \n" +
            "r3kb1r/1b4p1/pq2pn1p/1N2p3/8/3B2Q1/PPP2PPP/2KRR3 w kq - 1 0 bm Bg6+; \n" +
            "4rk2/5p1b/1p3R1K/p6p/2P2P2/1P6/2q4P/Q5R1 w - - 1 0 bm Rxf7+; \n" +
            "8/6pk/pb5p/8/1P2qP2/P3p3/2r2PNP/1QR3K1 b - - 0 1 bm exf2+; \n" +
            "r1b2nrk/1p3p1p/p2p1P2/5P2/2q1P2Q/8/PpP5/1K1R3R w - - 1 0 bm h4h7+; \n" +
            "2r5/1Nr1kpRp/p3b3/N3p3/1P3n2/P7/5PPP/K6R b - - 0 1 bm Rc1+; \n" +
            "r5k1/2p2ppp/p1P2n2/8/1pP2bbQ/1B3PP1/PP1Pq2P/RNB3K1 b - - 0 1 bm Qe1+; \n" +
            "2bqr2k/1r1n2bp/pp1pBp2/2pP1PQ1/P3PN2/1P4P1/1B5P/R3R1K1 w - - 1 0 bm Ng6+; \n" +
            "rn1q3r/pp2kppp/3Np3/2b1n3/3N2Q1/3B4/PP4PP/R1B2RK1 w - - 1 0 bm f1f7+; \n" +
            "r5rk/2p1Nppp/3p3P/pp2p1P1/4P3/2qnPQK1/8/R6R w - - 1 0 bm hxg7+; \n" +
            "1r2k1r1/pbppnp1p/1b3P2/8/Q7/B1PB1q2/P4PPP/3R2K1 w - - 1 0 bm Qxd7+; \n" +
            "Q7/p1p1q1pk/3p2rp/4n3/3bP3/7b/PP3PPK/R1B2R2 b - - 0 1 bm Bxg2; \n" +
            "r1bqr3/ppp1B1kp/1b4p1/n2B4/3PQ1P1/2P5/P4P2/RN4K1 w - - 1 0 bm Qe5; \n" +
            "r1b3kr/3pR1p1/ppq4p/5P2/4Q3/B7/P5PP/5RK1 w - - 1 0 bm Rxg7; \n" +
            "2k4r/1r1q2pp/QBp2p2/1p6/8/8/P4PPP/2R3K1 w - - 1 0 bm Qa8; \n" +
            "6kr/pp2r2p/n1p1PB1Q/2q5/2B4P/2N3p1/PPP3P1/7K w - - 1 0 bm Qg7; \n" +
            "r3k3/pbpqb1r1/1p2Q1p1/3pP1B1/3P4/3B4/PPP4P/5RK1 w - - 1 0 bm Bxg6; \n" +
            "8/6R1/p2kp2r/qb5P/3p1N1Q/1p1Pr3/PP6/1K5R w - - 1 0 bm Qe7+; \n" +
            "2b2k2/2p2r1p/p2pR3/1p3PQ1/3q3N/1P6/2P3PP/5K2 w - - 1 0 bm Ng6+; \n" +
            "1Qb2b1r/1p1k1p1p/3p1p2/3p4/p2NPP2/1R6/q1P3PP/4K2R w K - 0 1 bm Rxb7+; \n" +
            "3r2k1/3q2p1/1b3p1p/4p3/p1R1P2N/Pr5P/1PQ3P1/5R1K b - - 0 1 bm Rxh3+; \n" +
            "3r2k1/pp5p/6p1/2Ppq3/4Nr2/4B2b/PP2P2K/R1Q1R2B b - - 0 1 bm Rf2+; \n" +
            "1q1N4/3k1BQp/5r2/5p2/3P3P/8/3B1PPb/3n3K w - - 1 0 bm Be6+; \n" +
            "3r1rk1/ppqn3p/1npb1P2/5B2/2P5/2N3B1/PP2Q1PP/R5K1 w - - 1 0 bm Qg4+; \n" +
            "2r1k3/2P3R1/3P2K1/6N1/8/8/8/3r4 w - - 1 0 bm Re7+; \n" +
            "r1b2k1r/pppp4/1bP2qp1/5pp1/4pP2/1BP5/PBP3PP/R2Q1R1K b - - 0 1 bm Rxh2+; \n" +
            "rr2k3/5p2/p1bppPpQ/2p1n1P1/1q2PB2/2N4R/PP4BP/6K1 w - - 1 0 bm Qf8+; \n" +
            "2r1rk2/p1q3pQ/4p3/1pppP1N1/7p/4P2P/PP3P2/1K4R1 w - - 1 0 bm Nxe6+; \n" +
            "6r1/p3p1rk/1p1pPp1p/q3n2R/4P3/3BR2P/PPP2QP1/7K w - - 0 1 bm Rxh6+; \n" +
            "2q1nk1r/4Rp2/1ppp1P2/6Pp/3p1B2/3P3P/PPP1Q3/6K1 w - - 0 1 bm Rxe8+; \n" +
            "6k1/3b3r/1p1p4/p1n2p2/1PPNpP1q/P3Q1p1/1R1RB1P1/5K2 b - - 0 1 bm qxf4; \n" + // m5
            "2R3Bk/6p1/1p5p/4pbPP/1P1b4/5pK1/5P2/8 w - - 1 0 bm Be6+; \n" +
            "5r2/r4p1p/1p3n2/n1pp1NNk/p2P4/P1P3R1/1P5P/5RK1 w - - 1 0 bm Ng7+; \n" +
            "3r4/1b5p/ppqP1Ppk/2p1rp2/2P1P3/3n2N1/P5QP/3R1RK1 w - - 1 0 bm Nxf5+; \n" + // m5
            "6kr/4Bpb1/1p1p4/3B1P2/4R1Q1/2qn2P1/2P2P2/6K1 w - - 1 0 bm Bxf7+; \n" +
            "5r1k/2q4b/p3p2Q/1pp4p/8/1P3r2/P1P4P/1KBR2R1 w - - 1 0 bm Rd7; \n" +
            "7k/1p2r1p1/pPq4p/7R/1P1Nn2P/P5p1/1B3r2/3Q2K1 b - - 0 1 bm ng5; \n" + // m5
            "6k1/1P3p1p/3b2p1/2NQ2n1/8/2P2p1P/5PP1/4qBK1 b - - 0 1 bm nxh3+; \n" +
            "r4bk1/q5pp/3N1p2/2p5/1p2PB2/1Pp2PP1/4Q2P/1K1R4 b - - 0 1 bm c2+; \n" +
            "3kr3/pp2r3/2n2Q1p/2Rp4/2p2B2/2P3P1/2q2P1P/4R1K1 w - - 1 0 bm Qd6+; \n" +
            "2r3k1/p2R1p2/1p5Q/4N3/7P/4P3/b5PK/5q2 w - - 1 0 bm Nc6; \n" + //m6
            "5Q2/8/6p1/2p4k/p1Bpq2P/6PK/P2b4/8 w - - 1 0 bm Qh8+; \n" +
            "2Rr1qk1/5ppp/p2N4/P7/5Q2/8/1r4PP/5BK1 w - - 1 0 bm Qxf7+; \n" +
            "2qk1r2/Q3pr2/3p2pn/7p/5P2/4B2P/P1P3P1/1R4K1 w - - 1 0 bm Bb6+; \n" +
            "rnb3kb/pp5p/4p1pB/q1p2pN1/2r1PQ2/2P5/P4PPP/2R2RK1 w - - 1 0 bm Qd6; \n" +
            "3Q4/4r1pp/b6k/6R1/8/1qBn1N2/1P4PP/6KR w - - 1 0 bm Bxg7+; \n" +
            "2r5/1p5p/3p4/pP1P1R2/1n2B1k1/8/1P3KPP/8 w - - 1 0 bm h3+; \n" +
            "8/5r1p/R3pk2/2NR1np1/2P2r2/1P5K/P6P/8 b - - 0 1 bm g4+; \n" +
            "r1b1kr2/3q1p2/p1Q5/3p3p/8/3B4/PPP2PPP/R5K1 w q - 0 1 bm Re1+; \n" +
            "5r2/R4Nkp/1p4p1/2nR2N1/5p2/7P/6PK/1r6 w - - 1 0 bm Nh6+; \n" +
            "8/8/5K2/6r1/8/8/5Q1p/7k w - - 1 0 bm Qf1+; \n" +
            "6k1/4pp2/2q3pp/R1p1Pn2/2N2P2/1P4rP/1P3Q1K/8 b - - 0 1 bm Rxh3+; \n" +
            "4kb1Q/5p2/1p6/1K1N4/2P2P2/8/q7/8 w - - 1 0 bm Qe5+; \n" +
            "q3r3/4b1pn/pNrp2kp/1p6/4P3/1Q2B3/PPP1B1PP/7K w - - 1 0 bm Bh5+; \n" +
            "r4r1k/1p3p1p/pp1p1p2/4qN1R/PP2P1n1/6Q1/5PPP/R5K1 w - - 1 0 bm Rxh7+; \n" +
            "r3rn1k/4b1Rp/pp1p2pB/3Pp3/P2qB1Q1/8/2P3PP/5R1K w - - 1 0 bm Rxf8+; \n" +
            "r1r3k1/1bq2pbR/p5p1/1pnpp1B1/3NP3/3B1P2/PPPQ4/1K5R w - - 1 0 bm Bf6; \n" +
            "rk3q1r/pbp4p/1p3P2/2p1N3/3p2Q1/3P4/PPP3PP/R3R1K1 w - - 1 0 bm Nd7+; \n" +
            "3r3k/6pp/p3Qn2/P3N3/4q3/2P4P/5PP1/6K1 w - - 1 0 bm Nf7+; \n" +
            "4k3/2q2p2/4p3/3bP1Q1/p6R/r6P/6PK/5B2 w - - 1 0 bm Bb5+; \n" +
            "3r1b2/3P1p2/p3rpkp/2q2N2/5Q1R/2P3BP/P5PK/8 w - - 1 0 bm Rxh6+; \n" +
            "r3kr2/ppq3bp/2np2p1/2pBp1B1/4P1Q1/2PP4/PP3PPP/R3K2R w KQq - 0 1 bm Bxc6+; \n" +
            "r2qr1k1/1p1n2pp/2b1p3/p2pP1b1/P2P1Np1/3BPR2/1PQB3P/5RK1 w - - 1 0 bm Bxh7+; \n" +
            "7r/pRpk4/2np2p1/5b2/2P4q/2b1BBN1/P4PP1/3Q1K2 b - - 0 1 bm Bd3+;\n" +
            "1r3rk1/5p1p/pp2b1p1/4n3/4PP2/1BP1B1Pq/P6P/R1QR2K1 b - - 0 1 bm Nf3+; \n" +
            "2q2r1k/5Qp1/4p1P1/3p4/r6b/7R/5BPP/5RK1 w - - 1 0 bm Bxh4; \n" +
            "1r2k3/2pn1p2/p1Qb3p/7q/3PP3/2P1BN1b/PP1N1Pr1/RR5K b - - 0 1 bm Rg1+; \n" +
            "r1bqr3/4pkbp/2p1N2B/p2nP1Q1/2pP4/2N2P2/PP4P1/R3K2R w - - 1 0 bm Qxg7+; \n" +
            "3R4/rr2pp1k/1p1p1np1/1B1Pq2p/1P2P3/5P2/3Q2PP/2R3K1 w - - 1 0 bm Rh8+; \n" +
            "3Rr2k/pp4pb/2p4p/2P1n3/1P1Q3P/4r1q1/PB4B1/5RK1 b - - 0 1 bm Nf3+; \n" +
            "5r2/1pP1b1p1/pqn1k2p/4p3/QP2BP2/P3P1PK/3R4/3R4 w - - 1 0 bm Qb3+; \n" +
            "4r3/2B4B/2p1b3/ppk5/5R2/P2P3p/1PP5/1K5R w - - 1 0 bm b4+; \n" +
            "1r3r1k/6R1/1p2Qp1p/p1p4N/3pP3/3P1P2/PP2q2P/5R1K w - - 1 0 bm Rh7+; \n" +
            "r1bq1rk1/4np1p/1p3RpB/p1Q5/2Bp4/3P4/PPP3PP/R5K1 w - - 1 0 bm Qe5; \n" + // m4
            "2rr3k/1p1b1pq1/4pNp1/Pp2Q2p/3P4/7R/5PPP/4R1K1 w - - 1 0 bm Rxh5+; \n" +
            "3rnn2/p1r2pkp/1p2pN2/2p1P3/5Q1N/2P3P1/PP2qPK1/R6R w - - 1 0 bm Qg5+; \n" +
            "r3r1n1/pp3pk1/2q2p1p/P2NP3/2p1QP2/8/1P5P/1B1R3K w - - 1 0 bm Rg1+; \n" +
            "4Br1k/p5pp/1n6/8/3PQbq1/6P1/PP5P/RNB3K1 b - - 0 1 bm Be3+; \n" +
            "5rk1/pR4bp/6p1/6B1/5Q2/4P3/q2r1PPP/5RK1 w - - 1 0 bm Rxg7+; \n" +
            "1r2r3/2p2pkp/p1b2Np1/4P3/2p4P/qP4N1/2P2QP1/5RK1 w - - 1 0 bm Nfh5+; \n" +
            "1b4rk/4R1pp/p1b4r/2PB4/Pp1Q4/6Pq/1P3P1P/4RNK1 w - - 1 0 bm Qxg7+; \n" +
            "4k2r/1R3R2/p3p1pp/4b3/1BnNr3/8/P1P5/5K2 w - - 1 0 bm Rfe7+; \n" +
            "1R2n1k1/r3pp1p/6p1/3P4/P1p2B2/6P1/5PKP/b7 w - - 1 0 bm Bh6; \n" +
            "r1b1k2N/pppp2pp/2n5/2b1p3/2B1n2q/2N3P1/PPPP1P1P/R1BQK2R b KQq - 0 1 bm Bxf2+; \n" +
            "2r4k/ppqbpQ1p/3p1bpB/8/8/1Nr2P2/PPP3P1/2KR3R w - - 1 0 bm Bg7+; \n" +
            "r1brn3/p1q4p/p1p2P1k/2PpPPp1/P7/1Q2B2P/1P6/1K1R1R2 w - - 1 0 bm Bxg5+; \n" +
            "r1qr3k/3R2p1/p3Q3/1p2p1p1/3bN3/8/PP3PPP/5RK1 w - - 1 0 bm Qh3+; \n" +
            "r7/5B2/3npkP1/7K/1P3p2/5P2/p7/R7 b - - 0 1 bm Rh8+; \n" +
            "2kr1b1r/pp1n1ppp/2n1p3/1N1pP3/QP1P4/P2q1N2/3B1PPP/2R1K2R w K - 0 1 bm Rxc6+; \n" +
            "r1b1k2r/pp2bpp1/1np1p2p/8/4BB2/3R1N2/qPP1QPPP/2K4R b kq - 0 1 bm Qa1+; \n" +
            "2r3k1/p4p2/1p2P1pQ/3bR2p/1q6/1B6/PP2RPr1/5K2 w - - 1 0 bm exf7+; \n" +
            "r1bkr3/1p3ppp/p1p5/4P3/2B1n3/2P1B3/P1P3PP/R4RK1 w - - 1 0 bm Bb6+; \n" +
            "r4r1k/pppq1p1p/3p4/5p1Q/2B1Pp2/3P3P/PPn2P1K/R5R1 w - - 1 0 bm Rg7; \n" +
            "2kr1b1r/ppq5/1np1pp2/P3Pn2/1P3P2/2P2Qp1/6P1/RNB1RBK1 b - - 0 1 bm Rh1+; \n" +
            "2Q5/4ppbk/3p4/3P1NPp/4P3/5NB1/5PPK/rq6 w - - 1 0 bm g6+; \n" +
            "5r1k/p1p1q1pp/1p1p4/8/2PPn3/B1P1P3/P1Q1P2p/1R5K b - - 0 1 bm Nf2+; \n" +
            "2b5/3qr2k/5Q1p/P3B3/1PB1PPp1/4K1P1/8/8 w - - 1 0 bm Bg8+; \n" +
            "r1bq1bkr/6pp/p1p3P1/1p1p3Q/4P3/8/PPP3PP/RNB2RK1 w - - 1 0 bm gxh7+; \n" +
            "rr4Rb/2pnqb1k/np1p1p1B/3PpP2/p1P1P2P/2N3R1/PP2BP2/1KQ5 w - - 1 0 bm Bg7 g8h8; \n" + // m4
            "r4b1r/pp1n2k1/1qp1p2p/3pP1pQ/1P6/2BP2N1/P4PPP/R4RK1 w - - 1 0 bm Nf5+; \n" +
            "5k2/r3pp1p/6p1/q1pP3R/5B2/2b3PP/PQ3PK1/R7 w - - 1 0 bm Qb8+; \n" +
            "r3r1k1/pp3pb1/3pb1p1/q5B1/1n2N3/3B1N2/PPP2PPQ/2K4R w - - 1 0 bm Qh7+; \n" +
            "4r1rk/pQ2P2p/P7/2pqb3/3p1p2/8/3B2PP/4RRK1 b - - 0 1 bm Rxg2+; \n" +
            "r6r/pp2pk1p/1n3b2/5Q1N/8/3B4/q4PPP/3RR1K1 w - - 1 0 bm Rxe7+; \n" +
            "rnb3kr/ppp2ppp/1b6/3q4/3pN3/Q4N2/PPP2KPP/R1B1R3 w - - 0 1 bm e4f6; \n" +
            "3k4/1pp3b1/4b2p/1p3qp1/3Pn3/2P1RN2/r5P1/1Q2R1K1 b - - 0 1 bm Rxg2+; ";

    private static final String[] checkmatePositions = positions.split("\n");
}


