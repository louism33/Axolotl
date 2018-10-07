package chess;

import javafx.util.Pair;

public class Move {

    //could change all this to just be an int (short)


    public int move;
    public int source, destination;
    final private static int
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

    public Move(int source, int destination){
        makeSourceAndDest(source, destination);
    }

    public Move(int source, int destination, boolean castling, boolean enPassant, boolean promotion,
         boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen){

        makeSourceAndDest(source, destination);

        if (castling) this.move |= CASTLING_MASK;
        if (enPassant) this.move |= ENPASSANT_MASK;
        if (promotion) {
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }
    }


    private void makeSourceAndDest(int s, int d){
        if (s >= 64 | s < 0 | d >= 64 | d < 0){
            throw new RuntimeException("False Move " + s+" "+ d);
        }
        this.source = (s << sourceOffset) & SOURCE_MASK;
        this.destination = d & DESTINATION_MASK;
        this.move |= this.source;
        this.move |= this.destination;
    }


    Pair parseMove(){
        String s = Integer.toBinaryString(move);
        var moveSourceAndDest = new Pair(
                Integer.toBinaryString((move & SOURCE_MASK) >> sourceOffset),
                Integer.toBinaryString((move & DESTINATION_MASK))
        );
        return moveSourceAndDest;
    }

    @Override
    public String toString() {
        return (getSourceAsPiece() +" " +destination);

    }


    public String toComplicatedString(){
        return Art.makeMoveToString(this.move);
    }

    public int getSourceAsPiece() {
        return source >>> sourceOffset;
    }
}
