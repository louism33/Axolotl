package strategictestsuite;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
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
public class STS9abcPAWNS {


    private static final int timeLimit = 10_000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        EngineSpecifications.DEBUG = true;

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

    public STS9abcPAWNS(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getFullString());
        System.out.println(EPDObject.getBoard());
        int[] winningMoves = EPDObject.getBestMovesFromComments();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.DEBUG = false;
        int move = EngineBetter.searchFixedTime(EPDObject.getBoard(), timeLimit);

        System.out.println("my move: " + MoveParser.toString(move));
        
        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }

    private static final String positions = "" +
            "1b2r1k1/1bqn1pp1/p1p4p/Pp2p3/1P2B3/2B1PN1P/5PP1/1Q1R2K1 b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.001\"; c0 \"c5=10, Kf8=1, Nf8=2, g5=2\";\n" +
            "1k4nr/pppr1q2/3p2p1/3Nn1Qp/2P1PN2/1P6/P1P3PR/2KR4 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.002\"; c0 \"c5=10, Nd3=5, Rf1=2, Rhh1=3\";\n" +
            "1n4k1/2pb2q1/1p1p3p/3P1p2/1PP1pP2/3rN3/4N1PP/1RQ3K1 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.003\"; c0 \"b5=10\";\n" +
            "1nr5/p2p1qpk/1pb1p2p/5p1P/1PP2N2/P3PPQ1/6P1/3RKB2 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.004\"; c0 \"b5=10, Kf2=5, Ng6=6, a4=5\";\n" +
            "1q2r1k1/1b2bpp1/p2ppn1p/2p5/P3PP1B/2PB1RP1/2P1Q2P/2KR4 b - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.005\"; c0 \"c4=10, Bc6=5, Qa7=4, Qa8=5, Qc8=5, d5=5\";\n" +
            "1q2rb2/3b1r1k/p1p4p/B3p1p1/1PPpN3/3P1P1P/3QR1P1/4R1K1 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.006\"; c0 \"c5=10, Kh1=7, Qb2=6, Ra1=7\";\n" +
            "1r1qr1k1/1p3pp1/p1n1bn1p/2NN4/4P3/6P1/PP3PB1/2RQR1K1 w - - bm b3; id \"STS(v9.0) Advancement of a/b/c pawns.007\"; c0 \"b3=10, Qd2=5, Rf1=7, a4=5\";\n" +
            "1r1r1bk1/1bq2ppp/pnp1p3/4P3/5P2/P1NBB3/1P4PP/R1QR2K1 b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.008\"; c0 \"c5=10, Nd5=5, Rbc8=5, h6=2\";\n" +
            "1r1r2k1/p1p1p3/4n1p1/5p2/8/R3B3/Pn2BPPP/4K2R b K - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.009\"; c0 \"c5=10, Kf7=5, Nd4=5, Rb4=3\";\n" +
            "1r1r2k1/pb3ppp/1p1ppn2/8/PPPP1P2/2NB2P1/3K3P/RR6 w - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.010\"; c0 \"a5=10, Ke2=6, Nb5=8, Ra3=6\";\n" +
            "1r1r3k/p4pp1/3nb2p/2p1q3/8/P1PBPP2/2Q3PP/R1B2RK1 b - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.011\"; c0 \"c4=10, Bb3=3, Nc8=4, Rb3=4\";\n" +
            "1r2r1k1/2qp1ppp/2p1pn2/8/8/2PBQ1P1/PP3P1P/R4RK1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.012\"; c0 \"b4=10, Qd2=2, b3=4\";\n" +
            "1r2r1k1/2qp1ppp/2p2n2/4p3/1P6/2PBQ1P1/P4P1P/R4RK1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.013\"; c0 \"a4=10, Bc2=1\";\n" +
            "1r2rn2/1p3pk1/p4npp/P2p4/1P1N2P1/1P3P1P/4BK2/2R1R3 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.014\"; c0 \"b5=10, Bf1=6, Rc2=6, Rc7=7\";\n" +
            "1r3rk1/2qnbppp/2p1p3/pbPpP3/8/PP2QNP1/3B1PBP/2R1R1K1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.015\"; c0 \"b4=10, Bh1=4, Qd4=3, h4=2\";\n" +
            "1r4k1/p3pr1p/1qbp1pp1/2p5/2P5/1PN1Q2P/P2R1PP1/3R3K b - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.016\"; c0 \"a5=10, Kg7=7, Qb7=8, a6=6\";\n" +
            "1rbr2k1/p1q2p1p/2n1p1p1/1p4Q1/2pPP3/2P3P1/P1B1N1PP/2R2RK1 b - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.017\"; c0 \"b4=10, Qe7=8, Rb6=6, a5=7\";\n" +
            "1rq2r2/Q3pp1k/1B1p2pb/4n2p/2PN4/PP5P/3nBPP1/R2R2K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.018\"; c0 \"a4=10, Ra2=2, c5=2\";\n" +
            "1rr3k1/p2qbpnp/Ppp3p1/3pP3/3P4/1Q1B1P2/1P1B1P1P/2R3RK b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.019\"; c0 \"c5=10, Ne6=6, Nf5=4, Qe6=5\";\n" +
            "1rrqb3/1p3kp1/p3p2p/P1Rp1p1P/3P1P2/6P1/1P1QBPK1/2R5 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.020\"; c0 \"b4=10, Bd3=5, Qe1=5, b3=5\";\n" +
            "2r1n1k1/1p1R1r1p/2q1pp1B/p5p1/6P1/Q1P4P/PP6/1K1R4 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.021\"; c0 \"b5=10, b6=7\";\n" +
            "2r1r1k1/1b1n1pp1/1q2p2p/3p3n/P2N4/BP1NPP1P/2B3P1/2R1Q1K1 w - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.022\"; c0 \"a5=10\";\n" +
            "2r1r1k1/1b1q1pp1/4p2p/1Nnp3n/1N6/PP2PP1P/1BB2QP1/3R2K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.023\"; c0 \"a4=10, Na7=1\";\n" +
            "2r1r1k1/1p1npp1p/p2pb1p1/q7/2PnP3/1PNN2PP/P2Q1PB1/2RR2K1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.024\"; c0 \"b4=10, Nf4=5, Qb2=6, Qe3=3\";\n" +
            "2r2bk1/R4pp1/3pqnnp/R3p3/1P2P3/3Q1NNP/2rB1PPK/8 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.025\"; c0 \"b5=10, R5a6=3, R7a6=3, Ra8=1\";\n" +
            "2r2r1k/1bqnbppp/p3p3/1p1pP3/3B1P2/P1NB4/1PP2QPP/R4R1K w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.026\"; c0 \"b4=10, Qe3=4, Rad1=4, Rae1=5\";\n" +
            "2r2rk1/1p2bpp1/p3p3/4P1N1/PqB3P1/1Pn2NQ1/2b2P1P/R3R1K1 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.027\"; c0 \"b5=10, Bg6=4, Rfd8=3, Rxc4=4\";\n" +
            "2r2rk1/pp3ppp/2nb1q2/P3pb2/2P5/1NB3P1/1Q2PPBP/R4RK1 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.028\"; c0 \"c5=10, Nd2=3, Rfd1=4, f4=4\";\n" +
            "2r3k1/1pq1b1p1/p1b1pr1p/8/PP2pP2/2P1B3/4B1PP/RQR4K w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.029\"; c0 \"b5=10, Rd1=3, Rf1=3, g3=5\";\n" +
            "2r3k1/5p2/pr3p1p/8/3N2q1/P3P1P1/1P1Q1PP1/5RK1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.030\"; c0 \"b4=10\";\n" +
            "2r5/1r2nkp1/p3p2p/1p1b1p1P/8/PP3P2/1KPRB1P1/R5B1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.031\"; c0 \"a4=10, Bd4=7, Bh2=8, g4=7\";\n" +
            "2r5/3npk1p/1p1p1r2/pP1P2p1/3N1pP1/1P3P2/2P1R2P/R5K1 w - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.032\"; c0 \"c4=10, Kf2=6, Kg2=5, Nc6=3\";\n" +
            "2rqr1k1/5ppp/n4b2/pbP5/3p4/P4NP1/1N2PPBP/2RQR1K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.033\"; c0 \"a4=10, Na4=2, Nd3=4, c6=2\";\n" +
            "2rr2k1/1b3ppp/pp2pn2/2n5/2BNP3/4KPNP/PP4P1/2R4R w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.034\"; c0 \"b4=10\";\n" +
            "2rr2k1/1q3pp1/pp1pp2p/4n3/2PNP3/1P4P1/P1R1QPKP/3R4 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.035\"; c0 \"b5=10, Nd7=7, Qc7=7, Rc5=7\";\n" +
            "3b2rk/5pp1/2q2n1p/1p2BP2/7P/2NQ4/1PP5/1K4R1 b - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.036\"; c0 \"b4=10, Bb6=1, Nd7=4, Re8=1\";\n" +
            "3q2k1/1p1bprbp/3pn1p1/3N4/1PP5/r3B3/3QBPPP/2R1R1K1 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.037\"; c0 \"c5=10, Bd1=4, Bg4=4, Rb1=5\";\n" +
            "3r1nk1/pp2q2p/2p3p1/2NnPp2/5P2/1Q6/PP1rNRPP/2R3K1 b - - bm b6; id \"STS(v9.0) Advancement of a/b/c pawns.038\"; c0 \"b6=10, Nd7=3, Rb8=2, b5=1\";\n" +
            "3r1rk1/2pq2pp/p4pb1/1p6/4P1P1/2PnQN1P/PP1N1P2/RR4K1 b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.039\"; c0 \"c5=10, Qd6=3, Rfe8=1\";\n" +
            "3r1rk1/3n1pbp/1p1p2p1/pN1Ppn1q/2P3b1/PPN3P1/1B1Q1P1P/R3R1KB w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.040\"; c0 \"b4=10, Na4=5, Ne4=6, Qc2=7\";\n" +
            "3r2k1/1p2bpp1/pNrppnqp/8/1PPBP3/P4Q1P/5PP1/3RR1K1 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.041\"; c0 \"b5=10, Qd3=3, Rd3=2, a4=8\";\n" +
            "3r2k1/5p2/p1prb2p/1p2q3/4Pp2/1P1B1P2/P3Q1PP/2RR1K2 b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.042\"; c0 \"c5=10, Kh7=6, Qh5=4, R8d7=5\";\n" +
            "3rn1k1/1pqn1pp1/p3br1p/2P1p3/P3P3/3BBN1P/2P3PQ/RR5K w - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.043\"; c0 \"a5=10, Ra3=3, Rb4=6\";\n" +
            "3rr1k1/1bqn1ppp/p1p5/1p2b3/4P3/P3BB1P/1PQ1NPP1/1R1R3K b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.044\"; c0 \"c5=10, h6=3\";\n" +
            "3rr1k1/1p5p/p2B1bp1/5b2/1RP2P2/1PN5/1P1R1K1P/8 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.045\"; c0 \"c5=10, Kf3=6, Kg2=6, Rd5=1\";\n" +
            "3rr1k1/1q2bp1p/2b3pB/3npP2/1p6/5B2/NPP3PP/3RQR1K w - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.046\"; c0 \"c4=10, Qe2=5, c3=3, fxg6=4\";\n" +
            "4b3/ppr1bpk1/2n1p1p1/1B1rP2p/3P3P/P1R1BNP1/5PK1/2R5 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.047\"; c0 \"a4=10, Rb1=5\";\n" +
            "4q1k1/p5p1/1rp1p1p1/2R1Pp2/1PQ5/P5P1/5PK1/8 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.048\"; c0 \"a4=10, Qc3=3\";\n" +
            "4r1k1/3bpr1p/1qpp1ppQ/p7/2P1N3/1P5P/P2R1PP1/3R3K b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.049\"; c0 \"c5=10, Bf5=6, Qa7=7, Qc7=5\";\n" +
            "4r1k1/p2bpr1p/1qpp2pQ/5p2/2P4P/1P4N1/P2R1PP1/3R3K b - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.050\"; c0 \"a5=10, Qc5=5, Ref8=7, Rg7=7\";\n" +
            "4rk2/2p1rpp1/pbn3bp/1p2P3/PP6/1BP2NNP/5PP1/2R1R1K1 w - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.051\"; c0 \"a5=10, Nh4=3, axb5=5, e6=5\";\n" +
            "4rrk1/1q4pp/4p3/2bp4/5P2/3Q4/1PP3PP/R1B2R1K w - - bm c3; id \"STS(v9.0) Advancement of a/b/c pawns.052\"; c0 \"c3=10, Ra4=6, Ra5=6, b3=8\";\n" +
            "4rrk1/p2n2pp/qp2pb2/2p5/2P2P2/P1BP1Q2/4N1PP/1R3RK1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.053\"; c0 \"a4=10, Bxf6=6, Qe4=2, h3=6\";\n" +
            "5nk1/1bq2pp1/rp1r1b1p/1R1p1B2/2pP4/2N1PN2/PQ3PPP/1R4K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.054\"; c0 \"a4=10, Ne5=5, Rb4=6, h4=2\";\n" +
            "5r1k/1p2q1pp/p1p2n2/5r2/3Pp3/4P2R/PPB2PQP/6RK b - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.055\"; c0 \"c5=10\";\n" +
            "5r2/5r1k/p2pN1p1/1p1Pn2p/2q1P2b/P4pR1/1PPQ1B1P/1K2R3 w - - bm b3; id \"STS(v9.0) Advancement of a/b/c pawns.056\"; c0 \"b3=10, Nxf8+=2\";\n" +
            "5rk1/1prn1qp1/p2p3p/3Ppp1P/7R/1NPQ1PP1/PP6/1K1R4 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.057\"; c0 \"b5=10, Nc5=5, Re8=6, Rfc8=3\";\n" +
            "5rk1/2nq2pp/3p1p2/3P4/3B1Q2/PrN3P1/4PP1P/R5K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.058\"; c0 \"a4=10, Qe4=4, e3=2, h4=4\";\n" +
            "5rk1/p2qp2p/1p1p1pp1/2nN1P1r/P1P1P3/1P2R1P1/1Q4KP/4R3 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.059\"; c0 \"b4=10, Qd2=6, Ra1=5, h4=5\";\n" +
            "6k1/1b2bp1p/p3p3/1p2P1p1/2r5/2N1BP2/PP4PP/3R2K1 b - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.060\"; c0 \"b4=10, Bc6=4, Kf8=4\";\n" +
            "6k1/1p1n1rbp/r2n1pp1/4p3/4P3/4B1P1/PP2N1BP/R4RK1 w - - bm b3; id \"STS(v9.0) Advancement of a/b/c pawns.061\"; c0 \"b3=10, Bf2=5, Rad1=2, Rfc1=6\";\n" +
            "6k1/1pr2rp1/p4q1p/2p1p3/2Q1P3/P6P/2P2PP1/3R1RK1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.062\"; c0 \"a4=10, Qc3=5, Rd3=5, Rd5=5\";\n" +
            "6k1/2rqbrpp/2n2n2/pBpppN2/Pp4P1/2PP3P/1P2QP2/R1B1R1K1 w - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.063\"; c0 \"c4=10, Bg5=5, Bxc6=4, cxb4=4\";\n" +
            "6k1/4brpp/3p4/p1q1p3/1pN1Pr2/1P1R1P2/P1P3QP/1K4R1 b - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.064\"; c0 \"a4=10, Bf8=5, Kh8=7, R4f6=3\";\n" +
            "6k1/5r2/p4pp1/Br1pp3/2b5/6NP/1P3RP1/2R4K w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.065\"; c0 \"b4=10, Bc3=3, Bd8=5, Be1=6\";\n" +
            "8/5p2/bk2p3/1pr1Pp2/3p4/bP3BPP/P1nBN3/1R1K4 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.066\"; c0 \"b4=10, Bg5=7, Nf4=7, h4=7\";\n" +
            "8/r2b2pk/2p1pr1p/3nQp2/p7/qP3N2/P1PRBPPP/4R1K1 w - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.067\"; c0 \"c4=10, Bd3=5, Red1=5, h3=5\";\n" +
            "b2qr2k/2p3pp/8/1p3p2/2p2B2/2Pp1PPP/5QB1/R6K b - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.068\"; c0 \"b4=10, Bc6=2, Bd5=2, h6=2\";\n" +
            "r1b2k2/ppq2p1p/1np3p1/4p3/2n5/3N2P1/P1Q1PPBP/R2R2K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.069\"; c0 \"a4=10, Qb3=1, Qc3=3\";\n" +
            "r1bq1r2/1pp3bk/3p1npp/1N1Ppn2/PBP1N3/1Q1B4/5PPP/R4RK1 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.070\"; c0 \"c5=10, Nxf6+=6, Rae1=6, Rfe1=5, a5=6\";\n" +
            "r1r3k1/3nqpp1/1pp1pn1p/p2pN3/2PP1Q2/1P4P1/P2BPPKP/2RR4 b - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.071\"; c0 \"a4=10, Nxe5=6, Qe8=6, Ra7=7\";\n" +
            "r1r3k1/p3pp1p/3p2pb/R1nP4/4PP2/2Pp4/1P1N2PP/2BR2K1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.072\"; c0 \"b4=10, Nc4=5, Rf1=2, g3=6\";\n" +
            "r1r4k/1q1nbpp1/2bp1n1p/pp2pP2/4P3/P1N2BQ1/1PPN1BPP/2R1R1K1 b - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.073\"; c0 \"b4=10, Nb6=7, Rc7=7, Rd8=7\";\n" +
            "r2q1nk1/1p2rppp/p1p5/3p3b/PP1P4/2NBP3/2Q2PPP/2R2RK1 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.074\"; c0 \"b5=10, Ne2=7, Qb3=6, h3=6\";\n" +
            "r2rn1k1/3nqppp/1pp1p3/3p4/pPPP4/P2QN1P1/1B2PP1P/2RR2K1 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.075\"; c0 \"b5=10, Re1=2, cxd5=3, f3=2\";\n" +
            "r3kr2/pp1b1pp1/2p1p2p/4q3/7Q/2PBPR2/PP4PP/2KR4 b q - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.076\"; c0 \"c5=10, Qg5=4, a5=2, g5=1\";\n" +
            "r3r1k1/1p3pp1/1qp1b2p/p1Rn4/3p4/P2P1BP1/1P1BPP1P/2Q1R1K1 b - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.077\"; c0 \"a4=10, Kh7=1, Nf6=1, Red8=1\";\n" +
            "r3r1k1/1pb1qpp1/2p1b2p/p3P3/4Q3/2B1PN1P/PP3PP1/3RR1K1 w - - bm a3; id \"STS(v9.0) Advancement of a/b/c pawns.078\"; c0 \"a3=10, Qb1=5, a4=8, b3=4\";\n" +
            "r3r1k1/pn1qnppp/1p2p3/3pP3/b1pP4/B1P2N2/R1PQBPPP/R5K1 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.079\"; c0 \"b5=10, Bc6=6, Nc6=2, Nc8=2\";\n" +
            "r3rbk1/1npq1pp1/p2p3p/1p6/1P1P4/P1R1BN1P/5PP1/3QR1K1 b - - bm c6; id \"STS(v9.0) Advancement of a/b/c pawns.080\"; c0 \"c6=10, Nd8=5, Rac8=6\";\n" +
            "r3rbk1/1qpn1pp1/p4n1p/1p2pN2/2P1P3/5N2/PPQB1PPP/R3R1K1 b - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.081\"; c0 \"b4=10, Nc5=5, Re6=2, bxc4=4\";\n" +
            "r3rn1k/1q2b1p1/pp1p1n1p/4pQ2/P3P3/1NN1BR2/1PP3PP/5R1K b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.082\"; c0 \"b5=10, N8h7=4, Rab8=5, Rac8=5\";\n" +
            "r4rk1/1p4bp/2qp1np1/p1n1p3/2P5/P1B1N3/1PB2PPP/R2Q1RK1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.083\"; c0 \"b4=10, Qe1=5, Qe2=6, f3=3\";\n" +
            "r4rk1/2Q2ppp/4p3/p3P3/q1P5/P5P1/5P1P/2R1R1K1 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.084\"; c0 \"c5=10, Qb7=6, Rc3=5, Re3=6\";\n" +
            "r4rk1/2qb1ppp/p2pp3/1pn3P1/3RPP2/P1N2Q2/1PP3BP/2KR4 b - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.085\"; c0 \"a5=10, Bc6=7, Rac8=7, Rfc8=6\";\n" +
            "r4rk1/4bppp/b3pn2/1p1q2B1/3p4/3B1N2/PP2QPPP/R2R2K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.086\"; c0 \"a4=10, Bf4=4, Bxf6=1, a3=2\";\n" +
            "r4rkb/p2bp2p/2nq2pP/1p6/2pP4/2P1B1N1/P1B2PP1/2RQR1K1 w - - bm a4; id \"STS(v9.0) Advancement of a/b/c pawns.087\"; c0 \"a4=10, Be4=6, Qe2=5, Rb1=6\";\n" +
            "r5k1/1pp1r1pp/3pbpq1/pB2n3/P1P5/1P2BP2/2PQ2PP/2KRR3 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.088\"; c0 \"c5=10, Bf4=3, Bg1=5, f4=4\";\n" +
            "r5k1/1rpn1pbp/p2pp3/1p4Np/2PP4/1PN1P1P1/P4PK1/2RR4 w - - bm c5; id \"STS(v9.0) Advancement of a/b/c pawns.089\"; c0 \"c5=10, Nb1=3, Ne2=1, d5=3\";\n" +
            "r5k1/2p1qp2/1p1p2bp/p1nPr1p1/2P5/PP2P1N1/1Q1B2PP/3R1RK1 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.090\"; c0 \"b5=10, Rb8=3, Re8=6, Rf8=3\";\n" +
            "r5k1/5q1p/2ppNr1b/2pPp1pP/p1P3P1/P7/1PQ1RPK1/1R6 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.091\"; c0 \"b4=10, Rd1=2, Rd2=2, b3=4\";\n" +
            "rr2q1k1/3bnpb1/ppp3pp/3p4/1P1P4/PQ1BP1NP/1NR2PP1/3R2K1 b - - bm a5; id \"STS(v9.0) Advancement of a/b/c pawns.092\"; c0 \"a5=10, Bf8=3, Ra7=4, h5=2\";\n" +
            "1r1rb1k1/pp3pbp/4p1p1/1P2P3/2PBQ3/q7/P3B1PP/2R2R1K b - - bm a6; id \"STS(v9.0) Advancement of a/b/c pawns.093\"; c0 \"a6=10, b6=3, h5=2, Rd7=4\";\n" +
            "2r1k2r/1n2ppb1/p2p2p1/1p1P3p/8/1P2BP1P/P1P1BP2/2KR2R1 w k - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.094\"; c0 \"b4=10, Bd4=2, Kb1=2\";\n" +
            "2r2rk1/1pqn3p/p2p1npb/N2Pp3/2P2p2/Q4PP1/PP2BBKP/3RR3 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.095\"; c0 \"b4=10\";\n" +
            "2r3k1/p1q1bpp1/2p2n2/2Np3r/1P1B3p/P1Q4P/5PP1/2R1R1K1 w - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.096\"; c0 \"b5=10, Qb2=3, Qd3=2, Qe3=3\";\n" +
            "3r2k1/4rppp/2ppbn2/Nq2p3/4P3/1P2Q1PP/P1PR1PBK/4R3 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.097\"; c0 \"b4=10, Nc4=1, Nxc6=4, Qc3=4\";\n" +
            "r6k/p3Npb1/qp4pp/4p3/1B6/1PP3P1/P2R1P1P/3R2K1 b - - bm b5; id \"STS(v9.0) Advancement of a/b/c pawns.098\"; c0 \"b5=10, Kh7=6, Qb7=7, Re8=7\";\n" +
            "r6r/4p1k1/3q1p2/p1pPn1p1/1p2P2p/4Q3/PP2BPPP/1R2R1K1 b - - bm c4; id \"STS(v9.0) Advancement of a/b/c pawns.099\"; c0 \"c4=10, Rac8=5, Rhc8=2\";\n" +
            "rq4kb/5p1p/3p1Pp1/p2Pp1P1/3pP1bP/1P6/P1Q2R2/4BBK1 w - - bm b4; id \"STS(v9.0) Advancement of a/b/c pawns.100\"; c0 \"b4=10, Qc1=3, Qc4=3, Qc6=4\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    