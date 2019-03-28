package standalone;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import org.junit.Test;

import java.util.Arrays;

import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

@SuppressWarnings("ALL")
public class SearchInDetail {

    @Test
    public void master(){
        Chessboard board = new Chessboard();
        
        int totalMoves = 5;
        int[] recorder = new int[totalMoves + 1];
        int[] scoreRecorder = new int[totalMoves];
        recorder[recorder.length - 1] = totalMoves;
        
        int depth = 12;
        long time = 0;
        int doUntil = 3;
        int move;
        
        
        for (int i = 0; i < totalMoves; i++) {
            
            getMoveHierarchy(board, depth, time, doUntil, true);

            recorder[i] = favouriteMove;
            scoreRecorder[i] = favouriteMoveScore;
            board.makeMoveAndFlipTurn(favouriteMove);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("making move " + MoveParser.toString(favouriteMove));
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
            
            if (i != (totalMoves - 1)) {
                System.out.println();
                System.out.println();
                System.out.println();

                System.out.println();
            }
            else {
                System.out.println("board position after " + totalMoves + ": ");
                System.out.println(board);
                System.out.println("moves made: ");
                MoveParser.printMove(recorder);
                System.out.println("score: ");
                System.out.println(Arrays.toString(scoreRecorder));
            }
        }

        System.out.println("timelapse:");
        Chessboard b = new Chessboard();
        System.out.println(b);
        for (int i = 0; i < recorder[recorder.length - 1]; i++) {
            b.makeMoveAndFlipTurn(recorder[i]);
            System.out.println(b);
        }

        System.out.println("board position after " + totalMoves + ": ");
        System.out.println(board);
        System.out.println("moves made: ");
        MoveParser.printMove(recorder);
        System.out.println("score: ");
        System.out.println(Arrays.toString(scoreRecorder));
        
    }
    
    
    @Test
    public void startingMoves(){
        Chessboard board = new Chessboard();
        getMoveHierarchy(board, 15, 0, 5, true);
    }

    @Test
    public void testMoveTwoKP(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));

        getMoveHierarchy(board, 15, 0, 3, true);
    }

    @Test
    public void testMoveTwoQP(){
        Chessboard board = new Chessboard();
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "d2d4"));

        getMoveHierarchy(board, 15, 0, 5, true);
    }
    
    @Test
    public void testMoveThreeKPkp(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));

        getMoveHierarchy(board, 15, 0, 5, true);
    }

    @Test
    public void testMoveFourKPkpKN(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));

        getMoveHierarchy(board, 14, 10000, 3, true);
    }

    @Test
    public void testMoveFiveKPkpKNqn(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b8c6"));

        getMoveHierarchy(board, 14, 10000, 3, true);
    }

    @Test
    public void testMoveSixKPkpKNqn(){
        Chessboard board = new Chessboard();

        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b8c6"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "f1b5"));

        getMoveHierarchy(board, 14, 10000, 3, true);
    }    
    
    @Test
    public void testTradeBishopKnight(){
        Chessboard board = new Chessboard();

        EngineSpecifications.DEBUG = true;
        
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e2e4"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "e7e5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "g1f3"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "b8c6"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "f1b5"));
        board.makeMoveAndFlipTurn(MoveParserFromAN.buildMoveFromLAN(board, "a7a6"));

        getMoveHierarchy(board, 14, 10000, 3, true);
    }


    public void getMoveHierarchy(Chessboard board, int depth, long time, int doUntil) {
        getMoveHierarchy(board, depth, time, doUntil, false);
    }

    public void getMoveHierarchy(Chessboard board, int depth, long time, int doUntil, boolean print) {
        EngineBetter.quitOnSingleMove = false;
        EngineBetter.computeMoves = false;
        int[] moves = board.generateLegalMoves();

        System.out.println(board);

        if (print) {
            Evaluator.printEval(board, board.turn);
        }

        System.out.println();

        int[] bestMoves = new int[doUntil + 1];
        int[] bestMoveScores = new int[doUntil];
        bestMoves[bestMoves.length - 1] = doUntil;

        getMoveHierarchyHelper(board, moves, depth, time, 1, doUntil, print, bestMoves, bestMoveScores);

        System.out.println();
        System.out.println("My moves in order of precedence are: ");
        MoveParser.printMove(bestMoves);
        System.out.println("with scores:");
        System.out.println(Arrays.toString(bestMoveScores));
        
    }

    int favouriteMove = 0;
    int favouriteMoveScore = 0;
    
    public void getMoveHierarchyHelper(Chessboard board, int[] moves,
                                       int depth, long time, int iter, int doUntil, boolean print, int[] bestMoves, int[] bestMoveScores) {

        EngineBetter.resetFull();
        
        System.out.println();
        System.out.println();

        System.out.println("***************************************");
        System.out.println("THIS IS ITERATION NUMBER " + iter);
        System.out.println("***************************************");

        int move;
        int length = moves.length;
        int lastMove = moves[length - 1];

        if (lastMove == 0) {
            return;
        }

        EngineBetter.rootMoves = moves;

        if (depth == 0) {
            move = EngineBetter.searchFixedTime(board, time);
        }
        else {
            move = EngineBetter.searchFixedDepth(board, depth);
        }

        System.out.println();
        System.out.println("        best move: " + MoveParser.toString(move));
        System.out.println("        with score: "  + EngineBetter.aiMoveScore);

        bestMoves[iter - 1] = move;
        bestMoveScores[iter - 1] = EngineBetter.aiMoveScore;
        
        if (iter == 1) {
            favouriteMove = move;
            favouriteMoveScore = EngineBetter.aiMoveScore;
        }

        if (print) {
            board.makeMoveAndFlipTurn(move);
            System.out.println(board);
            Evaluator.printEval(board, board.turn);
            board.unMakeMoveAndFlipTurn();
        }

        if (iter == doUntil) {
            return;
        }


        int[] newMoves = new int[length];
        newMoves[length - 1] = lastMove - 1;

        for (int i = 0; i < lastMove; i++) {
            int m = moves[i] & MOVE_MASK_WITHOUT_CHECK;
            if (m != move) {
                newMoves[i] = m;
                continue;
            }

            for (int j = i+1; j < lastMove; j++) {
                int mm = moves[j];
                newMoves[j - 1] = mm;
            }

            break;
        }


        getMoveHierarchyHelper(board, newMoves, depth, time, ++iter, doUntil, print, bestMoves, bestMoveScores);
    }
}
