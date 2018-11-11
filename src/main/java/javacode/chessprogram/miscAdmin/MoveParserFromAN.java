package javacode.chessprogram.miscAdmin;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.graphicsandui.Art;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveParserFromAN {

    public static int destinationIndex(Chessboard board, String algebraicNotation){
        return rankAndFile(board, algebraicNotation);
    }
    
    private static Move makeMoveBasedOnAlgNotation(Chessboard board, String algebraicNotation){
        System.out.println(algebraicNotation);
        System.out.println();

        long whichPieceCouldBeMoving = whichPieceIsMoving(board, algebraicNotation);
        int x = rankAndFile(board, algebraicNotation);
        
        long destinationSquare = BitManipulations.newPieceOnSquare(x);

        Piece piece = extractRealPieceFromLong(board, whichPieceCouldBeMoving, destinationSquare);

//        findOriginalPiece(piece, destinationSquare);

        Art.printLong(whichPieceCouldBeMoving);
        Art.printLong(destinationSquare);

        return null;
    }
    enum Piece {
        WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN, WHITE_KING,
        BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN, BLACK_KING
    }

//    private static long findOriginalPiece(Piece piece, long whichPieceCouldBeMoving, long destinationSquare){
//        switch (piece) {
//            case WHITE_PAWN: {
//                return boa;
//            }
//            case WHITE_KNIGHT: {
//                return 6;
//            }
//            case "c": {
//                return 5;
//            }
//            case "d": {
//                return 4;
//            }
//            case "e": {
//                return 3;
//            }
//            case "f": {
//                return 2;
//            }
//            case "g": {
//                return 1;
//            }
//            case "h": {
//                return 0;
//            }
//            default:
//                System.out.println("problem with Piece identifier");
//                return 0;
//        }
//    }
    
    
    
    
    private static Piece extractRealPieceFromLong(Chessboard board, long whichPieceCouldBeMoving, long destinationSquare){
        if ((whichPieceCouldBeMoving & board.WHITE_PAWNS) != 0){
            return Piece.WHITE_PAWN;
        }
        else if ((whichPieceCouldBeMoving & board.WHITE_KNIGHTS) != 0){
            return Piece.WHITE_KNIGHT;
        }
        else if ((whichPieceCouldBeMoving & board.WHITE_BISHOPS) != 0){
            return Piece.WHITE_BISHOP;
        }
        else if ((whichPieceCouldBeMoving & board.WHITE_ROOKS) != 0){
            return Piece.WHITE_ROOK;
        }
        else if ((whichPieceCouldBeMoving & board.WHITE_QUEEN) != 0){
            return Piece.WHITE_QUEEN;
        }
        else if ((whichPieceCouldBeMoving & board.WHITE_KING) != 0){
            return Piece.WHITE_KING;
        }

        else if ((whichPieceCouldBeMoving & board.BLACK_PAWNS) != 0){
            return Piece.BLACK_PAWN;
        }
        else if ((whichPieceCouldBeMoving & board.BLACK_KNIGHTS) != 0){
            return Piece.BLACK_KNIGHT;
        }
        else if ((whichPieceCouldBeMoving & board.BLACK_BISHOPS) != 0){
            return Piece.BLACK_BISHOP;
        }
        else if ((whichPieceCouldBeMoving & board.BLACK_ROOKS) != 0){
            return Piece.BLACK_ROOK;
        }
        else if ((whichPieceCouldBeMoving & board.BLACK_QUEEN) != 0){
            return Piece.BLACK_QUEEN;
        }
        else if ((whichPieceCouldBeMoving & board.BLACK_KING) != 0){
            return Piece.BLACK_KING;
        }
        else {
            throw new RuntimeException("Could not retrieve Piece");
        }
    }

    private static long whichPieceIsMoving(Chessboard board, String algebraicNotation){
        String boardPattern = "[p|n|b|r|q|k|P|N|B|R|Q|K]?";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(algebraicNotation);

        String pieceFromAN = "";

        if (m.find()){
            pieceFromAN = m.group();
        }
        if (pieceFromAN.length() == 0){
            throw new RuntimeException("Could not parse Piece");
        }

        System.out.println(pieceFromAN);
        
        switch (pieceFromAN) {
            case "p": {
                return board.BLACK_PAWNS;
            }
            case "n": {
                return board.BLACK_KNIGHTS;
            }
            case "b": {
                return board.BLACK_BISHOPS;
            }
            case "r": {
                return board.BLACK_ROOKS;
            }
            case "q": {
                return board.BLACK_QUEEN;
            }
            case "k": {
                return board.BLACK_KING;
            }

            case "P": {
                return board.WHITE_PAWNS;
            }
            case "N": {
                return board.WHITE_KNIGHTS;
            }
            case "B": {
                return board.WHITE_BISHOPS;
            }
            case "R": {
                return board.WHITE_ROOKS;
            }
            case "Q": {
                return board.WHITE_QUEEN;
            }
            case "K": {
                return board.WHITE_KING;
            }
            default:
                System.out.println("problem with Piece identifier in which piece in moving()");
                return 0;
        }
    }
    
    private static int rankAndFile(Chessboard board, String algebraicNotation){
        int f = whichDestinationFile(board, algebraicNotation);
        int r = whichDestinationRank(board, algebraicNotation);
        return (r-1) * 8 + f;
    }
    private static int whichDestinationRank(Chessboard board, String algebraicNotation){
        String boardPattern = ".?x?.x?(\\d)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(algebraicNotation);

        String pieceFromAN = "";

        if (m.find()){
            pieceFromAN = m.group(1);
        }
        if (pieceFromAN.length() == 0){
            throw new RuntimeException("Could not parse file");
        }
        
        return Integer.parseInt(pieceFromAN);
    }
    
    private static int whichDestinationFile(Chessboard board, String algebraicNotation){
        String boardPattern = "([a-h])(\\d)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(algebraicNotation);

        String pieceFromAN = "";

        if (m.find()){
            pieceFromAN = m.group(1);
        }
        if (pieceFromAN.length() == 0){
            throw new RuntimeException("Could not parse file");
        }

        switch (pieceFromAN) {
            case "a": {
                return 7;
            }
            case "b": {
                return 6;
            }
            case "c": {
                return 5;
            }
            case "d": {
                return 4;
            }
            case "e": {
                return 3;
            }
            case "f": {
                return 2;
            }
            case "g": {
                return 1;
            }
            case "h": {
                return 0;
            }
            default:
                System.out.println("problem with Getting destinationIndex file");
                return 0;
        }
    }
    
    
}
