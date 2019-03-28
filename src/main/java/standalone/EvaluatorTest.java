package standalone;

import com.fluxchess.jcpi.models.IllegalNotationException;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Test;

@SuppressWarnings("All")
public class EvaluatorTest {


    @Test
    public void compareBishopColourScores() throws IllegalNotationException {
        Chessboard board = new Chessboard();
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        printNStuff(board);
//        printNStuff(board);
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
//        printNStuff(board);
//
//        board.unMakeMoveAndFlipTurn();
//        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));
//
//        printNStuff(board);
//
    }

    @Test
    public void compareKnights2() throws IllegalNotationException {
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        printNStuff(board);
        
        board.unMakeMoveAndFlipTurn();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));

        printNStuff(board);

    }
    
    @Test
    public void compareKnights() throws IllegalNotationException {
        Chessboard board = new Chessboard();
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b8c6"));

        printNStuff(board);
        
    }
    
    
    @Test
    public void printEval() throws IllegalNotationException {
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b8c6"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "f1b5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "a7a6"));


        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b5c6"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7c6"));
        printNStuff(board);

        board.unMakeMoveAndFlipTurn();
        board.unMakeMoveAndFlipTurn();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b5a4"));
        printNStuff(board);

        
    }



    static void printNStuff(Chessboard board){
        System.out.println(board);
        int[] moves = board.generateLegalMoves();
        Evaluator.printEval(board, board.turn, moves);
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("\n\n");
    }

   
}
