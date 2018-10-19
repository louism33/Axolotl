package tests.programtests;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.miscAdmin.DetailedPerftSearching;
import javacode.chessprogram.miscAdmin.FenParser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DetailedPerftSearchingTestSuite {

    /*
    Many thanks to:
    
    - Martin Sedlak
    http://www.talkchess.com/forum3/viewtopic.php?t=47318
    
    - JVMerlino
    http://www.talkchess.com/forum3/viewtopic.php?f=7&t=47318&sid=346f11bd9bec1af8783af3009d320d94&start=20
    
    - Peter Ellis Jones
    https://gist.github.com/peterellisjones/8c46c28141c162d1d8a0f0badbc9cff9
     */

    @Test
    void regularBoard() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, new Chessboard(), 4865609),
                4865609);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, new Chessboard(), 119060324),
                119060324);
    }

    @Test
    void tinyDepths() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("r6r/1b2k1bq/8/8/7B/8/8/R3K2R b QK - 3 2"), 8),
                8);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("8/8/8/2k5/2pP4/8/B7/4K3 b - d3 5 3"), 8),
                8);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("r1bqkbnr/pppppppp/n7/8/8/P7/1PPPPPPP/RNBQKBNR w QqKk - 2 2"), 19),
                19);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("r3k2r/p1pp1pb1/bn2Qnp1/2qPN3/1p2P3/2N5/PPPBBPPP/R3K2R b QqKk - 3 2"), 5),
                5);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("2kr3r/p1ppqpb1/bn2Qnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b QK - 3 2"), 44),
                44);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("rnb2k1r/pp1Pbppp/2p5/q7/2B5/8/PPPQNnPP/RNB1K2R w QK - 3 9"), 39),
                39);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(1, FenParser.makeBoardBasedOnFEN("2r5/3pk3/8/2P5/8/2K5/8/8 w - - 5 4"), 9),
                9);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(3, FenParser.makeBoardBasedOnFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"), 62379),
                62379);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(3, FenParser.makeBoardBasedOnFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10"), 89890),
                89890);
        
    }


    @Test
    void test1() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1"), 1134888),
                1134888);
    }

    @Test
    void test2() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1"), 1015133),
                1015133);
    }

    @Test
    void test3() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"), 1440467),
                1440467);
    }

    @Test
    void test4() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("5k2/8/8/8/8/8/8/4K2R w K - 0 1"), 661072),
                661072);

    }

    @Test
    void test5() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1"), 803711),
                803711);

    }


    @Test
    void test6() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1"), 1274206),
                1274206);
    }


    @Test
    void test7() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1"), 1720476),
                1720476);

    }

    @Test
    void test8() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1"), 3821001),
                3821001);
    }

    @Test
    void test9() {

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, FenParser.makeBoardBasedOnFEN("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1"), 1004658),
                1004658);
    }

    @Test
    void test10() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("4k3/1P6/8/8/8/8/K7/8 w - - 0 1"), 217342),
                217342);
    }

    @Test
    void test11() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/P1k5/K7/8/8/8/8/8 w - - 0 1"), 92683),
                92683);

    }

    @Test
    void test12() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("K1k5/8/P7/8/8/8/8/8 w - - 0 1"), 2217),
                2217);
    }

    @Test
    void test13() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(7, FenParser.makeBoardBasedOnFEN("8/k1P5/8/1K6/8/8/8/8 w - - 0 1"), 567584),
                567584);
    }


    @Test
    void test14() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1"), 23527),
                23527);

    }


    @Test
    void test15() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, FenParser.makeBoardBasedOnFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -"), 193690690),
                193690690);

    }

    @Test
    void AvoidIllegalEPCapture() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/5bk1/8/2Pp4/8/1K6/8/8 w - d6 0 1"), 824064),
                824064);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1"), 824064),
                824064);
    }


    @Test
    void EPCaptureChecksOpponent() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1"), 1440467),
                1440467);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/5k2/8/2Pp4/2B5/1K6/8/8 w - d6 0 1"), 1440467),
                1440467);
    }


    @Test
    void shortCastlingGivesCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("5k2/8/8/8/8/8/8/4K2R w K - 0 1"), 661072),
                661072);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("4k2r/8/8/8/8/8/8/5K2 b k - 0 1"), 661072),
                661072);
    }

    @Test
    void longCastlingGivesCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1"), 803711),
                803711);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("r3k3/8/8/8/8/8/8/3K4 b q - 0 1"), 803711),
                803711);
    }


    @Test
    void CastlingCRAndDoublePins() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1"), 1274206),
                1274206);

        System.out.println("-----------------------------");


        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("r3k2r/7b/8/8/8/8/1B4BQ/R3K2R b KQkq - 0 1"), 1274206),
                1274206);
    }


    @Test
    void CastlingPrevented() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1"), 1720476),
                1720476);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("r3k2r/8/5Q2/8/8/3q4/8/R3K2R w KQkq - 0 1"), 1720476),
                1720476);
    }

    @Test
    void PromoteOutOfCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1"), 3821001),
                3821001);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("3K4/8/8/8/8/8/4p3/2k2R2 b - - 0 1"), 3821001),
                3821001);
    }

    @Test
    void discoveredCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, FenParser.makeBoardBasedOnFEN("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1"), 1004658),
                1004658);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, FenParser.makeBoardBasedOnFEN("5K2/8/1Q6/2N5/8/1p2k3/8/8 w - - 0 1"), 1004658),
                1004658);
    }

    @Test
    void promoteToCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("4k3/1P6/8/8/8/8/K7/8 w - - 0 1"), 217342),
                217342);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/k7/8/8/8/8/1p6/4K3 b - - 0 1"), 217342),
                217342);
    }

    @Test
    void underPromoteToCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/P1k5/K7/8/8/8/8/8 w - - 0 1"), 92683),
                92683);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/8/8/8/8/k7/p1K5/8 b - - 0 1"), 92683),
                92683);
    }

    @Test
    void selfStaleMate() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("K1k5/8/P7/8/8/8/8/8 w - - 0 1"), 2217),
                2217);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/8/8/8/8/p7/8/k1K5 b - - 0 1"), 2217),
                2217);
    }

    @Test
    void checkStalemate() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(7, FenParser.makeBoardBasedOnFEN("8/k1P5/8/1K6/8/8/8/8 w - - 0 1"), 567584),
                567584);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(7, FenParser.makeBoardBasedOnFEN("8/8/8/8/1k6/8/K1p5/8 b - - 0 1"), 567584),
                567584);
    }


    @Test
    void doubleCheck() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1"), 23527),
                23527);

        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("8/5k2/8/5N2/5Q2/2K5/8/8 w - - 0 1"), 23527),
                23527);
    }

    @Test
    void misc() {

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(4, FenParser.makeBoardBasedOnFEN("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"), 67197),
                67197);


        System.out.println("-----------------------------");

        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"), 38633283),
                38633283);
    }
    
    
    @Test
    void bigDepth1() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("r3k2r/8/8/8/3pPp2/8/8/R3K1RR b KQkq e3 0 1"), 485647607),
                485647607);

    }

    @Test
    void bigDepth2() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1"), 706045033),
                706045033);
    }

    @Test
    void bigDepth3() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("8/7p/p5pb/4k3/P1pPn3/8/P5PP/1rB2RK1 b - d3 0 28"), 38633283),
                38633283);
    }

    @Test
    void bigDepth4() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(7, FenParser.makeBoardBasedOnFEN("8/3K4/2p5/p2b2r1/5k2/8/8/1q6 b - - 1 67"), 493407574),
                493407574);
    }
    @Test
    void bigDepth5() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("rnbqkb1r/ppppp1pp/7n/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3"), 244063299),
                244063299);
    }

    @Test
    void bigDepth6() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, FenParser.makeBoardBasedOnFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -"), 193690690),
                193690690);
    }



    @Test
    void bigDepth7() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(8, FenParser.makeBoardBasedOnFEN("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -"), 8103790),
                8103790);
    }

    @Test
    void bigDepth8() {
        Assert.assertEquals(
                    DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - -"), 71179139),
                    71179139);
    }


    @Test
    void bigDepth9() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -"), 77054993),
                77054993);
    }

    @Test
    void bigDepth10() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(7, FenParser.makeBoardBasedOnFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -"), 178633661),
                178633661);


    }


    @Test
    void bigDepth11() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(8, FenParser.makeBoardBasedOnFEN("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -"), 64451405),
                64451405);
    }

    @Test
    void bigDepth12() {
        Assert.assertEquals(
                DetailedPerftSearching.runPerftTestWithBoard(5, FenParser.makeBoardBasedOnFEN("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R w KQkq -"), 29179893),
                29179893);
    }
    
    
}