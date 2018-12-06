package tests.enginetests;

import com.github.louism33.axolotl.search.Engine;
import old.chessprogram.chess.Move;
import old.chessprogram.graphicsandui.Art;
import old.chessprogram.miscAdmin.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ZugzwangPositions {

    private static final int timeLimit = 10000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();
        for (String splitUpZZ : splitUpZZs) {
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpZZ);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }


    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public ZugzwangPositions(Object edp, Object name) {
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


    private static final String zzTests = "" +
            "8/8/1p1r1k2/p1pPN1p1/P3KnP1/1P6/8/3R4 b - - bm Nxd5; id \"ZPTS.01\"; \n" +
            "4KBkr/7p/6PP/4P3/8/3P1p2/8/8 w - - bm g7; id \"ZPTS.02\"; \n" +
            "8/6B1/p5p1/Pp4kp/1P5r/5P1Q/4q1PK/8 w - - bm Qxh4; id \"ZPTS.03\"; \n" +
            "8/8/8/2p5/1pp5/brpp4/1pprp2P/qnkbK3 w - - bm h3; id \"ZPTS.04\"; \n" +
            "6Q1/8/8/7k/8/8/3p1pp1/3Kbrrb w - - bm Qg7; id \"ZPTS.05\"; \n" +
            "n1QBq1k1/5p1p/5KP1/p7/8/8/8/8 w - - bm Bc7; id \"ZPTS.06\"; \n" +
            "3nQ1k1/p2P2p1/1p6/8/5q1P/8/PP6/1K6 b - - bm Kh7; id \"ZPTS.07\"; \n" +
            "8/8/8/1B6/6p1/8/4KPpp/3N2kr w - - bm Kd3 Ke3; id \"ZPTS.08\"; \n" +
            "8/3p1p2/5Ppp/K2R2bk/4pPrr/6Pp/4B2P/3N4 w - - bm Nc3; id \"ZPTS.09\"; \n" +
            "8/1B6/8/5p2/8/8/5Qrq/1K1R2bk w - - bm Qa7; id \"ZPTS.10\"; \n" +
            "5R2/2K5/1pP5/4k2p/3pp3/2p4N/B4N1b/n1R1B2b w - - bm Rxc4; id \"ZPTS.11\";\n" +
            "4B3/8/p7/k2N4/7p/K6p/PP5P/2q5 w - - bm Ba4; id \"ZPTS.12\"; \n" +
            "3k4/8/4K3/2R5/8/8/8/8 w - - bm Rc6; id \"ZPTS.13\"; \n" +
            "1k6/7R/2K5/8/8/8/8/8 w - - bm Rh8; id \"ZPTS.14\"; \n" +
            "8/3k4/8/8/3PK3/8/8/8 w - - bm Kd5; id \"ZPTS.15\"; \n" +
            "2k5/8/1K1P4/8/8/8/8/8 w - - bm Kc6; id \"ZPTS.16\";\n" +
            "1b6/8/8/7p/6k1/6P1/8/6K1 w - - bm Kg2; id \"ZPTS.17\"; \n" +
            "k2N2K1/8/8/8/5R2/3n4/3p4/8 w - - bm Rf7; id \"ZPTS.18\"; \n" +
            "2n5/8/Pp5n/5N2/8/5k2/3P4/7K w - - bm d4; id \"ZPTS.19\"; \n" +
            "8/5b2/p2k4/1p1p1p1p/1P1K1P1P/2P1PB2/8/8 w - - bm Be2; id \"ZPTS.20\"; \n" +
            "8/8/p3R3/1p5p/1P5p/6rp/5K1p/7k w - - bm Re1; id \"ZPTS.21\"; \n" +
            "8/p7/1p6/p7/kq1Q4/8/K7/8 w - - bm Qd3; id \"ZPTS.22\"; \n" +
            "8/8/8/4N3/8/7p/8/5K1k w - - bm Ng4; id \"ZPTS.23\"; \n" +
            "1r4RK/2n5/7k/8/8/8/8/8 b - - bm Ne8; id \"ZPTS.24\"; \n" +
            "8/8/1p1K4/Pp6/2k1p3/8/1P6/8 w - - bm a6; id \"ZPTS.25\";\n" +
            "6k1/3p4/P2P4/8/5Kp1/1p4Q1/p5p1/b7 w - - bm Qxg2; id \"ZPTS.26\"; \n" +
            "8/5p2/4b1p1/7R/5K1P/2r3B1/7N/4b1k1 w - - bm Nf3; id \"ZPTS.27\"; \n" +
            "8/8/7p/2R5/4pp1K/8/8/3k2b1 w - - bm Rc4; id \"ZPTS.28\";  \n" +
            "8/1p5k/1P1p4/3p4/3Pp2p/2K1P2p/7P/8 w - - bm Kb2; id \"ZPTS.29\"; \n" +
            "8/3p1N2/8/4B3/2K2p2/b3P3/pP2P3/k7 b - - bm d6; id \"ZPTS.30\";" +
            "";

    private static final String[] splitUpZZs = zzTests.split("\\\n");

}

