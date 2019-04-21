package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class SCHACKNYTTtests {

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

    private static int timeLimit = 0;
    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public SCHACKNYTTtests(Object edp, Object name) {
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

        Assert.assertTrue(Utils.contains(winningMoves, move) && !Utils.contains(losingMoves, move));
    }


    private static final String positions = "" +
            "1r2r1k1/1pqbbppp/p2p1n2/4p3/P3PP2/2N1BB2/1PP2QPP/R4R1K b - - bm Rbc8; c0 \"positional scores are: Rbc8=10, exf4=9, Bc6=7, Rec8=6, b6=6, b5=5, Be6=4, h6=3\"; id \"tony.pos.01\";\n" +
            "r1bk1bnr/ppp2ppp/8/4n3/2P5/P3B3/1P3PPP/RN2KBNR w KQ - - bm Nf3; c0 \"positional scores are: Nf3=10, Nd2=9, Nc3=7, Be2=6, Bf4=4, f4=3, h3=3\"; id \"tony.pos.02\";\n" +
            "8/7p/3k2p1/6P1/4KP2/8/7P/8 w - - bm h4; c0 \"positional scores are: h4=10, Kf3=8, Ke3=6, Kd4=6, Kd3=6, h3=5\"; id \"tony.pos.03\";\n" +
            "r1b1r1k1/1p1n1pbp/2p1n1p1/q1P1p3/4P3/1PN1BNPP/2Q2PB1/1R1R2K1 w - - bm Rb2; c0 \"positional scores are: Rb2=10, Na4=8, b4=7, Nd2=6, Qb2=5, Bf1=5, Rd2=4, Ne2=3, Kh2=3, Ra1=1\"; id \"tony.pos.04\";\n" +
            "1rN1r1k1/1pq2pp1/2p1nn1p/p2p1B2/3P4/4P2P/PPQ1NPP1/2R2RK1 b - - bm Rexc8; c0 \"positional scores are: Rexc8=10, Rbxc8=7, Qxc8=7\"; id \"tony.pos.05\";\n" +
            "3r1rk1/p1q2pbp/1np1p1p1/1p2P3/5P2/2N2Q1P/PPP3P1/3RRBK1 b - - bm Rxd1; c0 \"positional scores are: Rxd1=10, f5=9, Na4=7, Nd5=5, a6=5, f6=4, g5=3\"; id \"tony.pos.06\";\n" +
            "r2r2k1/p3ppbp/1p4p1/3p4/3P4/2P1P2P/P3BPP1/2R2RK1 w - - bm f4; c0 \"positional scores are: f4=10, Ba6=8, c4=7, a4=5, Rfd1=4\"; id \"tony.pos.07\";\n" +
            "2rq3r/pb1pbkpp/1p2pp2/n1P5/2P5/QP2BNPB/P3PP1P/3R1RK1 w - - bm b4; c0 \"positional scores are: b4=10, Nd4=8, Rd2=5, Qc1=5\"; id \"tony.pos.08\";\n" +
            "1r1q1rk1/pp1bbppp/2n1p1n1/4P3/2BpN3/3P1N2/PP2QPPP/R1B1R1K1 b - - bm b5; c0 \"positional scores are: b5=10, Ngxe5=9, Ncxe5=9, Qc7=8, Na5=7, a6=6, h6=5, f6=3\"; id \"tony.pos.09\";\n" +
            "3r2k1/2p2ppp/1p1br3/pPn5/3PP3/P7/1B1N2PP/R3R1K1 w - - bm Re2; c0 \"positional scores are: Re2=10, d5=8, Nc4=7, e5=6, dxc5=5\"; id \"tony.pos.10\";\n" +
            "r4rk1/p2qn1bp/1pnp2p1/2p2p2/4PP1N/2PPB3/PP2QN1P/R4RK1 b - - bm d5; c0 \"positional scores are: d5=10, Bh6=8, Rae8=7, fxe4=5\"; id \"tony.pos.11\";\n" +
            "r3r1k1/1b1n1p2/1q1p1n1p/2p1p2P/p1P3p1/P1QNPPB1/1P2B1P1/2KR3R w - - bm e4; c0 \"positional scores are: e4=10, Rh4=8, Nf2=7, Qd2=6, Qc2=5, Bh4=4, Kb1=3\"; id \"tony.pos.12\";\n" +
            "1rb1nrk1/2q1bppp/p1n1p3/2p1P3/2Pp1PP1/3P1NN1/P5BP/R1BQ1RK1 w - - bm Nd2; c0 \"positional scores are: Nd2=10, Qe1=9, Qe2=8, Ne4=7, f5=6, Nh5=6, g5=6, h4=5, Bd2=4, Bh3=3, Re1=1\"; id \"tony.pos.13\";\n" +
            "r3r1k1/ppqbbpp1/2pp1nnp/3Pp3/2P1P3/5N1P/PPBN1PP1/R1BQR1K1 w - - bm Nb1; c0 \"positional scores are: Nb1=10, Rb1=9, Nf1=8, dxc6=5, Nb3=5, b3=4, a3=3, b4=2\"; id \"tony.pos.14\";\n" +
            "5r1k/1q2rnpp/p4p2/1pp5/6Q1/1P3P2/PBP3PP/3RR1K1 w - - bm Re6; c0 \"positional scores are: Re6=10, Kf2=9, h4=6, f4=5, Rxe7=4\"; id \"tony.pos.15\";\n" +
            "2r2k2/5p2/2Bp1b1r/2qPp1pp/PpN1P3/1P2Q3/5PPP/4R1K1 w - - bm Rc1; c0 \"positional scores are: Rc1=10, Nb6=8, Qxc5=7, a5=6, Qf3=5, h4=4, Nxe5=2\"; id \"tony.pos.16\";" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");
}

