package strategictestsuite;

import com.github.louism33.axolotl.search.Engine;
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
import static strategictestsuite.MasterParamTester.*;


@RunWith(Parameterized.class)
public class STS6Recapturing {

    private Engine engine = new Engine();
    private static int successes = 0;

    @AfterClass
    public static void finalSuccessTally() {
        System.out.println("STS6Recapturing: Successes: " + successes + " out of " + splitUpPositions.length);
        System.out.println();
    }

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        ResettingUtils.reset(); 
List<Object[]> answers = new ArrayList<>();

        

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

    public STS6Recapturing(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        if (printFen) {
            System.out.println(EPDObject.getFullString());
        }
        if (printBoard) {
            System.out.println(EPDObject.getBoard());
        }
        int[] winningMoves = EPDObject.getBestMovesFromComments();
        int[] losingMoves = EPDObject.getAvoidMoves();
        
        SearchSpecs.basicTimeSearch(timeLimit);

        final int move = engine.simpleSearch(EPDObject.getBoard());

                if (printMyMove) {
            System.out.println("my move: " + MoveParser.toString(move));
        }

        final boolean condition = contains(winningMoves, move) && !contains(losingMoves, move);
        if (condition) {
            successes++;
        }
                if (enableAssert) {
            Assert.assertTrue(condition);
        }
    }

