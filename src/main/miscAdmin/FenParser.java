package main.miscAdmin;

import main.chess.Art;
import main.chess.BitManipulations;
import main.chess.Chessboard;
import main.moveMaking.StackMoveData;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FenParser {

    FenParser(String fenString){
        Chessboard chessboard = parseFenString(fenString);
        String s = Art.boardArt(chessboard);
//        System.out.println(s);

    }

    public static Chessboard makeBoardBasedOnFEN(String fen){
        Chessboard board = parseFenString(fen);
        board.setWhiteTurn(isItWhitesTurn(fen));

        boolean[] castlingRights = castlingRights(fen);
        board.whiteCanCastleK = castlingRights[0];
        board.whiteCanCastleQ = castlingRights[1];
        board.blackCanCastleK = castlingRights[2];
        board.blackCanCastleQ = castlingRights[3];

        if (isEPFlagSet(fen)){
            epFlag(fen, board);
        }

        return board;
    }


    private static boolean totalMoves(String fen){
        //todo
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }


    private static boolean fiftyMoves(String fen){
        //todo
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }


    
    
    

    private static void epFlag(String fen, Chessboard board){
        String boardPattern = " (.) (\\w+|-) (\\w|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        switch (epFlags) {
            case "a": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 1, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "b": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 2, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "c": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 3, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "d": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 4, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "e": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 5, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "f": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 6, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "g": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 7, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            case "h": {
                StackMoveData previousMoveForEPPurposes = new StackMoveData
                        (null, board, 50, 8, StackMoveData.SpecialMove.ENPASSANTVICTIM);
                board.moveStack.push(previousMoveForEPPurposes);
                break;
            }
            default:
                System.out.println("problem with EP flag");
        }

    }

    private static boolean isEPFlagSet(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }



    private static boolean[] castlingRights(String fen){
        boolean[] castlingRights = {
                false, false, false, false,
        };

        String boardPattern = " (.) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String castleString = "";

        if (m.find()){
            castleString = m.group(2);
        }
        if (castleString.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        if (castleString.equals("-")){
            return castlingRights;
        }

        if (castleString.contains("K")){
            castlingRights[0] = true;
        }
        if (castleString.contains("Q")){
            castlingRights[1] = true;
        }
        if (castleString.contains("k")){
            castlingRights[2] = true;
        }
        if (castleString.contains("q")){
            castlingRights[3] = true;
        }

        return castlingRights;
    }



    private static boolean isItWhitesTurn(String fen){
        String boardPattern = " (.)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String player = "";

        if (m.find()){
            player = m.group(1);
        }
        if (player.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return player.equals("w");
    }




    private static Chessboard parseFenString (String fen){
        Chessboard board = BlankBoard.blankBoard();
        String boardRepresentation = boardRep(fen);
        int length = boardRepresentation.length();
        int index = -1;
        int square = 63;
        while (true){
            index++;
            if (index >= length){
                break;
            }
            if (square < 0){
                break;
            }
            String entry = Character.toString(boardRepresentation.charAt(index));
            if (entry.equals("/")){
                continue;
            }
            try {
                int i = Integer.parseInt(entry);
                square -= (i);
                continue;
            }
            catch (NumberFormatException e){
            }
            long pieceFromFen = BitManipulations.newPieceOnSquare(square);
            square--;
            switch (entry) {
                case "P":
                    board.WHITE_PAWNS |= pieceFromFen;
                    break;
                case "N":
                    board.WHITE_KNIGHTS |= pieceFromFen;
                    break;
                case "B":
                    board.WHITE_BISHOPS |= pieceFromFen;
                    break;
                case "R":
                    board.WHITE_ROOKS |= pieceFromFen;
                    break;
                case "Q":
                    board.WHITE_QUEEN |= pieceFromFen;
                    break;
                case "K":
                    board.WHITE_KING |= pieceFromFen;
                    break;

                case "p":
                    board.BLACK_PAWNS |= pieceFromFen;
                    break;
                case "n":
                    board.BLACK_KNIGHTS |= pieceFromFen;
                    break;
                case "b":
                    board.BLACK_BISHOPS |= pieceFromFen;
                    break;
                case "r":
                    board.BLACK_ROOKS |= pieceFromFen;
                    break;
                case "q":
                    board.BLACK_QUEEN |= pieceFromFen;
                    break;
                case "k":
                    board.BLACK_KING |= pieceFromFen;
                    break;
                default:
                    System.out.println("I don't know this piece");
            }
        }
        return board;
    }



    private static String boardRep(String fen){
        String boardPattern = "^[\\w*/]*";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String boardRepresentation = "";

        if (m.find()){
            boardRepresentation = m.group();
        }
        if (boardRepresentation.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return boardRepresentation;
    }

}
