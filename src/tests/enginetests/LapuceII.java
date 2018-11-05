package tests.enginetests;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.ExtendedPositionDescriptionParser;
import javacode.graphicsandui.Art;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LapuceII {
    /*
    Arasan = 	600 sec.	33/35
(rating: 2890)
     */

    private static final int timeLimit = 600000;

    @Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();
        for (String splitUpWAC : splitUpWACs) {
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EDPObject edpObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpWAC);
            objectAndName[0] = edpObject;
            objectAndName[1] = edpObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }


    private static ExtendedPositionDescriptionParser.EDPObject edpObject;

    public LapuceII(Object edp, Object name) {
        edpObject = (ExtendedPositionDescriptionParser.EDPObject) edp;
    }

    @Test
    public void test() {
        WACTests.reset();
        System.out.println(Art.boardArt(edpObject.getBoard()));
        Move move = Engine.search(edpObject.getBoard(), timeLimit);
        System.out.println(move);

        List<Integer> winningMoveDestination = edpObject.getBestMoveDestinationIndex();
        int myMoveDestination = move.destinationIndex;

        Assert.assertTrue(winningMoveDestination.contains(myMoveDestination));
    }



    private static final String wacTests = "" +
            "r3kb1r/3n1pp1/p6p/2pPp2q/Pp2N3/3B2PP/1PQ2P2/R3K2R w KQkq - bm d6; id \"LCTPOS01 (d6!)\";\n" +
            "1k1r3r/pp2qpp1/3b1n1p/3pNQ2/2pP1P2/2N1P3/PP4PP/1K1RR3 b - - bm Bb4; id \"LCTPOS02 (...Bb4!)\";\n" +
            "r6k/pp4p1/2p1b3/3pP3/7q/P2B3r/1PP2Q1P/2K1R1R1 w - - bm Qc5; id \"LCTPOS03 (Qc5!)\";\n" +
            "1nr5/2rbkppp/p3p3/Np6/2PRPP2/8/PKP1B1PP/3R4 b - - bm e5; id \"LCTPOS04 (....e5!)\";\n" +
            "2r2rk1/1p1bq3/p3p2p/3pPpp1/1P1Q4/P7/2P2PPP/2R1RBK1 b - - bm Bb5; id \"LCTPOS05 (...Bb5!)\";\n" +
            "3r1bk1/p4ppp/Qp2p3/8/1P1B4/Pq2P1P1/2r2P1P/R3R1K1 b - - bm e5; id \"LCTPOS06 (...e5!)\";\n" +
            "r1b2r1k/pp2q1pp/2p2p2/2p1n2N/4P3/1PNP2QP/1PP2RP1/5RK1 w - - bm Nd1; id \"LCTPOS07 (Nd1!)\";\n" +
            "r2qrnk1/pp3ppb/3b1n1p/1Pp1p3/2P1P2N/P5P1/1B1NQPBP/R4RK1 w - - bm Bh3; id \"LCTPOS08 (Bh3!)\";\n" +
            "5nk1/Q4bpp/5p2/8/P1n1PN2/q4P2/6PP/1R4K1 w - - bm Qd4; id \"LCTPOS09 (Qd4!)\";\n" +
            "r3k2r/3bbp1p/p1nppp2/5P2/1p1NP3/5NP1/PPPK3P/3R1B1R b kq - bm Bf8; id \"LCTPOS10 (...Bf8!)\";\n" +
            "bn6/1q4n1/1p1p1kp1/2pPp1pp/1PP1P1P1/3N1P1P/4B1K1/2Q2N2 w - - bm h4; id \"LCTPOS11 (h4!)\";\n" +
            "3r2k1/pp2npp1/2rqp2p/8/3PQ3/1BR3P1/PP3P1P/3R2K1 b - - bm Rb6; id \"LCTPOS12 (...Rb6!)\";\n" +
            "1r2r1k1/4ppbp/B5p1/3P4/pp1qPB2/2n2Q1P/P4PP1/4RRK1 b - - bm Nxa2; id \"LCTPOS13 (...Nxa2!)\";\n" +
            "r2qkb1r/1b3ppp/p3pn2/1p6/1n1P4/1BN2N2/PP2QPPP/R1BR2K1 w kq - bm d5; id \"LCTPOS14 (d5!)\";\n" +
            "1r4k1/1q2bp2/3p2p1/2pP4/p1N4R/2P2QP1/1P3PK1/8 w - - bm Nxd6; id \"LCTCMB01 (Nxd6!)\";\n" +
            "rn3rk1/pbppq1pp/1p2pb2/4N2Q/3PN3/3B4/PPP2PPP/R3K2R w KQ - bm Qxh7+; id \"LCTCMB02 (Qxh7!)\";\n" +
            "4r1k1/3b1p2/5qp1/1BPpn2p/7n/r3P1N1/2Q1RPPP/1R3NK1 b - - bm Qf3; id \"LCTCMB03 (...Qf3!)\";\n" +
            "2k2b1r/1pq3p1/2p1pp2/p1n1PnNp/2P2B2/2N4P/PP2QPP1/3R2K1 w - - bm exf6; id \"LCTCMB04 (exf6!)\";\n" +
            "2r2r2/3qbpkp/p3n1p1/2ppP3/6Q1/1P1B3R/PBP3PP/5R1K w - - bm Rxh7+; id \"LCTCMB05 (Rxh7!)\";\n" +
            "2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w - - bm exf6; id \"LCTCMB06 (exf6!)\";\n" +
            "2rr2k1/1b3ppp/pb2p3/1p2P3/1P2BPnq/P1N3P1/1B2Q2P/R4R1K b - - bm Rxc3; id \"LCTCMB07 (...Rxc3!)\";\n" +
            "2b1r1k1/r4ppp/p7/2pNP3/4Q3/q6P/2P2PP1/3RR1K1 w - - bm Nf6+; id \"LCTCMB08 (Nf6!)\";\n" +
            "6k1/5p2/3P2p1/7n/3QPP2/7q/r2N3P/6RK b - - bm Rxd2; id \"LCTCMB09 (...Rxd2!)\";\n" +
            "rq2rbk1/6p1/p2p2Pp/1p1Rn3/4PB2/6Q1/PPP1B3/2K3R1 w - - bm Bxh6; id \"LCTCMB10 (Bxh6!)\";\n" +
            "rnbq2k1/p1r2p1p/1p1p1Pp1/1BpPn1N1/P7/2P5/6PP/R1B1QRK1 w - - bm Nxh7; id \"LCTCMB11 (Nxh7!)\";\n" +
            "r2qrb1k/1p1b2p1/p2ppn1p/8/3NP3/1BN5/PPP3QP/1K3RR1 w - - bm e5; id \"LCTCMB12 (e5!)\";\n" +
            "8/1p3pp1/7p/5P1P/2k3P1/8/2K2P2/8 w - - bm f6; id \"LCTFIN01 (f6!)\";\n" +
            "8/pp2r1k1/2p1p3/3pP2p/1P1P1P1P/P5KR/8/8 w - - bm f5; id \"LCTFIN02 (f5!)\";\n" +
            "8/3p4/p1bk3p/Pp6/1Kp1PpPp/2P2P1P/2P5/5B2 b - - bm Bxe4; id \"LCTFIN03 (...Bxe4!)\";\n" +
            "5k2/7R/4P2p/5K2/p1r2P1p/8/8/8 b - - bm h3; id \"LCTFIN04 (...h3!)\";\n" +
            "6k1/6p1/7p/P1N5/1r3p2/7P/1b3PP1/3bR1K1 w - - bm a6; id \"LCTFIN05 (a6!)\";\n" +
            "8/3b4/5k2/2pPnp2/1pP4N/pP1B2P1/P3K3/8 b - - bm f4; id \"LCTFIN06 (...f4!)\";\n" +
            "6k1/4pp1p/3p2p1/P1pPb3/R7/1r2P1PP/3B1P2/6K1 w - - bm Bb4; id \"LCTFIN07 (Bb4!)\";\n" +
            "2k5/p7/Pp1p1b2/1P1P1p2/2P2P1p/3K3P/5B2/8 w - - bm c5; id \"LCTFIN08 (c5!)\";\n" +
            "8/5Bp1/4P3/6pP/1b1k1P2/5K2/8/8 w - - bm Kg4; id \"LCTFIN09 (Kg4!)\";\n" +
            "";

    private static final String[] splitUpWACs = wacTests.split("\\\n");
    static int totalWACS = splitUpWACs.length;

}
    
    

    /*
r3kb1r/3n1pp1/p6p/2pPp2q/Pp2N3/3B2PP/1PQ2P2/R3K2R w KQkq - bm d6; id "LCTPOS01 (d6!)";
1k1r3r/pp2qpp1/3b1n1p/3pNQ2/2pP1P2/2N1P3/PP4PP/1K1RR3 b - - bm Bb4; id "LCTPOS02 (...Bb4!)";
r6k/pp4p1/2p1b3/3pP3/7q/P2B3r/1PP2Q1P/2K1R1R1 w - - bm Qc5; id "LCTPOS03 (Qc5!)";
1nr5/2rbkppp/p3p3/Np6/2PRPP2/8/PKP1B1PP/3R4 b - - bm e5; id "LCTPOS04 (....e5!)";
2r2rk1/1p1bq3/p3p2p/3pPpp1/1P1Q4/P7/2P2PPP/2R1RBK1 b - - bm Bb5; id "LCTPOS05 (...Bb5!)";
3r1bk1/p4ppp/Qp2p3/8/1P1B4/Pq2P1P1/2r2P1P/R3R1K1 b - - bm e5; id "LCTPOS06 (...e5!)";
r1b2r1k/pp2q1pp/2p2p2/2p1n2N/4P3/1PNP2QP/1PP2RP1/5RK1 w - - bm Nd1; id "LCTPOS07 (Nd1!)";
r2qrnk1/pp3ppb/3b1n1p/1Pp1p3/2P1P2N/P5P1/1B1NQPBP/R4RK1 w - - bm Bh3; id "LCTPOS08 (Bh3!)";
5nk1/Q4bpp/5p2/8/P1n1PN2/q4P2/6PP/1R4K1 w - - bm Qd4; id "LCTPOS09 (Qd4!)";
r3k2r/3bbp1p/p1nppp2/5P2/1p1NP3/5NP1/PPPK3P/3R1B1R b kq - bm Bf8; id "LCTPOS10 (...Bf8!)";
bn6/1q4n1/1p1p1kp1/2pPp1pp/1PP1P1P1/3N1P1P/4B1K1/2Q2N2 w - - bm h4; id "LCTPOS11 (h4!)";
3r2k1/pp2npp1/2rqp2p/8/3PQ3/1BR3P1/PP3P1P/3R2K1 b - - bm Rb6; id "LCTPOS12 (...Rb6!)";
1r2r1k1/4ppbp/B5p1/3P4/pp1qPB2/2n2Q1P/P4PP1/4RRK1 b - - bm Nxa2; id "LCTPOS13 (...Nxa2!)";
r2qkb1r/1b3ppp/p3pn2/1p6/1n1P4/1BN2N2/PP2QPPP/R1BR2K1 w kq - bm d5; id "LCTPOS14 (d5!)";
1r4k1/1q2bp2/3p2p1/2pP4/p1N4R/2P2QP1/1P3PK1/8 w - - bm Nxd6; id "LCTCMB01 (Nxd6!)";
rn3rk1/pbppq1pp/1p2pb2/4N2Q/3PN3/3B4/PPP2PPP/R3K2R w KQ - bm Qxh7+; id "LCTCMB02 (Qxh7!)";
4r1k1/3b1p2/5qp1/1BPpn2p/7n/r3P1N1/2Q1RPPP/1R3NK1 b - - bm Qf3; id "LCTCMB03 (...Qf3!)";
2k2b1r/1pq3p1/2p1pp2/p1n1PnNp/2P2B2/2N4P/PP2QPP1/3R2K1 w - - bm exf6; id "LCTCMB04 (exf6!)";
2r2r2/3qbpkp/p3n1p1/2ppP3/6Q1/1P1B3R/PBP3PP/5R1K w - - bm Rxh7+; id "LCTCMB05 (Rxh7!)";
2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w - - bm exf6; id "LCTCMB06 (exf6!)";
2rr2k1/1b3ppp/pb2p3/1p2P3/1P2BPnq/P1N3P1/1B2Q2P/R4R1K b - - bm Rxc3; id "LCTCMB07 (...Rxc3!)";
2b1r1k1/r4ppp/p7/2pNP3/4Q3/q6P/2P2PP1/3RR1K1 w - - bm Nf6+; id "LCTCMB08 (Nf6!)";
6k1/5p2/3P2p1/7n/3QPP2/7q/r2N3P/6RK b - - bm Rxd2; id "LCTCMB09 (...Rxd2!)";
rq2rbk1/6p1/p2p2Pp/1p1Rn3/4PB2/6Q1/PPP1B3/2K3R1 w - - bm Bxh6; id "LCTCMB10 (Bxh6!)";
rnbq2k1/p1r2p1p/1p1p1Pp1/1BpPn1N1/P7/2P5/6PP/R1B1QRK1 w - - bm Nxh7; id "LCTCMB11 (Nxh7!)";
r2qrb1k/1p1b2p1/p2ppn1p/8/3NP3/1BN5/PPP3QP/1K3RR1 w - - bm e5; id "LCTCMB12 (e5!)";
8/1p3pp1/7p/5P1P/2k3P1/8/2K2P2/8 w - - bm f6; id "LCTFIN01 (f6!)";
8/pp2r1k1/2p1p3/3pP2p/1P1P1P1P/P5KR/8/8 w - - bm f5; id "LCTFIN02 (f5!)";
8/3p4/p1bk3p/Pp6/1Kp1PpPp/2P2P1P/2P5/5B2 b - - bm Bxe4; id "LCTFIN03 (...Bxe4!)";
5k2/7R/4P2p/5K2/p1r2P1p/8/8/8 b - - bm h3; id "LCTFIN04 (...h3!)";
6k1/6p1/7p/P1N5/1r3p2/7P/1b3PP1/3bR1K1 w - - bm a6; id "LCTFIN05 (a6!)";
8/3b4/5k2/2pPnp2/1pP4N/pP1B2P1/P3K3/8 b - - bm f4; id "LCTFIN06 (...f4!)";
6k1/4pp1p/3p2p1/P1pPb3/R7/1r2P1PP/3B1P2/6K1 w - - bm Bb4; id "LCTFIN07 (Bb4!)";
2k5/p7/Pp1p1b2/1P1P1p2/2P2P1p/3K3P/5B2/8 w - - bm c5; id "LCTFIN08 (c5!)";
8/5Bp1/4P3/6pP/1b1k1P2/5K2/8/8 w - - bm Kg4; id "LCTFIN09 (Kg4!)";

     */
