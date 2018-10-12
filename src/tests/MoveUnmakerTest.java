package tests;

import main.check.CheckChecker;
import main.chess.Art;
import main.chess.Chessboard;
import main.chess.Copier;
import main.chess.Move;
import main.moveGeneration.MoveGeneratorMaster;
import main.moveMaking.MoveOrganiser;
import main.moveMaking.MoveUnmaker;
import main.moveMaking.StackMoveData;
import main.utils.RandomBoard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static main.chess.StandAlone.board;
import static org.junit.jupiter.api.Assertions.*;

class MoveUnmakerTest {

    static Chessboard[] bs;

    @BeforeEach
    void setUp() {
        bs = RandomBoard.boardForTests();
//        RandomBoard.printBoards(bs);
    }


    @Test
    void unMakeMoveMaster() {
        int totalWhites = 0;

        boolean debug = false;

        System.out.println("------- Single Takebacks, starting white");
        for (int i = 0; i < bs.length; i++) {
            
            Chessboard b = bs[i];
            Chessboard copy = Copier.copyBoard(b, b.isWhiteTurn(), false);
            
            
            if (CheckChecker.boardInCheck(b, !b.isWhiteTurn())){
                continue;
            }
            
            
            if (debug) {
                System.out.println("----------------- " + i + " -------------------");
                System.out.println("White's turn: " + b.isWhiteTurn());
            }
            if (debug) {
                System.out.println(Art.boardArt(b));
            }
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(b, b.isWhiteTurn());
            for (int m = 0; m < moves.size(); m++) {
                totalWhites++;
                if (debug) {
                    System.out.println("Move Number " + m + ", " + moves.get(m));
                    System.out.println(Art.boardArt(b));
                }
                Move move = moves.get(m);
                if (debug) {
                    System.out.println(move);
                }
                MoveOrganiser.makeMoveMaster(b, move);
                MoveOrganiser.flipTurn(b);
                if (debug) {
                    System.out.println(Art.boardArt(b));
                }
                MoveUnmaker.unMakeMoveMaster(b);
                if (debug) {
                    System.out.println(Art.boardArt(b));
                }
                Assert.assertTrue(b.equals(copy));
            }
        }
        System.out.println("total white single tests: " + totalWhites);


        System.out.println("------- Single Takebacks, starting at black");
        int totalBlacks = 0;
        for (int i = 0; i < bs.length; i++) {
            MoveOrganiser.flipTurn(bs[i]);
            Chessboard b = bs[i];
            Chessboard copy = Copier.copyBoard(b, b.isWhiteTurn(), false);

            if (CheckChecker.boardInCheck(b, !b.isWhiteTurn())){
                continue;
            }
            
            
            
            if (debug) {
                System.out.println("----------------- " + i + " -------------------");
                System.out.println("Whites's turn: " + b.isWhiteTurn());
            }
            if (debug) {
                System.out.println("Board number " + i + ":");
                System.out.println(Art.boardArt(b));
            }
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(b, b.isWhiteTurn());
            for (int m = 0; m < moves.size(); m++) {
                totalBlacks++;
                if (debug) {
                    System.out.println("Move Number " + m + ", " + moves.get(m));
                    System.out.println(Art.boardArt(b));
                }
                Move move = moves.get(m);
                if (debug) {
                    System.out.println(move);
                }
                MoveOrganiser.makeMoveMaster(b, move);
                MoveOrganiser.flipTurn(b);
                if (debug) {
                    System.out.println(Art.boardArt(b));
                }
                MoveUnmaker.unMakeMoveMaster(b);
                if (debug) {
                    System.out.println(Art.boardArt(b));
                }
                Assert.assertTrue(b.equals(copy));
            }
        }
        System.out.println("total black single tests: " + totalBlacks);


        int total = 0;
        System.out.println("------- Multiple Takebacks, starting white");
        for (int i = 0; i < bs.length; i++) {
            
            Chessboard b = bs[i];
            b.setWhiteTurn(true);
            Chessboard copy = Copier.copyBoard(b, b.isWhiteTurn(), false);

            if (CheckChecker.boardInCheck(b, !b.isWhiteTurn())){
                continue;
            }
            
            
            if (debug) {
                System.out.println("----------------- " + i + " -------------------");
                System.out.println("Whites's turn: " + b.isWhiteTurn());
            }
            if (debug) {
                System.out.println(Art.boardArt(b));
            }

            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(b, b.isWhiteTurn());
            for (int m = 0; m < moves.size(); m++) {
                if (debug) {
                    System.out.println("Move Number " + m + ", " + moves.get(m));
                    System.out.println(Art.boardArt(b));
                }
                Move move = moves.get(m);
                if (debug) {
                    System.out.println(move);
                }
                MoveOrganiser.makeMoveMaster(b, move);
                if (debug) {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                }
                MoveOrganiser.flipTurn(b);

                List<Move> movesss = MoveGeneratorMaster.generateLegalMoves(b, b.isWhiteTurn());
                for (int mm = 0; mm < movesss.size(); mm++) {
                    total++;
                    if (debug) {
                        System.out.println("Move Number " + mm + ", " + movesss.get(mm));
                        System.out.println(Art.boardArt(b));
                    }
                    
                    
                    Move moveX = movesss.get(mm);
                    if (debug) {
                        System.out.println(moveX);
                    }
                    MoveOrganiser.makeMoveMaster(b, moveX);
                    MoveOrganiser.flipTurn(b);
                    if (debug) {
                        System.out.println(Art.boardArt(b));
                    }
                    MoveUnmaker.unMakeMoveMaster(b);
                    if (debug) {
                        System.out.println(Art.boardArt(b));
                    }
                }


                if (debug) {
                    System.out.println(Art.boardArt(b));
                }
                if (debug) {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                }
                MoveUnmaker.unMakeMoveMaster(b);
                if (debug) {
                    System.out.println(Art.boardArt(b));
                }
                Assert.assertTrue(b.equals(copy));
            }
        }
        System.out.println("total multi level tests: " + total);




        Chessboard board = new Chessboard();
        int num = 1000;
        System.out.println("------- Random high depth, starting white");
        Chessboard copy1 = Copier.copyBoard(board, board.isWhiteTurn(), false);
        moveNStuffRandom(board, num, debug);
        System.out.println(Art.boardArt(board));
        unMoveNStuff(board, debug);

        System.out.println("Same as before ? " + board.equals(copy1));
        System.out.println();
        Assert.assertEquals(board, copy1);        
    }



    static void unMoveNStuff(Chessboard board, boolean debug){

        int size = board.moveStack.size();
        System.out.println(size);

        for (int undo = 0; undo < size; undo++) {
            MoveUnmaker.unMakeMoveMaster(board);
            if (debug) {
                System.out.println(Art.boardArt(board));
            }
        }
    }

    static void moveNStuffRandom (Chessboard board, int totalRandoms, boolean debug){
        for (int r = 0; r < totalRandoms; r++) {
            List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
            Random rand = new Random();
            
            if (moves.size() == 0){
                System.out.println("Checkmate");
                return;
            }

            Move move = moves.get(rand.nextInt(moves.size()));

            if (debug) {
                System.out.println(moves + " " + moves.size());
                System.out.println(move);
            }
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            if (debug) {
                System.out.println(Art.boardArt(board));
                StackMoveData peek = board.moveStack.peek();
                System.out.println(peek);
//            System.out.println("Peek turn white : " + peek.whiteTurn);
            }
        }
    }
}