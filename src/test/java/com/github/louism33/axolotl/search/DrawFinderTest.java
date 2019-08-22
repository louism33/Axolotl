package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
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
public class DrawFinderTest {

    private static final int timeLimit = 1_000;
    private static Engine engine = new Engine();

    @BeforeClass
    public static void setup() {
        ResettingUtils.reset();
        Engine.setThreads(1);

        final String str = "Testing " + checkmatePositions.length + " Draw positions with " + 1 + " thread. " +
                "Time per position: " + timeLimit + " milliseconds.";
        System.out.println(str);
    }

    @AfterClass
    public static void finalSuccessTally() {
        ResettingUtils.reset();
        System.out.println();
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

    public DrawFinderTest(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        int[] winningMoves = EPDObject.getBestMoves();
        final Chessboard board = EPDObject.getBoard();
        SearchSpecs.basicTimeSearch(timeLimit);
        
//        EngineSpecifications.PRINT_PV = true;
        
        final int move = engine.simpleSearch(board);
        boolean whateverDraw = Engine.aiMoveScore == 0;
        Assert.assertTrue(contains(winningMoves, move) || whateverDraw);
    }

    private static final String positions = "" +
            "8/8/2Q2bk1/5p1p/4pP1P/7K/5q2/8 w - - bm Qxf6;";

    private static final String[] checkmatePositions = positions.split("\n");
}


