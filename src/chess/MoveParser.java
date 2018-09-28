package chess;

import javafx.util.Pair;

public class MoveParser {

    final private static int
            SOURCE_MASK = 0x0000fc00,
            DESTINATION_MASK = 0x000003f0,
            QUEEN_PROMOTION_MASK = 0x0000000c,
            ROOK_PROMOTION_MASK = 0x00000008,
            BISHOP_PROMOTION_MASK = 0x00000004,
            KNIGHT_PROMOTION_MASK = 0x00000000,
            PROMOTION_MASK = 0x00000003,
            ENPASSANT_MASK = 0x00000002,
            CASTLING_MASK = 0x00000001;

    static Pair parseMove(Move move){
        String s = Integer.toBinaryString(move.move);
        var moveSourceAndDest = new Pair(
                Integer.toBinaryString((move.move & SOURCE_MASK) >> 10),
                Integer.toBinaryString((move.move & DESTINATION_MASK) >> 5)
        );
        return moveSourceAndDest;
    }
}
