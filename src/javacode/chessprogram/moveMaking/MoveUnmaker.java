package javacode.chessprogram.moveMaking;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.graphicsandui.Art;

public class MoveUnmaker {

    public static void unMakeMoveMaster(Chessboard board) {
        StackMoveData popSMD = board.moveStack.pop();
        int pieceToMoveBack = popSMD.move.destination;
        int squareToMoveBackTo = popSMD.move.getSourceAsPiece();

        if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.BASICQUIETPUSH){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }
        
        else if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.BASICLOUDPUSH){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.BASICCAPTURE){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = popSMD.takenPiece;
            if (takenPiece != 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
        }

        //double pawn push
        else if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.ENPASSANTVICTIM){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
        }

        else if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.ENPASSANTCAPTURE){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);
            makeRegularMove(board, basicReversedMove);
            int takenPiece = popSMD.takenPiece;
            
            if (popSMD.whiteTurn) {
                addRelevantPieceToSquare(board, 7, pieceToMoveBack - 8);
            }
            else {
                addRelevantPieceToSquare(board, 1, pieceToMoveBack + 8);
            }
        }
        
        else if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.CASTLING){
            Move basicReversedMove = new Move(pieceToMoveBack, squareToMoveBackTo);

            if (pieceToMoveBack == 1){
                long originalKing = BitManipulations.newPieceOnSquare(squareToMoveBackTo);
                long originalRook = BitManipulations.newPieceOnSquare(0);
                long newRook = BitManipulations.newPieceOnSquare(pieceToMoveBack + 1);
                long newKing = BitManipulations.newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.WHITE_KING |= originalKing;
                board.WHITE_ROOKS |= originalRook;
            }

            else if (pieceToMoveBack == 5){
                long originalKing = BitManipulations.newPieceOnSquare(squareToMoveBackTo);
                long originalRook = BitManipulations.newPieceOnSquare(7);
                long newRook = BitManipulations.newPieceOnSquare(pieceToMoveBack - 1);
                long newKing = BitManipulations.newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.WHITE_KING |= originalKing;
                board.WHITE_ROOKS |= originalRook;
            }

            else if (pieceToMoveBack == 57){
                long originalKing = BitManipulations.newPieceOnSquare(squareToMoveBackTo);
                long originalRook = BitManipulations.newPieceOnSquare(56);
                long newRook = BitManipulations.newPieceOnSquare(pieceToMoveBack + 1);
                long newKing = BitManipulations.newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.BLACK_KING |= originalKing;
                board.BLACK_ROOKS |= originalRook;
            }

            else if (pieceToMoveBack == 61){
                long originalKing = BitManipulations.newPieceOnSquare(squareToMoveBackTo);
                long originalRook = BitManipulations.newPieceOnSquare(63);
                long newRook = BitManipulations.newPieceOnSquare(pieceToMoveBack - 1);
                long newKing = BitManipulations.newPieceOnSquare(pieceToMoveBack);

                MoveMakingUtilities.removePieces(board, newKing, newRook);
                board.BLACK_KING |= originalKing;
                board.BLACK_ROOKS |= originalRook;
            }
            
        }
        
        else if (popSMD.typeOfSpecialMove == StackMoveData.SpecialMove.PROMOTION){
            long sourceSquare = BitManipulations.newPieceOnSquare(pieceToMoveBack);
            long destinationSquare = BitManipulations.newPieceOnSquare(squareToMoveBackTo);
            MoveMakingUtilities.removePieces(board, sourceSquare, destinationSquare);
            if (popSMD.whiteTurn) {
                addRelevantPieceToSquare(board, 1, squareToMoveBackTo);
            }
            else {
                addRelevantPieceToSquare(board, 7, squareToMoveBackTo);
            }
            int takenPiece = popSMD.takenPiece;
            if (takenPiece > 0){
                addRelevantPieceToSquare(board, takenPiece, pieceToMoveBack);
            }
        }

        board.whiteCanCastleK = popSMD.whiteCanCastleK;
        board.whiteCanCastleQ = popSMD.whiteCanCastleQ;
        board.blackCanCastleK = popSMD.blackCanCastleK;
        board.blackCanCastleQ = popSMD.blackCanCastleQ;

        board.setWhiteTurn(popSMD.whiteTurn);
    }


    private static void addRelevantPieceToSquare(Chessboard board, int pieceToAdd, int placeToAddIt){
        long placeToAddPiece = BitManipulations.newPieceOnSquare(placeToAddIt);

        if (pieceToAdd == 1){
            board.WHITE_PAWNS |= placeToAddPiece;
        }
        else if (pieceToAdd == 2){
            board.WHITE_KNIGHTS |= placeToAddPiece;
        }
        else if (pieceToAdd == 3){
            board.WHITE_BISHOPS |= placeToAddPiece;
        }
        else if (pieceToAdd == 4){
            board.WHITE_ROOKS |= placeToAddPiece;
        }
        else if (pieceToAdd == 5){
            board.WHITE_QUEEN |= placeToAddPiece;
        }
        else if (pieceToAdd == 6){
            board.WHITE_KING |= placeToAddPiece;
        }

        else if (pieceToAdd == 7){
            board.BLACK_PAWNS |= placeToAddPiece;
        }
        else if (pieceToAdd == 8){
            board.BLACK_KNIGHTS |= placeToAddPiece;
        }
        else if (pieceToAdd == 9){
            board.BLACK_BISHOPS |= placeToAddPiece;
        }
        else if (pieceToAdd == 10){
            board.BLACK_ROOKS |= placeToAddPiece;
        }
        else if (pieceToAdd == 11){
            board.BLACK_QUEEN |= placeToAddPiece;
        }
        else if (pieceToAdd == 12){
            board.BLACK_KING |= placeToAddPiece;
        }
        else {
            throw new RuntimeException("problem with putting back a captured piece");
        }
    }


    private static void makeRegularMove(Chessboard board, Move move){
        long sourcePiece = BitManipulations.newPieceOnSquare(move.getSourceAsPiece());
        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);

        if ((sourcePiece & board.WHITE_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_PAWNS |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_KNIGHTS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_KNIGHTS |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_BISHOPS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_BISHOPS |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_ROOKS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_ROOKS |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_QUEEN) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_QUEEN |= destinationPiece;
        }
        else if ((sourcePiece & board.WHITE_KING) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.WHITE_KING |= destinationPiece;
        }

        else if ((sourcePiece & board.BLACK_PAWNS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_PAWNS |= destinationPiece;
        }
        else if ((sourcePiece & board.BLACK_KNIGHTS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_KNIGHTS |= destinationPiece;
        }
        else if ((sourcePiece & board.BLACK_BISHOPS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_BISHOPS |= destinationPiece;
        }
        else if ((sourcePiece & board.BLACK_ROOKS) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_ROOKS |= destinationPiece;
        }
        else if ((sourcePiece & board.BLACK_QUEEN) != 0){
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_QUEEN |= destinationPiece;
        }
        else if ((sourcePiece & board.BLACK_KING) != 0) {
            MoveMakingUtilities.removePieces(board, sourcePiece, destinationPiece);
            board.BLACK_KING |= destinationPiece;
        }
        else {
            System.out.println(Art.boardArt(board));
            System.out.println(move);
            throw new RuntimeException("false move");
        }
    }

}
