package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static challenges.Utils.contains;

@RunWith(Parameterized.class)
public class Pet {
    /*
    Arasan = 	60 sec.	42/48
     */

    private static final int timeLimit = 60_000;

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

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public Pet(Object edp, Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @Test
    public void test() {
        Engine.resetFull();
        System.out.println(EPDObject.getFullString());
        int[] winningMoves = EPDObject.getBestMoves();
        int[] losingMoves = EPDObject.getAvoidMoves();
        EngineSpecifications.PRINT_PV = false;
        int move = Engine.searchFixedTime(EPDObject.getBoard(), timeLimit);

        System.out.println("my move: " + MoveParser.toString(move));
        
        Assert.assertTrue(contains(winningMoves, move) && !contains(losingMoves, move));
    }


    private static final String positions = "" +
            "8/7p/2k1Pp2/pp1p2p1/3P2P1/4P3/P3K2P/8 w - - bm e4;             id \"PET001: Pawn endgame\";\n" +
            "8/1p4kP/5pP1/3p4/8/4P3/7K/8 w - - bm e4;                       id \"PET002: Pawn endgame\";\n" +
            "8/8/8/pPk3pp/2P1p3/1pK3P1/5PP1/8 w - - bm g4;                  id \"PET003: Pawn endgame\";\n" +
            "6k1/3p4/3p4/3P4/4P1p1/6P1/8/K7 w - - bm Kb1;                   id \"PET004: Pawn endgame\";\n" +
            "8/p7/1p2k1p1/8/1p2p1Pp/3pP2P/PP6/5K2 w - - bm b3;              id \"PET005: Pawn endgame\";\n" +
            "8/p7/1p2k1p1/8/1p2p1Pp/3pP2P/PP6/5K2 b - - bm b3;              id \"PET006: Pawn endgame\";\n" +
            "8/7p/p3pk2/1p5P/3K1P2/1P6/P7/8 w - - bm a4;                    id \"PET007: Pawn endgame\";\n" +
            "8/8/1p3p2/p3k1pp/4P2P/P3K1P1/1P6/8 w - - bm g4;                id \"PET008: Pawn endgame\";\n" +
            "8/8/p3k1p1/1pp5/4KP2/1P6/P5P1/8 w - - bm g4;                   id \"PET009: Pawn endgame\";\n" +
            "8/1p3p2/p4kpp/1PPp4/P7/7P/5K1P/8 b - - bm Ke6 Ke7;             id \"PET010: Pawn endgame\";\n" +
            "8/6p1/7p/p1N5/6PP/2k5/8/3K4 b - - bm a4;                       id \"PET011: pawns vs N\";\n" +
            "8/8/8/5Bp1/7k/8/4pPKP/8 w - - bm Bg4;                          id \"PET012: pawns vs B\";\n" +
            "8/8/2p5/pkp3R1/7B/P7/2p3K1/8 w - - bm a4+;                     id \"PET013: pawns vs R+B\";\n" +
            "8/6k1/6p1/2n2PP1/3N3p/3pK3/8/8 b - - bm d2;                    id \"PET014: Knight endgame\";\n" +
            "8/1N6/1p1P1n2/p1p1p3/P1P5/1P2k3/2K5/8 w - - bm Nxc5;           id \"PET015: Knight endgame\";\n" +
            "4k3/B1p4p/8/p2n1p2/5P2/5P2/1P2K2P/8 w - - am Be3;              id \"PET016: bishop vs knight\";\n" +
            "8/8/p2k1p2/1p1p3p/1P1P3p/P3NPP1/5K2/1b6 w - - bm Ng2;          id \"PET017: bishop vs knight\";\n" +
            "8/3k4/3b4/1K6/7p/5B1P/PP6/8 w - - bm b4;                       id \"PET018: opposite bishops\";\n" +
            "8/8/4kpp1/3p1b2/p6P/2B5/6P1/6K1 b - - bm Bh3;                  id \"PET019: opposite bishops\";\n" +
            "8/4k1p1/1p2B2p/4p3/8/4P2P/1PP1KbP1/8 b - - am Bxe3;            id \"PET020: opposite bishops\";\n" +
            "5k2/8/4b3/1B5P/8/8/5pPP/5K2 b - - bm Bh3;                      id \"PET021: bishop endgame\";\n" +
            "8/2B5/1p1p4/1PkP1p2/P4P2/5P2/1p6/bK6 w - - bm Bd8;             id \"PET022: bishop endgame\";\n" +
            "8/2p1bpp1/2Pp4/p2P1P1p/2K2B1P/k5P1/8/8 w - - bm f6;            id \"PET023: bishop endgame\";\n" +
            "8/4ppbk/p5pp/3pP3/3B4/5P1P/PP3P2/6K1 b - - am g5;              id \"PET024: bishop endgame\";\n" +
            "4K3/2k1Bp1N/6p1/5PP1/8/7p/b7/8 w - - bm Bf6;                   id \"PET025: BN vs B\";\n" +
            "8/5k2/4p3/4Pp1p/5P1P/3Rn2K/6p1/8 b - - bm g1=B;                id \"PET026: rook vs knight\";\n" +
            "5k2/8/8/3R4/6K1/8/3b2pP/8 w - - bm Rf5+;                       id \"PET027: rook vs bishop\";\n" +
            "8/4kp2/4p1p1/2p1r3/PpP5/3R4/1P1K1PP1/8 w - - bm g4;            id \"PET028: rook endgame\";\n" +
            "1r3k2/5pp1/3p2p1/8/3P4/P6P/2R2P1K/8 b - - bm Ra8;              id \"PET029: rook endgame\";\n" +
            "5k2/p1p4R/1pr5/3p1pP1/P2P1P2/2P2K2/8/8 w - - bm Kg3;           id \"PET030: rook endgame\";\n" +
            "8/1r3pkp/8/5p2/8/5PP1/1P1R3P/6K1 b - - bm f4;                  id \"PET031: rook endgame\";\n" +
            "8/5pk1/r5pp/P7/3R3P/6P1/5PK1/8 w - - bm Ra4;                   id \"PET032: rook endgame\";\n" +
            "8/4k3/2P2p2/6p1/1P4R1/4K3/7r/8 w - - bm Rd4;                   id \"PET033: rook endgame\";\n" +
            "3r4/7p/Rp4k1/5p2/4p3/2P5/PP3P1P/5K2 b - - bm Rd2;              id \"PET034: rook endgame\";\n" +
            "8/8/p2k3p/1P1p1pp1/P1r5/1R1K1PP1/7P/8 b - - bm a5;             id \"PET035: rook endgame\";\n" +
            "8/1r3pkp/R7/6Pp/8/4P3/5PK1/8 b - - bm Re7;                     id \"PET036: rook endgame\";\n" +
            "8/pR4pk/1b6/2p5/N1p5/8/PP1r2PP/6K1 b - - bm Rxb2;              id \"PET037: RN vs RB\";\n" +
            "4k3/2p1b3/4p1p1/1pp1P3/5PP1/1PBK4/r1P2R2/8 b - - bm c4+;       id \"PET038: RB vs RB\";\n" +
            "1r6/1pb1k1p1/4p2p/1p1p4/3Pp2P/1R2P1PB/1P2PK2/8 b - - bm b4;    id \"PET039: RB vs RB\";\n" +
            "8/8/5P1k/8/2K5/pr2r3/4R3/2R5 w - - bm f7;                      id \"PET042: double rook endgame\";\n" +
            "r5k1/5pp1/1P5p/3R4/2r5/P3P3/2p2PPP/2R3K1 b - - bm Rxa3;        id \"PET043: double rook endgame\";\n" +
            "1r6/Rp2rp2/1Pp2kp1/N1Pp3p/3Pp1nP/4P1P1/R4P2/6K1 w - - bm Nxb7 Rxb7; id \"PET044: RRN vs RRN\";\n" +
            "5k2/4p3/3p2Q1/3Pq1pP/5pP1/2P4K/8/8 w - - bm Qf5+;              id \"PET045: Queen endgame\";\n" +
            "8/8/1P1k2p1/8/5P1p/4Qb2/1q4PK/8 w - - bm Qe5+;                 id \"PET046: Q+B vs Q\";\n" +
            "8/p4k2/1p1p1qp1/1P1P4/P5P1/7P/4Q1K1/2b5 b - - am Qb2;          id \"PET047: Q+B vs Q\";\n" +
            "6qk/1p4pn/8/3p4/2P5/p3P1P1/P1Q3K1/1B6 w - - am Qxh7+;          id \"PET048: Q+B vs Q+N\";\n" +
            "1Q6/Nn3pbp/2N2p2/1Pqk1P1P/8/8/6P1/7K w - - am Qxb7;            id \"PET049: QNN vs QBN\";\n" +
            "7k/7p/1p4p1/n2p4/1K1p3R/2NB4/3Q1P2/q1r5 w - - bm Rxh7+;        id \"PET050: QNBR vs QNR\";\n" +
            "";

    private static final String[] splitUpPositions = positions.split("\n");

}
    
