package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.commands.EngineDebugCommand;
import com.github.louism33.axolotl.util.Util;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.DEBUG;

public class UCICommands {

    @BeforeAll
    static void setup() {
        Util.reset();
    }

    @AfterAll
    static void reset() {
        Util.reset();
    }
    
    @Test
    void debugTrueTest() {
//        UCIEntryOld uciEntry = new UCIEntryOld();
//        DEBUG = false;
//
//        uciEntry.receive(new EngineDebugCommand(false, true));
//
//        Assert.assertTrue(DEBUG);
//        DEBUG = false;
//
//        uciEntry.receive(new EngineDebugCommand(false, true));
//
//        Assert.assertTrue(DEBUG);
//        DEBUG = false;
//
//        uciEntry.receive(new EngineDebugCommand(true, false));
//
//        Assert.assertTrue(DEBUG);
//        DEBUG = false;
    }

    @Test
    void debugFalseTest() {
//        UCIEntryOld uciEntry = new UCIEntryOld();
//        DEBUG = true;
//
//        uciEntry.receive(new EngineDebugCommand(false, false));
//
//        Assert.assertFalse(DEBUG);
//        DEBUG = true;
//
//        uciEntry.receive(new EngineDebugCommand(false, false));
//
//        Assert.assertFalse(DEBUG);
//        DEBUG = false;
    }
}
