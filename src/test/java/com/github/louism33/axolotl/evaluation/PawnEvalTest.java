package com.github.louism33.axolotl.evaluation;

import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.axolotl.search.EngineBetter.searchFixedDepth;

public class PawnEvalTest {

    @BeforeAll
    static void reset() {
        Util.reset();
    }
    
    @Test
    void retrieveFromTableSimpleTest() {
        PawnTranspositionTable.reset();
        
        Chessboard board = new Chessboard();

        long[] pawnData = PawnTranspositionTable.retrieveFromTable(board.zobristPawnHash);

        Assert.assertNull(pawnData);

        pawnData = PawnEval.calculatePawnData(board);
        PawnTranspositionTable.addToTableReplaceArbitrarily(board.zobristPawnHash, pawnData, PawnEval.pawnScores);


        Assert.assertNotNull(pawnData);
    }

    public static List<Long> zobbies = new ArrayList<>();
    public static int oldzobHit = 0;
    public static int newZob = 0;



    @Test
    void aaaTest() {
//
//        int pawnIndex = 36;
//        int turn = WHITE;
//        long pawn = BitOperations.newPieceOnSquare(pawnIndex);
//
//        Art.printLong(pawn);
//        Art.printLong(pawnAttackSpans[1 - turn][pawnIndex + (8 - (16 * turn))]);
//        Art.printLong(pawnAttackSpans[1 - turn][pawnIndex]);
//
//        for (int i = 0; i < 64; i++) {
////            Art.printLong(pawnAttackSpans[1 - turn][i]);
//        }
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

    @Test
    void numberOfPawnMoves() {
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
