package tests.enginetests;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.ExtendedPositionDescriptionParser;
import javacode.graphicsandui.Art;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class )
public class QuickTest {

    private static final int timeLimit = 10000;
    private static final Engine engine = null;
    
    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();
        for (String splitUpBK : splitUpBKs) {
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EDPObject edpObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpBK);
            objectAndName[0] = edpObject;
            objectAndName[1] = edpObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }


    private static ExtendedPositionDescriptionParser.EDPObject edpObject;

    public QuickTest(Object edp, Object name) {
        edpObject = (ExtendedPositionDescriptionParser.EDPObject) edp;
    }

    @Test
    public void test() {
        WACTests.reset();
        System.out.println(Art.boardArt(edpObject.getBoard()));
        Move move = engine.searchFixedTime(edpObject.getBoard(), timeLimit);
        System.out.println(move);

        List<Integer> winningMoveDestination = edpObject.getBestMoveDestinationIndex();
        int myMoveDestination = move.destinationIndex;

        Assert.assertTrue(winningMoveDestination.contains(myMoveDestination));

        List<Integer> losingMoveDestination = edpObject.getAvoidMoveDestinationIndex();

        Assert.assertFalse(losingMoveDestination.contains(myMoveDestination));
    }


    private static final String bkTests = "" +
            "1n1r1rk1/ppq2ppp/3p2b1/3B1NP1/4PB1R/bP2P2P/P1P5/3KQ1R1 w - - bm Qc3; id Quick-01;\n" +
            "1q6/r4pbk/1r1p2pp/B2Pn3/Q2NP3/1p3P2/6PP/1R3RK1 b - - bm Rxa5; id Quick-02;\n" +
            "3Q4/3p4/P2p4/N2b4/8/4P3/5p1p/5Kbk w - - bm Qa8; id Quick-03;\n" +
            "4qrk1/3nppb1/R1Np2p1/3P2P1/1Pr5/4B3/5Q1P/5R1K w - - bm Ra8; id Quick-04;\n" +
            "r3r3/2R2pk1/p2p1bpp/3P4/q2pQ3/5N1P/5PP1/1R4K1 w - - bm Rxf7+; id Quick-05;\n" +
            "r1b1Rbk1/pp3p2/2npN2p/2qp2p1/8/1QPB3P/PP3PPB/6K1 b - - bm Bxe6; id Quick-06;\n" +
            "r5k1/Rb4p1/2q2pBp/1pp5/1b4QN/1P2P2P/5PP1/6K1 w - - bm Rxb7; id Quick-07;\n" +
            "3R4/5r1p/5ppk/8/1Q3PPq/5P2/6K1/8 w - - bm Rg8; id Quick-08;\n" +
//            "2kr3r/ppp3pp/2pbbn2/4N3/3Pp3/2P3Pq/PP1NQP1P/R1B2RK1 w - - am Nxe4; id Quick-09;\n" +
            "2r3k1/pp1bpp1p/3p1npQ/q1r5/4P1P1/2NR1P2/PPP1N3/2K4R w - - bm g5; id Quick-10;\n" +
            "r1b2rk1/pp3p2/2p2bpQ/8/1q1P4/2N2N2/Pn3PPP/1B1RR1K1 w - - bm Bxg6; id Quick-11;\n" +
            "r2qk2r/1p1bbp2/1P2p3/p2pPp2/n2N1N1p/3PB3/5QPP/R4RK1 w kq - bm Rxa4; id Quick-12;\n" +
            "3r1n1r/1p2q1k1/p1p1P1p1/3n4/5Pp1/P5N1/1P3QP1/1BR1R1K1 w - - bm Bxg6; id Quick-13;\n" +
            "r2q1rk1/p1p3pp/b2bp3/2pp4/6p1/2NPPN2/PPP2PP1/R1BQR1K1 w - - bm Ne5; id Quick-14;\n" +
            "r2qr1k1/p2b1ppp/5n2/2pp4/5b2/NP6/PBP1NPPP/R3QRK1 b - - bm Bxh2+; id Quick-15;\n" +
            "3k4/p7/K3BP2/8/7p/8/2P4P/8 w - - bm Kb7; id Quick-16;\n" +
            "rq4k1/pp1nrppp/4bn2/6R1/3QP3/P4PN1/4B1PP/2B2RK1 w - - bm Rxg7+; id Quick-17;\n" +
            "2r4k/pb2q2P/1p6/3Pp3/4p3/1P2R3/PBrQ2PP/5RK1 w - - bm Qb4; id Quick-18;\n" +
            "5k2/6p1/2p2p2/P7/1Q6/2P1pqPP/7K/8 b - - bm c5; id Quick-19;\n" +
            "rnbq1b1r/ppp1p1pp/1n1p2k1/4P1N1/8/5Q2/PPPP1PPP/RNB1K2R b KQ - bm Qe8; id Quick-20;\n" +
            "r1b1kb1r/2q2ppp/p2ppP2/1pn3P1/3NP3/2N2Q2/PPP4P/2KR1B1R w kq - bm Bxb5+; id Quick-21;\n" +
