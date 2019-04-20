package standalone;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.axolotl.search.Engine.*;

public class EngineFeatures {

    
    @Test
    public void aaa() {
        Chessboard board = new Chessboard("2r1r2k/1q3ppp/p2Rp3/2p1P3/6QB/p3P3/bP3PPP/3R2K1 w - -");
//        EngineBetter.searchFixedTime(board, 60000);
        Engine.searchFixedDepth(board, 12);
    }
    
    @Test
    public void testIID() {
        EngineSpecifications.DEBUG = false;
        for (int depth = 8; depth < 15; depth++) {
            Engine.resetFull();
            iidFail = 0;
            iidSuccess = 0;
            iidTotal = 0;
            Chessboard board = new Chessboard();
            Engine.searchFixedDepth(board, depth);
            System.out.println("\nfor depth " + depth);
            System.out.println("iid successes: " + iidSuccess);
            System.out.println("iid fails : " + iidFail);
            System.out.println("iid total: " + iidTotal);
            System.out.println("successes of total:  " + (((double) iidSuccess) / (double) iidTotal));
        }
    }

    @Test
    public void testFutility() {
        EngineSpecifications.DEBUG = false;
        for (int depth = 8; depth < 15; depth++) {
            Engine.resetFull();
            futilityFail = 0;
            futilitySuccess = 0;
            futilityTotal = 0;
            Chessboard board = new Chessboard();
            Engine.searchFixedDepth(board, depth);
            System.out.println("\nfor depth " + depth);
            System.out.println("futility successes:  " + futilitySuccess);
            System.out.println("futility fails :     " + futilityFail);
            System.out.println("futility total:      " + futilityTotal);
            System.out.println("successes of total:  " + (((double) futilitySuccess) / (double) futilityTotal));
        }
    }

    @Test
    public void testNull() {
        EngineSpecifications.DEBUG = false;
        for (int depth = 8; depth < 15; depth++) {
            Engine.resetFull();
            nullFail = 0;
            nullSuccess = 0;
            nullTotal = 0;
            Chessboard board = new Chessboard();
            Engine.searchFixedDepth(board, depth);
            System.out.println("\nfor depth " + depth);
            System.out.println("null successes:  " + nullSuccess);
            System.out.println("null fails :     " + nullFail);
            System.out.println("null total:      " + nullTotal);
            System.out.println("successes of total:  " + (((double) nullSuccess) / (double) nullTotal));
        }
    }

    @Test
    public void testAsp() {
        EngineSpecifications.DEBUG = false;
        for (int depth = 8; depth < 15; depth++) {
            Engine.resetFull();
            aspFailA = 0;
            aspFailB = 0;
            aspSuccess = 0;
            aspTotal = 0;
            Chessboard board = new Chessboard();
            Engine.searchFixedDepth(board, depth);
            System.out.println("\nfor depth " + depth);
            System.out.println("asp successes:  " + aspSuccess);
            System.out.println("aspA fails :     " + aspFailA);
            System.out.println("aspB fails :     " + aspFailB);
            System.out.println("asp total:      " + aspTotal);
            System.out.println("successes of total:  " + (((double) aspSuccess) / (double) aspTotal));
        }
    }
    
    @Test
    public void branchingFactorTest() {
        for (int i = 8; i < 15; i++) {
            Chessboard board = new Chessboard();
            System.out.println("depth " + i);
            Engine.resetFull();
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
