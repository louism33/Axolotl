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
public class ZugzwangPositions3 {

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

    public ZugzwangPositions3(Object edp, Object name) {
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
            "8/8/p1p5/1p5p/1P5p/8/PPP2K1p/4R1rk w - - 0 1 bm Rf1; id \"zugzwang.001\";\n" +
            "1q1k4/2Rr4/8/2Q3K1/8/8/8/8 w - - 0 1 bm Kh6;  id \"zugzwang.002\";\n" +
            "7k/5K2/5P1p/3p4/6P1/3p4/8/8 w - - 0 1 bm g5; id \"zugzwang.003\";\n" +
            "8/6B1/p5p1/Pp4kp/1P5r/5P1Q/4q1PK/8 w - - 0 32 bm Qxh4; id \"zugzwang.004\";\n" +
            "8/8/1p1r1k2/p1pPN1p1/P3KnP1/1P6/8/3R4 b - - 0 1 bm Nxd5; id \"zugzwang.005\"" +
            "";

    private static final String[] splitUpZZs = zzTests.split("\n");

}
