package javacode.chessprogram.chess;

import javacode.chessprogram.miscAdmin.MovePrettifier;
import javacode.graphicsandui.Art;

import java.util.Objects;

public class Move {

    public int move;
    private int source;
    public int destinationIndex;
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

    public Move(int source, int destinationIndex){
        makeSourceAndDest(source, destinationIndex);
    }

    public Move(int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen){

        makeSourceAndDest(source, destinationIndex);

        if (castling) this.move |= CASTLING_MASK;
        if (enPassant) this.move |= ENPASSANT_MASK;
        if (promotion) {
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }
    }

    public Move(int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen, int hack){

        makeSourceAndDest(source, destinationIndex);

        if (castling) this.move |= CASTLING_MASK;
        if (enPassant) this.move |= ENPASSANT_MASK;
        if (promotion) {
            this.move |= PROMOTION_MASK;
            if (promoteToKnight) this.move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) this.move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) this.move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) this.move |= QUEEN_PROMOTION_MASK;
        }
    }


    private void makeSourceAndDest(int s, int d){
        if (s >= 64 | s < 0 | d >= 64 | d < 0){
            throw new RuntimeException("Move: False Move " + s+" "+ d);
        }
        this.source = (s << sourceOffset) & SOURCE_MASK;
        this.destinationIndex = d & DESTINATION_MASK;
        this.move |= this.source;
        this.move |= this.destinationIndex;
    }

    @Override
    public String toString() {
        return MovePrettifier.prettyMove(this);
    }

    public String toStringSimple() {
        return (getSourceAsPieceIndex() +" " + destinationIndex);
    }
    
    public String toComplicatedString(){
        return Art.makeMoveToString(this.move);
    }

    public int getSourceAsPieceIndex() {
        return source >>> sourceOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move1 = (Move) o;
        return move == move1.move &&
                source == move1.source &&
                destinationIndex == move1.destinationIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(move, source, destinationIndex);
    }
}
