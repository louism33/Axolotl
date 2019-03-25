package strategictestsuite;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
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
public class STS12CenterControl {


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

    public STS12CenterControl(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getBoardFen());
        System.out.println(EPDObject.getBoard());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.DEBUG = false;
        int move = EngineBetter.searchFixedTime(EPDObject.getBoard(), timeLimit);

        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }

    private static final String positions = "" +
            "1k1r4/4bp2/p1q1pnr1/6B1/NppP3P/6P1/1P3P2/2RQR1K1 w - - bm Re5; id \"STS(v12.0) Center Control.001\";\n" +
            "1k5r/1pq1b2r/p2p1p2/4n1p1/R3P1p1/1BP3B1/PP1Q3P/1K1R4 w - - bm Bd5; id \"STS(v12.0) Center Control.002\";\n" +
            "1kb4r/1p3pr1/3b1p1p/q2B1p2/p7/P1P3P1/1P1Q2NP/K2RR3 b - - bm Be5; id \"STS(v12.0) Center Control.003\";\n" +
            "1kr5/1b3ppp/p4n2/3p4/2qN1P2/2r2B2/PQ4PP/R2R3K b - - bm Ne4; id \"STS(v12.0) Center Control.004\";\n" +
            "1n1r4/p1q2pk1/b2bp2p/4N1p1/3P1P2/1QN1P3/5PBP/1R5K w - - bm Ne4; id \"STS(v12.0) Center Control.005\";\n" +
            "1n1rr1k1/1pq2pp1/3b2p1/2p3N1/P1P5/P3B2P/2Q2PP1/R2R2K1 w - - bm Ne4; id \"STS(v12.0) Center Control.006\";\n" +
            "1n1rr1k1/5pp1/1qp4p/3p3P/3P4/pP1Q1N2/P1R2PP1/1KR5 w - - bm Ne5; id \"STS(v12.0) Center Control.007\";\n" +
            "1r1r1bk1/1bq2p1p/pn2p1p1/2p1P3/5P2/P1NBB3/1P3QPP/R2R2K1 b - - bm Nd5; id \"STS(v12.0) Center Control.008\";\n" +
            "1r2qrk1/2p3pp/2Qb1p2/2p1pP2/8/BP6/3P1PPP/R4RK1 w - - bm Qe4; id \"STS(v12.0) Center Control.009\";\n" +
            "1r2qrk1/4n3/ppbp3p/n1p1p1p1/2P5/B1PP2PP/Q2N1PB1/1R2R1K1 w - - bm Ne4; id \"STS(v12.0) Center Control.010\";\n" +
            "1r3b2/3R2pk/6q1/3Q1p2/p6P/4B2P/1P3P1K/8 w - - bm Bd4; id \"STS(v12.0) Center Control.011\";\n" +
            "1r3r1k/6pp/p2p4/q3pb2/2P4P/1P2Q1P1/P4PB1/R4K1R w - - bm Bd5; id \"STS(v12.0) Center Control.012\";\n" +
            "1r3r1k/7p/p1p1q3/1pP1P3/3P4/Pb6/3Q2BP/2R1R1K1 b - - bm Bd5; id \"STS(v12.0) Center Control.013\";\n" +
            "1r4k1/p1rnpp2/p2p1bp1/2qP4/4PQ2/1PN4P/P2B2P1/1R3R1K b - - bm Qd4; id \"STS(v12.0) Center Control.014\";\n" +
            "1rb2rnk/2qn4/pp1p3p/2pP1p2/PPPbp2B/1QN5/R2NBPPP/1R4K1 b - - bm Ne5; id \"STS(v12.0) Center Control.015\";\n" +
            "2b2q1k/2p4p/1rNn1p2/1p1P4/p3p3/P1P5/1P1Q2PP/1B1R2K1 w - - bm Qd4; id \"STS(v12.0) Center Control.016\";\n" +
            "2b3k1/1q3pp1/p2R3p/8/Q7/2P1r3/6PP/R5K1 w - - bm Qd4; id \"STS(v12.0) Center Control.017\";\n" +
            "2br4/2q2k1p/p2bp1p1/1r6/4BP2/2p1B3/P3Q1PP/2R2R1K w - - bm Bd4; id \"STS(v12.0) Center Control.018\";\n" +
            "2q1rbk1/1b3p2/p4Pp1/1p1p3p/3B4/1P1Q1N1P/1P3PP1/R5K1 w - - bm Ne5; id \"STS(v12.0) Center Control.019\";\n" +
            "2r1r1k1/p1qn1pp1/1p3n1p/3p4/3P3B/P1NQ4/1P3PPP/2RR2K1 b - - bm Ne4; id \"STS(v12.0) Center Control.020\";\n" +
            "2r1rbk1/pp2p2p/6p1/n4p2/1NbP4/4PBBP/P4PP1/2RR2K1 w - - bm Nd5; id \"STS(v12.0) Center Control.021\";\n" +
            "2r2k2/p1q1bpp1/2p2n2/2Np3r/1P5p/P1Q1B2P/5PP1/2R1R1K1 w - - bm Bd4; id \"STS(v12.0) Center Control.022\";\n" +
            "2r2rk1/1p3ppp/p2q1n2/3pn3/N7/4P2P/PP2BPP1/2RQ1RK1 w - - bm Qd4; id \"STS(v12.0) Center Control.023\";\n" +
            "2r2rk1/1pp2qpb/1n2p2p/pP1p1P2/P2PPBP1/7P/3NQ3/R1R3K1 w - - bm Be5; c0 \"Be5=10, Bg3=4, Ra3=4, Rf1=4\"; id \"STS(v12.0) Center Control.024\";\n" +
            "2r3k1/1b2q1pp/1pr1p3/4B3/2P1p3/4P3/4QPPP/1R1R2K1 w - - bm Rd4; c0 \"Rd4=10, h3=2, h4=2, Qg4=2\"; id \"STS(v12.0) Center Control.025\";\n" +
            "2r3k1/p2npp2/p2p1bp1/3P4/1qN1PQ1P/1P2B3/r5P1/1R3R1K b - - bm Ne5; c0 \"Ne5=10, a5=7, Bd4=10, Qb8=6, Rc7=6\"; id \"STS(v12.0) Center Control.026\";\n" +
            "2r3k1/ppr2p2/4b2p/4b1p1/P7/2N1P3/1PB2PPP/2RRK3 w - - bm Be4; c0 \"Be4=10, Bd3=6, h3=6, Rd3=6\"; id \"STS(v12.0) Center Control.027\";\n" +
            "2r4k/5p1p/1prp1q1P/2n1p1p1/p3P1P1/P3BP2/1PP5/1KRR3Q w - - bm Rd5; c0 \"Rd5=10, Qg2=3, Qh3=7, Qh5=5, Re1=6, Rf1=7\"; id \"STS(v12.0) Center Control.028\";\n" +
            "2r5/1b2kp1p/3qp1p1/1B6/3pn2N/8/PP2QPPP/4R1K1 b - - bm Qd5; c0 \"Qd5=10, Nf6=4, Qf4=5, Rc5=4\"; id \"STS(v12.0) Center Control.029\";\n" +
            "2r5/1bq2k2/p2pp2r/5p2/2PN1Pp1/1PB1Q3/P5PP/4R1K1 b - - bm Be4; c0 \"Be4=10, g3=1, Qc5=1, Qd7=2\"; id \"STS(v12.0) Center Control.030\";\n" +
            "2rq1k1r/pp3p2/7p/1BPp1bp1/1P6/P1N1P3/3Q1PPP/2R1K2n w - - bm Qd4; c0 \"Qd4=10, Ke2=4, Kf1=5, Nxd5=5\"; id \"STS(v12.0) Center Control.031\";\n" +
            "2rq1rk1/p3b1pp/2p1p3/8/P2Pp3/1P1bP1P1/3B3P/2RQRBK1 b - - bm Qd5; c0 \"Qd5=10, Bxf1=1, e5=1\"; id \"STS(v12.0) Center Control.032\";\n" +
            "2rq1rk1/pb2bppp/1p2pn2/4N3/3P1B2/2PB4/P4PPP/2RQR1K1 b - - bm Qd5; c0 \"Qd5=10, Ba3=7, Nd5=7, Rc7=6\"; id \"STS(v12.0) Center Control.033\";\n" +
            "2rq1rk1/pb3ppp/1p1p1b2/2n1p3/P1B1P3/1P3N2/2P1RPPP/R1BQ2K1 w - - bm Bd5; c0 \"Bd5=10, Bb2=6, Nd2=3, Rb1=5\"; id \"STS(v12.0) Center Control.034\";\n" +
            "2rr4/p1N1pk1p/1p4pb/n4p2/2bP4/4PB1P/P1R2PPB/3R2K1 w - - bm d5; c0 \"d5=10, Be5=4, Rdc1=5\"; id \"STS(v12.0) Center Control.035\";\n" +
            "3r1bk1/3q1p2/4bP1p/pp4pP/2pP4/2P3B1/P1Q3P1/4RRK1 w - - bm Re5; c0 \"Re5=10, a3=1, Re3=4, Rf3=3\"; id \"STS(v12.0) Center Control.036\";\n" +
            "3r1qk1/6pp/1p1np3/2n4Q/p7/P3N1P1/1B2PP1P/3R2K1 w - - bm Rd4; c0 \"Rd4=10, Bd4=6, f3=3, Nc4=5, Qg4=4, Qh4=3\"; id \"STS(v12.0) Center Control.037\";\n" +
            "3r1r2/2n3kp/1pp1p1p1/p2nP3/P7/2NR2PB/1P3P1P/3R2K1 w - - bm Ne4; c0 \"Ne4=10, f4=4, Rd4=3\"; id \"STS(v12.0) Center Control.038\";\n" +
            "3r1r2/2p3k1/1p3b1p/p7/2P1qP1P/1P1R2P1/P4P2/1Q1R2K1 b - - bm Bd4; c0 \"Bd4=10, Rde8=2, Rf7=2, Rxd3=2\"; id \"STS(v12.0) Center Control.039\";\n" +
            "3r2k1/1p1b1pp1/p1q4p/8/2nNRB2/2P3P1/P4P1P/4Q1K1 b - - bm Qd5; c0 \"Qd5=10, Qb6=2, Qc5=3, Qc8=2\"; id \"STS(v12.0) Center Control.040\";\n" +
            "3r2k1/3r1pp1/2QP4/pP5p/1qN5/4R1P1/4PK1P/8 w - - bm Re4; c0 \"Re4=10, Rd3=1\"; id \"STS(v12.0) Center Control.041\";\n" +
            "3r2k1/3r2p1/2q1p2p/1p1n1p2/p7/P3RQ1P/BPP2PP1/3R2K1 w - - bm Re5; c0 \"Re5=10, h4=3, Rd2=3, Rde1=2, Ree1=1\"; id \"STS(v12.0) Center Control.042\";\n" +
            "3r2r1/BpqP1pkp/6p1/3Qb3/8/2p3P1/PP1R1PBP/5K2 w - - bm Bd4; c0 \"Bd4=10, Bb6=3, bxc3=7, Rd1=5\"; id \"STS(v12.0) Center Control.043\";\n" +
            "3r4/1p4kp/p1b3p1/2q1p3/P1P5/1B3P1P/4Q1P1/1R5K b - - bm Rd4; c0 \"Rd4=10, a5=3, b6=4, Kf6=4, Kf7=4\"; id \"STS(v12.0) Center Control.044\";\n" +
            "3rbk2/4np1p/1p2p3/pP2r1p1/P1R1N1P1/3BP3/3K3P/3R4 b - - bm Nd5; c0 \"Nd5=10, Bd7=3, h6=4, Kg7=4\"; id \"STS(v12.0) Center Control.045\";\n" +
            "4k3/p1p3p1/1pbp2qp/n3p3/2P3PB/2PPQ2P/P3P1B1/6K1 w - - bm Bd5; c0 \"Bd5=10, Bf1=5, Bf2=5, Bxc6+=7\"; id \"STS(v12.0) Center Control.046\";\n" +
            "4r1k1/1b3pp1/p2pqb2/1p4r1/4PR2/PNN4P/1PP2QP1/5R1K w - - bm Nd4; c0 \"Nd4=10, Nd5=1, Qb6=3\"; id \"STS(v12.0) Center Control.047\";\n" +
            "4r1k1/1pb1qp2/p1b3p1/3p1rN1/P4P1p/1PPRB1P1/3Q3P/3R2K1 w - - bm Bd4; c0 \"Bd4=10, Bf2=4, Nf3=3, Re1=4, Rf1=4\"; id \"STS(v12.0) Center Control.048\";\n" +
            "4r1k1/3r1p1p/3p2p1/3P2Q1/4PR2/2n2B1P/5qPK/R7 b - - bm Qd4; c0 \"Qd4=10, Qd2=1, Qe3=1, Ra8=6, Rb7=1\"; id \"STS(v12.0) Center Control.049\";\n" +
            "4r1k1/4r1pp/p7/3n4/2p1P3/2P1BPN1/P1P4P/1K2R3 w - - bm Bd4; c0 \"Bd4=10, Bd2=7, Kc1=7, Nf5=7\"; id \"STS(v12.0) Center Control.050\";\n" +
            "4r1k1/p5b1/P2p1pp1/q1pP3p/2Pn1BbP/2NP2P1/3Q2BK/1R6 w - - bm Ne4; c0 \"Ne4=10, Be5=7, Kh1=6, Nb5=10, Rb7=7\"; id \"STS(v12.0) Center Control.051\";\n" +
            "4r1k1/pb2qp1p/4n2P/3p1pP1/1P1N1Q2/2N5/P1P5/5R1K w - - bm Qe5; c0 \"Qe5=10, Qd2=1\"; id \"STS(v12.0) Center Control.052\";\n" +
            "4r1k1/R3rpp1/1p3n1p/8/2Q5/4B2P/1q3PP1/3R2K1 w - - bm Bd4; c0 \"Bd4=10, Raa1=3, Rxe7=2\"; id \"STS(v12.0) Center Control.053\";\n" +
            "4r2k/1q4p1/p2Bp2p/P3P3/1PpN2P1/2Pb2b1/Q7/K2R4 b - - bm Qe4; c0 \"Qe4=10, Qa8=2, Qd7=1, Qf7=1\"; id \"STS(v12.0) Center Control.054\";\n" +
            "4r3/p4pk1/3p1n1p/1P4p1/4q3/1P4P1/1N1QP2P/4R1K1 b - - bm Qe5; c0 \"Qe5=10, d5=4, Qf5=4, Rb8=4\"; id \"STS(v12.0) Center Control.055\";\n" +
            "5n2/kbr3p1/5n1p/Pp2pP2/4P1PP/2p5/P1Br4/2RNRNK1 b - - bm Rd4; c0 \"Rd4=10, Ka6=5, Rd8=2, Rdd7=2\"; id \"STS(v12.0) Center Control.056\";\n" +
            "5r1k/p1r3np/1p3q2/2p4P/2Q2P2/8/PPB5/1K1R1R2 w - - bm Rd5; c0 \"Rd5=10, f5=2, Rfe1=2, Rg1=2\"; id \"STS(v12.0) Center Control.057\";\n" +
            "5rk1/p3pp1p/2Q2bp1/q1P5/3p4/5BP1/Pr2PP1P/2RR2K1 w - - bm Qe4; c0 \"Qe4=10, a4=6, h4=6, Kg2=3\"; id \"STS(v12.0) Center Control.058\";\n" +
            "5rk1/pb3ppp/1p3q2/2n5/2B1p3/2P5/P3QPPP/R2R2K1 w - - bm Rd4; c0 \"Rd4=10, a4=4, Bd5=5, Qe1=4, Qe3=5, Rac1=4\"; id \"STS(v12.0) Center Control.059\";\n" +
            "5rk1/qbrpnppp/p3p3/P2nP3/Np6/1N1B4/1PPQ1PPP/3RR1K1 w - - bm Re4; c0 \"Re4=10, c3=2, c4=2, g3=2\"; id \"STS(v12.0) Center Control.060\";\n" +
            "5rqk/3b2pp/1pnNp3/3r4/6P1/PQ2B1RP/5PK1/3R4 b - - bm Ne5; c0 \"Ne5=10, Na5=7, Ne7=7, Rxd1=6\"; id \"STS(v12.0) Center Control.061\";\n" +
            "6k1/1p2p3/2n1P2p/p3b3/2P2p2/1P4qP/P7/1B3QBK w - - bm Be4; c0 \"Be4=10, Bf2=4, Qg2=2\"; id \"STS(v12.0) Center Control.062\";\n" +
            "6k1/1p3pp1/p3q2p/3p3P/1Pn2Q2/P3PP2/4NKP1/8 w - - bm Qd4; c0 \"Qd4=10, g4=2\"; id \"STS(v12.0) Center Control.063\";\n" +
            "6k1/4qp2/p6p/1p6/2n2p2/P4Q2/1P4PP/6BK b - - bm Qe5; c0 \"Qe5=10, Qd6=3, Qf6=4, Qg5=1\"; id \"STS(v12.0) Center Control.064\";\n" +
            "6k1/5p2/p3b2p/1pp4q/4Pp2/1P3P2/P1Q3PP/3B1K2 b - - bm Qe5; c0 \"Qe5=10, c4=2, f6=1, Kh8=3\"; id \"STS(v12.0) Center Control.065\";\n" +
            "7k/1qr2p2/p3p2p/P1b1P1p1/2Q1n3/1p2BN1P/1P3PP1/2R3K1 w - - bm Nd4; c0 \"Nd4=10, Bd4=1, Kf1=1, Kh2=1\"; id \"STS(v12.0) Center Control.066\";\n" +
            "8/2p1r1pk/2pq2bp/2p5/2P3Q1/pP1P2NP/P3PRK1/8 b - - bm Qe5; c0 \"Qe5=10, Qd8=7, Re5=7, Re6=7\"; id \"STS(v12.0) Center Control.067\";\n" +
            "8/3n1kpp/1pr1p3/2n5/pR6/P3NPP1/1B2PK1P/8 w - - bm Bd4; c0 \"Bd4=10, f4=4, Nc4=4, Ng4=4\"; id \"STS(v12.0) Center Control.068\";\n" +
            "8/6k1/1b1p1pp1/1b1Pp3/2q1P3/6P1/2B2PKN/3Q4 b - - bm Qd4; c0 \"Qd4=10, Bc5=4, Bd4=4, Qa2=4\"; id \"STS(v12.0) Center Control.069\";\n" +
            "8/pp2r1kp/2prP3/5R2/2P5/3n2P1/P5BP/1R5K w - - bm Be4; c0 \"Be4=10, Bd5=6, Bf1=7, Rb3=7, Rf3=7\"; id \"STS(v12.0) Center Control.070\";\n" +
            "b4rk1/8/4pr1p/2q5/P4p2/2PB4/6PP/R3QR1K w - - bm Be4; c0 \"Be4=10, Be2=2, Qe4=5, Rb1=4, Rd1=3, Rg1=2\"; id \"STS(v12.0) Center Control.071\";\n" +
            "brr1n1k1/4bpp1/q2p4/Np2n3/1P1RP2p/2N4P/2P3PB/3RQB1K w - - bm Nd5; c0 \"Nd5=10, Be2=1, Qd2=1, Ra1=1\"; id \"STS(v12.0) Center Control.072\";\n" +
            "k2r4/2q3p1/p1Pr1p2/P1R1p2P/5pP1/8/1PQp1P2/1K1R4 b - - bm Rd4; c0 \"Rd4=10, Ka7=6, Kb8=6, Rd3=6\"; id \"STS(v12.0) Center Control.073\";\n" +
            "nr2q1k1/1p2rpb1/p2p2pp/P7/1PRN4/4BBPb/3Q1P1P/2R3K1 w - - bm Bd5; c0 \"Bd5=10, Kh1=5, Ne2=6, R4c2=5\"; id \"STS(v12.0) Center Control.074\";\n" +
            "r1q1r1k1/1p2b1pp/2p1P1b1/3p1p2/p2N1B2/P1Q1PP1P/1P4P1/2RR2K1 w - - bm Be5; c0 \"Be5=10, Kf2=1, Kh2=2, Rd2=2\"; id \"STS(v12.0) Center Control.075\";\n" +
            "r1q3k1/p2rnpb1/1pnBb1pp/2p1P3/6P1/2N2N1P/PP2QPB1/R2R2K1 w - - bm Qe4; c0 \"Qe4=10, Qe3=4, Rac1=5, Rd2=5\"; id \"STS(v12.0) Center Control.076\";\n" +
            "r1r3k1/pb3p1p/1pqBp1p1/4P3/3b4/2P2P2/PR1N2PP/2RQ3K w - - bm Ne4; c0 \"Ne4=10, Qe2=3, Rbb1=1, Rbc2=1\"; id \"STS(v12.0) Center Control.077\";\n" +
            "r2q1rk1/pp3pbp/3P2p1/4nb2/2p2N2/4B1P1/PP3PBP/R2Q1RK1 w - - bm Qd5; c0 \"Qd5=10, b3=2, Rc1=2, Re1=1\"; id \"STS(v12.0) Center Control.078\";\n" +
            "r2q3r/1p3pk1/2b1p1p1/2R1P2p/p2P1P1R/P1Q1N1P1/1P3K2/8 b - - bm Be4; c0 \"Be4=10, Rc8=4\"; id \"STS(v12.0) Center Control.079\";\n" +
            "r2qk2r/3nbpp1/2bBp2p/p3P3/1p6/8/PPPQBPPP/1NKR3R w kq - bm Qd4; c0 \"Qd4=10, f4=5, Qe3=7, Rhg1=5\"; id \"STS(v12.0) Center Control.080\";\n" +
            "r2r2k1/p2n1p2/4q2p/3p2p1/1PpB4/P1NnPP2/2Q3PP/R2R2K1 b - - bm N7e5; c0 \"N7e5=10, a5=6, a6=6, Nb8=5\"; id \"STS(v12.0) Center Control.081\";\n" +
            "r2r2k1/pN3p1p/2n1pp2/4q3/2P1P3/1Q6/P4PPP/R4RK1 b - - bm Rd4; c0 \"Rd4=10, Rd2=6, Rd7=7, Rdb8=6\"; id \"STS(v12.0) Center Control.082\";\n" +
            "r3k2r/2qb1ppp/p3p3/2PpP3/2p2P2/P1P5/4B1PP/1R1Q1RK1 w kq - bm Qd4; c0 \"Qd4=10, c6=7, Rb2=7, Rb6=7\"; id \"STS(v12.0) Center Control.083\";\n" +
            "r3kb1r/3n1ppp/p1bPp3/1q6/Npp1BB2/8/PP3PPP/2RQR1K1 w kq - bm Qd4; c0 \"Qd4=10, b3=2, Bxc6=4, Qc2=3, Qf3=7\"; id \"STS(v12.0) Center Control.084\";\n" +
            "r3q1k1/p3r1p1/3R3p/2p5/1p2P3/1P4P1/P1Q4P/4R1K1 b - - bm Re5; c0 \"Re5=10, Qb5=5, Qh5=5, Rc8=4\"; id \"STS(v12.0) Center Control.085\";\n" +
            "r3qrk1/4bpp1/Rp2pn1p/2p1N3/3P4/2P1P1B1/4QPPP/3R2K1 b - - bm Ne4; c0 \"Ne4=10, cxd4=2, Nd5=1, Rxa6=1\"; id \"STS(v12.0) Center Control.086\";\n" +
            "r3r1k1/1b2qpp1/p3pn1p/1pPp4/PP1Q3P/1B2PPB1/6P1/R3K2R w KQ - bm Be5; c0 \"Be5=10, axb5=3, Bd6=3, Qb2=3\"; id \"STS(v12.0) Center Control.087\";\n" +
            "r3r1k1/pp2qpb1/1n2b1pp/8/5B2/1P3NP1/P4PBP/R2QR1K1 w - - bm Be5; c0 \"Be5=10, Bd6=6, Nd2=6, Rc1=2\"; id \"STS(v12.0) Center Control.088\";\n" +
            "r3r3/2q1bp1k/2N3pp/p1p5/Pp2Q3/4P3/1P3PPP/2RR2K1 w - - bm Qd5; c0 \"Qd5=10, Nxe7=6, Qf3=5\"; id \"STS(v12.0) Center Control.089\";\n" +
            "r4r1k/p5p1/1qp1bp1p/8/3N4/P1Q1P3/1P4PP/2R2RK1 b - - bm Bd5; c0 \"Bd5=10, Bd7=4, Bf7=5, Bg8=5\"; id \"STS(v12.0) Center Control.090\";\n" +
            "r4rk1/1pp2qpb/1n2p2p/pP1p1P2/P2PPBP1/7P/3NQ3/R3R1K1 w - - bm Be5; c0 \"Be5=10, Bg3=3, Ra3=3, Rac1=2, Rec1=2, Rf1=1\"; id \"STS(v12.0) Center Control.091\";\n" +
            "r4rk1/2pb2b1/np1p3p/3P1p2/1PP1pP1q/4B3/2QNN1PP/1R3RK1 w - - bm Bd4; c0 \"Bd4=10, Nd4=1, Rf2=3\"; id \"STS(v12.0) Center Control.092\";\n" +
            "r4rk1/pb1p2pp/1q1Ppn2/2p5/2P2P2/3BB3/PP2Q1PP/R3K2R b KQ - bm Be4; c0 \"Be4=10, Ne4=6, Qxd6=6, Rab8=6\"; id \"STS(v12.0) Center Control.093\";\n" +
            "r5k1/3pppbp/6p1/4n3/1q2P3/4B3/P4PPP/2RQ1K1R w - - bm Qd5; c0 \"Qd5=10, h4=1, Qc2=4\"; id \"STS(v12.0) Center Control.094\";\n" +
            "r5k1/8/r6p/P1q1p3/5p2/2P5/6PP/R3QR1K w - - bm Qe4; c0 \"Qe4=10, Qb1=2, Rb1=2, Rd1=1\"; id \"STS(v12.0) Center Control.095\";\n" +
            "r5k1/p2b1pbp/6p1/2p5/1nn5/2N3P1/PP1N1P1P/R1B2RK1 b - - bm Ne5; c0 \"Ne5=10, Nb6=2, Nc2=2\"; id \"STS(v12.0) Center Control.096\";\n" +
            "r5r1/1pp4p/2bn1k1B/p1p1p3/5PP1/1P5P/P1P1R3/R3N1K1 b - - bm e4; c0 \"e4=10, exf4=3, Kg6=2, Nf7=2, Rae8=3\"; id \"STS(v12.0) Center Control.097\";\n" +
            "r6k/pp1q1pp1/1n6/1pQp4/3Pr2p/P2NP2P/5PP1/2R2RK1 w - - bm Ne5; c0 \"Ne5=10, Qb4=3, Qc7=4, Rb1=4\"; id \"STS(v12.0) Center Control.098\";\n" +
            "r6r/4kp2/1qbp1p1b/p3pP1N/1pB1P2p/8/PPP1Q1PP/1K2R2R b - - bm Qd4; c0 \"Qd4=10, Bg5=3, Qc5=3, Rac8=3\"; id \"STS(v12.0) Center Control.099\";\n" +
            "r7/4kp2/3Rp2b/2p1P3/bp3P1P/rB2N3/P1P5/1K1R4 w - - bm Bd5; c0 \"Bd5=10, Nc4=2\"; id \"STS(v12.0) Center Control.100\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    