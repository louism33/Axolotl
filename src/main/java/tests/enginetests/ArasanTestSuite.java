package tests.enginetests;

import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;
import javacode.chessprogram.miscAdmin.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ArasanTestSuite {

    private static final int timeLimit = 10000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();
        for (String splitUpArasan : splitUpArasan) {
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpArasan);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }


    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public ArasanTestSuite(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        WACTests.reset();
        System.out.println(Art.boardArt(EPDObject.getBoard()));
        Move move = new Engine().searchFixedTime(EPDObject.getBoard(), timeLimit);
        System.out.println(move);

        List<Integer> winningMoveDestination = EPDObject.getBestMoveDestinationIndex();
        int myMoveDestination = move.destinationIndex;

        Assert.assertTrue(winningMoveDestination.contains(myMoveDestination));
        
    }


    private static final String arasanTests = "" +
            "r1bq1r1k/p1pnbpp1/1p2p3/6p1/3PB3/5N2/PPPQ1PPP/2KR3R w - - bm g4; id \"arasan20.1\"; \n" +
            "r1b2rk1/1p1nbppp/pq1p4/3B4/P2NP3/2N1p3/1PP3PP/R2Q1R1K w - - bm Rxf7; id \"arasan20.2\";\n" +
            "r1q1k2r/1p1nbpp1/2p2np1/p1Pp4/3Pp3/P1N1P1P1/1P1B1P1P/R2QRBK1 b kq - bm Bxc5; id \"arasan20.3\";\n" +
            "2rr3k/2qnbppp/p1n1p3/1p1pP3/3P1N2/1Q1BBP2/PP3P1P/1KR3R1 w - - bm Bxh7; id \"arasan20.4\";\n" +
            "3q1r1k/1b3ppp/p1n5/1p1pPB2/2rP4/P6N/1P2Q1PP/R4RK1 w - - bm Qh5; id \"arasan20.5\"; \n" +
            "r1b1k2r/1p1pppb1/p5pp/3P4/q2p1B2/3P1Q2/PPP2PPP/R3R1K1 w kq - bm Rxe7+; id \"arasan20.6\"; \n" +
            "R4bk1/2Bbp2p/2p2pp1/1rPp4/3P4/4P2P/4BPPK/1q1Q4 w - - bm Qa4; id \"arasan20.7\"; \n" +
            "r1r3k1/p3bppp/2bp3Q/q2pP1P1/1p1BP3/8/PPP1B2P/2KR2R1 w - - bm e6; id \"arasan20.8\"; \n" +
            "b2rk1r1/p3q3/2p5/3nPR2/3P2pp/1R1B2P1/P1Q2P2/6K1 w - - bm Bc4; id \"arasan20.9\"; \n" +
            "r2q3r/1p1bbQ2/4p1Bk/3pP3/1n1P1P1p/pP6/Pn4PP/R1B1R1K1 w - - bm g4; id \"arasan20.10\"; \n" +
            "1r2brk1/4n1p1/4p2p/p2pP1qP/2pP1NP1/P1Q1BK2/2P4R/6R1 b - - bm Bg6; id \"arasan20.11\";\n" +
            "1rb2k1r/2q2pp1/p2b3p/2n3B1/2QN4/3B4/PpP3PP/1K2R2R w - - bm Bd8; id \"arasan20.12\";\n" +
            "5rk1/1pp3p1/3ppr1p/pP2p2n/4P2q/P2PQ2P/2P1NPP1/R4RK1 b - - bm Rf3; id \"arasan20.13\"; \n" +
            "r4rk1/1b1n1pb1/3p2p1/1p1Pq1Bp/2p1P3/2P2RNP/1PBQ2P1/5R1K w - - bm Nf5; id \"arasan20.14\";\n" +
            "2kr2r1/ppq1bp1p/4pn2/2p1n1pb/4P1P1/2P2N1P/PPBNQP2/R1B1R1K1 b - - bm Nfxg4; id \"arasan20.15\";\n" +
            "8/3r4/pr1Pk1p1/8/7P/6P1/3R3K/5R2 w - - bm Re2+; id \"arasan20.16\"; \n" +
            "3r1rk1/q4pp1/n1bNp2p/p7/pn2P1N1/6P1/1P1Q1PBP/2RR2K1 w - - bm Nxh6+ e5; id \"arasan20.17\";\n" +
            "r1q2rk1/ppnbbpp1/n4P1p/4P3/3p4/2N1B1PP/PP4BK/R2Q1R2 w - - bm Bxh6; id \"arasan20.18\"; \n" +
            "1R6/5p1k/4bPpp/3pN3/2pP1P1P/2r5/6PK/8 w - - bm h5; id \"arasan20.19\"; \n" +
            "3q1rk1/pr1b1p1p/1bp2p2/2ppP3/8/2P1BN2/PPQ3PP/R4RK1 w - - bm Bh6; id \"arasan20.20\"; \n" +
            "8/5pk1/p4npp/1pPN4/1P2p3/1P4PP/5P2/5K2 w - - bm Nxf6; id \"arasan20.21\"; \n" +
            "8/6p1/P1b1pp2/2p1p3/1k4P1/3PP3/1PK5/5B2 w - - bm Bg2; id \"arasan20.22\"; \n" +
            "r5n1/p1p1q2k/4b2p/3pB3/3PP1pP/8/PPPQ2P1/5RK1 w - - bm Qf4; id \"arasan20.23\"; \n" +
            "2b2rk1/r3q1pp/1nn1p3/3pP1NP/p1pP2Q1/2P1N3/1P1KBP2/R5R1 w - - bm Nxh7; id \"arasan20.24\";\n" +
            "rnb3k1/p3qpr1/2p1p3/2NP3p/1pP3p1/3BQ3/P4PP1/4RRK1 w - - bm Qd4; id \"arasan20.25\"; \n" +
            "r3r1k1/p3bppp/q1b2n2/5Q2/1p1B4/1BNR4/PPP3PP/2K2R2 w - - bm Rg3; id \"arasan20.26\"; ;\n" +
            "2bq1rk1/rpb2p2/2p1p1p1/p1N3Np/P2P1P1P/1Q2R1P1/1P3P2/3R2K1 w - - bm f5; id \"arasan20.27\"; \n" +
            "3q1r1k/2r2pp1/p6p/1pbppP1N/3pP1PP/3P1Q2/PPP4R/5RK1 w - - bm g5; id \"arasan20.28\";\n" +
            "1q6/6k1/5Np1/1r4Pp/2p4P/2Nrb3/PP6/KR5Q b - - bm Bd4; id \"arasan20.29\";\n" +
            "b2rk3/r4p2/p3p3/P3Q1Np/2Pp3P/8/6P1/6K1 w - - bm Qh8+; id \"arasan20.30\";\n" +
            "2kr1b1r/1pp1ppp1/p7/q2P3n/2BB1pb1/2NQ4/P1P1N3/1R3RK1 w - - bm Rxb7; id \"arasan20.31\"; \n" +
            "r4rkq/1ppb4/3P2n1/1N1Pp3/P3Pbn1/3B1NP1/1P2QB2/R4RK1 b - - bm Rf7; id \"arasan20.32\"; \n" +
            "br4k1/1qrnbppp/pp1ppn2/8/NPPBP3/PN3P2/5QPP/2RR1B1K w - - bm Nxb6; id \"arasan20.33\"; \n" +
            "r2q1rk1/ppp2p2/3p1np1/4pNQ1/4P1pP/1PPP4/1P3P2/R3K1R1 w Q - bm Qh6; id \"arasan20.34\";\n" +
            "1qb2rk1/3p1pp1/1p6/1pbBp3/r5p1/3QB3/PPP2P1P/2KR2R1 w - - bm b3; id \"arasan20.35\";\n" +
            "r1b2q1k/2Qn1p1p/1p1Rpp2/p6B/4P2P/6N1/P4PP1/6K1 w - - bm e5; id \"arasan20.36\"; \n" +
            "r2q1rk1/p2pn3/bpp2p1p/3Nb1pQ/7B/8/PPB2PPP/R3R1K1 w - - bm Bxg5; id \"arasan20.37\"; \n" +
            "r4rk1/p4ppp/qp2p3/b5B1/n1R5/5N2/PP2QPPP/1R4K1 w - - bm Bf6; id \"arasan20.38\"; \n" +
            "r2q1rk1/4bppp/3pb3/2n1pP2/1p2P1PP/1P3Q2/1BP1N1B1/2KR3R b - - bm Ra2; id \"arasan20.39\";\n" +
            "2r2rkb/1Q1b3p/p2p3q/2PPnp2/1P2p1p1/2N5/P3BPPB/4RRK1 b - - bm e3; id \"arasan20.40\"; \n" +
            "2b1rk2/5p2/p1P5/2p2P2/2p5/7B/P7/2KR4 w - - bm f6; id \"arasan20.41\";\n" +
            "rn1qr1k1/1p2bppp/p3p3/3pP3/P2P1B2/2RB1Q1P/1P3PP1/R5K1 w - - bm Bxh7+; id \"arasan20.42\"; \n" +
            "1k4rr/p1pq2b1/1p6/1P1pp1p1/6n1/2PP2QN/PN1BP1B1/5RK1 b - - bm e4; id \"arasan20.43\";\n" +
            "1n3rk1/3rbppp/p2p4/4pP2/Ppq1P3/1N2B3/1PP3PP/R2Q1R1K w - - bm f6; id \"arasan20.44\"; \n" +
            "8/2p1k3/3p3p/2PP1pp1/1P1K1P2/6P1/8/8 w - - bm g4; id \"arasan20.45\"; \n" +
            "r1b2rk1/pp2bppp/3p4/q7/3BN1n1/1B3Q2/PPP3PP/R4RK1 w - - bm Qxf7+; id \"arasan20.46\"; \n" +
            "r1b1rk2/p1pq2p1/1p1b1p1p/n2P4/2P1NP2/P2B1R2/1BQ3PP/R6K w - - bm Nxf6; id \"arasan20.47\"; \n" +
            "r2qr3/2p1b1pk/p5pp/1p2p3/nP2P1P1/1BP2RP1/P3QPK1/R1B5 w - - bm Bxh6; id \"arasan20.48\"; \n" +
            "1rbq1rk1/p5bp/3p2p1/2pP4/1p1n1BP1/3P3P/PP2N1B1/1R1Q1RK1 b - - bm Bxg4; id \"arasan20.49\"; \n" +
            "k1b4r/1p3p2/pq2pNp1/5n1p/P3QP2/1P1R1BPP/2P5/1K6 b - - am Nxg3; id \"arasan20.50\";  \n" +
            "7b/8/kq6/8/8/1N2R3/K2P4/8 w - - bm Nd4; id \"arasan20.51\";  \n" +
            "q3nrk1/4bppp/3p4/r3nPP1/4P2P/NpQ1B3/1P4B1/1K1R3R b - - bm Nc7; id \"arasan20.52\";  \n" +
            "2r5/8/6k1/P1p3p1/2R5/1P1q4/1K4Q1/8 w - - bm a6; id \"arasan20.53\";  \n" +
            "8/3R1P2/1ppP1p2/3r4/8/K7/p4k2/8 w - - bm Kb2; id \"arasan20.54\";  \n" +
            "2qrrbk1/1b3ppp/pn1Pp3/6P1/1Pp2B2/1nN2NQB/1P3P1P/3RR1K1 w - - bm g6; id \"arasan20.55\";  \n" +
            "r1q1nrk1/3nb2p/3p2p1/p5P1/1pbNPB2/1N6/PPPQ4/2KR1B1R w - - bm Nf5; id \"arasan20.56\";  \n" +
            "5rk1/pp3ppp/3q4/8/2Pp2b1/P5Pn/PBQPr1BP/4RR1K b - - bm Rxg2; id \"arasan20.57\";  \n" +
            "3b3r/1q3pk1/4b2p/3pPppQ/R1pP1P1P/1rP1N1P1/6N1/2R3K1 w - - bm g4; id \"arasan20.58\";  \n" +
            "r1b2rk1/pp1p2pR/8/1pb2p2/5N2/7Q/qPPB1PPP/6K1 w - - bm g3; id \"arasan20.59\";  \n" +
            "7q/3k2p1/n1p1p1Pr/1pPpPpQ1/3P1N1p/1P2KP2/6P1/7R w - - bm Nxd5; id \"arasan20.60\";  \n" +
            "5rk1/8/pqPp1r1p/1p1Pp1bR/4B3/5PP1/PP2Q1K1/R7 w - - bm Rxg5+; id \"arasan20.61\";  \n" +
            "3r2k1/pb3Np1/4pq1p/2pp1n2/3P4/1PQ5/P4PPP/R2R2K1 b - - bm Nxd4; id \"arasan20.62\";  \n" +
            "2kr1r2/ppq1b1p1/2n5/2PpPb1N/QP1B1pp1/2P5/P2N1P1P/R4RK1 b - - bm Rh8; id \"arasan20.63\";  \n" +
            "r1r2k2/pp2bpp1/2bppn1p/6B1/2qNPPPP/2N5/PPPQ4/1K1RR3 w - - bm f5; id \"arasan20.64\";  \n" +
            "3r2k1/6p1/B1R2p1p/1pPr1P2/3P4/8/1P3nP1/2KR4 w - - bm Rc8; id \"arasan20.65\";  \n" +
            "3qb1k1/5rb1/r3p1Np/1n1pP2P/p1pB1PQ1/2P5/R1B4K/6R1 w - - bm Bc5; id \"arasan20.66\";  \n" +
            "3q1k2/p4pb1/3Pp3/p3P3/r6p/2QB3P/3B1P2/6K1 w - - bm Bb5; id \"arasan20.67\";  \n" +
            "r4r1k/ppqbn1pp/3b1p2/2pP3B/2P4N/7P/P2B1PP1/1R1QR1K1 w - - bm Rxe7; id \"arasan20.68\";  \n" +
            "1r4k1/1q3pp1/r3b2p/p2N4/3R4/QP3P2/2P3PP/1K1R4 w - - bm Nf6+; id \"arasan20.69\";  \n" +
            "r2q1r2/1bp1npk1/3p1p1p/p3p2Q/P3P2N/1BpPP3/1P1N2PP/5RK1 w - - bm Rf3; id \"arasan20.70\";  \n" +
            "2r3k1/1bp3pp/pp1pNn1r/3P1p1q/1PP1pP2/P3P1P1/3Q3P/2RR1BK1 w - - bm c5; id \"arasan20.71\";  \n" +
            "2r3r1/1p1qb2k/p5pp/2n1Bp2/2RP3P/1P2PNQ1/5P1K/3R4 w - - bm Ng5+; id \"arasan20.72\";  \n" +
            "rn3rk1/pp1q3p/4p1B1/2p5/3N1b2/4B3/PPQ2PPP/3R2K1 w - - bm Nf5; id \"arasan20.73\";  \n" +
            "rr5k/1q2pPbp/3p2p1/PbpP4/1nB1nP1Q/1NB5/1P4PP/R4R1K w - - bm f5; id \"arasan20.74\";  \n" +
            "r4rk1/pp1qbppp/1n6/6R1/P1pP4/5Q1P/2B2PP1/2B2RK1 w - - bm Rxg7+; id \"arasan20.75\";  \n" +
            "1r4k1/p7/2P1n1pp/5p2/2QPqP2/PN2p3/5P1P/4RK2 b - - bm Rc8; id \"arasan20.76\";  \n" +
            "1qrrbbk1/1p1nnppp/p3p3/4P3/2P5/1PN1N3/PB2Q1PP/1B2RR1K w - - bm Bxh7+; id \"arasan20.77\";  \n" +
            "r1b2rk1/qp5p/p1n1ppp1/7N/4P1P1/2N1pP2/PPP5/2KR1QR1 w - - bm e5; id \"arasan20.78\";  \n" +
            "3r4/2q5/5pk1/p3n1p1/N3Pp1p/1PPr1P1P/2Q1R1P1/5R1K b - - bm g4; id \"arasan20.79\";  \n" +
            "1q2r1k1/3R1pb1/3R2p1/7p/p3N3/2P1BP1P/1P3PK1/8 b - - bm Rxe4; id \"arasan20.80\";  \n" +
            "r1b1k2r/2q2pp1/p1p1pn2/2b4p/Pp2P3/3B3P/1PP1QPP1/RNB2RK1 b kq - bm Ng4; id \"arasan20.81\";  \n" +
            "2r1rb1k/ppq2pp1/4b2p/3pP2Q/5B2/2PB2R1/P4PPP/1R4K1 w - - bm Rxg7; id \"arasan20.82\";  \n" +
            "6k1/p4qp1/1p3r1p/2pPp1p1/1PP1PnP1/2P1KR1P/1B6/7Q b - - bm h5; id \"arasan20.83\";  \n" +
            "rnb1kb1r/pp1p1ppp/1q2p3/8/3NP1n1/2N1B3/PPP2PPP/R2QKB1R w KQkq - bm Qxg4; id \"arasan20.84\";\n" +
            "r3kb1r/1b1n2p1/p3Nn1p/3Pp3/1p4PP/3QBP2/qPP5/2KR1B1R w kq - bm Qg6+; id \"arasan20.85\";  \n" +
            "1r1qrbk1/pb3p2/2p1pPpp/1p4B1/2pP2PQ/2P5/P4PBP/R3R1K1 w - - bm Bxh6; id \"arasan20.86\";  \n" +
            "2r1r2k/1b1n1p1p/p3pPp1/1p1pP2q/3N4/P3Q1P1/1PP4P/2KRRB2 w - - bm g4; id \"arasan20.87\";  \n" +
            "2r1b1k1/5p2/1R2nB2/1p2P2p/2p5/2Q1P2K/3R1PB1/r3q3 w - - bm Rxe6; id \"arasan20.88\";  \n" +
            "rn2r1k1/ppq1pp1p/2b2bp1/8/2BNPP1B/2P4P/P1Q3P1/1R3RK1 w - - bm Bxf7+; id \"arasan20.89\";  \n" +
            "1kr5/1p3p2/q3p3/pRbpPp2/P1rNnP2/2P1B1Pp/1P2Q2P/R5K1 b - - bm Bxd4; id \"arasan20.90\";  \n" +
            "r3r2k/1pq2pp1/4b2p/3pP3/p1nB3P/P2B1RQ1/1PP3P1/3R3K w - - bm Rf6; id \"arasan20.91\";  \n" +
            "r3brk1/2q1bp1p/pnn3p1/1p1pP1N1/3P4/3B2P1/PP1QNR1P/R1B3K1 w - - bm Nxh7; id \"arasan20.92\";  \n" +
            "1r3r2/q5k1/4p1n1/1bPpPp1p/pPpR1Pp1/P1B1Q3/2B3PP/3R2K1 w - - bm Rxd5; id \"arasan20.93\";  \n" +
            "rq3rk1/1b1n1ppp/ppn1p3/3pP3/5B2/2NBP2P/PP2QPP1/2RR2K1 w - - bm Nxd5; id \"arasan20.94\";  \n" +
            "7r/k4pp1/pn2p1pr/2ppP3/1q3P2/1PN2R1P/P1P2QP1/3R3K w - - bm a3; id \"arasan20.95\";  \n" +
            "1r3rk1/3bbppp/1qn2P2/p2pP1P1/3P4/2PB1N2/6K1/qNBQ1R2 w - - bm Bxh7+; id \"arasan20.96\";  \n" +
            "1r1qrbk1/5ppp/2b1p2B/2npP3/1p4QP/pP1B1N2/P1P2PP1/1K1R3R w - - bm Bxh7+; id \"arasan20.97\";  \n" +
            "r5k1/pbpq1pp1/3b2rp/N3n3/1N6/2P3B1/PP1Q1PPP/R4RK1 b - - bm Rxg3; id \"arasan20.98\";  \n" +
            "1r2r1k1/2R2p2/1N1Rp2p/p2b3P/4pPP1/8/P4K2/8 w - - bm g5; id \"arasan20.99\";  \n" +
            "r4r2/pp1b1ppk/2n1p3/3pPnB1/q1pP2QP/P1P4R/2PKNPP1/R7 w - - bm Qh5+; id \"arasan20.100\";  \n" +
            "8/2k2Bp1/2n5/p1P4p/4pPn1/P3PqPb/1r1BQ2P/2R1K1R1 b - - bm Nce5; id \"arasan20.101\";  \n" +
            "8/5kpp/8/8/8/5P2/1RPK2PP/6r1 w - - bm c4; id \"arasan20.102\";  \n" +
            "r3rnk1/pp2ppb1/1np3p1/3qP2p/3P1B2/4Q1N1/PP2BPP1/1K1R3R w - - bm Bh6; id \"arasan20.103\";  \n" +
            "1r1q2k1/2r3bp/B2p1np1/3P1p2/R1P1pP2/4B2P/P5PK/3Q1R2 b - - bm Ng4+; id \"arasan20.104\";  \n" +
            "2r1rnk1/1p2pp1p/p1np2p1/q4PP1/3NP2Q/4B2R/PPP4P/3R3K w - - bm b4; id \"arasan20.105\";  \n" +
            "2b2qk1/1r4pp/2p1p3/p2n1PPB/2p4P/2p5/P4Q2/4RRK1 w - - bm Qg3; id \"arasan20.106\";  \n" +
            "1r1rkb2/2q2p2/p2p1P1B/P1pPp2Q/2P3b1/1P6/2B3PP/5R1K w - - bm Qxg4; id \"arasan20.107\";  \n" +
            "r4rk1/3b3p/p1pb4/1p1n2p1/2P2p2/1B1P2Pq/PP1NRP1P/R1BQ2K1 w - - bm Qf1; id \"arasan20.108\";  \n" +
            "1r3rk1/4bpp1/p3p2p/q1PpPn2/bn3Q1P/1PN1BN2/2P1BPP1/1KR2R2 b - - bm Bxb3; id \"arasan20.109\";  \n" +
            "2nb2k1/1rqb1pp1/p2p1n1p/2pPp3/P1P1P3/2B1NN1P/2B2PP1/Q3R2K w - - bm Nxe5; id \"arasan20.110\";  \n" +
            "3r2k1/p1qn1p1p/4p1p1/2p1N3/8/2P3P1/PP2QP1P/4R1K1 w - - bm Nxf7; id \"arasan20.111\";  \n" +
            "r2q1rk1/pb1nbp1p/1pp1pp2/8/2BPN2P/5N2/PPP1QPP1/2KR3R w - - bm Nfg5; id \"arasan20.112\";  \n" +
            "4rr2/3bp1bk/p2q1np1/2pPp2p/2P4P/1R4N1/1P1BB1P1/1Q3RK1 w - - bm Bxh5; id \"arasan20.113\";  \n" +
            "8/8/4b1p1/2Bp3p/5P1P/1pK1Pk2/8/8 b - - bm g5 d4+; id \"arasan20.114\";  \n" +
            "8/5p2/3p2p1/1bk4p/p2pBNnP/P5P1/1P3P2/4K3 b - - bm d3; id \"arasan20.115\";  \n" +
            "8/4nk2/1p3p2/1r1p2pp/1P1R1N1P/6P1/3KPP2/8 w - - bm Nd3; id \"arasan20.116\";  \n" +
            "6k1/1bq1bpp1/p6p/2p1pP2/1rP1P1P1/2NQ4/2P4P/K2RR3 b - - bm Bd5; id \"arasan20.117\";  \n" +
            "r3r1k1/1bqnbp1p/pp1pp1p1/6P1/Pn2PP1Q/1NN1BR2/1PPR2BP/6K1 w - - bm Rh3; id \"arasan20.118\";  \n" +
            "4rrk1/1pp1n1pp/1bp1P2q/p4p2/P4P2/3R2N1/1PP2P1P/2BQRK2 w - - bm Nh5; id \"arasan20.119\";  \n" +
            "3q4/4k3/1p1b1p1r/p2Q4/3B1p1p/7P/1P4P1/3R3K w - - bm b4; id \"arasan20.120\";  \n" +
            "8/5p1k/6p1/1p1Q3p/3P4/1R2P1KP/6P1/r4q2 b - - bm h4+; id \"arasan20.121\";  \n" +
            "7k/3q1pp1/1p3r2/p1bP4/P1P2p2/1P2rNpP/2Q3P1/4RR1K b - - bm Rxf3; id \"arasan20.122\";  \n" +
            "3r3r/k1p2pb1/B1b2q2/2RN3p/3P2p1/1Q2B1Pn/PP3PKP/5R2 w - - bm Rfc1; id \"arasan20.123\";  \n" +
            "r1b3kr/pp1n2Bp/2pb2q1/3p3N/3P4/2P2Q2/P1P3PP/4RRK1 w - - bm Re5; id \"arasan20.124\";  \n" +
            "2r3k1/1q3pp1/2n1b2p/4P3/3p1BP1/Q6P/1p3PB1/1R4K1 b - - bm Rb8; id \"arasan20.125\";  \n" +
            "rn2kb1r/1b1n1p1p/p3p1p1/1p2q1B1/3N3Q/2N5/PPP3PP/2KR1B1R w kq - bm Nxe6; id \"arasan20.126\";  \n" +
            "r7/ppp3kp/2bn4/4qp2/2B1pR2/2P1Q2P/P5P1/5RK1 w - - bm Rxf5; id \"arasan20.127\";  \n" +
            "1r6/r6k/2np2p1/2pNp1qp/1pP1Pp1b/1P1P1P2/1B3P2/1Q1R1K1R b - - bm Bxf2; id \"arasan20.128\";  \n" +
            "1nr3k1/q4rpp/1p1p1n2/3Pp3/1PQ1P1b1/4B1P1/2R2NBP/2R3K1 w - - bm Qxc8+; id \"arasan20.129\";  \n" +
            "8/5rk1/p2p4/1p1P1b1p/1P1K2pP/2P3P1/4R3/5B2 w - - bm Rf2; id \"arasan20.130\";  \n" +
            "5rk1/2p1R2p/r5q1/2pPR2p/5p2/1p5P/P4PbK/2BQ4 w - - bm d6; id \"arasan20.131\";  \n" +
            "r2q1r2/1b2bpkp/p3p1p1/2ppP1P1/7R/1PN1BQR1/1PP2P1P/4K3 w - - bm Qf6+; id \"arasan20.132\";  \n" +
            "r1r3k1/1ppn2bp/p1q1p1p1/3pP3/3PB1P1/PQ3NP1/3N4/2BK3R w - - bm Ng5; id \"arasan20.133\";  \n" +
            "1rr1b1k1/1pq1bp2/p2p1np1/4p3/P2BP3/2NB2Q1/1PP3PP/4RR1K w - - bm Rxf6; id \"arasan20.134\";  \n" +
            "r1r3k1/1q3p1p/4p1pP/1bnpP1P1/pp1Q1P2/1P6/P1PR1N2/1K3B1R b - - bm axb3; id \"arasan20.135\";  \n" +
            "r1b2rk1/pppnq3/4ppp1/6N1/3P3Q/2PB4/P1PK2PP/3R3R w - - bm Nxe6; id \"arasan20.136\";  \n" +
            "3r1r1k/pp5p/4b1pb/6q1/3P4/4p1BP/PP2Q1PK/3RRB2 b - - bm Qxg3+; id \"arasan20.137\";  \n" +
            "r2r2k1/3bb1Pp/3pp1p1/p1q5/1p2PP2/P1N5/1PPQ4/1K1R1B1R w - - bm Nd5; id \"arasan20.138\";  \n" +
            "8/2R5/3p4/3P4/3k3P/2p3K1/1r4P1/8 w - - bm Kf3; id \"arasan20.139\";  \n" +
            "r1bq2k1/1pp2ppp/3prn2/p3n3/2P5/PQN1PP2/1P1PB2P/R1B2R1K b - - bm Nfg4; id \"arasan20.140\";  \n" +
            "2kr3r/pp4pp/4pp2/2pq4/P1Nn4/4Q3/KP2B1PP/2RR4 b - - am Qxg2; id \"arasan20.141\";  \n" +
            "5r2/1p4k1/pP1pP1pp/2rP2q1/4Qp2/3Bb3/P5PP/4RR1K w - - bm Rf3; id \"arasan20.142\";  \n" +
            "r2qr1k1/1b1pppbp/1p4p1/pP2P1B1/3N4/R7/1PP2PPP/3QR1K1 w - a6 bm Nf5; id \"arasan20.143\";  \n" +
            "4k3/1R6/Pb3p2/1P1n4/5p2/8/4K3/8 w - - bm Kd3; id \"arasan20.144\";  \n" +
            "r4nk1/2pq1ppp/3p4/p3pNPQ/4P3/2PP1RP1/Pr3PK1/7R w - - bm Ne3; id \"arasan20.145\";  \n" +
            "r1q2rk1/1b2bppp/p1p1p3/4B3/PP6/3B3P/2P1QPP1/R2R2K1 w - - bm Bxh7+; id \"arasan20.146\";  \n" +
            "r2qrb1k/1p1b2p1/p2ppn1p/8/3NP3/1BN5/PPP3QP/1K3RR1 w - - bm e5; id \"arasan20.147\";  \n" +
            "r2q1k1r/pp2n1pp/2nb1p2/1B1p3Q/N2P4/2P1B3/PP4PP/R4RK1 w - - bm Rxf6+; id \"arasan20.148\";  \n" +
            "4r1k1/6p1/bp2r2p/3QNp2/P2BnP2/4P2P/5qPK/3RR3 b - - bm Kh7; id \"arasan20.149\";  \n" +
            "8/5rk1/p3Q1pp/1p1P1p1b/2p1Pq1P/P4P2/1PKN4/5R2 w - - bm d6; id \"arasan20.150\";  \n" +
            "r1bqkb1r/4pppp/p1p5/2ppP3/8/2P2N2/PPP2PPP/R1BQR1K1 w kq - bm e6; id \"arasan20.151\";  \n" +
            "3r1rk1/1b2bpp1/2n1p2p/qp1n2N1/4N3/P3P3/1BB1QPPP/2R2RK1 w - - bm Qh5; id \"arasan20.152\";  \n" +
            "3R4/pp2r1pk/q1p3bp/2P2r2/PP6/2Q3P1/6BP/5RK1 w - - bm Rxf5; id \"arasan20.153\";  \n" +
            "r3k3/1p4p1/1Bb1Bp1p/P1p1bP1P/2Pp2P1/3P4/5K2/4R3 w - - bm g5; id \"arasan20.154\";  \n" +
            "1r1rb1k1/5ppp/4p3/1p1p3P/1q2P2Q/pN3P2/PPP4P/1K1R2R1 w - - bm Rxg7+; id \"arasan20.155\";  \n" +
            "1r1q1rk1/4bp1p/n3p3/pbNpP1PB/5P2/1P2B1K1/1P1Q4/2RR4 w - - bm Ne4; id \"arasan20.156\";  \n" +
            "r1bq1rk1/pp2bppp/1n2p3/3pP3/8/2RBBN2/PP2QPPP/2R3K1 w - - bm Bxh7+; id \"arasan20.157\";  \n" +
            "r6k/N1Rb2bp/p2p1nr1/3Pp2q/1P2Pp1P/5N2/P3QBP1/4R1K1 b - - bm Bh3; id \"arasan20.158\";  \n" +
            "r1b2rk1/1pq1nppp/pbn1p3/8/3N4/3BBN2/PPP1QPPP/3R1RK1 w - - bm Bxh7+; id \"arasan20.159\";  \n" +
            "3r1rk1/1b2qp1p/1p3np1/1N1p4/6n1/2NBP1K1/PBQ2PP1/3RR3 b - - bm d4; id \"arasan20.160\";  \n" +
            "br3bk1/3r1p2/3q2p1/3P2Np/2B4P/3QR1P1/3R1P1K/8 w - - bm Nxf7; id \"arasan20.161\";  \n" +
            "r3r2k/ppq3np/2p3p1/NPPp1bb1/P2Pnp2/3B1P2/2Q3PP/1RN1BRK1 b - - bm Ng3; id \"arasan20.162\";  \n" +
            "7k/5rp1/3q1p1p/2bNpQ1P/4P1P1/8/1R3PK1/8 w - - bm g5; id \"arasan20.163\";  \n" +
            "4r3/4r3/1ppqpnk1/p3Rp1p/P2P1R1Q/2PB2P1/1P3P2/6K1 w - - bm Bxf5+; id \"arasan20.164\";  \n" +
            "r3nrk1/1pqbbppp/p2pp3/2n1P3/5P2/2NBBNQ1/PPP3PP/R4RK1 w - - bm Bxh7; id \"arasan20.165\";  \n" +
            "rnbq3r/ppp2kpp/4pp2/3n4/2BP4/BQ3N2/P4PPP/4RRK1 w - - bm Ng5+; id \"arasan20.166\";  \n" +
            "8/2N5/1P2p3/5bPk/1q3b2/3Bp2P/2P5/6QK b - - bm Kh4; id \"arasan20.167\";  \n" +
            "1k1r1b1r/1p6/p4pp1/P1p1p3/2NpP1p1/1PPP2Pq/1B3P1P/2RQR1K1 b - - bm f5; id \"arasan20.168\";  \n" +
            "5r2/3rkp2/2R2p2/p2Bb2Q/1p2P2P/4q1P1/Pp6/1K1R4 b - - bm b3; id \"arasan20.169\";  \n" +
            "5rk1/qp1b1rnp/4p1p1/p2pB3/8/1R1B4/PP1QRPPP/6K1 w - - bm Bxg6; id \"arasan20.170\";  \n" +
            "6k1/5r1p/1p2Q1p1/p7/P1P2P2/2K1R1P1/2N2qb1/8 w - - bm Qd6 Qe8+; id \"arasan20.171\";  \n" +
            "4r1k1/1p4p1/p1qBp1Qp/b1pnP3/8/5NP1/1P3PKP/3R4 w - - bm Rxd5; id \"arasan20.172\";  \n" +
            "2r1k2r/pp1bb1pp/6n1/3Q1p2/1B1N4/P7/1q4PP/4RRK1 w k - bm Bxe7; id \"arasan20.173\";  \n" +
            "3b2k1/4qp2/2P4Q/3B3p/1P6/1K6/8/8 w - - bm Bc4; id \"arasan20.174\";  \n" +
            "1r2brk1/6p1/1q2p1Pp/pN1pPPb1/np1N4/5Q2/1PP1B3/1K1R3R w - - bm f6; id \"arasan20.175\";  \n" +
            "2rq1Nk1/pb3pp1/4p3/1p6/3b1Pn1/P1N5/1PQ3PP/R1B2R1K b - - bm f5; id \"arasan20.176\";  \n" +
            "r1b2rk1/1p4p1/p1n1p3/3p1pB1/NqP3n1/b2BP3/1PQN1P1P/1K4RR w - - bm Rxg4; id \"arasan20.177\";  \n" +
            "q2rn1k1/1b3p1p/1p4p1/2n1B1P1/r1PN3P/P4P2/4Q1B1/3RR1K1 w - - bm Bf6; id \"arasan20.178\";  \n" +
            "r1b3r1/5p1k/p1n2P1p/P1qpp1P1/1p1p4/3P2Q1/BPPB2P1/R4RK1 w - - bm Kf2; id \"arasan20.179\";  \n" +
            "r2q1rk1/2p2ppp/pb1p1n2/n3p3/P2PP3/2P2NN1/R4PPP/2BQ1RK1 w - - bm Bg5; id \"arasan20.180\";  \n" +
            "1r2rbk1/1p1n1p2/p3b1p1/q2NpNPp/4P2Q/1P5R/6BP/5R1K w - h6 bm Ng3; id \"arasan20.181\";  \n" +
            "r4rk1/1bqnppBp/pp1p1np1/8/P2pP3/2N1QN1P/1PP1BPP1/R4RK1 w - - bm Qh6; id \"arasan20.182\";  \n" +
            "5b2/1b2qp1k/2pp1npp/1p6/1P2PP2/r1PQ2NP/2B3P1/3RB1K1 w - - bm e5; id \"arasan20.183\";  \n" +
            "r1qr1bk1/2p2pp1/ppn1p2p/8/1PPPN1nP/P4NP1/2Q2PK1/2BRR3 w - - bm Neg5; id \"arasan20.184\";  \n" +
            "r1b2r1k/4qp1p/p2ppb1Q/4nP2/1p1NP3/2N5/PPP4P/2KR1BR1 w - - bm Nc6; id \"arasan20.185\";  \n" +
            "5rk1/1p3n2/1q2pB2/1P1p1b1p/5Q1P/3p1NP1/5P2/2R3K1 w - - bm Ne5; id \"arasan20.186\";  \n" +
            "8/2k5/2PrR1p1/7p/5p1P/5P1K/6P1/8 w - - bm Rxd6; id \"arasan20.187\";  \n" +
            "8/4bBpp/3p4/P6P/2PN2p1/3k1b2/P7/6K1 w - - bm h6; id \"arasan20.188\";  \n" +
            "4K1k1/8/1p5p/1Pp3b1/8/1P3P2/P1B2P2/8 w - - bm f4; id \"arasan20.189\";  \n" +
            "5rn1/1p3p1k/r5pp/p1ppPPq1/6N1/1PPP3Q/1P5P/R4R1K w - - bm e6; id \"arasan20.190\";  \n" +
            "8/k3qrpR/1p1p4/p2QpPp1/P1P1P1K1/1P6/8/8 w - - bm b4; id \"arasan20.191\";  \n" +
            "3r1rk1/pbq1bp1p/1n1Rp1p1/2p1P1N1/4N2P/1P3Q2/PB3PP1/K6R w - - bm h5; id \"arasan20.192\";  \n" +
            "r2qk2r/2p1bpp1/p5B1/1p1pP3/3P2p1/5PnP/PP3R2/RNBQ2K1 b kq - bm Rxh3; id \"arasan20.193\";  \n" +
            "rn2r1k1/p4pn1/1p2p3/qPppP1Q1/3P4/2P2N2/2P2PPP/1R3RK1 w - - bm Nh4; id \"arasan20.194\";  \n" +
            "k6r/ppqb4/2n5/4p2r/P2p1P1P/B1pQ2P1/2P3B1/RR4K1 w - - bm a5; id \"arasan20.195\";  \n" +
            "1r1q2k1/p4p1p/2Pp2p1/2p1P3/1r1n4/1P4P1/3R1PBP/3QR1K1 w - - bm e6; id \"arasan20.196\";  \n" +
            "1rr5/5R2/6k1/3B2P1/1p2P1n1/1PpK4/8/R7 w - - bm Ra6+; id \"arasan20.197\";  \n" +
            "b1r1r1k1/p2n1p2/1p5p/2qp1Rn1/2P3pN/6P1/P2N1P1P/Q3RBK1 b - - bm Qb4; id \"arasan20.198\";  \n" +
            "1q4rk/R1nbp3/1n1p3p/QP1P4/3pPp2/2N2P1P/1P1N3K/5B2 w - - bm Nb3; id \"arasan20.199\";  \n" +
            "4rrk1/1bq1pp2/p2p1n1Q/1pn2p1p/4P3/P1N2P2/BPP3PP/2KRR3 w - - bm g4; id \"arasan20.200\";  " +
            "";

    private static final String[] splitUpArasan = arasanTests.split("\\\n");

}


