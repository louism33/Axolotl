package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static challenges.Utils.contains;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;

@RunWith(Parameterized.class)
public class TranspositionTableSearchSanity {

    private static final int threads = 4;
    private static final int timeLimit = 10_000;
    private static int successes = 0;
    private static final int targetSuccesses = 145;
    private static Engine engine = new Engine();

    @BeforeClass
    public static void setup() {
        Engine.resetFull();
        EngineSpecifications.PRINT_PV = true;
        Engine.setThreads(threads);
    }

    @AfterClass
    public static void finalSuccessTally() {
        EngineSpecifications.PRINT_PV = false;
    }


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

    public TranspositionTableSearchSanity(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        System.out.println(EPDObject.getFullString());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        final Chessboard board = EPDObject.getBoard();
        
        engine.receiveSearchSpecs(board, true, timeLimit);
        final int move = engine.simpleSearch();

        Assert.assertTrue(Engine.aiMoveScore >= CHECKMATE_ENEMY_SCORE_MAX_PLY);
//        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }

    private static final String positions = "" +
            "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - bm Rxh8+; dm 7;\n" +
            "3k4/1p6/1P1K4/2P5/8/8/8/8 w - - bm Ke6; dm 8;\n" +
            "k7/P7/1P6/1K6/8/8/8/8 w - - bm Kc5; dm 8;\n" +
            "4k3/8/8/PpPpPpPp/PpPpPpPp/8/8/4K3 b - a3 bm bxa3; dm 11;\n" +
            "4k3/8/8/8/8/8/R7/4K3 w - - bm Ra7; dm 12;\n" +
            "8/2k5/4P3/8/1K6/8/8/8 w - - bm Kc5; dm 12;\n" +
            "8/8/4K3/4P2p/8/5k2/8/8 w - - bm Kf5; dm 13;\n" +
            "8/8/8/1k6/8/8/8/RK6 w - - bm Kc2; dm 13;\n" +
            "8/3p1k2/3P4/2P2K2/8/8/8/8 w - - bm Ke5; dm 14;\n" +
            "6k1/7p/7K/7P/8/8/6P1/8 w - - bm g3; dm 15;\n" +
            "8/4k3/8/8/4PK2/8/8/8 w - - bm Ke5; dm 15;\n" +
            "8/8/K1p1k3/2P5/8/8/8/8 w - - bm Kb7; dm 15;\n" +
            "8/p7/6k1/1P6/6KP/8/8/8 w - - bm Kf4 h5+; dm 15;\n" +
            "6k1/8/5KP1/6P1/8/8/8/8 w - - bm g7; dm 16;\n" +
            "8/2k5/8/8/6K1/8/7P/8 w - - bm Kf5 Kg5 Kh5; dm 16;\n" +
            "8/8/2p5/8/8/5k2/P7/5K2 w - - bm a4; dm 16;\n" +
            "8/8/8/6K1/8/5k2/P7/8 w - - bm Kf5; dm 16;\n" +
            "8/2k5/p1P5/P1K5/8/8/8/8 w - - bm Kd5; dm 17;\n" +
            "8/6p1/8/4K3/7P/4k2P/8/8 w - - bm Ke6; dm 17;\n" +
            "8/8/6p1/7p/5k2/8/4K2P/8 b - - bm Kg4; dm 17;\n" +
            "8/8/8/7p/1PK2k2/8/8/8 w - - bm b5; dm 17;\n" +
            "8/p4K2/P7/8/8/8/1k6/8 w - - bm Ke6; dm 17;\n" +
            "k7/P7/1P6/4p3/4Pp2/5K2/8/8 w - - bm Kf2; dm 17;\n" +
            "3k4/2p5/2K5/1P1P4/8/8/8/8 w - - bm Kb7; dm 18;\n" +
            "3k4/8/2pP1K2/2P5/8/8/8/8 w - - bm d7; dm 18;\n" +
            "4K3/8/2p5/8/P2k4/8/P7/8 w - - bm a5; dm 18;\n" +
            "8/6k1/8/1p1K4/5P1P/8/8/8 w - - bm Kc5; dm 18;\n" +
            "8/6p1/8/5P2/5P2/5K2/8/6k1 w - - bm f6; dm 18;\n" +
            "8/8/1p6/8/8/6P1/k1K5/8 w - - bm Kc3; dm 18;\n" +
            "8/8/8/8/4P1k1/8/5K2/8 w - - bm Ke3; dm 18;\n" +
            "7k/8/5PpK/Pp1P2pp/3P4/8/5p2/8 w - - bm a6; dm 19;\n" +
            "8/7k/4K2p/6p1/6P1/7P/8/8 w - - bm Kf7; dm 19;\n" +
            "8/8/1k1K4/pP6/P7/8/8/8 w - - bm Kd5 Ke6; dm 19;\n" +
            "8/8/4k3/3p2p1/1P1K2P1/8/8/8 w - - bm Kc5 b5; dm 19;\n" +
            "8/8/5p2/pK6/2Pk2P1/8/8/8 w - - bm g5; dm 19;\n" +
            "8/ppp5/8/PPP5/8/7k/8/7K w - - bm b6; dm 19;\n" +
            "1k6/8/P1P5/8/5ppp/8/6K1/8 w - - bm Kg1; dm 20;\n" +
            "8/5p2/2k5/K7/8/1P6/8/8 b - - bm Kd5; dm 20;\n" +
            "8/8/4k3/1pp5/2p1PP2/8/3K4/8 b - - bm b4; dm 20;\n" +
            "8/2k5/2Pp3p/1P6/8/5K2/8/8 w - - bm Kf4; dm 21;\n" +
            "8/8/1p4K1/p1p5/P1P3k1/1P6/8/8 w - - bm Kf7; dm 21;\n" +
            "8/8/8/4kPp1/6P1/4K3/8/8 w - - bm Kd2 Ke2; dm 21;\n" +
            "8/p2p4/8/8/8/k7/5P1P/7K w - - bm f4; dm 21;\n" +
            "4k3/8/8/8/8/8/4P3/4K3 w - - bm Kd2; dm 22;\n" +
            "8/2p5/1pPp4/1P1Pp3/4Pp1k/5P2/5KP1/8 w - - bm g3+; dm 22;\n" +
            "8/5k2/6p1/5pKp/8/6P1/5P1P/8 w - - bm Kh6; dm 22;\n" +
            "8/8/8/1P2kp2/P2p2p1/6P1/3K4/8 b - - bm f4; dm 24;\n" +
            "8/1p3pp1/7p/5P1P/2k3P1/8/2K2P2/8 w - - bm f6; dm 25;\n" +
            "8/2p4p/7P/2K5/2P5/8/k7/8 w - - bm Kb4; dm 25;\n" +
            "4K3/8/8/1p5p/1P5P/8/8/4k3 w - - bm Ke7; dm 26;\n" +
            "6k1/6p1/8/4K1P1/8/7P/8/8 w - - bm Kf4; dm 26;\n" +
            "8/8/1kp5/8/K1PP4/8/8/8 w - - bm Kb3; dm 27;\n" +
            "8/8/p7/8/1P6/7p/P4k1P/3K4 w - - bm a3; dm 27;\n" +
            "8/6p1/p7/5P1p/1Pk5/8/5KPP/8 w - - bm h4; dm 28;\n" +
            "7k/6p1/6P1/8/8/p5K1/P7/8 w - - bm Kf4; dm 30;\n" +
            "8/8/8/KP6/1p6/k4p2/5P2/8 w - - bm b6; dm 30;\n" +
            "k7/8/1p6/p1p5/2P4K/8/PP6/8 w - - bm a4; dm 32;\n" +
            "k7/4p3/4p3/8/8/3P1P2/5P2/K7 w - - bm Kb2; dm 33;\n" +
            "8/5p2/7p/5pk1/8/5KPP/8/8 b - - bm h5; dm 35;\n" +
            "8/8/4k3/6p1/2pPpP2/4P2P/6K1/8 w - - bm f5+; dm 35;\n" +
            "8/k1b5/P4p2/1Pp2p1p/K1P2P1P/8/3B4/8 w - - bm b6+; dm 49;\n" +
            "k7/2p1pp2/2P3p1/4P1P1/5P2/p7/Kp3P2/8 w - - bm f5; dm 64;\n" +
            "r3k2r/8/8/8/8/8/8/R3K2R w - - bm Rxa8+; dm 65;\n" +
            "r3k2r/8/8/8/8/8/8/R3K2R w K - bm Rxa8+; dm 65;\n" +
            "r3k2r/8/8/8/8/8/8/R3K2R w k - bm Rxa8+; dm 65;\n" +
            "r3k2r/8/8/8/8/8/8/R3K2R w Q - bm Rxa8+; dm 65;\n" +
            "r3k2r/8/8/8/8/8/8/R3K2R w q - bm Rxa8+; dm 65;\n" +
            "8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - - bm Kb1; dm 35;\n" +
            "8/2pp2pp/8/2PP1P2/1p5k/8/PP4p1/6K1 w - - bm f6; dm 82;\n" +
            "8/5pp1/4p2p/3k3P/1p1P2P1/1P1K1P2/8/8 b - - bm g5; dm 82;\n" +
            "8/5p1p/8/6k1/8/6P1/5PP1/7K w - - bm Kh2; dm 85;\n" +
            "k7/8/pp6/2p4K/8/PPP5/8/8 w - - bm c4; dm 90;" +
            "";

    private static final String[] checkmatePositions = positions.split("\n");
}


