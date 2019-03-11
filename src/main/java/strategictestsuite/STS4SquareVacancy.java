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
public class STS4SquareVacancy {


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

    public STS4SquareVacancy(Object edp, Object name) {
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
            "6k1/p2pp2p/bp4n1/q1r4R/1RP1P3/2P2B2/P2Q2P1/4K3 w - - bm Rd5; c0 \"Rd5=10, Rf5=6, g4=7\"; id \"STS(v4.0) Square Vacancy.001\";\n" +
            "r2r2k1/pp3ppp/2p1qn2/5N1b/1n2PP2/4Q2P/PPP3B1/R1B2RK1 w - - bm Qc5; c0 \"Qc5=10, Qg3=3\"; id \"STS(v4.0) Square Vacancy.002\";\n" +
            "3r4/p4pk1/P1pr3p/3nb3/1p6/5B1P/1P3PP1/R1BR2K1 w - - bm Ra5; c0 \"Ra5=10, Kf1=2, g3=5\"; id \"STS(v4.0) Square Vacancy.003\";\n" +
            "1b1r3r/3pkpp1/3np1q1/2p5/2P1PPP1/5Q2/PP4B1/R1BR2K1 b - - bm Rh4; c0 \"Rh4=10, Nxc4=6, Rc8=7\"; id \"STS(v4.0) Square Vacancy.004\";\n" +
            "7k/1p6/1n1rrq1p/1R1p1p1p/3P1P2/QR2P2P/6PK/5B2 w - - bm Qa7; c0 \"Qa7=10, Kg1=7, R5b4=7\"; id \"STS(v4.0) Square Vacancy.005\";\n" +
            "5r1k/5rp1/p1n1p1qp/2P1p3/P7/4QN1P/5PP1/2R1R2K w - - bm Rc4; c0 \"Rc4=10, Qe2=5, Rc3=7\"; id \"STS(v4.0) Square Vacancy.006\";\n" +
            "6r1/1p2Q1pk/2p2p1p/3p1P1P/p1nP4/PqP1P3/1P2RN2/2K5 b - - bm Qa2; c0 \"Qa2=10, Qb5=5, b6=3\"; id \"STS(v4.0) Square Vacancy.007\";\n" +
            "6r1/1p2Q1pk/2p2p1p/n2p1P1P/p2P4/P1P1P3/1P1KR3/q2N4 b - - bm Qb1; c0 \"Qb1=10, Kh8=3, Nb3+=4\"; id \"STS(v4.0) Square Vacancy.008\";\n" +
            "5r1k/1b3pp1/p3pb2/4N2q/3R1B2/4P3/1PB2PPP/1K4R1 b - - bm Qe2; c0 \"Qe2=10, Bxe5=7, Kg8=4\"; id \"STS(v4.0) Square Vacancy.009\";\n" +
            "8/1b2r2p/1p1r1kp1/p2p1p2/Pn1P3P/1R1B1N2/1P3PPK/2R5 w - - bm Bb5; c0 \"Bb5=10, Bf1=2, Ne5=6\"; id \"STS(v4.0) Square Vacancy.010\";\n" +
            "1q1k2r1/p2bn3/1r3n2/2QPp1Rp/2P4P/2PB1P2/2K1N3/R7 w - - bm Qa5; c0 \"Qa5=10, Qa3=4, Rxg8+=5\"; id \"STS(v4.0) Square Vacancy.011\";\n" +
            "8/p3q1kp/1p1p1rp1/3Bn3/P1PQ1p2/1P6/6PP/4R2K b - - bm Rf5; c0 \"Rf5=10, Kh6=6, Rf8=3\"; id \"STS(v4.0) Square Vacancy.012\";\n" +
            "4r1rk/pp6/5q2/3pNp1P/2pPnQ2/2P1P2P/P6K/R5R1 w - - bm Rg6; c0 \"Rg6=10, Rab1=4, Rac1=4\"; id \"STS(v4.0) Square Vacancy.013\";\n" +
            "5r2/p1qn3k/bp1p1pp1/3P4/P2BPP2/2Pp4/3N2P1/R2Q2K1 w - - bm Qg4; c0 \"Qg4=10, Qe1=4, a5=5\"; id \"STS(v4.0) Square Vacancy.014\";\n" +
            "1r3q2/1n4pk/R4p1p/4p2P/2B1P1P1/2P1QPK1/8/8 w - - bm Be6; c0 \"Be6=10, Bd5=7, Rb6=8\"; id \"STS(v4.0) Square Vacancy.015\";\n" +
            "4r1k1/1pb3p1/p1p3q1/3n4/1P1P4/P3pRP1/4Q1K1/2B2R2 b - - bm Qe4; c0 \"Qe4=10, Bb6=5, Bd8=5\"; id \"STS(v4.0) Square Vacancy.016\";\n" +
            "3R4/pkn5/1p2qp2/1p5p/1P2P1pR/P5P1/1N3PP1/5K2 b - - bm Qb3; c0 \"Qb3=10, Qa2=5\"; id \"STS(v4.0) Square Vacancy.017\";\n" +
            "4r1k1/p5pp/4q3/P2R4/2p2P2/2N3P1/2nB1K1P/1R6 b - - bm Qh3; c0 \"Qh3=10, a6=5, h6=6\"; id \"STS(v4.0) Square Vacancy.018\";\n" +
            "1Q6/1p2p2k/1r4pp/p1p2P2/q2nr1P1/7R/1P1R4/5NK1 w - - bm Qf8; c0 \"Qf8=10, Kh2=7, Rg2=7\"; id \"STS(v4.0) Square Vacancy.019\";\n" +
            "4R3/p2q2k1/2p2r1p/6p1/8/P5P1/4QP1P/3rB1K1 b - - bm Qd4; c0 \"Qd4=10, Kf7=6, Kg6=6\"; id \"STS(v4.0) Square Vacancy.020\";\n" +
            "3q1r1k/5ppp/1pR1pn2/p7/B1PP4/P3QP1P/5PK1/8 w - - bm Qe5; id \"STS(v4.0) Square Vacancy.021\"; c0 \"Qe5=10, f4=1, Kf1=1, Kh2=1\";\n" +
            "8/Qpnbk2q/4pp2/1P1p4/P1r5/2P3P1/1K2N3/1R3B2 b - - bm Qd3; c0 \"Qd3=10, Qh2=2, e5=2\"; id \"STS(v4.0) Square Vacancy.022\";\n" +
            "2r2rk1/pb2qp1p/n3p1p1/2pp4/N1P1n3/1P2P1P1/PN3PBP/2R1QRK1 w - - bm Qa5; c0 \"Qa5=10, Qe2=6, Rd1=4\"; id \"STS(v4.0) Square Vacancy.023\";\n" +
            "r5k1/1p5p/p1p1qrnP/2B1pb2/R1P5/2P2B2/P2Q2P1/5RK1 w - - bm Qg5; c0 \"Qg5=10, Qb2=1, Rb4=6\"; id \"STS(4.0) Square Vacancy.024\";\n" +
            "Q7/2pq3k/1rp2b1p/2R5/8/1P1NP3/P2K1Pr1/5R2 w - - bm Qf8; c0 \"Qf8=10, Qa4=5, Rc2=4\"; id \"STS(v4.0) Square Vacancy.025\";\n" +
            "q5k1/p2p2bp/1p1p2r1/2p1np2/6p1/1PP2PP1/P2PQ1KP/4R1NR b - - bm Qd5; c0 \"Qd5=10, Re6=4, a5=1, c4=2\"; id \"STS(v4.0) Square Vacancy.026\";\n" +
            "4r1k1/1p5p/1nq1p1p1/4B3/6R1/P6P/3Q1PPK/8 w - - bm Qh6; c0 \"Qh6=10, Ba1=4, Qd3=4\"; id \"STS(v4.0) Square Vacancy.027\";\n" +
            "r5nk/p4p1r/qp1Rb2p/2p1Pp2/5P1Q/P4N2/2B3PP/5RK1 w - - bm Qh5; c0 \"Qh5=10, a4=2\"; id \"STS(v4.0) Square Vacancy.028\";\n" +
            "r2r2k1/1q2bpp1/4p2p/p6Q/N4P2/1P2R1P1/2P4P/2KR4 b - - bm Qb4; c0 \"Qb4=10, Qg2=5, Rdc8=5\"; id \"STS(v4.0) Square Vacancy.029\";\n" +
            "r4rk1/1b2bpp1/1P1p4/p1q1p2p/R3PPn1/3B3Q/2P1N1PP/1R1N3K b - - bm Qf2; c0 \"Qf2=10, d5=1\"; id \"STS(v4.0) Square Vacancy.030\";\n" +
            "1r5r/1p6/p2p2p1/P2P1pkq/1R1Qp3/2P1P1bP/1P2R3/5BK1 b - - bm Qf3; c0 \"Qf3=10, Be5=8, Rh6=5\"; id \"STS(v4.0) Square Vacancy.031\";\n" +
            "2r1r2k/q6p/6p1/3R1p2/1p1P1B2/2bQPBP1/5P1P/6K1 w - - bm Qb5; c0 \"Qb5=10, Kg2=5, Rb5=5\"; id \"STS(v4.0) Square Vacancy.032\";\n" +
            "1r3q1k/6p1/r6p/3P3b/2PQ1R2/pp4P1/5P2/R1B3K1 b - - bm Qb4; c0 \"Qb4=10, Qd6=1, Qe7=1\"; id \"STS(v4.0) Square Vacancy.033\";\n" +
            "5k2/pb3pp1/qp3n1p/2p5/2P2B2/8/P1PN1PPP/R3R1K1 b - - bm Qa4; c0 \"Qa4=10, Bc6=1\"; id \"STS(v4.0) Square Vacancy.034\";\n" +
            "2r1qr1k/1p4bp/p1n1bpp1/4p3/B3P3/4QN1P/PP1B1PP1/R1R3K1 w - - bm Qb6; c0 \"Qb6=10, Rc2=5, Rc3=5, a3=4\"; id \"STS(v4.0) Square Vacancy.035\";\n" +
            "6rk/5q1p/p2p4/P1nP1p2/2B2N1Q/1Pb1pPR1/7P/7K w - - bm Qh6; c0 \"Qh6=10, Rg5=3, Rxg8+=5\"; id \"STS(v4.0) Square Vacancy.036\";\n" +
            "3r2k1/p4rp1/1pRp3q/1P1Ppb2/7P/Q4P2/P5BP/2R4K b - - bm Qd2; c0 \"Qd2=10, Kh7=5, Qf4=4\"; id \"STS(v4.0) Square Vacancy.037\";\n" +
            "rq4k1/3bn1bp/3p2p1/3Pp1N1/4P1p1/2N1B1P1/1P2QP1P/4R1K1 b - - bm Qb3; c0 \"Qb3=10, Kh8=4, h5=4\"; id \"STS(v4.0) Square Vacancy.038\";\n" +
            "3rk3/2p1r1p1/7q/p1p1P1R1/2B4P/4P3/PPQ5/6K1 w - - bm Qe4; c0 \"Qe4=10, Kg2=4, b3=3\"; id \"STS(v4.0) Square Vacancy.039\";\n" +
            "2r2r1k/3q2np/p2P1pp1/1pP1p3/7N/5QP1/P4P1P/2R1R1K1 w - - bm Qd5; c0 \"Qd5=10, Qd1=7, Qd3=7\"; id \"STS(v4.0) Square Vacancy.040\";\n" +
            "r2r2k1/5p1p/6nQ/ppq5/2p1P3/P4P2/1P4PP/R1N2R1K b - - bm Qd4; c0 \"Qd4=10, Qd6=5, Qf2=1\"; id \"STS(v4.0) Square Vacancy.041\";\n" +
            "r3r1k1/ppq3p1/2p1n3/3pN1p1/Pb1P2P1/3QB2P/1P3P2/R1R3K1 w - - bm Qf5; c0 \"Qf5=10, Kg2=4, Ng6=3, Qg6=4, Rc2=4\"; id \"STS(v4.0) Square Vacancy.042\";\n" +
            "2bq1rk1/1r3p2/3p2pp/p1p1p3/PnP1P3/2QPP1N1/2B3PP/1R3RK1 b - - bm Qg5; c0 \"Qg5=10, Bd7=3, Qe8=3, h5=4\"; id \"STS(v4.0) Square Vacancy.043\";\n" +
            "2q5/5kp1/pP2p1r1/3b1p1Q/3P1P2/6P1/5K2/4RB2 b - - bm Qc3; c0 \"Qc3=10, Bb7=2, Qc2+=3, Qc6=1\"; id \"STS(v4.0) Square Vacancy.044\";\n" +
            "2q2r1k/7p/p2p1p2/1p1Np3/nP6/4Q2P/P4PP1/4R1K1 w - - bm Qh6; c0 \"Qh6=10, Qd2=2, Rc1=6, Re2=1\"; id \"STS(v4.0) Square Vacancy.045\";\n" +
            "8/3k4/2p5/3r4/1PQBp1pq/4P3/P4PpP/6K1 w - - bm Qa6; c0 \"Qa6=10, Kxg2=4, a4=6, b5=5\"; id \"STS(v4.0) Square Vacancy.046\";\n" +
            "8/7p/p2p1kqP/2r1b3/P1B1p1r1/1P6/4QP2/3R1K1R b - - bm Qf5; c0 \"Qf5=10, Qg5=1, Rf4=3, a5=3\"; id \"STS(v4.0) Square Vacancy.047\";\n" +
            "2b1rnk1/1p3pp1/r3p2p/1q2P3/p2PQ3/P1P2PB1/6PP/1BKR3R b - - bm Qb3; c0 \"Qb3=10, Bd7=5, Qa5=4, Rc6=4\"; id \"STS(v4.0) Square Vacancy.048\";\n" +
            "2r5/p4kp1/1pn2n2/3q1b2/3p4/P2P2Q1/2RB1PPP/4R1K1 b - - bm Qb3; c0 \"Qb3=10, Nd7=5, Nh5=3, Qb5=3\"; id \"STS(v4.0) Square Vacancy.049\";\n" +
            "r6k/1p2rR1P/2p3p1/4p1q1/8/pP6/P1P5/1K2Q2R w - - bm Qa5; c0 \"Qa5=10, Rf2=6, Rf3=6, Rff1=6\"; id \"STS(v4.0) Square Vacancy.050\";\n" +
            "r3r1k1/2q3p1/p2p1p2/3P2p1/n2B2P1/8/P1PQ3P/K2R1R2 b - - bm Qc4; c0 \"Qc4=10, Qc8=2, Rac8=6, Re4=6\"; id \"STS(v4.0) Square Vacancy.051\";\n" +
            "3r2k1/2p2pp1/p6p/1p5q/1PbB4/P1Q3P1/5P1P/4R1K1 b - - bm Qg4; c0 \"Qg4=10, Rd6=2, a5=2, f6=2\"; id \"STS(v4.0) Square Vacancy.052\";\n" +
            "r5k1/1q4b1/6n1/3pp1Nn/1N3p2/3P2Pp/2QBP2P/1R4K1 w - - bm Qc5; c0 \"Qc5=10, Nxh3=4\"; id \"STS(v4.0) Square Vacancy.053\";\n" +
            "6r1/4pp1k/3p3p/2qP1P2/r3P1PK/1R6/4Q3/1R6 b - - bm Qd4; c0 \"Qd4=10, Ra3=1, Rg5=1, Rga8=2, Rg7=1\"; id \"STS(v4.0) Square Vacancy.054\";\n" +
            "8/p3b1k1/2p5/3qp2p/P2p3P/1P1Q1PP1/3NP1K1/8 w - - bm Qf5; c0 \"Qf5=10, Nc4=7, Qa6=6, Qb1=5, Qc2=5\"; id \"STS(v4.0) Square Vacancy.055\";\n" +
            "1rb2rk1/2p3pp/p1p1p3/2N5/8/1PQ2PPq/P3P3/R2R2K1 w - - bm Qe5; c0 \"Qe5=10, Kf2=4, Ne4=6\"; id \"STS(v4.0) Square Vacancy.056\";\n" +
            "2rqrbk1/1b1n1p2/p2p1npp/1p6/4P2B/1B3NNP/PP1Q1PP1/3RR1K1 w - - bm Qf4; c0 \"Qf4=10, Re2=3, a3=1, a4=2\"; id \"STS(v4.0) Square Vacancy.057\";\n" +
            "r1k5/4qp2/P1p1b1p1/6Pp/n3P2P/6Q1/2N5/1K1R2R1 b - - bm Qc5; c0 \"Qc5=10, Rxa6=2\"; id \"STS(v4.0) Square Vacancy.058\";\n" +
            "6k1/3qbp1p/6p1/3Pp1P1/3n3P/pP1Q4/P7/1K2B2B b - - bm Qg4; c0 \"Qg4=10, Bd6=5, Kf8=3, h6=3\"; id \"STS(v4.0) Square Vacancy.059\";\n" +
            "3n4/p2rk3/3pq1p1/pR3p1p/2PQ1P2/1PB4P/6PK/8 w - - bm Qh8; c0 \"Qh8=10, Bb2=5, Kh1=5, Rb8=6, Rd5=5\"; id \"STS(v4.0) Square Vacancy.060\";\n" +
            "5b1k/r7/Pq5p/2p5/3p3N/R2Q2P1/5PK1/8 w - - bm Qf5; c0 \"Qf5=10, Ng6+=2, Qf3=8, Qg6=2\"; id \"STS(v4.0) Square Vacancy.061\";\n" +
            "5rk1/3n4/3Rp1p1/2P2q1p/1p1P2n1/6P1/1B1NQ2P/6K1 b - - bm Qc2; id \"STS(v4.0) Square Vacancy.062\"; c0 \"Qc2=10, Ndf6=4, Nxc5=4\";\n" +
            "2r3k1/5pbp/2pB2p1/p3P3/2R2P2/2Q3P1/5K1P/1q6 b - - bm Qh1; c0 \"Qh1=10, Qa2+=2, Qb6+=3, c5=2\"; id \"STS(v4.0) Square Vacancy.063\";\n" +
            "5r2/3q2pk/5n1p/3p1p1P/1p1P4/4PP2/1P2R1PQ/2rNR1K1 b - - bm Qb5; c0 \"Qb5=10, Re8=4\"; id \"STS(v4.0) Square Vacancy.064\";\n" +
            "5r1k/2pb1q2/1p1p4/pP1Pp1Rp/2P1P3/1KN4r/P7/4Q2R b - - bm Qf3; c0 \"Qf3=10, Qh7=4, Rg8=2, Rxh1=3\"; id \"STS(v4.0) Square Vacancy.065\";\n" +
            "7k/p4r2/b3p2q/Pp1p2pp/3P2p1/1P2P1P1/2Q2PBP/2R3K1 w - - bm Qc6; c0 \"Qc6=10, Bf1=5, Qe2=4, e4=6\"; id \"STS(v4.0) Square Vacancy.066\";\n" +
            "3r1k2/2q5/1p6/5P1p/pP2Q3/P2p4/3R4/1K6 b - - bm Qg3; c0 \"Qg3=10, Qc3=5, Qe7=7, Re8=4\"; id \"STS(v4.0) Square Vacancy.067\";\n" +
            "6k1/4bp2/4q3/3pP3/p1pr3p/P1NbQR2/1P4PR/3K4 b - - bm Qg4; c0 \"Qg4=10, Bc2+=4, Rg4=6\"; id \"STS(v4.0) Square Vacancy.068\";\n" +
            "5bk1/3r1p1p/3P1Pp1/1q4Pn/2pN3P/1P2B3/1K3Q2/R7 b - - bm Qe5; c0 \"Qe5=10, Qb8=7, Qc5=4, Qd5=8\"; id \"STS(v4.0) Square Vacancy.069\";\n" +
            "1r1n3k/1pq3pp/p2Nrn2/2P1p3/3p4/Q2P2P1/4PPB1/1RR3K1 w - - bm Rb6; c0 \"Rb6=10, Qa2=4, Qa4=4, Qb3=4, Rb3=5\"; id \"STS(v4.0) Square Vacancy.070\";\n" +
            "3r1q1k/6pp/4R3/8/n2p1PB1/3PpQP1/4P1K1/8 w - - bm Qc6; c0 \"Qc6=10, Bh3=3, Kh2=3, Re5=3, f5=3\"; id \"STS(v4.0) Square Vacancy.071\";\n" +
            "8/1p3q1k/p1p2b1P/2B1nP2/8/1Pn2PN1/2Q3P1/6K1 b - - bm Qd5; c0 \"Qd5=10, Bh4=3, Nd5=3, Nd7=5\"; id \"STS(v4.0) Square Vacancy.072\";\n" +
            "2r4k/7p/2q2nr1/p3R3/Ppp1P3/7P/P1B3P1/3R1Q1K w - - bm Qf5; c0 \"Qf5=10, Qf3=4, Rd2=4, Re7=2\"; id \"STS(v4.0) Square Vacancy.073\";\n" +
            "bq6/4bk1p/1B1p2p1/3Ppp2/2P5/7P/1Q1N1PP1/6K1 w - - bm Qb5; c0 \"Qb5=10, Qb1=2, Qb3=4, Qb4=2\"; id \"STS(v4.0) Square Vacancy.074\";\n" +
            "b3qbk1/B6p/1QNp2p1/3Pp3/2P2p2/5P1P/6P1/6K1 w - - bm Qc7; c0 \"Qc7=10, Kh2=2, Nd8=4, Qb5=2\"; id \"STS(v4.0) Square Vacancy.075\";\n" +
            "r3r1k1/1b3p1p/3b2p1/p2Pp1P1/1pq1B3/4B3/PPP3QP/1K1R1R2 w - - bm Rf6; c0 \"Rf6=10, Bd3=2, b3=1, h4=3\"; id \"STS(v4.0) Square Vacancy.076\";\n" +
            "r4q1k/1p2b2r/7P/p1pPp3/8/P2PP1R1/1P4Q1/1K5R w - - bm Qe4; c0 \"Qe4=10, Rf1=5, Rg4=5, Rg7=5\"; id \"STS(v4.0) Square Vacancy.077\";\n" +
            "2b1q3/p4rbk/1p1R2pp/1P1N4/P2pPp2/B4P1P/4Q1P1/6K1 w - - bm Qc4; c0 \"Qc4=10, Nb4=5, Qd2=2, Qd3=2\"; id \"STS(v4.0) Square Vacancy.078\";\n" +
            "r1b2rk1/p6p/1p1P1pp1/q1p5/2P1PQ2/P7/1R2B1PP/5R1K b - - bm Qc3; c0 \"Qc3=10, Bb7=3, Bd7=1, Be6=2\"; id \"STS(v4.0) Square Vacancy.079\";\n" +
            "2b2kn1/5pp1/r2q3p/p2pN3/2pP1PP1/5P2/P1Q4P/1B2R2K w - - bm Qh7; c0 \"Qh7=10, Kg1=6, Qc3=6, g5=6\"; id \"STS(v4.0) Square Vacancy.080\";\n" +
            "2r1r3/1Rpp2pk/2n4p/p1PNqp2/P3p3/2P1P1P1/4QP1P/3R2K1 w - - bm Qh5; c0 \"Qh5=10, Nf4=5, Qa6=2, Rb5=2\"; id \"STS(v4.0) Square Vacancy.081\";\n" +
            "r1b1k2r/2q1pp2/1p5p/p1pPp1p1/2P5/3B3P/PP2QPP1/4RRK1 w kq - bm Qh5; c0 \"Qh5=10, Bc2=4, Qxe5=4, a3=4\"; id \"STS(v4.0) Square Vacancy.082\";\n" +
            "1rr3k1/pp3p1p/6p1/1PPB4/4P2q/2Q2P2/P4P1P/2R2K2 w - - bm Qe5; c0 \"Qe5=10, Kg1=3, Kg2=3, Rd1=3\"; id \"STS(v4.0) Square Vacancy.083\";\n" +
            "r2bn1k1/1p3pp1/pqb1p2p/4B3/2B2Q1P/2N2P2/PPP3P1/1K1R4 b - - bm Qf2; c0 \"Qf2=10, Bf6=1, Qc5=2, Rc8=1\"; id \"STS(v4.0) Square Vacancy.084\";\n" +
            "7r/q3kp2/2rp1p2/3RpPb1/1p2P1P1/1P1N4/P4R1P/1K2Q3 b - - bm Qa3; c0 \"Qa3=10, Kd7=2\"; id \"STS(v4.0) Square Vacancy.085\";\n" +
            "1n2r1k1/1q4pp/4pp2/p1R5/Pp1Q4/4B2P/1P3PP1/6K1 w - - bm Qd6; c0 \"Qd6=10, Qc4=5, Qd3=5, Qg4=4\"; id \"STS(v4.0) Square Vacancy.086\";\n" +
            "2r5/p4p1k/1p5p/2qPnN2/P2R4/1P4Pp/7P/3Q2K1 b - - bm Qc3; c0 \"Qc3=10, Qc1=2\"; id \"STS(v4.0) Square Vacancy.087\";\n" +
            "4r1k1/1rp2ppp/p3b3/1n1p4/1q1P1B2/1P3P1P/P1Q3P1/3RRNK1 w - - bm Qc6; c0 \"Qc6=10, Ne3=4, Qd2=5, Qd3=5, Qf2=3\"; id \"STS(v4.0) Square Vacancy.088\";\n" +
            "r1b1n2r/1p1pqpkp/p1n1p3/4P3/4NP2/3B3P/PPP3P1/R2Q1R1K w - - bm Qh5; c0 \"Qh5=10, Qg4+=7, c4=7, f5=7\"; id \"STS(v4.0) Square Vacancy.089\";\n" +
            "6rk/5Qp1/7p/4p2P/1p1rq3/6R1/PP6/K5R1 b - - bm Qc2; c0 \"Qc2=10, Qd5=3, Qe2=5, Qf4=5\"; id \"STS(v4.0) Square Vacancy.090\";\n" +
            "3r2k1/5pb1/5qpp/1Np5/8/1P3P1P/1PQ3P1/1K5R b - - bm Qg5; c0 \"Qg5=10, Qa6=3, Qh4=3, Rb8=4\"; id \"STS(v4.0) Square Vacancy.091\";\n" +
            "5k2/q7/2p5/4pppn/1pP2nN1/1P2RPPP/3r1B1K/4Q3 b - - bm Qd4; c0 \"Qd4=10, Nxg3=2, Rxf2+=4\"; id \"STS(v4.0) Square Vacancy.092\";\n" +
            "2q2rk1/p1P1bppp/Pn3n2/3p1b2/3P1B2/1P3N1P/4BPP1/2Q2RK1 w - - bm Qc6; c0 \"Qc6=10, Nh4=7, Qe3=7, g4=6\"; id \"STS(v4.0) Square Vacancy.093\";\n" +
            "5rk1/1Q3pp1/p2ppq1p/6b1/N2NP1P1/8/PP2K1P1/3R4 b - - bm Qf4; c0 \"Qf4=10, Qe5=5, a5=6, d5=6\"; id \"STS(v4.0) Square Vacancy.094\";\n" +
            "2k3r1/1b1n1p2/5n2/1Rp1q2p/1pPpP1p1/1N3P2/4B1PP/Q4RK1 w - - bm Qa7; c0 \"Qa7=10, Qa5=1, Rf2=2, fxg4=3\"; id \"STS(v4.0) Square Vacancy.095\";\n" +
            "r3r1k1/5pp1/p2p3p/2nPP3/p5q1/2P1RNP1/2Q2P1P/2R3K1 b - - bm Qc4; c0 \"Qc4=10, Rac8=3, Rad8=3\"; id \"STS(v4.0) Square Vacancy.096\";\n" +
            "7k/ppr1qpb1/1n2pN1p/4P1p1/8/1B3QP1/PP3PKP/4R3 w - - bm Qh5; c0 \"Qh5=10, Re2=3, Re3=1, h3=2\"; id \"STS(v4.0) Square Vacancy.097\";\n" +
            "3r2k1/R5p1/2b2npp/1pP5/4pP1P/6Pq/1Q2BP2/4B1K1 w - - bm Qe5; c0 \"Qe5=10, Bf1=2, Ra6=3, Rc7=6\"; id \"STS(v4.0) Square Vacancy.098\";\n" +
            "3r4/k1pr4/npR1p3/p3Pp1p/P1QP2pN/q5P1/6PP/1R5K b - - bm Qe3; c0 \"Qe3=10, Kb7=4, Qe7=5, f4=4\"; id \"STS(v4.0) Square Vacancy.099\";\n" +
            "R7/2q3k1/p1r2p1n/2p1pPp1/P3P3/2PP4/7Q/5BK1 w - - bm Qh5; c0 \"Qh5=10, Be2=1, Qb2=1, Qe2=1\"; id \"STS(v4.0) Square Vacancy.100\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}

    