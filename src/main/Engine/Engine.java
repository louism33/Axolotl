package main.Engine;

import main.Evaluator.Evaluator;
import main.chess.Chessboard;
import main.chess.Copier;
import main.chess.Move;
import main.moveGeneration.MoveGeneratorMaster;
import main.moveMaking.MoveOrganiser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Engine {

    Chessboard board;

    public Move search (Chessboard board, long timeLimit){
        List<Move> moves = new ArrayList<>();

        Move aiMove = aspirationSearch(board, timeLimit);

        return aiMove;
    }


    private Move aspirationSearch(Chessboard board, long timeLimit){
        List<Move> possibleMoves = MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn());
        List<Integer> scores = new ArrayList<>();


        for (Move move : possibleMoves){
            // makeMove

            Chessboard babyBoard = Copier.copyBoard(board, board.isWhiteTurn(), false);
            MoveOrganiser.makeMoveMaster(babyBoard, move);
            babyBoard.setWhiteTurn(!board.isWhiteTurn());
            int movesAtDepth = Evaluator.eval(babyBoard, board.isWhiteTurn());
            scores.add(movesAtDepth);

            //unmakeMove
        }

        int max = Collections.max(scores);
        int maxIndex = scores.indexOf(max);

        return possibleMoves.get(maxIndex);
    }





    public static Move randomMove (Chessboard board, List<Move> moves){
        Random r = new Random();
        int i = r.nextInt(moves.size());
        return moves.get(i);
    }
}
