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
public class STS15PointlessExchange {


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

    public STS15PointlessExchange(Object edp, Object name) {
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
            "rnb1r1k1/pp2bppp/2p2n2/8/1q1Q1B2/2N2NPP/PP2PPB1/R4RK1 w - - bm Qd2; id \"STS(v15.0) AT.001\"; c0 \"Qd2=10, Rfd1=1, Rfc1=3, Rab1=2, Qxb4=3\";\n" +
            "rn2kbnr/pp3pp1/q1p1p3/3pP2p/3P4/2NQ1N2/PPP2PPP/R1B2RK1 w kq - bm Qd1; id \"STS(v15.0) AT.002\"; c0 \"Qd1=10, Rb1=2, Qxa6=3, Qe3=2, Qe2=2, Qd2=5, Bd2=1\";\n" +
            "rn2k2r/p1pq1ppp/bp2pn2/8/Q2P4/P4N2/1P2PPPP/R1B1KB1R w KQkq - bm Qc2; id \"STS(v15.0) AT.003\"; c0 \"Qc2=10, Qd1=2\";\n" +
            "rn2k1r1/pbpqnp2/1p1pp3/6p1/Q1PP4/2P1P1B1/P2N1PP1/1R2KB1R w Kq - bm Qc2; id \"STS(v15.0) AT.004\"; c0 \"Qc2=10, Qxd7+=8, Qd1=9\";\n" +
            "rbb2r1k/1p3pp1/p3q2p/B7/4Q3/1N4P1/PP3P1P/3R1RK1 w - - bm Qb4; id \"STS(v15.0) AT.005\"; c0 \"Qb4=10, Rfe1=3, Qg2=5, Qd4=9\";\n" +
            "r7/4bpkp/4p1p1/pq1nP3/Q2P3P/5PP1/3BNK2/R7 b - - bm Qd3; id \"STS(v15.0) AT.006\"; c0 \"Qd3=10, Qb8=9, Qb7=9, Qb6=9, Qb2=8\";\n" +
            "r4rk1/ppqb1ppp/4p3/2Qnb3/4N3/P1P3PP/1P3PB1/R1B2R1K b - - bm Bc6; id \"STS(v15.0) AT.007\"; c0 \"Bc6=10, Qd8=4\";\n" +
            "r4rk1/pp2ppbp/1qp2np1/n2P4/1Q2P3/2N2B2/PP3PPP/R1B2RK1 w - - bm Qa4; id \"STS(v15.0) AT.008\"; c0 \"Qa4=10, Qxe7=1, Qa3=2\";\n" +
            "r4rk1/p4ppp/1pn1pn2/2p5/2PPq3/P2QPN1P/1B3PP1/2R1R1K1 w - - bm Qd1; id \"STS(v15.0) AT.009\"; c0 \"Qd1=10, Red1=1, Qf1=3, Qe2=3, Qc3=1, Qb3=5\";\n" +
            "r4k2/6b1/5qP1/2p2pQ1/p2p4/8/1P1N1PP1/4R1K1 w - - bm Qg3; id \"STS(v15.0) AT.010\"; c0 \"Qg3=10, Qh5=6, Qf4=9, Qe3=6\";\n" +
            "r3r1k1/p1pp1p2/1pn2n1p/2P3p1/3P4/2P1B1P1/P1Q1qPNP/2RR2K1 b - - bm Qf3; id \"STS(v15.0) AT.011\"; c0 \"Qf3=10, Qxc2=7, Qh5=9, Qg4=8, Qc4=1\";\n" +
            "r3r1k1/bb3pp1/pp2pn1p/2pqN3/3P4/2P1BQ2/PP3PPP/R2R1BK1 w - - bm Qg3; id \"STS(v15.0) AT.012\"; c0 \"Qg3=10, Qxd5=1, Qh3=7, Qf4=4, Qe2=3\";\n" +
            "r3qb2/2nkpQ2/3p1ppp/2p5/4PP2/2BP2P1/2PN2K1/1R6 w - - bm Qg8; id \"STS(v15.0) AT.013\"; c0 \"Qg8=10, Qh7=2, Qc4=4, Qb3=8\";\n" +
            "r3kb1r/ppp1pppp/2n2n2/8/2PPq3/5N2/PP2QPPP/RNB1K2R w KQkq - bm Be3; id \"STS(v15.0) AT.014\"; c0 \"Be3=10, a3=6, Qxe4=9, Nc3=7, Kf1=2, Bd2=2\";\n" +
            "r3k2r/5p2/p3p2p/3pPb1P/1q6/8/1PPQNP2/2KR3R b kq - bm Qa4; id \"STS(v15.0) AT.015\"; c0 \"Qa4=10\";\n" +
            "r2r2k1/5pp1/2bPp3/p1N4p/1qQ5/2P2P2/BP4PP/1K1R4 b - - bm Qb8; id \"STS(v15.0) AT.016\"; c0 \"Qb8=10, Qxc4=9, Qb6=9\";\n" +
            "r2q1rk1/pp3ppp/1np1b3/8/3Q4/bPN1B1PP/P3PPB1/R4RK1 b - - bm Qc8; id \"STS(v15.0) AT.017\"; c0 \"Qc8=10, f6=4, Re8=4, Rc8=1, Qxd4=1, Qe7=4, Qc7=2, Bf5=2\";\n" +
            "r2q1rk1/pp3pp1/2p2n1p/8/2BQ2b1/4PN2/PP3PPP/2R2RK1 w - - bm Qf4; id \"STS(v15.0) AT.018\"; c0 \"Qf4=10\";\n" +
            "r2q1rk1/pp2ppbp/2n3p1/2P1Pb2/1n6/2N1BNP1/PP3PBP/R2Q1RK1 w - - bm Qa4; id \"STS(v15.0) AT.019\"; c0 \"Qa4=10, a3=3, Rc1=3, Qb3=8, Ne2=2, Nd4=3, Bg5=2\";\n" +
            "r2q1rk1/4bppp/p1p1pn2/4B3/8/2N3P1/PPP2P1P/R2Q1RK1 w - - bm Qe2; id \"STS(v15.0) AT.020\"; c0 \"Qe2=10, Re1=8, Qf3=9\";\n" +
            "r1r3k1/p4ppp/4p3/3p4/3B3P/6P1/P1q2PB1/R2Q2K1 w - - bm Qg4; id \"STS(v15.0) AT.021\"; c0 \"Qg4=10, Qh5=5, Qf3=5, Qe1=8\";\n" +
            "r1r2bk1/5p1p/5qp1/2np4/2pN1Q2/1P2P3/P3NPPP/R4RK1 b - - bm Qa6; id \"STS(v15.0) AT.022\"; c0 \"Qa6=10, Qd8=8\";\n" +
            "r1bq2nr/pp2bkpp/2pp4/Q7/3PP3/5N2/P4PPP/RNB1K2R w KQ - bm Qa4; id \"STS(v15.0) AT.023\"; c0 \"Qa4=10, Qd2=5, Qc3=7, Qb4=6, Qa3=9\";\n" +
            "r1bq1rk1/pp2ppbp/2n3p1/2P1P3/1n6/2N2NP1/PP3PBP/R1BQ1RK1 w - - bm Qa4; id \"STS(v15.0) AT.024\"; c0 \"Qa4=10, a3=9, Qe2=4, Bg5=8, Bf4=7, Be3=7\";\n" +
            "r1bq1rk1/1p2bppp/p1p1p3/4P3/8/7N/PPP2PPP/R1BQR1K1 w - - bm Qg4; id \"STS(v15.0) AT.025\"; c0 \"Qg4=10, Qh5=6, Qf3=8, Qe2=5, Bf4=2, Be3=1\";\n" +
            "r1bq1rk1/1p2bppp/p1n1pn2/2P5/P1B5/2N1PN2/1P3PPP/R1BQ1RK1 w - - bm Bd2; id \"STS(v15.0) AT.026\"; c0 \"Bd2=10, e4=3, b3=7, Qe2=5, Qc2=7, Ne2=2\";\n" +
            "r1b3k1/pp2b1pp/2p1pn2/8/P1p1Pq2/2N5/1P2BP1P/R1Q2RK1 b - - bm Qe5; id \"STS(v15.0) AT.027\"; c0 \"Qe5=10, Qd6=3\";\n" +
            "r1b2rk1/pp2p1b1/nqpp1npp/5p2/2PP1B2/1QN2NP1/PP2PPBP/R2R2K1 w - - bm Qc2; id \"STS(v15.0) AT.028\"; c0 \"Qc2=10, Qxb6=3, Qa4=8, Qa3=9, Ne1=6, Bd2=9, Bc1=5\";\n" +
            "r1b2rk1/1p1nq1pp/p1pb1p2/8/4Q3/1P2PN2/PB2BPPP/1R2R1K1 w - - bm Qh4; id \"STS(v15.0) AT.029\"; c0 \"Qh4=10, Qxe7=7, Qc4+=9, Qc2=9, Nd2=8, Bc4+=9\";\n" +
            "r1b2k1r/pp4qp/4p2Q/PPbp4/8/2PB4/5PPP/RN3RK1 w - - bm Qf4+; id \"STS(v15.0) AT.030\"; c0 \"Qf4+=10, Qh5=9, Qh3=9\";\n" +
            "r1b2k1r/pp3ppp/4p3/1B6/1b6/2q5/P1Q2PPP/R1B2RK1 w - - bm Qa4; id \"STS(v15.0) AT.031\"; c0 \"Qa4=10, Qe2=6, Qb1=3\";\n" +
            "r1b1k2r/p2n1q2/3B1nQp/3p4/2pP4/2P1PN2/P4PPP/4RRK1 w kq - bm Qc2; id \"STS(v15.0) AT.032\"; c0 \"Qc2=10, Nh4=8\";\n" +
            "r1b1k1nr/pppq1pbp/6p1/4n3/Q1p5/4BNP1/PP1NPPBP/R3K2R w KQkq - bm Qa3; id \"STS(v15.0) AT.033\"; c0 \"Qa3=10, Qxd7+=6, Qc2=4, Qb4=4, Qa5=4\";\n" +
            "r1b1k1nr/1p1pqppp/p1nQ4/4p3/4P3/8/PPP2PPP/RNB1KB1R w KQkq - bm Qd1; id \"STS(v15.0) AT.034\"; c0 \"Qd1=10, Qxe7+=7, Qd3=7, Qd2=6, Qa3=3\";\n" +
            "b1r4r/4bpk1/1p1p1np1/p1q1p3/4P2p/PPN1QB1P/1BP2PP1/3RR1K1 w - - bm Qe2; id \"STS(v15.0) AT.035\"; c0 \"Qe2=10, a4=3, Re2=2, Qd3=5\";\n" +
            "8/r1q2p1k/1Q1p1p1p/p3pP2/4P1R1/1P5P/2P3PK/8 w - - bm Qe3; id \"STS(v15.0) AT.036\"; c0 \"Qe3=10, Qg1=2, Qf2=5, Qb5=5\";\n" +
            "8/6p1/qp2p2k/pQ1nPp2/P2P1Pn1/5NP1/1B2K3/8 b - - bm Qa8; id \"STS(v15.0) AT.037\"; c0 \"Qa8=10, Qc8=9, Qb7=7\";\n" +
            "8/3r2k1/p1rPp1p1/1q5p/p3NPnP/6PK/1P1RQ3/3R4 w - - bm Qe1; id \"STS(v15.0) AT.038\"; c0 \"Qe1=10, Re1=5, Qd3=2, Ng5=2, Kg2=3\";\n" +
            "8/1b6/1p3pk1/1Prp1pp1/3Nq3/4P3/5PPP/1Q1R2K1 w - - bm Qa1; id \"STS(v15.0) AT.039\"; c0 \"Qa1=10, g3=3, Qb3=5, Qb2=3\";\n" +
            "6rk/5r2/1p1npq1p/1Pp1p2p/P1PbP2Q/3R1PP1/B6K/3RB3 b - - bm Qg6; id \"STS(v15.0) AT.040\"; c0 \"Qg6=10, Qxh4+=3, Qg5=3, Qf4=6, Bg1+=3\";\n" +
            "6k1/ppp1rppp/7q/2Npr3/P4Q2/3B2P1/1PP4P/5RK1 b - - bm Qd6; id \"STS(v15.0) AT.041\"; c0 \"Qd6=10\";\n" +
            "6k1/pb1r1p1p/1b1r2pP/p3p1q1/4P3/PN1B1RN1/1RPQ4/7K b - - bm Qd8; id \"STS(v15.0) AT.042\"; c0 \"Qd8=10, Qh4+=3, Qg4=7, Qe7=5\";\n" +
            "6k1/5p1p/1pp1n1p1/p3P3/2P1P3/1PNq1Q1P/P5P1/7K b - - bm Qd4; id \"STS(v15.0) AT.043\"; c0 \"Qd4=10, Qd2=6, Qc2=2\";\n" +
            "6k1/3r3p/1pb2pp1/p1qpn3/P2Q1N2/1P2P2P/5PP1/1B1R2K1 w - - bm Qd2; id \"STS(v15.0) AT.044\"; c0 \"Qd2=10, h4=7, f3=5, Qxc5=9, Kf1=4, Ba2=4\";\n" +
            "6k1/1r2bppp/p3pn2/Pp2q3/2pN4/5P1P/1PP3P1/3RQRK1 b - - bm Qc5; id \"STS(v15.0) AT.045\"; c0 \"Qc5=10\";\n" +
            "6k1/1p3pp1/p1rpq2p/3Q4/P1P2P2/1P6/3R1KPP/8 w - - bm Qd4; id \"STS(v15.0) AT.046\"; c0 \"Qd4=10, h3=9, g3=9, a5=8, Rd3=9, Qd3=9, Kf3=9\";\n" +
            "6k1/1p3pp1/p1rpq2p/3Q4/2P5/1P6/P4PPP/3R2K1 w - - bm Qf3; id \"STS(v15.0) AT.047\"; c0 \"Qf3=10, a4=1, Qh5=3, Qd3=9, Qd2=2, Qa5=2, Kf1=3\";\n" +
            "5rk1/1p1r1pp1/2pNp2p/p1PnP2P/3P4/1Pq2Q2/P4P1K/3RR3 w - - bm Qe2; id \"STS(v15.0) AT.048\"; c0 \"Qe2=10\";\n" +
            "5rb1/p1r1p1kp/5pp1/p2n4/1qBPR2P/1P4P1/P2Q1PK1/2R1N3 w - - bm Qe2; id \"STS(v15.0) AT.049\"; c0 \"Qe2=10, Qf4+=9\";\n" +
            "5r2/6k1/Pb1pBn1p/p2Pq3/5pPP/5K2/4Q3/3N3R b - - bm Be3; id \"STS(v15.0) AT.050\"; c0 \"Be3=10, h5=4, Qxe2+=8, Qd4=7\";\n" +
            "5r1r/4k2p/p3qp2/2b1pQ1N/1p1p4/8/PPP3P1/1K2RR2 w - - bm Qe4; id \"STS(v15.0) AT.051\"; c0 \"Qe4=10, Qf3=9\";\n" +
            "5r1k/1r1q1p2/pp3Pp1/7p/3Q4/1P4P1/1P3P2/2R1R1K1 w - - bm Qf4; id \"STS(v15.0) AT.052\"; c0 \"Qf4=10, Qe4=6, Qc3=3\";\n" +
            "5b1k/1p1r3p/p1p5/3qpP2/4Q3/P5P1/2N2P1P/1R4K1 w - - bm Qe2; id \"STS(v15.0) AT.053\"; c0 \"Qe2=10\";\n" +
            "4rrk1/1p3pb1/2p1pqp1/p2n3p/P2P4/2P2QBP/1P2RPP1/1B1R2K1 w - - bm Qe4; id \"STS(v15.0) AT.054\"; c0 \"Qe4=10, Rd3=3, Qxf6=8, Qd3=2, Be4=2, Bd6=2\";\n" +
            "4rrk1/1p1n3p/3p1np1/p1pP2N1/1q3P2/1P3B1P/P2Q1RP1/1R4K1 w - - bm Qc1; id \"STS(v15.0) AT.055\"; c0 \"Qc1=10, g4=3, Qd3=6, Qd1=2, Qb2=2, Ne6=3\";\n" +
            "4rk2/5pp1/p3n2p/1prNR3/2q2P1P/6P1/PP2Q3/1K2R3 w - - bm Qd2; id \"STS(v15.0) AT.056\"; c0 \"Qd2=10\";\n" +
            "4rbk1/1r3p2/ppp3P1/nq4Bp/3pP3/Pb1Q1N2/1P4P1/1BR1R1K1 w - - bm Qd2; id \"STS(v15.0) AT.057\"; c0 \"Qd2=10\";\n" +
            "4r2k/3qbp2/p3bp1p/8/8/P1RQ4/1PP2PPP/2K1R3 b - - bm Qa4; id \"STS(v15.0) AT.058\"; c0 \"Qa4=10\";\n" +
            "4r1k1/p4pp1/4r1b1/q1pp2P1/5B1P/8/PPPQ1P2/1K1R2R1 b - - bm Qb5; id \"STS(v15.0) AT.059\"; c0 \"Qb5=10, Qxd2=1, Qa6=3, Qa4=4\";\n" +
            "4r1k1/4rppn/pNp5/3p1P2/PP2nqP1/4Q2P/2P1R1B1/4R1K1 b - - bm Qd6; id \"STS(v15.0) AT.060\"; c0 \"Qd6=10, Qc7=9, Qb8=9\";\n" +
            "4r1k1/3b1pp1/pB6/P2r3p/1q1P1P2/2Q5/1P2p1PP/2R1R2K b - - bm Qd6; id \"STS(v15.0) AT.061\"; c0 \"Qd6=10, Rb5=3, Qe7=7\";\n" +
            "4b2k/p5bp/Pp3q2/2p1p3/2P1B3/1P3QP1/3r1N1P/4R1K1 w - - bm Bf5; id \"STS(v15.0) AT.062\"; c0 \"Bf5=10, Re3=2, Re2=4, Qxf6=2, Qf5=1\";\n" +
            "3rrbk1/5ppp/b1q5/p1p2P2/p1P5/4BQN1/PP1R2PP/2R3K1 b - - bm Qf6; id \"STS(v15.0) AT.063\"; c0 \"Qf6=10, Rc8=4, Qc8=7, Qc7=6, Qb6=7\";\n" +
            "3rr3/1p3pkp/3qb2N/p2p1pPB/2nR4/2P3Q1/PP5P/4R2K w - - bm Qf2; id \"STS(v15.0) AT.064\"; c0 \"Qf2=10, Qh4=5, Qh3=5, Qg2=4, Qg1=5, Qf3=3\";\n" +
            "3r3k/1pq2bp1/p4p2/Q1p1pBb1/2P1P3/1PB4P/P4PK1/4R3 b - - bm Qd6; id \"STS(v15.0) AT.065\"; c0 \"Qd6=10, b6=2, Qxa5=3, Qe7=5\";\n" +
            "3r2rk/1p3Rpb/2nBp2p/p1P5/1pB1q3/1P2Q2P/5RPK/8 w - - bm Qg3; id \"STS(v15.0) AT.066\"; c0 \"Qg3=10, Re2=6, Qc1=3\";\n" +
            "3r2k1/1pbr1p2/p1q1p1pp/2P2n2/1PNPQP2/2B3P1/7P/3RR1K1 b - - bm Rd5; id \"STS(v15.0) AT.067\"; c0 \"Rd5=10\";\n" +
            "3r1rk1/p1qb2pp/1p6/5p2/2PNpQn1/1P4P1/P4PBP/R3R1K1 b - - bm Qc5; id \"STS(v15.0) AT.068\"; c0 \"Qc5=10, Qxf4=3, Qc8=1, Ne5=8\";\n" +
            "3r1rk1/1pp2ppp/2np2q1/p4Q2/2B1PP2/7R/PPP2RPP/6K1 w - - bm Qb5; id \"STS(v15.0) AT.069\"; c0 \"Qb5=10, Qxg6=5, Qd5=8\";\n" +
            "3r1r2/3q2bk/6pp/pQ3p2/P1p4P/2PnBNP1/1P3PK1/R2R4 b - - bm Qe6; id \"STS(v15.0) AT.070\"; c0 \"Qe6=10\";\n" +
            "3qr2k/3Q3p/p2p2pB/3P1p2/4p3/PPb3P1/2P1PP1P/5BK1 w - - bm Qc6; id \"STS(v15.0) AT.071\"; c0 \"Qc6=10, Qxd8=1, Qb7=5, Qa7=5, Qa4=9\";\n" +
            "2rrk3/pp1bppbp/2n3p1/q7/2BPPN2/4BP1P/P2Q2P1/2RR2K1 w - - bm Qf2; id \"STS(v15.0) AT.072\"; c0 \"Qf2=10, Qe2=5, Qd3=3, Qb2=9\";\n" +
            "2rr2k1/pp2pp1p/5bp1/P7/1B1Pq3/4P1P1/5PQ1/R1R3K1 b - - bm Qe6; id \"STS(v15.0) AT.073\"; c0 \"Qe6=10, Rxc1+=2, Qxg2+=6, Qg4=1, Qf5=5\";\n" +
            "2rr2k1/pp1bppbp/2n3p1/q7/2BPPN2/4BP2/P2Q2PP/2RR2K1 w - - bm Qb2; id \"STS(v15.0) AT.074\"; c0 \"Qb2=10, Rb1=3, Qxa5=9, Kf2=3\";\n" +
            "2rr2k1/4bppp/p3p3/4P3/2q1QBPn/2N5/PP3R1P/5R1K b - - bm Rd4; id \"STS(v15.0) AT.075\"; c0 \"Rd4=10, h6=6, Rf8=4, Rd7=7, Rd3=6, Qxe4+=5, Ng6=8\";\n" +
            "2rr2k1/4bppp/p3p1n1/4P3/2q1Q1P1/2N3B1/PP3R1P/5R1K b - - bm Rd4; id \"STS(v15.0) AT.076\"; c0 \"Rd4=10, Rf8=6, Rd3=7, Qxe4+=6\";\n" +
            "2rr2k1/4bppp/4p3/4P3/2q1QBPn/2N5/PP3R1P/5R1K b - - bm Qa6; id \"STS(v15.0) AT.077\"; c0 \"Qa6=10, Rd4=4\";\n" +
            "2rq2k1/R5pp/8/Q2pnp2/8/1P2P1P1/7P/5B1K w - - bm Qa1; id \"STS(v15.0) AT.078\"; c0 \"Qa1=10, Qd2=9, Qb5=4\";\n" +
            "2r3k1/pp3p2/1n2rPp1/4P2p/4R3/P1q4P/3Q2P1/2B1R1K1 w - - bm Qg5; id \"STS(v15.0) AT.079\"; c0 \"Qg5=10, Qh6=6, Qf4=5, Qd6=4\";\n" +
            "2r3k1/1p1rppbp/5np1/p4PB1/P1qN4/2PQ4/1P4PP/4RR1K w - - bm Qh3; id \"STS(v15.0) AT.080\"; c0 \"Qh3=10\";\n" +
            "2r3k1/1R3p2/5rp1/p2p3p/3P1B1P/2p1PP2/P2nq1P1/2RQ2K1 b - - bm Qc4; id \"STS(v15.0) AT.081\"; c0 \"Qc4=10, Qd3=4, Qa6=9\";\n" +
            "2r2rk1/1p2bpp1/p2p3p/P2Pp2n/4P3/1PqQB2P/3N1PP1/R4RK1 w - - bm Qb1; id \"STS(v15.0) AT.082\"; c0 \"Qb1=10, Qxc3=5, Qe2=5\";\n" +
            "2r2qkr/1p1b3p/p1n1p1pQ/P2pPp2/8/1PBR1N1P/2P2PP1/5RK1 w - - bm Qd2; id \"STS(v15.0) AT.083\"; c0 \"Qd2=10, Qxf8+=2, Qg5=5, Qf4=5, Qe3=8, Qc1=4\";\n" +
            "2r1r1k1/q4ppp/b2b1n2/p2p4/N2Q4/P3PPP1/1P4BP/R1BR3K b - - bm Qb8; id \"STS(v15.0) AT.084\"; c0 \"Qb8=10, Qe7=9, Qc7=5\";\n" +
            "2r1r1k1/1b3pp1/p3pn1p/P1b3q1/1pN5/4P1Q1/1P1BBPPP/1R3RK1 b - - bm Qf5; id \"STS(v15.0) AT.085\"; c0 \"Qf5=10, Qxg3=3, Ne4=2\";\n" +
            "2r1kb1r/5p2/p1npb2p/1p1NppqQ/4P3/N1PB4/PP3PPP/R3R1K1 w k - bm Qe2; id \"STS(v15.0) AT.086\"; c0 \"Qe2=10, Qf3=3, Qd1=8, Nc2=6\";\n" +
            "2r1k2r/1b2bppp/p3pn2/8/N2N1q1P/P4P2/1PPQ2P1/2KR1B1R b k - bm Qb8; id \"STS(v15.0) AT.087\"; c0 \"Qb8=10, Qg3=7, Qe5=5, Qd6=6, Qc7=6\";\n" +
            "1r6/8/1ppp1qkp/5pn1/2P2B2/2Q3R1/1P3PP1/6K1 w - - bm Qd2; id \"STS(v15.0) AT.088\"; c0 \"Qd2=10, Rd3=9, Qe3=9\";\n" +
            "1r6/5pk1/Q2q2p1/8/Pp1r3p/3B3P/2Pb2P1/1R1R3K b - - bm Qc5; id \"STS(v15.0) AT.089\"; c0 \"Qc5=10, b3=1, Rb6=3, Qxa6=2, Qc7=7, Bf4=1, Be3=1, Bc3=1\";\n" +
            "1r4k1/3b2b1/3pNp1p/3Pn1pB/pr2Pp1P/2Nq4/RPR2PP1/3Q2K1 w - - bm Rd2; id \"STS(v15.0) AT.090\"; c0 \"Rd2=10, Qc1=4\";\n" +
            "1r4k1/1brp1pp1/1p3n1p/2q1p3/2P1P3/1PNBQ3/P3RP1P/3R3K w - - bm Qh3; id \"STS(v15.0) AT.091\"; c0 \"Qh3=10, Red2=4, Qf3=8, Bc2=6, Bb1=5\";\n" +
            "1r3r1k/8/3p2p1/p1pQq1pp/N1P1P3/Pp3PP1/1Pn2K1P/1R3R2 b - - bm Qf6; id \"STS(v15.0) AT.092\"; c0 \"Qf6=10, g4=2, Qxd5=3\";\n" +
            "1r2kbr1/p4p2/b1ppp1p1/4qPP1/4P2P/2N3Q1/PPP5/2KR2RB w - - bm Qf2; id \"STS(v15.0) AT.093\"; c0 \"Qf2=10, Qe3=6, Bf3=9\";\n" +
            "1r1r2k1/5pp1/p2b2bp/1q1Pp3/1p6/1P2P1P1/R2NQPB1/2R3K1 w - - bm Qg4; id \"STS(v15.0) AT.094\"; c0 \"Qg4=10, Qd1=8, Nc4=9\";\n" +
            "1r1r1nk1/3q3p/1p2p1p1/pQp1Pp2/P2P4/2P3NP/5PP1/1R1R2K1 w - - bm Qb3; id \"STS(v15.0) AT.095\"; c0 \"Qb3=10, f4=6, f3=4, Qxd7=5, Qc4=8, Ne2=6\";\n" +
            "1k6/1r3p2/3p1b1p/pprPpPpP/5qP1/PRPB1Q2/1P6/1K2R3 w - - bm Qg2; id \"STS(v15.0) AT.096\"; c0 \"Qg2=10, Qh3=2, Qe2=4\";\n" +
            "1k1rr2b/2q2p2/p4p1P/1p3p2/PR2bQ2/3B4/1PP5/1K1RN3 w - - bm Qc1; id \"STS(v15.0) AT.097\"; c0 \"Qc1=10, Qf2=8, Qf1=8, Qd2=8\";\n" +
            "1k1rqb1r/ppp5/2n4n/3p2p1/3P3p/2PB4/PP2Q1PP/RN2BRK1 b - - bm Qd7; id \"STS(v15.0) AT.098\"; c0 \"Qd7=10\";\n" +
            "1k1r3r/1b2b3/p1p1Bp1p/6q1/N2pP3/1P6/P1PQ2P1/1K1R1R2 w - - bm Qe2; id \"STS(v15.0) AT.099\"; c0 \"Qe2=10, Qf2=5, Qe1=4, Qd3=8, Nb6=1, Nb2=2, Bc4=1\";\n" +
            "1R6/2r2bkp/p2q1rp1/4Np2/2pPp2P/Q5P1/PP1R1P1K/8 w - - bm Qe3; id \"STS(v15.0) AT.100\"; c0 \"Qe3=10\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    