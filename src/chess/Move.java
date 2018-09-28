package chess;

import javafx.util.Pair;

public class Move {
    int move;
    int source, destination;
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

    Move(int source, int destination){
        if (source >= 64 | source < 0 | destination >= 64 | destination < 0){
            throw new RuntimeException("False Move " + source+" "+ destination);
        }
        this.source = source << 10;
        this.destination = destination << 5;

        this.move |= (this.source & SOURCE_MASK);  //just in case there is a problem
        this.move |= (this.destination & DESTINATION_MASK);
    }

    Move(int source, int destination,
         boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen,
         boolean promotion, boolean enPassant, boolean castling){

        if (source >= 64 | source < 0 | destination >= 64 | destination < 0){
            throw new RuntimeException("False Move " + source+" "+ destination);
        }

        this.source = source << 10;
        this.destination = destination << 5;

        this.move |= (this.source & SOURCE_MASK);  //just in case there is a problem
        this.move |= (this.destination & DESTINATION_MASK);

        if (promotion) {
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }

        if (enPassant) this.move |= ENPASSANT_MASK;
        if (castling) this.move |= CASTLING_MASK;

    }

    Pair parseMove(){
        String s = Integer.toBinaryString(move);
        var moveSourceAndDest = new Pair(
                Integer.toBinaryString((move & SOURCE_MASK) >> 10),
                Integer.toBinaryString((move & DESTINATION_MASK) >> 5)
        );
        return moveSourceAndDest;
    }
    
    @Override
    public String toString() {
        return Art.makeMoveToString(this.move);
    }
}
