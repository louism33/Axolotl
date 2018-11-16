package javacode.chessprogram.miscAdmin;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.graphicsandui.Art;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveUnmaker;

import java.util.ArrayList;
import java.util.List;

public class DetailedPerftSearching {

    class MoveNLong {
        final long number;
        final Move move;

        MoveNLong(long number, Move move) {
            this.number = number;
            this.move = move;
        }

        @Override
        public String toString() {
            return "\nMoveNLong{" +
                    "move=" + move +
                    ", number=" + number +
                    '}';
        }
    }
    private List<MoveNLong> childrenNumbersAndMoves(Chessboard board, int depth){
        List<MoveNLong> moveNLongs = new ArrayList<>();
        System.out.println(Art.boardArt(board));

        List<Long> longs = sumOfChildrenStartingAt(board, depth);
//        System.out.println(longs);
        long total = sumList(longs);

        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
//        System.out.println(moves);

        for (int i = 0; i < longs.size(); i++) {
            Long l = longs.get(i);
            MoveNLong m = new MoveNLong(longs.get(i), moves.get(i));

            moveNLongs.add(m);
        }
        System.out.println(total);
        System.out.println("------------");

        return moveNLongs;
    }

    private static List<Long> sumOfChildrenStartingAt(Chessboard board, int d){
        List<Long> movesByMove = new ArrayList<>();
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        for (Move move : moves) {
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            long l = countFinalNodesAtDepthHelper(board, d - 1);
            movesByMove.add(l);
//            System.out.println("move number : " + m +", move: "+ move +" , number of kids: " + l);
            MoveUnmaker.unMakeMoveMaster(board);
        }
        return movesByMove;
    }

    private static List<Long> oddOnesOut(List<Long> l1, List<Long> l2){
        List<Long> odds = new ArrayList<>();

        List<Long> listOne = new ArrayList<>(l1);
        List<Long> listTwo = new ArrayList<>(l2);

        for (Long l : listOne) {
            listTwo.remove(l);
        }
        return listTwo;

    }

    private static long sumList(List<Long> movesByMove){
        long total = 0;
        for (Long l : movesByMove){
            total += l;
        }
        return total;
    }

    public static long runPerftTestWithBoard(int d, Chessboard board, long correctAnswer){
        String s = Art.boardArt(board);
        System.out.println(s);
        System.out.println("-----------------------------------");
        System.out.println("Correct Number of nodes at depth " + d + ": "+ correctAnswer);
        System.out.println("-----------------------------------");
        return runPerftTestWithBoard(d, board);
    }

    public static long runPerftTestWithBoardLong(int d, Chessboard board, long correctAnswer){
        String s = Art.boardArt(board);
        System.out.println(s);
        System.out.println("-----------------------------------");
        System.out.println("Correct Number of nodes at depth " + d + ": "+ correctAnswer);
        System.out.println("-----------------------------------");
        return runPerftTestWithBoard(d, board);
    }

    private static long runPerftTestWithBoard(int d, Chessboard board){
        int maxD = d > 0 ? d : 6;
//        for (int depth = 1; depth <= maxD; depth++) {
//            countFinalNodesAtDepth(board, depth);
//            System.out.println();
//            System.out.println("-----");
//            reset();
//        }
        reset();
        return countFinalNodesAtDepth(board, maxD);
    }

    private static long countFinalNodesAtDepth(Chessboard board, int depth) {
        long t1 = System.currentTimeMillis();
        long ii = DetailedPerftSearching.countFinalNodesAtDepthHelper(board, depth);
        System.out.println("Final Nodes at Depth " + depth + ": " + ii);
        System.out.println("--previous checks: " + MoveGeneratorMaster.numberOfChecks);
        System.out.println("--checkmates: " + MoveGeneratorMaster.numberOfCheckMates);
        System.out.println("--stalemates: " + MoveGeneratorMaster.numberOfStaleMates);
        System.out.println("--eps: " + MoveOrganiser.epNum);
        System.out.println("--previous captures: " + MoveOrganiser.captures);
        System.out.println("--previous castlings: " + MoveOrganiser.castlings);
        System.out.println("--previous promotions: " + MoveOrganiser.promotions);
        long t2 = System.currentTimeMillis();
        long t = t2 - t1;
        long seconds = t / 1000;
        System.out.println("Depth " + depth + " took " + seconds + " seconds (" + t+" milliseconds).");
        if (t > 0) {
            System.out.println("Veeeery roughly " + (ii / t) * 1000 + " (final) nodes per second.");
        }
        return ii;
    }

    private static long countFinalNodesAtDepthHelper(Chessboard board, int depth){
        long temp = 0;
        if (depth == 0){
            return 1;
        }
        List<Move> moves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        if (depth == 1){
            return moves.size();
        }
        for (Move move : moves) {
            MoveOrganiser.makeMoveMaster(board, move);
            MoveOrganiser.flipTurn(board);
            long movesAtDepth = countFinalNodesAtDepthHelper(board, depth - 1);
            temp += movesAtDepth;
            MoveUnmaker.unMakeMoveMaster(board);
        }
        return temp;
    }

    private static void reset(){
        MoveGeneratorMaster.numberOfChecks = 0;
        MoveGeneratorMaster.numberOfCheckMates = 0;
        MoveGeneratorMaster.numberOfStaleMates = 0;
        MoveOrganiser.epNum = 0;
        MoveOrganiser.captures = 0;
        MoveOrganiser.castlings = 0;
        MoveOrganiser.promotions = 0;
    }
}
