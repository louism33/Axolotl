package challenges;

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

@RunWith(Parameterized.class)
public class BT2630 {
    /*
    Arasan = 900 sec.	27/30
(rating: 2540)

30 minutes!
     */

    private static final int timeLimit = 900_000;


    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < splitUpPositions.length; i++) {
            String pos = splitUpPositions[i];
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(pos);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public BT2630(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getBoardFen());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.DEBUG = false;
        int move = EngineBetter.searchFixedTime(EPDObject.getBoard(), timeLimit);

        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }

    private static final String positions = "" +
            "rq2r1k1/5pp1/p7/4bNP1/1p2P2P/5Q2/PP4K1/5R1R w - - bm Nxg7; id \"test 1\";\n" +
            "6k1/2b2p1p/ppP3p1/4p3/PP1B4/5PP1/7P/7K w - - bm Bxb6; id \"test 2\";\n" +
            "5r1k/p1q2pp1/1pb4p/n3R1NQ/7P/3B1P2/2P3P1/7K w - - bm Re6; id \"test 3\";\n" +
            "5r1k/1P4pp/3P1p2/4p3/1P5P/3q2P1/Q2b2K1/B3R3 w - - bm Qf7; id \"test 4\";\n" +
            "3B4/8/2B5/1K6/8/8/3p4/3k4 w - - bm Ka6; id \"test 5\";\n" +
            "1k1r4/1pp4p/2n5/P6R/2R1p1r1/2P2p2/1PP2B1P/4K3 b - - bm e3; id \"test 6\";\n" +
            "6k1/p3q2p/1nr3pB/8/3Q1P2/6P1/PP5P/3R2K1 b - - bm Rd6; id \"test 7\";\n" +
            "2krr3/1p4pp/p1bRpp1n/2p5/P1B1PP2/8/1PP3PP/R1K3B1 w - - bm Rxc6+; id \"test 8\";\n" +
            "r5k1/pp2p1bp/6p1/n1p1P3/2qP1NP1/2PQB3/P5PP/R4K2 b - - bm g5; id \"test 9\";\n" +
            "2r3k1/1qr1b1p1/p2pPn2/nppPp3/8/1PP1B2P/P1BQ1P2/5KRR w - - bm Rxg7+; id \"test 10\";\n" +
            "1br3k1/p4p2/2p1r3/3p1b2/3Bn1p1/1P2P1Pq/P3Q1BP/2R1NRK1 b - - bm Qxh2+; id \"test 11\";\n" +
            "8/pp3k2/2p1qp2/2P5/5P2/1R2p1rp/PP2R3/4K2Q b - - bm Qe4; id \"test 12\";\n" +
            "2bq3k/2p4p/p2p4/7P/1nBPPQP1/r1p5/8/1K1R2R1 b - - bm Be6; id \"test 13\";\n" +
            "3r1rk1/1p3pnp/p3pBp1/1qPpP3/1P1P2R1/P2Q3R/6PP/6K1 w - - bm Rxh7; id \"test 14\";\n" +
            "2b1q3/p7/1p1p2kb/nPpN3p/P1P1P2P/6P1/5R1K/5Q2 w - - bm e5; id \"test 15\";\n" +
            "2krr3/pppb1ppp/3b4/3q4/3P3n/2P2N1P/PP2B1P1/R1BQ1RK1 b - - bm Nxg2; id \"test 16\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/3B1P2/PP3PK1/2Q4R w - - bm Qxf4; id \"test 17\";\n" +
            "8/4p3/8/3P3p/P2pK3/6P1/7b/3k4 w - - bm d6; id \"test 18\";\n" +
            "3r2k1/pp4B1/6pp/PP1Np2n/2Pp1p2/3P2Pq/3QPPbP/R4RK1 b - - bm f3; id \"test 19\";\n" +
            "r4rk1/5p2/1n4pQ/2p5/p5P1/P4N2/1qb1BP1P/R3R1K1 w - - bm Ra2; id \"test 20\";\n" +
            "k7/8/PP1b2P1/K2Pn2P/4R3/8/6np/8 w - - bm Re1; id \"test 21\";\n" +
            "rnb1k2r/pp2qppp/3p1n2/2pp2B1/1bP5/2N1P3/PP2NPPP/R2QKB1R w KQkq - bm a3; id \"test 22\";\n" +
            "8/7p/8/p4p2/5K2/Bpk3P1/4P2P/8 w - - bm g4; id \"test 23\";\n" +
            "R7/3p3p/8/3P2P1/3k4/1p5p/1P1NKP1P/7q w - - bm g6; id \"test 24\";\n" +
            "8/8/3k1p2/p2BnP2/4PN2/1P2K1p1/8/5b2 b - - bm Nd3; id \"test 25\";\n" +
            "2r3k1/pbr1q2p/1p2pnp1/3p4/3P1P2/1P1BR3/PB1Q2PP/5RK1 w - - bm f5; id \"test 26\";\n" +
            "3r2k1/p2r2p1/1p1B2Pp/4PQ1P/2b1p3/P3P3/7K/8 w - - bm Bb4; id \"test 27\";\n" +
            "rnb1k1nr/p2p1ppp/3B4/1p1N1N1P/4P1P1/3P1Q2/PqP5/R4Kb1 w kq - bm Re1; id \"test 28\";\n" +
            "r1b1kb1r/pp1n1ppp/2q5/2p3B1/Q1B5/2p2N2/PP3PPP/R3K2R w KQkq - bm Bxf7+; id \"test 29\";\n" +
            "2k5/2p3Rp/p1pb4/1p2p3/4P3/PN1P1P2/1P2KP1r/8 w - - bm f4; id \"test 30\";\n" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}
    
    