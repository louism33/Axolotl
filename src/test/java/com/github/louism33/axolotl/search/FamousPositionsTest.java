package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.ExtendedPositionDescriptionParser;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_PV;

public class FamousPositionsTest {

    Engine engine = new Engine();


    @BeforeAll
    public static void setup() {
        Util.reset();
        PRINT_PV = true;
    }

    @AfterAll
    public static void after() {
        PRINT_PV = false;
    }


    // todo, very tough!
    @Test
    void behtingTest() {
        String pos = "8/8/7p/3KNN1k/2p4p/8/3P2p1/8 w - - ; bm Kc6";
        ExtendedPositionDescriptionParser.EPDObject EPDObject =
                ExtendedPositionDescriptionParser.parseEDPPosition(pos);
//        System.out.println(EPDObject.getBoard());
//        Engine.setThreads(4);
        engine.receiveSearchSpecs(EPDObject.getBoard(), true, 1_000);
        final int move = engine.simpleSearch();
        MoveParser.printMove(move);
//        Assert.assertEquals(MoveParser.toString(move), "d5c6");
    }   
    
    @Test
    void retiTest() {
        System.out.println("testing reti position to see if engine finds the draw");
        String pos = "7K/8/k1P5/7p/8/8/8/8 w - -";
        ExtendedPositionDescriptionParser.EPDObject EPDObject =
                ExtendedPositionDescriptionParser.parseEDPPosition(pos);
//        System.out.println(EPDObject.getBoard());
        
        engine.receiveSearchSpecs(EPDObject.getBoard(), true, 1_000);
        final int move = engine.simpleSearch();
        MoveParser.printMove(move);
        Assert.assertEquals(MoveParser.toString(move), "h8g7");
        Assert.assertEquals(Engine.aiMoveScore, 0);
    }
}
