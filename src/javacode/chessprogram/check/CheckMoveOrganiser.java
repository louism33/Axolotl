package javacode.chessprogram.check;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.*;

import java.util.ArrayList;
import java.util.List;

import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.chess.BitIndexing.UNIVERSE;
import static javacode.chessprogram.chess.BitIndexing.getIndexOfFirstPiece;

public class CheckMoveOrganiser {

    public static List<Move> evadeCheckMovesMaster(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        long ignoreThesePieces = PinnedManager.whichPiecesArePinned(board, white, myKing);
        // if a piece in pinned to the king, it can never be used to block / capture a different checker
        return allLegalCheckEscapeMoves(board, white, ignoreThesePieces);
    }


    private static List<Move> allLegalCheckEscapeMoves(Chessboard board, boolean white, long ignoreThesePieces) {
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
            blockingSquaresMask = extractRayFromTwoPieces(board, myKing, slider) & (~slider);
            checkingPieceMask = slider;
        }
        long PENULTIMATE_RANK = white ? BitBoards.RANK_SEVEN : BitBoards.RANK_TWO;
        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long promotablePawns = myPawns & PENULTIMATE_RANK;
        long piecesToIgnoreAndPromotingPawns = ignoreThesePieces | promotablePawns;
        
        moves.addAll(MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask));

        moves.addAll(KingLegalMoves.kingLegalMovesOnly(board, white));
        
        moves.addAll(MoveGeneratorPromotion.generatePromotionMoves(board, white, ignoreThesePieces, blockingSquaresMask, checkingPieceMask));

        moves.addAll(MoveGeneratorEnPassant.generateEnPassantMoves(board, white, piecesToIgnoreAndPromotingPawns, blockingSquaresMask, checkingPieceMask));

        return moves;
    }

    private static long extractRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;
        long ALL_PIECES_TO_AVOID = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();

        ALL_PIECES_TO_AVOID ^= pieceTwo;
        ALL_PIECES_TO_AVOID ^= pieceOne;
        
        // necessary as java offers signed ints, which get confused if talking about square 63
        int indexOfPieceOne = getIndexOfFirstPiece(pieceOne);
        int indexOfPieceTwo = getIndexOfFirstPiece(pieceTwo);
        long bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
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

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
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

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;

        while (true) {
            if ((smallPiece & BitBoards.RANK_EIGHT) != 0) {
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

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
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

    public static long extractInfiniteRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;

        int indexOfPieceOne = getIndexOfFirstPiece(pieceOne);
        int indexOfPieceTwo = getIndexOfFirstPiece(pieceTwo);
        long bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        long possibleAnswer = 0;
        long answer = 0;
        
        boolean thisOne = false;
        while (true) {
            if ((smallPiece & BitBoards.FILE_A) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 1;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & BitBoards.FILE_H) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 1;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & NORTH_WEST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 9;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & SOUTH_EAST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 9;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & BitBoards.RANK_EIGHT) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 8;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & BitBoards.RANK_ONE) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 8;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & NORTH_EAST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece <<= 7;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }

        bigPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(indexOfPieceOne > indexOfPieceTwo) ? pieceTwo : pieceOne;
        possibleAnswer = 0;
        
        thisOne = false;
        while (true) {
            if ((smallPiece & SOUTH_WEST) != 0) {
                if (thisOne) {
                    answer |= possibleAnswer;
                }
                break;
            }
            smallPiece >>>= 7;
            if ((smallPiece & bigPiece) != 0) {
                thisOne = true;
            }
            possibleAnswer |= smallPiece;
        }
        return answer;
    }


    private static long inCheckByAJumper(Chessboard board, boolean white){
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

        long possiblePawn = PieceMovePawns.singlePawnCaptures(board, myKing, white, pawns);
        if (possiblePawn != 0) {
            return possiblePawn;
        }
        long possibleKnight = PieceMoveKnight.singleKnightCaptures(board, myKing, white, UNIVERSE) & knights;
        if (possibleKnight != 0) {
            return possibleKnight;
        }

        return 0;
    }

    private static long inCheckByASlider(Chessboard board, boolean white){
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

        long possibleBishop = PieceMoveSliding.singleBishopCaptures(board, myKing, white, bishops);
        if (possibleBishop != 0) {
            return possibleBishop;
        }
        long possibleRook = PieceMoveSliding.singleRookCaptures(board, myKing, white, rooks);
        if (possibleRook != 0){
            return possibleRook;
        }
        long possibleQueen = PieceMoveSliding.singleQueenCaptures(board, myKing, white, queens);
        if (possibleQueen != 0){
            return possibleQueen;
        }
        return 0;
    }

}
