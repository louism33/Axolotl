package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.ExtendedPositionDescriptionParser;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class ReflectionTests {

    private static final int timeLimit = 60_000;
    private final boolean[][] successOrFailure = new boolean[4][50];

    @AfterAll
    public void compare(){
        System.out.println(Arrays.deepToString(successOrFailure));
        for (int x = 0; x < successOrFailure.length; x++){

            if (x > 0){
                Assert.assertArrayEquals(successOrFailure[x], successOrFailure[x - 1]);
            }

            for (int y = 0; y < successOrFailure[x].length; y++){

            }
        }
    }

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < splitReflections.length; i++) {
            String pos = splitReflections[i];
            Object[] objectAndName = new Object[3];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(pos);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            objectAndName[2] = i;
            answers.add(objectAndName);
        }
        return answers;
    }

    private static final int i = 0;
    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public ReflectionTests(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        List<Integer> winningMoveDestination = EPDObject.getBestMoves();
        System.out.println();
        System.out.println(EPDObject.getBoardFen());
        System.out.println("Move to get: " + MoveParser.toString(winningMoveDestination.get(0)));

        int move = Engine.searchFixedDepth(EPDObject.getBoard(), EngineSpecifications.MAX_DEPTH);

        int x = i / 47;
        int y = i % 47;

        successOrFailure[x][y] = winningMoveDestination.contains(move);

        Assert.assertTrue(winningMoveDestination.contains(move));
    }


    // 49 each ?
    private static final String wacTests = "" +
            "1br2rk1/1pqb1ppp/p3pn2/8/1P6/P1N1PN1P/1B3PP1/1QRR2K1 w - - bm Ne4; id \"WAC.152-XTDa.1-\";\n" +
            "1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id \"WAC.238\";\n" +
            "1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id \"WAC.293\";\n" +
            "1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id \"WAC.243\";\n" +
            "1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 \"Bg4 wins, but Re2 is far better.\"; id \"WAC.252\";\n" +
            "2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id \"WAC.230\";\n" +
            "2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id \"WAC.247\";\n" +
            "2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id \"WAC.269\";\n" +
            "2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id \"WAC.071\";\n" +
            "2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id \"WAC.091\";\n" +
            "2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id \"WAC.265\";\n" +
            "2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id \"WAC.270\";\n" +
            "2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id \"WAC.131\";\n" +
            "2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id \"WAC.241\";\n" +
            "2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id \"WAC.200\";\n" +
            "3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id \"WAC.283\";\n" +
            "3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id \"WAC.256\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id \"WAC.141\";\n" +
            "5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id \"WAC.155\";\n" +
            "5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id \"WAC.248\";\n" +
            "5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id \"WAC.291\";\n" +
            "5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id \"WAC.235\";\n" +
            "5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id \"WAC.163\";\n" +
            "6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";\n" +
            "6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id \"WAC.062\";\n" +
            "8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id \"WAC.151\";\n" +
            "8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id \"WAC.239\";\n" +
            "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.002\";\n" +
            "8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id \"WAC.229\";\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id \"WAC.100\";\n" +
            "8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id \"WAC.087\";\n" +
            "8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id \"WAC.086\";\n" +
            "nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id \"WAC.272\";\n" +
            "r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";\n" +
            "r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id \"WAC.204\";\n" +
            "r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id \"WAC.019\";\n" +
            "r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id \"WAC.176\";\n" +
            "r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id \"WAC.223\";\n" +
            "r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id \"WAC.052\";\n" +
            "r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id \"WAC.078\";\n" +
            "r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id \"WAC.214\";\n" +
            "r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id \"WAC.264\";\n" +
            "r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id \"WAC.116\";\n" +
            "r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id \"WAC.228\";\n" +
            "r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id \"WAC.092\";\n" +
            "r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id \"WAC.254\";\n" +
            "rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id \"WAC.287\";\n" +
            "rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id \"WAC.196\";\n" +
            "\n" +
            "\n" +
            "\n" +
            "1kr2rb1/ppp1bqp1/2np3p/8/6P1/P1NP1N1P/1PP3B1/1K2RRQ1 w - - bm Nd4; id \"WAC.152-XTDb.1-\";\n" +
            "1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id \"WAC.238\";\n" +
            "1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id \"WAC.293\";\n" +
            "1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id \"WAC.243\";\n" +
            "1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 \"Bg4 wins, but Re2 is far better.\"; id \"WAC.252\";\n" +
            "2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id \"WAC.230\";\n" +
            "2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id \"WAC.247\";\n" +
            "2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id \"WAC.269\";\n" +
            "2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id \"WAC.071\";\n" +
            "2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id \"WAC.091\";\n" +
            "2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id \"WAC.265\";\n" +
            "2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id \"WAC.270\";\n" +
            "2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id \"WAC.131\";\n" +
            "2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id \"WAC.241\";\n" +
            "2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id \"WAC.200\";\n" +
            "3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id \"WAC.283\";\n" +
            "3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id \"WAC.256\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id \"WAC.141\";\n" +
            "5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id \"WAC.155\";\n" +
            "5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id \"WAC.248\";\n" +
            "5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id \"WAC.291\";\n" +
            "5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id \"WAC.235\";\n" +
            "5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id \"WAC.163\";\n" +
            "6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";\n" +
            "6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id \"WAC.062\";\n" +
            "8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id \"WAC.151\";\n" +
            "8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id \"WAC.239\";\n" +
            "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.002\";\n" +
            "8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id \"WAC.229\";\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id \"WAC.100\";\n" +
            "8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id \"WAC.087\";\n" +
            "8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id \"WAC.086\";\n" +
            "nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id \"WAC.272\";\n" +
            "r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";\n" +
            "r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id \"WAC.204\";\n" +
            "r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id \"WAC.019\";\n" +
            "r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id \"WAC.176\";\n" +
            "r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id \"WAC.223\";\n" +
            "r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id \"WAC.052\";\n" +
            "r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id \"WAC.078\";\n" +
            "r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id \"WAC.214\";\n" +
            "r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id \"WAC.264\";\n" +
            "r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id \"WAC.116\";\n" +
            "r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id \"WAC.228\";\n" +
            "r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id \"WAC.092\";\n" +
            "r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id \"WAC.254\";\n" +
            "rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id \"WAC.287\";\n" +
            "rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id \"WAC.196\";\n" +
            "\n" +
            "\n" +
            "\n" +
            "1qrr2k1/1b3pp1/p1n1pn1p/1p6/8/P3PN2/1PQB1PPP/1BR2RK1 b - - bm Ne5; id \"WAC.152-XTDc.1-\";\n" +
            "1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id \"WAC.238\";\n" +
            "1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id \"WAC.293\";\n" +
            "1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id \"WAC.243\";\n" +
            "1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 \"Bg4 wins, but Re2 is far better.\"; id \"WAC.252\";\n" +
            "2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id \"WAC.230\";\n" +
            "2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id \"WAC.247\";\n" +
            "2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id \"WAC.269\";\n" +
            "2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id \"WAC.071\";\n" +
            "2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id \"WAC.091\";\n" +
            "2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id \"WAC.265\";\n" +
            "2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id \"WAC.270\";\n" +
            "2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id \"WAC.131\";\n" +
            "2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id \"WAC.241\";\n" +
            "2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id \"WAC.200\";\n" +
            "3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id \"WAC.283\";\n" +
            "3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id \"WAC.256\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id \"WAC.141\";\n" +
            "5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id \"WAC.155\";\n" +
            "5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id \"WAC.248\";\n" +
            "5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id \"WAC.291\";\n" +
            "5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id \"WAC.235\";\n" +
            "5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id \"WAC.163\";\n" +
            "6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";\n" +
            "6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id \"WAC.062\";\n" +
            "8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id \"WAC.151\";\n" +
            "8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id \"WAC.239\";\n" +
            "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.002\";\n" +
            "8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id \"WAC.229\";\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id \"WAC.100\";\n" +
            "8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id \"WAC.087\";\n" +
            "8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id \"WAC.086\";\n" +
            "nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id \"WAC.272\";\n" +
            "r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";\n" +
            "r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id \"WAC.204\";\n" +
            "r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id \"WAC.019\";\n" +
            "r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id \"WAC.176\";\n" +
            "r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id \"WAC.223\";\n" +
            "r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id \"WAC.052\";\n" +
            "r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id \"WAC.078\";\n" +
            "r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id \"WAC.214\";\n" +
            "r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id \"WAC.264\";\n" +
            "r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id \"WAC.116\";\n" +
            "r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id \"WAC.228\";\n" +
            "r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id \"WAC.092\";\n" +
            "r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id \"WAC.254\";\n" +
            "rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id \"WAC.287\";\n" +
            "rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id \"WAC.196\";\n" +
            "\n" +
            "\n" +
            "\n" +
            "1k2rrq1/1pp3b1/p1np1n1p/6p1/8/2NP3P/PPP1BQP1/1KR2RB1 b - - bm Nd5; id \"WAC.152-XTDd.1-\";\n" +
            "1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id \"WAC.238\";\n" +
            "1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id \"WAC.293\";\n" +
            "1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id \"WAC.243\";\n" +
            "1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 \"Bg4 wins, but Re2 is far better.\"; id \"WAC.252\";\n" +
            "2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id \"WAC.230\";\n" +
            "2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id \"WAC.247\";\n" +
            "2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id \"WAC.269\";\n" +
            "2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id \"WAC.071\";\n" +
            "2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id \"WAC.091\";\n" +
            "2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id \"WAC.265\";\n" +
            "2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id \"WAC.270\";\n" +
            "2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id \"WAC.131\";\n" +
            "2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id \"WAC.241\";\n" +
            "2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id \"WAC.200\";\n" +
            "3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id \"WAC.283\";\n" +
            "3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id \"WAC.256\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id \"WAC.141\";\n" +
            "5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id \"WAC.155\";\n" +
            "5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id \"WAC.248\";\n" +
            "5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id \"WAC.291\";\n" +
            "5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id \"WAC.235\";\n" +
            "5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id \"WAC.163\";\n" +
            "6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";\n" +
            "6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id \"WAC.062\";\n" +
            "8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id \"WAC.151\";\n" +
            "8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id \"WAC.239\";\n" +
            "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.002\";\n" +
            "8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id \"WAC.229\";\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id \"WAC.100\";\n" +
            "8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id \"WAC.087\";\n" +
            "8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id \"WAC.086\";\n" +
            "nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id \"WAC.272\";\n" +
            "r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";\n" +
            "r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id \"WAC.204\";\n" +
            "r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id \"WAC.019\";\n" +
            "r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id \"WAC.176\";\n" +
            "r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id \"WAC.223\";\n" +
            "r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id \"WAC.052\";\n" +
            "r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id \"WAC.078\";\n" +
            "r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id \"WAC.214\";\n" +
            "r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id \"WAC.264\";\n" +
            "r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id \"WAC.116\";\n" +
            "r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id \"WAC.228\";\n" +
            "r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id \"WAC.092\";\n" +
            "r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id \"WAC.254\";\n" +
            "rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id \"WAC.287\";\n" +
            "rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id \"WAC.196\";" +
            "";

    private static final String[] splitReflections = wacTests.split("\n");
}
    /*
    I suggest a few days of debugging before begining a big testing phase.

For instance, do reflections of some interesting positions and see if they all get exactly the same score with eval. For instance, each of the following 4 sets are exactly the same except for rotation or color reversal:
1br2rk1/1pqb1ppp/p3pn2/8/1P6/P1N1PN1P/1B3PP1/1QRR2K1 w - - bm Ne4; id "WAC.152-XTDa.1-";
1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id "WAC.238";
1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id "WAC.293";
1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id "WAC.243";
1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 "Bg4 wins, but Re2 is far better."; id "WAC.252";
2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id "WAC.230";
2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id "WAC.247";
2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id "WAC.269";
2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id "WAC.071";
2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id "WAC.091";
2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id "WAC.265";
2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id "WAC.270";
2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id "WAC.131";
2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id "WAC.241";
2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id "WAC.200";
3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id "WAC.283";
3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id "WAC.256";
4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id "WAC.141";
5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id "WAC.155";
5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id "WAC.248";
5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id "WAC.291";
5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id "WAC.235";
5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id "WAC.163";
6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id "WAC.111";
6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id "WAC.062";
8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id "WAC.151";
8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id "WAC.239";
8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id "WAC.002";
8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id "WAC.229";
8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id "WAC.100";
8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id "WAC.087";
8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id "WAC.086";
nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id "WAC.272";
r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id "WAC.133";
r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id "WAC.204";
r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id "WAC.019";
r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id "WAC.176";
r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id "WAC.223";
r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id "WAC.052";
r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id "WAC.078";
r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id "WAC.214";
r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id "WAC.264";
r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id "WAC.116";
r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id "WAC.228";
r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id "WAC.092";
r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id "WAC.254";
rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id "WAC.287";
rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id "WAC.196";



1kr2rb1/ppp1bqp1/2np3p/8/6P1/P1NP1N1P/1PP3B1/1K2RRQ1 w - - bm Nd4; id "WAC.152-XTDb.1-";
1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id "WAC.238";
1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id "WAC.293";
1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id "WAC.243";
1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 "Bg4 wins, but Re2 is far better."; id "WAC.252";
2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id "WAC.230";
2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id "WAC.247";
2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id "WAC.269";
2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id "WAC.071";
2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id "WAC.091";
2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id "WAC.265";
2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id "WAC.270";
2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id "WAC.131";
2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id "WAC.241";
2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id "WAC.200";
3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id "WAC.283";
3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id "WAC.256";
4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id "WAC.141";
5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id "WAC.155";
5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id "WAC.248";
5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id "WAC.291";
5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id "WAC.235";
5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id "WAC.163";
6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id "WAC.111";
6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id "WAC.062";
8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id "WAC.151";
8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id "WAC.239";
8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id "WAC.002";
8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id "WAC.229";
8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id "WAC.100";
8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id "WAC.087";
8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id "WAC.086";
nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id "WAC.272";
r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id "WAC.133";
r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id "WAC.204";
r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id "WAC.019";
r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id "WAC.176";
r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id "WAC.223";
r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id "WAC.052";
r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id "WAC.078";
r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id "WAC.214";
r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id "WAC.264";
r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id "WAC.116";
r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id "WAC.228";
r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id "WAC.092";
r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id "WAC.254";
rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id "WAC.287";
rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id "WAC.196";



1qrr2k1/1b3pp1/p1n1pn1p/1p6/8/P3PN2/1PQB1PPP/1BR2RK1 b - - bm Ne5; id "WAC.152-XTDc.1-";
1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id "WAC.238";
1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id "WAC.293";
1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id "WAC.243";
1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 "Bg4 wins, but Re2 is far better."; id "WAC.252";
2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id "WAC.230";
2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id "WAC.247";
2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id "WAC.269";
2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id "WAC.071";
2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id "WAC.091";
2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id "WAC.265";
2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id "WAC.270";
2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id "WAC.131";
2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id "WAC.241";
2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id "WAC.200";
3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id "WAC.283";
3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id "WAC.256";
4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id "WAC.141";
5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id "WAC.155";
5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id "WAC.248";
5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id "WAC.291";
5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id "WAC.235";
5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id "WAC.163";
6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id "WAC.111";
6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id "WAC.062";
8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id "WAC.151";
8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id "WAC.239";
8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id "WAC.002";
8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id "WAC.229";
8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id "WAC.100";
8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id "WAC.087";
8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id "WAC.086";
nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id "WAC.272";
r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id "WAC.133";
r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id "WAC.204";
r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id "WAC.019";
r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id "WAC.176";
r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id "WAC.223";
r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id "WAC.052";
r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id "WAC.078";
r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id "WAC.214";
r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id "WAC.264";
r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id "WAC.116";
r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id "WAC.228";
r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id "WAC.092";
r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id "WAC.254";
rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id "WAC.287";
rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id "WAC.196";



1k2rrq1/1pp3b1/p1np1n1p/6p1/8/2NP3P/PPP1BQP1/1KR2RB1 b - - bm Nd5; id "WAC.152-XTDd.1-";
1k1r4/pp1r1pp1/4n1p1/2R5/2Pp1qP1/3P2QP/P4PB1/1R4K1 w - - bm Bxb7; id "WAC.238";
1nbq1r1k/3rbp1p/p1p1pp1Q/1p6/P1pPN3/5NP1/1P2PPBP/R4RK1 w - - bm Nfg5; id "WAC.293";
1r3r1k/3p4/1p1Nn1R1/4Pp1q/pP3P1p/P7/5Q1P/6RK w - - bm Qe2; id "WAC.243";
1rb1r1k1/p1p2ppp/5n2/2pP4/5P2/2QB4/qNP3PP/2KRB2R b - - bm Bg4 Re2; c0 "Bg4 wins, but Re2 is far better."; id "WAC.252";
2b5/1r6/2kBp1p1/p2pP1P1/2pP4/1pP3K1/1R3P2/8 b - - bm Rb4; id "WAC.230";
2k1r3/1p2Bq2/p2Qp3/Pb1p1p1P/2pP1P2/2P5/2P2KP1/1R6 w - - bm Rxb5; id "WAC.247";
2kr2nr/pp1n1ppp/2p1p3/q7/1b1P1B2/P1N2Q1P/1PP1BPP1/R3K2R w KQ - bm axb4; id "WAC.269";
2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id "WAC.071";
2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id "WAC.091";
2r1k2r/2pn1pp1/1p3n1p/p3PP2/4q2B/P1P5/2Q1N1PP/R4RK1 w k - bm exf6; id "WAC.265";
2r1r1k1/pp1q1ppp/3p1b2/3P4/3Q4/5N2/PP2RPPP/4R1K1 w - - bm Qg4; id "WAC.270";
2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id "WAC.131";
2rq1rk1/pp3ppp/2n2b2/4NR2/3P4/PB5Q/1P4PP/3R2K1 w - - bm Qxh7+; id "WAC.241";
2rqrn1k/pb4pp/1p2pp2/n2P4/2P3N1/P2B2Q1/1B3PPP/2R1R1K1 w - - bm Bxf6; id "WAC.200";
3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id "WAC.283";
3r1rk1/1pb1qp1p/2p3p1/p7/P2Np2R/1P5P/1BP2PP1/3Q1BK1 w - - bm Nf5; id "WAC.256";
4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id "WAC.141";
5bk1/1rQ4p/5pp1/2pP4/3n1PP1/7P/1q3BB1/4R1K1 w - - bm d6; id "WAC.155";
5r1k/1p4pp/3q4/3Pp1R1/8/8/PP4PP/4Q1K1 b - - bm Qc5+; id "WAC.248";
5r1k/3b2p1/p6p/1pRpR3/1P1P2q1/P4pP1/5QnP/1B4K1 w - - bm h3; id "WAC.291";
5r2/1p1RRrk1/4Qq1p/1PP3p1/8/4B3/1b3P1P/6K1 w - - bm Qxf7+ Rxf7+; id "WAC.235";
5rk1/2p4p/2p4r/3P4/4p1b1/1Q2NqPp/PP3P1K/R4R2 b - - bm Qg2+; id "WAC.163";
6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id "WAC.111";
6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id "WAC.062";
8/3b2kp/4p1p1/pr1n4/N1N4P/1P4P1/1K3P2/3R4 w - - bm Nc3; id "WAC.151";
8/6k1/5pp1/Q6p/5P2/6PK/P4q1P/8 b - - bm Qf1+; id "WAC.239";
8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id "WAC.002";
8/8/8/1p5r/p1p1k1pN/P2pBpP1/1P1K1P2/8 b - - bm Rxh4 b4; id "WAC.229";
8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id "WAC.100";
8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id "WAC.087";
8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id "WAC.086";
nrq4r/2k1p3/1p1pPnp1/pRpP1p2/P1P2P2/2P1BB2/1R2Q1P1/6K1 w - - bm Bxc5; id "WAC.272";
r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id "WAC.133";
r1b1qrk1/1p3ppp/p1p5/3Nb3/5N2/P7/1P4PQ/K1R1R3 w - - bm Rxe5; id "WAC.204";
r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id "WAC.019";
r1bq3r/ppp2pk1/3p1pp1/8/2BbPQ2/2NP2P1/PPP4P/R4R1K b - - bm Rxh2+; id "WAC.176";
r1bqk2r/pp3ppp/5n2/8/1b1npB2/2N5/PP1Q2PP/1K2RBNR w kq - bm Nxe4; id "WAC.223";
r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id "WAC.052";
r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id "WAC.078";
r2r2k1/1p2qpp1/1np1p1p1/p3N3/2PPN3/bP5R/4QPPP/4R1K1 w - - bm Ng5; id "WAC.214";
r2r2k1/1R2qp2/p5pp/2P5/b1PN1b2/P7/1Q3PPP/1B1R2K1 b - - bm Rab8; id "WAC.264";
r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id "WAC.116";
r4rk1/1bq1bp1p/4p1p1/p2p4/3BnP2/1N1B3R/PPP3PP/R2Q2K1 w - - bm Bxe4; id "WAC.228";
r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id "WAC.092";
r6k/pp3p1p/2p1bp1q/b3p3/4Pnr1/2PP2NP/PP1Q1PPN/R2B2RK b - - bm Nxh3; id "WAC.254";
rn3k1r/pp2bBpp/2p2n2/q5N1/3P4/1P6/P1P3PP/R1BQ1RK1 w - - bm Qh5; id "WAC.287";
rr4k1/p1pq2pp/Q1n1pn2/2bpp3/4P3/2PP1NN1/PP3PPP/R1B1K2R b KQ - bm Nb4; id "WAC.196";

     */
