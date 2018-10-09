package check;

import bitboards.BitBoards;
import chess.Chessboard;
import chess.Move;
import moveGeneration.*;

import java.util.ArrayList;
import java.util.List;

import static bitboards.BitBoards.*;
import static chess.BitIndexing.UNIVERSE;

public class CheckMoveOrganiser {

    public static List<Move> evadeCheckMovesMaster(Chessboard board, boolean white){
        // can be combined with first checkchecker
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;

        int numberOfCheckers = CheckChecker.numberOfPiecesThatLegalThreatenSquare(board, white, myKing);

        if (numberOfCheckers > 1){
            return KingLegalMoves.kingLegalMovesOnly(board, white);
        }

        //can this be done earlier ? should it ?
        long ignoreThesePieces = PinnedManager.whichPiecesArePinned(board, white, myKing);
        return allLegalCheckEscapeMoves(board, white, ignoreThesePieces);
    }


    static List<Move> allLegalCheckEscapeMoves(Chessboard board, boolean white, long ignoreThesePieces) {
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
            // are we counting the capture of the slider twice ?
//            blockingSquaresMask = extractRayFromTwoPieces(board, myKing, slider);
            blockingSquaresMask = extractRayFromTwoPieces(board, myKing, slider) & (~slider);

            checkingPieceMask = slider;
        }

        List<Move> restrictedMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, white, ignoreThesePieces, blockingSquaresMask, checkingPieceMask);
        moves.addAll(restrictedMoves);

        List<Move> kingLegalMoves = KingLegalMoves.kingLegalMovesOnly(board, white);
        moves.addAll(kingLegalMoves);




        //todo this should be in restricted moves
        //todo refactor all this movegen into one function. Most functions should only be used once
//        moves.addAll(MoveGeneratorPromotion.generatePromotionMoves(board, white, ignoreThesePieces));
//        moves.addAll(MoveGeneratorEnPassant.generateEnPassantMoves(board, white, ignoreThesePieces));



        return moves;
    }




    public static long extractRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;
        long ALL_PIECES_TO_AVOID = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();

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






    public static long extractInfiniteRayFromTwoPieces(Chessboard board, long pieceOne, long pieceTwo){
        if (pieceOne == pieceTwo) return 0;
        long bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        long smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
        long answer = 0, possibleAnswer = 0;
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



        bigPiece = !(pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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





        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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

        bigPiece = !(pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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



        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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



        bigPiece = !(pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = !(pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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




        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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

        bigPiece = (pieceOne > pieceTwo) ? pieceOne : pieceTwo;
        smallPiece = (pieceOne > pieceTwo) ? pieceTwo : pieceOne;
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
