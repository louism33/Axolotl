package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.axolotl.search.SearchSpecs;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.louism33.axolotl.search.EngineSpecifications.MAX_TABLE_SIZE_MB;

@RunWith(Parameterized.class)
public class MateTest2 {

    private static final int timeLimit = 180_000;

    private static Engine engine = new Engine();

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < checkmatePositions.length; i++) {
            String pos = checkmatePositions[i];
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject =
                    ExtendedPositionDescriptionParser.parseEDPPosition(pos);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getBestPrettyMoves();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public MateTest2(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        EngineSpecifications.TABLE_SIZE_MB = MAX_TABLE_SIZE_MB;
                
        Engine.resetFull();
                
        System.out.println(EPDObject.getFullString());

        int[] winningMoves = EPDObject.getBestMoves();
        EngineSpecifications.PRINT_PV = true;
        final Chessboard board = EPDObject.getBoard();
                
        SearchSpecs.basicTimeSearch(timeLimit);
        System.out.println(board);
        final int move = engine.simpleSearch(EPDObject.getBoard());
        MoveParser.printMove(move);
        Assert.assertTrue(Utils.contains(winningMoves, move));

    }

    private static final String positions = "" +
            "8/2Nb4/pp6/4rp1p/1Pp1pPkP/PpPpR3/1B1P2N1/1K6 w - - bm Kc1 fxe5; #5;\n" +
            "8/8/2B1N3/3rp3/4k2K/7Q/2r3Pn/1b1N4 w - - bm Kh5 Kg5; #5;\n" +
            "qb6/3N2p1/r2p4/pP1k2p1/1pp3R1/6BB/2P1P3/2N1K3 w - - bm Bh4 bxa6; #5;\n" +
            "1K1N1b2/RPp1pr2/1kP5/2p5/P7/4B1P1/4p1b1/6n1 w - - bm Kc8 Bd2; #6;\n" +
            "n1N3br/2p1Bpkr/1pP2R1b/pP1p1PpR/Pp4P1/1P6/1K1P4/8 w - - bm Rh1 Nd6; #6;\n" +
            "1N1K1b1r/P3pPp1/4k1P1/rp1pB1RN/q4RP1/8/p2pB1p1/1b6 w - - bm Nd7 Rxa4; #6;\n" +
            "5R2/2P2pK1/2P2P2/1Pp1BP1P/b6p/1p1RPB2/1p2NPn1/6rk w - - bm Ra8 Bxb2 h6; #6;\n" +
            "3K4/1p1B4/bB1k4/rpR1p3/2ppppp1/8/RPPPPP2/r1n5 w - - bm b4 Rxa5; #7;\n" +
            "4k1r1/2pnp3/2B1N3/8/8/5R2/6P1/5K2 w - - bm Rf2 Ke1 Rf8+; #11;\n" +
            "n2Bqk2/5p1p/Q4KP1/p7/8/8/8/8 w - - bm Qc8; #13; \n" +
            "8/5K2/3p3p/3p3P/pp1P4/rkb1R3/p1p3P1/n1B2B2 w - - bm Rd3; #16;\n" +
            "1kn5/p2p4/P1pP1p1q/1PP2P1P/5p2/4rQp1/K7/5B2 w - - bm bxc6; #17;\n" +
            "8/6p1/p7/rp1K2p1/kb3pP1/2p2p1b/P1Np1P2/3N2R1 w - - bm Ke6; #20;\n" +
            "8/7p/6pP/5pP1/3BpP2/p1KpP3/pn1N4/k7 w - - bm Bh8 Be5; #21;\n" +
            "1B3N2/5p1B/2K2pn1/5krb/4p1p1/4P1P1/2P1PP2/8 w - - bm Ba7; #22;\n" +
            "1k3b1q/pP2p1p1/P1K1P1Pp/7P/2B5/8/8/8 w - - bm Bb5 Kd5; #27;\n" +
            "4rk2/2P2p2/p4P2/2p2b2/2p5/8/P7/2KR4 w - - bm Rd8; #29 ;" +
            "";

    private static final String[] checkmatePositions = positions.split("\n");
}


