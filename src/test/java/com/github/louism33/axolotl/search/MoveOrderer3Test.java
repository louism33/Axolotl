package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.ResettingUtils;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.louism33.axolotl.search.MoveOrderer.*;
import static com.github.louism33.axolotl.search.MoveOrderingConstants.maxNodeQuietScore;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.BoardConstants.STAR;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

public class MoveOrderer3Test {

    @BeforeAll
    static void setup() {
        ResettingUtils.reset();
    }

    @AfterAll
    static void reset() {
        ResettingUtils.reset();
    }

    @Test
    void kingMoveOrder1Test() {
        Chessboard board = new Chessboard();
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder2Test() {
        Chessboard board = new Chessboard("r1b3k1/6p1/P1n1pr1p/q1p5/1b1P4/2N2N2/PP1QBPPP/R3K2R b");
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder3Test() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder4Test() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5);
    }



    @Test
    void kingMoveOrder5TestQ() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder6TestQ() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5);
    }


    @Test
    void kingMoveOrder7TestQ() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder8TestQ() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5);
    }




    @Test
    void kingMoveOrder9TestR() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder10TestR() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5);
    }


    @Test
    void kingMoveOrder11TestR() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5);
    }

    @Test
    void kingMoveOrder12TestR() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5);
    }

    private static void countFinalNodesAtDepthHelper(Chessboard board, int depth) {
        int[] moves = board.generateLegalMoves();

        if (depth <= 1) {
            return;
        }

        boolean inCheck = board.inCheckRecorder;

        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            checkMoveOrdererIsCheckingMove(board, move);

            board.makeMoveAndFlipTurn(move);

            countFinalNodesAtDepthHelper(board, depth - 1);

            board.unMakeMoveAndFlipTurn();
        }

    }

    private static void checkMoveOrdererIsCheckingMove(Chessboard board, int move) {

        final long enemyKing = board.pieces[1 - board.turn][KING];
        final int enemyKingIndex = Long.numberOfTrailingZeros(enemyKing);
        final long enemyKingCross = CROSSES[enemyKingIndex];
        final long enemyKingX = EXES[enemyKingIndex];
        final long enemyKingStar = STAR[enemyKingIndex];

        final boolean captureMove = MoveParser.isCaptureMove(move);
        final boolean epMove = MoveParser.isEnPassantMove(move);
        final boolean promotionMove = MoveParser.isPromotionMove(move);
        final boolean interestingMove = captureMove || epMove || promotionMove;

        if (interestingMove) {
            return;
        }
        final boolean predictedCheckingState = checkingMove(board, move, enemyKingIndex, enemyKingCross, enemyKingX, enemyKingStar);
        boolean isReallyCheck;
        board.makeMoveAndFlipTurn(move);
        isReallyCheck = board.inCheck(board.turn == WHITE);
        board.unMakeMoveAndFlipTurn();

        Assert.assertEquals(isReallyCheck, predictedCheckingState);
    }

}