package com.github.louism33.axolotl.util;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;

public class Util {
    
    public static void reset() {
        EngineSpecifications.DEBUG = false;
        EngineBetter.resetFull();
        EngineBetter.uciEntry = null;
    }
}
