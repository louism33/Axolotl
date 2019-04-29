package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.BitOperations;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.evaluation.EvaluatorEndgame.chebyshevDistance;
import static com.github.louism33.axolotl.evaluation.EvaluatorEndgame.manhattanDistance;

public class DistanceTest {
    
    @Test
    void ManhattanTest() {
        Assert.assertEquals(0, manhattanDistance(0, 0));
        Assert.assertEquals(0, manhattanDistance(12, 12));
        Assert.assertEquals(0, manhattanDistance(13, 13));
        Assert.assertEquals(0, manhattanDistance(60, 60));
        
        Assert.assertEquals(1, manhattanDistance(60, 61));
        Assert.assertEquals(1, manhattanDistance(50, 51));

        Assert.assertEquals(2, manhattanDistance(4, 20));

        Assert.assertEquals(7, manhattanDistance(7, 63));
        Assert.assertEquals(14, manhattanDistance(0, 63));
        Assert.assertEquals(14, manhattanDistance(7, 56));
    }
    
    @Test
    void chebyshevDistanceTest() {
        Assert.assertEquals(0, chebyshevDistance(0, 0));
        Assert.assertEquals(0, chebyshevDistance(12, 12));
        Assert.assertEquals(0, chebyshevDistance(13, 13));
        Assert.assertEquals(0, chebyshevDistance(60, 60));

        Assert.assertEquals(1, chebyshevDistance(60, 61));
        Assert.assertEquals(1, chebyshevDistance(50, 51));

        Assert.assertEquals(2, chebyshevDistance(4, 20));

        Assert.assertEquals(7, chebyshevDistance(7, 63));
        Assert.assertEquals(7, chebyshevDistance(0, 63));
        Assert.assertEquals(7, chebyshevDistance(7, 56));
    }
}
