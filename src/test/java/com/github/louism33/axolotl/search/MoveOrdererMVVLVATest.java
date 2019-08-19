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
import static com.github.louism33.axolotl.search.MoveOrderingConstants.maxNodeQuietScore;
import static com.github.louism33.chesscore.BoardConstants.BLACK_KING;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KING;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

//@Disabled
public class MoveOrdererMVVLVATest {

    @BeforeAll
    static void setup() {
        ResettingUtils.reset();
    }

    @AfterAll
    static void reset() {
        ResettingUtils.reset();
    }

    @Test
    void order1Test() {
        Chessboard board = new Chessboard();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7d5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g8f6"));
        
        final int e4d5 = MoveParserFromAN.buildMoveFromLAN(board, "e4d5");
        Assert.assertTrue(
                (getMVVLVAScore(e4d5) ==
                getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "d4e5"))));

        Assert.assertTrue(e4d5 >
                getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "f3e5")));
        
    }


    @Test
    void order2Test() {
        Chessboard board = new Chessboard("1Q6/6Rp/4Bk2/5p2/p1p2P2/BrNpPK2/1P1R3P/8 b - -");

        Assert.assertTrue(
                (getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "b3b8")) >
                        getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "f6g7"))));

        Assert.assertTrue(MoveParserFromAN.buildMoveFromLAN(board, "f6g7") >
                getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "b3a3")));

        Assert.assertTrue(MoveParserFromAN.buildMoveFromLAN(board, "b3a3") >
                getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "f6e6")));

        Assert.assertTrue(MoveParserFromAN.buildMoveFromLAN(board, "f6e6") >
                getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "b3c3")));

        Assert.assertTrue(MoveParserFromAN.buildMoveFromLAN(board, "b3c3") >
                getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "b3b2")));
    }


    @Test
    void order3Test() {
        Chessboard board = new Chessboard("rQ6/6Rp/4Bk2/5p2/p1p2P2/BrNpPK2/1P1R3P/8 b - -");

        Assert.assertTrue(
                (getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "b3b8")) ==
                        getMVVLVAScore(MoveParserFromAN.buildMoveFromLAN(board, "a8b8"))));
    }
}