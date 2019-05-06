package strategictestsuite;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.axolotl.search.SearchSpecs;
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

import static challenges.Utils.contains;
import static com.github.louism33.utils.ExtendedPositionDescriptionParser.parseEDPPosition;


@RunWith(Parameterized.class)
public class STS7Simplification {


    private static final int timeLimit = 10_000;
    private Engine engine = new Engine();
    private static int successes = 0;

    @AfterClass
    public static void finalSuccessTally() {
        System.out.println("Successes: " + successes + " out of " + splitUpPositions.length);
    }

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        EngineSpecifications.PRINT_PV = true;

        for (int i = 0; i < splitUpPositions.length; i++) {

            String splitUpWAC = splitUpPositions[i];
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = parseEDPPosition(splitUpWAC);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public STS7Simplification(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        System.out.println(EPDObject.getFullString());
        System.out.println(EPDObject.getBoard());
        int[] winningMoves = EPDObject.getBestMovesFromComments();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.PRINT_PV = false;
        SearchSpecs.basicTimeSearch(timeLimit);

        final int move = engine.simpleSearch(EPDObject.getBoard());

        System.out.println("my move: " + MoveParser.toString(move));

        final boolean condition = contains(winningMoves, move) && !contains(losingMoves, move);
        if (condition) {
            successes++;
        }
        Assert.assertTrue(condition);
    }

    private static final String positions = "" +
            "1R3b2/r4pk1/2qpn1p1/P1p1p2p/2P1P2P/5PP1/6K1/1Q1BB3 w - - bm Qb6; id \"STS(v7.0) Simplification.001\"; c0 \"Qb6=10, Bc3=7, Kf1=7, Qb5=9\";\n" +
            "1b4k1/6p1/pr2p2p/5p2/P2N4/P1Q1P1qP/6P1/5RK1 b - - bm Be5; id \"STS(v7.0) Simplification.002\"; c0 \"Be5=10, Bd6=3, Kh7=4, Qh2+=7\";\n" +
            "1k1r1b1r/ppqbnp2/4p1pp/n2pP3/2pP4/P1P3B1/1P1NBPPP/1R1QRNK1 b - - bm Nf5; id \"STS(v7.0) Simplification.003\"; c0 \"Nf5=10\";\n" +
            "1nb2rk1/2p1pqb1/1r2p2p/R6P/1PBP2p1/B4NN1/5PK1/3QR3 w - - bm Rf5; id \"STS(v7.0) Simplification.004\"; c0 \"Rf5=10\";\n" +
            "1nr3k1/1pr2pp1/p3bn2/P3p2p/1PP4P/3BNPP1/3RN1K1/R7 w - - bm Bf5; id \"STS(v7.0) Simplification.005\"; c0 \"Bf5=10, Nc3=1, Rdd1=2, b5=1\";\n" +
            "1q1rb1k1/5p1p/p3pnpb/P1r5/2B1P3/1P3P2/2R1N1PP/R2NQ2K b - - bm Bb5; id \"STS(v7.0) Simplification.006\"; c0 \"Bb5=10\";\n" +
            "1r1r2k1/2p1qpp1/pnP1b2p/1p2P3/3N4/P3P2P/1QB2PP1/3RR1K1 w - - bm Qb4; id \"STS(v7.0) Simplification.007\"; c0 \"Qb4=10, Bd3=7, Qc3=8, f4=9\";\n" +
            "1r2kbQ1/1r1q1p2/p2p2n1/6P1/N1pBP3/P1N5/1PPR4/K7 w - - bm Bg7; id \"STS(v7.0) Simplification.008\"; c0 \"Bg7=10, Bc5=7, Bf6=5, Rf2=5\";\n" +
            "1r2r1k1/5pp1/R2p3n/3P1P1p/2p2PP1/1qb2B1P/Q7/2BR1K2 b - - bm Bd2; id \"STS(v7.0) Simplification.009\"; c0 \"Bd2=10, Bb2=5, Bb4=5, hxg4=3\";\n" +
            "1r3qk1/rpb2pp1/2p2n1p/p1P1p3/P1bPP2Q/2N1PN1P/1RB3P1/1R5K w - - bm Bb3; id \"STS(v7.0) Simplification.010\"; c0 \"Bb3=10, Qe1=6, Qf2=6, g4=6\";\n" +
            "1r3rk1/1nqb1p1p/p3p1p1/1ppPb3/2P1N3/1P1Q2PP/P2B2BK/1RR5 w - - bm Bc3; id \"STS(v7.0) Simplification.011\"; c0 \"Bc3=10, Bf4=6, Qf3=5, Rf1=5\";\n" +
            "1r4k1/3b1pbp/R2Np1p1/4n3/1P1NP3/3BP3/4K1PP/8 w - - bm Nf3; id \"STS(v7.0) Simplification.012\"; c0 \"Nf3=10, Nc4=3, Ra7=1, b5=1\";\n" +
            "1r4k1/4qpp1/r1b4p/pp1pPB2/n1pP3P/2P2N2/R1P3P1/R3Q1K1 b - - bm Bd7; id \"STS(v7.0) Simplification.013\"; c0 \"Bd7=10, Be8=8, Rab6=8, Re8=7\";\n" +
            "1r4k1/5bpp/3b2p1/3PpqPn/1pB5/rP1NBP2/P4Q2/1K1R3R w - - bm Bc5; id \"STS(v7.0) Simplification.014\"; c0 \"Bc5=10, Rd2=1, Rh2=5, Rh4=1\";\n" +
            "1r4k1/pp1r1p1p/2np1np1/2q5/P3PB2/1QP2B2/5PPP/R2R2K1 w - - bm Qb5; id \"STS(v7.0) Simplification.015\"; c0 \"Qb5=10, Ra2=2, a5=3, h3=2\";\n" +
            "1rb1r1k1/2q1bp1p/3p1np1/p1nPp3/4P3/1PRB1N1P/3BQPP1/1R3NK1 w - - bm Be3; id \"STS(v7.0) Simplification.016\"; c0 \"Be3=10, Bc1=3, Rbc1=4, Rcc1=4\";\n" +
            "2b1r1k1/p1B1ppbp/1p4p1/8/4n3/7P/P1RRBPP1/6K1 w - - bm Rd8; id \"STS(v7.0) Simplification.017\"; c0 \"Rd8=10\";\n" +
            "2bq2k1/4pp2/p2p1n2/n1pP2p1/2P3Pp/P1Q1N2P/3NPPB1/6K1 w - - bm Be4; id \"STS(v7.0) Simplification.018\"; c0 \"Be4=10, Nc2=8, Nd1=7, Nef1=9\";\n" +
            "2br3k/2pqb2r/p1n1n3/P3PQ2/1pB5/5N2/1P1N1PPP/R3R1K1 b - - bm Ncd4; id \"STS(v7.0) Simplification.019\"; c0 \"Ncd4=10, Nc5=4, Ned4=5, Rf8=4\";\n" +
            "2q1r1k1/1p2rpp1/p1p3pb/2Pp2n1/3P1R2/1Q2P1P1/PP3NKP/2B2R2 b - - bm Ne4; id \"STS(v7.0) Simplification.020\"; c0 \"Ne4=10, Nh7=8, Qd7=7, Qd8=7\";\n" +
            "2r1r1k1/1bq1bpp1/pp1pp1n1/2n3P1/2P1PP1p/BPN1QB1P/P1R5/3RN1K1 w - - bm Bh5; id \"STS(v7.0) Simplification.021\"; c0 \"Bh5=10, Bg4=3, Ng2=9\";\n" +
            "2r1r1k1/1p1bn1p1/p2Bpb1p/5p1q/P2P4/5NQP/1P2BPP1/R2R2K1 w - - bm Ne5; id \"STS(v7.0) Simplification.022\"; c0 \"Ne5=10\";\n" +
            "2r1r1k1/pN2b1p1/2p4p/1p1nBp1P/3P2n1/1NP5/PP2KPP1/3R3R b - - bm Bf6; id \"STS(v7.0) Simplification.023\"; c0 \"Bf6=10\";\n" +
            "2r1r1k1/pb2bp1p/8/2p1Rp1q/P1N5/6P1/2Q2P1P/3R1BK1 w - - bm Bg2; id \"STS(v7.0) Simplification.024\"; c0 \"Bg2=10, Be2=5, Ne3=6, Rxf5=9\";\n" +
            "2r1rk2/1p2bpp1/p4nbp/4R3/P2R1P1B/2N5/1P2B1PP/5K2 b - - bm Red8; id \"STS(v7.0) Simplification.025\"; c0 \"Red8=10\";\n" +
            "2r2n2/6k1/1pNBbb2/3p1p2/3P1P2/P4KP1/2R5/4N3 w - - bm Ne5; id \"STS(v7.0) Simplification.026\"; c0 \"Ne5=10, Ke2=8, Ke3=8, Kf2=7\";\n" +
            "2r2rk1/p3qp1p/2b1pp1Q/2b5/1n6/2N2NP1/1P2PPBP/R2R2K1 w - - bm Nh4; id \"STS(v7.0) Simplification.027\"; c0 \"Nh4=10, Nd2=4, Ne1=4, Ra5=5\";\n" +
            "2r3k1/1p1b1pb1/1qn3p1/pN1p2Pp/P3r1nP/1NP3B1/1P2BPR1/R2Q2K1 b - - bm Be5; id \"STS(v7.0) Simplification.028\"; c0 \"Be5=10, Bf8=3, Na7=4, Re6=4\";\n" +
            "2r3k1/2q1bpp1/p1P2n1p/4pb2/8/2N1BB2/1rPR1QPP/R5K1 w - - bm Nd5; id \"STS(v7.0) Simplification.029\"; c0 \"Nd5=10, Na4=8, Nd1=8, Rxa6=8\";\n" +
            "2r3k1/p1q1bp1p/2p1p1p1/4P3/1p6/1P1QP1P1/PB3P1P/3R2K1 b - - bm Rd8; id \"STS(v7.0) Simplification.030\"; c0 \"Rd8=10, Qa5=3, a5=1, c5=1\";\n" +
            "2r3k1/p2q1pp1/2p4p/1p1p1b1n/2PP4/P1QP1PP1/3B3P/4RBK1 b - - bm Bh3; id \"STS(v7.0) Simplification.031\"; c0 \"Bh3=10\";\n" +
            "2r5/4k1p1/4p2p/1pnbPp2/3B4/2P5/1PB3PP/5R1K b - - bm Be4; id \"STS(v7.0) Simplification.032\"; c0 \"Be4=10, Kf7=4, Nb3=5, Rc7=4\";\n" +
            "2r5/p4k2/4bp2/Ppp3p1/4p3/B1P4r/2P2P1P/R3KNR1 w - - bm Rg3; id \"STS(v7.0) Simplification.033\"; c0 \"Rg3=10, Bb2=7, Bc1=8, Rg2=8\";\n" +
            "2rn4/2p1rpk1/p3n1p1/P3N1P1/BpN1b2P/1P4K1/2PR4/3R4 w - - bm Rd7; id \"STS(v7.0) Simplification.034\"; c0 \"Rd7=10\";\n" +
            "2rq1rk1/pb3p1p/1p1p2p1/4p3/1PPnP3/2N2P2/P2QB1PP/2RR2K1 w - - bm Nb5; id \"STS(v7.0) Simplification.035\"; c0 \"Nb5=10, Bd3=1, Kh1=1\";\n" +
            "2rr2k1/1p3pp1/p3b2p/3pP2Q/3B4/bqP5/1P1RB1PP/1K3R2 b - - bm Bc5; id \"STS(v7.0) Simplification.036\"; c0 \"Bc5=10\";\n" +
            "3Q4/2p2k1p/p3b1p1/2p3P1/4Pq2/1P2NP2/P5K1/8 w - - bm Nc4; id \"STS(v7.0) Simplification.037\"; c0 \"Nc4=10, Kf2=3, Nd1=2\";\n" +
            "3br1k1/r4pp1/p1qpbn1p/n1p1pN2/P1B1P3/2PPN2P/3BQPP1/R4RK1 w - - bm Bd5; id \"STS(v7.0) Simplification.038\"; c0 \"Bd5=10, Bxe6=7, Bxe6=8, Ng3=7, f3=7\";\n" +
            "3qr3/2n1rpbk/3p2np/R1pP2p1/P3P3/2N2P1P/2QB2P1/1R3B1K w - - bm Nb5; id \"STS(v7.0) Simplification.039\"; c0 \"Nb5=10, Nd1=7, Qa2=7, Ra7=6\";\n" +
            "3r1r2/1p2k1pp/p2ppn2/4n3/P3P3/1BN4P/1PP2RP1/5R1K b - - bm Nfd7; id \"STS(v7.0) Simplification.040\"; c0 \"Nfd7=10, Rb8=6, Rf7=6, g5=6\";\n" +
            "3r1rk1/4q1pp/1pb1p3/p1p1P3/P3pP2/1P2P3/4Q1PP/N2R1RK1 b - - bm Rd3; id \"STS(v7.0) Simplification.041\"; c0 \"Rd3=10, Bd5=1, Qb7=2, Rxd1=1\";\n" +
            "3r2k1/1R3ppp/8/p1p1r3/4n3/4P2P/PP2BP2/1K5R w - - bm Rd1; id \"STS(v7.0) Simplification.042\"; c0 \"Rd1=10\";\n" +
            "3r2k1/1b3qp1/pp2p1p1/4P1P1/2PN1P1P/b1NR2K1/P3Q3/8 w - - bm Nc2; id \"STS(v7.0) Simplification.043\"; c0 \"Nc2=10, Nb3=6, Ne4=3, Nf3=4\";\n" +
            "3r2k1/1p3ppp/p3nn2/Qbp4r/2N5/1B3PP1/PP3RKP/8 w - - bm Rd2; id \"STS(v7.0) Simplification.044\"; c0 \"Rd2=10\";\n" +
            "3r2k1/2rq2pp/4bp2/p1p1pn2/1n2N3/B2PP1P1/1R1Q1P1P/1R3BK1 b - - bm Nd6; id \"STS(v7.0) Simplification.045\"; c0 \"Nd6=10\";\n" +
            "3r3k/p3bppp/4q3/2p1P1Bb/3p4/P6P/1P1Q1PP1/1B2R1K1 w - - bm Ba2; id \"STS(v7.0) Simplification.046\"; c0 \"Ba2=10, Bd3=3, Bf5=4, g4=3\";\n" +
            "3r4/1R4bk/7p/p3p2p/3rP3/3p3Q/P1qN1PP1/1R4K1 b - - bm Rb4; id \"STS(v7.0) Simplification.047\"; c0 \"Rb4=10, Qc6=7, Qc8=7, Qxd2=8\";\n" +
            "3rr3/1k3p1p/p2P1np1/8/pn6/1bN1NP2/1P1RBKPP/7R w - - bm Bd1; id \"STS(v7.0) Simplification.048\"; c0 \"Bd1=10, Ra1=2, Rc1=1, g4=4\";\n" +
            "4qnk1/1p1rbppb/4p2p/1P2P2P/3N1P2/2Q2BP1/5BK1/2R5 w - - bm Qc8; id \"STS(v7.0) Simplification.049\"; c0 \"Qc8=10\";\n" +
            "4r1k1/1p1n1p1b/2n2q2/2P4p/1PBp1PpN/6P1/3Q2P1/3R1NK1 w - - bm Bd3; id \"STS(v7.0) Simplification.050\"; c0 \"Bd3=10\";\n" +
            "4r1k1/2R3p1/p4qnp/8/2P2p2/1Pn2Q2/5BPP/5N1K b - - bm Ne4; id \"STS(v7.0) Simplification.051\"; c0 \"Ne4=10, Rd8=4\";\n" +
            "4r1k1/4bp1p/p4np1/1r1p4/2NP1B1P/P7/1P3PP1/3RR1K1 w - - bm Bg5; id \"STS(v7.0) Simplification.052\"; c0 \"Bg5=10\";\n" +
            "4r1k1/p2n3p/1qp3p1/3rp3/6P1/2B1R3/PP2Q2P/4R2K b - - bm Qb5; id \"STS(v7.0) Simplification.053\"; c0 \"Qb5=10\";\n" +
            "4r1k1/p6p/2pB2pb/3pP1q1/8/1P5b/P1P2Q1P/3BR2K b - - bm Qd2; id \"STS(v7.0) Simplification.054\"; c0 \"Qd2=10, Bf5=3, Qd8=3, Qf5=7\";\n" +
            "4r1k1/pp3p2/2n1r1p1/2q4p/n3p3/4B1Q1/P1PRBPPP/3R2K1 b - - bm Qe5; id \"STS(v7.0) Simplification.055\"; c0 \"Qe5=10, Qb4=5, Qc3=6\";\n" +
            "4r3/q4pkp/2p1bnp1/p3p3/PbP1P2P/4NNP1/1RQ2P2/5BK1 b - - bm Ng4; id \"STS(v7.0) Simplification.056\"; c0 \"Ng4=10\";\n" +
            "4rb1k/3q1p2/2R4p/1P1npN2/4Q3/7P/1P3PP1/6K1 b - - bm Re6; id \"STS(v7.0) Simplification.057\"; c0 \"Re6=10, Nb4=4, f6=5, h5=3\";\n" +
            "4rk2/1b2rqb1/p4p2/1p1P1Pp1/2pRp1N1/P1N1R1PQ/1P4P1/6K1 w - - bm Nh6; id \"STS(v7.0) Simplification.058\"; c0 \"Nh6=10, Kf2=3, Qh2=3, Qh7=3\";\n" +
            "5k2/1p1n1pp1/p5bp/P2p4/1P1N4/2P2PP1/4QK1P/2q2B2 w - - bm Qe3; id \"STS(v7.0) Simplification.059\"; c0 \"Qe3=10\";\n" +
            "5k2/3q1pp1/pB6/4b2p/4N3/2N3P1/BP3P1P/6K1 b - - bm Bd4; id \"STS(v7.0) Simplification.060\"; c0 \"Bd4=10, Ke8=3, f5=4, h4=2\";\n" +
            "5k2/4rpp1/pp6/4b2p/4N1q1/2NRB1P1/BP3P1P/6K1 b - - bm Rd7; id \"STS(v7.0) Simplification.061\"; c0 \"Rd7=10, Qf5=2\";\n" +
            "5r1k/2q2rpp/2BbRn2/p1pP1P2/1p1p2PB/1P1P1Q1P/6K1/R7 w - - bm Bg3; id \"STS(v7.0) Simplification.062\"; c0 \"Bg3=10, Qf2=5, Ra2=6, Rae1=7\";\n" +
            "5r2/6pk/5qnp/4p3/2QpP3/P2R1NP1/1r3P1P/2R3K1 b - - bm Nf4; id \"STS(v7.0) Simplification.063\"; c0 \"Nf4=10, Nh4=9\";\n" +
            "5rk1/2p1q2p/1r2p2Q/3nNp2/8/6P1/1P3P1P/2RR2K1 w - - bm Rc6; id \"STS(v7.0) Simplification.064\"; c0 \"Rc6=10, Nc4=8, Rc2=9, Rd2=8\";\n" +
            "5rk1/p3qp1p/2r1p2Q/2b2p2/1n5N/2N3P1/1P2PP1P/R2R2K1 w - - bm e4; id \"STS(v7.0) Simplification.065\"; c0 \"e4=10, Kg2=4, Nf3=5, Rac1=4\";\n" +
            "5rk1/pb2qppp/1p2p3/7B/2P5/1P4QP/P4PP1/3R2K1 w - - bm Qd6; id \"STS(v7.0) Simplification.066\"; c0 \"Qd6=10, Be2=4, Qd3=4, Qe5=3\";\n" +
            "6bk/6p1/1b1q2n1/p6p/3N4/1P1Q4/P4PPP/3R2K1 w - - bm Nf3; id \"STS(v7.0) Simplification.067\"; c0 \"Nf3=10, Nc2=5, Ne2=4, Nf5=5\";\n" +
            "6k1/p4ppp/4p3/3n4/8/1Pn1PP1P/P3b1P1/R1B3K1 w - - bm Bd2; id \"STS(v7.0) Simplification.068\"; c0 \"Bd2=10, Bb2=9, Kf2=4, a4=5\";\n" +
            "6k1/pp3p1p/1q2r1p1/3p4/6n1/6P1/PPPQ1P1P/3R1BK1 w - - bm Qd4; id \"STS(v7.0) Simplification.069\"; c0 \"Qd4=10, Bh3=9, b3=5, c3=7\";\n" +
            "6rk/2R4p/Pp1pq3/1P2pp2/1P2b3/8/6PP/2Q2BK1 w - - bm Qc4; id \"STS(v7.0) Simplification.070\"; c0 \"Qc4=10, a7=5, g3=5, h4=4\";\n" +
            "7k/4r1p1/1p1b3p/p4q2/8/P5PP/1P1NQPK1/4R3 w - - bm Qf3; id \"STS(v7.0) Simplification.071\"; c0 \"Qf3=10, Ne4=7, Qd1=6, Qf1=1\";\n" +
            "7r/2q2pkp/2b1n1pN/3pP3/2pP4/4Q2P/2B2PP1/R5K1 w - - bm Bf5; id \"STS(v7.0) Simplification.072\"; c0 \"Bf5=10, Bd1=1\";\n" +
            "7r/p1P2kb1/1nB1b3/6pp/4p3/2P3BP/PP3PP1/R5K1 b - - bm Nd5; id \"STS(v7.0) Simplification.073\"; c0 \"Nd5=10, Nc4=7, e3=8, h4=7\";\n" +
            "8/p4k2/1p3r2/2pPnb2/2Pr3P/PPK3P1/4B1Q1/8 b - - bm Bg4; id \"STS(v7.0) Simplification.074\"; c0 \"Bg4=10, Bd3=7, Be4=7, Ng4=4\";\n" +
            "b5k1/q4p1p/3p1bp1/4p3/1NB1P3/3P3P/1Q3PP1/6K1 w - - bm Bd5; id \"STS(v7.0) Simplification.075\"; c0 \"Bd5=10, g3=4\";\n" +
            "b7/3r2bk/p2Np1p1/2B1p1Pp/qP2P2P/P2Q1RK1/8/8 w - - bm Rf7; id \"STS(v7.0) Simplification.076\"; c0 \"Rf7=10, Kh2=7, Qb1=7, Qe2=7\";\n" +
            "q1rr2k1/pbnn1pbp/1p4p1/4p1P1/2P1P3/1NN2B2/PP2QB1P/3RR1K1 w - - bm Bg4; id \"STS(v7.0) Simplification.077\"; c0 \"Bg4=10, Be3=6, Nc1=3, Nd5=4\";\n" +
            "r1b2kr1/pp1nbp2/4pn1p/2q3p1/B1P5/P1B2N1P/1P2QPP1/3R1KNR w - - bm Ne5; id \"STS(v7.0) Simplification.078\"; c0 \"Ne5=10, Nd2=6, Nh2=7, b4=5\";\n" +
            "r1b2rk1/1p2Bpbp/1np3p1/4P3/pn6/1N3N1P/1P1RBPP1/R5K1 b - - bm Nc2; id \"STS(v7.0) Simplification.079\"; c0 \"Nc2=10, N4d5=1\";\n" +
            "r1b2rk1/p1qn1ppp/2pb1n2/8/Np1NP3/7P/PPQ1BPP1/R1B2RK1 w - - bm Nf5; id \"STS(v7.0) Simplification.080\"; c0 \"Nf5=10, Nxc6=6, Rd1=7, f4=7\";\n" +
            "r1b2rk1/pp1p1ppp/4pn2/6q1/2PP4/P1N5/1PQ2PPP/R3KB1R w KQ - bm Qd2; id \"STS(v7.0) Simplification.081\"; c0 \"Qd2=10, Qc1=3, g3=6, h4=6\";\n" +
            "r1bR4/1p3pkp/1np3p1/4P3/pB6/1n3N1P/1P2BPP1/6K1 b - - bm Be6; id \"STS(v7.0) Simplification.082\"; c0 \"Be6=10, Bd7=6, Bf5=7, c5=8\";\n" +
            "r1bq1rk1/pp2ppbp/1n1n2p1/2p5/5B2/1PN2NP1/P1Q1PPBP/3R1RK1 w - - bm Nb5; id \"STS(v7.0) Simplification.083\"; c0 \"Nb5=10, Bxd6=2, Ng5=9, e4=4\";\n" +
            "r1bq2k1/ppbnr1p1/2p1ppBp/2Pp4/P2P3N/2Q5/1P1B1PPP/4RRK1 b - - bm Nf8; id \"STS(v7.0) Simplification.084\"; c0 \"Nf8=10, Rb8=4, a5=9, a6=3\";\n" +
            "r1q2r1k/1bBnbpp1/4p2p/pN1p3P/8/5BQ1/PPP2PP1/1K1R3R b - - bm Bc6; id \"STS(v7.0) Simplification.085\"; c0 \"Bc6=10\";\n" +
            "r2q1r1k/ppp3bp/3pb3/6p1/2Pn4/1PN3BP/P2QBPP1/3RR1K1 w - - bm Bg4; id \"STS(v7.0) Simplification.086\"; c0 \"Bg4=10, Bh5=6, Nb5=7, Nd5=6\";\n" +
            "r2q1rk1/1p3pp1/6p1/3Bn3/Ppp1P1P1/1n3P2/1P5P/1RBQ1RK1 w - - bm Bf4; id \"STS(v7.0) Simplification.087\"; c0 \"Bf4=10, Be3=8, Kh1=8, f4=7\";\n" +
            "r2r2k1/1q3pbp/p3b3/2pPN3/2p1RB2/7P/PP3PP1/R2Q2K1 w - - bm Nc6; id \"STS(v7.0) Simplification.088\"; c0 \"Nc6=10, Qe2=6, Qh5=3, Re2=3\";\n" +
            "r2r2k1/p2bppbp/1p4p1/2pP4/2q1P3/2P1B3/P1QN1PPP/2R2RK1 b - - bm Qa4; id \"STS(v7.0) Simplification.089\"; c0 \"Qa4=10, Qa6=2, Qb5=2, Qe2=6\";\n" +
            "r2r4/5kpp/p2q1n2/1p4B1/3b4/2Np1Q2/PP3PPP/3R1RK1 w - - bm Ne4; id \"STS(v7.0) Simplification.090\"; c0 \"Ne4=10, Bf4=1, Bxf6=2\";\n" +
            "r3k1r1/2q1bp2/2p1p1np/p1Pp2P1/Pp1Pn2P/1P2P3/1B2NNP1/R3QRK1 b q - bm Ng3; id \"STS(v7.0) Simplification.091\"; c0 \"Ng3=10, Nf6=1, Nxf2=3\";\n" +
            "r3kb1r/3n1ppp/p3p3/3bP3/Pp6/4B3/1P1NNPPP/R4RK1 w kq - bm Nf4; id \"STS(v7.0) Simplification.092\"; c0 \"Nf4=10, Bd4=8, Rac1=8, Rfc1=8\";\n" +
            "r3nbk1/1b3ppp/pr6/1pp1PBq1/P1P5/7P/1B1NQPP1/R3R1K1 w - - bm Be4; id \"STS(v7.0) Simplification.093\"; c0 \"Be4=10, Bg4=7, Qg4=4\";\n" +
            "r3r1k1/2q2np1/3p1pNp/p2P4/R4PP1/2P3K1/1P1Q4/7R w - - bm Re1; id \"STS(v7.0) Simplification.094\"; c0 \"Re1=10, Raa1=6\";\n" +
            "r3r1k1/5pp1/p1n2q1p/P1bp1P2/2p2P2/5N2/2BB3P/R2QRK2 b - - bm Nd4; id \"STS(v7.0) Simplification.095\"; c0 \"Nd4=10, Rad8=3, c3=6, d4=4\";\n" +
            "r4bk1/1p1nqp1p/p1p1b1p1/P3p3/2P1P3/1QN2PP1/1P5P/R4BBK b - - bm Qb4; id \"STS(v7.0) Simplification.096\"; c0 \"Qb4=10, Nc5=4, Qd6=1, Rb8=1\";\n" +
            "r4rk1/2pqbppp/p1n5/1p1pP3/3P4/1B2Bb1P/PP1Q1PP1/R1R3K1 w - - bm Qc3; id \"STS(v7.0) Simplification.097\"; c0 \"Qc3=10, Qc2=6, Rxc6=7, gxf3=1\";\n" +
            "r7/4pp1k/6pb/p1rPp2p/2P1P2q/7P/1RQ1BPP1/1R4K1 w - - bm Rb5; id \"STS(v7.0) Simplification.098\"; c0 \"Rb5=10, Bf1=6, Rb3=5, Rb8=6\";\n" +
            "rn3rk1/pp1q2bp/3ppnp1/2p3N1/2P1P3/8/PP2NPPP/R1BQ1RK1 w - - bm e5; id \"STS(v7.0) Simplification.099\"; c0 \"e5=10, Qd3=1\";\n" +
            "rq4k1/1b1nbpp1/4p2p/r1PnP3/Np6/1P1B1N2/1Q1B1PPP/R2R2K1 b - - bm Bc6; id \"STS(v7.0) Simplification.100\"; c0 \"Bc6=10, Bf8=4, Nxc5=6, Qa7=6\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    
