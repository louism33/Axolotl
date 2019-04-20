package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static challenges.Utils.contains;
import static com.github.louism33.utils.ExtendedPositionDescriptionParser.EPDObject;
import static com.github.louism33.utils.ExtendedPositionDescriptionParser.parseEDPPosition;

@RunWith(Parameterized.class)
public class PassedPawnTest {

    private static final int timeLimit = 10_000;

    // tough at 5 sec
    private static final int[] infamousIndexes = {86, 163, 180, 196, 222, 230, 243, 293};

    @Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        EngineSpecifications.DEBUG = true;

        for (int i = 0; i < splitUpPositions.length; i++) {

            if (!contains(infamousIndexes, i + 1)) {
//                continue;
            }
            String splitUpWAC = splitUpPositions[i];
            System.out.println(splitUpWAC);
            Object[] objectAndName = new Object[2];
            EPDObject EPDObject = parseEDPPosition(splitUpWAC);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static EPDObject EPDObject;

    public PassedPawnTest(Object edp, Object name) {
        EPDObject = (EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        System.out.println(EPDObject.getFullString());
        System.out.println(EPDObject.getBoard());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        int move = Engine.searchFixedTime(EPDObject.getBoard(), timeLimit);
        MoveParser.printMove(move);
        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }

    private static final String positions = "" +
            "4Q3/6pk/2pq4/3p4/1p1P3p/1P1K1P2/1PP3P1/8 b - - bm Qg6;\n" +
            "8/5pk1/4p3/7Q/8/3q4/KP6/8 b - - bm Qd5;\n" +
            "r3bb2/P1q3k1/Q2p3p/2pPp1pP/2B1P3/2B5/6P1/R5K1 w - - bm Bxe5;\n" +
            "r1b5/p2k1r1p/3P2pP/1ppR4/2P2p2/2P5/P1B4P/4R1K1 w - - bm Bxg6;\n" +
            "6r1/1p3k2/pPp4R/K1P1p1p1/1P2Pp1p/5P1P/6P1/8 w - - bm Rxc6;\n" +
            "1k2b3/4bpp1/p2pp1P1/1p3P2/2q1P3/4B3/PPPQN2r/1K1R4 w - - bm f6;\n" +
            "2kr3r/ppp1qpp1/2p5/2b2b2/2P1pPP1/1P2P1p1/PBQPB3/RN2K1R1 b Q - bm Rh1;\n" +
            "6k1/2q3p1/1n2Pp1p/pBp2P2/Pp2P3/1P1Q1KP1/8/8 w - - bm e5;\n" +
            "5r2/pp1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Rxf7 Qxf7;\n" +
            "6k1/1q2rpp1/p6p/P7/1PB1n3/5Q2/6PP/5R1K w - - bm b5;\n" +
            "3r2k1/p6p/b2r2p1/2qPQp2/2P2P2/8/6BP/R4R1K w - - bm Rxa6;\n" +
            "8/6Bp/6p1/2k1p3/4PPP1/1pb4P/8/2K5 b - - bm b2;\n" +
            "2r1rbk1/p1Bq1ppp/Ppn1b3/1Npp4/B7/3P2Q1/1PP2PPP/R4RK1 w - - bm Nxa7;\n" +
            "r4rk1/ppq3pp/2p1Pn2/4p1Q1/8/2N5/PP4PP/2KR1R2 w - - bm Rxf6;\n" +
            "6k1/p4pp1/Pp2r3/1QPq3p/8/6P1/2P2P1P/1R4K1 w - - bm cxb6;\n" +
            "8/2k5/2p5/2pb2K1/pp4P1/1P1R4/P7/8 b - - bm Bxb3;\n" +
            "2r5/1r5k/1P3p2/PR2pP1p/4P2p/2p1BP2/1p2n3/4R2K b - - bm Nd4;\n" +
            "8/1R2P3/6k1/3B4/2P2P2/1p2r3/1Kb4p/8 w - - bm Be6;\n" +
            "1q1r3k/3P1pp1/ppBR1n1p/4Q2P/P4P2/8/5PK1/8 w - - bm Rxf6;\n" +
            "6k1/5pp1/pb1r3p/8/2q1P3/1p3N1P/1P3PP1/2R1Q1K1 b - - bm Qc2;\n" +
            "8/Bpk5/8/P2K4/8/8/8/8 w - - bm Kd4;\n" +
            "1r6/5k2/p4p1K/5R2/7P/8/6P1/8 w - - bm Kh7;\n" +
            "8/6k1/p4p2/P3q2p/7P/5Q2/5PK1/8 w - - bm Qg3;\n" +
            "8/8/6p1/3Pkp2/4P3/2K5/6P1/n7 w - - bm d6;" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}
    