//            "4r2k/3n3p/2q3p1/2p1p1Q1/1pP1P3/1P6/5PP1/R2B2K1 b - - am Qxe4; id Quick-22;\n" +
            "r3r1k1/1Bp1qppp/3p1n2/pNb5/2P5/PQ6/1P3PPP/R2R2K1 b - - bm Ng4; id Quick-23;\n" +
            "3B4/1R3p1k/2p4p/2Pp3r/3P4/4Q1K1/6P1/3b1q2 w - - bm Bf6; id Quick-24;" +
            "";

    private static final String[] splitUpBKs = bkTests.split("\\\n");
    static int totalBKs = splitUpBKs.length;
}
    
    /*
    1n1r1rk1/ppq2ppp/3p2b1/3B1NP1/4PB1R/bP2P2P/P1P5/3KQ1R1 w - - bm Qc3; id Quick-01;
1q6/r4pbk/1r1p2pp/B2Pn3/Q2NP3/1p3P2/6PP/1R3RK1 b - - bm Rxa5; id Quick-02;
3Q4/3p4/P2p4/N2b4/8/4P3/5p1p/5Kbk w - - bm Qa8; id Quick-03;
4qrk1/3nppb1/R1Np2p1/3P2P1/1Pr5/4B3/5Q1P/5R1K w - - bm Ra8; id Quick-04;
r3r3/2R2pk1/p2p1bpp/3P4/q2pQ3/5N1P/5PP1/1R4K1 w - - bm Rxf7+; id Quick-05;
r1b1Rbk1/pp3p2/2npN2p/2qp2p1/8/1QPB3P/PP3PPB/6K1 b - - bm Bxe6; id Quick-06;
r5k1/Rb4p1/2q2pBp/1pp5/1b4QN/1P2P2P/5PP1/6K1 w - - bm Rxb7; id Quick-07;
3R4/5r1p/5ppk/8/1Q3PPq/5P2/6K1/8 w - - bm Rg8; id Quick-08;
2kr3r/ppp3pp/2pbbn2/4N3/3Pp3/2P3Pq/PP1NQP1P/R1B2RK1 w - - am Nxe4; id Quick-09;
2r3k1/pp1bpp1p/3p1npQ/q1r5/4P1P1/2NR1P2/PPP1N3/2K4R w - - bm g5; id Quick-10;
r1b2rk1/pp3p2/2p2bpQ/8/1q1P4/2N2N2/Pn3PPP/1B1RR1K1 w - - bm Bxg6; id Quick-11;
r2qk2r/1p1bbp2/1P2p3/p2pPp2/n2N1N1p/3PB3/5QPP/R4RK1 w kq - bm Rxa4; id Quick-12;
3r1n1r/1p2q1k1/p1p1P1p1/3n4/5Pp1/P5N1/1P3QP1/1BR1R1K1 w - - bm Bxg6; id Quick-13;
r2q1rk1/p1p3pp/b2bp3/2pp4/6p1/2NPPN2/PPP2PP1/R1BQR1K1 w - - bm Ne5; id Quick-14;
r2qr1k1/p2b1ppp/5n2/2pp4/5b2/NP6/PBP1NPPP/R3QRK1 b - - bm Bxh2+; id Quick-15;
3k4/p7/K3BP2/8/7p/8/2P4P/8 w - - bm Kb7; id Quick-16;
rq4k1/pp1nrppp/4bn2/6R1/3QP3/P4PN1/4B1PP/2B2RK1 w - - bm Rxg7+; id Quick-17;
2r4k/pb2q2P/1p6/3Pp3/4p3/1P2R3/PBrQ2PP/5RK1 w - - bm Qb4; id Quick-18;
5k2/6p1/2p2p2/P7/1Q6/2P1pqPP/7K/8 b - - bm c5; id Quick-19;
rnbq1b1r/ppp1p1pp/1n1p2k1/4P1N1/8/5Q2/PPPP1PPP/RNB1K2R b KQ - bm Qe8; id Quick-20;
r1b1kb1r/2q2ppp/p2ppP2/1pn3P1/3NP3/2N2Q2/PPP4P/2KR1B1R w kq - bm Bxb5+; id Quick-21;
4r2k/3n3p/2q3p1/2p1p1Q1/1pP1P3/1P6/5PP1/R2B2K1 b - - am Qxe4; id Quick-22;
r3r1k1/1Bp1qppp/3p1n2/pNb5/2P5/PQ6/1P3PPP/R2R2K1 b - - bm Ng4; id Quick-23;
3B4/1R3p1k/2p4p/2Pp3r/3P4/4Q1K1/6P1/3b1q2 w - - bm Bf6; id Quick-24;
     */