package standalone;

import com.github.louism33.axolotl.evaluation.EvalPrintObject;
import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Test;

import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;

@SuppressWarnings("All")
public class EvaluatorSituations {

    @Test
    public void start()  {
        Chessboard board = new Chessboard();
        printNStuff(board);
    } 
    
    @Test
    public void startMirrorKing() {
        Chessboard board = new Chessboard();
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
//        printNStuff(board);
        
    }    
    
    @Test
    public void highKingSafety()  {
        Chessboard board = new Chessboard();
        printNStuff(board);
        Chessboard board1 = new Chessboard("rnbqkbnr/pppppppp/8/8/8/5NP1/PPPPPPBP/RNBQ1RK1 w KQkq -");
        printNStuff(board1);
    }       
    
    
    @Test
    public void neutralKingSafety()  {
        Chessboard board = new Chessboard("rnbqkbnr/pppppppp/8/8/8/8/2K5/8 w KQkq -");
        printNStuff(board);
    }    
    
    @Test
    public void ksWithoutQueen()  {
        Chessboard board = new Chessboard("r1bqk1nr/1pppbppp/p1n5/4p3/B3P3/3P1N2/PPP2PPP/RNBQK2R w KQkq -");
        printNStuff(board);

        System.out.println("********");
        System.out.println("********");
        Chessboard board1 = new Chessboard("r1bqk1nr/1pppbppp/p1n5/4p3/B3P3/3P1N2/PPP2PPP/RNB1K2R w KQkq -");
        printNStuff(board1);
    }    
    
    @Test
    public void castle()  {
        Chessboard board = new Chessboard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQ1RK1 w kq -");
        printNStuff(board);
    }

    @Test
    public void prePostCastle()  {
        Chessboard board = new Chessboard("r1bqk1nr/1pppbppp/p1n5/4p3/B3P3/3P1N2/PPP2PPP/RNBQK2R w KQkq -");
        printNStuff(board);

        System.out.println("*************************");
        System.out.println("*************************");
        System.out.println("*************************");
        Chessboard board1 = new Chessboard("r1bqk1nr/1pppbppp/p1n5/4p3/B3P3/3P1N2/PPP2PPP/RNBQ1RK1 w KQkq -");
        printNStuff(board1);
    }
    
    @Test
    public void postCastle()  {
        Chessboard board = new Chessboard("r1bqk1nr/1ppp1ppp/p1n5/2b1p3/B3P3/3P1N2/PPP2PPP/RNBQ1RK1 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void bishopExtremeColourWeakness()  {
        Chessboard board = new Chessboard("4k3/1p3p1p/p1p1p1p1/3p4/4P3/1P1P1P1P/P1P3P1/4KB2 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void space()  {
        Chessboard board = new Chessboard();
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));

        Evaluator.eval(board, board.generateLegalMoves());
        System.out.println(board 
                + "\nwhite space score: " + Evaluator.scoresForEPO[WHITE][EvalPrintObject.spaceScore]
                + "\nblack space score: " + Evaluator.scoresForEPO[BLACK][EvalPrintObject.spaceScore]);
        
    }

    @Test
    public void compareBishopColourScores()  {
        Chessboard board = new Chessboard();
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        printNStuff(board);

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d3"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d7d6"));
        printNStuff(board);
    }

    @Test
    public void compareKnights2()  {
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        printNStuff(board);
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        printNStuff(board);
        
        board.unMakeMoveAndFlipTurn();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b1c3"));

        printNStuff(board);

    }
    
    @Test
    public void compareKnights()  {
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
    public void compareBnTradeToRetreat()  {
        Chessboard board = new Chessboard();

        printNStuff(board);


        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        printNStuff(board);
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

    @Test
    public void oneBishopReachUndefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/8/8/2B5/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneBishopReachDefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/8/4P3/1B6/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneBishopOnUndefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/3B4/8/8/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneBishopOnDefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/3B4/4P3/8/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneKnightReachUndefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/8/8/2N5/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneKnightReachDefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/8/4P3/2N5/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneKnightOnUndefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/3N4/8/8/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void oneKnightOnDefOutpost()  {
        Chessboard board = new Chessboard("4k3/pp1p1ppp/8/3N4/4P3/8/8/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void reach2Outposts()  {
        Chessboard board = new Chessboard("rnbqkbnr/1p1p1ppp/8/8/P3P3/2N5/1PPP1PPP/R1BQKBNR w KQkq -");
        printNStuff(board);
    }

    @Test
    public void knightReach2OutpostsUndefended()  {
        Chessboard board = new Chessboard("4k3/1p1p1ppp/8/8/8/2N5/PPPPPPPP/4K3 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void rookSemi()  {
        Chessboard board = new Chessboard("4k3/pppppppp/8/8/8/8/8/R2K4 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void rookThreatenPawns()  {
        Chessboard board = new Chessboard("4k3/1ppppppp/p7/8/8/8/8/R2K4 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void rookOpen()  {
        Chessboard board = new Chessboard("4k3/1ppppppp/8/8/8/8/8/R2K4 w KQkq -");
        printNStuff(board);
    }

    @Test
    public void rookBattery()  {
        Chessboard board = new Chessboard("4k3/pppppppp/8/8/8/R7/8/R2K4 w KQkq -");
        printNStuff(board);
    }
    
    @Test
    public void whiteCantCastle()  {
        Chessboard board = new Chessboard();
        printNStuff(board);
        Chessboard board1 = new Chessboard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq -");
        printNStuff(board1);
    }



    static void printNStuff(Chessboard board){
        System.out.println(board);
        int[] moves = board.generateLegalMoves();
        Evaluator.printEval(board, board.turn, moves);
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("\n");
    }

   
}