/*
1r2r1k1/1pqbbppp/p2p1n2/4p3/P3PP2/2N1BB2/1PP2QPP/R4R1K b - - bm Rbc8; c0 "positional scores are: Rbc8=10, exf4=9, Bc6=7, Rec8=6, b6=6, b5=5, Be6=4, h6=3"; id "tony.pos.01";
r1bk1bnr/ppp2ppp/8/4n3/2P5/P3B3/1P3PPP/RN2KBNR w KQ - - bm Nf3; c0 "positional scores are: Nf3=10, Nd2=9, Nc3=7, Be2=6, Bf4=4, f4=3, h3=3"; id "tony.pos.02";
8/7p/3k2p1/6P1/4KP2/8/7P/8 w - - bm h4; c0 "positional scores are: h4=10, Kf3=8, Ke3=6, Kd4=6, Kd3=6, h3=5"; id "tony.pos.03";
r1b1r1k1/1p1n1pbp/2p1n1p1/q1P1p3/4P3/1PN1BNPP/2Q2PB1/1R1R2K1 w - - bm Rb2; c0 "positional scores are: Rb2=10, Na4=8, b4=7, Nd2=6, Qb2=5, Bf1=5, Rd2=4, Ne2=3, Kh2=3, Ra1=1"; id "tony.pos.04";
1rN1r1k1/1pq2pp1/2p1nn1p/p2p1B2/3P4/4P2P/PPQ1NPP1/2R2RK1 b - - bm Rexc8; c0 "positional scores are: Rexc8=10, Rbxc8=7, Qxc8=7"; id "tony.pos.05";
3r1rk1/p1q2pbp/1np1p1p1/1p2P3/5P2/2N2Q1P/PPP3P1/3RRBK1 b - - bm Rxd1; c0 "positional scores are: Rxd1=10, f5=9, Na4=7, Nd5=5, a6=5, f6=4, g5=3"; id "tony.pos.06";
r2r2k1/p3ppbp/1p4p1/3p4/3P4/2P1P2P/P3BPP1/2R2RK1 w - - bm f4; c0 "positional scores are: f4=10, Ba6=8, c4=7, a4=5, Rfd1=4"; id "tony.pos.07";
2rq3r/pb1pbkpp/1p2pp2/n1P5/2P5/QP2BNPB/P3PP1P/3R1RK1 w - - bm b4; c0 "positional scores are: b4=10, Nd4=8, Rd2=5, Qc1=5"; id "tony.pos.08";
1r1q1rk1/pp1bbppp/2n1p1n1/4P3/2BpN3/3P1N2/PP2QPPP/R1B1R1K1 b - - bm b5; c0 "positional scores are: b5=10, Ngxe5=9, Ncxe5=9, Qc7=8, Na5=7, a6=6, h6=5, f6=3"; id "tony.pos.09";
3r2k1/2p2ppp/1p1br3/pPn5/3PP3/P7/1B1N2PP/R3R1K1 w - - bm Re2; c0 "positional scores are: Re2=10, d5=8, Nc4=7, e5=6, dxc5=5"; id "tony.pos.10";
r4rk1/p2qn1bp/1pnp2p1/2p2p2/4PP1N/2PPB3/PP2QN1P/R4RK1 b - - bm d5; c0 "positional scores are: d5=10, Bh6=8, Rae8=7, fxe4=5"; id "tony.pos.11";
r3r1k1/1b1n1p2/1q1p1n1p/2p1p2P/p1P3p1/P1QNPPB1/1P2B1P1/2KR3R w - - bm e4; c0 "positional scores are: e4=10, Rh4=8, Nf2=7, Qd2=6, Qc2=5, Bh4=4, Kb1=3"; id "tony.pos.12";
1rb1nrk1/2q1bppp/p1n1p3/2p1P3/2Pp1PP1/3P1NN1/P5BP/R1BQ1RK1 w - - bm Nd2; c0 "positional scores are: Nd2=10, Qe1=9, Qe2=8, Ne4=7, f5=6, Nh5=6, g5=6, h4=5, Bd2=4, Bh3=3, Re1=1"; id "tony.pos.13";
r3r1k1/ppqbbpp1/2pp1nnp/3Pp3/2P1P3/5N1P/PPBN1PP1/R1BQR1K1 w - - bm Nb1; c0 "positional scores are: Nb1=10, Rb1=9, Nf1=8, dxc6=5, Nb3=5, b3=4, a3=3, b4=2"; id "tony.pos.14";
5r1k/1q2rnpp/p4p2/1pp5/6Q1/1P3P2/PBP3PP/3RR1K1 w - - bm Re6; c0 "positional scores are: Re6=10, Kf2=9, h4=6, f4=5, Rxe7=4"; id "tony.pos.15";
2r2k2/5p2/2Bp1b1r/2qPp1pp/PpN1P3/1P2Q3/5PPP/4R1K1 w - - bm Rc1; c0 "positional scores are: Rc1=10, Nb6=8, Qxc5=7, a5=6, Qf3=5, h4=4, Nxe5=2"; id "tony.pos.16";
 */