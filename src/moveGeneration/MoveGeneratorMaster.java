package moveGeneration;

import bitboards.BitBoards;
import check.CheckChecker;
import check.CheckMoveOrganiser;
import check.KingLegalMoves;
import chess.BitExtractor;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorMaster {

    public static List<Move> generateLegalMoves(Chessboard board, boolean whiteTurn) {
        if (CheckChecker.boardInCheck(board, whiteTurn)){
            return CheckMoveOrganiser.evadeCheckMovesMaster(board, whiteTurn);
        }
        return notInCheckMoves(board, whiteTurn);
    }


    private static List<Move> notInCheckMoves(Chessboard board, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();

        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        long myKing = (whiteTurn) ? board.WHITE_KING : board.BLACK_KING;
        long pinnedPieces = PinnedManager.whichPiecesArePinned(board, whiteTurn, myKing);

        moves.addAll(MoveGeneratorCastling.generateCastlingMoves(board, whiteTurn));
        moves.addAll(MoveGeneratorPromotion.generatePromotionMoves(board, whiteTurn, pinnedPieces));
        moves.addAll(MoveGeneratorEnPassant.generateEnPassantMoves(board, whiteTurn, pinnedPieces));
        moves.addAll(KingLegalMoves.kingLegalMovesOnly(board, whiteTurn));


        long PENULTIMATE_RANK = whiteTurn ? BitBoards.RANK_SEVEN : BitBoards.RANK_TWO;
        long myPawns = whiteTurn ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        if (pinnedPieces == 0){
            List<Move> regularPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                    (board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.ALL_PIECES(), ENEMY_PIECES);
            moves.addAll(regularPiecesMoves);
            return moves;
        }

        moves.addAll(pinnedMoveManager(board, whiteTurn, pinnedPieces, myKing));

        List<Move> unpinnedPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.ALL_PIECES(), ENEMY_PIECES);

        moves.addAll(unpinnedPiecesMoves);

        return moves;
    }


    public static List<Move> pinnedMoveManager(Chessboard board, boolean whiteTurn,
                                               long pinnedPieces, long squareWeArePinnedTo){
        List<Move> moves = new ArrayList<>();
        List<Long> allPinnedPieces = BitExtractor.getAllPieces(pinnedPieces, 0);

        long ans = 0, pawns, knights, bishops, rooks, queens;
        if (whiteTurn){
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

        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        for (long pinnedPiece : allPinnedPieces){

            long infiniteRay = CheckMoveOrganiser.extractInfiniteRayFromTwoPieces(board, squareWeArePinnedTo, pinnedPiece);
            long pushMask = infiniteRay & ~(board.ALL_BLACK_PIECES() | board.ALL_WHITE_PIECES());
            long captureMask = infiniteRay & ENEMY_PIECES;

            if ((pinnedPiece & knights) != 0) {
                break;
            }
            if ((pinnedPiece & pawns) != 0) {
                long singlePawnAllMoves = PieceMovePawns.singlePawnAllMoves(board, pinnedPiece, whiteTurn, pushMask, captureMask);
                List<Move> pawnMoves = MoveGenerationUtilities.movesFromAttackBoardLong(singlePawnAllMoves, pinnedPiece);
                moves.addAll(pawnMoves);
                break;
            }
            if ((pinnedPiece & bishops) != 0) {
                long singleBishopAllMoves = PieceMoveSliding.singleBishopAllMoves(board, pinnedPiece, whiteTurn, pushMask, captureMask);
                List<Move> bishopMoves = MoveGenerationUtilities.movesFromAttackBoardLong(singleBishopAllMoves, pinnedPiece);
                moves.addAll(bishopMoves);
                break;
            }
            if ((pinnedPiece & rooks) != 0) {
                long singleRookAllMoves = PieceMoveSliding.singleRookAllMoves(board, pinnedPiece, whiteTurn, pushMask, captureMask);
                List<Move> rookMoves = MoveGenerationUtilities.movesFromAttackBoardLong(singleRookAllMoves, pinnedPiece);
                moves.addAll(rookMoves);
                break;
            }
            if ((pinnedPiece & queens) != 0) {
                long singleQueenAllMoves = PieceMoveSliding.singleQueenAllMoves(board, pinnedPiece, whiteTurn, pushMask, captureMask);
                List<Move> queenMoves = MoveGenerationUtilities.movesFromAttackBoardLong(singleQueenAllMoves, pinnedPiece);
                moves.addAll(queenMoves);
                break;
            }
        }
        return moves;
    }


}
