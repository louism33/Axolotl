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
import static com.github.louism33.chesscore.BoardConstants.BLACK_KING;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KING;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITH_CHECK;

//@Disabled
public class MoveOrderer2Test {

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
        countFinalNodesAtDepthHelper(board, 5, false, false, true);
    }

    @Test
    void kingMoveOrder2Test() {
        Chessboard board = new Chessboard("r1b3k1/6p1/P1n1pr1p/q1p5/1b1P4/2N2N2/PP1QBPPP/R3K2R b");
        countFinalNodesAtDepthHelper(board, 5, false, false, true);
    }

    @Test
    void kingMoveOrder3Test() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5, false, false, true);
    }

    @Test
    void kingMoveOrder4Test() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5, false, false, true);
    }
    
    

    @Test
    void kingMoveOrder5TestQ() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5, true, false, false);
    }

    @Test
    void kingMoveOrder6TestQ() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5, true, false, false);
    }


    @Test
    void kingMoveOrder7TestQ() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5, true, false, false);
    }

    @Test
    void kingMoveOrder8TestQ() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5, true, false, false);
    }




    @Test
    void kingMoveOrder9TestR() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5, false, true, false);
    }

    @Test
    void kingMoveOrder10TestR() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5, false, true, false);
    }


    @Test
    void kingMoveOrder11TestR() {
        Chessboard board = new Chessboard("3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - -");
        countFinalNodesAtDepthHelper(board, 5, false, true, false);
    }

    @Test
    void kingMoveOrder12TestR() {
        Chessboard board = new Chessboard("2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - -");
        countFinalNodesAtDepthHelper(board, 5, false, true, false);
    }

    private static void countFinalNodesAtDepthHelper(Chessboard board, int depth, boolean useQSort, boolean useRSort, boolean useRegSort) {
        int[] moves = board.generateLegalMoves();
        if (useRegSort) {
//            scoreMoves(moves, board, 0, 0, 0);
            scoreMovesNew(moves, board, 1, 0, 0);
        } else if (useRSort) {
            scoreMovesAtRoot(moves, moves[moves.length - 1], board);
        } else if (useQSort) {
            scoreMovesQuiescenceNew(moves, 0, 0);
        }
        
        checkThatKingMovesAreLast(board, moves, useQSort);

        if (depth <= 1) {
            return;
        }

        boolean inCheck = board.inCheckRecorder;
        
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }

            board.makeMoveAndFlipTurn(move);

            countFinalNodesAtDepthHelper(board, depth - 1, false, false, true);

            board.unMakeMoveAndFlipTurn();
        }

    }

    private static void checkThatKingMovesAreLast(Chessboard board, int[] moves, boolean useQSort) {
        final int lastMove = moves[moves.length - 1];
        boolean kingMove = false;
        boolean qSearchFoundScore0 = false;
        
        for (int i = 0; i < lastMove; i++) {
            if (moves[i] == 0) {
                break;
            }
            int move = moves[i];

            if (useQSort) {

                final int loudMoveScore = getMoveScore(move);

                final boolean captureMove = MoveParser.isCaptureMove(move);
                final boolean epMove = MoveParser.isEnPassantMove(move);
                final boolean promotionMove = MoveParser.isPromotionMove(move);


                if (loudMoveScore == 0) {
                    qSearchFoundScore0 = true;
                    continue;
                } else if (qSearchFoundScore0) {
                    Assert.fail("non zero score after a 0 score");
                }
            }
            
            
            
            final int movingPieceInt = MoveParser.getMovingPieceInt(move);
            if ((!MoveParser.isCaptureMove(move) && !MoveParser.isCastlingMove(move)) // if it is a quiet move
                    && (movingPieceInt == WHITE_KING || movingPieceInt == BLACK_KING)) {
                kingMove = true;
            } else if (kingMove && !MoveParser.isPromotionToBishop(move) && !MoveParser.isPromotionToRook(move)) {
                System.out.println(board);
                MoveParser.printMove(moves);
                MoveParser.printMove(move);
                System.out.println("index: " + i + ", move score: " + getMoveScore(move));
                System.out.println("prev index move score: " + getMoveScore(moves[i - 1]));

                final int scoreOfMove = quietHeuristicMoveScore(move & MOVE_MASK_WITH_CHECK, board.turn, maxNodeQuietScore);
//                System.out.println(scoreOfMove);
                final int scoreOfMoveP = quietHeuristicMoveScore(moves[i - 1] & MOVE_MASK_WITH_CHECK, board.turn, maxNodeQuietScore);
//                System.out.println(scoreOfMoveP);

                Assert.fail("moving a king quietly with higher priority than another piece");
            }
        }
    }

}