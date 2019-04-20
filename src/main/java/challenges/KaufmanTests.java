package challenges;

import com.github.louism33.axolotl.search.Engine;
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
public class KaufmanTests {

    private static final int timeLimit = 10000;

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

    public KaufmanTests(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        System.out.println(EPDObject.getFullString());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.DEBUG = false;
        int move = Engine.searchFixedTime(EPDObject.getBoard(), timeLimit);

        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }
    private static final String positions = "" +
            "1rbq1rk1/p1b1nppp/1p2p3/8/1B1pN3/P2B4/1P3PPP/2RQ1R1K w - - bm Nf6+; id \"position 01\";\n" +
            "3r2k1/p2r1p1p/1p2p1p1/q4n2/3P4/PQ5P/1P1RNPP1/3R2K1 b - - bm Nxd4 id \"position 02\";\n" +
            "3r2k1/1p3ppp/2pq4/p1n5/P6P/1P6/1PB2QP1/1K2R3 w - - am Rd1; id \"position 03\";\n" +
            "r1b1r1k1/1ppn1p1p/3pnqp1/8/p1P1P3/5P2/PbNQNBPP/1R2RB1K w - - bm Rxb2; id \"position 04\";\n" +
            "2r4k/pB4bp/1p4p1/6q1/1P1n4/2N5/P4PPP/2R1Q1K1 b - - bm Qxc1; id \"position 05\";\n" +
            "r5k1/3n1ppp/1p6/3p1p2/3P1B2/r3P2P/PR3PP1/2R3K1 b - - am Rxa2; id \"position 06\";\n" +
            "2r2rk1/1bqnbpp1/1p1ppn1p/pP6/N1P1P3/P2B1N1P/1B2QPP1/R2R2K1 b - - bm Bxe4 id \"position 07\";\n" +
            "5r1k/6pp/1n2Q3/4p3/8/7P/PP4PK/R1B1q3 b - - bm h6; id \"position 08\";\n" +
            "r3k2r/pbn2ppp/8/1P1pP3/P1qP4/5B2/3Q1PPP/R3K2R w KQkq - bm Be2; id \"position 09\";\n" +
            "3r2k1/ppq2pp1/4p2p/3n3P/3N2P1/2P5/PP2QP2/K2R4 b - - bm Nxc3; id \"position 10\";\n" +
            "q3rn1k/2QR4/pp2pp2/8/P1P5/1P4N1/6n1/6K1 w - - bm Nf5; id \"position 11\";\n" +
            "6k1/p3q2p/1nr3pB/8/3Q1P2/6P1/PP5P/3R2K1 b - - bm Rd6; id \"position 12\";\n" +
            "1r4k1/7p/5np1/3p3n/8/2NB4/7P/3N1RK1 w - - bm Nxd5; id \"position 13\";\n" +
            "1r2r1k1/p4p1p/6pB/q7/8/3Q2P1/PbP2PKP/1R3R2 w - - bm Rxb2; id \"position 14\";\n" +
            "r2q1r1k/pb3p1p/2n1p2Q/5p2/8/3B2N1/PP3PPP/R3R1K1 w - - bm Bxf5; id \"position 15\";\n" +
            "8/4p3/p2p4/2pP4/2P1P3/1P4k1/1P1K4/8 w - - bm b4; id \"position 16\";\n" +
            "1r1q1rk1/p1p2pbp/2pp1np1/6B1/4P3/2NQ4/PPP2PPP/3R1RK1 w - - bm e5; id \"position 17\";\n" +
            "q4rk1/1n1Qbppp/2p5/1p2p3/1P2P3/2P4P/6P1/2B1NRK1 b - - bm Qc8; id \"position 18\";\n" +
            "r2q1r1k/1b1nN2p/pp3pp1/8/Q7/PP5P/1BP2RPN/7K w - - bm Qxd7; id \"position 19\";\n" +
            "8/5p2/pk2p3/4P2p/2b1pP1P/P3P2B/8/7K w - - bm Bg4; id \"position 20\";\n" +
            "8/2k5/4p3/1nb2p2/2K5/8/6B1/8 w - - bm Kxb5; id \"position 21\";\n" +
            "1B1b4/7K/1p6/1k6/8/8/8/8 w - - bm Ba7; id \"position 22\";\n" +
            "rn1q1rk1/1b2bppp/1pn1p3/p2pP3/3P4/P2BBN1P/1P1N1PP1/R2Q1RK1 b - - bm Ba6; id \"position 23\";\n" +
            "8/p1ppk1p1/2n2p2/8/4B3/2P1KPP1/1P5P/8 w - - bm Bxc6; id \"position 24\";\n" +
            "8/3nk3/3pp3/1B6/8/3PPP2/4K3/8 w - - bm Bxd7; id \"position 25\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");
    
}