/*
r1bq1r1k/p1pnbpp1/1p2p3/6p1/3PB3/5N2/PPPQ1PPP/2KR3R w - - bm g4; id "arasan20.1"; 
r1b2rk1/1p1nbppp/pq1p4/3B4/P2NP3/2N1p3/1PP3PP/R2Q1R1K w - - bm Rxf7; id "arasan20.2";
r1q1k2r/1p1nbpp1/2p2np1/p1Pp4/3Pp3/P1N1P1P1/1P1B1P1P/R2QRBK1 b kq - bm Bxc5; id "arasan20.3";
2rr3k/2qnbppp/p1n1p3/1p1pP3/3P1N2/1Q1BBP2/PP3P1P/1KR3R1 w - - bm Bxh7; id "arasan20.4";
3q1r1k/1b3ppp/p1n5/1p1pPB2/2rP4/P6N/1P2Q1PP/R4RK1 w - - bm Qh5; id "arasan20.5"; 
r1b1k2r/1p1pppb1/p5pp/3P4/q2p1B2/3P1Q2/PPP2PPP/R3R1K1 w kq - bm Rxe7+; id "arasan20.6"; 
R4bk1/2Bbp2p/2p2pp1/1rPp4/3P4/4P2P/4BPPK/1q1Q4 w - - bm Qa4; id "arasan20.7"; 
r1r3k1/p3bppp/2bp3Q/q2pP1P1/1p1BP3/8/PPP1B2P/2KR2R1 w - - bm e6; id "arasan20.8"; 
b2rk1r1/p3q3/2p5/3nPR2/3P2pp/1R1B2P1/P1Q2P2/6K1 w - - bm Bc4; id "arasan20.9"; 
r2q3r/1p1bbQ2/4p1Bk/3pP3/1n1P1P1p/pP6/Pn4PP/R1B1R1K1 w - - bm g4; id "arasan20.10"; 
1r2brk1/4n1p1/4p2p/p2pP1qP/2pP1NP1/P1Q1BK2/2P4R/6R1 b - - bm Bg6; id "arasan20.11";
1rb2k1r/2q2pp1/p2b3p/2n3B1/2QN4/3B4/PpP3PP/1K2R2R w - - bm Bd8; id "arasan20.12";
5rk1/1pp3p1/3ppr1p/pP2p2n/4P2q/P2PQ2P/2P1NPP1/R4RK1 b - - bm Rf3; id "arasan20.13"; 
r4rk1/1b1n1pb1/3p2p1/1p1Pq1Bp/2p1P3/2P2RNP/1PBQ2P1/5R1K w - - bm Nf5; id "arasan20.14";
2kr2r1/ppq1bp1p/4pn2/2p1n1pb/4P1P1/2P2N1P/PPBNQP2/R1B1R1K1 b - - bm Nfxg4; id "arasan20.15";
8/3r4/pr1Pk1p1/8/7P/6P1/3R3K/5R2 w - - bm Re2+; id "arasan20.16"; 
3r1rk1/q4pp1/n1bNp2p/p7/pn2P1N1/6P1/1P1Q1PBP/2RR2K1 w - - bm Nxh6+ e5; id "arasan20.17";
r1q2rk1/ppnbbpp1/n4P1p/4P3/3p4/2N1B1PP/PP4BK/R2Q1R2 w - - bm Bxh6; id "arasan20.18"; 
1R6/5p1k/4bPpp/3pN3/2pP1P1P/2r5/6PK/8 w - - bm h5; id "arasan20.19"; 
3q1rk1/pr1b1p1p/1bp2p2/2ppP3/8/2P1BN2/PPQ3PP/R4RK1 w - - bm Bh6; id "arasan20.20"; 
8/5pk1/p4npp/1pPN4/1P2p3/1P4PP/5P2/5K2 w - - bm Nxf6; id "arasan20.21"; 
8/6p1/P1b1pp2/2p1p3/1k4P1/3PP3/1PK5/5B2 w - - bm Bg2; id "arasan20.22"; 
r5n1/p1p1q2k/4b2p/3pB3/3PP1pP/8/PPPQ2P1/5RK1 w - - bm Qf4; id "arasan20.23"; 
2b2rk1/r3q1pp/1nn1p3/3pP1NP/p1pP2Q1/2P1N3/1P1KBP2/R5R1 w - - bm Nxh7; id "arasan20.24";
rnb3k1/p3qpr1/2p1p3/2NP3p/1pP3p1/3BQ3/P4PP1/4RRK1 w - - bm Qd4; id "arasan20.25"; 
r3r1k1/p3bppp/q1b2n2/5Q2/1p1B4/1BNR4/PPP3PP/2K2R2 w - - bm Rg3; id "arasan20.26"; ;
2bq1rk1/rpb2p2/2p1p1p1/p1N3Np/P2P1P1P/1Q2R1P1/1P3P2/3R2K1 w - - bm f5; id "arasan20.27"; 
3q1r1k/2r2pp1/p6p/1pbppP1N/3pP1PP/3P1Q2/PPP4R/5RK1 w - - bm g5; id "arasan20.28";
1q6/6k1/5Np1/1r4Pp/2p4P/2Nrb3/PP6/KR5Q b - - bm Bd4; id "arasan20.29";
b2rk3/r4p2/p3p3/P3Q1Np/2Pp3P/8/6P1/6K1 w - - bm Qh8+; id "arasan20.30";
2kr1b1r/1pp1ppp1/p7/q2P3n/2BB1pb1/2NQ4/P1P1N3/1R3RK1 w - - bm Rxb7; id "arasan20.31"; 
r4rkq/1ppb4/3P2n1/1N1Pp3/P3Pbn1/3B1NP1/1P2QB2/R4RK1 b - - bm Rf7; id "arasan20.32"; 
br4k1/1qrnbppp/pp1ppn2/8/NPPBP3/PN3P2/5QPP/2RR1B1K w - - bm Nxb6; id "arasan20.33"; 
r2q1rk1/ppp2p2/3p1np1/4pNQ1/4P1pP/1PPP4/1P3P2/R3K1R1 w Q - bm Qh6; id "arasan20.34";
1qb2rk1/3p1pp1/1p6/1pbBp3/r5p1/3QB3/PPP2P1P/2KR2R1 w - - bm b3; id "arasan20.35";
r1b2q1k/2Qn1p1p/1p1Rpp2/p6B/4P2P/6N1/P4PP1/6K1 w - - bm e5; id "arasan20.36"; 
r2q1rk1/p2pn3/bpp2p1p/3Nb1pQ/7B/8/PPB2PPP/R3R1K1 w - - bm Bxg5; id "arasan20.37"; 
r4rk1/p4ppp/qp2p3/b5B1/n1R5/5N2/PP2QPPP/1R4K1 w - - bm Bf6; id "arasan20.38"; 
r2q1rk1/4bppp/3pb3/2n1pP2/1p2P1PP/1P3Q2/1BP1N1B1/2KR3R b - - bm Ra2; id "arasan20.39";
2r2rkb/1Q1b3p/p2p3q/2PPnp2/1P2p1p1/2N5/P3BPPB/4RRK1 b - - bm e3; id "arasan20.40"; 
2b1rk2/5p2/p1P5/2p2P2/2p5/7B/P7/2KR4 w - - bm f6; id "arasan20.41";
rn1qr1k1/1p2bppp/p3p3/3pP3/P2P1B2/2RB1Q1P/1P3PP1/R5K1 w - - bm Bxh7+; id "arasan20.42"; 
1k4rr/p1pq2b1/1p6/1P1pp1p1/6n1/2PP2QN/PN1BP1B1/5RK1 b - - bm e4; id "arasan20.43";
1n3rk1/3rbppp/p2p4/4pP2/Ppq1P3/1N2B3/1PP3PP/R2Q1R1K w - - bm f6; id "arasan20.44"; 
8/2p1k3/3p3p/2PP1pp1/1P1K1P2/6P1/8/8 w - - bm g4; id "arasan20.45"; 
r1b2rk1/pp2bppp/3p4/q7/3BN1n1/1B3Q2/PPP3PP/R4RK1 w - - bm Qxf7+; id "arasan20.46"; 
r1b1rk2/p1pq2p1/1p1b1p1p/n2P4/2P1NP2/P2B1R2/1BQ3PP/R6K w - - bm Nxf6; id "arasan20.47"; 
r2qr3/2p1b1pk/p5pp/1p2p3/nP2P1P1/1BP2RP1/P3QPK1/R1B5 w - - bm Bxh6; id "arasan20.48"; 
1rbq1rk1/p5bp/3p2p1/2pP4/1p1n1BP1/3P3P/PP2N1B1/1R1Q1RK1 b - - bm Bxg4; id "arasan20.49"; 
k1b4r/1p3p2/pq2pNp1/5n1p/P3QP2/1P1R1BPP/2P5/1K6 b - - am Nxg3; id "arasan20.50";  
7b/8/kq6/8/8/1N2R3/K2P4/8 w - - bm Nd4; id "arasan20.51";  
q3nrk1/4bppp/3p4/r3nPP1/4P2P/NpQ1B3/1P4B1/1K1R3R b - - bm Nc7; id "arasan20.52";  
2r5/8/6k1/P1p3p1/2R5/1P1q4/1K4Q1/8 w - - bm a6; id "arasan20.53";  
8/3R1P2/1ppP1p2/3r4/8/K7/p4k2/8 w - - bm Kb2; id "arasan20.54";  
2qrrbk1/1b3ppp/pn1Pp3/6P1/1Pp2B2/1nN2NQB/1P3P1P/3RR1K1 w - - bm g6; id "arasan20.55";  
r1q1nrk1/3nb2p/3p2p1/p5P1/1pbNPB2/1N6/PPPQ4/2KR1B1R w - - bm Nf5; id "arasan20.56";  
5rk1/pp3ppp/3q4/8/2Pp2b1/P5Pn/PBQPr1BP/4RR1K b - - bm Rxg2; id "arasan20.57";  
3b3r/1q3pk1/4b2p/3pPppQ/R1pP1P1P/1rP1N1P1/6N1/2R3K1 w - - bm g4; id "arasan20.58";  
r1b2rk1/pp1p2pR/8/1pb2p2/5N2/7Q/qPPB1PPP/6K1 w - - bm g3; id "arasan20.59";  
7q/3k2p1/n1p1p1Pr/1pPpPpQ1/3P1N1p/1P2KP2/6P1/7R w - - bm Nxd5; id "arasan20.60";  
5rk1/8/pqPp1r1p/1p1Pp1bR/4B3/5PP1/PP2Q1K1/R7 w - - bm Rxg5+; id "arasan20.61";  
3r2k1/pb3Np1/4pq1p/2pp1n2/3P4/1PQ5/P4PPP/R2R2K1 b - - bm Nxd4; id "arasan20.62";  
2kr1r2/ppq1b1p1/2n5/2PpPb1N/QP1B1pp1/2P5/P2N1P1P/R4RK1 b - - bm Rh8; id "arasan20.63";  
r1r2k2/pp2bpp1/2bppn1p/6B1/2qNPPPP/2N5/PPPQ4/1K1RR3 w - - bm f5; id "arasan20.64";  
3r2k1/6p1/B1R2p1p/1pPr1P2/3P4/8/1P3nP1/2KR4 w - - bm Rc8; id "arasan20.65";  
3qb1k1/5rb1/r3p1Np/1n1pP2P/p1pB1PQ1/2P5/R1B4K/6R1 w - - bm Bc5; id "arasan20.66";  
3q1k2/p4pb1/3Pp3/p3P3/r6p/2QB3P/3B1P2/6K1 w - - bm Bb5; id "arasan20.67";  
r4r1k/ppqbn1pp/3b1p2/2pP3B/2P4N/7P/P2B1PP1/1R1QR1K1 w - - bm Rxe7; id "arasan20.68";  
1r4k1/1q3pp1/r3b2p/p2N4/3R4/QP3P2/2P3PP/1K1R4 w - - bm Nf6+; id "arasan20.69";  
r2q1r2/1bp1npk1/3p1p1p/p3p2Q/P3P2N/1BpPP3/1P1N2PP/5RK1 w - - bm Rf3; id "arasan20.70";  
2r3k1/1bp3pp/pp1pNn1r/3P1p1q/1PP1pP2/P3P1P1/3Q3P/2RR1BK1 w - - bm c5; id "arasan20.71";  
2r3r1/1p1qb2k/p5pp/2n1Bp2/2RP3P/1P2PNQ1/5P1K/3R4 w - - bm Ng5+; id "arasan20.72";  
rn3rk1/pp1q3p/4p1B1/2p5/3N1b2/4B3/PPQ2PPP/3R2K1 w - - bm Nf5; id "arasan20.73";  
rr5k/1q2pPbp/3p2p1/PbpP4/1nB1nP1Q/1NB5/1P4PP/R4R1K w - - bm f5; id "arasan20.74";  
r4rk1/pp1qbppp/1n6/6R1/P1pP4/5Q1P/2B2PP1/2B2RK1 w - - bm Rxg7+; id "arasan20.75";  
1r4k1/p7/2P1n1pp/5p2/2QPqP2/PN2p3/5P1P/4RK2 b - - bm Rc8; id "arasan20.76";  
1qrrbbk1/1p1nnppp/p3p3/4P3/2P5/1PN1N3/PB2Q1PP/1B2RR1K w - - bm Bxh7+; id "arasan20.77";  
r1b2rk1/qp5p/p1n1ppp1/7N/4P1P1/2N1pP2/PPP5/2KR1QR1 w - - bm e5; id "arasan20.78";  
3r4/2q5/5pk1/p3n1p1/N3Pp1p/1PPr1P1P/2Q1R1P1/5R1K b - - bm g4; id "arasan20.79";  
1q2r1k1/3R1pb1/3R2p1/7p/p3N3/2P1BP1P/1P3PK1/8 b - - bm Rxe4; id "arasan20.80";  
r1b1k2r/2q2pp1/p1p1pn2/2b4p/Pp2P3/3B3P/1PP1QPP1/RNB2RK1 b kq - bm Ng4; id "arasan20.81";  
2r1rb1k/ppq2pp1/4b2p/3pP2Q/5B2/2PB2R1/P4PPP/1R4K1 w - - bm Rxg7; id "arasan20.82";  
6k1/p4qp1/1p3r1p/2pPp1p1/1PP1PnP1/2P1KR1P/1B6/7Q b - - bm h5; id "arasan20.83";  
rnb1kb1r/pp1p1ppp/1q2p3/8/3NP1n1/2N1B3/PPP2PPP/R2QKB1R w KQkq - bm Qxg4; id "arasan20.84";
r3kb1r/1b1n2p1/p3Nn1p/3Pp3/1p4PP/3QBP2/qPP5/2KR1B1R w kq - bm Qg6+; id "arasan20.85";  
1r1qrbk1/pb3p2/2p1pPpp/1p4B1/2pP2PQ/2P5/P4PBP/R3R1K1 w - - bm Bxh6; id "arasan20.86";  
2r1r2k/1b1n1p1p/p3pPp1/1p1pP2q/3N4/P3Q1P1/1PP4P/2KRRB2 w - - bm g4; id "arasan20.87";  
2r1b1k1/5p2/1R2nB2/1p2P2p/2p5/2Q1P2K/3R1PB1/r3q3 w - - bm Rxe6; id "arasan20.88";  
rn2r1k1/ppq1pp1p/2b2bp1/8/2BNPP1B/2P4P/P1Q3P1/1R3RK1 w - - bm Bxf7+; id "arasan20.89";  
1kr5/1p3p2/q3p3/pRbpPp2/P1rNnP2/2P1B1Pp/1P2Q2P/R5K1 b - - bm Bxd4; id "arasan20.90";  
r3r2k/1pq2pp1/4b2p/3pP3/p1nB3P/P2B1RQ1/1PP3P1/3R3K w - - bm Rf6; id "arasan20.91";  
r3brk1/2q1bp1p/pnn3p1/1p1pP1N1/3P4/3B2P1/PP1QNR1P/R1B3K1 w - - bm Nxh7; id "arasan20.92";  
1r3r2/q5k1/4p1n1/1bPpPp1p/pPpR1Pp1/P1B1Q3/2B3PP/3R2K1 w - - bm Rxd5; id "arasan20.93";  
rq3rk1/1b1n1ppp/ppn1p3/3pP3/5B2/2NBP2P/PP2QPP1/2RR2K1 w - - bm Nxd5; id "arasan20.94";  
7r/k4pp1/pn2p1pr/2ppP3/1q3P2/1PN2R1P/P1P2QP1/3R3K w - - bm a3; id "arasan20.95";  
1r3rk1/3bbppp/1qn2P2/p2pP1P1/3P4/2PB1N2/6K1/qNBQ1R2 w - - bm Bxh7+; id "arasan20.96";  
1r1qrbk1/5ppp/2b1p2B/2npP3/1p4QP/pP1B1N2/P1P2PP1/1K1R3R w - - bm Bxh7+; id "arasan20.97";  
r5k1/pbpq1pp1/3b2rp/N3n3/1N6/2P3B1/PP1Q1PPP/R4RK1 b - - bm Rxg3; id "arasan20.98";  
1r2r1k1/2R2p2/1N1Rp2p/p2b3P/4pPP1/8/P4K2/8 w - - bm g5; id "arasan20.99";  
r4r2/pp1b1ppk/2n1p3/3pPnB1/q1pP2QP/P1P4R/2PKNPP1/R7 w - - bm Qh5+; id "arasan20.100";  
8/2k2Bp1/2n5/p1P4p/4pPn1/P3PqPb/1r1BQ2P/2R1K1R1 b - - bm Nce5; id "arasan20.101";  
8/5kpp/8/8/8/5P2/1RPK2PP/6r1 w - - bm c4; id "arasan20.102";  
r3rnk1/pp2ppb1/1np3p1/3qP2p/3P1B2/4Q1N1/PP2BPP1/1K1R3R w - - bm Bh6; id "arasan20.103";  
1r1q2k1/2r3bp/B2p1np1/3P1p2/R1P1pP2/4B2P/P5PK/3Q1R2 b - - bm Ng4+; id "arasan20.104";  
2r1rnk1/1p2pp1p/p1np2p1/q4PP1/3NP2Q/4B2R/PPP4P/3R3K w - - bm b4; id "arasan20.105";  
2b2qk1/1r4pp/2p1p3/p2n1PPB/2p4P/2p5/P4Q2/4RRK1 w - - bm Qg3; id "arasan20.106";  
1r1rkb2/2q2p2/p2p1P1B/P1pPp2Q/2P3b1/1P6/2B3PP/5R1K w - - bm Qxg4; id "arasan20.107";  
r4rk1/3b3p/p1pb4/1p1n2p1/2P2p2/1B1P2Pq/PP1NRP1P/R1BQ2K1 w - - bm Qf1; id "arasan20.108";  
1r3rk1/4bpp1/p3p2p/q1PpPn2/bn3Q1P/1PN1BN2/2P1BPP1/1KR2R2 b - - bm Bxb3; id "arasan20.109";  
2nb2k1/1rqb1pp1/p2p1n1p/2pPp3/P1P1P3/2B1NN1P/2B2PP1/Q3R2K w - - bm Nxe5; id "arasan20.110";  
3r2k1/p1qn1p1p/4p1p1/2p1N3/8/2P3P1/PP2QP1P/4R1K1 w - - bm Nxf7; id "arasan20.111";  
r2q1rk1/pb1nbp1p/1pp1pp2/8/2BPN2P/5N2/PPP1QPP1/2KR3R w - - bm Nfg5; id "arasan20.112";  
4rr2/3bp1bk/p2q1np1/2pPp2p/2P4P/1R4N1/1P1BB1P1/1Q3RK1 w - - bm Bxh5; id "arasan20.113";  
8/8/4b1p1/2Bp3p/5P1P/1pK1Pk2/8/8 b - - bm g5 d4+; id "arasan20.114";  
8/5p2/3p2p1/1bk4p/p2pBNnP/P5P1/1P3P2/4K3 b - - bm d3; id "arasan20.115";  
8/4nk2/1p3p2/1r1p2pp/1P1R1N1P/6P1/3KPP2/8 w - - bm Nd3; id "arasan20.116";  
6k1/1bq1bpp1/p6p/2p1pP2/1rP1P1P1/2NQ4/2P4P/K2RR3 b - - bm Bd5; id "arasan20.117";  
r3r1k1/1bqnbp1p/pp1pp1p1/6P1/Pn2PP1Q/1NN1BR2/1PPR2BP/6K1 w - - bm Rh3; id "arasan20.118";  
4rrk1/1pp1n1pp/1bp1P2q/p4p2/P4P2/3R2N1/1PP2P1P/2BQRK2 w - - bm Nh5; id "arasan20.119";  
3q4/4k3/1p1b1p1r/p2Q4/3B1p1p/7P/1P4P1/3R3K w - - bm b4; id "arasan20.120";  
8/5p1k/6p1/1p1Q3p/3P4/1R2P1KP/6P1/r4q2 b - - bm h4+; id "arasan20.121";  
7k/3q1pp1/1p3r2/p1bP4/P1P2p2/1P2rNpP/2Q3P1/4RR1K b - - bm Rxf3; id "arasan20.122";  
3r3r/k1p2pb1/B1b2q2/2RN3p/3P2p1/1Q2B1Pn/PP3PKP/5R2 w - - bm Rfc1; id "arasan20.123";  
r1b3kr/pp1n2Bp/2pb2q1/3p3N/3P4/2P2Q2/P1P3PP/4RRK1 w - - bm Re5; id "arasan20.124";  
2r3k1/1q3pp1/2n1b2p/4P3/3p1BP1/Q6P/1p3PB1/1R4K1 b - - bm Rb8; id "arasan20.125";  
rn2kb1r/1b1n1p1p/p3p1p1/1p2q1B1/3N3Q/2N5/PPP3PP/2KR1B1R w kq - bm Nxe6; id "arasan20.126";  
r7/ppp3kp/2bn4/4qp2/2B1pR2/2P1Q2P/P5P1/5RK1 w - - bm Rxf5; id "arasan20.127";  
1r6/r6k/2np2p1/2pNp1qp/1pP1Pp1b/1P1P1P2/1B3P2/1Q1R1K1R b - - bm Bxf2; id "arasan20.128";  
1nr3k1/q4rpp/1p1p1n2/3Pp3/1PQ1P1b1/4B1P1/2R2NBP/2R3K1 w - - bm Qxc8+; id "arasan20.129";  
8/5rk1/p2p4/1p1P1b1p/1P1K2pP/2P3P1/4R3/5B2 w - - bm Rf2; id "arasan20.130";  
5rk1/2p1R2p/r5q1/2pPR2p/5p2/1p5P/P4PbK/2BQ4 w - - bm d6; id "arasan20.131";  
r2q1r2/1b2bpkp/p3p1p1/2ppP1P1/7R/1PN1BQR1/1PP2P1P/4K3 w - - bm Qf6+; id "arasan20.132";  
r1r3k1/1ppn2bp/p1q1p1p1/3pP3/3PB1P1/PQ3NP1/3N4/2BK3R w - - bm Ng5; id "arasan20.133";  
1rr1b1k1/1pq1bp2/p2p1np1/4p3/P2BP3/2NB2Q1/1PP3PP/4RR1K w - - bm Rxf6; id "arasan20.134";  
r1r3k1/1q3p1p/4p1pP/1bnpP1P1/pp1Q1P2/1P6/P1PR1N2/1K3B1R b - - bm axb3; id "arasan20.135";  
r1b2rk1/pppnq3/4ppp1/6N1/3P3Q/2PB4/P1PK2PP/3R3R w - - bm Nxe6; id "arasan20.136";  
3r1r1k/pp5p/4b1pb/6q1/3P4/4p1BP/PP2Q1PK/3RRB2 b - - bm Qxg3+; id "arasan20.137";  
r2r2k1/3bb1Pp/3pp1p1/p1q5/1p2PP2/P1N5/1PPQ4/1K1R1B1R w - - bm Nd5; id "arasan20.138";  
8/2R5/3p4/3P4/3k3P/2p3K1/1r4P1/8 w - - bm Kf3; id "arasan20.139";  
r1bq2k1/1pp2ppp/3prn2/p3n3/2P5/PQN1PP2/1P1PB2P/R1B2R1K b - - bm Nfg4; id "arasan20.140";  
2kr3r/pp4pp/4pp2/2pq4/P1Nn4/4Q3/KP2B1PP/2RR4 b - - am Qxg2; id "arasan20.141";  
5r2/1p4k1/pP1pP1pp/2rP2q1/4Qp2/3Bb3/P5PP/4RR1K w - - bm Rf3; id "arasan20.142";  
r2qr1k1/1b1pppbp/1p4p1/pP2P1B1/3N4/R7/1PP2PPP/3QR1K1 w - a6 bm Nf5; id "arasan20.143";  
4k3/1R6/Pb3p2/1P1n4/5p2/8/4K3/8 w - - bm Kd3; id "arasan20.144";  
r4nk1/2pq1ppp/3p4/p3pNPQ/4P3/2PP1RP1/Pr3PK1/7R w - - bm Ne3; id "arasan20.145";  
r1q2rk1/1b2bppp/p1p1p3/4B3/PP6/3B3P/2P1QPP1/R2R2K1 w - - bm Bxh7+; id "arasan20.146";  
r2qrb1k/1p1b2p1/p2ppn1p/8/3NP3/1BN5/PPP3QP/1K3RR1 w - - bm e5; id "arasan20.147";  
r2q1k1r/pp2n1pp/2nb1p2/1B1p3Q/N2P4/2P1B3/PP4PP/R4RK1 w - - bm Rxf6+; id "arasan20.148";  
4r1k1/6p1/bp2r2p/3QNp2/P2BnP2/4P2P/5qPK/3RR3 b - - bm Kh7; id "arasan20.149";  
8/5rk1/p3Q1pp/1p1P1p1b/2p1Pq1P/P4P2/1PKN4/5R2 w - - bm d6; id "arasan20.150";  
r1bqkb1r/4pppp/p1p5/2ppP3/8/2P2N2/PPP2PPP/R1BQR1K1 w kq - bm e6; id "arasan20.151";  
3r1rk1/1b2bpp1/2n1p2p/qp1n2N1/4N3/P3P3/1BB1QPPP/2R2RK1 w - - bm Qh5; id "arasan20.152";  
3R4/pp2r1pk/q1p3bp/2P2r2/PP6/2Q3P1/6BP/5RK1 w - - bm Rxf5; id "arasan20.153";  
r3k3/1p4p1/1Bb1Bp1p/P1p1bP1P/2Pp2P1/3P4/5K2/4R3 w - - bm g5; id "arasan20.154";  
1r1rb1k1/5ppp/4p3/1p1p3P/1q2P2Q/pN3P2/PPP4P/1K1R2R1 w - - bm Rxg7+; id "arasan20.155";  
1r1q1rk1/4bp1p/n3p3/pbNpP1PB/5P2/1P2B1K1/1P1Q4/2RR4 w - - bm Ne4; id "arasan20.156";  
r1bq1rk1/pp2bppp/1n2p3/3pP3/8/2RBBN2/PP2QPPP/2R3K1 w - - bm Bxh7+; id "arasan20.157";  
r6k/N1Rb2bp/p2p1nr1/3Pp2q/1P2Pp1P/5N2/P3QBP1/4R1K1 b - - bm Bh3; id "arasan20.158";  
r1b2rk1/1pq1nppp/pbn1p3/8/3N4/3BBN2/PPP1QPPP/3R1RK1 w - - bm Bxh7+; id "arasan20.159";  
3r1rk1/1b2qp1p/1p3np1/1N1p4/6n1/2NBP1K1/PBQ2PP1/3RR3 b - - bm d4; id "arasan20.160";  
br3bk1/3r1p2/3q2p1/3P2Np/2B4P/3QR1P1/3R1P1K/8 w - - bm Nxf7; id "arasan20.161";  
r3r2k/ppq3np/2p3p1/NPPp1bb1/P2Pnp2/3B1P2/2Q3PP/1RN1BRK1 b - - bm Ng3; id "arasan20.162";  
7k/5rp1/3q1p1p/2bNpQ1P/4P1P1/8/1R3PK1/8 w - - bm g5; id "arasan20.163";  
4r3/4r3/1ppqpnk1/p3Rp1p/P2P1R1Q/2PB2P1/1P3P2/6K1 w - - bm Bxf5+; id "arasan20.164";  
r3nrk1/1pqbbppp/p2pp3/2n1P3/5P2/2NBBNQ1/PPP3PP/R4RK1 w - - bm Bxh7; id "arasan20.165";  
rnbq3r/ppp2kpp/4pp2/3n4/2BP4/BQ3N2/P4PPP/4RRK1 w - - bm Ng5+; id "arasan20.166";  
8/2N5/1P2p3/5bPk/1q3b2/3Bp2P/2P5/6QK b - - bm Kh4; id "arasan20.167";  
1k1r1b1r/1p6/p4pp1/P1p1p3/2NpP1p1/1PPP2Pq/1B3P1P/2RQR1K1 b - - bm f5; id "arasan20.168";  
5r2/3rkp2/2R2p2/p2Bb2Q/1p2P2P/4q1P1/Pp6/1K1R4 b - - bm b3; id "arasan20.169";  
5rk1/qp1b1rnp/4p1p1/p2pB3/8/1R1B4/PP1QRPPP/6K1 w - - bm Bxg6; id "arasan20.170";  
6k1/5r1p/1p2Q1p1/p7/P1P2P2/2K1R1P1/2N2qb1/8 w - - bm Qd6 Qe8+; id "arasan20.171";  
4r1k1/1p4p1/p1qBp1Qp/b1pnP3/8/5NP1/1P3PKP/3R4 w - - bm Rxd5; id "arasan20.172";  
2r1k2r/pp1bb1pp/6n1/3Q1p2/1B1N4/P7/1q4PP/4RRK1 w k - bm Bxe7; id "arasan20.173";  
3b2k1/4qp2/2P4Q/3B3p/1P6/1K6/8/8 w - - bm Bc4; id "arasan20.174";  
1r2brk1/6p1/1q2p1Pp/pN1pPPb1/np1N4/5Q2/1PP1B3/1K1R3R w - - bm f6; id "arasan20.175";  
2rq1Nk1/pb3pp1/4p3/1p6/3b1Pn1/P1N5/1PQ3PP/R1B2R1K b - - bm f5; id "arasan20.176";  
r1b2rk1/1p4p1/p1n1p3/3p1pB1/NqP3n1/b2BP3/1PQN1P1P/1K4RR w - - bm Rxg4; id "arasan20.177";  
q2rn1k1/1b3p1p/1p4p1/2n1B1P1/r1PN3P/P4P2/4Q1B1/3RR1K1 w - - bm Bf6; id "arasan20.178";  
r1b3r1/5p1k/p1n2P1p/P1qpp1P1/1p1p4/3P2Q1/BPPB2P1/R4RK1 w - - bm Kf2; id "arasan20.179";  
r2q1rk1/2p2ppp/pb1p1n2/n3p3/P2PP3/2P2NN1/R4PPP/2BQ1RK1 w - - bm Bg5; id "arasan20.180";  
1r2rbk1/1p1n1p2/p3b1p1/q2NpNPp/4P2Q/1P5R/6BP/5R1K w - h6 bm Ng3; id "arasan20.181";  
r4rk1/1bqnppBp/pp1p1np1/8/P2pP3/2N1QN1P/1PP1BPP1/R4RK1 w - - bm Qh6; id "arasan20.182";  
5b2/1b2qp1k/2pp1npp/1p6/1P2PP2/r1PQ2NP/2B3P1/3RB1K1 w - - bm e5; id "arasan20.183";  
r1qr1bk1/2p2pp1/ppn1p2p/8/1PPPN1nP/P4NP1/2Q2PK1/2BRR3 w - - bm Neg5; id "arasan20.184";  
r1b2r1k/4qp1p/p2ppb1Q/4nP2/1p1NP3/2N5/PPP4P/2KR1BR1 w - - bm Nc6; id "arasan20.185";  
5rk1/1p3n2/1q2pB2/1P1p1b1p/5Q1P/3p1NP1/5P2/2R3K1 w - - bm Ne5; id "arasan20.186";  
8/2k5/2PrR1p1/7p/5p1P/5P1K/6P1/8 w - - bm Rxd6; id "arasan20.187";  
8/4bBpp/3p4/P6P/2PN2p1/3k1b2/P7/6K1 w - - bm h6; id "arasan20.188";  
4K1k1/8/1p5p/1Pp3b1/8/1P3P2/P1B2P2/8 w - - bm f4; id "arasan20.189";  
5rn1/1p3p1k/r5pp/p1ppPPq1/6N1/1PPP3Q/1P5P/R4R1K w - - bm e6; id "arasan20.190";  
8/k3qrpR/1p1p4/p2QpPp1/P1P1P1K1/1P6/8/8 w - - bm b4; id "arasan20.191";  
3r1rk1/pbq1bp1p/1n1Rp1p1/2p1P1N1/4N2P/1P3Q2/PB3PP1/K6R w - - bm h5; id "arasan20.192";  
r2qk2r/2p1bpp1/p5B1/1p1pP3/3P2p1/5PnP/PP3R2/RNBQ2K1 b kq - bm Rxh3; id "arasan20.193";  
rn2r1k1/p4pn1/1p2p3/qPppP1Q1/3P4/2P2N2/2P2PPP/1R3RK1 w - - bm Nh4; id "arasan20.194";  
k6r/ppqb4/2n5/4p2r/P2p1P1P/B1pQ2P1/2P3B1/RR4K1 w - - bm a5; id "arasan20.195";  
1r1q2k1/p4p1p/2Pp2p1/2p1P3/1r1n4/1P4P1/3R1PBP/3QR1K1 w - - bm e6; id "arasan20.196";  
1rr5/5R2/6k1/3B2P1/1p2P1n1/1PpK4/8/R7 w - - bm Ra6+; id "arasan20.197";  
b1r1r1k1/p2n1p2/1p5p/2qp1Rn1/2P3pN/6P1/P2N1P1P/Q3RBK1 b - - bm Qb4; id "arasan20.198";  
1q4rk/R1nbp3/1n1p3p/QP1P4/3pPp2/2N2P1P/1P1N3K/5B2 w - - bm Nb3; id "arasan20.199";  
4rrk1/1bq1pp2/p2p1n1Q/1pn2p1p/4P3/P1N2P2/BPP3PP/2KRR3 w - - bm g4; id "arasan20.200";  
 */