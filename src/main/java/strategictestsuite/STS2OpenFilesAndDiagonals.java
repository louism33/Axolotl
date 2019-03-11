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
public class STS2OpenFilesAndDiagonals {


    private static final int timeLimit = 10_000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        EngineSpecifications.INFO = true;

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

    public STS2OpenFilesAndDiagonals(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getBoardFen());
        System.out.println(EPDObject.getBoard());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.INFO = false;
        int move = EngineBetter.searchFixedTime(EPDObject.getBoard(), timeLimit);

        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }

    private static final String positions = "" +
            "1b1r4/3rkp2/p3p2p/4q3/P5P1/2RBP3/P1Q4P/1R3K2 b - - bm Ba7; c0 \"Ba7=10, Qf6+=3, a5=3, h5=5\"; id \"STS(v2.2) Open Files and Diagonals.001\";\n" +
            "1bq3rk/R6p/5n1P/1N1p4/1PnP2p1/6P1/5B2/2Q2BK1 w - - bm Re7; c0 \"Re7=10, Ra2=5, Rg7=2\"; id \"STS(v2.2) Open Files and Diagonals.002\";\n" +
            "1k1r3r/1p1b1Q1p/p7/q3p3/4p3/2P1N3/P4PPP/R4RK1 w - - bm Rad1; c0 \"Rad1=10, Qf6=7, Rfd1=7, a4=2\"; id \"STS(v2.2) Open Files and Diagonals.003\";\n" +
            "1Q6/1b4pk/2q2b1p/1p1ppP2/1Pp5/2P2P1P/2BB2P1/6K1 b - - bm Qa6; c0 \"Qa6=10, Bc8=5, Qd7=7\"; id \"STS(v2.2) Open Files and Diagonals.004\";\n" +
            "1qrr3k/1p2bp1p/1n2p1pP/p2pP3/P4B2/1PPB2P1/2R1QP2/3R2K1 w - - bm Bb5; c0 \"Bb5=10, Qe1=2, Qe3=2\"; id \"STS(v2.2) Open Files and Diagonals.005\";\n" +
            "1r1n1rk1/3qp2p/P2p2p1/1p6/5pP1/1p3P1P/5PB1/R1QR2K1 w - - bm Bf1; c0 \"Bf1=10, Qb2=7, Qc3=7, Qd2=6\"; id \"STS(v2.2) Open Files and Diagonals.006\";\n" +
            "1r1n2k1/5r1p/P2qp1p1/3p4/1p3pP1/1Q3P1P/R4P2/2R2BK1 w - - bm Rac2; c0 \"Rac2=10, Kg2=5, Qa4=4\"; id \"STS(v2.2) Open Files and Diagonals.007\";\n" +
            "1r1q1nk1/p3bpp1/b1p1p2p/4P3/1P2NB2/P4N2/5PPP/2Q1R1K1 w - - bm Be3; c0 \"Be3=10, Nd6=5, Rd1=7, h3=6\"; id \"STS(v2.2) Open Files and Diagonals.008\";\n" +
            "1r1q4/5p2/2p4k/1n2p1pP/4P3/P4BR1/3Q1PKP/8 w - - bm Qc1; c0 \"Qc1=10, Qa2=5, Qe3=6\"; id \"STS(v2.2) Open Files and Diagonals.009\";\n" +
            "1r1qbr1k/4bp1p/p3p2Q/3pP3/2pP4/P1N1PN2/1PR2RP1/6K1 b - - bm Rg8; c0 \"Rg8=10, Bc6=1, Bd7=4\"; id \"STS(v2.2) Open Files and Diagonals.010\";\n" +
            "1r3k2/8/R7/4pPP1/P1p5/1nP5/R5P1/3rB1K1 w - - bm Rh6; c0 \"Rh6=10, Kf1=6, Rf6+=6\"; id \"STS(v2.2) Open Files and Diagonals.011\";\n" +
            "1r3rk1/2b3pp/p4p2/2p5/P1Np4/1P1R2P1/1P3P1P/2R3K1 b - - bm Rfe8; c0 \"Rfe8=10, Rfc8=7, Rfd8=8\"; id \"STS(v2.2) Open Files and Diagonals.012\";\n" +
            "1r5k/p7/3pQ1p1/1Np5/2P3P1/7P/1PK5/7q b - - bm Qf1; c0 \"Qf1=10, Qg2+=2, Qh2+=2\"; id \"STS(v2.2) Open Files and Diagonals.013\";\n" +
            "1r6/4q3/2p2p1k/4p1pP/P2nP2P/5BR1/5PK1/2Q5 w - - bm Bg4; c0 \"Bg4=10, Bd1=6, a5=8\"; id \"STS(v2.2) Open Files and Diagonals.014\";\n" +
            "1r6/7k/1p4r1/1P2p3/2P1p2P/2RbB3/6P1/2R4K w - - bm Ra1; c0 \"Ra1=10, Kg1=7, Kh2=8\"; id \"STS(v2.2) Open Files and Diagonals.015\";\n" +
            "1r6/8/1r5p/p2pk3/4p3/2P4P/RP2B1n1/1RK5 b - - bm Rf6; c0 \"Rf6=10, Rf8=8, Rg6=6\"; id \"STS(v2.2) Open Files and Diagonals.016\";\n" +
            "1rbr2k1/pp3pp1/q6p/4P3/3p1P2/6P1/P2QP1BP/R1R3K1 w - - bm Be4; c0 \"Be4=10, Bf3=4, Qd3=5\"; id \"STS(v2.2) Open Files and Diagonals.017\";\n" +
            "2b1r1k1/2r5/p3pbp1/5p2/PN5P/1Pp2BP1/4PP2/2R2RK1 w - - bm Rfd1; c0 \"Rfd1=10\"; id \"STS(v2.2) Open Files and Diagonals.018\";\n" +
            "2r1k3/p5pp/1p2p3/7P/P2B2Q1/1bP5/R4PPK/4q3 w - - bm Re2; c0 \"Re2=10, Qxg7=4, Rb2=4\"; id \"STS(v2.2) Open Files and Diagonals.019\";\n" +
            "2r1q1k1/2p2rbp/p2p2p1/Q2P4/b3P3/3p2P1/3B1PBP/2R1R1K1 w - - bm Bh3; c0 \"Bh3=10, Bf1=7, e5=8\"; id \"STS(v2.2) Open Files and Diagonals.020\";\n" +
            "2r1q2k/7p/p1np1P1P/8/1pP2R2/8/PP1Q4/R1KN2r1 b - - bm Qg6; c0 \"Qg6=10, Ne5=4, Qe5=2\"; id \"STS(v2.2) Open Files and Diagonals.021\";\n" +
            "2r1q3/p1p1nk1p/4p2p/2R1Pp2/1P1Q4/P3N1P1/5P2/6K1 w - - bm Qh4; c0 \"Qh4=10, Rc1=6, Rc2=9, Rc4=6\"; id \"STS(v2.2) Open Files and Diagonals.022\";\n" +
            "2r2k2/R4bpp/2p2p2/1pN5/1n3PP1/1P2P2P/8/5BK1 w - - bm Bg2; c0 \"Bg2=10, e4=6\"; id \"STS(v2.2) Open Files and Diagonals.023\";\n" +
            "2r2rk1/1p1nbppp/p2p4/B2Pp1PP/5P2/1P6/P1P5/1K1R1B1R w - - bm Bh3; c0 \"Bh3=10, Bd2=5, Re1=6\"; id \"STS(v2.2) Open Files and Diagonals.024\";\n" +
            "2r2rk1/4q1pp/p7/1pb1Pp2/5P2/1P1QB3/b3N1PP/2R1K2R b K - bm Rfd8; c0 \"Rfd8=10, Bxe3=5, Kh8=3\"; id \"STS(v2.2) Open Files and Diagonals.025\";\n" +
            "2r3k1/1b1n1npp/1pq2p2/8/1P1QP3/6P1/1B1NBP1P/3R2K1 w - - bm Rc1; c0 \"Rc1=10, Bg4=7, f3=7, f4=5\"; id \"STS(v2.2) Open Files and Diagonals.026\";\n" +
            "2r3n1/p1p3kp/1q2p2p/4Pp2/1P4N1/P5P1/1Q3P2/2R3K1 w - - bm Qd2; c0 \"Qd2=10, Ne3=9, Nf6=5\"; id \"STS(v2.2) Open Files and Diagonals.027\";\n" +
            "2rq2k1/1p3pb1/1n4pp/pP2p3/P1b1P3/2N4P/2B1NPP1/R1Q3K1 b - - bm Bf8; c0 \"Bg7f8=10, Bg7f6=8, Qd8d6=7\"; id \"STS(v2.2) Open Files and Diagonals.028\";\n" +
            "2rq2k1/4rpp1/p3p3/P2n2pP/2pPR3/2P3B1/2Q2PP1/R5K1 b - - bm Rb7; c0 \"Rb7=10, Ra7=4, Rd7=3\"; id \"STS(v2.2) Open Files and Diagonals.029\";\n" +
            "2rr1b2/3q3k/p4n1p/1p1p2p1/2nNp3/P1N2PQ1/1PP3PP/R1BR2K1 b - - bm Bd6; c0 \"Bd6=10, Bc5=7, Bg7=5\"; id \"STS(v2.2) Open Files and Diagonals.030\";\n" +
            "3b2k1/1p1b4/4p2B/1n1p1p2/pN6/P2P4/1PR3PP/7K w - - bm Be3; c0 \"Be3=10, Bf4=7, Re2=7\"; id \"STS(v2.2) Open Files and Diagonals.031\";\n" +
            "3q2k1/5rpp/r1pPp3/1bQn4/p2B4/4P1P1/1P1R2BP/R5K1 w - - bm Bh3; c0 \"Bh3=10, Rc1=8, Rc2=7\"; id \"STS(v2.2) Open Files and Diagonals.032\";\n" +
            "3Q4/5pk1/4p1p1/3pb2p/P3q2P/1r4P1/2R2P1K/2R5 b - - bm Bf4; c0 \"Bf4=10, Ra3=3, Rd3=4\"; id \"STS(v2.2) Open Files and Diagonals.033\";\n" +
            "3q4/7k/3ppp2/p3n2B/P1r1P2P/8/1PPQ4/1K3R2 w - - bm Rg1; c0 \"Rg1=10, Qe3=7, Qg2=5\"; id \"STS(v2.2) Open Files and Diagonals.034\";\n" +
            "3r1k2/R4bpp/2p2p2/1pN5/1n3PP1/1P2P2P/6B1/6K1 w - - bm Be4; c0 \"Be4=10, Bf3=2, g5=3\"; id \"STS(v2.2) Open Files and Diagonals.035\";\n" +
            "3r1r2/p5k1/1pnpP2p/6p1/2P3P1/4P3/P1B1K1P1/3R3R w - - bm Ba4; c0 \"Ba4=10, Rdf1=6, Rhf1=7\"; id \"STS(v2.2) Open Files and Diagonals.036\";\n" +
            "3r2k1/4q1pp/p7/1p2Pp2/5P2/1P2Q3/b5PP/2N1K2R b K - bm Bb1; c0 \"Bb1=10\"; id \"STS(v2.2) Open Files and Diagonals.037\";\n" +
            "3r2k1/pp1q1ppp/4rnn1/3p4/P1pP4/2P1P2P/1BQN1PP1/RR4K1 w - - bm Ba3; c0 \"Ba3=10, Qd1=4, Qf5=4\"; id \"STS(v2.2) Open Files and Diagonals.038\";\n" +
            "3r2k1/R7/1p3np1/1P1b4/1p6/5PqP/2Q3P1/3R2K1 b - - bm Re8; c0 \"Re8=10, Rf8=4\"; id \"STS(v2.2) Open Files and Diagonals.039\";\n" +
            "3r3k/1p2R2p/pP4p1/8/2rp1p2/5P2/2P3PP/4R1K1 w - - bm Rc7; c0 \"Rc7=10\"; id \"STS(v2.2) Open Files and Diagonals.040\";\n" +
            "3r4/3pkpp1/4p3/2p3q1/6r1/1P6/P5B1/2R1RQK1 b - - bm Rh8; c0 \"Rh8=10, Ra8=1, f5=1\"; id \"STS(v2.2) Open Files and Diagonals.041\";\n" +
            "3r4/3q2bk/3rp1p1/1p5p/4QP2/R1Pp2P1/P2B1R1P/6K1 b - - bm Rc8; c0 \"Rc8=10, Rc6=6, Re8=6\"; id \"STS(v2.2) Open Files and Diagonals.042\";\n" +
            "3r4/p2rppk1/2R3p1/4q3/3b4/PP4P1/2QRNP2/5K2 b - - bm Qh5; c0 \"Qh5=10, Bb6=2, Qd5=2\"; id \"STS(v2.2) Open Files and Diagonals.043\";\n" +
            "3rr1k1/1p2bppp/p1p5/P2P3q/2B1PPb1/1P1R4/3B1QP1/4R1K1 w - - bm Bc3; c0 \"Bc3=10, b4=8, f5=4\"; id \"STS(v2.2) Open Files and Diagonals.044\";\n" +
            "3rr1k1/pp2q1bp/n1p1P1p1/5p2/P2N1Pb1/1BP5/6PP/R1B1QRK1 w - - bm Ba3; c0 \"Ba3=10, Ra2=5, h3=5\"; id \"STS(v2.2) Open Files and Diagonals.045\";\n" +
            "4b2k/1q2bprp/pr6/3pP3/2pP4/P4N2/1P3RPQ/3N1RK1 b - - bm Bd7; c0 \"Bd7=10, Bd8=8, Rg8=1\"; id \"STS(v2.2) Open Files and Diagonals.046\";\n" +
            "4k2r/p2n3p/4q1pQ/1p2b2P/8/P1P2R2/1P4P1/K4R2 w - - bm Rd1; c0 \"Rd1=10, Re1=9, Re3=5\"; id \"STS(v2.2) Open Files and Diagonals.047\";\n" +
            "4qrk1/pb4p1/2p1p2p/PpP1Pr2/1P2QP1R/2B3P1/6R1/6K1 w - - bm Rd2; c0 \"Rd2=10, Be1=9, Ra2=9\"; id \"STS(v2.2) Open Files and Diagonals.048\";\n" +
            "4r1k1/3q1ppb/2p5/2Pp1P1p/p2P3P/P3P1PB/5Q1K/4R3 w - - bm Rb1; c0 \"Rb1=10, Qf4=5, Re2=2, Re2=5\"; id \"STS(v2.2) Open Files and Diagonals.049\";\n" +
            "4r1k1/pbq2rpp/1p2p3/4P2P/P1p4Q/1nP1B3/R1B2PP1/1R4K1 w - - bm Bg6; c0 \"Bg6=10, Qg5=1, Rd1=3, a5=4\"; id \"STS(v2.2) Open Files and Diagonals.050\";\n" +
            "4r1k1/R5p1/4b2p/1p2P3/7P/P1nP4/6PK/4R3 w - - bm Rc1; c0 \"Rc1=10, Ra6=7, d4=6\"; id \"STS(v2.2) Open Files and Diagonals.051\";\n" +
            "4r2k/1pp1n1pp/pb3r2/6Nq/P2P4/2PQ2P1/1R3RKP/2B5 w - - bm Rbe2; c0 \"Rbe2=10, Rf3=4, Rxf6=3, a5=3, h3=4, h4=3\"; id \"STS(v2.2) Open Files and Diagonals.052\";\n" +
            "4rrk1/1b3pp1/1q3b1p/p2p1P2/3N4/2P3N1/1P3QPP/1R3R1K b - - bm Ba6; c0 \"Ba6=10\"; id \"STS(v2.2) Open Files and Diagonals.053\";\n" +
            "5b2/1p1q3n/pB1p2k1/P4N1p/6pP/4R3/6P1/5RK1 w - - bm Bd4; c0 \"Bd4=10\"; id \"STS(v2.2) Open Files and Diagonals.054\";\n" +
            "5k2/ppQbbp1p/8/5p2/P7/1P6/4KPPP/q4B1R b - - bm Bb4; c0 \"Bb4=10, Qa2+=2, Qb2+=2\"; id \"STS(v2.2) Open Files and Diagonals.055\";\n" +
            "5r1k/6p1/p1n1pr1p/2P1p2q/P5RN/4Q2P/5PP1/2R4K w - - bm Rb1; c0 \"Rb1=10, Qd3=6, Rd1=7\"; id \"STS(v2.2) Open Files and Diagonals.056\";\n" +
            "5rk1/p3qpb1/1p5p/5Q2/P7/1B2P3/1p3PPP/5RK1 b - - bm Rd8; c0 \"Rd8=10, Qb4=6, Qe5=5\"; id \"STS(v2.2) Open Files and Diagonals.057\";\n" +
            "5rk1/p7/1n1q1p2/1Prp1pNp/8/5NPP/P2Q1PB1/5RK1 w - - bm Re1; c0 \"Re1=10, Qe3=4, a4=3\"; id \"STS(v2.2) Open Files and Diagonals.058\";\n" +
            "5rk1/pp4p1/5rnp/3p1b2/2p2P2/P1P3P1/2P1B2P/R1B1R1K1 w - - bm Bf3; c0 \"Bf3=10, Bd1=2, Be3=7\"; id \"STS(v2.2) Open Files and Diagonals.059\";\n" +
            "5rrk/pR4p1/3p2q1/P6p/2Q1pn2/7P/1PP2PP1/R3N1K1 w - - bm Ra3; c0 \"Ra3=10, Kh2=6, Rb3=4\"; id \"STS(v2.2) Open Files and Diagonals.060\";\n" +
            "6k1/1b3p2/p2q2p1/P2Pp2p/1p2Pb1P/1Br5/Q4PP1/3RN1K1 b - - bm Bc8; c0 \"Bc8=10\"; id \"STS(v2.2) Open Files and Diagonals.061\";\n" +
            "6k1/1q3rpp/5n2/Rp2r3/4p3/1B5P/3BQPP1/6K1 w - - bm Be3; c0 \"Be3=10, Bc3=5, g3=5\"; id \"STS(v2.2) Open Files and Diagonals.062\";\n" +
            "6k1/1q5p/5np1/pb1pNp2/3P1B1P/1Q6/1P2PP2/6K1 w - - bm Qa3; c0 \"Qa3=10, Qa2=6, Qc3=7\"; id \"STS(v2.2) Open Files and Diagonals.063\";\n" +
            "6k1/2q4p/r3b1p1/2P1p3/r7/4QP2/p1RN2PP/R5K1 b - - bm Rb4; c0 \"Rb4=10, R6a5=8, Ra8=8\"; id \"STS(v2.2) Open Files and Diagonals.064\";\n" +
            "6k1/pb4p1/1p1b2q1/1P1p3p/3Pn3/P1r2N1P/4QPB1/2B1R1K1 b - - bm Bc8; c0 \"Bc8=10, Bb8=5, Rc4=7\"; id \"STS(v2.2) Open Files and Diagonals.065\";\n" +
            "6k1/q4p2/2b1p2p/4Pp2/pP3N2/Q4PP1/6KP/8 b - - bm Qd4; c0 \"Qd4=10, Bb5=9, Qb8=6\"; id \"STS(v2.2) Open Files and Diagonals.066\";\n" +
            "6r1/1R6/1bp1knp1/pp1n3p/4pP1P/1PN3B1/1P3PB1/5K2 w - - bm Bh3+; c0 \"Bh3+=10, Nxd5=6, Nxe4=6\"; id \"STS(v2.2) Open Files and Diagonals.067\";\n" +
            "6r1/4bbk1/p3p1p1/Pp1pPp2/3P1P2/2P2B2/3B2K1/1R6 b - - bm Rh8; c0 \"Rh8=10, Bd8=7, Be8=7, g5=7\"; id \"STS(v2.2) Open Files and Diagonals.068\";\n" +
            "6rk/1n4bp/p3R2r/1p1P1Pp1/1P6/P1pB2P1/5P2/2R3K1 b - - bm Rc8; c0 \"Rc8=10\"; id \"STS(v2.2) Open Files and Diagonals.069\";\n" +
            "8/2b1rk2/2b1p1p1/5n2/p1pP2Q1/2p3P1/P4P2/2B1RK2 w - - bm Bg5; c0 \"Bg5=10, Ba3=5, Bf4=4\"; id \"STS(v2.2) Open Files and Diagonals.070\";\n" +
            "8/3q1pk1/3p4/2pB2p1/P2n4/1P4BP/6P1/4R1K1 b - - bm Qf5; c0 \"Qf5=10, Kh6=3, g4=5\"; id \"STS(v2.2) Open Files and Diagonals.071\";\n" +
            "8/3r1k2/2p3p1/1pPb1p2/p7/P1B2P2/4BK2/3R4 w - - bm Rh1; c0 \"Rh1=10, Bd3=5, Rd4=6\"; id \"STS(v2.2) Open Files and Diagonals.072\";\n" +
            "8/5r1k/p3b1pp/4p1q1/P3Pn2/2B1N2P/3R1PPK/3Q4 w - - bm Qb1; c0 \"Qb1=10, Ba1=5, Bb2=5\"; id \"STS(v2.2) Open Files and Diagonals.073\";\n" +
            "8/5r2/1pp2p1R/p3n3/4Pp1p/2P2P1k/4K2P/R7 b - - bm Rg7; c0 \"Rg7=10, b5=3\"; id \"STS(v2.2) Open Files and Diagonals.074\";\n" +
            "8/p1r3kp/r3p1p1/PRp1n1P1/7R/8/4K2P/7B b - - bm Rd6; c0 \"Rd6=10, Nd7=5, Nf7=7\"; id \"STS(v2.2) Open Files and Diagonals.075\";\n" +
            "8/p1rb3k/1p1b1p1p/3q1p2/1P1P4/P1n1PPP1/3N3P/R1R2Q1K b - - bm Bb5; c0 \"Bb5=10, Kg6=5, Kh8=3, h5=3\"; id \"STS(v2.2) Open Files and Diagonals.076\";\n" +
            "b1r3k1/3n1pb1/p2q2p1/P2Pp2p/1p2P2P/1Rr1NN2/Q1B2PP1/3R2K1 b - - bm Bh6; c0 \"Bh6=10\"; id \"STS(v2.2) Open Files and Diagonals.077\";\n" +
            "b2r4/3qk3/1p1pp3/pN4b1/P3Pp1p/2P5/4BQPP/5R1K w - - bm Bg4; c0 \"Bg4=10, Rd1=1\"; id \"STS(v2.2) Open Files and Diagonals.078\";\n" +
            "q1b2k2/5ppp/2n5/P1N1n3/8/2PpB1P1/3N1P1P/R4RK1 w - - bm Rfb1; c0 \"Rfb1=10, Rfe1=7, a6=7\"; id \"STS(v2.2) Open Files and Diagonals.079\";\n" +
            "r1b2r1k/1p2qpb1/1np3pp/p7/3P4/PBN2N2/1PQ2PPP/3R1RK1 w - - bm Rfe1; c0 \"Rfe1=10, Qc1=7, Qd2=6, h3=7\"; id \"STS(v2.2) Open Files and Diagonals.080\";\n" +
            "r1b2rk1/4bpp1/7p/1B1pP3/3B4/8/1PP3PP/2KR3R b - - bm Bg4; c0 \"Bg4=10, Bf5=9, Ra5=8\"; id \"STS(v2.2) Open Files and Diagonals.081\";\n" +
            "r1b2rk1/pp3pb1/1npN1qpp/2P5/1PBp4/P4NPP/5P2/2RQ1RK1 w - - bm Re1; c0 \"Re1=10, Nxc8=7, Qd3=7\"; id \"STS(v2.2) Open Files and Diagonals.082\";\n" +
            "r1b3k1/4brp1/p6p/1p1p4/3B4/8/PPP3PP/2KR1B1R w - - bm Bd3; c0 \"Bd3=10, Be3=4, Kb1=2\"; id \"STS(v2.2) Open Files and Diagonals.083\";\n" +
            "r1b3k1/pp2qrpp/1n6/1Bb1Pp2/3p1P2/7Q/PP1BN1PP/R3K2R b KQ - bm Be6; c0 \"Be6=10, a6=2\"; id \"STS(v2.2) Open Files and Diagonals.084\";\n" +
            "r1b3kr/p3qpp1/1pn1p2p/3pP2P/7n/3B3Q/2P2PPN/1RB1R1K1 w - - bm Ba3; c0 \"Ba3=10, Qg3=4\"; id \"STS(v2.2) Open Files and Diagonals.085\";\n" +
            "r1br2k1/p4p2/1qp4p/1p2P1p1/6n1/5NP1/PP2QN1P/R1B2BK1 b - - bm Be6; c0 \"Be6=10, Bf5=3, b4=4\"; id \"STS(v2.2) Open Files and Diagonals.086\";\n" +
            "r3r1k1/1b3ppp/2p2n2/1p2b1B1/4P1B1/P6P/1PQ1NPP1/6K1 w - - bm Bf5; c0 \"Bf5=10, Bf3=3, Bxf6=3, f3=2\"; id \"STS(v2.2) Open Files and Diagonals.087\";\n" +
            "r3r1k1/2q2pp1/1pp2np1/4p3/nP2P1P1/P3Q2P/3R1PB1/B1R3K1 w - - bm Bf1; c0 \"Bf1=10, Bf3=5, Rdc2=6\"; id \"STS(v2.2) Open Files and Diagonals.088\";\n" +
            "r3r1k1/ppn2ppp/2p5/5P2/P7/2N4P/1PP5/R1B2RK1 w - - bm Bf4; c0 \"Bf4=10, Kg2=7, Rd1=7, f6=6\"; id \"STS(v2.2) Open Files and Diagonals.089\";\n" +
            "r3r2k/2R4p/3B2b1/p7/1p2p3/6PP/PPP4K/4R3 w - - bm Bc5; c0 \"Bc5=10, c3=7, c4=7\"; id \"STS(v2.2) Open Files and Diagonals.090\";\n" +
            "r4qk1/1p3rbp/3p1np1/2pPp3/b1P5/P1NBBPP1/3Q1R1P/4R1K1 w - - bm Rb1; c0 \"Rb1=10, Kg2=3, Qc1=3, Ree2=3\"; id \"STS(v2.0) Open Files and Diagonals.091\";\n" +
            "r4rk1/1p2pp1p/3p1np1/q2P1P2/2P3BQ/pP5R/P1R3PP/7K w - - bm Re2; c0 \"Re2=10, Be2=2, fxg6=4\"; id \"STS(v2.2) Open Files and Diagonals.092\";\n" +
            "r4rk1/1p2ppbp/1q1p2p1/p1nP1P2/2P3B1/5R2/PPRB2PP/1Q5K w - - bm Qe1; c0 \"Qe1=10, Bg5=4, Rh3=5\"; id \"STS(v2.2) Open Files and Diagonals.093\";\n" +
            "r4rk1/5pb1/2p3pp/pp2n3/1n3B2/1PN1PP1P/1P2BPK1/2R3R1 w - - bm Rgd1; c0 \"Rgd1=10\"; id \"STS(v2.2) Open Files and Diagonals.094\";\n" +
            "r4rk1/6b1/2p1N1p1/8/p2p3R/8/R4P1P/5K2 b - - bm Rfb8; c0 \"Rfb8=10, Rf6=6, c5=8\"; id \"STS(v2.2) Open Files and Diagonals.095\";\n" +
            "r4rk1/ppp1qp1p/1n4p1/4P3/6Q1/1N6/PP3PPP/2R1R1K1 b - - bm Rad8; c0 \"Rad8=10, Nd5=5, Rfd8=6\"; id \"STS(v2.2) Open Files and Diagonals.096\";\n" +
            "r5k1/p1q2pbp/Ppp1bnp1/4p1B1/Q1P1P3/2N4P/1P2BPP1/5RK1 w - - bm Rd1; c0 \"Rd1=10, Be3=1, Rc1=8, b4=1\"; id \"STS(v2.2) Open Files and Diagonals.097\";\n" +
            "r5k1/p4nbp/2qN2p1/2P2p2/3p1B2/6P1/P2Q1P1P/3R2K1 w - - bm Qe2; c0 \"Qe2=10, Nxf7=8, Qd3=7\"; id \"STS(v2.2) Open Files and Diagonals.098\";\n" +
            "r7/3b1pk1/6p1/3Pp2p/2P4P/p4B2/5PP1/2R3K1 b - - bm Bf5; c0 \"Bf5=10, Kf6=6, Kf8=5\"; id \"STS(v2.2) Open Files and Diagonals.099\";\n" +
            "rbbrqnk1/pp3pp1/2p2n1p/5N2/3P1P2/1BN4P/PPQB2P1/R4RK1 w - - bm Rae1; c0 \"Rae1=10, Rfe1=5\"; id \"STS(v2.2) Open Files and Diagonals.100\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    