package tests.enginetests;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.miscAdmin.ExtendedPositionDescriptionParser;
import javacode.graphicsandui.Art;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EET {
    /*
    Arasan = 60 sec.	88/100
     */

    private static final int timeLimit = 60000;

    @Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        List<Object[]> answers = new ArrayList<>();
        for (String splitUpWAC : splitUpWACs) {
            Object[] objectAndName = new Object[2];
            ExtendedPositionDescriptionParser.EDPObject edpObject = ExtendedPositionDescriptionParser.parseEDPPosition(splitUpWAC);
            objectAndName[0] = edpObject;
            objectAndName[1] = edpObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }


    private static ExtendedPositionDescriptionParser.EDPObject edpObject;

    public EET(Object edp, Object name) {
        edpObject = (ExtendedPositionDescriptionParser.EDPObject) edp;
    }

    @Test
    public void test() {

        System.out.println(Art.boardArt(edpObject.getBoard()));
        Move move = Engine.search(edpObject.getBoard(), timeLimit);
        System.out.println(move);

        int winningMoveDestination = edpObject.getBestMoveDestinationIndex();
        int myMoveDestination = move.destinationIndex;

        assertEquals(winningMoveDestination, myMoveDestination);
    }



    private static final String wacTests = "" +
            "8/8/p2p3p/3k2p1/PP6/3K1P1P/8/8 b - - bm Kc6; id \"E_E_T 001 - B vs B\";\n" +
            "8/p5pp/3k1p2/3p4/1P3P2/P1K5/5P1P/8 b - - bm g5; id \"E_E_T 002 - B vs B\";\n" +
            "8/1p3p2/p7/8/2k5/4P1pP/2P1K1P1/8 w - - bm h4; id \"E_E_T 003 - B vs B\";\n" +
            "8/pp5p/3k2p1/8/4Kp2/2P1P2P/P7/8 w - - bm exf4; id \"E_E_T 004 - B vs B\";\n" +
            "8/7p/1p3pp1/p2K4/Pk3PPP/8/1P6/8 b - - bm Kb3 f5; id \"E_E_T 005 - B vs B\";\n" +
            "2k5/3b4/PP3K2/7p/4P3/1P6/8/8 w - - bm Ke7; id \"E_E_T 006 - B vs L\";\n" +
            "8/3Pb1p1/8/3P2P1/5P2/7k/4K3/8 w - - bm Kd3; id \"E_E_T 007 - B vs L\";\n" +
            "8/1Pk2Kpp/8/8/4nPP1/7P/8/8 b - - bm Nf2; id \"E_E_T 008 - B vs S\";\n" +
            "2n5/4k1p1/P6p/3PK3/7P/8/6P1/8 b - - bm g6; id \"E_E_T 009 - B vs S\";\n" +
//            "4k3/8/3PP1p1/8/3K3p/8/3n2PP/8 b - - am Nf1; id \"E_E_T 010 - B vs S\";\n" +
//            "6k1/5p2/P3p1p1/2Qp4/5q2/2K5/8/8 b - - am Qc1+ Qe5+; id \"E_E_T 011 - D vs D\";\n" +
            "8/6pk/8/2p2P1p/6qP/5QP1/8/6K1 w - - bm Qd3 Qf2; id \"E_E_T 012 - D vs D\";\n" +
            "5q1k/5P1p/Q7/5n1p/6P1/7K/8/8 w - - bm Qa1+; id \"E_E_T 013 - D vs D&S\";\n" +
            "4qr2/4p2k/1p2P1pP/5P2/1P3P2/6Q1/8/3B1K2 w - - bm Ba4; id \"E_E_T 014 - D&L vs D&T\";\n" +
            "8/kn4b1/P2B4/8/1Q6/6pP/1q4pP/5BK1 w - - bm Bc5+; id \"E_E_T 015 - D&L&L vs D&L&S\";\n" +
            "6k1/1p2p1bp/p5p1/4pb2/1q6/4Q3/1P2BPPP/2R3K1 w - - bm Qa3; id \"E_E_T 017 - D&T&L vs D&L&L\";\n" +
            "1rr2k2/p1q5/3p2Q1/3Pp2p/8/1P3P2/1KPRN3/8 w - e6 bm Rd1; id \"E_E_T 018 - D&T&S vs D&T&T\";\n" +
//            "r5k1/3R2p1/p1r1q2p/P4p2/5P2/2p1P3/5P1P/1R1Q2K1 w - - am Rbb7; id \"E_E_T 019 - D&T&T vs D&T&T\";\n" +
//            "8/1p4k1/pK5p/2B5/P4pp1/8/7P/8 b - - am g3; id \"E_E_T 020 - L vs B\";\n" +
            "8/6p1/6P1/6Pp/B1p1p2K/6PP/3k2P1/8 w - - bm Bd1; id \"E_E_T 021 - L vs B\";\n" +
            "8/4k3/8/2Kp3p/B3bp1P/P7/1P6/8 b - - bm Bg2; id \"E_E_T 022 - L vs L\";\n" +
//            "8/8/2p1K1p1/2k5/p7/P4BpP/1Pb3P1/8 w - - am Kd7; id \"E_E_T 023 - L vs L\";\n" +
            "8/3p3B/5p2/5P2/p7/PP5b/k7/6K1 w - - bm b4; id \"E_E_T 024 - L vs L\";\n" +
//            "8/p4p2/1p2k2p/6p1/P4b1P/1P6/3B1PP1/6K1 w - - am Bxf4; id \"E_E_T 025 - L vs L\";\n" +
//            "3b3k/1p4p1/p5p1/4B3/8/7P/1P3PP1/5K2 b - - am Bf6; id \"E_E_T 027 - L vs L\";\n" +
            "4b1k1/1p3p2/4pPp1/p2pP1P1/P2P4/1P1B4/8/2K5 w - - bm b4; id \"E_E_T 028 - L vs L\";\n" +
//            "8/3k1p2/n3pP2/1p2P2p/5K2/1PB5/7P/8 b - - am Kc6 b4; id \"E_E_T 029 - L vs S\";\n" +
            "8/8/4p1p1/1P1kP3/4n1PK/2B4P/8/8 b - - bm Kc5; id \"E_E_T 030 - L vs S\";\n" +
            "8/5k2/4p3/B2p2P1/3K2n1/1P6/8/8 b - - bm Kg6; id \"E_E_T 031 - L vs S\";\n" +
            "5b2/p4B2/5B2/1bN5/8/P3r3/4k1K1/8 w - - bm Bh5+; id \"E_E_T 032 - L&L&S vs T&L&L\";\n" +
            "8/p5pq/8/p2N3p/k2P3P/8/KP3PB1/8 w - - bm Be4; id \"E_E_T 033 - L&S vs D\";\n" +
            "1b6/1P6/8/2KB4/6pk/3N3p/8/8 b - - bm Kg3; id \"E_E_T 034 - L&S vs L&B\";\n" +
            "8/p7/7k/1P1K3P/8/1n6/4Bp2/5Nb1 b - - bm Na5; id \"E_E_T 035 - L&S vs L&S\";\n" +
            "8/8/8/3K4/2N5/p2B4/p7/k4r2 w - - bm Kc5; id \"E_E_T 036 - L&S vs T&B\";\n" +
            "8/8/2kp4/5Bp1/8/5K2/3N4/2rN4 w - - bm Nb3; id \"E_E_T 037 - L&S&S vs T&B\";\n" +
            "k2K4/1p4pN/P7/1p3P2/pP6/8/8/8 w - - bm f6 Kc7; id \"E_E_T 038 - S vs B\";\n" +
            "k2N4/1qpK1p2/1p6/1P4p1/1P4P1/8/8/8 w - - bm Nc6; id \"E_E_T 039 - S vs D\";\n" +
            "6k1/4b3/4p1p1/8/6pP/4PN2/5K2/8 w - - bm Ne5 Nh2; id \"E_E_T 040 - S vs L\";\n" +
            "8/8/6Np/2p3kP/1pPbP3/1P3K2/8/8 w - - bm e5; id \"E_E_T 041 - S vs L\";\n" +
            "8/3P4/1p3b1p/p7/P7/1P3NPP/4p1K1/3k4 w - - bm g4; id \"E_E_T 042 - S vs L\";\n" +
            "8/8/1p2p3/p3p2b/P1PpP2P/kP6/2K5/7N w - - bm Nf2; id \"E_E_T 043 - S vs L\";\n" +
            "4N3/8/3P3p/1k2P3/7p/1n1K4/8/8 w - - bm d7; id \"E_E_T 044 - S vs S\";\n" +
            "N5n1/2p1kp2/2P3p1/p4PP1/K4P2/8/8/8 w - - bm f6 Kb5; id \"E_E_T 045 - S vs S\";\n" +
            "8/8/2pn4/p4p1p/P4N2/1Pk2KPP/8/8 w - - bm Ne2 Ne6; id \"E_E_T 046 - S vs S\";\n" +
            "8/7k/2P5/2p4p/P3N2K/8/8/5r2 w - - bm Ng5+; id \"E_E_T 047 - S vs T\";\n" +
            "2k1r3/p7/K7/1P6/P2N4/8/P7/8 w - - bm Nc6; id \"E_E_T 048 - S vs T\";\n" +
            "1k6/8/8/1K6/5pp1/8/4Pp1p/R7 w - - bm Kb6; id \"E_E_T 049 - T vs B\";\n" +
            "6k1/8/8/1K4p1/3p2P1/2pp4/8/1R6 w - - bm Kc6; id \"E_E_T 050 - T vs B\";\n" +
            "8/5p2/3pp2p/p5p1/4Pk2/2p2P1P/P1Kb2P1/1R6 w - - bm a4 Rb5; id \"E_E_T 051 - T vs L\";\n" +
            "8/8/4pR2/3pP2p/6P1/2p4k/P1Kb4/8 b - - bm hxg4; id \"E_E_T 052 - T vs L\";\n" +
            "3k3K/p5P1/P3r3/8/1N6/8/8/8 w - - bm Kh7; id \"E_E_T 053 - T vs S\";\n" +
//            "8/8/5p2/5k2/p4r2/PpKR4/1P5P/8 w - - am Rd4; id \"E_E_T 054 - T vs T\";\n" +
            "5k2/7R/8/4Kp2/5Pp1/P5rp/1P6/8 w - - bm Kf6; id \"E_E_T 055 - T vs T\";\n" +
            "2K5/p7/7P/5pR1/8/5k2/r7/8 w - - bm Rxf5+; id \"E_E_T 056 - T vs T\";\n" +
            "8/2R4p/4k3/1p2P3/pP3PK1/r7/8/8 b - - bm h5 Ra1; id \"E_E_T 057 - T vs T\";\n" +
            "2k1r3/5R2/3KP3/8/1pP3p1/1P5p/8/8 w - - bm Rc7+; id \"E_E_T 058 - T vs T\";\n" +
//            "8/6p1/1p5p/1R2k3/4p3/1P2P3/1K4PP/3r4 b - - am Rd5; id \"E_E_T 059 - T vs T\";\n" +
            "5K2/kp3P2/2p5/2Pp4/3P4/r7/p7/6R1 w - - bm Ke7; id \"E_E_T 060 - T vs T\";\n" +
            "8/pp3K2/2P4k/5p2/8/6P1/R7/6r1 w - - bm Kf6; id \"E_E_T 061 - T vs T\";\n" +
            "2r3k1/6pp/3pp1P1/1pP5/1P6/P4R2/5K2/8 w - - bm c6; id \"E_E_T 062 - T vs T\";\n" +
            "r2k4/8/8/1P4p1/8/p5P1/6P1/1R3K2 w - - bm b6; id \"E_E_T 063 - T vs T\";\n" +
            "8/4k3/1p4p1/p7/P1r1P3/1R4Pp/5P1P/4K3 w - - bm Ke2; id \"E_E_T 064 - T vs T\";\n" +
            "R7/4kp2/P3p1p1/3pP1P1/3P1P2/p6r/3K4/8 w - - bm Kc2; id \"E_E_T 065 - T vs T\";\n" +
//            "8/1pp1r3/p1p2k2/6p1/P5P1/1P3P2/2P1RK2/8 b - - am Rxe2+ Re5; id \"E_E_T 066 - T vs T\";\n" +
            "8/1p2R3/8/p5P1/3k4/P2p2K1/1P6/5r2 w - - bm Kg2; id \"E_E_T 067 - T vs T\";\n" +
            "R7/P5Kp/2P5/k3r2P/8/5p2/p4P2/8 w - - bm Rb8; id \"E_E_T 068 - T vs T\";\n" +
            "8/2p4K/4k1p1/p1p1P3/PpP2P2/5R1P/8/6r1 b - - bm Kf7; id \"E_E_T 069 - T vs T\";\n" +
            "8/B7/1R6/q3k2p/8/6p1/5P2/5K2 w - - bm Rb3; id \"E_E_T 071 - T&L vs D\";\n" +
            "5k2/8/2Pb1B2/8/6RK/7p/5p1P/8 w - - bm Be5; id \"E_E_T 072 - T&L vs L&B\";\n" +
            "3kB3/R4P2/8/8/1p6/pp6/5r2/1K6 w - - bm f8=Q f8=R; id \"E_E_T 073 - T&L vs T&B\";\n" +
            "5k2/1p6/1P1p4/1K1p2p1/PB1P2P1/3pR2p/1P2p1pr/8 w - - bm Ba5; id \"E_E_T 074 - T&L vs T&B\";\n" +
            "6k1/p6p/1p1p2p1/2bP4/P1P5/2B3P1/4r2P/1R5K w - - bm a5; id \"E_E_T 075 - T&L vs T&L\";\n" +
            "3R3B/8/1r4b1/8/4pP2/7k/8/7K w - - bm Bd4; id \"E_E_T 076 - T&L vs T&L\";\n" +
            "rk1b4/p2p2p1/1P6/2R2P2/8/2K5/8/5B2 w - - bm Rc8+; id \"E_E_T 077 - T&L vs T&L\";\n" +
            "3r1k2/8/7R/8/8/pp1B4/P7/n1K5 w - - bm Rf6+; id \"E_E_T 078 - T&L vs T&S\";\n" +
            "r5k1/5ppp/1p6/2b1R3/1p3P2/2nP2P1/1B3PKP/5B2 w - - bm d4; id \"E_E_T 079 - T&L&L vs T&L&S\";\n" +
            "5k2/3p1b2/4pN2/3PPpp1/6R1/6PK/1B1q1P2/8 w - - bm Ba3+; id \"E_E_T 080 - T&L&S vs D&L\";\n" +
            "8/1p5p/6p1/1p4Pp/1PpR4/2P1K1kB/6Np/7b w - - bm Rd1; id \"E_E_T 081 - T&L&S vs L&B\";\n" +
            "7k/1p1Nr2P/3Rb3/8/3K4/6b1/8/5B2 w - - bm Ne5; id \"E_E_T 082 - T&L&S vs T&L&L\";\n" +
            "8/1B4k1/5pn1/6N1/1P3rb1/P1K4p/3R4/8 w - - bm Nxh3; id \"E_E_T 083 - T&L&S vs T&L&S\";\n" +
            "8/7p/6p1/3Np1bk/4Pp2/1R3PPK/5r1P/8 w - - bm Nc7; id \"E_E_T 084 - T&S vs T&L\";\n" +
            "1r6/3b1p2/2k4P/1N3p1P/5P2/8/3R4/2K5 w - - bm Na7+; id \"E_E_T 085 - T&S vs T&L\";\n" +
            "k6r/8/1R6/8/1pK2p2/8/7N/3b4 w - - bm Nf1; id \"E_E_T 086 - T&S vs T&L\";\n" +
            "8/8/8/p1p5/2P1k3/1Pn5/1N1R2K1/1r6 w - - bm Kg3; id \"E_E_T 087 - T&S vs T&S\";\n" +
            "5n1k/1r3P1p/p2p3P/P7/8/1N6/5R2/4K3 b - - bm Re7+; id \"E_E_T 088 - T&S vs T&S\";\n" +
            "6R1/P2k1N2/r7/7P/r7/p7/7K/8 w - - bm Nh6; id \"E_E_T 089 - T&S vs T&T\";\n" +
            "8/1rk1P3/7p/P7/1N2r3/5RKb/8/8 w - - bm Na6+; id \"E_E_T 090 - T&S&B vs T&T&L\";\n" +
            "2K5/k3q3/6pR/6p1/6Pp/7P/8/3R4 w - - bm Rh7; id \"E_E_T 090 - T&T vs D\";\n" +
            "R5bk/5r2/P7/1P1pR3/3P4/7p/5p1K/8 w - - bm Rh5+; id \"E_E_T 092 - T&T vs T&L\";\n" +
            "4k3/7r/3nb3/2R5/8/6n1/1R3K2/8 w - - bm Re5; id \"E_E_T 093 - T&T vs T&L&S\";\n" +
            "1r6/1r6/1P1KP3/6k1/1R4p1/7p/7R/8 w - - bm Kc6 Rb5; id \"E_E_T 094 - T&T vs T&T\";\n" +
            "1k1K4/1p6/P4P2/2R5/4p2R/r2p4/8/3r4 w - - bm Rf4; id \"E_E_T 095 - T&T vs T&T\";\n" +
            "5k2/R1p5/p1R3Pb/2K5/2B5/2q2b2/8/8 w - - bm g7+; id \"E_E_T 096 - T&T&L vs D&L&L\";\n" +
            "8/8/k7/n7/p1R5/p7/4r1p1/KB3R2 w - - bm Rc3; id \"E_E_T 097 - T&T&L vs T&S&B\";\n" +
            "3r2k1/p1R2ppp/1p6/P1b1PP2/3p4/3R2B1/5PKP/1r6 w - - bm f6; id \"E_E_T 098 - T&T&L vs T&T&L\";\n" +
            "8/5p2/5rp1/p2k1r1p/P1pNp2P/RP1bP1P1/5P1R/4K3 b - - bm c3; id \"E_E_T 099 - T&T&S vs T&T&L\";\n" +
            "1r4k1/6pp/3Np1b1/p1R1P3/6P1/P2pr2P/1P1R2K1/8 b - - bm Rf8; id \"E_E_T 100 - T&T&S vs T&T&L\";\n" +
//            "4k1r1/pp2p2p/3pN1n1/2pP4/2P3P1/PP5P/8/5RK1 b - -  am Nf8; id \"E_E_T 16b -  T&S vs T&S\";\n" +
//            "8/3k3p/1p2p3/p4p2/Pb1Pp3/2B3PP/1P3P2/5K2 w - -  am Bxb4; id \"E_E_T 26b -  L vs L\";\n" +
            "8/1k6/8/Q7/7p/6p1/6pr/6Kb w - - bm Qc5; id \"E_E_T 70b - D vs T&L&B\";" +
            "";

    private static final String[] splitUpWACs = wacTests.split("\\\n");
    static int totalWACS = splitUpWACs.length;

}
    
    

    /*
8/8/p2p3p/3k2p1/PP6/3K1P1P/8/8 b - - bm Kc6; id "E_E_T 001 - B vs B";
8/p5pp/3k1p2/3p4/1P3P2/P1K5/5P1P/8 b - - bm g5; id "E_E_T 002 - B vs B";
8/1p3p2/p7/8/2k5/4P1pP/2P1K1P1/8 w - - bm h4; id "E_E_T 003 - B vs B";
8/pp5p/3k2p1/8/4Kp2/2P1P2P/P7/8 w - - bm exf4; id "E_E_T 004 - B vs B";
8/7p/1p3pp1/p2K4/Pk3PPP/8/1P6/8 b - - bm Kb3 f5; id "E_E_T 005 - B vs B";
2k5/3b4/PP3K2/7p/4P3/1P6/8/8 w - - bm Ke7; id "E_E_T 006 - B vs L";
8/3Pb1p1/8/3P2P1/5P2/7k/4K3/8 w - - bm Kd3; id "E_E_T 007 - B vs L";
8/1Pk2Kpp/8/8/4nPP1/7P/8/8 b - - bm Nf2; id "E_E_T 008 - B vs S";
2n5/4k1p1/P6p/3PK3/7P/8/6P1/8 b - - bm g6; id "E_E_T 009 - B vs S";
4k3/8/3PP1p1/8/3K3p/8/3n2PP/8 b - - am Nf1; id "E_E_T 010 - B vs S";
6k1/5p2/P3p1p1/2Qp4/5q2/2K5/8/8 b - - am Qc1+ Qe5+; id "E_E_T 011 - D vs D";
8/6pk/8/2p2P1p/6qP/5QP1/8/6K1 w - - bm Qd3 Qf2; id "E_E_T 012 - D vs D";
5q1k/5P1p/Q7/5n1p/6P1/7K/8/8 w - - bm Qa1+; id "E_E_T 013 - D vs D&S";
4qr2/4p2k/1p2P1pP/5P2/1P3P2/6Q1/8/3B1K2 w - - bm Ba4; id "E_E_T 014 - D&L vs D&T";
8/kn4b1/P2B4/8/1Q6/6pP/1q4pP/5BK1 w - - bm Bc5+; id "E_E_T 015 - D&L&L vs D&L&S";
6k1/1p2p1bp/p5p1/4pb2/1q6/4Q3/1P2BPPP/2R3K1 w - - bm Qa3; id "E_E_T 017 - D&T&L vs D&L&L";
1rr2k2/p1q5/3p2Q1/3Pp2p/8/1P3P2/1KPRN3/8 w - e6 bm Rd1; id "E_E_T 018 - D&T&S vs D&T&T";
r5k1/3R2p1/p1r1q2p/P4p2/5P2/2p1P3/5P1P/1R1Q2K1 w - - am Rbb7; id "E_E_T 019 - D&T&T vs D&T&T";
8/1p4k1/pK5p/2B5/P4pp1/8/7P/8 b - - am g3; id "E_E_T 020 - L vs B";
8/6p1/6P1/6Pp/B1p1p2K/6PP/3k2P1/8 w - - bm Bd1; id "E_E_T 021 - L vs B";
8/4k3/8/2Kp3p/B3bp1P/P7/1P6/8 b - - bm Bg2; id "E_E_T 022 - L vs L";
8/8/2p1K1p1/2k5/p7/P4BpP/1Pb3P1/8 w - - am Kd7; id "E_E_T 023 - L vs L";
8/3p3B/5p2/5P2/p7/PP5b/k7/6K1 w - - bm b4; id "E_E_T 024 - L vs L";
8/p4p2/1p2k2p/6p1/P4b1P/1P6/3B1PP1/6K1 w - - am Bxf4; id "E_E_T 025 - L vs L";
3b3k/1p4p1/p5p1/4B3/8/7P/1P3PP1/5K2 b - - am Bf6; id "E_E_T 027 - L vs L";
4b1k1/1p3p2/4pPp1/p2pP1P1/P2P4/1P1B4/8/2K5 w - - bm b4; id "E_E_T 028 - L vs L";
8/3k1p2/n3pP2/1p2P2p/5K2/1PB5/7P/8 b - - am Kc6 b4; id "E_E_T 029 - L vs S";
8/8/4p1p1/1P1kP3/4n1PK/2B4P/8/8 b - - bm Kc5; id "E_E_T 030 - L vs S";
8/5k2/4p3/B2p2P1/3K2n1/1P6/8/8 b - - bm Kg6; id "E_E_T 031 - L vs S";
5b2/p4B2/5B2/1bN5/8/P3r3/4k1K1/8 w - - bm Bh5+; id "E_E_T 032 - L&L&S vs T&L&L";
8/p5pq/8/p2N3p/k2P3P/8/KP3PB1/8 w - - bm Be4; id "E_E_T 033 - L&S vs D";
1b6/1P6/8/2KB4/6pk/3N3p/8/8 b - - bm Kg3; id "E_E_T 034 - L&S vs L&B";
8/p7/7k/1P1K3P/8/1n6/4Bp2/5Nb1 b - - bm Na5; id "E_E_T 035 - L&S vs L&S";
8/8/8/3K4/2N5/p2B4/p7/k4r2 w - - bm Kc5; id "E_E_T 036 - L&S vs T&B";
8/8/2kp4/5Bp1/8/5K2/3N4/2rN4 w - - bm Nb3; id "E_E_T 037 - L&S&S vs T&B";
k2K4/1p4pN/P7/1p3P2/pP6/8/8/8 w - - bm f6 Kc7; id "E_E_T 038 - S vs B";
k2N4/1qpK1p2/1p6/1P4p1/1P4P1/8/8/8 w - - bm Nc6; id "E_E_T 039 - S vs D";
6k1/4b3/4p1p1/8/6pP/4PN2/5K2/8 w - - bm Ne5 Nh2; id "E_E_T 040 - S vs L";
8/8/6Np/2p3kP/1pPbP3/1P3K2/8/8 w - - bm e5; id "E_E_T 041 - S vs L";
8/3P4/1p3b1p/p7/P7/1P3NPP/4p1K1/3k4 w - - bm g4; id "E_E_T 042 - S vs L";
8/8/1p2p3/p3p2b/P1PpP2P/kP6/2K5/7N w - - bm Nf2; id "E_E_T 043 - S vs L";
4N3/8/3P3p/1k2P3/7p/1n1K4/8/8 w - - bm d7; id "E_E_T 044 - S vs S";
N5n1/2p1kp2/2P3p1/p4PP1/K4P2/8/8/8 w - - bm f6 Kb5; id "E_E_T 045 - S vs S";
8/8/2pn4/p4p1p/P4N2/1Pk2KPP/8/8 w - - bm Ne2 Ne6; id "E_E_T 046 - S vs S";
8/7k/2P5/2p4p/P3N2K/8/8/5r2 w - - bm Ng5+; id "E_E_T 047 - S vs T";
2k1r3/p7/K7/1P6/P2N4/8/P7/8 w - - bm Nc6; id "E_E_T 048 - S vs T";
1k6/8/8/1K6/5pp1/8/4Pp1p/R7 w - - bm Kb6; id "E_E_T 049 - T vs B";
6k1/8/8/1K4p1/3p2P1/2pp4/8/1R6 w - - bm Kc6; id "E_E_T 050 - T vs B";
8/5p2/3pp2p/p5p1/4Pk2/2p2P1P/P1Kb2P1/1R6 w - - bm a4 Rb5; id "E_E_T 051 - T vs L";
8/8/4pR2/3pP2p/6P1/2p4k/P1Kb4/8 b - - bm hxg4; id "E_E_T 052 - T vs L";
3k3K/p5P1/P3r3/8/1N6/8/8/8 w - - bm Kh7; id "E_E_T 053 - T vs S";
8/8/5p2/5k2/p4r2/PpKR4/1P5P/8 w - - am Rd4; id "E_E_T 054 - T vs T";
5k2/7R/8/4Kp2/5Pp1/P5rp/1P6/8 w - - bm Kf6; id "E_E_T 055 - T vs T";
2K5/p7/7P/5pR1/8/5k2/r7/8 w - - bm Rxf5+; id "E_E_T 056 - T vs T";
8/2R4p/4k3/1p2P3/pP3PK1/r7/8/8 b - - bm h5 Ra1; id "E_E_T 057 - T vs T";
2k1r3/5R2/3KP3/8/1pP3p1/1P5p/8/8 w - - bm Rc7+; id "E_E_T 058 - T vs T";
8/6p1/1p5p/1R2k3/4p3/1P2P3/1K4PP/3r4 b - - am Rd5; id "E_E_T 059 - T vs T";
5K2/kp3P2/2p5/2Pp4/3P4/r7/p7/6R1 w - - bm Ke7; id "E_E_T 060 - T vs T";
8/pp3K2/2P4k/5p2/8/6P1/R7/6r1 w - - bm Kf6; id "E_E_T 061 - T vs T";
2r3k1/6pp/3pp1P1/1pP5/1P6/P4R2/5K2/8 w - - bm c6; id "E_E_T 062 - T vs T";
r2k4/8/8/1P4p1/8/p5P1/6P1/1R3K2 w - - bm b6; id "E_E_T 063 - T vs T";
8/4k3/1p4p1/p7/P1r1P3/1R4Pp/5P1P/4K3 w - - bm Ke2; id "E_E_T 064 - T vs T";
R7/4kp2/P3p1p1/3pP1P1/3P1P2/p6r/3K4/8 w - - bm Kc2; id "E_E_T 065 - T vs T";
8/1pp1r3/p1p2k2/6p1/P5P1/1P3P2/2P1RK2/8 b - - am Rxe2+ Re5; id "E_E_T 066 - T vs T";
8/1p2R3/8/p5P1/3k4/P2p2K1/1P6/5r2 w - - bm Kg2; id "E_E_T 067 - T vs T";
R7/P5Kp/2P5/k3r2P/8/5p2/p4P2/8 w - - bm Rb8; id "E_E_T 068 - T vs T";
8/2p4K/4k1p1/p1p1P3/PpP2P2/5R1P/8/6r1 b - - bm Kf7; id "E_E_T 069 - T vs T";
8/B7/1R6/q3k2p/8/6p1/5P2/5K2 w - - bm Rb3; id "E_E_T 071 - T&L vs D";
5k2/8/2Pb1B2/8/6RK/7p/5p1P/8 w - - bm Be5; id "E_E_T 072 - T&L vs L&B";
3kB3/R4P2/8/8/1p6/pp6/5r2/1K6 w - - bm f8=Q f8=R; id "E_E_T 073 - T&L vs T&B";
5k2/1p6/1P1p4/1K1p2p1/PB1P2P1/3pR2p/1P2p1pr/8 w - - bm Ba5; id "E_E_T 074 - T&L vs T&B";
6k1/p6p/1p1p2p1/2bP4/P1P5/2B3P1/4r2P/1R5K w - - bm a5; id "E_E_T 075 - T&L vs T&L";
3R3B/8/1r4b1/8/4pP2/7k/8/7K w - - bm Bd4; id "E_E_T 076 - T&L vs T&L";
rk1b4/p2p2p1/1P6/2R2P2/8/2K5/8/5B2 w - - bm Rc8+; id "E_E_T 077 - T&L vs T&L";
3r1k2/8/7R/8/8/pp1B4/P7/n1K5 w - - bm Rf6+; id "E_E_T 078 - T&L vs T&S";
r5k1/5ppp/1p6/2b1R3/1p3P2/2nP2P1/1B3PKP/5B2 w - - bm d4; id "E_E_T 079 - T&L&L vs T&L&S";
5k2/3p1b2/4pN2/3PPpp1/6R1/6PK/1B1q1P2/8 w - - bm Ba3+; id "E_E_T 080 - T&L&S vs D&L";
8/1p5p/6p1/1p4Pp/1PpR4/2P1K1kB/6Np/7b w - - bm Rd1; id "E_E_T 081 - T&L&S vs L&B";
7k/1p1Nr2P/3Rb3/8/3K4/6b1/8/5B2 w - - bm Ne5; id "E_E_T 082 - T&L&S vs T&L&L";
8/1B4k1/5pn1/6N1/1P3rb1/P1K4p/3R4/8 w - - bm Nxh3; id "E_E_T 083 - T&L&S vs T&L&S";
8/7p/6p1/3Np1bk/4Pp2/1R3PPK/5r1P/8 w - - bm Nc7; id "E_E_T 084 - T&S vs T&L";
1r6/3b1p2/2k4P/1N3p1P/5P2/8/3R4/2K5 w - - bm Na7+; id "E_E_T 085 - T&S vs T&L";
k6r/8/1R6/8/1pK2p2/8/7N/3b4 w - - bm Nf1; id "E_E_T 086 - T&S vs T&L";
8/8/8/p1p5/2P1k3/1Pn5/1N1R2K1/1r6 w - - bm Kg3; id "E_E_T 087 - T&S vs T&S";
5n1k/1r3P1p/p2p3P/P7/8/1N6/5R2/4K3 b - - bm Re7+; id "E_E_T 088 - T&S vs T&S";
6R1/P2k1N2/r7/7P/r7/p7/7K/8 w - - bm Nh6; id "E_E_T 089 - T&S vs T&T";
8/1rk1P3/7p/P7/1N2r3/5RKb/8/8 w - - bm Na6+; id "E_E_T 090 - T&S&B vs T&T&L";
2K5/k3q3/6pR/6p1/6Pp/7P/8/3R4 w - - bm Rh7; id "E_E_T 090 - T&T vs D";
R5bk/5r2/P7/1P1pR3/3P4/7p/5p1K/8 w - - bm Rh5+; id "E_E_T 092 - T&T vs T&L";
4k3/7r/3nb3/2R5/8/6n1/1R3K2/8 w - - bm Re5; id "E_E_T 093 - T&T vs T&L&S";
1r6/1r6/1P1KP3/6k1/1R4p1/7p/7R/8 w - - bm Kc6 Rb5; id "E_E_T 094 - T&T vs T&T";
1k1K4/1p6/P4P2/2R5/4p2R/r2p4/8/3r4 w - - bm Rf4; id "E_E_T 095 - T&T vs T&T";
5k2/R1p5/p1R3Pb/2K5/2B5/2q2b2/8/8 w - - bm g7+; id "E_E_T 096 - T&T&L vs D&L&L";
8/8/k7/n7/p1R5/p7/4r1p1/KB3R2 w - - bm Rc3; id "E_E_T 097 - T&T&L vs T&S&B";
3r2k1/p1R2ppp/1p6/P1b1PP2/3p4/3R2B1/5PKP/1r6 w - - bm f6; id "E_E_T 098 - T&T&L vs T&T&L";
8/5p2/5rp1/p2k1r1p/P1pNp2P/RP1bP1P1/5P1R/4K3 b - - bm c3; id "E_E_T 099 - T&T&S vs T&T&L";
1r4k1/6pp/3Np1b1/p1R1P3/6P1/P2pr2P/1P1R2K1/8 b - - bm Rf8; id "E_E_T 100 - T&T&S vs T&T&L";
4k1r1/pp2p2p/3pN1n1/2pP4/2P3P1/PP5P/8/5RK1 b - -  am Nf8; id "E_E_T 16b -  T&S vs T&S";
8/3k3p/1p2p3/p4p2/Pb1Pp3/2B3PP/1P3P2/5K2 w - -  am Bxb4; id "E_E_T 26b -  L vs L";
8/1k6/8/Q7/7p/6p1/6pr/6Kb w - - bm Qc5; id "E_E_T 70b - D vs T&L&B";
     */
