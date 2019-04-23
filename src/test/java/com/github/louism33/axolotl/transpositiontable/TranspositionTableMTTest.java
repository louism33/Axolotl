package com.github.louism33.axolotl.transpositiontable;

import com.github.louism33.axolotl.search.Engine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class TranspositionTableMTTest {

    @BeforeAll
    static void setup() {
        Engine.resetFull();

    }

    @AfterAll
    static void reset() {
        Engine.resetFull();

    }

}