    private static final String positions = "" +
            "1k1r1r2/p1p5/Bpnbb3/3p2pp/3P4/P1N1NPP1/1PP4P/2KR1R2 w - - bm Ncxd5; id \"STS(v6.0) Recapturing.001\"; c0 \"Ncxd5=10, Nb5=4, Ne2=3, Nexd5=6\";\n" +
            "1k1r4/4rp2/1p1qbnp1/p2p3p/P2P4/1Nn2P2/1P1QBRPP/2R3K1 w - - bm Rxc3; id \"STS(v6.0) Recapturing.002\"; c0 \"Rxc3=10, Qxc3=5, Re1=8, bxc3=7\";\n" +
            "1k1rr3/pp2qpp1/1b2p3/4N2p/2R1n2P/P2R2P1/1PP1QP2/1K6 w - - bm Rxe4; id \"STS(v6.0) Recapturing.003\"; c0 \"Rxe4=10, Ng6=7, Rxd8+=3\";\n" +
            "1k3rn1/3r1p2/p2p1np1/Pp1P3p/1Pp1PP2/2P1R1bP/2B2K1B/6R1 w - - bm Bxg3; id \"STS(v6.0) Recapturing.004\"; c0 \"Bxg3=10, Kxg3=5, Rexg3=5, Rgxg3=3\";\n" +
            "1q2rnk1/5rb1/bp1p1np1/pNpP2Bp/P1P1Pp1P/3B2P1/3QNRR1/7K w - - bm gxf4; id \"STS(v6.0) Recapturing.005\"; c0 \"gxf4=10, Bxf4=8, Bxf6=7, Rxf4=7\";\n" +
            "1q4k1/1r3pp1/3p1n1p/2pPpP2/b1PnP3/rPB1R1NP/3Q2PK/1R3B2 b - - bm Bxb3; id \"STS(v6.0) Recapturing.006\"; c0 \"Bxb3=10, Raxb3=2, Rbxb3=3\";\n" +
            "1qr2rk1/4ppbp/6p1/pp1b4/2NP4/4B3/PPB2PPP/2RQR2K b - - bm bxc4; id \"STS(v6.0) Recapturing.007\"; c0 \"bxc4=10, Bxc4=3, Rfd8=6, Rxc4=5\";\n" +
            "1qrr3k/6p1/1p1pp2p/pNn5/Pn1bP1PP/5Q2/1PP1N3/1K1R2R1 w - - bm Nexd4; id \"STS(v6.0) Recapturing.008\"; c0 \"Nexd4=10, Nbxd4=4, Rg2=5\";\n" +
            "1r1q2k1/5pp1/2bp2rp/p1pNp2n/2PBP3/1P1B1P2/P2Q2PP/1R3RK1 b - - bm cxd4; id \"STS(v6.0) Recapturing.009\"; c0 \"cxd4=10, Bxd5=8, Ng3=8, exd4=7\";\n" +
            "1r1r2k1/5pb1/6p1/p1pb2Pp/PpB1PR2/7P/1PP5/2KR4 w - - bm exd5; id \"STS(v6.0) Recapturing.010\"; c0 \"exd5=10, Ba6=6, Bxd5=2\";\n" +
            "1r2r1k1/pb1n1pp1/1p1p2np/2P1p1q1/P1P1P3/2P2PP1/2QNBB1P/1R1R3K b - - bm Nxc5; id \"STS(v6.0) Recapturing.011\"; c0 \"Nxc5=10, Bc6=8, bxc5=7, dxc5=7\";\n" +
            "1r2r2k/1p3pbp/b5p1/p2np3/P1P1B1PP/1PN2P2/6K1/3R1R2 w - - bm Nxd5; id \"STS(v6.0) Recapturing.012\"; c0 \"Nxd5=10, Bxd5=4, Rxd5=5, cxd5=4\";\n" +
            "1r2rbk1/1b1q1p2/3pn1p1/1p5p/1p2P2P/P1P1BPP1/4QNBK/2R1R3 w - - bm axb4; id \"STS(v6.0) Recapturing.013\"; c0 \"axb4=10, Qa2=1, Qb2=2, cxb4=6\";\n" +
            "1r3nk1/3r1qb1/bp1p1np1/pNpP2Bp/P1P1Pp1P/7N/1P1QBR2/6RK w - - bm Rxf4; id \"STS(v6.0) Recapturing.014\"; c0 \"Rxf4=10, Bf3=1, Nc3=1, Qxf4=2\";\n" +
            "1r3r2/1b4bk/p1n2ppp/qp1p4/4PN2/1B2B1P1/P3QP2/1R1R2K1 w - - bm exd5; id \"STS(v6.0) Recapturing.015\"; c0 \"exd5=10, Bc5=3, Ne6=6, Nxg6=3\";\n" +
            "1r3rk1/p1qnppbp/p5p1/2pPP3/2P5/4R2P/PP2Q1P1/1RBN3K b - - bm Bxe5; id \"STS(v6.0) Recapturing.016\"; c0 \"Bxe5=10, Nxe5=8, Rb7=8, Rbe8=8\";\n" +
            "1r4k1/1rq2ppp/3p1n2/2pPp3/p1PnP3/PPB4P/3Q1PP1/1R2RBK1 b - - bm axb3; id \"STS(v6.0) Recapturing.017\"; c0 \"axb3=10, Nd7=4, Nxb3=3, Rxb3=8\";\n" +
            "1r4k1/pr3ppp/q2p1n2/2pPp3/b1PnP3/PPB3NP/3Q1PP1/1R2RBK1 b - - bm Nxb3; id \"STS(v6.0) Recapturing.018\"; c0 \"Nxb3=10, Bxb3=7, Nd7=7, Rxb3=6\";\n" +
            "1rq2rk1/4bp2/2np2p1/p1p1p3/P1PNP1P1/1PB2P2/1Q3K1P/R2R1N2 b - - bm exd4; id \"STS(v6.0) Recapturing.019\"; c0 \"exd4=10, Bh4+=7, Nxd4=5, cxd4=6\";\n" +
            "1rq2rk1/4bp2/2np2p1/p1p1p3/P1PNP1P1/1PB2P2/1Q4P1/R2R1NK1 b - - bm cxd4; id \"STS(v6.0) Recapturing.020\"; c0 \"cxd4=10, Nxd4=1, Rb7=7, exd4=2\";\n" +
            "1rq2rk1/4bp2/2np2p1/p1p1p3/P1PNP1P1/1PB2P2/1Q4PK/R2R1N2 b - - bm cxd4; id \"STS(v6.0) Recapturing.021\"; c0 \"cxd4=10, Nb4=7, Nxd4=2, exd4=4\";\n" +
            "1rq2rk1/4bp2/3pn1p1/p1p1p3/P1PNP1P1/1PB2P2/1Q5P/R2R1NK1 b - - bm Nxd4; id \"STS(v6.0) Recapturing.022\"; c0 \"Nxd4=10, Ng5=6, cxd4=6, exd4=1\";\n" +
            "2kr3r/p1p4p/1np2p2/3P4/1b2N3/1P4P1/PB3PP1/R2K1R2 b - - bm Nxd5; id \"STS(v6.0) Recapturing.023\"; c0 \"Nxd5=10, Rhe8=1, Rhf8=3, Rxd5+=3\";\n" +
            "2qr2r1/1p3pk1/p1np1np1/7p/2P1Pp2/1PN5/3QN1PP/4RR1K w - - bm Nxf4; id \"STS(v6.0) Recapturing.024\"; c0 \"Nxf4=10, Nd5=4, Qxf4=5, Rxf4=4\";\n" +
            "2r1rbk1/1p3p2/1qn3p1/p1Pp1b1p/5P2/2P1RNP1/PP1Q2BP/RN5K b - - bm Bxc5; id \"STS(v6.0) Recapturing.025\"; c0 \"Bxc5=10, Qa7=3, Qb5=2, Qxc5=6\";\n" +
            "2r2r1k/1q1nbpp1/p3p2p/2P1P3/1p1N1B1P/6Q1/PP3PP1/R2R2K1 b - - bm Nxc5; id \"STS(v6.0) Recapturing.026\"; c0 \"Nxc5=10, Nb8=6, Rxc5=7\";\n" +
            "2r2rk1/1p1q1pb1/p2p1np1/7p/2P1Pp1Q/1PN5/P2B2PP/4RR1K w - - bm Rxf4; id \"STS(v6.0) Recapturing.027\"; c0 \"Rxf4=10, Bxf4=1\";\n" +
            "2r2rk1/3bqppp/1p6/p2pb2Q/Pn1P1P2/1P2PN2/6PP/R1B2RK1 w - - bm fxe5; id \"STS(v6.0) Recapturing.028\"; c0 \"fxe5=10, Nxe5=1, Qxe5=2, dxe5=4\";\n" +
            "2r2rk1/3bqppp/2p5/p2pb2Q/Pn1P1P2/1P2PN2/6PP/R1B2RK1 w - - bm Qxe5; id \"STS(v6.0) Recapturing.029\"; c0 \"Qxe5=10, Nxe5=6, dxe5=3, fxe5=7\";\n" +
            "2r3k1/1bqnbpp1/1p2p2p/2P5/P7/4PN1B/1B2QPPP/R5K1 b - - bm Nxc5; id \"STS(v6.0) Recapturing.030\"; c0 \"Nxc5=10, Bxc5=4, Qxc5=2, bxc5=4\";\n" +
            "2r3k1/1p1nqp1p/1P4p1/Q2Pp3/1RbnP3/5P1P/3N2P1/5B1K w - - bm Rxc4; id \"STS(v6.0) Recapturing.031\"; c0 \"Rxc4=10, Bxc4=2, Nxc4=5, d6=1\";\n" +
            "2r5/4bkp1/p3q3/1b2p2n/N3PBp1/1p2Q3/1P3PPP/RB1R2K1 b - - bm Nxf4; id \"STS(v6.0) Recapturing.032\"; c0 \"Nxf4=10, Bxa4=7, Rc4=7\";\n" +
            "2rq1r1k/p2nb1p1/1p3P1p/3p4/8/1P1p3Q/P2B1PPP/1R1N1RK1 b - - bm Bxf6; id \"STS(v6.0) Recapturing.033\"; c0 \"Bxf6=10, Nxf6=3, gxf6=6\";\n" +
            "2rq1rk1/4ppbp/p5p1/1p1b4/1PNP4/4B3/P1B2PPP/2RQR1K1 b - - bm Rxc4; id \"STS(v6.0) Recapturing.034\"; c0 \"Rxc4=10, Bxc4=7, Bxg2=6, bxc4=6\";\n" +
            "2rq1rk1/4ppbp/p5p1/1p1b4/2NP4/P3B2P/1PB2PP1/2RQR1K1 b - - bm Rxc4; id \"STS(v6.0) Recapturing.035\"; c0 \"Rxc4=10, Bxc4=4, Bxg2=6, bxc4=6\";\n" +
            "2rq1rk1/4ppbp/p5p1/1p1b4/2NP4/P3B3/1P3PPP/1BRQR2K b - - bm Rxc4; id \"STS(v6.0) Recapturing.036\"; c0 \"Rxc4=10, Bxc4=5, Bxg2+=6, bxc4=5\";\n" +
            "2rqr1k1/1b2bp1n/1pnpp1pp/p7/P2pP2P/1NPB1NP1/1P2QPK1/1R2B1R1 w - - bm Nbxd4; id \"STS(v6.0) Recapturing.037\"; c0 \"Nbxd4=10, Bd2=6, Nfxd4=6, cxd4=6\";\n" +
            "2rr4/1pqb1ppk/p1Nppn1p/4n3/2P1P3/2N3P1/PP1QB1PP/R3BRK1 b - - bm Bxc6; id \"STS(v6.0) Recapturing.038\"; c0 \"Bxc6=10, Nxc6=4, Qxc6=4\";\n" +
            "3b2nk/p2Nqrpp/1pB1p3/3pPnp1/1P1P1P1P/P7/2R1QB1K/8 w - - bm fxg5; id \"STS(v6.0) Recapturing.039\"; c0 \"fxg5=10, Qf3=6, Qg4=6, hxg5=7\";\n" +
            "3q4/p2b2kp/1R1P1p2/b1p5/2B1rPp1/8/P5P1/B1Q2RK1 b - - bm Bxb6; id \"STS(v6.0) Recapturing.040\"; c0 \"Bxb6=10, Qxb6=7, Rxc4=7, axb6=8\";\n" +
            "3r1r2/pkp4p/1np1p3/3P4/1b2N3/1P3P2/1B4PP/2R2RK1 b - - bm exd5; id \"STS(v6.0) Recapturing.041\"; c0 \"exd5=10, Nxd5=3, Rxd5=3, cxd5=4\";\n" +
            "3r1rk1/ppp2ppp/1nnN4/7q/8/1B5P/PP1BRPP1/R3Q1K1 b - - bm cxd6; id \"STS(v6.0) Recapturing.042\"; c0 \"cxd6=10, Nd4=8, Nd7=8, Rxd6=8\";\n" +
            "3r4/pkp2r2/1np5/3P3p/1b1BN1p1/1P3PP1/6P1/2R2R1K b - - bm cxd5; id \"STS(v6.0) Recapturing.043\"; c0 \"cxd5=10, Nxd5=6, Rxd5=7\";\n" +
            "3r4/pkp2r2/1np5/3P3p/1b1BN1pP/1P3P2/6P1/2R2R1K b - - bm Rxd5; id \"STS(v6.0) Recapturing.044\"; c0 \"Rxd5=10, cxd5=7, gxf3=2\";\n" +
            "3rkb1r/p2npp1p/1p4p1/1Q1p4/P2p1B2/2q1PN2/2P2PPP/R4RK1 w k - bm exd4; id \"STS(v6.0) Recapturing.045\"; c0 \"exd4=10, Be5=7, Nxd4=6, a5=5\";\n" +
            "3rr1k1/p4bbp/1p1p4/3p1BPp/P2p1N1P/2P1P1K1/R1P5/3R4 w - - bm exd4; id \"STS(v6.0) Recapturing.046\"; c0 \"exd4=10, Kf2=7, a5=7, cxd4=8\";\n" +
            "3rr1k1/pR1n1p1p/5b2/3N4/6p1/P2bp1P1/3N1PBP/2R3K1 w - - bm Nxe3; id \"STS(v6.0) Recapturing.047\"; c0 \"Nxe3=10, Ne4=8, Nxf6+=8, fxe3=8\";\n" +
            "4q1k1/p2n1pp1/4p2p/3n4/1P1P4/P4P2/1B1QN1PP/2r2K2 w - - bm Bxc1; id \"STS(v6.0) Recapturing.048\"; c0 \"Bxc1=10, Kf2=7, Qxc1=4\";\n" +
            "4r1k1/2p2rp1/1pnp4/p2N3p/2P2p2/PP1RP1P1/6KP/5R2 w - - bm gxf4; id \"STS(v6.0) Recapturing.049\"; c0 \"gxf4=10, Nxf4=5, Rxf4=1, exf4=8\";\n" +
            "4rnk1/3r1qb1/bp1p1np1/pNpP2Bp/P1P1Pp1P/3B4/1P1QNR2/6RK w - - bm Nxf4; id \"STS(v6.0) Recapturing.050\"; c0 \"Nxf4=10, Qxf4=2, Rxf4=3\";\n" +
            "5r2/4k3/2qpp2b/pN2p2n/P3PR1p/2P5/6PP/QB2R2K b - - bm Bxf4; id \"STS(v6.0) Recapturing.051\"; c0 \"Bxf4=10, Nxf4=6, Rxf4=2, exf4=2\";\n" +
            "6k1/pp2npp1/2r1bq1p/3p4/PP3N1P/5BP1/4QP2/3R3K w - - bm Bxd5; id \"STS(v6.0) Recapturing.052\"; c0 \"Bxd5=10, Kg1=8, Nxd5=8, b5=8\";\n" +
            "6n1/1k1qn1r1/1p1p4/p1pPpPp1/2P3N1/2B2PP1/PP4Q1/KR6 b - - bm Nxf5; id \"STS(v6.0) Recapturing.053\"; c0 \"Nxf5=10, Qxf5=3, Rf7=7, Rh7=8\";\n" +
            "8/1p4k1/3p4/p1pbp1r1/P1P1P2p/1PK2B2/5R2/8 w - - bm exd5; id \"STS(v6.0) Recapturing.054\"; c0 \"exd5=10, Kd3=6, Rd2=6, cxd5=8\";\n" +
            "8/p1p2p1k/1p1pqn1p/7r/P1PPP1rP/2P1RQ2/3N1K2/6R1 b - - bm Rhxh4; id \"STS(v6.0) Recapturing.055\"; c0 \"Rhxh4=10, Kg6=5, Rgxh4=9, Rxg1=5\";\n" +
            "b2r1r2/6bk/p1n2ppp/q2p4/1p2PN2/1B2B1P1/P3QP2/1R1R2K1 w - - bm Rxd5; id \"STS(v6.0) Recapturing.056\"; c0 \"Rxd5=10, Bxd5=3, Ne6=4, exd5=4\";\n" +
            "b2r1r2/6bk/p1n2ppp/qp1p4/4PN2/1B2B1PP/P3Q3/1R1R2K1 w - - bm Nxd5; id \"STS(v6.0) Recapturing.057\"; c0 \"Nxd5=10, exd5=3\";\n" +
            "b4r1r/3kq1p1/p1p1p1p1/NpPpPP2/1P1P2P1/4Q2p/1K5P/4R2R w - - bm fxe6+; id \"STS(v6.0) Recapturing.058\"; c0 \"fxe6+=10, Ref1=1, Rhf1=1\";\n" +
            "br1r2k1/p2n1pp1/np1p4/2P1p1qp/2P1P3/P1P2PP1/2QNBB1P/2R2R1K b - - bm Naxc5; id \"STS(v6.0) Recapturing.059\"; c0 \"Naxc5=10, Ndxc5=7, bxc5=1, dxc5=1\";\n" +
            "q1rb2rk/1b3ppp/2n1p2n/p1BpP3/B2P1PP1/1p2RN1P/2PN1Q2/1R4K1 w - - bm Rexb3; id \"STS(v6.0) Recapturing.060\"; c0 \"Rexb3=10, Bxb3=7, Nxb3=6, c3=6\";\n" +
            "q1rb2rk/1b3ppp/2n1p2n/p2pP3/B2P1PP1/Bp2RN1P/1RPNQ3/6K1 w - - bm Nxb3; id \"STS(v6.0) Recapturing.061\"; c0 \"Nxb3=10, Rbxb3=3, Rexb3=6, c3=4\";\n" +
            "q2r2k1/1br1bpp1/pp3n2/2nN4/P1P4P/1P3P1B/1BQ4P/1R1RN1K1 b - - bm Nxd5; id \"STS(v6.0) Recapturing.062\"; c0 \"Nxd5=10, Bxd5=1, Qb8=7, Rxd5=5\";\n" +
            "r1b2rk1/4qppp/pp6/3pb2Q/Pn1P1P2/1P2PN2/6PP/R1B2RK1 w - - bm dxe5; id \"STS(v6.0) Recapturing.063\"; c0 \"dxe5=10, Nxe5=1, Qxe5=2, fxe5=6\";\n" +
            "r1b2rk1/p3qppp/Pp6/3pb2Q/1n1P1P2/1P2PN2/1B4PP/R4RK1 w - - bm dxe5; id \"STS(v6.0) Recapturing.064\"; c0 \"dxe5=10, Qxe5=1, fxe5=2\";\n" +
            "r1b3k1/5r2/p4n1p/3q2pP/2p1p3/P1PBB3/3N1PP1/R2QK2R b KQ - bm exd3; id \"STS(v6.0) Recapturing.065\"; c0 \"exd3=10, Bg4=6, Qxd3=2, cxd3=3\";\n" +
            "r1bq1rk1/1p1nbppp/p3pn2/P1p5/4p3/2NP2N1/BPPB1PPP/R2Q1RK1 w - - bm dxe4; id \"STS(v6.0) Recapturing.066\"; c0 \"dxe4=10, Bf4=3, Ncxe4=5, Ngxe4=4\";\n" +
            "r1q1k2r/1p1nbppp/p7/3bpP2/2P1P1P1/1PN1BQ2/7P/2KR3R w kq - bm Nxd5; id \"STS(v6.0) Recapturing.067\"; c0 \"Nxd5=10, Kb2=5, Rxd5=3, exd5=5\";\n" +
            "r1q1k2r/1p1nbppp/p7/Q2bpP2/2B1P1P1/1P2N3/PK5P/3R3R w kq - bm Qxd5; id \"STS(v6.0) Recapturing.068\"; c0 \"Qxd5=10, Bxd5=3, Nxd5=4, Rxd5=1\";\n" +
            "r1qr2k1/1b1nbpp1/1P6/p1B1p2p/4P3/3B3Q/2P1N1PP/RR1N3K b - - bm Nxc5; id \"STS(v6.0) Recapturing.069\"; c0 \"Nxc5=10, Bg5=6\";\n" +
            "r1qr2k1/4bp1p/2p1p1p1/2pb4/PP1P1P2/3NQ3/5BPP/R2R2K1 w - - bm Nxc5; id \"STS(v6.0) Recapturing.070\"; c0 \"Nxc5=10, Rdb1=4, bxc5=1, dxc5=1\";\n" +
            "r1qr2k1/4bp2/p1p1p1p1/2pb4/PP1P1P2/3NQ3/5BPP/R2R2K1 w - - bm bxc5; id \"STS(v6.0) Recapturing.071\"; c0 \"bxc5=10, Nxc5=7, dxc5=9, f5=2\";\n" +
            "r1qr2k1/p3bp1p/P1p1p1p1/2pb4/1P1P1P2/3NQ3/5BPP/R2R2K1 w - - bm dxc5; id \"STS(v6.0) Recapturing.072\"; c0 \"dxc5=10, Nxc5=8, Qe1=7, bxc5=8\";\n" +
            "r1r2nk1/5pp1/1pnpbq2/2B5/p1P1PN1p/8/PPNR1QP1/3R1BK1 b - - bm dxc5; id \"STS(v6.0) Recapturing.073\"; c0 \"dxc5=10, Bg4=6, Ne5=6, bxc5=6\";\n" +
            "r1r3k1/1p2q1pp/n2pp3/pN6/2nbP1P1/P1N2Q2/1PP4P/1K1R1R2 w - - bm Rxd4; id \"STS(v6.0) Recapturing.074\"; c0 \"Rxd4=10, Na4=6, Nxd4=3, g5=6\";\n" +
            "r2q1rk1/1b1pbppp/1pn1pn2/p7/P2pP3/2PB1N2/1P1NQPPP/R1B2RK1 w - - bm Nxd4; id \"STS(v6.0) Recapturing.075\"; c0 \"Nxd4=10, Rb1=7, Re1=7, cxd4=7\";\n" +
            "r2q1rk1/2pnbppp/p3p3/3b4/3P2P1/3QnN1P/PP1BPP2/RNR3K1 w - - bm Qxe3; id \"STS(v6.0) Recapturing.076\"; c0 \"Qxe3=10, Ne1=7\";\n" +
            "r2q1rk1/p1p1n1pp/1p1p4/P2Ppb2/1PP5/4b1P1/3QNPBP/R4RK1 w - - bm fxe3; id \"STS(v6.0) Recapturing.077\"; c0 \"fxe3=10, Qb2=7, Qd1=7, Qxe3=8\";\n" +
            "r2qk1r1/1b2bp2/3ppp1p/p1p5/2B1PP2/1nN5/PPPQ2PP/1K1R3R w q - bm axb3; id \"STS(v6.0) Recapturing.078\"; c0 \"axb3=10, Bb5+=5, Bxb3=5, cxb3=6\";\n" +
            "r2qk1r1/1b3pb1/2pppp1p/8/2B1PP2/1nN5/PPPQ2PP/1K1R3R w q - bm Bxb3; id \"STS(v6.0) Recapturing.079\"; c0 \"Bxb3=10, Qe3=4, axb3=5, cxb3=8\";\n" +
            "r2qk1r1/3bbp2/3ppp2/p7/2B1PP1p/1nN5/PPPQ2PP/1K1R3R w q - bm cxb3; id \"STS(v6.0) Recapturing.080\"; c0 \"cxb3=10, Bxb3=3, Qf2=4, axb3=8\";\n" +
            "r2r1k2/2b1npp1/p1np3p/1p6/3P1B2/P1N4P/1PBR1PP1/3qR1K1 w - - bm Bxd1; id \"STS(v6.0) Recapturing.081\"; c0 \"Bxd1=10, Nxd1=6, Rdxd1=5, Rexd1=6\";\n" +
            "r2r1nk1/1q2bp2/p3pnpp/2p1B3/2p5/1P2N1P1/P1QNPP1P/2R2RK1 w - - bm Ndxc4; id \"STS(v6.0) Recapturing.082\"; c0 \"Ndxc4=10, Bxf6=4, Nexc4=7, bxc4=3\";\n" +
            "r3b3/4kpp1/3p1n1p/5P2/1r2P1P1/1p2RN1P/2PN4/1R4K1 w - - bm Rbxb3; id \"STS(v6.0) Recapturing.083\"; c0 \"Rbxb3=10, Rexb3=5, cxb3=7, e5=6\";\n" +
            "r3r1k1/1p3pb1/6p1/p2b2Pp/PpB1PR2/1P6/2P4P/2KR4 w - - bm Rxd5; id \"STS(v6.0) Recapturing.084\"; c0 \"Rxd5=10, Bd3=7, Bxd5=5, exd5=4\";\n" +
            "r4r1k/p2nb1p1/1p2pPqp/3p4/P1pPn3/2P1BN1P/1QP1BPP1/R5RK b - - bm Qxf6; id \"STS(v6.0) Recapturing.085\"; c0 \"Qxf6=10, Bxf6=4, Ndxf6=5, Rxf6=8\";\n" +
            "r4r2/5pk1/1bR1n1p1/Pp1Np2p/1P1pP3/8/5PPP/2R3K1 w - - bm Nxb6; id \"STS(v6.0) Recapturing.086\"; c0 \"Nxb6=10, Kf1=5, axb6=3\";\n" +
            "r4rk1/1b1pq1pp/1p6/p1n1pp2/2PBN3/P3P3/1PQ1BPPP/3R1RK1 b - - bm Bxe4; id \"STS(v6.0) Recapturing.087\"; c0 \"Bxe4=10, Nxe4=6, exd4=6, fxe4=5\";\n" +
            "r4rk1/1p1bqpp1/2p4p/3pb2Q/Pn1P1P2/1P2PN2/6PP/R1B2RK1 w - - bm Nxe5; id \"STS(v6.0) Recapturing.088\"; c0 \"Nxe5=10, Qxe5=7, fxe5=5\";\n" +
            "r4rk1/1p5p/2p2p2/2P2Pp1/p5B1/PbR2n2/1P2PPKP/2R5 w - - bm Bxf3; id \"STS(v6.0) Recapturing.089\"; c0 \"Bxf3=10, Kxf3=3, Rxf3=5\";\n" +
            "r4rk1/3p1b2/2p2p2/1pP1pPp1/4P1B1/P1R2n2/1P3PKP/2R5 w - - bm Bxf3; id \"STS(v6.0) Recapturing.090\"; c0 \"Bxf3=10, Kxf3=6, Re3=6, Rxf3=7\";\n" +
            "r4rk1/pp1qppbp/2p1b1p1/n7/P2PpB1N/2P3P1/2P1QPBP/1R2R1K1 w - - bm Bxe4; id \"STS(v6.0) Recapturing.091\"; c0 \"Bxe4=10, Qd2=8, Qe3=8, Qxe4=8\";\n" +
            "r4rk1/ppp2ppp/1nnb4/8/1P1P3q/PBN1B2P/4bPP1/R2QR1K1 w - - bm Qxe2; id \"STS(v6.0) Recapturing.092\"; c0 \"Qxe2=10, Bxf7+=6, Nxe2=3, Rxe2=6\";\n" +
            "r4rn1/2q2p1k/p2p2np/4pb1Q/1pP1P3/4B2P/2P3PN/3R1R1K w - - bm Rxf5; id \"STS(v6.0) Recapturing.093\"; c0 \"Rxf5=10, Bxh6=6, Qxf5=3, exf5=4\";\n" +
            "r5b1/3rn1pk/p1n4p/1p1p1p1P/3P1B2/P1N3P1/1P1R1P2/3qRBK1 w - - bm Nxd1; id \"STS(v6.0) Recapturing.094\"; c0 \"Nxd1=10, Rdxd1=6, Rexd1=5, Rxe7=7\";\n" +
            "r5k1/5pbp/6P1/p2R4/1p5r/P5R1/1PP5/2KB4 b - - bm hxg6; id \"STS(v6.0) Recapturing.095\"; c0 \"hxg6=10, Bh6+=6, bxa3=2, fxg6=7\";\n" +
            "r6r/ppk1npp1/1b2p2p/2pnP2P/4N3/2p2N2/PPPB1PP1/1K1RR3 w - - bm Bxc3; id \"STS(v6.0) Recapturing.096\"; c0 \"Bxc3=10, Bc1=8, Nxc3=8, bxc3=6\";\n" +
            "rn1q1rk1/1b2bppp/1p1pp1n1/pN6/P2pP3/2PB1NP1/1P2QP1P/R1B2RK1 w - - bm cxd4; id \"STS(v6.0) Recapturing.097\"; c0 \"cxd4=10, Nbxd4=2, Nfxd4=5, Rd1=3\";\n" +
            "rq1r2k1/pb2npbp/4pnp1/3P2N1/Q6P/1B4N1/PP3PP1/R1BR2K1 b - - bm Bxd5; id \"STS(v6.0) Recapturing.098\"; c0 \"Bxd5=10, Nexd5=6, exd5=3\";\n" +
            "rqr3k1/3bb1p1/p1n2n1p/3p1p2/1P1NpP2/2N1B2P/1PPQB1P1/1K1R3R b - - bm Bxb4; id \"STS(v6.0) Recapturing.099\"; c0 \"Bxb4=10, Nxb4=4, Qb7=3, Qxb4=3\";\n" +
            "rqr3k1/4bpp1/pBbp1n1p/Pp2p3/3PPP2/2N1QNP1/1P4P1/R4R1K w - - bm fxe5; id \"STS(v6.0) Recapturing.100\"; c0 \"fxe5=10, Rae1=2, d5=1, dxe5=4\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    
