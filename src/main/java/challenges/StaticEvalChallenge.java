package challenges;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class StaticEvalChallenge {

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

    public StaticEvalChallenge(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        System.out.println(EPDObject.getFullString());
        Chessboard board = EPDObject.getBoard();
        System.out.println(Evaluator.eval(board, board.generateLegalMoves()));

        Evaluator.printEval(board, board.turn, board.generateLegalMoves());

    }

    private static final String zzTests = "" +
//            "[King's safety]\n" +
//            "\n" +
            "r2k1b1r/2p2ppp/p3q3/2PpN3/Q2Pn3/4B3/PP3PPP/1R2R1K1 b - - ; id \"KS-00001\"\n" +
            "2rqkb1r/p2p1p2/b1n2p2/1p2PN1p/8/8/PB3PPP/2RQR1K1 b k - ; id \"KS-00002\"\n" +
            "rnb2b1r/ppp1k1pp/q7/3B4/4n3/4BN2/PPQ2PPP/3RK2R b K - ; id \"KS-00003\"\n" +
            "1nr3k1/pp4rp/nq2p1p1/1b1pP3/3P1N2/1BP2Q2/PP3P2/K5RR w - - ; id \"KS-00004\"\n" +
            "r3r1k1/1bq2p2/p1np1n1Q/1p3P2/4P3/P1N2BN1/1PP5/2KR4 b - - ; id \"KS-00005\"\n" +
            "3r1r1k/pb3p1p/1pn2P2/2p1q2p/8/b2BQ3/P1P3PP/3R1R1K w - - ; id \"KS-00006\"\n" +
            "3r1rk1/pb2np1p/1p4pQ/2p1qP1N/8/P2B4/1P4PP/3R1R1K w - - ; id \"KS-00007\"\n" +
            "4rk1b/8/p3pPQ1/q3p3/1r1B4/1PN5/1P6/6RK b - - ; id \"KS-00008\"\n" +
            "r2r3k/pp1b1pRp/2p1pP2/8/2PP1b2/P7/1PB2P1P/1K1R4 b - - ; id \"KS-00009\"\n" +
            "rq3r2/3bp3/5pk1/2pPP3/ppn2P1Q/3R4/P3N1PP/6K1 w - - ; id \"KS-00010\"\n" +
            "3r2k1/1pp2ppp/7r/1P2p2q/3P4/3P1PRn/1BQ1BP2/2R4K b - - ; id \"KS-00011\"\n" +
            "r3qr2/1b1n2k1/5p2/1p1PpR1p/2p5/2P4P/1PBQ2P1/5R1K w - - ; id \"KS-00012\"\n" +
            "r3qr1k/1b1n4/8/1p1Pp1Qp/2p5/2P4P/1PB3P1/5R1K w - - ; id \"KS-00013\"\n" +
            "r1r3kb/pp2pp2/3p1npB/6Q1/2qNPNP1/3R1P2/2P1K3/7R b - - ; id \"KS-00014\"\n" +
            "1n3rk1/Q5p1/3bq2p/1p6/3P1P2/5bP1/PP1B1P1P/RN3RK1 w - - ; id \"KS-00015\"\n" +
//            "\n" +
//            "[Passed pawns]\n" +
//            "\n" +
            "8/1r5k/3R4/pp6/5B2/1q3P2/6PP/2R4K b - -; id \"PP-00001\"\n" +
            "8/7p/6p1/8/6P1/2K1kp1P/8/4B3 w - -; id \"PP-00002\"\n" +
            "8/7p/6p1/8/6P1/3k1p1P/1K3B2/8 w - -; id \"PP-00003\"\n" +
            "8/4k3/8/7P/1P6/3p4/4p3/4K3 b - -; id \"PP-00004\"\n" +
            "8/P5p1/4P3/8/2k5/2P1pb2/8/4K3 b - -; id \"PP-00005\"\n" +
            "1r6/3k2P1/2n1p3/b7/Pp1PB3/2p5/2K1N3/3R4 w - -; id \"PP-00006\"\n" +
            "6r1/4k3/4P1b1/2PP1p2/p5p1/8/8/4R1K1 w - -; id \"PP-00007\"\n" +
//            "\n" +
//            "[imbalances]\n" +
//            "\n" +
            "8/2p5/1b1p3k/1b6/2n1PPn1/1p5N/1P1R2PP/1B5K w - -; id \"IM-00001\"\n" +
            "6k1/1p3pb1/bn3np1/8/8/2P3P1/PPP2P1P/3RR1K1 w - -; id \"IM-00002\"\n" +
            "6k1/1pn2np1/2b2b2/8/8/2P3P1/PP3P1P/3RR1K1 w - -; id \"IM-00003\"\n" +
            "r7/p3p2k/1pp3nb/3b4/P6P/2Q5/1P4P1/5R1K w - -; id \"IM-00004\"\n" +
//            "\n" +
//            "\n" +
//            "[Endgames]\n" +
//            "\n" +
            "8/7p/6p1/6P1/7P/2K5/4k3/8 w - -; id \"EG-00001\"\n" +
            "8/8/8/6k1/8/8/5KBP/8 b - -; id \"EG-00001\"" +
            "";

    private static final String[] splitUpZZs = zzTests.split("\n");

}
