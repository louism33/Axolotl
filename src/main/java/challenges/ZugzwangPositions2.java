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

@RunWith(Parameterized.class)
public class ZugzwangPositions2 {

    private static final int timeLimit = 10000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < splitUpZZs.length; i++) {
            String pos = splitUpZZs[i];
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(pos);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public ZugzwangPositions2(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getBoardFen());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.DEBUG = false;
        int move = Engine.searchFixedTime(EPDObject.getBoard(), timeLimit);

        Assert.assertTrue(Utils.contains(winningMoves, move) && !Utils.contains(losingMoves, move));
    }

    private static final String zzTests = "" +
            "8/8/1p1r1k2/p1pPN1p1/P3KnP1/1P6/8/3R4 b - - bm Nxd5; id \"ZPTS.01\"; CCC post 27.4.2004 by Gerd Isenberg\n" +
            "4KBkr/7p/6PP/4P3/8/3P1p2/8/8 w - - bm g7; id \"ZPTS.02\"; CCC post 27.4.2004 by Gerd Isenberg\n" +
            "8/6B1/p5p1/Pp4kp/1P5r/5P1Q/4q1PK/8 w - - bm Qxh4; id \"ZPTS.03\"; CCC post 10.9.2004 by Alvaro Begue\n" +
            "8/8/8/2p5/1pp5/brpp4/1pprp2P/qnkbK3 w - - bm h3; id \"ZPTS.04\"; CCC post by Joachim Rang, mate in 15, h4 is only draw\n" +
            "6Q1/8/8/7k/8/8/3p1pp1/3Kbrrb w - - bm Qg7; id \"ZPTS.05\"; CCC post by Joachim Rang, mate in 4\n" +
            "n1QBq1k1/5p1p/5KP1/p7/8/8/8/8 w - - bm Bc7; id \"ZPTS.06\"; CCC post\n" +
            "3nQ1k1/p2P2p1/1p6/8/5q1P/8/PP6/1K6 b - - bm Kh7; id \"ZPTS.07\"; CCC post\n" +
            "8/8/8/1B6/6p1/8/4KPpp/3N2kr w - - bm Kd3 Ke3; id \"ZPTS.08\"; CCC post by Tim Foden, id \"MES.831\", white wins, 1. Kd3 g3 2. f4 Kf1 3. Kd2+ Kg1 4. Bd7 Kf1 5. Bh3 Rg1 6. Bg4 Rh1 7. Be2+ Kg1 8. Nc3 Kf2 9. Ne4+ Kg1 10. Ng5 Kf2 11. Nh3#\n" +
            "8/3p1p2/5Ppp/K2R2bk/4pPrr/6Pp/4B2P/3N4 w - - bm Nc3; id \"ZPTS.09\"; CCC post by Tim Foden, id \"CCC.321751\"\n" +
            "8/1B6/8/5p2/8/8/5Qrq/1K1R2bk w - - bm Qa7; id \"ZPTS.10\"; CCC post by Tim Foden, id \"CCC.321759\"\n" +
            "5R2/2K5/1pP5/4k2p/3pp3/2p4N/B4N1b/n1R1B2b w - - bm Rxc3; id \"ZPTS.11\"; CCC post by Tim Foden, id \"CCC.321966\"\n" +
            "4B3/8/p7/k2N4/7p/K6p/PP5P/2q5 w - - bm Ba4; id \"ZPTS.12\"; CCC post by Tim Foden, id \"CCC.347609\"\n" +
            "3k4/8/4K3/2R5/8/8/8/8 w - - bm Rc6; id \"ZPTS.13\"; CCC post\n" +
            "1k6/7R/2K5/8/8/8/8/8 w - - bm Rh8; id \"ZPTS.14\"; CCC post\n" +
            "8/3k4/8/8/3PK3/8/8/8 w - - bm Kd5; id \"ZPTS.15\"; CCC post\n" +
            "2k5/8/1K1P4/8/8/8/8/8 w - - bm Kc6; id \"ZPTS.16\"; white wins\n" +
            "1b6/8/8/7p/6k1/6P1/8/6K1 w - - bm Kg2; id \"ZPTS.17\"; CCC post by Dr. Robert Hyatt, draw\n" +
            "k2N2K1/8/8/8/5R2/3n4/3p4/8 w - - bm Rf7; id \"ZPTS.18\"; CCC post by Ed Schroeder, Troitzky\n" +
            "2n5/8/Pp5n/5N2/8/5k2/3P4/7K w - - bm d4; id \"ZPTS.19\"; CCC post by Ed Schroeder, Troitzky\n" +
            "8/5b2/p2k4/1p1p1p1p/1P1K1P1P/2P1PB2/8/8 w - - bm Be2; id \"ZPTS.20\"; CCC post by Sune Fischer, white wins\n" +
            "8/8/p3R3/1p5p/1P5p/6rp/5K1p/7k w - - bm Re1; id \"ZPTS.21\"; CCC post by Eduard Nemeth, mate in 7\n" +
            "8/p7/1p6/p7/kq1Q4/8/K7/8 w - - bm Qd3; id \"ZPTS.22\"; post in Avler chess forum\n" +
            "8/8/8/4N3/8/7p/8/5K1k w - - bm Ng4; id \"ZPTS.23\"; white mates\n" +
            "1r4RK/2n5/7k/8/8/8/8/8 b - - bm Ne8; id \"ZPTS.24\"; Polgar - Kasparov, 1996: black wins after 91. Rf8, Kg6 92. Rg8+, Kf7\n" +
            "8/8/1p1K4/Pp6/2k1p3/8/1P6/8 w - - bm a6; id \"ZPTS.25\"; Kubbel 1927, 1 ... e3 2 a7 e2 3 a8/Q e1/Q 4 Qd5+ Kb4 5 Qd3!, white wins\n" +
            "6k1/3p4/P2P4/8/5Kp1/1p4Q1/p5p1/b7 w - - bm Qxg2; id \"ZPTS.26\"; Kasparyan 1959, 1 ... Be5+ 2 Kf5! a1/Q 3 a7! Qxa7 4 Kg6! Qa1! 5 Qd5! Kh8 6 Qe4!!, white wins\n" +
            "8/5p2/4b1p1/7R/5K1P/2r3B1/7N/4b1k1 w - - bm Nf3; id \"ZPTS.27\"; Noam Elkies 1984, 1 Nf3+! Rxf3+! 2 Kxf3 Bg4+! 3 Kf4!! Bxg3+ 4 Kxg4 gxh5+ 5 Kh3!! Kf1 6 Kxg3 Ke2 7 Kf4 f6 8 Kf5 Kf3 9 Kxf6 Kg4 10 Ke5 Kh4 11 Kf4, draw\n" +
            "8/8/7p/2R5/4pp1K/8/8/3k2b1 w - - bm Rc4; id \"ZPTS.28\"; Kricheli 1986, 1 Rc4! e3 2 Rd4+! Kc2 3 Rxf4 e2 4 Re4 Kd2 5 Rxe2+ Kxe2 6 Kg4!, draw\n" +
            "8/1p5k/1P1p4/3p4/3Pp2p/2K1P2p/7P/8 w - - bm Kb2; id \"ZPTS.29\"; 1 Kb2! Kg8 2 Ka1!!, draw\n" +
            " bm d6; id \"ZPTS.30\"; Noam Elkies 1987, 1 ... d6! 2 Bc3!! Bxb2 3 Kb3! Bxc3 4 Kc2 fxe3 5 Nd6 Ba5! 6 Nf5 Bb6 7 Ne7 Bd4 8 Nd5 Bc5 9 Nf4 Bb4(d6,e7,f8) 10 Ne6 Bc5! 11 Nd8! Bd4 12 Nb7! Bb6 13 Kc1!, mate in 16" +
            "";

    private static final String[] splitUpZZs = zzTests.split("\n");

}
