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

    public static int numberOfChecks = 0;
    public static int numberOfCheckMates = 0;
    public static int numberOfStaleMates = 0;

    public static List<Move> generateLegalMoves(Chessboard board, boolean whiteTurn) {

        if (CheckChecker.boardInCheck(board, whiteTurn)){
            numberOfChecks++;
            List<Move> checkEscapeMoves = CheckMoveOrganiser.evadeCheckMovesMaster(board, whiteTurn);

            if (checkEscapeMoves.size() == 0){
                numberOfCheckMates++;
//                System.out.println("CHECKMATE");
            }
            return checkEscapeMoves;
        }

        List<Move> moves = notInCheckMoves(board, whiteTurn);
        if (moves.size() == 0){
            numberOfStaleMates++;
//            System.out.println("Stalemate");
        }

        return moves;
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
                long singleBishopsAllPushes = PieceMoveSliding.singleBishopPushes(board, pinnedPiece, whiteTurn, pushMask);
                List<Move> bishopMovesPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singleBishopsAllPushes, pinnedPiece);
                moves.addAll(bishopMovesPushes);

                long singleBishopAllCaptures = PieceMoveSliding.singleBishopCaptures(board, pinnedPiece, whiteTurn, captureMask);
                List<Move> bishopMovesCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singleBishopAllCaptures, pinnedPiece);
                moves.addAll(bishopMovesCaptures);
                break;
            }
            if ((pinnedPiece & rooks) != 0) {
                long singleRookAllPushes = PieceMoveSliding.singleRookPushes(board, pinnedPiece, whiteTurn, pushMask);
                List<Move> rookMovesPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singleRookAllPushes, pinnedPiece);
                moves.addAll(rookMovesPushes);

                long singleRookAllCaptures = PieceMoveSliding.singleRookCaptures(board, pinnedPiece, whiteTurn, captureMask);
                List<Move> rookMovesCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singleRookAllCaptures, pinnedPiece);
                moves.addAll(rookMovesCaptures);
                break;
            }
            if ((pinnedPiece & queens) != 0) {
                long singleQueenAllPushes = PieceMoveSliding.singleQueenPushes(board, pinnedPiece, whiteTurn, pushMask);
                List<Move> queenPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singleQueenAllPushes, pinnedPiece);
                moves.addAll(queenPushes);

                long singleQueenAllCaptures = PieceMoveSliding.singleQueenCaptures(board, pinnedPiece, whiteTurn, captureMask);
                List<Move> queenCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singleQueenAllCaptures, pinnedPiece);
                moves.addAll(queenCaptures);
                break;
            }
        }
        return moves;
    }


}
