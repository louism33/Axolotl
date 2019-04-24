package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import org.junit.jupiter.api.Test;

public class Util {

    public static void reset() {
        EngineSpecifications.PRINT_PV = false;
        Engine.resetFull();
    }

}
