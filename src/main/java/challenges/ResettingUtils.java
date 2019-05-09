package challenges;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;

public class ResettingUtils {

    public static void reset() {
        EngineSpecifications.PRINT_PV = false;
        EngineSpecifications.MASTER_DEBUG = false;
        EngineSpecifications.DEBUG = false;
        Engine.resetFull();
    }

}
