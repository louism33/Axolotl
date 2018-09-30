package check;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGenerationUtilities;
import moveGeneration.PieceMoveJumping;

import java.util.ArrayList;
import java.util.List;

public class CheckMoveOrganiser {

    public static List<Move> evadeCheckMovesMaster(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        if (CheckChecker.boardInDoubleCheck(board, white)) {
            return kingLegalMovesOnly(board, white);
        }


        return allLegalCheckEscapeMoves(board, white);
    }

    public static List<Move> kingLegalMovesOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int indexOfFirstPiece = BitIndexing.getIndexOfFirstPiece(myKing);
        return MoveGenerationUtilities.movesFromAttackBoard(kingLegalTableOnly(board, white), indexOfFirstPiece);
    }

    public static long kingLegalTableOnly(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long kingSafeSquares = ~CheckUtilities.kingDangerSquares(board, white);
        long kingSafeMoves = PieceMoveJumping.kingMove(board, myKing, white) & kingSafeSquares;
        return kingSafeMoves;
    }

    static List<Move> allLegalCheckEscapeMoves(Chessboard board, boolean white) {
        List<Move> moves = new ArrayList<>();

        List<Move> kingLegalMoves = kingLegalMovesOnly(board, white);
        moves.addAll(kingLegalMoves);

        return moves;
    }



}
