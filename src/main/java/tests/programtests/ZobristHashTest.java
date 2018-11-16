package tests.programtests;

import javacode.chessengine.search.Engine;
import javacode.chessengine.transpositiontable.EngineMovesAndHash;
import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;
import javacode.chessprogram.miscAdmin.FenParser;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

class ZobristHashTest {

    @Test
    void zobristTest1() {
        Chessboard chessboard = FenParser.makeBoardBasedOnFEN("6r1/p3p1rk/1p1pPp1p/q3n2R/4P3/3BR2P/PPP2QP1/7K w - - 0 1");
        System.out.println(Art.boardArt(chessboard));

        final ZobristHash testHash = new ZobristHash(chessboard);

        Engine engine = new Engine();
        Move move = engine.searchFixedTime(chessboard, 1000);
        System.out.println(move);

//        ZobristHash zobristHash = engine.zobristHash;
//        System.out.println("ZobristHash: \n"+zobristHash);
//        System.out.println("TestHash: \n" + testHash);
//        Assert.assertEquals(zobristHash, testHash);
    }

    @Test
    void zobristTest2() {
        Chessboard board = new Chessboard();
        System.out.println(Art.boardArt(board));

        ZobristHash myHash = new ZobristHash(board);
        final ZobristHash testHash = new ZobristHash(board);

        Assert.assertEquals(myHash, testHash);

        Random r = new Random(100);
        
        int num = 100;
        for (int m = 0; m < num; m++){
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
            Move move = moves.get(r.nextInt(moves.size()));
            EngineMovesAndHash.makeMoveAndHashUpdate(board, move, myHash);
        }
        System.out.println(Art.boardArt(board));

        for (int m = 0; m < num; m++){
            EngineMovesAndHash.UnMakeMoveAndHashUpdate(board, myHash);
        }

        Assert.assertEquals(myHash, testHash);
    }


    @Test
    void zobristTest3() {
        Chessboard board = FenParser.makeBoardBasedOnFEN("r3kb1r/1p1b1p2/p1nppp2/7p/4PP2/qNN5/P1PQB1PP/R4R1K w kq - ");
        System.out.println(Art.boardArt(board));

        ZobristHash myHash = new ZobristHash(board);
        final ZobristHash testHash = new ZobristHash(board);

        Assert.assertEquals(myHash, testHash);

        Random r = new Random(100);

        int num = 100;
        for (int m = 0; m < num; m++){
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
            Move move = moves.get(r.nextInt(moves.size()));
            EngineMovesAndHash.makeMoveAndHashUpdate(board, move, myHash);
        }
        System.out.println(Art.boardArt(board));

        for (int m = 0; m < num; m++){
            EngineMovesAndHash.UnMakeMoveAndHashUpdate(board, myHash);
        }

        Assert.assertEquals(myHash, testHash);
    }



    @Test
    void zobristTestNull() {
        Chessboard board = FenParser.makeBoardBasedOnFEN("r3kb1r/1p1b1p2/p1nppp2/7p/4PP2/qNN5/P1PQB1PP/R4R1K w kq - ");
        System.out.println(Art.boardArt(board));

        ZobristHash myHash = new ZobristHash(board);
        final ZobristHash testHash = new ZobristHash(board);

        Assert.assertEquals(myHash, testHash);

        EngineMovesAndHash.makeNullMove(board, myHash);

        EngineMovesAndHash.unMakeNullMove(board, myHash);

        Assert.assertEquals(myHash, testHash);
    }



    @Test
    void zobristTestNull2() {
        Chessboard board = FenParser.makeBoardBasedOnFEN("r3kb1r/1p1b1p2/p1nppp2/7p/4PP2/qNN5/P1PQB1PP/R4R1K w kq - ");
        System.out.println(Art.boardArt(board));

        ZobristHash myHash = new ZobristHash(board);
        final ZobristHash testHash = new ZobristHash(board);

        Assert.assertEquals(myHash, testHash);

        Random r = new Random(100);

        int num = 100;
        for (int m = 0; m < num; m++){
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
            Move move = moves.get(r.nextInt(moves.size()));
            EngineMovesAndHash.makeMoveAndHashUpdate(board, move, myHash);

            List<Move> moves2 = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
            Move move2 = moves2.get(r.nextInt(moves2.size()));
            EngineMovesAndHash.makeMoveAndHashUpdate(board, move2, myHash);
            
            EngineMovesAndHash.makeNullMove(board, myHash);
        }
        
        
        System.out.println(Art.boardArt(board));

        for (int m = 0; m < num; m++){
            EngineMovesAndHash.unMakeNullMove(board, myHash);
            EngineMovesAndHash.UnMakeMoveAndHashUpdate(board, myHash);
            EngineMovesAndHash.UnMakeMoveAndHashUpdate(board, myHash);
            
        }

        Assert.assertEquals(myHash, testHash);
    }
    
}