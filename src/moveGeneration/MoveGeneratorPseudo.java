package moveGeneration;

import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorPseudo {


    public static List<Move> generatePseudoMoves(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorJumping.masterMoveJumping(board, whiteTurn, legalPushes, legalCaptures));
        moves.addAll(MoveGeneratorSliding.masterMoveSliding(board, whiteTurn, legalPushes, legalCaptures));
        moves.addAll(MoveGeneratorPawns.masterMovePawns(board, whiteTurn, legalPushes, legalCaptures));
        return moves;
    }

    public static long generatePseudoPushTable(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        long ans = 0;
        ans |= PieceMoveJumping.masterAttackTableJumping(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMovePawns.masterPawnPushesTable(board, whiteTurn, legalPushes, legalCaptures);
        return ans;
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        long ans = 0;
        ans |= PieceMoveJumping.masterAttackTableJumping(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMovePawns.masterPawnCapturesTable(board, whiteTurn, legalPushes, legalCaptures);
        return ans;
    }



}
