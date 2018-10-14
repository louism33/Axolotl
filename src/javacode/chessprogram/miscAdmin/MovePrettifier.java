package javacode.chessprogram.miscAdmin;

import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveParser;

public class MovePrettifier {

    public static String prettyMove(Move move){
        int sourceAsPiece = move.getSourceAsPiece();
        String file = getFile(sourceAsPiece);
        String rank = getRank(sourceAsPiece);
        int destination = move.destination;
        String destinationFile = getFile(destination);
        String destinationRank = getRank(destination);
        String m = ""+file+ rank+destinationFile+ destinationRank;

        if (MoveParser.isCastlingMove(move)){
            m += "-C";
        }
        else if (MoveParser.isEnPassantMove(move)){
            m += "-Ep";
        }

        else if (MoveParser.isPromotionMove(move)){
            m += "-P";

            if (MoveParser.isPromotionToKnight(move)){
                m += "-N";
            }
            else if (MoveParser.isPromotionToBishop(move)){
                m += "-B";
            }
            else if (MoveParser.isPromotionToRook(move)){
                m += "-R";
            }
            else if (MoveParser.isPromotionToQueen(move)){
                m += "-Q";
            }
        }
        return m;
    }
    
    private static String getRank(int square){
        return (square / 8 + 1) + "";
    }


    private static String getFile(int square){
        int i = square % 8;
        String file = "";
        switch (i){
            case 0: 
                file = "h";
                break;
            case 1:
                file = "g";
                break;
            case 2:
                file = "f";
                break;
            case 3:
                file = "e";
                break;
            case 4:
                file = "d";
                break;
            case 5:
                file = "c";
                break;
            case 6:
                file = "b";
                break;
            case 7:
                file = "a";
                break;
        }
        return file;
    }
}
