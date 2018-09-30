package moveGeneration;

import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorMaster {

    public static List<Move> generateMoves(Chessboard board, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorJumping.masterMoveJumping(board, whiteTurn));
        moves.addAll(MoveGeneratorSliding.masterMoveSliding(board, whiteTurn));
        moves.addAll(MoveGeneratorPawns.masterMovePawns(board, whiteTurn));
        return moves;
    }

}
