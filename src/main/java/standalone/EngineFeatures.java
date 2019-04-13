package standalone;

import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.axolotl.search.EngineBetter.*;

public class EngineFeatures {

    
    @Test
    public void testIID() {
        EngineSpecifications.DEBUG = false;
        for (int depth = 8; depth < 15; depth++) {
            EngineBetter.resetFull();
            iidFail = 0;
            iidSuccess = 0;
            iidTotal = 0;
            Chessboard board = new Chessboard();
            EngineBetter.searchFixedDepth(board, depth);
            System.out.println("\nfor depth " + depth);
            System.out.println("iid successes: " + iidSuccess);
            System.out.println("iid fails : " + iidFail);
            System.out.println("iid total: " + iidTotal);
        }
    }
    
    
    @Test
    public void branchingFactorTest() {
        for (int i = 8; i < 15; i++) {
            Chessboard board = new Chessboard();
            System.out.println("depth " + i);
            EngineBetter.resetFull();
            searchFixedDepth(board, i);
            System.out.println();
            System.out.println(nonTerminalTime);
            System.out.println(terminalTime);
            System.out.println("branching factor is " + ((double) (nonTerminalNodes + terminalNodes)) / ((double) nonTerminalNodes));
            System.out.println("branching factor by time is " + ((double) (nonTerminalTime + terminalTime)) / ((double) nonTerminalTime));
            System.out.println();
        }
    }





    /**
     *
     * add to engine 
     *             final long zobristPawnHash = board.zobristPawnHash;
     *             if (zobbies.contains(zobristPawnHash)) {
     *                 oldzobHit++;
     *             } else {
     *                 zobbies.add(zobristPawnHash);
     *                 newZob++;
     *             }
     *
     *
     */

    public static List<Long> zobbies = new ArrayList<>();
    public static int oldzobHit = 0;
    public static int newZob = 0;

    @Test
    public void numberOfPawnMoves() {
        /*
        depth 8
oldzobHit: 
14646
newZob
2913
size: 
2913
ratio: 83.41
        
        depth 10:
oldzobHit: 
52036
newZob
7990
size: 
7990
ratio: 86.68

        depth 12:
oldzobHit: 
267197
newZob
29964
size: 
29964
ratio: 89.91

        depth 14:
oldzobHit: 
1494753
newZob
117698
size: 
117698
ratio: 92.70
        
         */
        Chessboard board = new Chessboard();

        searchFixedDepth(board, 14);

        System.out.println("oldzobHit: ");
        System.out.println(oldzobHit);
        System.out.println("newZob");

        System.out.println(newZob);

        System.out.println("size: ");
        System.out.println(zobbies.size());

        System.out.println("ratio: " + (100 * ((double) oldzobHit / (double)(oldzobHit + newZob))));


    }
}
