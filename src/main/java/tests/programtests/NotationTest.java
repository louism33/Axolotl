package tests.programtests;

import javacode.chessprogram.miscAdmin.FenParser;
import org.junit.jupiter.api.Test;

import static javacode.chessprogram.miscAdmin.DetailedPerftSearching.runPerftTestWithBoard;

public class NotationTest {

    @Test
    void test1() {
        runPerftTestWithBoard(6, FenParser.makeBoardBasedOnFEN("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1"), 1134888);
    }
    
    
}
