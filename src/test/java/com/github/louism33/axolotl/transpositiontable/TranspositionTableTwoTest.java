package com.github.louism33.axolotl.transpositiontable;

import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.evaluation.EvaluationConstants;
import com.github.louism33.axolotl.main.PVLine;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Art;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveConstants;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.ParagraphView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.louism33.axolotl.transpositiontable.TranspositionTable.*;
import static com.github.louism33.axolotl.transpositiontable.TranspositionTableConstants.EXACT;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

public class TranspositionTableTwoTest {

//    @Test
//    public void buildTableEntryTestSingle() {
//
//        Chessboard board = new Chessboard();
//
//        int bestMove = MoveParserFromAN.buildMoveFromLAN(board, "e2e4");
//        int bestScore = 100;
//        int depth = 6;
//        int flag = EXACT;
//        int ply = 0;
//        
//        addToTableReplaceByDepth(board.zobristHash,
//                bestMove & MOVE_MASK_WITHOUT_CHECK, bestScore, depth, flag, ply);
//
//  
//    }
//
//    @Test
//    public void persistenceTest() {
//
//        Chessboard board = new Chessboard();
//
//        int bestMove = EngineBetter.searchFixedDepth(board, 3);
//        int move = getMove(retrieveFromTable(board.zobristHash));
//
//        Assert.assertEquals(bestMove, move);
//        
//        List<GenericMove> genericMoves = PVLine.retrievePV(board);
//
//        System.out.println(genericMoves);
//
//
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(0).toString()));
//        
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, genericMoves.get(1).toString()));
//
//        System.out.println(board);
//
//
//    }

}