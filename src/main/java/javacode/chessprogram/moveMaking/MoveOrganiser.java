package javacode.chessprogram.moveMaking;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.moveMaking.StackMoveData.SpecialMove.*;

public class MoveOrganiser {

    public static void flipTurn(Chessboard board){
        board.setWhiteTurn(!board.isWhiteTurn());
    }

    public static int captures = 0;
    public static int promotions = 0;
    public static int castlings = 0;
    public static int epNum = 0;
    
    public static void makeMoveMaster(Chessboard board, Move move) {

        if (MoveParser.isSpecialMove(move)){
            if (MoveParser.isCastlingMove(move)) {
                castlings++;
                StackMoveData stackMoveData = new StackMoveData(move, board, 50, CASTLING);
                board.moveStack.push(stackMoveData);
                MoveCastling.makeCastlingMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }

            else if (MoveParser.isEnPassantMove(move)){
                epNum++;
                StackMoveData stackMoveData = new StackMoveData
                        (move, board, 50, ENPASSANTCAPTURE);
                board.moveStack.push(stackMoveData);
                MoveEnPassant.makeEnPassantMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }

            else if (MoveParser.isPromotionMove(move)){
                int destination = move.destinationIndex;
                long destSquare = newPieceOnSquare(destination);
                boolean capturePromotion = (destSquare & board.ALL_PIECES()) != 0;
                if (capturePromotion) {
                    long destinationPiece = newPieceOnSquare(move.destinationIndex);
                    int takenPiece = whichPieceOnSquare(board, destinationPiece);

                    promotions++;
                    captures++;
                    StackMoveData stackMoveData = new StackMoveData(move, board, 50, PROMOTION, takenPiece);
                    board.moveStack.push(stackMoveData);
                    MovePromotion.makePromotingMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }
                else {
                    promotions++;
                    StackMoveData stackMoveData = new StackMoveData(move, board, 50, PROMOTION);
                    board.moveStack.push(stackMoveData);
                    MovePromotion.makePromotingMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }

            }
        }


        else {
            int destination = move.destinationIndex;
            long destSquare = newPieceOnSquare(destination);
            boolean captureMove = (destSquare & board.ALL_PIECES()) != 0;
            if (captureMove) {
                captures++;
                long destinationPiece = newPieceOnSquare(move.destinationIndex);
                int takenPiece = whichPieceOnSquare(board, destinationPiece);
                StackMoveData stackMoveData = new StackMoveData
                        (move, board, 50, BASICCAPTURE, takenPiece);
                board.moveStack.push(stackMoveData);
                MoveRegular.makeRegularMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }
            
            else if (enPassantPossibility(board, move)){
                int sourceAsPiece = move.getSourceAsPieceIndex();
                int whichFile = 8 - sourceAsPiece % 8;
                StackMoveData stackMoveData = new StackMoveData
                        (move, board, 50, whichFile, ENPASSANTVICTIM);
                board.moveStack.push(stackMoveData);
                MoveRegular.makeRegularMove(board, move);
                MoveCastling.castleFlagManager(board, move);
            }

            else {
                long destinationPiece = newPieceOnSquare(move.getSourceAsPieceIndex());
                int movingPiece = whichPieceOnSquare(board, destinationPiece);
                if (movingPiece == 1 || movingPiece == 7){
                    StackMoveData stackMoveData = new StackMoveData
                            (move, board, 50, BASICLOUDPUSH);
                    board.moveStack.push(stackMoveData);
                    MoveRegular.makeRegularMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }
                else {
                    // increment 50 move rule
                    StackMoveData stackMoveData = new StackMoveData
                            (move, board, 50, BASICQUIETPUSH);
                    board.moveStack.push(stackMoveData);
                    MoveRegular.makeRegularMove(board, move);
                    MoveCastling.castleFlagManager(board, move);
                }
            }
        }
    }

    private static boolean enPassantPossibility(Chessboard board, Move move){
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(move.getSourceAsPieceIndex());
        long destinationSquare = newPieceOnSquare(move.destinationIndex);
        long HOME_RANK = (board.isWhiteTurn()) ? BitBoards.RANK_TWO : BitBoards.RANK_SEVEN;
        long MY_PAWNS = (board.isWhiteTurn()) ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enPassantPossibilityRank = (board.isWhiteTurn()) ? BitBoards.RANK_FOUR : BitBoards.RANK_FIVE;

        if ((sourceSquare & HOME_RANK) == 0){
            return false;
        }

        if ((sourceSquare & MY_PAWNS) == 0){
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }


    public static int whichPieceOnSquare(Chessboard board, long destinationPiece){

        if ((destinationPiece & board.ALL_PIECES()) == 0){
            return 0;
        }

        if ((destinationPiece & board.WHITE_PAWNS) != 0){
            return 1;
        }
        else if ((destinationPiece & board.WHITE_KNIGHTS) != 0){
            return 2;
        }
        else if ((destinationPiece & board.WHITE_BISHOPS) != 0){
            return 3;
        }
        else if ((destinationPiece & board.WHITE_ROOKS) != 0){
            return 4;
        }
        else if ((destinationPiece & board.WHITE_QUEEN) != 0){
            return 5;
        }
        else if ((destinationPiece & board.WHITE_KING) != 0){
            return 6;
        }

        else if ((destinationPiece & board.BLACK_PAWNS) != 0){
            return 7;
        }
        else if ((destinationPiece & board.BLACK_KNIGHTS) != 0){
            return 8;
        }
        else if ((destinationPiece & board.BLACK_BISHOPS) != 0){
            return 9;
        }
        else if ((destinationPiece & board.BLACK_ROOKS) != 0){
            return 10;
        }
        else if ((destinationPiece & board.BLACK_QUEEN) != 0){
            return 11;
        }
        else if ((destinationPiece & board.BLACK_KING) != 0) {
            return 12;
        }
        else {
            throw new RuntimeException("false entry");
        }
    }

}
