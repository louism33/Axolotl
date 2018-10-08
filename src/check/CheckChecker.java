package check;

import chess.BitIndexing;
import chess.Chessboard;
import moveGeneration.*;

public class CheckChecker {


    public static boolean boardInCheck(Chessboard board, boolean white){
        /*
        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        List<Move> regularPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, whiteTurn, ~board.ALL_PIECES(), ENEMY_PIECES);

         */

        /*
        List<Move> unpinnedPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.ALL_PIECES(), ENEMY_PIECES);

         */

        //todo: would I be checked by a pawn that is on penultimate rank?

        //todo: uses old functions
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long enemyKing = (!white) ? board.WHITE_KING : board.BLACK_KING;


        // todo: currently pinned pieces cannot check us along their legal pinned squares
        long pinnedPieces = PinnedManager.whichPiecesArePinned(board, !white, enemyKing);
        long pseudoTable = MoveGeneratorPseudo.generatePseudoCaptureTable(board, !white,
                pinnedPieces, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE);

        return (myKing & pseudoTable) != 0;
    }

    public static boolean boardInDoubleCheck(Chessboard board, boolean white){
        long ans = 0, pawns, knights, bishops, rooks, queens, myKing;
        if (!white){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
            myKing = board.WHITE_KING;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
            myKing = board.BLACK_KING;
        }

        int numberOfCheckers = 0;

        if ((PieceMovePawns.singlePawnCaptures(board, myKing, white, pawns)) != 0) numberOfCheckers++;
        if ((PieceMoveKnight.singleKnightCaptures(board, myKing, white, knights)) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.singleBishopAllMoves(board, myKing, white, bishops, bishops)) != 0) numberOfCheckers++;
        if ((PieceMoveSliding.singleRookAllMoves(board, myKing, white, rooks, rooks)) != 0) numberOfCheckers++;

        long queenAttacks = PieceMoveSliding.singleQueenAllMoves(board, myKing, white, queens, queens);
        if (queenAttacks != 0) numberOfCheckers += BitIndexing.populationCount(queenAttacks);

        return numberOfCheckers > 1;
    }

}
