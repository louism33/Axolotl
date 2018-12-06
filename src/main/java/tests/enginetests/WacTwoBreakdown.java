package tests.enginetests;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.search.Engine;
import old.chessprogram.chess.Chessboard;
import old.chessprogram.chess.Move;
import old.chessprogram.graphicsandui.Art;
import old.chessprogram.miscAdmin.ExtendedPositionDescriptionParser;
import old.chessprogram.moveGeneration.MoveGeneratorMaster;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class WacTwoBreakdown {

    private static final int timeLimit = 25_000;

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();

        for (String splitUpWAC : splitUpWACs) {
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EPDObject EPDObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpWAC);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }

        return answers;
    }


    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public WacTwoBreakdown(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }


    public static void reset(){
        Engine engine = new Engine();
    }

    @org.junit.Test
    public void test() {
        Engine engine = new Engine();
        Chessboard board = EPDObject.getBoard();
        System.out.println(Art.boardArt(board));

        System.out.println("base score: ");
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
//        System.out.println(moves);
        System.out.println(new Evaluator(engine).eval(board, board.isWhiteTurn(),
                moves));

        Move move = engine.searchFixedTime(board, timeLimit);
        System.out.println(move);
        
//        Move move = engine.searchFixedDepth(board, 5);
//        System.out.println(move);




        List<Integer> winningMoveDestination = EPDObject.getBestMoveDestinationIndex();
        int myMoveDestination = move.destinationIndex;

        Assert.assertTrue(winningMoveDestination.contains(myMoveDestination));


        List<Integer> losingMoveDestination = EPDObject.getAvoidMoveDestinationIndex();

        Assert.assertFalse(losingMoveDestination.contains(myMoveDestination));
    }

    private static final String wacTests = ""
            + "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - - bm Rxb2; id \"WAC.0021\";\n" 
            + "8/7p/5k2/5p2/p1p2P2/P2pPK2/1r1R3P/8 w - - bm Rxb2; id \"WAC.0022\";\n" 
            + "8/7p/5k2/5p2/p1p2P2/P2pPK2/1R5P/8 b - - bm c3; id \"WAC.0023\";\n" 
            + "8/7p/5k2/5p2/p4P2/P1ppPK2/1R5P/8 w - - bm Rb6; id \"WAC.0024\";\n" 
            + "8/7p/1R3k2/5p2/p4P2/P1ppPK2/7P/8 b - - bm Ke7; id \"WAC.0025\";\n" 
            + "8/4k2p/1R6/5p2/p4P2/P1ppPK2/7P/8 w - - bm Rc6; id \"WAC.0026\";\n" 
            
            + "8/4k2p/2R5/5p2/p4P2/P1ppPK2/7P/8 b - - bm c2; id \"WAC.0027\";\n" 
            + "8/4k2p/2R5/5p2/p4P2/P2pPK2/2p4P/8 w - - bm Kf2; id \"WAC.0028\";\n"
            
            + "8/4k2p/2R5/5p2/p4P2/P2pP3/2p2K1P/8 b - - bm d2; id \"WAC.0029\";\n" 
            + "8/4k2p/2R5/5p2/p4P2/P3P3/2pp1K1P/8 w - - bm Rc2; id \"WAC.00291\";\n" 
            
            + "8/4k2p/8/5p2/p4P2/P3P3/2Rp1K1P/8 b - - bm d1q; id \"WAC.00292\";\n" 
            
            
            
            
            ;


    private static final String[] splitUpWACs = wacTests.split("\\\n");
    static int totalWACS = splitUpWACs.length;

}

