package moveGeneration;

import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorPseudo {


    public static List<Move> generatePseudoMoves(Chessboard board, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorJumping.masterMoveJumping(board, whiteTurn));
        moves.addAll(MoveGeneratorSliding.masterMoveSliding(board, whiteTurn));
        moves.addAll(MoveGeneratorPawns.masterMovePawns(board, whiteTurn));
        return moves;
    }

    public static long generatePseudoPushTable(Chessboard board, boolean whiteTurn){
        long ans = 0;
        ans |= PieceMoveJumping.masterAttackTableJumping(board, whiteTurn);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn);
        ans |= PieceMovePawns.masterPawnPushesTable(board, whiteTurn);
        return ans;
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn){
        long ans = 0;
        ans |= PieceMoveJumping.masterAttackTableJumping(board, whiteTurn);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn);
        ans |= PieceMovePawns.masterPawnCapturesTable(board, whiteTurn);
        return ans;
    }



}
