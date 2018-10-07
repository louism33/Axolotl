package moveMaking;

import chess.Move;
import javafx.util.Pair;

public class MoveParser {

    final public static int
            sourceOffset = 6,
            SOURCE_MASK = 0x00000fc0,
            DESTINATION_MASK = 0x0000003f,

            SPECIAL_MOVE_MASK = 0x00003000,
            CASTLING_MASK = 0x00001000,
            ENPASSANT_MASK = 0x00002000,
            PROMOTION_MASK = 0x00003000,

            WHICH_PROMOTION = 0x0000c000,
            KNIGHT_PROMOTION_MASK = 0x00000000,
            BISHOP_PROMOTION_MASK = 0x00004000,
            ROOK_PROMOTION_MASK = 0x00008000,
            QUEEN_PROMOTION_MASK = 0x0000c000;


//del
    static Pair parseMove(Move move){
        String s = Integer.toBinaryString(move.move);
        var moveSourceAndDest = new Pair(
                Integer.toBinaryString((move.move & SOURCE_MASK) >> sourceOffset),
                Integer.toBinaryString((move.move & DESTINATION_MASK))
        );
        return moveSourceAndDest;
    }

    //del
    static Pair parseMoveInteger(Move move){
        String s = Integer.toBinaryString(move.move);
        var moveSourceAndDest = new Pair(
                ((move.move & SOURCE_MASK) >> sourceOffset),
                ((move.move & DESTINATION_MASK))
        );
        return moveSourceAndDest;
    }

    static boolean isSpecialMove (Move move){
        return (move.move & SPECIAL_MOVE_MASK) != 0;
    }

    static boolean isCastlingMove (Move move){
        return (move.move & SPECIAL_MOVE_MASK) == CASTLING_MASK;
    }

    static boolean isEnPassantMove (Move move){
        return (move.move & SPECIAL_MOVE_MASK) == ENPASSANT_MASK;
    }

    static boolean isPromotionMove (Move move){
        return (move.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK;
    }


    static boolean isPromotionToKnight (Move move){
        if (!((move.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move.move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK;
    }

    static boolean isPromotionToBishop(Move move){
        if (!((move.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move.move & WHICH_PROMOTION) == BISHOP_PROMOTION_MASK;
    }

    static boolean isPromotionToRook (Move move){
        if (!((move.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move.move & WHICH_PROMOTION) == ROOK_PROMOTION_MASK;
    }

    static boolean isPromotionToQueen (Move move){
        if (!((move.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move.move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK;
    }

}