/*
8/8/1p1r1k2/p1pPN1p1/P3KnP1/1P6/8/3R4 b - - bm Nxd5; id "ZPTS.01"; 
4KBkr/7p/6PP/4P3/8/3P1p2/8/8 w - - bm g7; id "ZPTS.02"; 
8/6B1/p5p1/Pp4kp/1P5r/5P1Q/4q1PK/8 w - - bm Qxh4; id "ZPTS.03"; 
8/8/8/2p5/1pp5/brpp4/1pprp2P/qnkbK3 w - - bm h3; id "ZPTS.04"; 
6Q1/8/8/7k/8/8/3p1pp1/3Kbrrb w - - bm Qg7; id "ZPTS.05"; 
n1QBq1k1/5p1p/5KP1/p7/8/8/8/8 w - - bm Bc7; id "ZPTS.06"; 
3nQ1k1/p2P2p1/1p6/8/5q1P/8/PP6/1K6 b - - bm Kh7; id "ZPTS.07"; 
8/8/8/1B6/6p1/8/4KPpp/3N2kr w - - bm Kd3 Ke3; id "ZPTS.08"; 
8/3p1p2/5Ppp/K2R2bk/4pPrr/6Pp/4B2P/3N4 w - - bm Nc3; id "ZPTS.09"; 
8/1B6/8/5p2/8/8/5Qrq/1K1R2bk w - - bm Qa7; id "ZPTS.10"; 
5R2/2K5/1pP5/4k2p/3pp3/2p4N/B4N1b/n1R1B2b w - - bm Rxc4; id "ZPTS.11";
4B3/8/p7/k2N4/7p/K6p/PP5P/2q5 w - - bm Ba4; id "ZPTS.12"; 
3k4/8/4K3/2R5/8/8/8/8 w - - bm Rc6; id "ZPTS.13"; 
1k6/7R/2K5/8/8/8/8/8 w - - bm Rh8; id "ZPTS.14"; 
8/3k4/8/8/3PK3/8/8/8 w - - bm Kd5; id "ZPTS.15"; 
2k5/8/1K1P4/8/8/8/8/8 w - - bm Kc6; id "ZPTS.16";
1b6/8/8/7p/6k1/6P1/8/6K1 w - - bm Kg2; id "ZPTS.17"; 
k2N2K1/8/8/8/5R2/3n4/3p4/8 w - - bm Rf7; id "ZPTS.18"; 
2n5/8/Pp5n/5N2/8/5k2/3P4/7K w - - bm d4; id "ZPTS.19"; 
8/5b2/p2k4/1p1p1p1p/1P1K1P1P/2P1PB2/8/8 w - - bm Be2; id "ZPTS.20"; 
8/8/p3R3/1p5p/1P5p/6rp/5K1p/7k w - - bm Re1; id "ZPTS.21"; 
8/p7/1p6/p7/kq1Q4/8/K7/8 w - - bm Qd3; id "ZPTS.22"; 
8/8/8/4N3/8/7p/8/5K1k w - - bm Ng4; id "ZPTS.23"; 
1r4RK/2n5/7k/8/8/8/8/8 b - - bm Ne8; id "ZPTS.24"; 
8/8/1p1K4/Pp6/2k1p3/8/1P6/8 w - - bm a6; id "ZPTS.25";
6k1/3p4/P2P4/8/5Kp1/1p4Q1/p5p1/b7 w - - bm Qxg2; id "ZPTS.26"; 
8/5p2/4b1p1/7R/5K1P/2r3B1/7N/4b1k1 w - - bm Nf3; id "ZPTS.27"; 
8/8/7p/2R5/4pp1K/8/8/3k2b1 w - - bm Rc4; id "ZPTS.28";  
8/1p5k/1P1p4/3p4/3Pp2p/2K1P2p/7P/8 w - - bm Kb2; id "ZPTS.29"; 
8/3p1N2/8/4B3/2K2p2/b3P3/pP2P3/k7 b - - bm d6; id "ZPTS.30";

 */