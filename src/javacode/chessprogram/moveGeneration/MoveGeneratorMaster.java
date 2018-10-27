package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.check.CheckMoveOrganiser;
import javacode.chessprogram.check.KingLegalMoves;
import javacode.chessprogram.chess.BitExtractor;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.graphicsandui.Art;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorMaster {

    public static int numberOfChecks = 0;
    public static int numberOfCheckMates = 0;
    public static int numberOfStaleMates = 0;

    public static List<Move> generateLegalMoves(Chessboard board, boolean white) {
        List<Move> moves = generateLegalMovesHelper(board, white);

        if (moves.size() == 0) {
            long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
            int numberOfCheckers = CheckChecker.numberOfPiecesThatLegalThreatenSquare(board, white, myKing);
            if (numberOfCheckers > 0) {
//                System.out.println("Checkmate");
                numberOfCheckMates++;
            }
            else {
//                System.out.println("Stalemate");
                numberOfStaleMates++;
            }
        }
        return moves;
    }

    private static List<Move> generateLegalMovesHelper(Chessboard board, boolean white) {
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int numberOfCheckers = CheckChecker.numberOfPiecesThatLegalThreatenSquare(board, white, myKing);

        if (numberOfCheckers > 1){
            numberOfChecks++;
            return KingLegalMoves.kingLegalMovesOnly(board, white);
        }
        else if (numberOfCheckers == 1){
            numberOfChecks++;
            return CheckMoveOrganiser.evadeCheckMovesMaster(board, white);
        }

        else {
            return notInCheckMoves(board, white);
        }

    }


    private static List<Move> notInCheckMoves(Chessboard board, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();

        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        long ALL_EMPTY_SQUARES = ~board.ALL_PIECES();
        long myKing = (whiteTurn) ? board.WHITE_KING : board.BLACK_KING;
        long pinnedPieces = PinnedManager.whichPiecesArePinned(board, whiteTurn, myKing);
        long PENULTIMATE_RANK = whiteTurn ? BitBoards.RANK_SEVEN : BitBoards.RANK_TWO;
        long myPawns = whiteTurn ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        moves.addAll(MoveGeneratorCastling.generateCastlingMoves(board, whiteTurn));

        moves.addAll(KingLegalMoves.kingLegalMovesOnly(board, whiteTurn));

        if (pinnedPieces == 0){
            List<Move> regularPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                    (board, whiteTurn, promotablePawns, ALL_EMPTY_SQUARES, ENEMY_PIECES);

            moves.addAll(regularPiecesMoves);

            moves.addAll(MoveGeneratorEnPassant.generateEnPassantMoves
                    (board, whiteTurn, promotablePawns, ALL_EMPTY_SQUARES, ENEMY_PIECES));

            moves.addAll(MoveGeneratorPromotion.generatePromotionMoves
                    (board, whiteTurn, 0, ALL_EMPTY_SQUARES, ENEMY_PIECES));

            return moves;
        }

        moves.addAll(MoveGeneratorEnPassant.generateEnPassantMoves
                (board, whiteTurn, pinnedPiecesAndPromotingPawns, ALL_EMPTY_SQUARES, ENEMY_PIECES));

        moves.addAll(MoveGeneratorPromotion.generatePromotionMoves
                (board, whiteTurn, pinnedPieces, ALL_EMPTY_SQUARES, ENEMY_PIECES));
        
        List<Move> pinnedPiecesMoves = pinnedMoveManager(board, whiteTurn, pinnedPieces, myKing);
        moves.addAll(pinnedPiecesMoves);

        List<Move> unpinnedPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, whiteTurn, pinnedPiecesAndPromotingPawns, ~board.ALL_PIECES(), ENEMY_PIECES);
        moves.addAll(unpinnedPiecesMoves);

        return moves;
    }


    private static List<Move> pinnedMoveManager(Chessboard board, boolean whiteTurn,
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

        long FRIENLDY_PIECES = (whiteTurn) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();

        for (long pinnedPiece : allPinnedPieces){
            long infiniteRay = CheckMoveOrganiser.extractInfiniteRayFromTwoPieces(board, squareWeArePinnedTo, pinnedPiece);
            long pushMask = infiniteRay & ~(board.ALL_BLACK_PIECES() | board.ALL_WHITE_PIECES());
            long captureMask = infiniteRay & ENEMY_PIECES;

            if ((pinnedPiece & knights) != 0) {
                // knights cannot move cardinally or diagonally, and so cannot move while pinned
                continue;
            }
            if ((pinnedPiece & pawns) != 0) {

                long PENULTIMATE_RANK = whiteTurn ? BitBoards.RANK_SEVEN : BitBoards.RANK_TWO;
                long allButPinnedFriends = FRIENLDY_PIECES & ~pinnedPiece;
                
                if ((pinnedPiece & PENULTIMATE_RANK) == 0) {

                    long singlePawnAllPushes = PieceMovePawns.singlePawnPushes(board, pinnedPiece, whiteTurn, pushMask);
                    List<Move> pawnPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singlePawnAllPushes, pinnedPiece);
                    moves.addAll(pawnPushes);

                    long singlePawnAllCaptures = PieceMovePawns.singlePawnCaptures(board, pinnedPiece, whiteTurn, captureMask);
                    List<Move> pawnCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singlePawnAllCaptures, pinnedPiece);
                    moves.addAll(pawnCaptures);
                    
                    // a pinned pawn may still EP
                    moves.addAll(MoveGeneratorEnPassant.generateEnPassantMoves
                            (board, whiteTurn, allButPinnedFriends, pushMask, captureMask));
                }
                else {
                    // a pinned pawn may still promote, through a capture of the pinner
                    moves.addAll(MoveGeneratorPromotion.generatePromotionMoves
                            (board, whiteTurn, allButPinnedFriends, pushMask, captureMask));
                }
                continue;
            }
            if ((pinnedPiece & bishops) != 0) {
                long singleBishopsAllPushes = PieceMoveSliding.singleBishopPushes(board, pinnedPiece, whiteTurn, pushMask);
                List<Move> bishopMovesPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singleBishopsAllPushes, pinnedPiece);
                
                moves.addAll(bishopMovesPushes);

                long singleBishopAllCaptures = PieceMoveSliding.singleBishopCaptures(board, pinnedPiece, whiteTurn, captureMask);
                List<Move> bishopMovesCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singleBishopAllCaptures, pinnedPiece);
                moves.addAll(bishopMovesCaptures);
                continue;
            }
            if ((pinnedPiece & rooks) != 0) {
                long singleRookAllPushes = PieceMoveSliding.singleRookPushes(board, pinnedPiece, whiteTurn, pushMask);
                List<Move> rookMovesPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singleRookAllPushes, pinnedPiece);
                moves.addAll(rookMovesPushes);

                long singleRookAllCaptures = PieceMoveSliding.singleRookCaptures(board, pinnedPiece, whiteTurn, captureMask);
                List<Move> rookMovesCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singleRookAllCaptures, pinnedPiece);
                moves.addAll(rookMovesCaptures);
                continue;
            }
            if ((pinnedPiece & queens) != 0) {
                long singleQueenAllPushes = PieceMoveSliding.singleQueenPushes(board, pinnedPiece, whiteTurn, pushMask);
                List<Move> queenPushes = MoveGenerationUtilities.movesFromAttackBoardLong(singleQueenAllPushes, pinnedPiece);
                moves.addAll(queenPushes);

                long singleQueenAllCaptures = PieceMoveSliding.singleQueenCaptures(board, pinnedPiece, whiteTurn, captureMask);
                List<Move> queenCaptures = MoveGenerationUtilities.movesFromAttackBoardLong(singleQueenAllCaptures, pinnedPiece);
                moves.addAll(queenCaptures);
            }
        }
        return moves;
    }


}
