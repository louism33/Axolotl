package strategictestsuite;

import com.github.louism33.axolotl.search.Engine;


//@RunWith(Parameterized.class)
public class MasterParamTester {

    public static boolean printBoard = false, printFen = true;
    
//    public static final int timeLimit = 1_000;
    public static final int timeLimit = 50;
    public Engine engine = new Engine();

    private static int successes = 0;

//    @BeforeAll
//    public void reset() {
//        ResettingUtils.reset();
//    }
//    
//    @AfterClass
//    public static void finalSuccessTally() {
//        System.out.println("Successes: " + successes + " out of " + splitUpPositions.length);
//        ResettingUtils.reset();
//    }
//
//    @Parameterized.Parameters(name = "{index} Test: {1}")
//    public static Collection<Object[]> data() {
//        List<Object[]> answers = new ArrayList<>();
//
//        for (int i = 0; i < splitUpPositions.length; i++) {
//
//            String splitUpWAC = splitUpPositions[i];
//            Object[] objectAndName = new Object[2];
//            ExtendedPositionDescriptionParser.EPDObject EPDObject = parseEDPPosition(splitUpWAC);
//            objectAndName[0] = EPDObject;
//            objectAndName[1] = EPDObject.getId();
//            answers.add(objectAndName);
//        }
//        return answers;
//    }
//
//    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;
//
//    public MasterParamTester(Object edp, Object name) {
//        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
//    }
//
//    @Test
//    public void test() {
//        Engine.resetFull();
//        if (printFen) {
//            System.out.println(EPDObject.getFullString());
//        }
//        if (printBoard) {
//            System.out.println(EPDObject.getBoard());
//        }
//        int[] winningMoves = EPDObject.getBestMovesFromComments();
//        int[] losingMoves = EPDObject.getAvoidMoves();
//        
//        SearchSpecs.basicTimeSearch(timeLimit);
//        final int move = engine.simpleSearch(EPDObject.getBoard());
//
//        final boolean condition = contains(winningMoves, move) && !contains(losingMoves, move);
//        if (condition) {
//            successes++;
//        }
//        Assert.assertTrue(condition);
//    }
//
//    private static final String positions = "";

//    private static final String[] splitUpPositions = positions.split("\n");

}

    
