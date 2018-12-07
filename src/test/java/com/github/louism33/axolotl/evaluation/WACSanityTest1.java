package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.ExtendedPositionDescriptionParser;
import com.github.louism33.chesscore.MoveParser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class WACSanityTest1 {

    private static final int timeLimit = 5_000;
    private static int successes = 0;

    /*
    done this way to check general playing strength. Score would never be 300 / 300
     */
    @AfterClass
    public static void finalSuccessTally(){
        System.out.println("Successful tests: " + successes);
        Assert.assertTrue(successes > 200);
    }
    
    @Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < splitUpWACs.length; i++) {
            String splitUpWAC = splitUpWACs[i];
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpWAC);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public WACSanityTest1(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        List<Integer> winningMoves = EPDObject.getBestMoves();
        List<Integer> losingMoves = EPDObject.getAvoidMoves();
        System.out.println(EPDObject.getBoardFen());
        System.out.println("Move(s) to get: " + Arrays.toString(MoveParser.toString(winningMoves)));
        
        if (losingMoves.size() > 0){
            System.out.println("Move(s) to avoid: " + Arrays.toString(MoveParser.toString(losingMoves)));
        }
        
        int move = Engine.searchFixedTime(EPDObject.getBoard(), timeLimit);
        
        if (winningMoves.contains(move) && !losingMoves.contains(move)){
            System.out.println("success");
            successes++;
        }
        else {
            System.out.println("failure");
        }

        System.out.println();
    }
    
    

    private static final String wacTests = "" +
            "2rr3k/pp3pp1/1nnqbN1p/3pN3/2pP4/2P3Q1/PPB4P/R4RK1 w - - bm Qg6; id \"WAC.001\";\n" +
            "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.002\";\n" +
            "5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";\n" +
            "r1bq2rk/pp3pbp/2p1p1pQ/7P/3P4/2PB1N2/PP3PPR/2KR4 w - - bm Qxh7+; id \"WAC.004\";\n" +
            "5k2/6pp/p1qN4/1p1p4/3P4/2PKP2Q/PP3r2/3R4 b - - bm Qc4+; id \"WAC.005\";\n" +
            "7k/p7/1R5K/6r1/6p1/6P1/8/8 w - - bm Rb7; id \"WAC.006\";\n" +
            "rnbqkb1r/pppp1ppp/8/4P3/6n1/7P/PPPNPPP1/R1BQKBNR b KQkq - bm Ne3; id \"WAC.007\";\n" +
            "r4q1k/p2bR1rp/2p2Q1N/5p2/5p2/2P5/PP3PPP/R5K1 w - - bm Rf7; id \"WAC.008\";\n" +
            "3q1rk1/p4pp1/2pb3p/3p4/6Pr/1PNQ4/P1PB1PP1/4RRK1 b - - bm Bh2+; id \"WAC.009\";\n" +
            "2br2k1/2q3rn/p2NppQ1/2p1P3/Pp5R/4P3/1P3PPP/3R2K1 w - - bm Rxh7; id \"WAC.010\";\n" +
            "r1b1kb1r/3q1ppp/pBp1pn2/8/Np3P2/5B2/PPP3PP/R2Q1RK1 w kq - bm Bxc6; id \"WAC.011\";\n" +
            "4k1r1/2p3r1/1pR1p3/3pP2p/3P2qP/P4N2/1PQ4P/5R1K b - - bm Qxf3+; id \"WAC.012\";\n" +
            "5rk1/pp4p1/2n1p2p/2Npq3/2p5/6P1/P3P1BP/R4Q1K w - - bm Qxf8+; id \"WAC.013\";\n" +
            "r2rb1k1/pp1q1p1p/2n1p1p1/2bp4/5P2/PP1BPR1Q/1BPN2PP/R5K1 w - - bm Qxh7+; id \"WAC.014\";\n" +
            "1R6/1brk2p1/4p2p/p1P1Pp2/P7/6P1/1P4P1/2R3K1 w - - bm Rxb7; id \"WAC.015\";\n" +
            "r4rk1/ppp2ppp/2n5/2bqp3/8/P2PB3/1PP1NPPP/R2Q1RK1 w - - bm Nc3; id \"WAC.016\";\n" +
            "1k5r/pppbn1pp/4q1r1/1P3p2/2NPp3/1QP5/P4PPP/R1B1R1K1 w - - bm Ne5; id \"WAC.017\";\n" +
            "R7/P4k2/8/8/8/8/r7/6K1 w - - bm Rh8; id \"WAC.018\";\n" +
            "r1b2rk1/ppbn1ppp/4p3/1QP4q/3P4/N4N2/5PPP/R1B2RK1 w - - bm c6; id \"WAC.019\";\n" +
            "r2qkb1r/1ppb1ppp/p7/4p3/P1Q1P3/2P5/5PPP/R1B2KNR b kq - bm Bb5; id \"WAC.020\";\n" +
            "5rk1/1b3p1p/pp3p2/3n1N2/1P6/P1qB1PP1/3Q3P/4R1K1 w - - bm Qh6; id \"WAC.021\";\n" +
            "r1bqk2r/ppp1nppp/4p3/n5N1/2BPp3/P1P5/2P2PPP/R1BQK2R w KQkq - bm Ba2 Nxf7; id \"WAC.022\";\n" +
            "r3nrk1/2p2p1p/p1p1b1p1/2NpPq2/3R4/P1N1Q3/1PP2PPP/4R1K1 w - - bm g4; id \"WAC.023\";\n" +
            "6k1/1b1nqpbp/pp4p1/5P2/1PN5/4Q3/P5PP/1B2B1K1 b - - bm Bd4; id \"WAC.024\";\n" +
            "3R1rk1/8/5Qpp/2p5/2P1p1q1/P3P3/1P2PK2/8 b - - bm Qh4+; id \"WAC.025\";\n" +
            "3r2k1/1p1b1pp1/pq5p/8/3NR3/2PQ3P/PP3PP1/6K1 b - - bm Bf5; id \"WAC.026\";\n" +
            "7k/pp4np/2p3p1/3pN1q1/3P4/Q7/1r3rPP/2R2RK1 w - - bm Qf8+; id \"WAC.027\";\n" +
            "1r1r2k1/4pp1p/2p1b1p1/p3R3/RqBP4/4P3/1PQ2PPP/6K1 b - - bm Qe1+; id \"WAC.028\";\n" +
            "r2q2k1/pp1rbppp/4pn2/2P5/1P3B2/6P1/P3QPBP/1R3RK1 w - - bm c6; id \"WAC.029\";\n" +
            "1r3r2/4q1kp/b1pp2p1/5p2/pPn1N3/6P1/P3PPBP/2QRR1K1 w - - bm Nxd6; id \"WAC.030\";\n" +
            "rb3qk1/pQ3ppp/4p3/3P4/8/1P3N2/1P3PPP/3R2K1 w - - bm Qxa8 d6 dxe6 g3; id \"WAC.031\";\n" +
            "6k1/p4p1p/1p3np1/2q5/4p3/4P1N1/PP3PPP/3Q2K1 w - - bm Qd8+; id \"WAC.032\";\n" +
            "8/p1q2pkp/2Pr2p1/8/P3Q3/6P1/5P1P/2R3K1 w - - bm Qe5+ Qf4; id \"WAC.033\";\n" +
            "7k/1b1r2p1/p6p/1p2qN2/3bP3/3Q4/P5PP/1B1R3K b - - bm Bg1; id \"WAC.034\";\n" +
            "r3r2k/2R3pp/pp1q1p2/8/3P3R/7P/PP3PP1/3Q2K1 w - - bm Rxh7+; id \"WAC.035\";\n" +
            "3r4/2p1rk2/1pQq1pp1/7p/1P1P4/P4P2/6PP/R1R3K1 b - - bm Re1+; id \"WAC.036\";\n" +
            "2r5/2rk2pp/1pn1pb2/pN1p4/P2P4/1N2B3/nPR1KPPP/3R4 b - - bm Nxd4+; id \"WAC.037\";\n" +
            "4k3/p4prp/1p6/2b5/8/2Q3P1/P2R1PKP/4q3 w - - bm Qd3 Rd8+; id \"WAC.038\";\n" +
            "r1br2k1/pp2bppp/2nppn2/8/2P1PB2/2N2P2/PqN1B1PP/R2Q1R1K w - - bm Na4; id \"WAC.039\";\n" +
            "3r1r1k/1p4pp/p4p2/8/1PQR4/6Pq/P3PP2/2R3K1 b - - bm Rc8; id \"WAC.040\";\n" +
            "1k6/5RP1/1P6/1K6/6r1/8/8/8 w - - bm Ka5 Kc5 b7; id \"WAC.041\";\n" +
            "r1b1r1k1/pp1n1pbp/1qp3p1/3p4/1B1P4/Q3PN2/PP2BPPP/R4RK1 w - - bm Ba5; id \"WAC.042\";\n" +
            "r2q3k/p2P3p/1p3p2/3QP1r1/8/B7/P5PP/2R3K1 w - - bm Be7 Qxa8; id \"WAC.043\";\n" +
            "3rb1k1/pq3pbp/4n1p1/3p4/2N5/2P2QB1/PP3PPP/1B1R2K1 b - - bm dxc4; id \"WAC.044\";\n" +
            "7k/2p1b1pp/8/1p2P3/1P3r2/2P3Q1/1P5P/R4qBK b - - bm Qxa1; id \"WAC.045\";\n" +
            "r1bqr1k1/pp1nb1p1/4p2p/3p1p2/3P4/P1N1PNP1/1PQ2PP1/3RKB1R w K - bm Nb5; id \"WAC.046\";\n" +
            "r1b2rk1/pp2bppp/2n1pn2/q5B1/2BP4/2N2N2/PP2QPPP/2R2RK1 b - - bm Nxd4; id \"WAC.047\";\n" +
            "1rbq1rk1/p1p1bppp/2p2n2/8/Q1BP4/2N5/PP3PPP/R1B2RK1 b - - bm Rb4; id \"WAC.048\";\n" +
            "2b3k1/4rrpp/p2p4/2pP2RQ/1pP1Pp1N/1P3P1P/1q6/6RK w - - bm Qxh7+; id \"WAC.049\";\n" +
            "k4r2/1R4pb/1pQp1n1p/3P4/5p1P/3P2P1/r1q1R2K/8 w - - bm Rxb6+; id \"WAC.050\";\n" +
            "r1bq1r2/pp4k1/4p2p/3pPp1Q/3N1R1P/2PB4/6P1/6K1 w - - bm Rg4+; id \"WAC.051\";\n" +
            "r1k5/1p3q2/1Qpb4/3N1p2/5Pp1/3P2Pp/PPPK3P/4R3 w - - bm Re7 c4; id \"WAC.052\";\n" +
            "6k1/6p1/p7/3Pn3/5p2/4rBqP/P4RP1/5QK1 b - - bm Re1; id \"WAC.053\";\n" +
            "r3kr2/1pp4p/1p1p4/7q/4P1n1/2PP2Q1/PP4P1/R1BB2K1 b q - bm Qh1+; id \"WAC.054\";\n" +
            "r3r1k1/pp1q1pp1/4b1p1/3p2B1/3Q1R2/8/PPP3PP/4R1K1 w - - bm Qxg7+; id \"WAC.055\";\n" +
            "r1bqk2r/pppp1ppp/5n2/2b1n3/4P3/1BP3Q1/PP3PPP/RNB1K1NR b KQkq - bm Bxf2+; id \"WAC.056\";\n" +
            "r3q1kr/ppp5/3p2pQ/8/3PP1b1/5R2/PPP3P1/5RK1 w - - bm Rf8+; id \"WAC.057\";\n" +
            "8/8/2R5/1p2qp1k/1P2r3/2PQ2P1/5K2/8 w - - bm Qd1+; id \"WAC.058\";\n" +
            "r1b2rk1/2p1qnbp/p1pp2p1/5p2/2PQP3/1PN2N1P/PB3PP1/3R1RK1 w - - bm Nd5; id \"WAC.059\";\n" +
            "rn1qr1k1/1p2np2/2p3p1/8/1pPb4/7Q/PB1P1PP1/2KR1B1R w - - bm Qh8+; id \"WAC.060\";\n" +
            "3qrbk1/ppp1r2n/3pP2p/3P4/2P4P/1P3Q2/PB6/R4R1K w - - bm Qf7+; id \"WAC.061\";\n" +
            "6r1/3Pn1qk/p1p1P1rp/2Q2p2/2P5/1P4P1/P3R2P/5RK1 b - - bm Rxg3+; id \"WAC.062\";\n" +
            "r1brnbk1/ppq2pp1/4p2p/4N3/3P4/P1PB1Q2/3B1PPP/R3R1K1 w - - bm Nxf7; id \"WAC.063\";\n" +
            "8/6pp/3q1p2/3n1k2/1P6/3NQ2P/5PP1/6K1 w - - bm g4+; id \"WAC.064\";\n" +
            "1r1r1qk1/p2n1p1p/bp1Pn1pQ/2pNp3/2P2P1N/1P5B/P6P/3R1RK1 w - - bm Ne7+; id \"WAC.065\";\n" +
            "1k1r2r1/ppq5/1bp4p/3pQ3/8/2P2N2/PP4P1/R4R1K b - - bm Qxe5; id \"WAC.066\";\n" +
            "3r2k1/p2q4/1p4p1/3rRp1p/5P1P/6PK/P3R3/3Q4 w - - bm Rxd5; id \"WAC.067\";\n" +
            "6k1/5ppp/1q6/2b5/8/2R1pPP1/1P2Q2P/7K w - - bm Qxe3; id \"WAC.068\";\n" +
            "2k5/pppr4/4R3/4Q3/2pp2q1/8/PPP2PPP/6K1 w - - bm f3 h3; id \"WAC.069\";\n" +
            "2kr3r/pppq1ppp/3p1n2/bQ2p3/1n1PP3/1PN1BN1P/1PP2PP1/2KR3R b - - bm Na2+; id \"WAC.070\";\n" +
            "2kr3r/pp1q1ppp/5n2/1Nb5/2Pp1B2/7Q/P4PPP/1R3RK1 w - - bm Nxa7+; id \"WAC.071\";\n" +
            "r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";\n" +
            "r1q3rk/1ppbb1p1/4Np1p/p3pP2/P3P3/2N4R/1PP1Q1PP/3R2K1 w - - bm Qd2; id \"WAC.073\";\n" +
            "5r1k/pp4pp/2p5/2b1P3/4Pq2/1PB1p3/P3Q1PP/3N2K1 b - - bm Qf1+; id \"WAC.074\";\n" +
            "r3r1k1/pppq1ppp/8/8/1Q4n1/7P/PPP2PP1/RNB1R1K1 b - - bm Qd6; id \"WAC.075\";\n" +
            "r1b1qrk1/2p2ppp/pb1pnn2/1p2pNB1/3PP3/1BP5/PP2QPPP/RN1R2K1 w - - bm Bxf6; id \"WAC.076\";\n" +
            "3r2k1/ppp2ppp/6q1/b4n2/3nQB2/2p5/P4PPP/RN3RK1 b - - bm Ng3; id \"WAC.077\";\n" +
            "r2q3r/ppp2k2/4nbp1/5Q1p/2P1NB2/8/PP3P1P/3RR1K1 w - - bm Ng5+; id \"WAC.078\";\n" +
            "r3k2r/pbp2pp1/3b1n2/1p6/3P3p/1B2N1Pq/PP1PQP1P/R1B2RK1 b kq - bm Qxh2+; id \"WAC.079\";\n" +
            "r4rk1/p1B1bpp1/1p2pn1p/8/2PP4/3B1P2/qP2QP1P/3R1RK1 w - - bm Ra1; id \"WAC.080\";\n" +
            "r4rk1/1bR1bppp/4pn2/1p2N3/1P6/P3P3/4BPPP/3R2K1 b - - bm Bd6; id \"WAC.081\";\n" +
            "3rr1k1/pp3pp1/4b3/8/2P1B2R/6QP/P3q1P1/5R1K w - - bm Bh7+; id \"WAC.082\";\n" +
            "3rr1k1/ppqbRppp/2p5/8/3Q1n2/2P3N1/PPB2PPP/3R2K1 w - - bm Qxd7; id \"WAC.083\";\n" +
            "r2q1r1k/2p1b1pp/p1n5/1p1Q1bN1/4n3/1BP1B3/PP3PPP/R4RK1 w - - bm Qg8+; id \"WAC.084\";\n" +
            "kr2R3/p4r2/2pq4/2N2p1p/3P2p1/Q5P1/5P1P/5BK1 w - - bm Na6; id \"WAC.085\";\n" +
            "8/p7/1ppk1n2/5ppp/P1PP4/2P1K1P1/5N1P/8 b - - bm Ng4+; id \"WAC.086\";\n" +
            "8/p3k1p1/4r3/2ppNpp1/PP1P4/2P3KP/5P2/8 b - - bm Rxe5; id \"WAC.087\";\n" +
            "r6k/p1Q4p/2p1b1rq/4p3/B3P3/4P3/PPP3P1/4RRK1 b - - bm Rxg2+; id \"WAC.088\";\n" +
            "1r3b1k/p4rpp/4pp2/3q4/2ppbPPQ/6RK/PP5P/2B1NR2 b - - bm g5; id \"WAC.089\";\n" +
            "3qrrk1/1pp2pp1/1p2bn1p/5N2/2P5/P1P3B1/1P4PP/2Q1RRK1 w - - bm Nxg7; id \"WAC.090\";\n" +
            "2qr2k1/4b1p1/2p2p1p/1pP1p3/p2nP3/PbQNB1PP/1P3PK1/4RB2 b - - bm Be6; id \"WAC.091\";\n" +
            "r4rk1/1p2ppbp/p2pbnp1/q7/3BPPP1/2N2B2/PPP4P/R2Q1RK1 b - - bm Bxg4; id \"WAC.092\";\n" +
            "r1b1k1nr/pp3pQp/4pq2/3pn3/8/P1P5/2P2PPP/R1B1KBNR w KQkq - bm Bh6; id \"WAC.093\";\n" +
            "8/k7/p7/3Qp2P/n1P5/3KP3/1q6/8 b - - bm e4+; id \"WAC.094\";\n" +
            "2r5/1r6/4pNpk/3pP1qp/8/2P1QP2/5PK1/R7 w - - bm Ng4+; id \"WAC.095\";\n" +
            "r1b4k/ppp2Bb1/6Pp/3pP3/1qnP1p1Q/8/PPP3P1/1K1R3R w - - bm Qd8+ b3; id \"WAC.096\";\n" +
            "6k1/5p2/p5np/4B3/3P4/1PP1q3/P3r1QP/6RK w - - bm Qa8+; id \"WAC.097\";\n" +
            "1r3rk1/5pb1/p2p2p1/Q1n1q2p/1NP1P3/3p1P1B/PP1R3P/1K2R3 b - - bm Nxe4; id \"WAC.098\";\n" +
            "r1bq1r1k/1pp1Np1p/p2p2pQ/4R3/n7/8/PPPP1PPP/R1B3K1 w - - bm Rh5; id \"WAC.099\";\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm Be3 b6+; id \"WAC.100\";\n" +
            "5rk1/p5pp/8/8/2Pbp3/1P4P1/7P/4RN1K b - - bm Bc3; id \"WAC.101\";\n" +
            "2Q2n2/2R4p/1p1qpp1k/8/3P3P/3B2P1/5PK1/r7 w - - bm Qxf8+; id \"WAC.102\";\n" +
            "6k1/2pb1r1p/3p1PpQ/p1nPp3/1q2P3/2N2P2/PrB5/2K3RR w - - bm Qxg6+; id \"WAC.103\";\n" +
            "b4r1k/pq2rp2/1p1bpn1p/3PN2n/2P2P2/P2B3K/1B2Q2N/3R2R1 w - - bm Qxh5; id \"WAC.104\";\n" +
            "r2r2k1/pb3ppp/1p1bp3/7q/3n2nP/PP1B2P1/1B1N1P2/RQ2NRK1 b - - bm Bxg3 Qxh4; id \"WAC.105\";\n" +
            "4rrk1/pppb4/7p/3P2pq/3Qn3/P5P1/1PP4P/R3RNNK b - - bm Nf2+; id \"WAC.106\";\n" +
            "5n2/pRrk2p1/P4p1p/4p3/3N4/5P2/6PP/6K1 w - - bm Nb5; id \"WAC.107\";\n" +
            "r5k1/1q4pp/2p5/p1Q5/2P5/5R2/4RKPP/r7 w - - bm Qe5; id \"WAC.108\";\n" +
            "rn2k1nr/pbp2ppp/3q4/1p2N3/2p5/QP6/PB1PPPPP/R3KB1R b KQkq - bm c3; id \"WAC.109\";\n" +
            "2kr4/bp3p2/p2p2b1/P7/2q5/1N4B1/1PPQ2P1/2KR4 b - - bm Be3; id \"WAC.110\";\n" +
            "6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";\n" +
            "r4kr1/ppp5/4bq1b/7B/2PR1Q1p/2N3P1/PP3P1P/2K1R3 w - - bm Rxe6; id \"WAC.112\";\n" +
            "rnbqkb1r/1p3ppp/5N2/1p2p1B1/2P5/8/PP2PPPP/R2QKB1R b KQkq - bm Qxf6; id \"WAC.113\";\n" +
            "r1b1rnk1/1p4pp/p1p2p2/3pN2n/3P1PPq/2NBPR1P/PPQ5/2R3K1 w - - bm Bxh7+; id \"WAC.114\";\n" +
            "4N2k/5rpp/1Q6/p3q3/8/P5P1/1P3P1P/5K2 w - - bm Nd6; id \"WAC.115\";\n" +
            "r2r2k1/2p2ppp/p7/1p2P1n1/P6q/5P2/1PB1QP1P/R5RK b - - bm Rd2; id \"WAC.116\";\n" +
            "3r1rk1/q4ppp/p1Rnp3/8/1p6/1N3P2/PP3QPP/3R2K1 b - - bm Ne4; id \"WAC.117\";\n" +
            "r5k1/pb2rpp1/1p6/2p4q/5R2/2PB2Q1/P1P3PP/5R1K w - - bm Rh4; id \"WAC.118\";\n" +
            "r2qr1k1/p1p2ppp/2p5/2b5/4nPQ1/3B4/PPP3PP/R1B2R1K b - - bm Qxd3; id \"WAC.119\";\n" +
            "r4rk1/1bn2qnp/3p1B1Q/p2P1pP1/1pp5/5N1P/PPB2P2/2KR3R w - - bm Rhg1 g6; id \"WAC.120\";\n" +
            "6k1/5p1p/2bP2pb/4p3/2P5/1p1pNPPP/1P1Q1BK1/1q6 b - - bm Bxf3+; id \"WAC.121\";\n" +
            "1k6/ppp4p/1n2pq2/1N2Rb2/2P2Q2/8/P4KPP/3r1B2 b - - bm Rxf1+; id \"WAC.122\";\n" +
            "6k1/1b2rp2/1p4p1/3P4/PQ4P1/2N2q2/5P2/3R2K1 b - - bm Bxd5 Rc7 Re6; id \"WAC.123\";\n" +
            "6k1/3r4/2R5/P5P1/1P4p1/8/4rB2/6K1 b - - bm g3; id \"WAC.124\";\n" +
            "r1bqr1k1/pp3ppp/1bp5/3n4/3B4/2N2P1P/PPP1B1P1/R2Q1RK1 b - - bm Bxd4+; id \"WAC.125\";\n" +
            "r5r1/pQ5p/1qp2R2/2k1p3/4P3/2PP4/P1P3PP/6K1 w - - bm Rxc6+; id \"WAC.126\";\n" +
            "2k4r/1pr1n3/p1p1q2p/5pp1/3P1P2/P1P1P3/1R2Q1PP/1RB3K1 w - - bm Rxb7; id \"WAC.127\";\n" +
            "6rk/1pp2Qrp/3p1B2/1pb1p2R/3n1q2/3P4/PPP3PP/R6K w - - bm Qg6; id \"WAC.128\";\n" +
            "3r1r1k/1b2b1p1/1p5p/2p1Pp2/q1B2P2/4P2P/1BR1Q2K/6R1 b - - bm Bf3; id \"WAC.129\";\n" +
            "6k1/1pp3q1/5r2/1PPp4/3P1pP1/3Qn2P/3B4/4R1K1 b - - bm Qh6 Qh8; id \"WAC.130\";\n" +
            "2rq1bk1/p4p1p/1p4p1/3b4/3B1Q2/8/P4PpP/3RR1K1 w - - bm Re8; id \"WAC.131\";\n" +
            "4r1k1/5bpp/2p5/3pr3/8/1B3pPq/PPR2P2/2R2QK1 b - - bm Re1; id \"WAC.132\";\n" +
            "r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";\n" +
            "3r2k1/p6p/2Q3p1/4q3/2P1p3/P3Pb2/1P3P1P/2K2BR1 b - - bm Rd1+; id \"WAC.134\";\n" +
            "3r1r1k/N2qn1pp/1p2np2/2p5/2Q1P2N/3P4/PP4PP/3R1RK1 b - - bm Nd4; id \"WAC.135\";\n" +
            "6kr/1q2r1p1/1p2N1Q1/5p2/1P1p4/6R1/7P/2R3K1 w - - bm Rc8+; id \"WAC.136\";\n" +
            "3b1rk1/1bq3pp/5pn1/1p2rN2/2p1p3/2P1B2Q/1PB2PPP/R2R2K1 w - - bm Rd7; id \"WAC.137\";\n" +
            "r1bq3r/ppppR1p1/5n1k/3P4/6pP/3Q4/PP1N1PP1/5K1R w - - bm h5; id \"WAC.138\";\n" +
            "rnb3kr/ppp2ppp/1b6/3q4/3pN3/Q4N2/PPP2KPP/R1B1R3 w - - bm Nf6+; id \"WAC.139\";\n" +
            "r2b1rk1/pq4p1/4ppQP/3pB1p1/3P4/2R5/PP3PP1/5RK1 w - - bm Bc7 Rc7; id \"WAC.140\";\n" +
            "4r1k1/p1qr1p2/2pb1Bp1/1p5p/3P1n1R/1B3P2/PP3PK1/2Q4R w - - bm Qxf4; id \"WAC.141\";\n" +
            "r2q3n/ppp2pk1/3p4/5Pr1/2NP1Qp1/2P2pP1/PP3K2/4R2R w - - bm Re8 f6+; id \"WAC.142\";\n" +
            "5b2/pp2r1pk/2pp1pRp/4rP1N/2P1P3/1P4QP/P3q1P1/5R1K w - - bm Rxh6+; id \"WAC.143\";\n" +
            "r2q1rk1/pp3ppp/2p2b2/8/B2pPPb1/7P/PPP1N1P1/R2Q1RK1 b - - bm d3; id \"WAC.144\";\n" +
            "r1bq4/1p4kp/3p1n2/p4pB1/2pQ4/8/1P4PP/4RRK1 w - - bm Re8; id \"WAC.145\";\n" +
            "8/8/2Kp4/3P1B2/2P2k2/5p2/8/8 w - - bm Bc8 Bd3 Bh3; id \"WAC.146\";\n" +
            "r2r2k1/ppqbppbp/2n2np1/2pp4/6P1/1P1PPNNP/PBP2PB1/R2QK2R b KQ - bm Nxg4; id \"WAC.147\";\n" +
            "2r1k3/6pr/p1nBP3/1p3p1p/2q5/2P5/P1R4P/K2Q2R1 w - - bm Rxg7; id \"WAC.148\";\n" +
            "6k1/6p1/2p4p/4Pp2/4b1qP/2Br4/1P2RQPK/8 b - - bm Bxg2; id \"WAC.149\";\n" +
            "";

    private static final String[] splitUpWACs = wacTests.split("\\\n");

}
    