package com.github.louism33.axolotl.search;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class EarlyGameTest {

    private static int depth = 12;
    
    @org.junit.Test
    public void testStartingMove(){
        Chessboard board = new Chessboard();
        EngineSpecifications.DEBUG = false;
        
        System.out.println("In this position: ");
        System.out.println(board);
        List<String> bestMoves = Arrays.asList("d2d4", "e2e4");
        
        int move = EngineBetter.searchFixedDepth(board, depth);

        String m = MoveParser.toString(move);
        
        System.out.println("best moves: " + bestMoves);
        System.out.println("my move: " + MoveParser.toString(move));
        
        Assert.assertTrue(bestMoves.contains(m));
    }

    @org.junit.Test
    public void testMoveTwoKP(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        EngineSpecifications.DEBUG = false;
        System.out.println("In this position: ");
        System.out.println(board);
        List<String> bestMoves = Arrays.asList("d7d5", "e7e5", "e7e6");
        
        int move = EngineBetter.searchFixedDepth(board, depth);

        String m = MoveParser.toString(move);

        System.out.println("best moves: " + bestMoves);
        System.out.println("my move: " + MoveParser.toString(move));

        Assert.assertTrue(bestMoves.contains(m));
    }

    @org.junit.Test
    public void testMoveTwoQP(){
        Chessboard board = new Chessboard();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));

        EngineSpecifications.DEBUG = false;
        System.out.println("In this position: ");
        System.out.println(board);
        List<String> bestMoves = Arrays.asList("d7d5", "e7e6");

        int move = EngineBetter.searchFixedDepth(board, depth);

        String m = MoveParser.toString(move);

        System.out.println("best moves: " + bestMoves);
        System.out.println("my move: " + MoveParser.toString(move));

        Assert.assertTrue(bestMoves.contains(m));
    }

    @org.junit.Test
    public void testMoveThreeKPkp(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));

        EngineSpecifications.DEBUG = false;
        System.out.println("In this position: ");
        System.out.println(board);
        List<String> bestMoves = Arrays.asList("g1f3", "f1c4");

        int move = EngineBetter.searchFixedDepth(board, depth);

        String m = MoveParser.toString(move);

        System.out.println("best moves: " + bestMoves);
        System.out.println("my move: " + MoveParser.toString(move));

        Assert.assertTrue(bestMoves.contains(m));
    }

    @org.junit.Test
    public void testMoveFourKPkpKN(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));

        EngineSpecifications.DEBUG = false;
        System.out.println("In this position: ");
        System.out.println(board);
        List<String> bestMoves = Arrays.asList("b8c6");

        int move = EngineBetter.searchFixedDepth(board, depth);

        String m = MoveParser.toString(move);

        System.out.println("best moves: " + bestMoves);
        System.out.println("my move: " + MoveParser.toString(move));
        System.out.println("---");

        Assert.assertTrue(bestMoves.contains(m));
    }


    @org.junit.Test
    public void testMoveFiveKPkpKNQN(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g8f6"));

        EngineSpecifications.DEBUG = false;
        System.out.println("In this position: ");
        System.out.println(board);
        List<String> bestMoves = Arrays.asList("f1b5", "f1c4", "d2d4");

        int move = EngineBetter.searchFixedDepth(board, depth);

        String m = MoveParser.toString(move);

        System.out.println("best moves: " + bestMoves);
        System.out.println("my move: " + MoveParser.toString(move));

        Assert.assertTrue(bestMoves.contains(m));
    }
}
