package check;

import chess.BitIndexing;
import chess.Chessboard;
import moveGeneration.MoveGeneratorPseudo;
import moveGeneration.PieceMoveKnight;
import moveGeneration.PieceMovePawns;
import moveGeneration.PieceMoveSliding;

public class CheckChecker {

    public static boolean boardInCheck(Chessboard board, boolean white){
        /*
        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        List<Move> regularPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, whiteTurn, ~board.ALL_PIECES(), ENEMY_PIECES);

         */
        //todo: uses old functions


        long pseudoTable = MoveGeneratorPseudo.generatePseudoCaptureTable(board, !white,
                0, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE);
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
        if ((PieceMovePawns.singlePawnCaptures(board, myKing, white, BitIndexing.UNIVERSE) & pawns) != 0) numberOfCheckers++;
        if ((PieceMoveKnight.singleKnightCaptures(board, myKing, white, BitIndexing.UNIVERSE) & knights) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.singleBishopAllMoves(board, myKing, white, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE) & bishops) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.singleRookAllMoves(board, myKing, white, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE) & rooks) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.singleQueenAllMoves(board, myKing, white, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE) & queens) != 0) numberOfCheckers++;
        return numberOfCheckers > 1;
    }

}
