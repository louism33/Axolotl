package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.commands.EngineDebugCommand;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.EngineSpecifications.*;

public class UCICommands {

    @BeforeAll
    static void setup() {
        EngineBetter.resetFull();
        EngineBetter.uciEntry = null;
    }

    @AfterAll
    static void reset() {
        EngineBetter.resetFull();
        EngineBetter.uciEntry = null;
    }
    
    @Test
    void debugTrueTest() {
        UCIEntry uciEntry = new UCIEntry();
        DEBUG = false;

        uciEntry.receive(new EngineDebugCommand(false, true));

        Assert.assertTrue(DEBUG);
        DEBUG = false;

        uciEntry.receive(new EngineDebugCommand(false, true));

        Assert.assertTrue(DEBUG);
        DEBUG = false;

        uciEntry.receive(new EngineDebugCommand(true, false));

        Assert.assertTrue(DEBUG);
        DEBUG = false;
    }

    @Test
    void debugFalseTest() {
        UCIEntry uciEntry = new UCIEntry();
        DEBUG = true;

        uciEntry.receive(new EngineDebugCommand(false, false));

        Assert.assertFalse(DEBUG);
        DEBUG = true;

        uciEntry.receive(new EngineDebugCommand(false, false));

        Assert.assertFalse(DEBUG);
        DEBUG = false;
    }
}
