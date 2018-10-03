package check;

import bitboards.BitBoards;
import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGeneratorPseudo;
import moveGeneration.PieceMoveKnight;
import moveGeneration.PieceMovePawns;
import moveGeneration.PieceMoveSliding;

import java.util.ArrayList;
import java.util.List;

import static chess.BitIndexing.UNIVERSE;

public class CheckMoveOrganiser {

    public static List<Move> evadeCheckMovesMaster(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        if (CheckChecker.boardInDoubleCheck(board, white)) {
            return KingLegalMoves.kingLegalMovesOnly(board, white);
        }

        return allLegalCheckEscapeMoves(board, white);
    }


    static List<Move> allLegalCheckEscapeMoves(Chessboard board, boolean white) {
        List<Move> moves = new ArrayList<>();

        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long blockingSquaresMask, checkingPieceMask;

        long jumper = inCheckByAJumper(board, white);
        if (jumper != 0){
            blockingSquaresMask = 0;
            checkingPieceMask = jumper;
        }
        else {
            long slider = inCheckByASlider(board, white);
            blockingSquaresMask = extractRayFromTwoPieces(board, myKing, slider);
            checkingPieceMask = slider;
        }

        List<Move> restrictedMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing(board, white, blockingSquaresMask, checkingPieceMask);
        moves.addAll(restrictedMoves);

        List<Move> kingLegalMoves = KingLegalMoves.kingLegalMovesOnly(board, white);
        moves.addAll(kingLegalMoves);

        return moves;
    }




    public static long extractRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){

        if (pieceOne == pieceTwo) return 0;

        long ALL_PIECES_TO_AVOID = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES(),
                NORTH_WEST = BitBoards.FILE_A | BitBoards.RANK_EIGHT,
                NORTH_EAST = BitBoards.FILE_H | BitBoards.RANK_EIGHT,
                SOUTH_WEST = BitBoards.FILE_A | BitBoards.RANK_ONE,
                SOUTH_EAST = BitBoards.FILE_H | BitBoards.RANK_ONE;

        ALL_PIECES_TO_AVOID ^= pieceTwo;
        ALL_PIECES_TO_AVOID ^= pieceOne;

        long bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
        long possibleAnswer = 0;

        while (true) {
            if ((smallPiece & BitBoards.FILE_A) != 0) {
                break;
            }
            smallPiece <<= 1;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & NORTH_WEST) != 0) {
                break;
            }
            smallPiece <<= 9;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & BitBoards.FILE_H) != 0) {
                break;
            }
            smallPiece <<= 8;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & NORTH_EAST) != 0) {
                break;
            }
            smallPiece <<= 7;
            if ((smallPiece & ALL_PIECES_TO_AVOID) != 0) {
                break;
            }
            if ((smallPiece & bigPiece) != 0) {
                return possibleAnswer;
            }
            possibleAnswer |= smallPiece;
        }

        return 0;
    }








    static long inCheckByAJumper(Chessboard board, boolean white){
        long ans = 0, pawns, knights;
        if (!white){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
        }
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;

        long possiblePawn = PieceMovePawns.singlePawnCaptures(board, myKing, white, UNIVERSE) & pawns;
        if (possiblePawn != 0) {
            return possiblePawn;
        }
        long possibleKnight = PieceMoveKnight.singleKnightCaptures(board, myKing, white, UNIVERSE) & knights;
        if (possibleKnight != 0) {
            return possibleKnight;
        }

        return 0;
    }

    static long inCheckByASlider(Chessboard board, boolean white){
        long ans = 0, bishops, rooks, queens;
        if (!white){
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
        }
        else {
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
        }
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;

        long possibleBishop = PieceMoveSliding.singleBishopAllMoves(board, myKing, white, UNIVERSE, UNIVERSE) & bishops;
        if (possibleBishop != 0) {
            return possibleBishop;
        }
        long possibleRook = PieceMoveSliding.singleRookAllMoves(board, myKing, white, UNIVERSE, UNIVERSE) & rooks;
        if (possibleRook != 0){
            return possibleRook;
        }
        long possibleQueen = PieceMoveSliding.singleQueenAllMoves(board, myKing, white, UNIVERSE, UNIVERSE) & queens;
        if (possibleQueen != 0){
            return possibleQueen;
        }

        return 0;
    }



}
