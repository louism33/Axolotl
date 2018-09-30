package check;

import chess.Chessboard;
import moveGeneration.MoveGeneratorPseudo;
import moveGeneration.PieceMoveJumping;
import moveGeneration.PieceMovePawns;
import moveGeneration.PieceMoveSliding;

public class CheckChecker {

    public static boolean boardInCheck(Chessboard board, boolean white){
        long pseudoTable = MoveGeneratorPseudo.generatePseudoCaptureTable(board, !white);
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        return (myKing & pseudoTable) != 0;
    }

    public static boolean boardInDoubleCheck(Chessboard board, boolean white){
        long ans = 0, pawns, knights, bishops, rooks, queens;
        if (!white){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
        }
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int numberOfCheckers = 0;
        if ((PieceMovePawns.pawnCaptures(board, myKing, white) & pawns) != 0) numberOfCheckers++;
        if ((PieceMoveJumping.knightMove(board, myKing, white) & knights) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.bishopSlidingMove(board, myKing, white) & bishops) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.rookSlidingMove(board, myKing, white) & rooks) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.queenSlidingMove(board, myKing, white) & queens) != 0) numberOfCheckers++;
        return numberOfCheckers > 1;
    }

}
