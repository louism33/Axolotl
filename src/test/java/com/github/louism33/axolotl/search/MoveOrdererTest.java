package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.addToTableReplaceByDepth;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public class MoveOrdererTest {

    @BeforeAll
    static void setup() {
        ResettingUtils.reset();
    }

    @AfterAll
    static void reset() {
        ResettingUtils.reset();
    }

    @Test
    public void moveOrdererQuietHeuristicTest() {
        try {
            for (int piece = WHITE_PAWN; piece <= BLACK_KING; piece++) {

                for (int source = 0; source < 64; source++) {
                    for (int dest = 0; dest < 64; dest++) {
                        int move = MoveParser.buildMove(source, piece, dest);
                        int i = quietHeuristicMoveScore(move, piece < BLACK_PAWN ? WHITE : BLACK, MoveOrderingConstants.maxRootQuietScore);
                    }
                }
            }
        } catch (Exception e) {
            throw new AssertionError("quiet heuristic orderer failed.");
        }
    }



    @Test
    public void moveScoreTest() {
        Chessboard board = new Chessboard();
        int[] ints = board.generateLegalMoves();
        int move = ints[0];

        int max = MoveOrderingConstants.hashScore;
        for (int s = 1; s < max; s++) {
            int moveScore = buildMoveScore(move, s);
            int scoreFromMove = getMoveScore(moveScore);

            Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(moveScore));
            Assert.assertEquals(scoreFromMove, s);
        }
    }


    @Test
    public void positiveMovesTest() {
        Chessboard board = new Chessboard("r2r2k1/pp2bppp/2p1p3/4qb1P/8/1BP1BQ2/PP3PP1/2KR3R b - - 0 1");

        int[] moves = board.generateLegalMoves();
        for (int m = 0; m < moves.length; m++) {
            int move = moves[m];
            if (move == 0) {
                break;
            }
            int max = MoveOrderingConstants.hashScore;
            for (int s = 1; s < max; s++) {
                int moveScore = buildMoveScore(move, s);
                int scoreFromMove = getMoveScore(moveScore);

                Assert.assertTrue(moveScore > 0);
                Assert.assertEquals(MoveParser.toString(move), MoveParser.toString(moveScore));
                Assert.assertEquals(scoreFromMove, s);
            }
        }
    }

}